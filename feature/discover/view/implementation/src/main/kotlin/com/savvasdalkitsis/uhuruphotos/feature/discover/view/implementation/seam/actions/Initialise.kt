/*
Copyright 2023 Savvas Dalkitsis

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.savvasdalkitsis.uhuruphotos.feature.discover.view.implementation.seam.actions

import com.savvasdalkitsis.uhuruphotos.feature.db.domain.api.people.People
import com.savvasdalkitsis.uhuruphotos.feature.discover.view.implementation.seam.DiscoverActionsContext
import com.savvasdalkitsis.uhuruphotos.feature.discover.view.implementation.seam.DiscoverMutation
import com.savvasdalkitsis.uhuruphotos.feature.discover.view.implementation.seam.DiscoverMutation.ChangeMapViewport
import com.savvasdalkitsis.uhuruphotos.feature.discover.view.implementation.seam.DiscoverMutation.DisableSearch
import com.savvasdalkitsis.uhuruphotos.feature.discover.view.implementation.seam.DiscoverMutation.EnableSearch
import com.savvasdalkitsis.uhuruphotos.feature.discover.view.implementation.seam.DiscoverMutation.HideSuggestions
import com.savvasdalkitsis.uhuruphotos.feature.discover.view.implementation.seam.DiscoverMutation.ShowPeople
import com.savvasdalkitsis.uhuruphotos.feature.discover.view.implementation.seam.DiscoverMutation.ShowPeopleUpsell
import com.savvasdalkitsis.uhuruphotos.feature.discover.view.implementation.seam.DiscoverMutation.ShowSearchSuggestions
import com.savvasdalkitsis.uhuruphotos.feature.discover.view.implementation.ui.state.AutoAlbumSearchSuggestionState
import com.savvasdalkitsis.uhuruphotos.feature.discover.view.implementation.ui.state.DiscoverState
import com.savvasdalkitsis.uhuruphotos.feature.discover.view.implementation.ui.state.PersonSearchSuggestionState
import com.savvasdalkitsis.uhuruphotos.feature.discover.view.implementation.ui.state.RecentSearchSuggestionState
import com.savvasdalkitsis.uhuruphotos.feature.discover.view.implementation.ui.state.ServerSearchSuggestionState
import com.savvasdalkitsis.uhuruphotos.feature.discover.view.implementation.ui.state.UserAlbumSearchSuggestionState
import com.savvasdalkitsis.uhuruphotos.feature.people.view.api.ui.state.toPerson
import com.savvasdalkitsis.uhuruphotos.feature.welcome.domain.api.usecase.flow
import com.savvasdalkitsis.uhuruphotos.foundation.coroutines.api.onErrors
import com.savvasdalkitsis.uhuruphotos.foundation.coroutines.api.onErrorsIgnore
import com.savvasdalkitsis.uhuruphotos.foundation.seam.api.andThen
import com.savvasdalkitsis.uhuruphotos.foundation.strings.api.R
import com.savvasdalkitsis.uhuruphotos.foundation.ui.api.ui.search.SearchSuggestion
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalCoroutinesApi::class)
data object Initialise : DiscoverAction() {
    override fun DiscoverActionsContext.handle(
        state: DiscoverState
    ) = merge(
        showLibrary(),
        showHeatMap(),
        showFeedDisplay(),
        showServerSearchSuggestion(),
        showPeopleSuggestion(),
        showSearchSuggestions()
    )

    context(DiscoverActionsContext)
    private fun showHeatMap() = heatMapUseCase.observeViewport()
        .map(::ChangeMapViewport)

    context(DiscoverActionsContext)
    private fun showSearchSuggestions() = welcomeUseCase.flow(
        withRemoteAccess = merge(
            flowOf(EnableSearch),
            combine(
                searchSuggestions(),
                queryFilter,
            ) { suggestions, query ->
                suggestions.filterQuery(query)
            }.map(::ShowSearchSuggestions)
        ),
        withoutRemoteAccess = flowOf(DisableSearch)
    )

    context(DiscoverActionsContext)
    private fun searchSuggestions() = combine(
        searchUseCase.getRecentTextSearches()
            .toSuggestion(::RecentSearchSuggestionState),
        peopleUseCase.observePeopleByPhotoCount()
            .onErrorsIgnore()
            .toPeople()
            .toSuggestion(::PersonSearchSuggestionState),
        userAlbumsUseCase.observeUserAlbums()
            .toSuggestion(::UserAlbumSearchSuggestionState),
        autoAlbumsUseCase.observeAutoAlbums()
            .toSuggestion(::AutoAlbumSearchSuggestionState),
        searchUseCase.getSearchSuggestions()
            .toSuggestion(::ServerSearchSuggestionState),
    ) { suggestions -> suggestions.flatMap { it } }

    private fun <T> Flow<List<T>>.toSuggestion(mapper: (T) -> SearchSuggestion) = map {
        it.map(mapper)
    }

    context(DiscoverActionsContext)
    private fun showPeopleSuggestion() = welcomeUseCase.flow(
        withRemoteAccess = peopleUseCase.observePeopleByPhotoCount()
            .onErrors {
                toaster.show(R.string.error_refreshing_people)
            }
            .toPeople()
            .map { it.subList(0, max(0, min(10, it.size - 1))) }
            .map { ShowPeople(it) andThen ShowPeopleUpsell(false)},
        withoutRemoteAccess = observeShowPeopleUpsell()
            .map(::ShowPeopleUpsell),
    )

    context(DiscoverActionsContext)
    private fun showServerSearchSuggestion() = welcomeUseCase.flow(
        withRemoteAccess = settingsUIUseCase.observeSearchSuggestionsEnabledMode().flatMapLatest { enabled ->
            if (enabled)
                searchUseCase.getRandomSearchSuggestion()
                    .map(DiscoverMutation::ShowSearchSuggestion)
            else
                flowOf(HideSuggestions)
        },
        withoutRemoteAccess = emptyFlow()
    )

    context(DiscoverActionsContext)
    private fun showLibrary() = settingsUIUseCase.observeShowLibrary()
        .map(DiscoverMutation::ShowLibrary)

    context(DiscoverActionsContext)
    private fun showFeedDisplay() = feedUseCase
        .observeFeedDisplay()
        .distinctUntilChanged()
        .map(DiscoverMutation::ChangeFeedDisplay)

    context(DiscoverActionsContext)
    private fun Flow<List<People>>.toPeople() = mapNotNull { people ->
        serverUseCase.getServerUrl()?.let { serverUrl ->
            people.map {
                it.toPerson { url -> "$serverUrl$url" }
            }
        }
    }

    private fun List<SearchSuggestion>.filterQuery(query: String): List<SearchSuggestion> = when {
        query.isBlank() -> this
        else -> filter { it.filterable.contains(query, ignoreCase = true) }
    }
}

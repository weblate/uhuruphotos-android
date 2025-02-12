/*
Copyright 2022 Savvas Dalkitsis

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
package com.savvasdalkitsis.uhuruphotos.search.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import com.savvasdalkitsis.uhuruphotos.home.module.HomeModule.HomeNavigationTargetFeed
import com.savvasdalkitsis.uhuruphotos.navigation.ControllersProvider
import com.savvasdalkitsis.uhuruphotos.navigation.NavigationTarget
import com.savvasdalkitsis.uhuruphotos.navigation.navigationTarget
import com.savvasdalkitsis.uhuruphotos.search.mvflow.SearchAction
import com.savvasdalkitsis.uhuruphotos.search.mvflow.SearchEffect
import com.savvasdalkitsis.uhuruphotos.search.view.SearchPage
import com.savvasdalkitsis.uhuruphotos.search.view.state.SearchState
import com.savvasdalkitsis.uhuruphotos.search.viewmodel.SearchEffectsHandler
import com.savvasdalkitsis.uhuruphotos.search.viewmodel.SearchViewModel
import com.savvasdalkitsis.uhuruphotos.settings.usecase.SettingsUseCase
import javax.inject.Inject

class SearchNavigationTarget @Inject constructor(
    private val effectsHandler: SearchEffectsHandler,
    private val controllersProvider: ControllersProvider,
    private val settingsUseCase: SettingsUseCase,
    @HomeNavigationTargetFeed private val feedNavigationName: String,
) : NavigationTarget {

    override suspend fun NavGraphBuilder.create() {
        navigationTarget<SearchState, SearchEffect, SearchAction, SearchViewModel>(
            name = name,
            effects = effectsHandler,
            themeMode = settingsUseCase.observeThemeModeState(),
            initializer = { _, actions -> actions(SearchAction.Initialise) },
            createModel = { hiltViewModel() }
        ) { state, actions ->
            SearchPage(state, actions, feedNavigationName, name, controllersProvider)
        }
    }

    companion object {
        const val name = "search"
    }
}
/*
Copyright 2024 Savvas Dalkitsis

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
package com.savvasdalkitsis.uhuruphotos.feature.portfolio.view.implementation.seam.actions

import com.savvasdalkitsis.uhuruphotos.feature.local.view.api.navigation.LocalAlbumNavigationRoute
import com.savvasdalkitsis.uhuruphotos.feature.media.local.domain.api.model.LocalMediaFolder
import com.savvasdalkitsis.uhuruphotos.feature.portfolio.view.implementation.seam.PortfolioActionsContext
import com.savvasdalkitsis.uhuruphotos.feature.portfolio.view.implementation.ui.state.PortfolioState
import com.savvasdalkitsis.uhuruphotos.foundation.seam.api.Mutation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class NavigateToFolder(
    val folder: LocalMediaFolder,
) : PortfolioAction() {
    context(PortfolioActionsContext)
    override fun handle(state: PortfolioState): Flow<Mutation<PortfolioState>> = flow {
        navigator.navigateTo(LocalAlbumNavigationRoute(folder.id))
    }
}
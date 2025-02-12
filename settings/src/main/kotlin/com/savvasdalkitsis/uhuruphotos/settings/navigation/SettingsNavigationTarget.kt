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
package com.savvasdalkitsis.uhuruphotos.settings.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import com.savvasdalkitsis.uhuruphotos.navigation.NavigationTarget
import com.savvasdalkitsis.uhuruphotos.navigation.navigationTarget
import com.savvasdalkitsis.uhuruphotos.settings.usecase.SettingsUseCase
import com.savvasdalkitsis.uhuruphotos.settings.view.Settings
import com.savvasdalkitsis.uhuruphotos.settings.view.state.SettingsState
import com.savvasdalkitsis.uhuruphotos.settings.viewmodel.SettingsAction
import com.savvasdalkitsis.uhuruphotos.settings.viewmodel.SettingsAction.LoadSettings
import com.savvasdalkitsis.uhuruphotos.settings.viewmodel.SettingsEffect
import com.savvasdalkitsis.uhuruphotos.settings.viewmodel.SettingsEffectHandler
import com.savvasdalkitsis.uhuruphotos.settings.viewmodel.SettingsViewModel
import javax.inject.Inject

class SettingsNavigationTarget @Inject constructor(
    private val settingsEffectHandler: SettingsEffectHandler,
    private val settingsUseCase: SettingsUseCase,
) : NavigationTarget {

    override suspend fun NavGraphBuilder.create() {
        navigationTarget<SettingsState, SettingsEffect, SettingsAction, SettingsViewModel>(
            name = name,
            effects = settingsEffectHandler,
            themeMode = settingsUseCase.observeThemeModeState(),
            initializer = { _, actions -> actions(LoadSettings) },
            createModel = { hiltViewModel() }
        ) { state, actions ->
            Settings(state, actions)
        }
    }

    companion object {
        const val name = "settings"
    }
}
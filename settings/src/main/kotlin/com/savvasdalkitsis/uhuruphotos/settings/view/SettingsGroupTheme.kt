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
package com.savvasdalkitsis.uhuruphotos.settings.view

import androidx.compose.runtime.Composable
import com.savvasdalkitsis.uhuruphotos.settings.view.state.SettingsState
import com.savvasdalkitsis.uhuruphotos.settings.viewmodel.SettingsAction
import com.savvasdalkitsis.uhuruphotos.settings.viewmodel.SettingsAction.ChangeThemeMode
import com.savvasdalkitsis.uhuruphotos.ui.theme.ThemeMode
import com.savvasdalkitsis.uhuruphotos.ui.theme.ThemeMode.*

@Composable
fun SettingsGroupTheme(
    state: SettingsState,
    action: (SettingsAction) -> Unit,
) {
    SettingsGroup(title = "Theme") {
        SettingsTextDropDownButtonRow(
            content = {
                ThemeRow(state.themeMode)
            },
            buttonText = "Change",
            action = action,
        ) {
            @Composable
            fun item(themeMode: ThemeMode) {
                Item({ ThemeRow(themeMode) }, ChangeThemeMode(themeMode))
            }
            item(FOLLOW_SYSTEM)
            item(DARK_MODE)
            item(LIGHT_MODE)
        }
    }
}
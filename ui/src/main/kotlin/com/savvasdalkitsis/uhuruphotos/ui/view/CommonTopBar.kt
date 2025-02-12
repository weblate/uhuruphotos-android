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
package com.savvasdalkitsis.uhuruphotos.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.savvasdalkitsis.uhuruphotos.ui.insets.insetsEnd
import com.savvasdalkitsis.uhuruphotos.ui.insets.insetsStart
import com.savvasdalkitsis.uhuruphotos.ui.insets.insetsTop

@Composable
fun CommonTopBar(
    topBarDisplayed: Boolean,
    toolbarColor: @Composable () -> Color,
    title: @Composable () -> Unit,
    navigationIcon: @Composable (() -> Unit)? = null,
    actionBarContent: @Composable (RowScope.() -> Unit) = {}
) {
    AnimatedVisibility(
        visible = topBarDisplayed,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Row(
            modifier = Modifier
                .background(toolbarColor())
        ) {
            Spacer(modifier = Modifier.width(insetsStart()))
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Spacer(modifier = Modifier.height(insetsTop()))
                TopAppBar(
                    title = title,
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp,
                    navigationIcon = navigationIcon,
                    actions = {
                        actionBarContent()
                    }
                )
            }
            Spacer(
                modifier = Modifier
                    .width(insetsEnd())
                    .background(toolbarColor())
            )
        }
    }
}
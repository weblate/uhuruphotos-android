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
package com.savvasdalkitsis.uhuruphotos.feature.lightbox.view.implementation.seam.actions

import com.savvasdalkitsis.uhuruphotos.feature.lightbox.view.implementation.seam.LightboxActionsContext
import com.savvasdalkitsis.uhuruphotos.feature.lightbox.view.implementation.seam.LightboxMutation
import com.savvasdalkitsis.uhuruphotos.feature.lightbox.view.implementation.ui.state.LightboxState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class ChangedToPage(val page: Int) : LightboxAction() {

    override fun LightboxActionsContext.handle(
        state: LightboxState
    ): Flow<LightboxMutation> = flow {
        if (state.media.isNotEmpty()) {
            val page = page.coerceAtMost(state.media.size - 1)
            currentMediaId.emit(state.media[page].id)
        }
    }
}
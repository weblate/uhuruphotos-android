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
package com.savvasdalkitsis.uhuruphotos.feature.feed.view.implementation.seam.actions

import com.savvasdalkitsis.uhuruphotos.feature.feed.view.implementation.seam.FeedActionsContext
import com.savvasdalkitsis.uhuruphotos.feature.feed.view.implementation.seam.FeedMutation.StartRefreshing
import com.savvasdalkitsis.uhuruphotos.feature.feed.view.implementation.seam.FeedMutation.StopRefreshing
import com.savvasdalkitsis.uhuruphotos.feature.feed.view.implementation.ui.state.FeedState
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

data class AddSelectedCelsToAlbum(val albumId: Int) : FeedAction() {
    context(FeedActionsContext) override fun handle(
        state: FeedState
    ) = flow {
        emit(StartRefreshing)
        val media = selectionList.ids.firstOrNull()?.mapNotNull { it.findRemote }
        if (media != null) {
            userAlbumUseCase.addMediaToAlbum(albumId, media)
        }
        selectionList.clear()
        emit(StopRefreshing)
    }
}

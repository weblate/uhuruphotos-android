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
package com.savvasdalkitsis.uhuruphotos.userbadge.usecase

import androidx.work.WorkInfo
import androidx.work.WorkInfo.State.*
import com.savvasdalkitsis.uhuruphotos.albums.worker.AlbumDownloadWorker
import com.savvasdalkitsis.uhuruphotos.auth.usecase.ServerUseCase
import com.savvasdalkitsis.uhuruphotos.photos.usecase.PhotosUseCase
import com.savvasdalkitsis.uhuruphotos.user.usecase.UserUseCase
import com.savvasdalkitsis.uhuruphotos.userbadge.api.UserBadgeUseCase
import com.savvasdalkitsis.uhuruphotos.userbadge.api.view.state.SyncState.*
import com.savvasdalkitsis.uhuruphotos.userbadge.api.view.state.UserInformationState
import com.savvasdalkitsis.uhuruphotos.worker.usecase.WorkerStatusUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class UserBadgeUseCase @Inject constructor(
    private val userUseCase: UserUseCase,
    private val workerStatusUseCase: WorkerStatusUseCase,
    private val photosUseCase: PhotosUseCase,
    private val serverUseCase: ServerUseCase,
) : UserBadgeUseCase {

    override fun getUserBadgeState(): Flow<UserInformationState> = combine(
        userUseCase.getUser(),
        workerStatusUseCase.monitorUniqueJobStatus(AlbumDownloadWorker.WORK_NAME),
        serverUseCase.observeServerUrl(),
    ) { user, status, serverUrl ->
        UserInformationState(
            avatarUrl = with(photosUseCase) { user.avatar?.toAbsoluteUrl() },
            syncState = when (status) {
                BLOCKED, FAILED -> BAD
                CANCELLED, ENQUEUED, SUCCEEDED -> GOOD
                RUNNING -> IN_PROGRESS
            },
            initials = user.firstName.initial() + user.lastName.initial(),
            userFullName = "${user.firstName} ${user.lastName}",
            serverUrl = serverUrl,
        )
    }

    private fun String?.initial() =
        orEmpty().firstOrNull()?.toString()?.uppercase() ?: ""
}

private operator fun WorkInfo.State.plus(other: WorkInfo.State): WorkInfo.State {
    if (this == RUNNING || other == RUNNING) return RUNNING
    if (this == FAILED || other == FAILED) return FAILED
    if (this == SUCCEEDED && other == SUCCEEDED) return SUCCEEDED
    return this
}

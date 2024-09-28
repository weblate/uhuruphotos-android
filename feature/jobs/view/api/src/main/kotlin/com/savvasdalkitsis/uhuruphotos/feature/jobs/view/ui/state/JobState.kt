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
package com.savvasdalkitsis.uhuruphotos.feature.jobs.view.ui.state

import androidx.compose.runtime.Immutable
import com.savvasdalkitsis.uhuruphotos.feature.jobs.domain.api.model.JobModel
import com.savvasdalkitsis.uhuruphotos.feature.jobs.domain.api.model.JobStatusModel
import com.savvasdalkitsis.uhuruphotos.foundation.strings.api.R.string
import com.savvasdalkitsis.uhuruphotos.foundation.ui.api.ui.text.state.Title

@Immutable
data class JobState(
    val title: Title,
    val job: JobModel,
    val status: JobStatusModel,
)

val Map<JobModel, JobStatusModel>.toJobState get() = map { (job, status) ->
    JobState(
        title = Title.Resource(when (job) {
            JobModel.FULL_FEED_SYNC -> string.full_feed_sync
            JobModel.PRECACHE_THUMBNAILS -> string.precache_thumbnails
            JobModel.LOCAL_MEDIA_SYNC -> string.scan_local_media
            JobModel.FEED_DETAILS_SYNC -> string.full_feed_details_sync
        }),
        job = job,
        status = status,
    )
}
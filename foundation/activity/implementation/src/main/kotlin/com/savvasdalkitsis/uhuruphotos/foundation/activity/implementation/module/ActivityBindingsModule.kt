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
package com.savvasdalkitsis.uhuruphotos.foundation.activity.implementation.module

import com.savvasdalkitsis.uhuruphotos.foundation.activity.implementation.holder.CurrentActivityHolder
import com.savvasdalkitsis.uhuruphotos.foundation.activity.implementation.request.ActivityRequestLauncher
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityBindingsModule {

    @Binds
    abstract fun activityRequestLauncher(launcher: ActivityRequestLauncher):
            com.savvasdalkitsis.uhuruphotos.foundation.activity.api.request.ActivityRequestLauncher

    @Binds
    abstract fun currentActivityHolder(holder: CurrentActivityHolder):
            com.savvasdalkitsis.uhuruphotos.foundation.activity.api.holder.CurrentActivityHolder
}
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
package com.savvasdalkitsis.uhuruphotos.log

import android.content.Context
import com.michaelflisar.lumberjack.FileLoggingSetup
import com.michaelflisar.lumberjack.L
import com.michaelflisar.lumberjack.sendFeedback
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FeedbackUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loggingSetup: FileLoggingSetup,
) {

    fun sendFeedback() {
        L.sendFeedback(context, loggingSetup.getLatestLogFiles(), EMAIL)
    }

    fun clearLogs() {
        loggingSetup.clearLogFiles()
    }

    companion object {
        const val EMAIL = "kurosavvas@gmail.com"
    }
}
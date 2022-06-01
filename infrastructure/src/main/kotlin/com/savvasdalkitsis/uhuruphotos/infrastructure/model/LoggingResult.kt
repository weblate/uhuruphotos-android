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
package com.savvasdalkitsis.uhuruphotos.infrastructure.model

import com.savvasdalkitsis.uhuruphotos.api.log.log

inline fun <T, R> T.runCatchingWithLog(block: T.() -> R): Result<R> = try {
    Result.success(block())
} catch (e: Throwable) {
    log(e)
    Result.failure(e)
}

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
package com.savvasdalkitsis.uhuruphotos.people.viewmodel

import com.savvasdalkitsis.uhuruphotos.people.view.state.PeopleState
import com.savvasdalkitsis.uhuruphotos.people.api.view.state.toPerson
import com.savvasdalkitsis.uhuruphotos.photos.usecase.PhotosUseCase
import com.savvasdalkitsis.uhuruphotos.viewmodel.Reducer
import javax.inject.Inject

class PeopleReducer @Inject constructor(
    private val photosUseCase: PhotosUseCase,
) : Reducer<PeopleState, PeopleMutation> {
    override suspend fun invoke(
        state: PeopleState,
        mutation: PeopleMutation,
    ): PeopleState =
        when (mutation) {
            is PeopleMutation.DisplayPeople -> with (photosUseCase) {
                state.copy(people = mutation.people.map { it.toPerson { url -> url.toAbsoluteUrl() } })
            }
            is PeopleMutation.SetSortOrder -> state.copy(sortOrder = mutation.sortOrder)
        }
}
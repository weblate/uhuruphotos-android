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
package com.savvasdalkitsis.uhuruphotos.heatmap.viewmodel

import com.google.android.gms.maps.model.LatLng
import com.savvasdalkitsis.uhuruphotos.photos.api.model.Photo

sealed class HeatMapMutation {
    data class UpdateAllPhotos(val photos: List<Photo>) : HeatMapMutation()
    data class UpdateDisplay(
        val photosToDisplay: List<Photo>,
        val pointsToDisplay: List<LatLng>,
    ) : HeatMapMutation()
    data class ShowLoading(val loading: Boolean) : HeatMapMutation()
}

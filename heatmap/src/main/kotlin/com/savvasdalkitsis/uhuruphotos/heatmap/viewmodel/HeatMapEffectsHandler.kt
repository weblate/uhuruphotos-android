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

import com.savvasdalkitsis.uhuruphotos.heatmap.viewmodel.HeatMapEffect.*
import com.savvasdalkitsis.uhuruphotos.navigation.ControllersProvider
import com.savvasdalkitsis.uhuruphotos.photos.navigation.PhotoNavigationTarget
import com.savvasdalkitsis.uhuruphotos.strings.R
import com.savvasdalkitsis.uhuruphotos.toaster.Toaster
import com.savvasdalkitsis.uhuruphotos.viewmodel.EffectHandler
import javax.inject.Inject

class HeatMapEffectsHandler @Inject constructor(
    private val controllersProvider: ControllersProvider,
    private val toaster: Toaster,
): EffectHandler<HeatMapEffect> {

    override suspend fun invoke(
        effect: HeatMapEffect
    ) {
        when (effect) {
            ErrorLoadingPhotoDetails -> toaster.show(R.string.error_loading_photo_details)
            NavigateBack -> controllersProvider.navController!!.popBackStack()
            is NavigateToPhoto -> controllersProvider.navController!!.navigate(with(effect) {
                PhotoNavigationTarget.name(photo.id, center, scale, photo.isVideo)
            })
        }
    }

}

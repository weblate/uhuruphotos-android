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
package com.savvasdalkitsis.uhuruphotos.implementation.heatmap.seam

import android.annotation.SuppressLint
import android.location.LocationManager
import android.location.LocationManager.NETWORK_PROVIDER
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import com.google.accompanist.permissions.isGranted
import com.savvasdalkitsis.uhuruphotos.api.albums.usecase.AlbumsUseCase
import com.savvasdalkitsis.uhuruphotos.api.coroutines.onErrors
import com.savvasdalkitsis.uhuruphotos.api.coroutines.safelyOnStart
import com.savvasdalkitsis.uhuruphotos.api.map.model.LatLon
import com.savvasdalkitsis.uhuruphotos.api.photos.model.Photo
import com.savvasdalkitsis.uhuruphotos.api.photos.model.latLng
import com.savvasdalkitsis.uhuruphotos.api.photos.usecase.PhotosUseCase
import com.savvasdalkitsis.uhuruphotos.api.seam.ActionHandler
import com.savvasdalkitsis.uhuruphotos.implementation.heatmap.seam.HeatMapAction.*
import com.savvasdalkitsis.uhuruphotos.implementation.heatmap.seam.HeatMapEffect.ErrorLoadingPhotoDetails
import com.savvasdalkitsis.uhuruphotos.implementation.heatmap.seam.HeatMapEffect.NavigateBack
import com.savvasdalkitsis.uhuruphotos.implementation.heatmap.seam.HeatMapEffect.NavigateToPhoto
import com.savvasdalkitsis.uhuruphotos.implementation.heatmap.seam.HeatMapMutation.ShowLoading
import com.savvasdalkitsis.uhuruphotos.implementation.heatmap.seam.HeatMapMutation.UpdateAllPhotos
import com.savvasdalkitsis.uhuruphotos.implementation.heatmap.seam.HeatMapMutation.UpdateVisibleMapContent
import com.savvasdalkitsis.uhuruphotos.implementation.heatmap.view.state.HeatMapState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import java.lang.Runnable
import javax.inject.Inject

class HeatMapActionHandler @Inject constructor(
    private val albumsUseCase: AlbumsUseCase,
    private val photosUseCase: PhotosUseCase,
    private val locationManager: LocationManager,
): ActionHandler<HeatMapState, HeatMapEffect, HeatMapAction, HeatMapMutation> {

    @Volatile
    private var boundsChecker: suspend (LatLon) -> Boolean = { true }
    private val detailsDownloading = MutableStateFlow(false)
    private var updateVisibleMapContentJob: Deferred<UpdateVisibleMapContent>? = null

    @SuppressLint("MissingPermission")
    override fun handleAction(
        state: HeatMapState,
        action: HeatMapAction,
        effect: suspend (HeatMapEffect) -> Unit
    ): Flow<HeatMapMutation> = when (action) {
        Load -> merge(
            photosUseCase.observeAllPhotoDetails()
                .map { photos ->
                    photos
                        .filter { it.latLng != null }
                        .map {
                            Photo(
                                id = it.imageHash,
                                thumbnailUrl = with(photosUseCase) {
                                    it.imageHash.toThumbnailUrlFromId()
                                },
                                fullResUrl = with(photosUseCase) {
                                    it.imageHash.toFullSizeUrlFromId(it.video ?: false)
                                },
                                latLng = it.latLng?.let { latLng ->
                                    latLng.lat to latLng.lon
                                },
                                isVideo = it.video ?: false,
                            )
                        }
                }
                .safelyOnStart {
                    albumsUseCase.observeAlbums().collect { albums ->
                        detailsDownloading.emit(true)
                        albums
                            .flatMap { it.photos }
                            .map { photo ->
                                CoroutineScope(currentCoroutineContext() + Dispatchers.IO).async {
                                    photosUseCase.refreshDetailsNowIfMissing(photo.id)
                                }
                            }
                            .awaitAll()
                        detailsDownloading.emit(false)
                    }
                }
                .debounce(500)
                .distinctUntilChanged()
                .onErrors { effect(ErrorLoadingPhotoDetails) }
                .flatMapLatest { photos ->
                    flowOf(UpdateAllPhotos(photos), updateDisplay(photos))
                },
            detailsDownloading
                .map(::ShowLoading)
        )
        BackPressed -> flow {
            effect(NavigateBack)
        }
        is CameraViewPortChanged -> flow {
            updateVisibleMapContentJob?.cancel()
            boundsChecker = action.boundsChecker
            updateVisibleMapContentJob = CoroutineScope(currentCoroutineContext()).async { updateDisplay(state.allPhotos) }
            updateVisibleMapContentJob?.await()?.let {
                emit(it)
            }
        }
        is SelectedPhoto -> flow {
            effect(with(action) {
                NavigateToPhoto(photo, center, scale)
            })
        }
        is MyLocationPressed -> flow {
            if (action.locationPermissionState.status.isGranted) {
                var location: LatLon? = null
                getCurrentLocation(locationManager, NETWORK_PROVIDER, null, Runnable::run) {
                    location = LatLon(it.latitude, it.longitude)
                }
                location?.let {
                    action.mapViewState.centerToLocation(it)
                }
            } else {
                action.locationPermissionState.launchPermissionRequest()
            }
        }
    }

    private suspend fun updateDisplay(allPhotos: List<Photo>): UpdateVisibleMapContent {
        val photosToDisplay = allPhotos
            .filter { photo ->
                val latLon = photo.latLng.toLatLon()
                latLon != null && boundsChecker(latLon)
            }
        val pointsToDisplay = photosToDisplay
            .mapNotNull { it.latLng.toLatLon() }
        return UpdateVisibleMapContent(photosToDisplay, pointsToDisplay)
    }

}

private fun Pair<Double, Double>?.toLatLon() =
    this?.let { (lat, lon) -> LatLon(lat, lon) }

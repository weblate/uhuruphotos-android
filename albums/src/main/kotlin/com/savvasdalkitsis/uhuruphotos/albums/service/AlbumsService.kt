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
package com.savvasdalkitsis.uhuruphotos.albums.service

import com.savvasdalkitsis.uhuruphotos.albums.service.model.AlbumById
import com.savvasdalkitsis.uhuruphotos.albums.service.model.AlbumsByDate
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AlbumsService {

    @GET("/api/albums/date/list/")
    suspend fun getAlbumsByDate(): AlbumsByDate

    @GET("/api/albums/date/{id}/")
    suspend fun getAlbum(@Path("id") id: String): AlbumById

    @GET("/api/albums/date/list/")
    suspend fun getAlbumsForPerson(@Query("person") personId: Int): AlbumsByDate

    @GET("/api/albums/date/{id}/")
    suspend fun getAlbumForPerson(@Path("id") id: String, @Query("person") personId: Int): AlbumById
}
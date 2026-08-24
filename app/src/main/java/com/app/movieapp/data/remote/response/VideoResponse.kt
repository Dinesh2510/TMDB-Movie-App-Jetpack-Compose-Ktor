/*
 * Copyright (c) 2026 Dinesh2510
 * File : VideoResponse.kt
 * Project : TMDB Ktor
 * Module : TMDB_Ktor.app.main
 * Created on : 2026-08-24 21:57
 * Last modified: 2026-08-24 21:57
 *
 * Author : Dinesh
 * GitHub : https://github.com/Dinesh2510
 * YouTube : https://www.youtube.com/@pixeldesigndeveloper
 * Website : https://pixeldev.in
 *
 * Copyright (c) 2026 Dinesh. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 */

package com.app.movieapp.data.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class VideoResponse(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("results")
    val results: List<VideoResult>? = emptyList()
) : Parcelable

@Serializable
@Parcelize
data class VideoResult(
    @SerialName("id")
    val id: String = "",
    @SerialName("iso_639_1")
    val iso6391: String = "",
    @SerialName("iso_3166_1")
    val iso31661: String = "",
    @SerialName("key")
    val key: String = "", // YouTube Video Key
    @SerialName("name")
    val name: String = "",
    @SerialName("site")
    val site: String = "", // "YouTube"
    @SerialName("size")
    val size: Int = 0,
    @SerialName("type")
    val type: String = "", // "Trailer", "Teaser", "Featurette"
    @SerialName("official")
    val official: Boolean = false,
    @SerialName("published_at")
    val publishedAt: String = ""
) : Parcelable {
    
    // Helper property to get full YouTube video URL
    val youtubeUrl: String
        get() = "https://www.youtube.com/watch?v=$key"

    // Helper property to get YouTube thumbnail image
    val youtubeThumbnailUrl: String
        get() = "https://img.youtube.com/vi/$key/hqdefault.jpg"
}
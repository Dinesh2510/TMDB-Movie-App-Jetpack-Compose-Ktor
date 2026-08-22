/*
 * Copyright (c) 2026 Dinesh2510
 * File : Search.kt
 * Project : TMDB Ktor
 * Module : TMDB_Ktor.app.main
 * Created on : 2026-08-22 15:27
 * Last modified: 2026-08-22 15:09
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

package com.app.movieapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Search(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("adult")
    val adult: Boolean? = false,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("genre_ids")
    val genreIds: List<Int>? = emptyList(),
    @SerialName("genres")
    val genres: List<Genre>? = emptyList(),
    @SerialName("imdb_id")
    val imdbId: String? = null,
    @SerialName("media_type")
    val mediaType: String? = null,
    @SerialName("origin_country")
    val originCountry: List<String>? = emptyList(),
    @SerialName("original_language")
    val originalLanguage: String? = null,
    @SerialName("original_name")
    val originalName: String? = null,
    @SerialName("original_title")
    val originalTitle: String? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("popularity")
    val popularity: Double? = 0.0,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("first_air_date")
    val firstAirDate: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("video")
    val video: Boolean? = false,
    @SerialName("runtime")
    val runtime: Int? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = 0.0,
    @SerialName("vote_count")
    val voteCount: Int? = 0
) : Parcelable {

    /**
     * Helpers to handle Movie vs TV Show naming seamlessly
     */
    val displayTitle: String
        get() = title ?: name ?: originalTitle ?: originalName ?: ""

    val displayReleaseDate: String
        get() = releaseDate ?: firstAirDate ?: ""
}
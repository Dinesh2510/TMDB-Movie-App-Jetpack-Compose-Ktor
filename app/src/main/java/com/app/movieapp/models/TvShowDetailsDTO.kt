/*
 * Copyright (c) 2026 Dinesh2510
 * File : TvShowDetailsDTO.kt
 * Project : TMDB Ktor
 * Module : TMDB_Ktor.app.main
 * Created on : 2026-08-27 22:34
 * Last modified: 2026-08-27 22:34
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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowDetailsDTO(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("overview") val overview: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    @SerialName("first_air_date") val firstAirDate: String? = null,
    @SerialName("number_of_seasons") val numberOfSeasons: Int = 0,
    @SerialName("number_of_episodes") val numberOfEpisodes: Int = 0,
    @SerialName("episode_run_time") val episodeRunTime: List<Int>? = emptyList(),
    @SerialName("seasons") val seasons: List<SeasonSummaryDTO> = emptyList(),
    @SerialName("genres") val genres: List<GenreDTO> = emptyList(),
    @SerialName("spoken_languages") val spokenLanguages: List<SpokenLanguageDTO> = emptyList(),
    @SerialName("credits") val credits: TvCreditsDTO? = null,
    @SerialName("similar") val similarTvShows: TvShowResponse? = null
) {
    // Helper property to safely extract the cast list for your UI
    val castList: List<Cast>
        get() = credits?.cast ?: emptyList()
}

@Serializable
data class TvCreditsDTO(
    @SerialName("cast") val cast: List<Cast> = emptyList()
)

@Serializable
data class GenreDTO(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String
)

@Serializable
data class SpokenLanguageDTO(
    @SerialName("english_name") val englishName: String? = null,
    @SerialName("name") val name: String
)

@Serializable
data class TvShowResponse(
    @SerialName("page") val page: Int = 1,
    @SerialName("results") val results: List<TvShowDetailsDTO> = emptyList()
)

@Serializable
data class SeasonSummaryDTO(
    @SerialName("id") val id: Int,
    @SerialName("season_number") val seasonNumber: Int,
    @SerialName("name") val name: String,
    @SerialName("episode_count") val episodeCount: Int,
    @SerialName("poster_path") val posterPath: String? = null
)

@Serializable
data class SeasonDetailsDTO(
    @SerialName("id") val id: Int? = null,
    @SerialName("season_number") val seasonNumber: Int,
    @SerialName("name") val name: String? = null,
    @SerialName("episodes") val episodes: List<EpisodeDTO> = emptyList()
)

@Serializable
data class EpisodeDTO(
    @SerialName("id") val id: Int,
    @SerialName("episode_number") val episodeNumber: Int,
    @SerialName("name") val name: String,
    @SerialName("overview") val overview: String? = null,
    @SerialName("still_path") val stillPath: String? = null,
    @SerialName("air_date") val airDate: String? = null,
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    @SerialName("runtime") val runtime: Int? = null
)
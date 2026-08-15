package com.app.movieapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class TvShow(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("adult")
    val adult: Boolean = false,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("genre_ids")
    val genreIds: List<Int>? = emptyList(),
    @SerialName("genres")
    val genres: List<Genre>? = emptyList(),
    @SerialName("imdb_id")
    val imdbId: String? = null,
    @SerialName("production_companies")
    val productionCompanies: List<ProductionCompany>? = emptyList(),
    @SerialName("original_language")
    val originalLanguage: String = "",
    @SerialName("original_name")
    val originalName: String = "",
    @SerialName("original_title")
    val originalTitle: String = "",
    @SerialName("overview")
    val overview: String = "",
    @SerialName("popularity")
    val popularity: Double = 0.0,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("first_air_date")
    val firstAirDate: String? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("runtime")
    val runtime: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("video")
    val video: Boolean = false,
    @SerialName("vote_average")
    val voteAverage: Double = 0.0,
    @SerialName("vote_count")
    val voteCount: Int = 0
) : Parcelable {

    val displayTitle: String
        get() = name ?: title ?: originalName ?: originalTitle ?: ""

    val displayAirDate: String
        get() = firstAirDate ?: releaseDate ?: ""
}
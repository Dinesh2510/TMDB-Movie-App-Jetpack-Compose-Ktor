package com.app.movieapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Movies(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("adult")
    val adult: Boolean = false,
    @SerialName("softcore")
    val softcore: Boolean = false,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("genre_ids")
    val genreIds: List<Int>? = emptyList(),
    @SerialName("genres")
    val genres: List<Genre>? = emptyList(),
    @SerialName("media_type")
    val mediaType: String? = null,
    @SerialName("imdb_id")
    val imdbId: String? = null,
    @SerialName("original_language")
    val originalLanguage: String = "",
    @SerialName("original_name")
    val originalName: String? = null,
    @SerialName("origin_country")
    val originCountry: List<String>? = emptyList(),
    @SerialName("overview")
    val overview: String = "",
    @SerialName("popularity")
    val popularity: Double = 0.0,
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("first_air_date")
    val firstAirDate: String? = null,
    @SerialName("runtime")
    val runtime: Int? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("video")
    val video: Boolean = false,
    @SerialName("vote_average")
    val voteAverage: Double = 0.0,
    @SerialName("vote_count")
    val voteCount: Int = 0
) : Parcelable {

    /**
     * Helper getters to seamlessly support both Movies (title, release_date)
     * and TV Shows (name, first_air_date) without Gson alternates
     */
    val displayTitle: String
        get() = title ?: name ?: originalName ?: ""

    val displayReleaseDate: String
        get() = releaseDate ?: firstAirDate ?: ""
}
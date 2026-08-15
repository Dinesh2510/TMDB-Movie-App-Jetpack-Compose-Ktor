package com.app.movieapp.data.remote.response

import com.app.movieapp.models.Genre
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
    @SerialName("genres")
    val genres: List<Genre> = emptyList()
)
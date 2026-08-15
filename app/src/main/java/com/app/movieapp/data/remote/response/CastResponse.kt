package com.app.movieapp.data.remote.response

import com.app.movieapp.models.Cast
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CastResponse(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("cast")
    val castResult: List<Cast> = emptyList()
)
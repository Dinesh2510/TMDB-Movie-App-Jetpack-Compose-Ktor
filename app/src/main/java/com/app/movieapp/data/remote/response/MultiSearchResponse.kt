package com.app.movieapp.data.remote.response

import com.app.movieapp.models.Search
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MultiSearchResponse(
    @SerialName("page")
    val page: Int = 1,
    @SerialName("results")
    val results: List<Search> = emptyList(),
    @SerialName("total_pages")
    val totalPages: Int = 0,
    @SerialName("total_results")
    val totalResults: Int = 0
)
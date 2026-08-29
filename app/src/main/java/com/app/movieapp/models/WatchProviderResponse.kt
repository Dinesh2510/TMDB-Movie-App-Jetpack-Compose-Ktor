/*
 * Copyright (c) 2026 Dinesh2510
 * File : WatchProviderResponse.kt
 * Project : TMDB Ktor
 * Module : TMDB_Ktor.app.main
 * Created on : 2026-08-29 12:50
 * Last modified: 2026-08-29 12:50
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
data class WatchProviderResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("results")
    val results: Map<String, CountryWatchProvidersDTO> = emptyMap()
)

@Serializable
data class CountryWatchProvidersDTO(
    @SerialName("link")
    val link: String? = null,
    @SerialName("flatrate")
    val flatrate: List<ProviderItemDTO> = emptyList(),
    @SerialName("rent")
    val rent: List<ProviderItemDTO> = emptyList(),
    @SerialName("buy")
    val buy: List<ProviderItemDTO> = emptyList()
)

@Serializable
data class ProviderItemDTO(
    @SerialName("logo_path")
    val logoPath: String? = null,
    @SerialName("provider_id")
    val providerId: Int,
    @SerialName("provider_name")
    val providerName: String,
    @SerialName("display_priority")
    val displayPriority: Int = 0
)
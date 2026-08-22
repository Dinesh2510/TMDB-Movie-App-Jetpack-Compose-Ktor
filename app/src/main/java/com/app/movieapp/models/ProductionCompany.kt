/*
 * Copyright (c) 2026 Dinesh2510
 * File : ProductionCompany.kt
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
data class ProductionCompany(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("logo_path")
    val logoPath: String? = null,
    @SerialName("name")
    val name: String = "",
    @SerialName("origin_country")
    val originCountry: String = ""
) : Parcelable
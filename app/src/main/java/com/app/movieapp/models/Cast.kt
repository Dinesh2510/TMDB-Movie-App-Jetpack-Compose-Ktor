/*
 * Copyright (c) 2026 Dinesh2510
 * File : Cast.kt
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
data class Cast(
    @SerialName("adult")
    val adult: Boolean = false,
    @SerialName("cast_id")
    val castId: Int = 0,
    @SerialName("character")
    val character: String? = null,
    @SerialName("credit_id")
    val creditId: String = "",
    @SerialName("gender")
    val gender: Int = 0,
    @SerialName("id")
    val id: Int = 0,
    @SerialName("known_for_department")
    val department: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("order")
    val order: Int = 0,
    @SerialName("original_name")
    val originalName: String = "",
    @SerialName("popularity")
    val popularity: Double = 0.0,
    @SerialName("profile_path")
    val profilePath: String? = null
) : Parcelable
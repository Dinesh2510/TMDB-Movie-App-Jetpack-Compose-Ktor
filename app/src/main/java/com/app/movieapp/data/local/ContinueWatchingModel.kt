/*
 * Copyright (c) 2026 Dinesh2510
 * File : ContinueWatchingModel.kt
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

package com.app.movieapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "continue_watching_table")
data class ContinueWatchingModel(
    @PrimaryKey val mediaId: Int,
    val title: String,
    val posterPath: String?,
    val backdropPath: String?,
    val currentPositionMs: Long,
    val totalDurationMs: Long,
    val releaseDate: String = "",
    val rating: Double = 0.0,
    val lastWatchedTimestamp: Long = System.currentTimeMillis(),
    val mediaType: String = "movie", // <--- Ensure mediaType is defined here

) {
    val progressFraction: Float
        get() = if (totalDurationMs > 0) {
            (currentPositionMs.toFloat() / totalDurationMs.toFloat()).coerceIn(0f, 1f)
        } else 0f

    val remainingMinutes: Long
        get() = ((totalDurationMs - currentPositionMs) / (1000 * 60)).coerceAtLeast(1)
}
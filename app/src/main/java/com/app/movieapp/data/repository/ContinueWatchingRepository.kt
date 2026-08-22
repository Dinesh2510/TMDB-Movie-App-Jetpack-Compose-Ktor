/*
 * Copyright (c) 2026 Dinesh2510
 * File : ContinueWatchingRepository.kt
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

package com.app.movieapp.data.repository

import com.app.movieapp.data.local.ContinueWatchingModel
import com.app.movieapp.data.local.MovieDao
import kotlinx.coroutines.flow.Flow

class ContinueWatchingRepository(private val movieDao: MovieDao) {

    val continueWatchingList: Flow<List<ContinueWatchingModel>> = 
        movieDao.getAllContinueWatching()

    suspend fun saveProgress(
        mediaId: Int,
        title: String,
        posterPath: String?,
        backdropPath: String?,
        currentPositionMs: Long,
        totalDurationMs: Long,
        releaseDate: String = "",
        rating: Double = 0.0
    ) {
        // If watched more than 95%, auto-remove from continue watching
        if (totalDurationMs > 0 && (currentPositionMs.toFloat() / totalDurationMs.toFloat()) >= 0.95f) {
            movieDao.removeContinueWatching(mediaId)
            return
        }

        val item = ContinueWatchingModel(
            mediaId = mediaId,
            title = title,
            posterPath = posterPath,
            backdropPath = backdropPath,
            currentPositionMs = currentPositionMs,
            totalDurationMs = totalDurationMs,
            releaseDate = releaseDate,
            rating = rating,
            lastWatchedTimestamp = System.currentTimeMillis()
        )
        movieDao.upsertContinueWatching(item)
    }

    suspend fun getProgress(mediaId: Int): ContinueWatchingModel? {
        return movieDao.getContinueWatchingById(mediaId)
    }

    suspend fun removeFromContinueWatching(mediaId: Int) {
        movieDao.removeContinueWatching(mediaId)
    }
}
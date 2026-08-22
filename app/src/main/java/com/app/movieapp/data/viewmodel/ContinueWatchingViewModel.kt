/*
 * Copyright (c) 2026 Dinesh2510
 * File : ContinueWatchingViewModel.kt
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

package com.app.movieapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.movieapp.data.local.ContinueWatchingModel
import com.app.movieapp.data.repository.ContinueWatchingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ContinueWatchingViewModel(
    private val repository: ContinueWatchingRepository
) : ViewModel() {

    val continueWatchingList: StateFlow<List<ContinueWatchingModel>> =
        repository.continueWatchingList.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun saveProgress(
        mediaId: Int,
        title: String,
        posterPath: String?,
        backdropPath: String?,
        currentPositionMs: Long,
        totalDurationMs: Long,
        releaseDate: String = "",
        rating: Double = 0.0
    ) {
        viewModelScope.launch {
            repository.saveProgress(
                mediaId = mediaId,
                title = title,
                posterPath = posterPath,
                backdropPath = backdropPath,
                currentPositionMs = currentPositionMs,
                totalDurationMs = totalDurationMs,
                releaseDate = releaseDate,
                rating = rating
            )
        }
    }

    fun removeProgress(mediaId: Int) {
        viewModelScope.launch {
            repository.removeFromContinueWatching(mediaId)
        }
    }
}
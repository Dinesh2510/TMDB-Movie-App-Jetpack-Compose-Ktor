/*
 * Copyright (c) 2026 Dinesh2510
 * File : TvDetailsViewModel.kt
 * Project : TMDB Ktor
 * Module : TMDB_Ktor.app.main
 * Created on : 2026-08-27 22:34
 * Last modified: 2026-08-27 22:34
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

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.movieapp.R
import com.app.movieapp.data.remote.response.VideoResponse
import com.app.movieapp.data.repository.MovieDetailsRepository
import com.app.movieapp.data.repository.TvShowRepository
import com.app.movieapp.models.EpisodeDTO
import com.app.movieapp.models.SeasonSummaryDTO
import com.app.movieapp.models.TvShowDetailsDTO
import com.app.movieapp.utlis.MovieState
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed interface TvDetailsUIState {
    data object Loading : TvDetailsUIState
    data class Success(
        val tvDetails: TvShowDetailsDTO,
        val availableSeasons: List<SeasonSummaryDTO>
    ) : TvDetailsUIState
    data class Error(@StringRes val messageRes: Int) : TvDetailsUIState
}

class TvDetailsViewModel(
    private val tvRepository: TvShowRepository,
    private val movieDetailsRepository: MovieDetailsRepository
) : ViewModel() {

    private val TAG = "TvDetailsViewModel"

    private val _uiState = MutableStateFlow<TvDetailsUIState>(TvDetailsUIState.Loading)
    val uiState: StateFlow<TvDetailsUIState> = _uiState.asStateFlow()

    private val _selectedSeasonNumber = MutableStateFlow(1)
    val selectedSeasonNumber: StateFlow<Int> = _selectedSeasonNumber.asStateFlow()

    private val _episodesState = MutableStateFlow<MovieState<List<EpisodeDTO>>>(MovieState.Loading)
    val episodesState: StateFlow<MovieState<List<EpisodeDTO>>> = _episodesState.asStateFlow()

    // Video State Flow for Trailer Dialog
    private val _videoResponses = MutableStateFlow<MovieState<VideoResponse>>(MovieState.Loading)
    val videoResponses: StateFlow<MovieState<VideoResponse>> = _videoResponses.asStateFlow()

    private val _similarTvState = MutableStateFlow<MovieState<List<TvShowDetailsDTO>>>(MovieState.Loading)
    val similarTvState: StateFlow<MovieState<List<TvShowDetailsDTO>>> = _similarTvState.asStateFlow()

    private var currentTvId: Int = -1

    fun loadTvShowDetails(tvId: Int) {
        currentTvId = tvId
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = TvDetailsUIState.Loading
            try {
                val details = tvRepository.getTvShowDetailsRepo(tvId).first()

                val filteredSeasons = details.seasons.filter { it.seasonNumber > 0 }
                val initialSeason = filteredSeasons.firstOrNull()?.seasonNumber ?: 1

                _uiState.value = TvDetailsUIState.Success(
                    tvDetails = details,
                    availableSeasons = filteredSeasons
                )

                // Auto-load Season 1 episodes
                selectSeason(initialSeason)

                // 👈 CALL IT HERE: Automatically fetch similar TV shows in the background
                fetchSimilarTvShows(tvId)

            } catch (e: CancellationException) {
                throw e
            } catch (e: ResponseException) {
                if (e.response.status.value == 404) {
                    _uiState.value = TvDetailsUIState.Error(R.string.error_content_not_found)
                } else {
                    _uiState.value = TvDetailsUIState.Error(R.string.error_network_communication)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching TV details: ${e.message}", e)
                _uiState.value = TvDetailsUIState.Error(R.string.error_unknown)
            }
        }
    }
    // Make sure _similarTvState is defined as TvShowDetailsDTO

    fun fetchSimilarTvShows(tvId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _similarTvState.value = MovieState.Loading
            try {
                val response = tvRepository.getSimilarTvShowsRepo(tvId).first()
                _similarTvState.value = MovieState.Success(response.results)
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching similar TV shows: ${e.message}")
                _similarTvState.value = MovieState.Error("Failed to load similar shows.")
            }
        }
    }
    fun selectSeason(seasonNumber: Int) {
        if (currentTvId == -1) return
        _selectedSeasonNumber.value = seasonNumber

        viewModelScope.launch(Dispatchers.IO) {
            _episodesState.value = MovieState.Loading
            try {
                val seasonDetails = tvRepository.getSeasonDetailsRepo(currentTvId, seasonNumber).first()
                _episodesState.value = MovieState.Success(seasonDetails.episodes)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading season $seasonNumber episodes: ${e.message}")
                _episodesState.value = MovieState.Error("Failed to load season episodes.")
            }
        }
    }

    // Fetches TV Show trailers/videos when "WATCH TRAILER" is clicked
    fun fetchTvShowVideos(tvId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _videoResponses.value = MovieState.Loading
            try {
                val videos = movieDetailsRepository.getTvShowVideosRepo(tvId).first()
                _videoResponses.value = MovieState.Success(videos)
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching TV videos: ${e.message}")
                _videoResponses.value = MovieState.Error("Failed to fetch trailers.")
            }
        }
    }
}
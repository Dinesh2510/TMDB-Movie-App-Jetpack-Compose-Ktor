/*
 * Copyright (c) 2026 Dinesh2510
 * File : TvShowRepositoryImpl.kt
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

package com.app.movieapp.data.repository

import com.app.movieapp.data.remote.ApiService
import com.app.movieapp.data.remote.response.MovieResponse
import com.app.movieapp.models.SeasonDetailsDTO
import com.app.movieapp.models.TvShowDetailsDTO
import com.app.movieapp.models.TvShowResponse
import com.app.movieapp.models.WatchProviderResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TvShowRepositoryImpl(
    private val apiService: ApiService
) : TvShowRepository {

    override fun getTvShowDetailsRepo(tvId: Int): Flow<TvShowDetailsDTO> = flow {
        val response = apiService.getTvShowDetails(tvId)
        emit(response)
    }.flowOn(Dispatchers.IO)

    override fun getSeasonDetailsRepo(tvId: Int, seasonNumber: Int): Flow<SeasonDetailsDTO> = flow {
        val response = apiService.getSeasonDetails(tvId, seasonNumber)
        emit(response)
    }.flowOn(Dispatchers.IO)


    override fun getSimilarTvShowsRepo(tvId: Int): Flow<TvShowResponse> = flow {
        val response = apiService.getSimilarTvShows(filmId = tvId)
        emit(response)
    }
   override fun getTvWatchProvidersRepo(tvId: Int): Flow<WatchProviderResponse> = flow {
        val response = apiService.getTvWatchProviders(tvId)
        emit(response)
    }
}
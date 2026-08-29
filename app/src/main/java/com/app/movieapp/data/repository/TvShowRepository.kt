/*
 * Copyright (c) 2026 Dinesh2510
 * File : TvShowRepository.kt
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
import com.app.movieapp.models.SeasonDetailsDTO
import com.app.movieapp.models.TvShowDetailsDTO
import com.app.movieapp.models.TvShowResponse
import com.app.movieapp.models.WatchProviderResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
interface TvShowRepository {
    fun getTvShowDetailsRepo(tvId: Int): Flow<TvShowDetailsDTO>
    fun getSeasonDetailsRepo(tvId: Int, seasonNumber: Int): Flow<SeasonDetailsDTO>
    fun getSimilarTvShowsRepo(tvId: Int): Flow<TvShowResponse>
    fun getTvWatchProvidersRepo(tvId: Int): Flow<WatchProviderResponse>
}

/*
 * Copyright (c) 2026 Dinesh2510
 * File : MovieDetailsRepository.kt
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

import com.app.movieapp.data.remote.ApiService
import com.app.movieapp.data.remote.response.CastResponse
import com.app.movieapp.data.remote.response.MovieDetailsDTO
import com.app.movieapp.data.remote.response.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieDetailsRepository(private val apiService: ApiService) {

    fun getMoviesDetailsRepo(movieId: String): Flow<MovieDetailsDTO> = flow {
        val response = apiService.getMoviesDetails(movieId.toInt())
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun getCastMoviesRepo(movieId: String): Flow<CastResponse> = flow {
        val response = apiService.getMovieCast(movieId.toInt())
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun getSimilarMoviesRepo(movieId: String): Flow<MovieResponse> = flow {
        val response = apiService.getSimilarMovies(movieId.toInt())
        emit(response)
    }.flowOn(Dispatchers.IO)
}
/*
 * Copyright (c) 2026 Dinesh2510
 * File : HomeRepository.kt
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

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.app.movieapp.data.paging.MovieGenrePagingSource
import com.app.movieapp.data.paging.MoviePagingSource
import com.app.movieapp.data.remote.ApiService
import com.app.movieapp.data.remote.response.GenreResponse
import com.app.movieapp.data.remote.response.MovieResponse
import com.app.movieapp.models.Movies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class HomeRepository(private val apiService: ApiService) {

    fun getNowPlayingMoviesRepo(): Flow<MovieResponse> = flow {
        val response = apiService.getNowPlayingMovies(page = 2)
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun getPopularMoviesRepo(): Flow<MovieResponse> = flow {
        val response = apiService.getPopularMovies(page = 1)
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun getAllMoviesPagination(tags: String): Pager<Int, Movies> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(apiService, tags) }
        )
    }

    fun getGenresWiseMovieRepo(genreId: Int): Pager<Int, Movies> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MovieGenrePagingSource(apiService, genreId) }
        )
    }

    fun getDiscoverMoviesRepo(): Flow<MovieResponse> = flow {
        val response = apiService.getDiscoverMovies(page = 1)
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun getTrendingMoviesRepo(): Flow<MovieResponse> = flow {
        val response = apiService.getTrendingMovies(page = 1)
        emit(response)
    }.flowOn(Dispatchers.IO)
    fun getTrendingAllRepo(): Flow<MovieResponse> = flow {
        val response = apiService.getTrendingAll(page = 1)
        emit(response)
    }.flowOn(Dispatchers.IO)
    fun getUpcomingMoviesRepo(): Flow<MovieResponse> = flow {
        val response = apiService.getUpcomingMovies(page = 1)
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun getMovieGenresRepo(): Flow<GenreResponse> = flow {
        val response = apiService.getMovieGenres()
        emit(response)
    }.flowOn(Dispatchers.IO)
}
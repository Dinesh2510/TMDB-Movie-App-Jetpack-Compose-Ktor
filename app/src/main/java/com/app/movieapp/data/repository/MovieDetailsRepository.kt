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
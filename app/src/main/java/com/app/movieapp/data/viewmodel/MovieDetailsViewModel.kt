package com.app.movieapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.movieapp.data.remote.response.MovieDetailsDTO
import com.app.movieapp.data.remote.response.MovieResponse
import com.app.movieapp.data.repository.MovieDetailsRepository
import com.app.movieapp.models.Cast
import com.app.movieapp.utlis.MovieState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val repository: MovieDetailsRepository
) : ViewModel() {

    private val _detailsMovieResponses: MutableStateFlow<MovieState<MovieDetailsDTO?>> =
        MutableStateFlow(MovieState.Loading)
    val detailsMovieResponses: StateFlow<MovieState<MovieDetailsDTO?>> = _detailsMovieResponses.asStateFlow()

    private val _similarMovieResponses: MutableStateFlow<MovieState<MovieResponse?>> =
        MutableStateFlow(MovieState.Loading)
    val similarMovieResponses: StateFlow<MovieState<MovieResponse?>> = _similarMovieResponses.asStateFlow()

    private val _castMovieResponses: MutableStateFlow<MovieState<List<Cast>?>> =
        MutableStateFlow(MovieState.Loading)
    val castMovieResponses: StateFlow<MovieState<List<Cast>?>> = _castMovieResponses.asStateFlow()

    fun fetchMoviesDetails(movieId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getMoviesDetailsRepo(movieId).first()
                _detailsMovieResponses.value = MovieState.Success(response)
            } catch (e: Exception) {
                _detailsMovieResponses.value = MovieState.Error("An error occurred. Please try again.")
            }
        }
    }

    fun fetchSimilarMovies(movieId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getSimilarMoviesRepo(movieId).first()
                _similarMovieResponses.value = MovieState.Success(response)
            } catch (e: Exception) {
                _similarMovieResponses.value = MovieState.Error("An error occurred. Please try again.")
            }
        }
    }

    fun fetchCasteOfMovies(movieId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getCastMoviesRepo(movieId).first()
                val castList = response.castResult
                _castMovieResponses.value = MovieState.Success(castList)
            } catch (e: Exception) {
                _castMovieResponses.value = MovieState.Error("An error occurred. Please try again.")
            }
        }
    }
}
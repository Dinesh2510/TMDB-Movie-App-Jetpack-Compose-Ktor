package com.app.movieapp.data.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.movieapp.R
import com.app.movieapp.data.remote.response.MovieDetailsDTO
import com.app.movieapp.data.remote.response.MovieResponse
import com.app.movieapp.data.remote.response.VideoResponse
import com.app.movieapp.data.remote.response.VideoResult
import com.app.movieapp.data.repository.MovieDetailsRepository
import com.app.movieapp.models.Cast
import com.app.movieapp.utlis.MovieState
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.serialization.json.Json
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed interface MovieDetailsUIState {
    data object Loading : MovieDetailsUIState
    data class Success(
        val movieDetails: MovieDetailsDTO,
        val castList: List<Cast>,
        val similarMovies: MovieResponse?
    ) : MovieDetailsUIState
    data class Error(@StringRes val messageRes: Int) : MovieDetailsUIState // Change String -> Int
}

class MovieDetailsViewModel(
    private val repository: MovieDetailsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieDetailsUIState>(MovieDetailsUIState.Loading)
    val uiState: StateFlow<MovieDetailsUIState> = _uiState.asStateFlow()

    private val _videoResponses = MutableStateFlow<MovieState<VideoResponse?>>(MovieState.Loading)
    val videoResponses: StateFlow<MovieState<VideoResponse?>> = _videoResponses.asStateFlow()

    private val _trailerKey = MutableStateFlow<String?>(null)
    val trailerKey: StateFlow<String?> = _trailerKey.asStateFlow()

    fun fetchAllMovieDetails(movieId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = MovieDetailsUIState.Loading

            try {
                supervisorScope {
                    val detailsDeferred = async { repository.getMoviesDetailsRepo(movieId).first() }
                    val castDeferred = async { repository.getCastMoviesRepo(movieId).first() }
                    val similarDeferred = async { repository.getSimilarMoviesRepo(movieId).first() }

                    val details = detailsDeferred.await()
                    val cast = castDeferred.await().castResult ?: emptyList()
                    val similar = similarDeferred.await()

                    _uiState.value = MovieDetailsUIState.Success(
                        movieDetails = details,
                        castList = cast,
                        similarMovies = similar
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: UnknownHostException) {
                _uiState.value = MovieDetailsUIState.Error((R.string.error_no_internet))
            } catch (e: IOException) {
                _uiState.value = MovieDetailsUIState.Error(R.string.error_network_communication)
            } catch (e: Exception) {
                _uiState.value = MovieDetailsUIState.Error(R.string.error_unknown)
            }
        }
    }

    fun fetchMovieVideos(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _videoResponses.value = MovieState.Loading
            try {
                val response = repository.getMovieVideosRepo(movieId).first()
                _videoResponses.value = MovieState.Success(response)

                val primaryTrailer = extractPrimaryTrailer(response.results)
                _trailerKey.value = primaryTrailer?.key
            } catch (e: Exception) {
                _videoResponses.value = MovieState.Error("Failed to load videos.")
            }
        }
    }

    fun fetchTvShowVideos(tvId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _videoResponses.value = MovieState.Loading
            try {
                val response = repository.getTvShowVideosRepo(tvId).first()
                _videoResponses.value = MovieState.Success(response)

                val primaryTrailer = extractPrimaryTrailer(response.results)
                _trailerKey.value = primaryTrailer?.key
            } catch (e: Exception) {
                _videoResponses.value = MovieState.Error("Failed to load videos.")
            }
        }
    }

    private fun extractPrimaryTrailer(results: List<VideoResult>?): VideoResult? {
        if (results.isNullOrEmpty()) return null
        return results.firstOrNull { it.site.equals("YouTube", ignoreCase = true) && it.type.equals("Trailer", ignoreCase = true) && it.official }
            ?: results.firstOrNull { it.site.equals("YouTube", ignoreCase = true) && it.type.equals("Trailer", ignoreCase = true) }
            ?: results.firstOrNull { it.site.equals("YouTube", ignoreCase = true) }
    }
}
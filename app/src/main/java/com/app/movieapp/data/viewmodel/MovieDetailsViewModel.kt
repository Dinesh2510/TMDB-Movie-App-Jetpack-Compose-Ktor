package com.app.movieapp.data.viewmodel

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.movieapp.R
import com.app.movieapp.data.remote.response.MovieDetailsDTO
import com.app.movieapp.data.remote.response.MovieResponse
import com.app.movieapp.data.repository.MovieDetailsRepository
import com.app.movieapp.models.Cast
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.io.IOException
import java.net.UnknownHostException

import androidx.annotation.StringRes

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
}
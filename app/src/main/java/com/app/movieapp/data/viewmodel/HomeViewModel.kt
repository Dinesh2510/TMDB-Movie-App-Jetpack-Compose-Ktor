package com.app.movieapp.data.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.app.movieapp.data.remote.response.GenreResponse
import com.app.movieapp.data.remote.response.MovieResponse
import com.app.movieapp.data.repository.HomeRepository
import com.app.movieapp.models.Movies
import com.app.movieapp.utlis.MovieState
import com.app.movieapp.utlis.NetworkUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    private val _discoveryMovieResponses: MutableStateFlow<MovieState<MovieResponse?>> =
        MutableStateFlow(MovieState.Loading)
    val discoveryMovieResponses: StateFlow<MovieState<MovieResponse?>> = _discoveryMovieResponses.asStateFlow()

    private val _trendingMovieResponses: MutableStateFlow<MovieState<MovieResponse?>> =
        MutableStateFlow(MovieState.Loading)
    val trendingMovieResponses: StateFlow<MovieState<MovieResponse?>> = _trendingMovieResponses.asStateFlow()

    private val _nowPlayingMoviesResponses: MutableStateFlow<MovieState<MovieResponse?>> =
        MutableStateFlow(MovieState.Loading)
    val nowPlayingMoviesResponses: StateFlow<MovieState<MovieResponse?>> = _nowPlayingMoviesResponses.asStateFlow()

    private val _upcomingMoviesResponses: MutableStateFlow<MovieState<MovieResponse?>> =
        MutableStateFlow(MovieState.Loading)
    val upcomingMoviesResponses: StateFlow<MovieState<MovieResponse?>> = _upcomingMoviesResponses.asStateFlow()

    private val _genresMoviesResponses: MutableStateFlow<MovieState<GenreResponse?>> =
        MutableStateFlow(MovieState.Loading)
    val genresMoviesResponses: StateFlow<MovieState<GenreResponse?>> = _genresMoviesResponses.asStateFlow()

    var genresWiseMovieListState: Flow<PagingData<Movies>>? = null

    private val networkUtils = NetworkUtils()
    val networkType = networkUtils.networkType

    init {
        fetchDiscoverMovies()
        fetchTrendingMovies()
        fetchNowPlayingMovies()
        fetchUpcomingMovies()
        fetchGenreResponse()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun registerNetwork(context: Context) {
        networkUtils.registerNetworkCallback(context)
    }

    // Pagination Streams
    val nowPlayingAllListState = repository.getAllMoviesPagination("nowPlayingAllListScreen").flow.cachedIn(viewModelScope)
    val popularAllListState = repository.getAllMoviesPagination("popularAllListScreen").flow.cachedIn(viewModelScope)
    val discoverListState = repository.getAllMoviesPagination("discoverListScreen").flow.cachedIn(viewModelScope)
    val upcomingListState = repository.getAllMoviesPagination("upcomingListScreen").flow.cachedIn(viewModelScope)

    fun setGenreData(genreSelected: Int) {
        genresWiseMovieListState = repository.getGenresWiseMovieRepo(genreSelected).flow.cachedIn(viewModelScope)
    }

    fun fetchDiscoverMovies() {
        viewModelScope.launch {
            try {
                val response = repository.getDiscoverMoviesRepo().first()
                _discoveryMovieResponses.value = MovieState.Success(response)
            } catch (e: Exception) {
                _discoveryMovieResponses.value = MovieState.Error("An error occurred. Please try again.")
            }
        }
    }

    fun fetchTrendingMovies() {
        viewModelScope.launch {
            try {
                val response = repository.getTrendingMoviesRepo().first()
                _trendingMovieResponses.value = MovieState.Success(response)
            } catch (e: Exception) {
                _trendingMovieResponses.value = MovieState.Error("An error occurred. Please try again.")
            }
        }
    }

    fun fetchNowPlayingMovies() {
        viewModelScope.launch {
            try {
                val response = repository.getNowPlayingMoviesRepo().first()
                _nowPlayingMoviesResponses.value = MovieState.Success(response)
            } catch (e: Exception) {
                _nowPlayingMoviesResponses.value = MovieState.Error("An error occurred. Please try again.")
            }
        }
    }

    fun fetchUpcomingMovies() {
        viewModelScope.launch {
            try {
                val response = repository.getUpcomingMoviesRepo().first()
                _upcomingMoviesResponses.value = MovieState.Success(response)
            } catch (e: Exception) {
                _upcomingMoviesResponses.value = MovieState.Error("An error occurred. Please try again.")
            }
        }
    }

    fun fetchGenreResponse() {
        viewModelScope.launch {
            try {
                val response = repository.getMovieGenresRepo().first()
                _genresMoviesResponses.value = MovieState.Success(response)
            } catch (e: Exception) {
                _genresMoviesResponses.value = MovieState.Error("An error occurred. Please try again.")
            }
        }
    }
}
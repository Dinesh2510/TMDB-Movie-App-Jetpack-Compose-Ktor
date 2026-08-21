package com.app.movieapp.data.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.app.movieapp.R
import com.app.movieapp.data.remote.response.GenreResponse
import com.app.movieapp.data.remote.response.MovieResponse
import com.app.movieapp.data.repository.HomeRepository
import com.app.movieapp.models.Movies
import com.app.movieapp.utlis.NetworkUtils
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.io.IOException
import java.net.UnknownHostException

// Unified UI State for single-pass Home Feed loading
sealed interface HomeFeedUIState {
    data object Loading : HomeFeedUIState
    data class Success(
        val discoverMovies: MovieResponse?,
        val trendingMovies: MovieResponse?,
        val nowPlayingMovies: MovieResponse?,
        val upcomingMovies: MovieResponse?,
        val genres: GenreResponse?,
        val trendingAll: MovieResponse?
    ) : HomeFeedUIState
    data class Error(@StringRes val messageRes: Int) : HomeFeedUIState // Change String -> Int
}

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    private val _homeFeedState = MutableStateFlow<HomeFeedUIState>(HomeFeedUIState.Loading)
    val homeFeedState: StateFlow<HomeFeedUIState> = _homeFeedState.asStateFlow()

    private val _selectedGenreId = MutableStateFlow<Int?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val genresWiseMovieListState: Flow<PagingData<Movies>> = _selectedGenreId
        .filterNotNull()
        .flatMapLatest { genreId ->
            repository.getGenresWiseMovieRepo(genreId).flow
        }
        .cachedIn(viewModelScope)

    private val networkUtils = NetworkUtils()
    val networkType = networkUtils.networkType

    // Pagination Streams for "See All" screens
    val nowPlayingAllListState = repository.getAllMoviesPagination("nowPlayingAllListScreen").flow.cachedIn(viewModelScope)
    val popularAllListState = repository.getAllMoviesPagination("popularAllListScreen").flow.cachedIn(viewModelScope)
    val discoverListState = repository.getAllMoviesPagination("discoverListScreen").flow.cachedIn(viewModelScope)
    val upcomingListState = repository.getAllMoviesPagination("upcomingListScreen").flow.cachedIn(viewModelScope)

    init {
        fetchAllHomeData()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun registerNetwork(context: Context) {
        networkUtils.registerNetworkCallback(context)
    }

    /**
     * Parallel execution protected by supervisorScope to prevent unhandled network crashes.
     */
    fun fetchAllHomeData() {
        viewModelScope.launch(Dispatchers.IO) {
            _homeFeedState.value = HomeFeedUIState.Loading

            try {
                supervisorScope {
                    val discoverDeferred = async { repository.getDiscoverMoviesRepo().first() }
                    val trendingDeferred = async { repository.getTrendingMoviesRepo().first() }
                    val nowPlayingDeferred = async { repository.getNowPlayingMoviesRepo().first() }
                    val upcomingDeferred = async { repository.getUpcomingMoviesRepo().first() }
                    val genresDeferred = async { repository.getMovieGenresRepo().first() }
                    val trendingAllDeferred = async { repository.getTrendingAllRepo().first() }

                    _homeFeedState.value = HomeFeedUIState.Success(
                        discoverMovies = discoverDeferred.await(),
                        trendingMovies = trendingDeferred.await(),
                        nowPlayingMovies = nowPlayingDeferred.await(),
                        upcomingMovies = upcomingDeferred.await(),
                        genres = genresDeferred.await(),
                        trendingAll = trendingAllDeferred.await()
                    )
                }
            } catch (e: CancellationException) {
                throw e // Essential for proper coroutine lifecycle cancellation
            } catch (e: UnknownHostException) {
                _homeFeedState.value = HomeFeedUIState.Error(R.string.error_no_internet)
            } catch (e: IOException) {
                _homeFeedState.value = HomeFeedUIState.Error(R.string.error_network_communication)
            } catch (e: Exception) {
                _homeFeedState.value = HomeFeedUIState.Error(R.string.error_unknown)
            }
        }
    }

    fun setGenreData(genreSelected: Int) {
        _selectedGenreId.value = genreSelected
    }
}
package com.app.movieapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.app.movieapp.data.paging.MoviePagingSource
import com.app.movieapp.data.remote.ApiService
import com.app.movieapp.models.Movies
import com.app.movieapp.utlis.Constants.Companion.topRatedMovies
import com.app.movieapp.utlis.Constants.Companion.topRatedTv
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ContentType { TV_SHOWS, MOVIES }

class TopRatedViewModel(private val apiService: ApiService) : ViewModel() {

    private val _selectedTab = MutableStateFlow(ContentType.TV_SHOWS)
    val selectedTab: StateFlow<ContentType> = _selectedTab.asStateFlow()

    val topRatedTvPagingFlow: Flow<PagingData<Movies>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false)
    ) {
        MoviePagingSource(apiService, topRatedTv)
    }.flow.cachedIn(viewModelScope)

    val topRatedMoviesPagingFlow: Flow<PagingData<Movies>> = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false)
    ) {
        MoviePagingSource(apiService, topRatedMovies)
    }.flow.cachedIn(viewModelScope)

    fun selectTab(tab: ContentType) {
        _selectedTab.value = tab
    }
}
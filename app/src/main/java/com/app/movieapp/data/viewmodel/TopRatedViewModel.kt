/*
 * Copyright (c) 2026 Dinesh2510
 * File : TopRatedViewModel.kt
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
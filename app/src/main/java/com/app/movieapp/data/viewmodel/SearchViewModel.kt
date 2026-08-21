package com.app.movieapp.data.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.app.movieapp.data.repository.SearchRepository
import com.app.movieapp.models.Search
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class SearchViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("Jack Reacher")
    val searchQuery = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchPagingFlow: Flow<PagingData<Search>> = _searchQuery
        .filter { it.isNotBlank() }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            searchRepository.multiSearch(
                searchParams = query,
                includeAdult = true
            ).map { pagingData ->
                pagingData.filter { item ->
                    item.title != null || item.originalName != null
                }
            }
        }
        .cachedIn(viewModelScope)

    fun onSearchQueryChanged(newQuery: String) {
        if (newQuery.isNotBlank()) {
            _searchQuery.value = newQuery
        }
    }
}
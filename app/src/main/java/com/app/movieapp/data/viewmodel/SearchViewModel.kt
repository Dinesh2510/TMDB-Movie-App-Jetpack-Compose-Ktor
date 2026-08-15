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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _multiSearch = mutableStateOf<Flow<PagingData<Search>>>(emptyFlow())
    val multiSearchState: State<Flow<PagingData<Search>>> = _multiSearch

    var searchParam = mutableStateOf("")

    init {
        searchParam.value = "Jack Reacher"
        searchRemoteMovie(includeAdult = true)
    }

    fun searchRemoteMovie(includeAdult: Boolean) {
        viewModelScope.launch {
            if (searchParam.value.isNotBlank()) {
                _multiSearch.value = searchRepository.multiSearch(
                    searchParams = searchParam.value,
                    includeAdult = includeAdult
                ).map { pagingData ->
                    pagingData.filter { item ->
                        item.title != null || item.originalName != null
                    }
                }.cachedIn(viewModelScope)
            }
        }
    }
}
/*
 * Copyright (c) 2026 Dinesh2510
 * File : MoviePagingSource.kt
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

package com.app.movieapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.app.movieapp.data.remote.ApiService
import com.app.movieapp.models.Movies
import com.app.movieapp.utlis.Constants.Companion.discoverListScreen
import com.app.movieapp.utlis.Constants.Companion.nowPlayingAllListScreen
import com.app.movieapp.utlis.Constants.Companion.popularAllListScreen
import com.app.movieapp.utlis.Constants.Companion.topRatedMovies
import com.app.movieapp.utlis.Constants.Companion.topRatedTv
import com.app.movieapp.utlis.Constants.Companion.upcomingListScreen

class MoviePagingSource(
    private val apiService: ApiService,
    private val tags: String
) : PagingSource<Int, Movies>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movies> {
        return try {
            val nextPage = params.key ?: 1

            val response = when (tags) {
                nowPlayingAllListScreen -> apiService.getNowPlayingMovies(page = nextPage)
                discoverListScreen -> apiService.getDiscoverMovies(page = nextPage)
                upcomingListScreen -> apiService.getUpcomingMovies(page = nextPage)
                popularAllListScreen -> apiService.getPopularMovies(page = nextPage)
                topRatedMovies -> apiService.getTopRatedMovies(page = nextPage)
                topRatedTv -> apiService.getTopRatedTvShows(page = nextPage)
                else -> apiService.getPopularMovies(page = nextPage)
            }

            val movieList = response.results ?: emptyList()

            LoadResult.Page(
                data = movieList,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (movieList.isEmpty() || nextPage >= (response.totalPages ?: 1)) null else nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movies>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
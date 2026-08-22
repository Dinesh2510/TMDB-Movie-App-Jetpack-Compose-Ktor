/*
 * Copyright (c) 2026 Dinesh2510
 * File : SearchFilmSource.kt
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
import com.app.movieapp.models.Search
import io.ktor.client.plugins.ResponseException
import java.io.IOException

class SearchFilmSource(
    private val api: ApiService,
    private val searchParams: String,
    private val includeAdult: Boolean
) : PagingSource<Int, Search>() {

    override fun getRefreshKey(state: PagingState<Int, Search>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Search> {
        return try {
            val nextPage = params.key ?: 1

            val searchMovies = api.multiSearch(
                page = nextPage,
                searchParams = searchParams,
                includeAdult = includeAdult
            )

            LoadResult.Page(
                data = searchMovies.results,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (searchMovies.results.isEmpty()) null else searchMovies.page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: ResponseException) {
            // Replaces retrofit2.HttpException
            LoadResult.Error(e)
        } catch (e: Exception) {
            // General catch-all for serialization or runtime exceptions
            LoadResult.Error(e)
        }
    }
}
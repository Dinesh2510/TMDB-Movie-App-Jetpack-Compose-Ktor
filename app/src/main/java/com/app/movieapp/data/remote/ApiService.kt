/*
 * Copyright (c) 2026 Dinesh2510
 * File : ApiService.kt
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

package com.app.movieapp.data.remote

import com.app.movieapp.data.remote.response.CastResponse
import com.app.movieapp.data.remote.response.GenreResponse
import com.app.movieapp.data.remote.response.MovieDetailsDTO
import com.app.movieapp.data.remote.response.MovieResponse
import com.app.movieapp.data.remote.response.MultiSearchResponse
import com.app.movieapp.data.remote.response.VideoResponse
import com.app.movieapp.utlis.Constants.Companion.API_KEY
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ApiService(private val client: HttpClient) {

    suspend fun getTrendingAll(
        page: Int = 1,
        apiKey: String = API_KEY,
        language: String = "en-US"
    ): MovieResponse = client.get("trending/all/week") {
        parameter("page", page)
        parameter("api_key", apiKey)
        parameter("language", language)
    }.body()
    
    // ==========================================
    // Movies
    // ==========================================

    suspend fun getTrendingMovies(
        page: Int = 1,
        apiKey: String = API_KEY,
        language: String = "en"
    ): MovieResponse = client.get("trending/movie/week") {
        parameter("page", page)
        parameter("api_key", apiKey)
        parameter("language", language)
    }.body()

    suspend fun getPopularMovies(
        page: Int = 1,
        apiKey: String = API_KEY,
        language: String = "en"
    ): MovieResponse = client.get("movie/popular") {
        parameter("page", page)
        parameter("api_key", apiKey)
        parameter("language", language)
    }.body()

    suspend fun getTopRatedMovies(
        page: Int = 1,
        apiKey: String = API_KEY,
        language: String = "en"
    ): MovieResponse = client.get("movie/top_rated") {
        parameter("page", page)
        parameter("api_key", apiKey)
        parameter("language", language)
    }.body()

    suspend fun getNowPlayingMovies(
        page: Int = 1,
        apiKey: String = API_KEY,
        language: String = "en"
    ): MovieResponse = client.get("movie/now_playing") {
        parameter("page", page)
        parameter("api_key", apiKey)
        parameter("language", language)
    }.body()

    suspend fun getUpcomingMovies(
        page: Int = 1,
        apiKey: String = API_KEY,
        language: String = "en"
    ): MovieResponse = client.get("movie/upcoming") {
        parameter("page", page)
        parameter("api_key", apiKey)
        parameter("language", language)
    }.body()

    suspend fun getRecommendedMovies(
        movieId: Int,
        page: Int = 1,
        apiKey: String = API_KEY,
        language: String = "en"
    ): MovieResponse = client.get("movie/$movieId/recommendations") {
        parameter("page", page)
        parameter("api_key", apiKey)
        parameter("language", language)
    }.body()

    suspend fun getGenreWiseMovieList(
        genresId: Int,
        page: Int = 1,
        apiKey: String = API_KEY,
        language: String = "en"
    ): MovieResponse = client.get("discover/movie") {
        parameter("with_genres", genresId)
        parameter("page", page)
        parameter("api_key", apiKey)
        parameter("language", language)
    }.body()

    suspend fun getSimilarMovies(
        filmId: Int,
        page: Int = 1,
        apiKey: String = API_KEY,
        language: String = "en"
    ): MovieResponse = client.get("movie/$filmId/similar") {
        parameter("page", page)
        parameter("api_key", apiKey)
        parameter("language", language)
    }.body()

    suspend fun getDiscoverMovies(
        page: Int = 1,
        gteReleaseDate: String = "1940-01-01",
        lteReleaseDate: String = "1981-01-01",
        apiKey: String = API_KEY,
        language: String = "en",
        sortBy: String = "vote_count.desc"
    ): MovieResponse = client.get("discover/movie") {
        parameter("page", page)
        parameter("primary_release_date.gte", gteReleaseDate)
        parameter("primary_release_date.lte", lteReleaseDate)
        parameter("api_key", apiKey)
        parameter("language", language)
        parameter("sort_by", sortBy)
    }.body()

    suspend fun getMoviesDetails(
        movieId: Int,
        appendToResponse: String = "videos,credits",
        apiKey: String = API_KEY,
        language: String = "en"
    ): MovieDetailsDTO = client.get("movie/$movieId") {
        parameter("append_to_response", appendToResponse)
        parameter("api_key", apiKey)
        parameter("language", language)
    }.body()

    suspend fun getMovieCast(
        filmId: Int,
        apiKey: String = API_KEY
    ): CastResponse = client.get("movie/$filmId/credits") {
        parameter("api_key", apiKey)
    }.body()

    suspend fun getMovieGenres(
        apiKey: String = API_KEY,
        language: String = "en"
    ): GenreResponse = client.get("genre/movie/list") {
        parameter("api_key", apiKey)
        parameter("language", language)
    }.body()

    suspend fun multiSearch(
        searchParams: String,
        page: Int = 1,
        includeAdult: Boolean = true,
        apiKey: String = API_KEY,
        language: String = "en"
    ): MultiSearchResponse = client.get("search/multi") {
        parameter("query", searchParams)
        parameter("page", page)
        parameter("include_adult", includeAdult)
        parameter("api_key", apiKey)
        parameter("language", language)
    }.body()

    // ==========================================
    // TV Shows
    // ==========================================

    suspend fun getTvShowGenres(
        apiKey: String = API_KEY,
        language: String = "en-US"
    ): GenreResponse = client.get("genre/tv/list") {
        parameter("api_key", apiKey)
        parameter("language", language)
    }.body()

    suspend fun getTvShowCast(
        filmId: Int,
        apiKey: String = API_KEY
    ): CastResponse = client.get("tv/$filmId/credits") {
        parameter("api_key", apiKey)
    }.body()

    suspend fun getSimilarTvShows(
        filmId: Int,
        page: Int = 1,
        apiKey: String = API_KEY,
        language: String = "en-US"
    ): MovieResponse = client.get("tv/$filmId/similar") {
        parameter("page", page)
        parameter("api_key", apiKey)
        parameter("language", language)
    }.body()

    suspend fun getTrendingTvSeries(
        page: Int = 1,
        apiKey: String = API_KEY,
        language: String = "en-US"
    ): MovieResponse = client.get("trending/tv/week") {
        parameter("page", page)
        parameter("api_key", apiKey)
        parameter("language", language)
    }.body()

    suspend fun getPopularTvShows(
        page: Int = 1,
        apiKey: String = API_KEY,
        language: String = "en-US"
    ): MovieResponse = client.get("tv/popular") {
        parameter("page", page)
        parameter("api_key", apiKey)
        parameter("language", language)
    }.body()

    suspend fun getTopRatedTvShows(
        page: Int = 1,
        apiKey: String = API_KEY,
        language: String = "en-US"
    ): MovieResponse = client.get("tv/top_rated") {
        parameter("page", page)
        parameter("api_key", apiKey)
        parameter("language", language)
    }.body()

    suspend fun getRecommendedTvShows(
        filmId: Int,
        page: Int = 1,
        apiKey: String = API_KEY,
        language: String = "en-US"
    ): MovieResponse = client.get("tv/$filmId/recommendations") {
        parameter("page", page)
        parameter("api_key", apiKey)
        parameter("language", language)
    }.body()

    suspend fun getDiscoverTvShows(
        page: Int = 1,
        gteFirstAirDate: String = "1940-01-01",
        lteFirstAirDate: String = "1981-01-01",
        apiKey: String = API_KEY,
        language: String = "en-US",
        sortBy: String = "vote_count.desc"
    ): MovieResponse = client.get("discover/tv") {
        parameter("page", page)
        parameter("first_air_date.gte", gteFirstAirDate)
        parameter("first_air_date.lte", lteFirstAirDate)
        parameter("api_key", apiKey)
        parameter("language", language)
        parameter("sort_by", sortBy)
    }.body()

    suspend fun getMovieVideos(
        movieId: Int,
        apiKey: String = API_KEY,
        language: String = "en-US"
    ): VideoResponse = client.get("movie/$movieId/videos") {
        parameter("api_key", apiKey)
        parameter("language", language)
    }.body()

    suspend fun getTvShowVideos(
        tvId: Int,
        apiKey: String = API_KEY,
        language: String = "en-US"
    ): VideoResponse = client.get("tv/$tvId/videos") {
        parameter("api_key", apiKey)
        parameter("language", language)
    }.body()
}

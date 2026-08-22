/*
 * Copyright (c) 2026 Dinesh2510
 * File : Constants.kt
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

package com.app.movieapp.utlis

import com.app.movieapp.BuildConfig

class Constants {
    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        val API_KEY: String = BuildConfig.TMDB_READ_ACCESS_TOKEN

        const val BASE_BACKDROP_IMAGE_URL = "https://image.tmdb.org/t/p/w780/"
        const val BASE_POSTER_IMAGE_URL = "https://image.tmdb.org/t/p/w500/"

        const val nowPlayingAllListScreen = "nowPlayingAllListScreen"
        const val popularAllListScreen = "popularAllListScreen"
        const val discoverListScreen = "DiscoverListScreen"
        const val upcomingListScreen = "upcomingListScreen"
        const val topRatedMovies = "topratedmovies"
        const val topRatedTv = "topratedtv"
    }
}
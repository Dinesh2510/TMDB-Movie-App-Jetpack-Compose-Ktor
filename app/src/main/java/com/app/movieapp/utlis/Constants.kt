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
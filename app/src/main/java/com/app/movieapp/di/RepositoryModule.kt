
/*
 * Copyright (c) 2026 Dinesh2510
 * File : RepositoryModule.kt
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

package com.app.movieapp.di

import com.app.movieapp.data.repository.ContinueWatchingRepository
import com.app.movieapp.data.repository.HomeRepository
import com.app.movieapp.data.repository.MovieDetailsRepository
import com.app.movieapp.data.repository.MyListMovieRepository
import com.app.movieapp.data.repository.SearchRepository
import com.app.movieapp.data.repository.TvShowRepository
import com.app.movieapp.data.repository.TvShowRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::MyListMovieRepository)
    singleOf(::HomeRepository)
    singleOf(::MovieDetailsRepository)
    singleOf(::SearchRepository)
    singleOf(::ContinueWatchingRepository)
    singleOf(::TvShowRepositoryImpl) bind TvShowRepository::class
}
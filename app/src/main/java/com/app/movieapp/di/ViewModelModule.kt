
/*
 * Copyright (c) 2026 Dinesh2510
 * File : ViewModelModule.kt
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

import com.app.movieapp.data.viewmodel.AiChatViewModel
import com.app.movieapp.data.viewmodel.ContinueWatchingViewModel
import com.app.movieapp.data.viewmodel.HomeViewModel
import com.app.movieapp.data.viewmodel.MovieDetailsViewModel
import com.app.movieapp.data.viewmodel.SearchViewModel
import com.app.movieapp.data.viewmodel.TopRatedViewModel
import com.app.movieapp.data.viewmodel.WatchListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::MovieDetailsViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::WatchListViewModel)
    viewModelOf(::ContinueWatchingViewModel) // Added
    viewModelOf (::TopRatedViewModel)
    viewModelOf (::AiChatViewModel)

}
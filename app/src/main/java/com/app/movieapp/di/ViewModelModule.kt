
/*
 * Project: TMDB Compose
 * Package: com.app.movieapp.di
 *
 * Copyright 2026 Dinesh
 * GitHub: https://github.com/Dinesh2510
 *
 * Created on: Saturday, August 15, 2026 at 01:18
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
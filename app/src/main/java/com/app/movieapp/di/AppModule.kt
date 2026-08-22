/*
 * Copyright (c) 2026 Dinesh2510
 * File : AppModule.kt
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

import com.app.movieapp.data.local.UserPreferences
import com.app.movieapp.data.local.dataStore
import com.app.movieapp.data.viewmodel.AuthViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/*
 * Project: TMDB Compose
 * Package: com.app.movieapp.di
 *
 * Copyright 2026 Dinesh
 * GitHub: https://github.com/Dinesh2510
 *
 * Created on: Saturday, August 15, 2026 at 01:11
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

// Preference & Auth Module Definition
val preferencesModule = module {
    // Provide DataStore Instance
    single { androidContext().dataStore }

    // Provide UserPreferences Repository
    single { UserPreferences(get()) }

    // Provide AuthViewModel
    viewModelOf(::AuthViewModel)}

// Complete App Modules List
val appModules = listOf(
    networkModule,     // Ktor 3.x HttpClient & ApiService
    databaseModule,    // Room Database & DAOs
    repositoryModule,  // Repositories (Home, MovieDetails, Search, MyList)
    viewModelModule,   // ViewModels (Home, MovieDetails, Search, WatchList)
    preferencesModule  // DataStore & AuthViewModel
)
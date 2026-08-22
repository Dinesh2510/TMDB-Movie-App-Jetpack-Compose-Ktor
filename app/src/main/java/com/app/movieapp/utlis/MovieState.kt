/*
 * Copyright (c) 2026 Dinesh2510
 * File : MovieState.kt
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

sealed class MovieState<out T> {
    data class Success<T>(val data: T) : MovieState<T>()
    data class Error(val message: String) : MovieState<Nothing>()
    object Loading : MovieState<Nothing>()
}
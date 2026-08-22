/*
 * Copyright (c) 2026 Dinesh2510
 * File : MyListMovieRepository.kt
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

package com.app.movieapp.data.repository

import com.app.movieapp.data.local.MovieDao
import com.app.movieapp.data.local.WatchListModel
import kotlinx.coroutines.flow.Flow

class MyListMovieRepository(private val movieDao: MovieDao) {

    suspend fun insertMovieInList(myListMovie: WatchListModel) {
        movieDao.insertMovieInList(myListMovie)
    }

    suspend fun removeFromList(mediaId: Int) {
        movieDao.removeFromList(mediaId)
    }

    suspend fun deleteList() {
        movieDao.deleteList()
    }

    suspend fun exist(mediaId: Int): Int {
        return movieDao.exists(mediaId)
    }

    fun getAllData(): Flow<List<WatchListModel>> {
        return movieDao.getAllWatchListData()
    }
}
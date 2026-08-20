package com.app.movieapp.data.repository

import com.app.movieapp.data.local.ContinueWatchingModel
import com.app.movieapp.data.local.MovieDao
import kotlinx.coroutines.flow.Flow

class ContinueWatchingRepository(private val movieDao: MovieDao) {

    val continueWatchingList: Flow<List<ContinueWatchingModel>> = 
        movieDao.getAllContinueWatching()

    suspend fun saveProgress(
        mediaId: Int,
        title: String,
        posterPath: String?,
        backdropPath: String?,
        currentPositionMs: Long,
        totalDurationMs: Long,
        releaseDate: String = "",
        rating: Double = 0.0
    ) {
        // If watched more than 95%, auto-remove from continue watching
        if (totalDurationMs > 0 && (currentPositionMs.toFloat() / totalDurationMs.toFloat()) >= 0.95f) {
            movieDao.removeContinueWatching(mediaId)
            return
        }

        val item = ContinueWatchingModel(
            mediaId = mediaId,
            title = title,
            posterPath = posterPath,
            backdropPath = backdropPath,
            currentPositionMs = currentPositionMs,
            totalDurationMs = totalDurationMs,
            releaseDate = releaseDate,
            rating = rating,
            lastWatchedTimestamp = System.currentTimeMillis()
        )
        movieDao.upsertContinueWatching(item)
    }

    suspend fun getProgress(mediaId: Int): ContinueWatchingModel? {
        return movieDao.getContinueWatchingById(mediaId)
    }

    suspend fun removeFromContinueWatching(mediaId: Int) {
        movieDao.removeContinueWatching(mediaId)
    }
}
package com.app.movieapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "continue_watching_table")
data class ContinueWatchingModel(
    @PrimaryKey
    val mediaId: Int,
    val title: String,
    val posterPath: String?,
    val backdropPath: String?,
    val currentPositionMs: Long,
    val totalDurationMs: Long,
    val releaseDate: String = "",
    val rating: Double = 0.0,
    val lastWatchedTimestamp: Long = System.currentTimeMillis()
) {
    val progressFraction: Float
        get() = if (totalDurationMs > 0) {
            (currentPositionMs.toFloat() / totalDurationMs.toFloat()).coerceIn(0f, 1f)
        } else 0f

    val remainingMinutes: Long
        get() = ((totalDurationMs - currentPositionMs) / (1000 * 60)).coerceAtLeast(1)
}
package com.app.movieapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertMovieInList(watchListModel: WatchListModel)

    @Query("DELETE FROM watch_list_table WHERE mediaId =:mediaId")
    suspend fun removeFromList(mediaId: Int)

    @Query("DELETE FROM watch_list_table")
    suspend fun deleteList()

    @Query("SELECT EXISTS (SELECT 1 FROM watch_list_table WHERE mediaId = :mediaId)")
    suspend fun exists(mediaId: Int): Int

    @Query("SELECT * FROM watch_list_table ORDER BY addedOn DESC")
    fun getAllWatchListData(): Flow<List<WatchListModel>>

    // --- Continue Watching Queries ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertContinueWatching(item: ContinueWatchingModel)

    @Query("SELECT * FROM continue_watching_table ORDER BY lastWatchedTimestamp DESC")
    fun getAllContinueWatching(): Flow<List<ContinueWatchingModel>>

    @Query("SELECT * FROM continue_watching_table WHERE mediaId = :mediaId LIMIT 1")
    suspend fun getContinueWatchingById(mediaId: Int): ContinueWatchingModel?

    @Query("DELETE FROM continue_watching_table WHERE mediaId = :mediaId")
    suspend fun removeContinueWatching(mediaId: Int)

    @Query("DELETE FROM continue_watching_table")
    suspend fun clearAllContinueWatching()
}


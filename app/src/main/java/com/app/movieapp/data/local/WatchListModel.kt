package com.app.movieapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "watch_list_table")
data class WatchListModel(
    @PrimaryKey
    val mediaId: Int,
    val title: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String,
    val rating: Double,
    val runtime: Int?,
    val overview: String,
    val genres: String, // Stored as comma-separated: "Action, Adventure, Sci-Fi"
    val originalLanguage: String,
    val mediaType: String = "movie",
    val addedOn: String
)
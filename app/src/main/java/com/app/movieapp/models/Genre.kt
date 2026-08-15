package com.app.movieapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Genre(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String = ""
) : Parcelable
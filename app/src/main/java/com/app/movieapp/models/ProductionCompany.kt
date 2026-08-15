package com.app.movieapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class ProductionCompany(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("logo_path")
    val logoPath: String? = null,
    @SerialName("name")
    val name: String = "",
    @SerialName("origin_country")
    val originCountry: String = ""
) : Parcelable
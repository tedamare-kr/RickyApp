package com.kroger.rickyapp.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Origin(
    val name: String,
    val url: String
) : Parcelable

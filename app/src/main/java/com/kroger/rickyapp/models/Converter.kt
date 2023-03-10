package com.kroger.rickyapp.models

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi

class Converter {

    private val moshi = Moshi.Builder().build()

    @TypeConverter
    fun fromLocation(value: Location): String {
        val adapter = moshi.adapter(Location::class.java)
        return adapter.toJson(value)
    }

    @TypeConverter
    fun toLocation(string: String): Location {
        val adapter = moshi.adapter(Location::class.java)
        return adapter.fromJson(string)!!
    }

    @TypeConverter
    fun fromOrigin(value: Origin): String {
        val adapter = moshi.adapter(Origin::class.java)
        return adapter.toJson(value)!!
    }

    @TypeConverter
    fun toOrigin(value: String): Origin {
        val adapter = moshi.adapter(Origin::class.java)
        return adapter.fromJson(value)!!
    }
}

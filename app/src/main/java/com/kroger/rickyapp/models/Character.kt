package com.kroger.rickyapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kroger.rickyapp.models.Character.Companion.TABLE_NAME
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
@Entity(tableName = TABLE_NAME)
data class Character(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val image: String,
    val url: String,
    val created: String
) : Serializable {
    internal companion object {
        const val TABLE_NAME: String = "characters"
    }
}

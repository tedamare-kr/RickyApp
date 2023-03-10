package com.kroger.rickyapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kroger.rickyapp.models.Character
import com.kroger.rickyapp.models.Character.Companion.TABLE_NAME

@Dao
interface CharacterDao {
    // Returns long...the ID that was inserted
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCharacter(character: Character): Long

    // Returns long...the ID that was inserted
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCharacters(character: List<Character>): List<Long>

    // Returns a livedata object...that doesn't work with coroutines
    /**
     * Whenever an item in the LiveData List changes, the LiveData will notify all of its observers
     * that subscribed to the changes of the LiveData
     */
    @Query("SELECT * FROM $TABLE_NAME ORDER BY name ASC")
    fun getAllCharacters(): List<Character>

    @Delete
    suspend fun deleteCharacter(character: Character)
}

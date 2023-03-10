package com.kroger.rickyapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kroger.rickyapp.models.Character

/**
 * [CharacterDatabase] returns an existing database or,
 * creates a new instance if the database doesn't exist
 *
 * We pass in the Character table as an array
 */
@Database(
    entities = [Character::class],
    version = 2,
    exportSchema = false
)
//@TypeConverters(Converter::class)
abstract class CharacterDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao

    companion object {
        private const val DATABASE_NAME = "characters"

        // Other threads can see when this instance is changed
        // Single instance of the database
        @Volatile
        private var INSTANCE: CharacterDatabase? = null

        // Making sure there is only one single instance of that database
        operator fun invoke(context: Context): CharacterDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = getDatabaseBuilder(context)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private fun getDatabaseBuilder(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            CharacterDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration()
    }
}

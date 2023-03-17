package com.kroger.rickyapp.hilt

import android.content.Context
import com.kroger.rickyapp.db.CharacterDao
import com.kroger.rickyapp.db.CharacterDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

// How long the application lives
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesCharacterDatabase(@ApplicationContext context: Context): CharacterDatabase {
        return CharacterDatabase.invoke(context)
    }

    @Provides
    fun providesCharacterDao(characterDatabase: CharacterDatabase): CharacterDao {
        return characterDatabase.characterDao()
    }
}

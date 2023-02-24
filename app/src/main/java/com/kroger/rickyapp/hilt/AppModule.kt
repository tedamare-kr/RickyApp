package com.kroger.rickyapp.hilt

import android.content.Context
import com.kroger.rickyapp.db.CharacterDatabase
import com.kroger.rickyapp.network.RickAndMortyService
import com.kroger.rickyapp.ui.characters.CharactersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// How long the application lives
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Binds

    @Provides
    fun providesContext(@ApplicationContext context: Context) = context

    @Provides
    @Singleton
    fun providesCharacterDatabase(context: Context): CharacterDatabase {
        return CharacterDatabase.invoke(context)
    }

    // Provides
    @Provides
    @Singleton
    fun providesCharactersRepository(
        characterDatabase: CharacterDatabase,
        rickAndMortyService: RickAndMortyService
    ): CharactersRepository {
        return CharactersRepository(
            characterDatabase = characterDatabase,
            rickAndMortyService = rickAndMortyService
        )
    }
}

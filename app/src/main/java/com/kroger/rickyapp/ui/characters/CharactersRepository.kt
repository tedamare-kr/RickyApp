package com.kroger.rickyapp.ui.characters

import com.kroger.rickyapp.db.CharacterDatabase
import com.kroger.rickyapp.models.Character
import com.kroger.rickyapp.network.RickAndMortyService

class CharactersRepository(
    private val characterDatabase: CharacterDatabase,
    private val rickAndMortyService: RickAndMortyService
) {
    suspend fun getAllCharacters(pageNumber: Int) =
        rickAndMortyService.getAllCharacters(pageNumber)

    suspend fun getCharacterById(id: Int) =
        rickAndMortyService.getCharacterById(id)

    suspend fun searchCharacters(searchQuery: String, pageNumber: Int) =
        rickAndMortyService.searchCharacters(searchQuery, pageNumber)

    suspend fun upsert(character: Character) =
        characterDatabase.characterDao().upsertCharacter(character)

    fun getFavorites() = characterDatabase.characterDao().getAllCharacters()
}

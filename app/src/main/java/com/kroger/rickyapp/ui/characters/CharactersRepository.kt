package com.kroger.rickyapp.ui.characters

import com.kroger.rickyapp.db.CharacterDatabase
import com.kroger.rickyapp.models.Character
import com.kroger.rickyapp.network.RetrofitInstance

class CharactersRepository(
    val characterDatabase: CharacterDatabase
) {
    suspend fun getAllCharacters(pageNumber: Int) =
        RetrofitInstance.rickandmortyService.getAllCharacters(pageNumber)

    suspend fun getCharacterById(id: Int) =
        RetrofitInstance.rickandmortyService.getCharacterById(id)

    suspend fun searchCharacters(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.rickandmortyService.searchCharacters(searchQuery, pageNumber)

    suspend fun upsert(character: Character) =
        characterDatabase.characterDao().upsertCharacter(character)

    fun getFavorites() = characterDatabase.characterDao().getAllCharacters()
}

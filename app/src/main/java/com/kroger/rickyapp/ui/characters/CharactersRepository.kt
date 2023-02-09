package com.kroger.rickyapp.ui.characters

import com.kroger.rickyapp.db.CharacterDatabase
import com.kroger.rickyapp.network.RetrofitInstance

class CharactersRepository(
    val characterDatabase: CharacterDatabase
) {
    suspend fun getAllCharacters(pageNumber: Int) =
        RetrofitInstance.rickandmortyService.getAllCharacters(pageNumber)
}

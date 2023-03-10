package com.kroger.rickyapp.ui.details

import com.kroger.rickyapp.db.CharacterDatabase
import com.kroger.rickyapp.models.Character
import javax.inject.Inject

class CharactersDetailRepository @Inject constructor(
    private val characterDatabase: CharacterDatabase
) {
    suspend fun upsert(character: Character) =
        characterDatabase.characterDao().upsertCharacter(character)
}

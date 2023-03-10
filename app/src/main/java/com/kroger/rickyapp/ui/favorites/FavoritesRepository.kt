package com.kroger.rickyapp.ui.favorites

import com.kroger.rickyapp.db.CharacterDatabase
import javax.inject.Inject

class FavoritesRepository @Inject constructor(
    private val characterDatabase: CharacterDatabase
) {
    fun getFavorites() = characterDatabase.characterDao().getAllCharacters()
}

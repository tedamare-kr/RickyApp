package com.kroger.rickyapp.ui.characters

import com.kroger.rickyapp.network.CharactersApi

class CharactersRepository constructor(private val retrofitService: CharactersApi) {
    fun getAllCharacters() = retrofitService.getAllCharacters()
}

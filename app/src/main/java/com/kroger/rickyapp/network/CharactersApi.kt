package com.kroger.rickyapp.network

import com.kroger.rickyapp.models.CharacterResponse
import retrofit2.Call
import retrofit2.http.GET

interface CharactersApi {
    @GET("character")
    fun getAllCharacters(): Call<CharacterResponse>
}

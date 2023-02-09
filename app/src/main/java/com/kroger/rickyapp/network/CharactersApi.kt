package com.kroger.rickyapp.network

import com.kroger.rickyapp.models.CharacterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CharactersApi {
    @GET("character")
    suspend fun getAllCharacters(
        @Query("page")
        pageNumber: Int = 1
    ): Response<CharacterResponse>
}

package com.kroger.rickyapp.network

import com.kroger.rickyapp.models.Character
import com.kroger.rickyapp.models.CharacterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyService {
    @GET("character")
    suspend fun getAllCharacters(
        @Query("page")
        pageNumber: Int = 1
    ): Response<CharacterResponse>

    @GET("character/")
    suspend fun getCharacterById(
        @Query("id")
        id: Int
    ): Response<Character>

    @GET("character")
    suspend fun searchCharacters(
        @Query("name")
        searchQuery: String,
        @Query("page")
        pageNumber: Int
    ): Response<CharacterResponse>
}

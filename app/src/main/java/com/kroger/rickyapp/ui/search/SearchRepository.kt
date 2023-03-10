package com.kroger.rickyapp.ui.search

import com.kroger.rickyapp.network.RickAndMortyService
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val rickAndMortyService: RickAndMortyService
) {
    suspend fun searchCharacters(searchQuery: String, pageNumber: Int) =
        rickAndMortyService.searchCharacters(searchQuery, pageNumber)
}

package com.kroger.rickyapp.ui.characters

import android.util.Log
import com.kroger.rickyapp.db.CharacterDatabase
import com.kroger.rickyapp.models.Character
import com.kroger.rickyapp.models.CharacterResponse
import com.kroger.rickyapp.network.RickAndMortyService
import javax.inject.Inject

class CharactersRepository @Inject constructor(
    private val rickAndMortyService: RickAndMortyService,
    private val characterDatabase: CharacterDatabase
) {

    private var lastAPIRequestTime: Long = 0
    private val FIVE_MINUTES_IN_MILLIS: Long = 300_000

    suspend fun getTheChars(): CharacterUIData {
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime.minus(lastAPIRequestTime)

        // check if it has been more than 5 min
        Log.i("Current Time: ", "$currentTime vs $elapsedTime")
        return if (elapsedTime > FIVE_MINUTES_IN_MILLIS) {
            // Make API request and update the database
            val response = getAllCharactersFromAPI(pageNumber = 1)
            if (response.isSuccessful) {
                val characters = response.body()?.results ?: emptyList()
                insertAllCharacters(characters)
                lastAPIRequestTime = System.currentTimeMillis()
                CharacterUIData.Success(CharacterResponse(characters))
            } else CharacterUIData.Error
        } else {
            val characters = getCharsFromDB()
            CharacterUIData.Success(CharacterResponse(characters))
        }
    }

    private suspend fun getAllCharactersFromAPI(pageNumber: Int) =
        rickAndMortyService.getAllCharacters(pageNumber)

    private fun getCharsFromDB(): List<Character> =
        characterDatabase.characterDao().getAllCharacters()

    private suspend fun insertAllCharacters(characters: List<Character>) {
        characterDatabase.characterDao().insertAllCharacters(characters)
    }
}

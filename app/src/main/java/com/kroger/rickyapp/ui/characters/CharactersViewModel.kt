package com.kroger.rickyapp.ui.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kroger.rickyapp.models.CharacterResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val charactersRepository: CharactersRepository
) : ViewModel() {

    /**
     * Hey Dagger, please inject all these dependencies we have in the constructor
     *
     * Then take a look at your module and see if you can find it
     */

    private val charactersListFromAPI: MutableLiveData<CharacterUIData> =
        MutableLiveData(CharacterUIData.Loading)
    val characterLiveData: LiveData<CharacterUIData> = charactersListFromAPI

    init {
        viewModelScope.launch {
            // make network call
            val characters = charactersRepository.getTheChars()
            // handle response
            charactersListFromAPI.postValue(characters)
        }
    }
}

sealed class CharacterUIData {
    object Loading : CharacterUIData()
    object Error : CharacterUIData()
    data class Success(val characterResponse: CharacterResponse) : CharacterUIData()
}

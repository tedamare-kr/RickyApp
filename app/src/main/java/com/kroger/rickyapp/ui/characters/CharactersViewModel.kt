package com.kroger.rickyapp.ui.characters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kroger.rickyapp.models.CharacterResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CharactersViewModel(private val repository: CharactersRepository) : ViewModel() {

    val charactersList = MutableLiveData<CharacterResponse>()

    fun getAllCharacters() {
        // Starts on background thread
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getAllCharacters()
//            if (response.isSuccessful) {
//                charactersList.postValue(response.body())
//                // live data needs to be on main thread - switch back ot main
////                charactersList.value = response.body()
//            }
        }
    }
}

class CharactersViewModelFactory(private val repository: CharactersRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharactersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CharactersViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

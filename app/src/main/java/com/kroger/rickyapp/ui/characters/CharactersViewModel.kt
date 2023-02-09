package com.kroger.rickyapp.ui.characters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kroger.rickyapp.models.CharacterResponse
import com.kroger.rickyapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class CharactersViewModel(
    private val charactersRepository: CharactersRepository
) : ViewModel() {

    val charactersList: MutableLiveData<Resource<CharacterResponse>> = MutableLiveData()
    var charactersPage = 1

    init {
        getAllCharacters()
    }

    // This function is not suspend because we don't want to propagate the function to our fragment
    // We would then start a coroutine inside the fragment...we don't want that
    private fun getAllCharacters() =
        viewModelScope.launch {
            charactersList.postValue(Resource.Loading())
            val response = charactersRepository.getAllCharacters(pageNumber = charactersPage)
            charactersList.postValue(handleResponse(response))
        }

    private fun handleResponse(response: Response<CharacterResponse>): Resource<CharacterResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}

class CharactersViewModelProviderFactory(
    private val repository: CharactersRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharactersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CharactersViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

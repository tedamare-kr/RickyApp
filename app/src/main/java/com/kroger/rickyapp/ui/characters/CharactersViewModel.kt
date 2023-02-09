package com.kroger.rickyapp.ui.characters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kroger.rickyapp.models.Character
import com.kroger.rickyapp.models.CharacterResponse
import com.kroger.rickyapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class CharactersViewModel(
    private val charactersRepository: CharactersRepository
) : ViewModel() {

    val charactersList: MutableLiveData<Resource<CharacterResponse>> = MutableLiveData()
    var charactersPage = 1

    val searchResults: MutableLiveData<Resource<CharacterResponse>> = MutableLiveData()
    var searchPageNum = 1

    val character: MutableLiveData<Resource<Character>> = MutableLiveData()

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

    private fun getCharacterById(id: Int) =
        viewModelScope.launch {
            character.postValue(Resource.Loading())
            val response = charactersRepository.getCharacterById(id)
            character.postValue(handleSingleCharacterResponse(response))
        }

    fun searchCharacters(searchQuery: String) =
        viewModelScope.launch {
            searchResults.postValue(Resource.Loading())
            val response = charactersRepository.searchCharacters(searchQuery, searchPageNum)
            searchResults.postValue(handleSearchResponse(response))
        }

    private fun handleResponse(response: Response<CharacterResponse>): Resource<CharacterResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchResponse(response: Response<CharacterResponse>): Resource<CharacterResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSingleCharacterResponse(response: Response<Character>): Resource<Character> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun favoriteCharacter(character: Character) =
        viewModelScope.launch {
            charactersRepository.upsert(character)
        }

    fun getFavorites() = charactersRepository.getFavorites()
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

package com.kroger.rickyapp.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kroger.rickyapp.models.CharacterResponse
import com.kroger.rickyapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {
    val searchResults: MutableLiveData<Resource<CharacterResponse>> = MutableLiveData()
    var searchPageNum = 1

    fun searchCharacters(searchQuery: String) =
        viewModelScope.launch {
            searchResults.postValue(Resource.Loading())
            val response = searchRepository.searchCharacters(searchQuery, searchPageNum)
            searchResults.postValue(handleSearchResponse(response))
        }

    private fun handleSearchResponse(response: Response<CharacterResponse>): Resource<CharacterResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}

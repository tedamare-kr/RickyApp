package com.kroger.rickyapp.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.kroger.rickyapp.models.Character
import kotlinx.coroutines.launch

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    val favList: MutableLiveData<List<Character>> =
        MutableLiveData()

    val listThingy: LiveData<List<Character>> = favList

    fun getFavorites() {
        viewModelScope.launch {
            favList.postValue(favoritesRepository.getFavorites())
        }
    }
}

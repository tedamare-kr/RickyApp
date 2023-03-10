package com.kroger.rickyapp.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kroger.rickyapp.models.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersDetailViewModel @Inject constructor(
    private val charactersDetailRepository: CharactersDetailRepository
) : ViewModel() {
    fun favoriteCharacter(character: Character) =
        viewModelScope.launch {
            charactersDetailRepository.upsert(character)
        }
}

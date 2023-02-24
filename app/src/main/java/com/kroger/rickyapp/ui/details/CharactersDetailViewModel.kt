package com.kroger.rickyapp.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kroger.rickyapp.models.Character
import com.kroger.rickyapp.ui.characters.CharactersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersDetailViewModel @Inject constructor(
    private val charactersRepository: CharactersRepository
) : ViewModel() {
    fun favoriteCharacter(character: Character) =
        viewModelScope.launch {
            charactersRepository.upsert(character)
        }
}

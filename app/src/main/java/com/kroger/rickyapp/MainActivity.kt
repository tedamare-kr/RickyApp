package com.kroger.rickyapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kroger.rickyapp.databinding.ActivityMainBinding
import com.kroger.rickyapp.db.CharacterDatabase
import com.kroger.rickyapp.ui.characters.CharactersRepository
import com.kroger.rickyapp.ui.characters.CharactersViewModel
import com.kroger.rickyapp.ui.characters.CharactersViewModelProviderFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: CharactersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val charactersRepository = CharactersRepository(CharacterDatabase(this))
        val viewModelProviderFactory =
            CharactersViewModelProviderFactory(repository = charactersRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(CharactersViewModel::class.java)
    }
}

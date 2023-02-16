package com.kroger.rickyapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.kroger.rickyapp.databinding.ActivityMainBinding
import com.kroger.rickyapp.db.CharacterDatabase
import com.kroger.rickyapp.ui.characters.CharactersFragment
import com.kroger.rickyapp.ui.characters.CharactersRepository
import com.kroger.rickyapp.ui.characters.CharactersViewModel
import com.kroger.rickyapp.ui.characters.CharactersViewModelProviderFactory
import com.kroger.rickyapp.ui.favorites.FavoritesFragment
import com.kroger.rickyapp.ui.search.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: CharactersViewModel

    lateinit var viewModelProviderFactory: CharactersViewModelProviderFactory
    lateinit var charactersRepository: CharactersRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(CharactersFragment.newInstance(), CharactersFragment.FRAGMENT_TAG)

        handleBottomMenuNavigation()

        charactersRepository = CharactersRepository(CharacterDatabase(this))
        viewModelProviderFactory =
            CharactersViewModelProviderFactory(repository = charactersRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(CharactersViewModel::class.java)
    }

    private fun handleBottomMenuNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.charactersFragment -> loadFragment(
                    CharactersFragment.newInstance(),
                    CharactersFragment.FRAGMENT_TAG
                )
                R.id.favoritesFragment -> loadFragment(
                    FavoritesFragment.newInstance(),
                    FavoritesFragment.FRAGMENT_TAG
                )
                R.id.searchFragment -> loadFragment(
                    SearchFragment.newInstance(),
                    SearchFragment.FRAGMENT_TAG
                )
                else -> {}
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment, fragmentTag: String) {
        supportFragmentManager.commit {
            replace(
                R.id.fragment_container,
                fragment,
                fragmentTag
            )
            setReorderingAllowed(true)
        }
    }
}

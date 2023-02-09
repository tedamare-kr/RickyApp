package com.kroger.rickyapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kroger.rickyapp.databinding.ActivityMainBinding
import com.kroger.rickyapp.db.CharacterDatabase
import com.kroger.rickyapp.ui.characters.CharactersRepository
import com.kroger.rickyapp.ui.characters.CharactersViewModel
import com.kroger.rickyapp.ui.characters.CharactersViewModelProviderFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    lateinit var viewModel: CharactersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the navigation host fragment from this Activity
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val navView = binding.bottomNavigationView
        navView.setupWithNavController(navController)

        val charactersRepository = CharactersRepository(CharacterDatabase(this))
        val viewModelProviderFactory =
            CharactersViewModelProviderFactory(repository = charactersRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(CharactersViewModel::class.java)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

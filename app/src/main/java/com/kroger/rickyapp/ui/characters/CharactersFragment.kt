package com.kroger.rickyapp.ui.characters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kroger.rickyapp.MainActivity
import com.kroger.rickyapp.R
import com.kroger.rickyapp.databinding.FragmentCharactersBinding
import com.kroger.rickyapp.models.Character
import com.kroger.rickyapp.models.CharacterResponse
import com.kroger.rickyapp.ui.details.DetailsFragment
import com.kroger.rickyapp.util.Resource

class CharactersFragment :
    Fragment(),
    CharactersAdapter.CharacterAdapterListener {

    /*
    We don't want to litter our code with '?' for null safety
    If we're certain the value won't be null after it is created, we can append '!!'
     */
    // get() = Cannot be assigned to something else (get-only)
    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!
    private lateinit var charactersAdapter: CharactersAdapter
    private var isLinearLayoutManager = false

    // Kotlin property delegate
    // delegates the responsibility of this viewModel object to the viewModels class
    private lateinit var viewModel: CharactersViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // If the viewModel were to be coming from the MainActivity, we access it like this
        // viewModel = (activity as MainActivity).viewModel

        viewModel =
            ViewModelProvider(
                this,
                (activity as MainActivity).viewModelProviderFactory
            ).get(CharactersViewModel::class.java)
        setupRecyclerView()

        viewModel.charactersList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> handleLoading(true)
                is Resource.Success -> response.data?.let {
                    displayContent(it)
                }
                is Resource.Error -> response.message?.let {
                    handleError(it)
                }
            }
        }
    }

    private fun handleError(message: String) {
        Log.e(FRAGMENT_TAG, "An error occurred : $message")
    }

    private fun displayContent(response: CharacterResponse) {
        handleLoading(false)
        charactersAdapter.differ.submitList(response.results)
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun setupRecyclerView() {
        charactersAdapter = CharactersAdapter(this)
        binding.rvCharacters.apply {
            adapter = charactersAdapter
            layoutManager = if (isLinearLayoutManager) {
                LinearLayoutManager(context)
            } else {
                GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCharacterItemClicked(character: Character) {
        activity?.supportFragmentManager?.commit {
            replace(
                R.id.fragment_container,
                DetailsFragment.newInstance(),
                DetailsFragment.FRAGMENT_TAG
            )
            setReorderingAllowed(true)
            addToBackStack("")
        }
    }

    companion object {
        const val FRAGMENT_TAG: String = "CharactersFragment"
        fun newInstance(): CharactersFragment = CharactersFragment()
    }
}

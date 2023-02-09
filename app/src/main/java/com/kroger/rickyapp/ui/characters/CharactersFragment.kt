package com.kroger.rickyapp.ui.characters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kroger.rickyapp.MainActivity
import com.kroger.rickyapp.databinding.FragmentCharactersBinding
import com.kroger.rickyapp.models.CharacterResponse
import com.kroger.rickyapp.util.Resource
import kotlinx.coroutines.launch

class CharactersFragment : Fragment() {

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
    val viewModel2: CharactersViewModel by viewModels()

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
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        lifecycleScope.launch {
        }

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
        Log.e(TAG, "An error occurred : $message")
    }

    private fun displayContent(response: CharacterResponse) {
        handleLoading(false)
        charactersAdapter.differ.submitList(response.results)
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun setupRecyclerView() {
        charactersAdapter = CharactersAdapter()
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

    companion object {
        private const val TAG = "CharactersFragment"
    }
}

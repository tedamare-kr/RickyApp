package com.kroger.rickyapp.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kroger.rickyapp.R
import com.kroger.rickyapp.databinding.FragmentSearchBinding
import com.kroger.rickyapp.models.Character
import com.kroger.rickyapp.models.CharacterResponse
import com.kroger.rickyapp.ui.characters.CharactersAdapter
import com.kroger.rickyapp.ui.characters.CharactersViewModel
import com.kroger.rickyapp.ui.details.CharactersDetailsFragment
import com.kroger.rickyapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), CharactersAdapter.CharacterAdapterListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var charactersAdapter: CharactersAdapter
    private val viewModel: CharactersViewModel by viewModels()
    private var isLinearLayoutManager = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        var job: Job? = null
        binding.searchBar.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)

                // If editable is not null
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchCharacters(editable.toString())
                    }
                }
            }
        }

        viewModel.searchResults.observe(viewLifecycleOwner) { response ->
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
        binding.rvSearch.apply {
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
        const val FRAGMENT_TAG: String = "SearchFragment"
        fun newInstance(): SearchFragment = SearchFragment()
    }

    override fun onCharacterItemClicked(character: Character) {
        activity?.supportFragmentManager?.commit {
            replace(
                R.id.fragment_container,
                CharactersDetailsFragment.newInstance(character),
                CharactersDetailsFragment.FRAGMENT_TAG
            )
            setReorderingAllowed(true)
            addToBackStack("")
        }
    }
}

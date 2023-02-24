package com.kroger.rickyapp.ui.characters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.kroger.rickyapp.R
import com.kroger.rickyapp.databinding.FragmentCharactersBinding
import com.kroger.rickyapp.models.Character
import com.kroger.rickyapp.models.CharacterResponse
import com.kroger.rickyapp.ui.details.DetailsFragment
import com.kroger.rickyapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
    private var isLinearLayoutManager = true
    private val viewModel: CharactersViewModel by viewModels()

    // Kotlin property delegate
    // delegates the responsibility of this viewModel object to the viewModels class
//    private var viewModel: CharactersViewModel by

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

        // Connect the SlidingPaneLayout to the system back button.
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            CharactersListOnBackPressedCallback(binding.slidingPaneLayout)
        )

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
//        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
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
        childFragmentManager.commit {
            replace(
                R.id.detail_container,
                DetailsFragment.newInstance(character),
                DetailsFragment.FRAGMENT_TAG
            )
            setReorderingAllowed(true)
            // If we're already open and the detail pane is visible,
            // crossfade between the fragments.
            if (binding.slidingPaneLayout.isOpen) {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            }
        }
        binding.slidingPaneLayout.open()
    }

    companion object {
        const val FRAGMENT_TAG: String = "CharactersFragment"
        fun newInstance(): CharactersFragment = CharactersFragment()
    }
}

/**
 * Call back is only enabled on smaller screen devices (slidingPaneLayout.isSlideable)
 * and
 * when the content pane is open (slidingPaneLayout.isOpen)
 */

class CharactersListOnBackPressedCallback(
    private val slidingPaneLayout: SlidingPaneLayout
) : OnBackPressedCallback(slidingPaneLayout.isSlideable && slidingPaneLayout.isOpen),
    SlidingPaneLayout.PanelSlideListener {

    init {
        slidingPaneLayout.addPanelSlideListener(this)
    }

    override fun handleOnBackPressed() {
        slidingPaneLayout.closePane()
    }

    override fun onPanelSlide(panel: View, slideOffset: Float) {
    }

    override fun onPanelOpened(panel: View) {
        isEnabled = true
    }

    override fun onPanelClosed(panel: View) {
        isEnabled = false
    }
}

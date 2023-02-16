package com.kroger.rickyapp.ui.favorites

import android.os.Bundle
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
import com.kroger.rickyapp.databinding.FragmentFavoritesBinding
import com.kroger.rickyapp.models.Character
import com.kroger.rickyapp.ui.characters.CharactersAdapter
import com.kroger.rickyapp.ui.characters.CharactersViewModel
import com.kroger.rickyapp.ui.details.DetailsFragment

class FavoritesFragment : Fragment(), CharactersAdapter.CharacterAdapterListener {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var charactersAdapter: CharactersAdapter
    private var isLinearLayoutManager = false
    private lateinit var viewModel: CharactersViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel =
            ViewModelProvider(
                this,
                (activity as MainActivity).viewModelProviderFactory
            ).get(CharactersViewModel::class.java)

        setupRecyclerView()

        viewModel.getFavorites().observe(viewLifecycleOwner) {
            charactersAdapter.differ.submitList(it)
        }
    }

    private fun setupRecyclerView() {
        charactersAdapter = CharactersAdapter(this)
        binding.rvFavorites.apply {
            adapter = charactersAdapter
            layoutManager = if (isLinearLayoutManager) {
                LinearLayoutManager(context)
            } else {
                GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            }
        }
    }

    companion object {
        const val FRAGMENT_TAG: String = "FavoritesFragment"
        fun newInstance(): FavoritesFragment = FavoritesFragment()
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
}

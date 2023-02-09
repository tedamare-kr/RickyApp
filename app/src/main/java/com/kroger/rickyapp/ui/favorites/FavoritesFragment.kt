package com.kroger.rickyapp.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kroger.rickyapp.MainActivity
import com.kroger.rickyapp.R
import com.kroger.rickyapp.databinding.FragmentFavoritesBinding
import com.kroger.rickyapp.ui.characters.CharactersAdapter
import com.kroger.rickyapp.ui.characters.CharactersViewModel
import kotlinx.coroutines.flow.observeOn

class FavoritesFragment : Fragment() {

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
        viewModel = (activity as MainActivity).viewModel

        setupRecyclerView()

        viewModel.getFavorites().observe(viewLifecycleOwner) {
            charactersAdapter.differ.submitList(it)
        }
    }

    private fun setupRecyclerView() {
        charactersAdapter = CharactersAdapter()
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
        private const val TAG = "FavoritesFragment"
    }
}

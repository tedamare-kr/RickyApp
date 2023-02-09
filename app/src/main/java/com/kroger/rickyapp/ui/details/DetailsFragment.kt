package com.kroger.rickyapp.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.kroger.rickyapp.MainActivity
import com.kroger.rickyapp.databinding.FragmentDetailsBinding
import com.kroger.rickyapp.ui.characters.CharactersViewModel

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: DetailsFragmentArgs by navArgs()

    private lateinit var viewModel: CharactersViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val character = args.character

        viewModel = (activity as MainActivity).viewModel

        binding.fab.setOnClickListener {
            viewModel.favoriteCharacter(character)
            Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
        }
        Glide
            .with(binding.root)
            .load(character.image)
            .centerCrop()
            .into(binding.charImage)
        binding.characterName.text = character.name
        binding.characterStatus.text = character.status
        binding.characterSpecies.text = character.species
        binding.characterGender.text = character.gender
        binding.characterCreated.text = character.created
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

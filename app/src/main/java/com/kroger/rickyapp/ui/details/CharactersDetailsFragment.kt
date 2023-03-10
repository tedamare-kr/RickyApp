package com.kroger.rickyapp.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.kroger.rickyapp.databinding.FragmentDetailsBinding
import com.kroger.rickyapp.models.Character
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharactersDetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharactersDetailViewModel by viewModels()

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

        this.arguments?.getParcelable<Character>(CHARACTER)?.let { character ->
            Glide
                .with(binding.root)
                .load(character.image)
                .centerCrop()
                .into(binding.characterImage)
            binding.characterName.text = character.name
            binding.characterStatus.text = character.status
            binding.characterSpecies.text = character.species
            binding.characterGender.text = character.gender
            binding.characterCreated.text = character.created

            binding.fab.setOnClickListener {
                viewModel.favoriteCharacter(character)
                Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val FRAGMENT_TAG: String = "DetailsFragment"
        private val CHARACTER = "character"

        fun newInstance(character: Character): CharactersDetailsFragment =
            CharactersDetailsFragment().also {
                it.arguments = Bundle().also { bundle ->
                    bundle.putParcelable(CHARACTER, character)
                }
            }
    }
}

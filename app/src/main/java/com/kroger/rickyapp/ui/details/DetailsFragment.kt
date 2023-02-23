package com.kroger.rickyapp.ui.details

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.kroger.rickyapp.MainActivity
import com.kroger.rickyapp.R
import com.kroger.rickyapp.databinding.FragmentDetailsBinding
import com.kroger.rickyapp.models.Character
import com.kroger.rickyapp.ui.characters.CharactersViewModel

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

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

        viewModel =
            ViewModelProvider(
                this,
                (activity as MainActivity).viewModelProviderFactory
            ).get(CharactersViewModel::class.java)
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

        fun newInstance(character: Character): DetailsFragment = DetailsFragment().also {
            it.arguments = Bundle().also { bundle ->
                bundle.putParcelable(CHARACTER, character)
            }
        }
    }
}

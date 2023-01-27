package com.kroger.rickyapp.ui.characters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.R
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kroger.rickyapp.databinding.FragmentCharactersBinding
import com.kroger.rickyapp.models.Character
import com.kroger.rickyapp.models.CharacterResponse
import com.kroger.rickyapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharactersFragment : Fragment(), CharactersAdapter.CharacterItemListener {

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!
    private lateinit var charactersAdapter: CharactersAdapter

    private lateinit var characters: CharacterResponse

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

        val response = RetrofitClient.rickandmortyService.getAllCharacters()
        response.enqueue(object : Callback<CharacterResponse> {
            override fun onResponse(call: Call<CharacterResponse>, response: Response<CharacterResponse>) {
                if (response.isSuccessful) {
                    Log.i(TAG, "successful response: ${response.body()}")
                    val result = response.body()?.results
                    result?.let {
                        setRecyclerView(it)
                    }
                }
            }

            override fun onFailure(call: Call<CharacterResponse>, t: Throwable) {
                Log.e(TAG, "Failure: ${t.message}")
            }
        })
    }

    private fun setRecyclerView(characters: List<Character>) {
        charactersAdapter = CharactersAdapter(characters, this)
        binding.rvCharacters.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = charactersAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "CharactersFragment"
        fun newInstance(): CharactersFragment = CharactersFragment()
    }

    override fun onCharacterClicked(characterId: Int) {
        findNavController().navigate(
            com.kroger.rickyapp.R.id.action_charactersFragment_to_detailsFragment,
            bundleOf("id" to characterId)
        )
    }
}

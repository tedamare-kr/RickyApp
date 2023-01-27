package com.kroger.rickyapp.ui.characters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kroger.rickyapp.databinding.ItemCharacterBinding
import com.kroger.rickyapp.models.Character

class CharactersAdapter(
    private val characters: List<Character>,
    private val listener: CharacterItemListener
) :
    RecyclerView.Adapter<CharacterViewHolder>() {

    interface CharacterItemListener {
        fun onCharacterClicked(characterId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener = listener
        )
    }

    // Bind the list items to a view
    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(characters[position])
    }

    override fun getItemCount(): Int =
        characters.size
}

class CharacterViewHolder(
    private val itemBinding: ItemCharacterBinding,
    private val listener: CharactersAdapter.CharacterItemListener
) :
    RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {

    private lateinit var character: Character

    init {
        itemBinding.charDetail.setOnClickListener(this)
    }

    fun bind(character: Character) {
        this.character = character
        itemBinding.charName.text = character.name
        Glide
            .with(itemBinding.root)
            .load(character.image)
            .centerCrop()
            .into(itemBinding.charImage)
    }

    override fun onClick(v: View?) {
        listener.onCharacterClicked(characterId = character.id)
    }
}

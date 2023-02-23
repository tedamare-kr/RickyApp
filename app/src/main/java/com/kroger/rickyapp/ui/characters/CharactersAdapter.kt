package com.kroger.rickyapp.ui.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kroger.rickyapp.databinding.ItemCharacterBinding
import com.kroger.rickyapp.models.Character

class CharactersAdapter(private val characterAdapterListener: CharacterAdapterListener) : RecyclerView.Adapter<CharacterViewHolder>() {

    interface CharacterAdapterListener {
        fun onCharacterItemClicked(character: Character)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            // If two articles have the same ID (url in this case), then they must be the same item
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            characterAdapterListener
        )
    }

    // Bind the list items to a view
    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = differ.currentList[position]
        holder.bind(character)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}

class CharacterViewHolder(
    private val itemBinding: ItemCharacterBinding,
    val characterAdapterListener: CharactersAdapter.CharacterAdapterListener
) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(character: Character) {
        itemBinding.charName.text = character.name
        itemBinding.characterStatus.text = character.status
        Glide
            .with(itemBinding.root)
            .load(character.image)
            .centerCrop()
            .into(itemBinding.charImage)
        itemBinding.root.setOnClickListener {
            characterAdapterListener.onCharacterItemClicked(character)
        }
    }
}

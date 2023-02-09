package com.kroger.rickyapp.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kroger.rickyapp.R
import com.kroger.rickyapp.databinding.ItemCharacterBinding
import com.kroger.rickyapp.models.Character

class CharactersAdapter : RecyclerView.Adapter<CharacterViewHolder>() {

    private var onItemClickListener: ((Character) -> Unit)? = null

    fun setOnItemClickListener(listener: (Character) -> Unit) {
        onItemClickListener = listener
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
            )
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
    private val itemBinding: ItemCharacterBinding
) : RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var character: Character

    fun bind(character: Character) {
        this.character = character
        itemBinding.charName.text = character.name
        Glide
            .with(itemBinding.root)
            .load(character.image)
            .centerCrop()
            .into(itemBinding.charImage)
        itemBinding.charDetail.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("character", character)
            }
            itemView.findNavController().navigate(
                R.id.action_charactersFragment_to_detailsFragment,
                bundle
            )
        }
    }
}

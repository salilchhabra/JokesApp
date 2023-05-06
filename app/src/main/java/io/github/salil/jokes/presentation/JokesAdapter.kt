package io.github.salil.jokes.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.salil.jokes.databinding.ItemJokeBinding

class JokesAdapter : ListAdapter<String, JokesAdapter.JokeViewHolder>(DiffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemJokeBinding.inflate(inflater, parent, false)

        return JokeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class JokeViewHolder(private val binding: ItemJokeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            with(binding) {
               tvTitle.text = item
            }
        }
    }

    companion object DiffUtilCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}
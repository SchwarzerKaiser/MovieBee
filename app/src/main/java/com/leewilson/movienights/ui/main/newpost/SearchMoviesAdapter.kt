package com.leewilson.movienights.ui.main.newpost

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.leewilson.movienights.R
import com.leewilson.movienights.model.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie.view.*

class SearchMoviesAdapter(
    private val interaction: Interaction? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onBottomReachedListener: (() -> Unit)? = null

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {

        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.imdbId == newItem.imdbId
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_movie,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
        if (position >= differ.currentList.size - 1) {
            onBottomReachedListener?.invoke()
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun addOnBottomReachedListener(callback: () -> Unit) {
        onBottomReachedListener = callback
    }

    fun submitList(list: List<Movie>) {
        differ.submitList(list)
    }

    class MovieViewHolder constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Movie): Unit = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            movieTitle.text = item.title
            movieYear.text = item.year
            val picasso = Picasso.get()
            picasso.isLoggingEnabled = true
            picasso.load(item.posterUrl)
                .error(R.drawable.movie_placeholder)
                .placeholder(R.drawable.movie_placeholder)
                .into(moviePoster)
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Movie)
    }
}
package com.leewilson.movienights.ui.main.feed

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.leewilson.movienights.R
import com.leewilson.movienights.databinding.ItemMovienightBinding
import com.leewilson.movienights.model.MovieNight
import com.squareup.picasso.Picasso

class FeedAdapter(
    differCallback: DiffUtil.ItemCallback<MovieNight>
) : PagingDataAdapter<MovieNight, FeedAdapter.MovieNightViewHolder>(differCallback) {

    private val TAG = "FeedAdapter"

    inner class MovieNightViewHolder(
        val binding: ItemMovienightBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movieNight: MovieNight) {
            Log.d(TAG, "bind: $movieNight")
            Picasso.get()
                .load(movieNight.imageUrl)
                .placeholder(R.drawable.movie_placeholder)
                .resize(300, 300)
                .centerCrop(Gravity.START)
                .error(R.drawable.movie_placeholder)
                .into(binding.poster)
        }
    }

    override fun onBindViewHolder(holder: MovieNightViewHolder, position: Int) {
        getItem(position)?.let { movieNight ->
            holder.bind(movieNight)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieNightViewHolder {
        val binding = ItemMovienightBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MovieNightViewHolder(binding)
    }
}
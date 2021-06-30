package com.leewilson.moviebee.ui.main.feed

import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.leewilson.moviebee.R
import com.leewilson.moviebee.databinding.ItemMovienightBinding
import com.leewilson.moviebee.model.MovieNight
import com.leewilson.moviebee.util.toFormattedString
import com.squareup.picasso.Picasso
import java.util.*

class FeedAdapter(
    differCallback: DiffUtil.ItemCallback<MovieNight>,
    private val interaction: Interaction,
    private val userId: String
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
                .error(R.drawable.movie_placeholder)
                .fit()
                .centerCrop()
                .into(binding.poster)

            val context = binding.root.context
            val hostingMsg = context.resources.getString(R.string.movie_night_hosting, movieNight.hostName)
            val formattedHostingMsg = Html.fromHtml(hostingMsg)
            binding.tvPersonHosting.setText(formattedHostingMsg)
            binding.movieName.text = movieNight.movieName

            val numGuestsString = if (movieNight.guestUids.size == 1)
                context.resources.getString(R.string.movie_night_num_guests_singular)
                else context.resources.getString(R.string.movie_night_num_guests_plural, movieNight.guestUids.size)
            binding.numberGuestsTv.text = numGuestsString

            movieNight.dateOfEvent?.let { timeStamp ->
                Log.d(TAG, "timestamp: $timeStamp")
                val date = Date(timeStamp.seconds * 1000)
                binding.dateTv.text = date.toFormattedString()
            }

            fun changeIcon(like: Boolean) {
                binding.likeIcon.setImageResource(
                    if (like) R.drawable.ic_liked
                        else R.drawable.ic_baseline_thumb_up_24
                )
            }

            binding.likeIcon.setOnClickListener {
                movieNight.likeUids?.let { likes ->
                    if (!likes.contains(userId)) {
                        // like
                        interaction.onLike(movieNight)
                        movieNight.likeUids?.add(userId)
                    } else {
                        // unlike
                        interaction.onUnLike(movieNight)
                        movieNight.likeUids?.remove(userId)
                    }
                }

                movieNight.likeUids?.let { likes ->
                    if (likes.contains(userId)) {
                        // like
                        changeIcon(true)
                    } else {
                        // unlike
                        changeIcon(false)
                    }
                    binding.numLikes.text = if (likes.size > 0)
                        likes.size.toString() else ""
                }
            }

            movieNight.likeUids?.let { likerIds ->
                if (likerIds.isNotEmpty()) {
                    binding.numLikes.text = likerIds.size.toString()
                }
                if (likerIds.contains(userId)) {
                    changeIcon(true)
                } else changeIcon(false)
            }
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

    interface Interaction {
        fun onLike(movieNight: MovieNight)
        fun onUnLike(movieNight: MovieNight)
    }
}
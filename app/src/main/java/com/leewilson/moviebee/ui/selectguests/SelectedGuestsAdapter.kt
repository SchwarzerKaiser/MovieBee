package com.leewilson.moviebee.ui.selectguests

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.leewilson.moviebee.R
import com.leewilson.moviebee.model.FollowUser
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class SelectedGuestsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FollowUser>() {
        override fun areItemsTheSame(oldItem: FollowUser, newItem: FollowUser): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: FollowUser, newItem: FollowUser): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SelectedGuestsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_guest_image,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SelectedGuestsViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<FollowUser>) {
        differ.submitList(list)
    }

    class SelectedGuestsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: FollowUser) {
            val profileImageView = itemView.findViewById<CircleImageView>(R.id.profileImageView)
            Picasso.get().load(item.imageUrl).into(profileImageView)
        }
    }
}
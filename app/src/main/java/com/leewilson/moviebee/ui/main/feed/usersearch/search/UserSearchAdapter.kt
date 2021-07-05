package com.leewilson.moviebee.ui.main.feed.usersearch.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.leewilson.moviebee.R
import com.leewilson.moviebee.databinding.ItemUserNocheckboxBinding
import com.leewilson.moviebee.model.FollowUser
import com.squareup.picasso.Picasso

class UserSearchAdapter(
    private val interaction: Interaction
) : RecyclerView.Adapter<UserSearchAdapter.UserViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FollowUser>() {
        override fun areItemsTheSame(oldItem: FollowUser, newItem: FollowUser): Boolean {
            return oldItem.uid == newItem.uid
        }
        override fun areContentsTheSame(oldItem: FollowUser, newItem: FollowUser): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            ItemUserNocheckboxBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(differ.currentList[position], position)
    }

    override fun getItemCount(): Int = differ.currentList.size

    class UserViewHolder(
        private val binding: ItemUserNocheckboxBinding,
        private val interaction: Interaction
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: FollowUser, position: Int) = with(itemView) {
            binding.userName.text = user.displayName
            user.imageUrl?.let { url ->
                Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.default_profile_img)
                    .into(binding.userImageView)
            }
            binding.root.setOnClickListener {
                interaction.onUserSelected(user, position)
            }
        }
    }

    fun submitList(list: List<FollowUser>) {
        differ.submitList(list)
    }

    interface Interaction {
        fun onUserSelected(user: FollowUser, position: Int)
    }
}
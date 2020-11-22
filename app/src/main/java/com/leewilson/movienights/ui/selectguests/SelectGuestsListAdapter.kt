package com.leewilson.movienights.ui.selectguests

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.leewilson.movienights.R
import com.leewilson.movienights.model.FollowUser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_user.view.*

class SelectGuestsListAdapter(private val interaction: Interaction) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val picasso: Picasso by lazy { Picasso.get() }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FollowUser>() {

        override fun areItemsTheSame(oldItem: FollowUser, newItem: FollowUser): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: FollowUser, newItem: FollowUser): Boolean {
            return oldItem.uid == newItem.uid
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return SelectGuestsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_user,
                parent,
                false
            ),
            interaction,
            picasso
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SelectGuestsViewHolder -> {
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

    class SelectGuestsViewHolder(
        itemView: View,
        private val interaction: Interaction,
        private val picasso: Picasso
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: FollowUser) = with(itemView) {
            userName.text = item.displayName
            picasso.load(item.imageUrl)
                .error(R.drawable.default_profile_img)
                .placeholder(R.drawable.default_profile_img)
                .into(userImageView)
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    interaction.onSelected(item)
                } else interaction.onDeselected(item)
            }
        }
    }

    interface Interaction {
        fun onSelected(user: FollowUser)
        fun onDeselected(user: FollowUser)
    }
}
package com.sdk.realtimedatabase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sdk.realtimedatabase.model.User
import com.sdk.realtimedatabase.databinding.UserLayoutBinding

class UserAdapter(
    private val onItemClickListener: OnItemClickListener
) : ListAdapter<User, UserAdapter.UserViewHolder>(DiffCallBack()) {
    private class DiffCallBack : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            UserLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class UserViewHolder(private val binding: UserLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                Glide.with(imageView)
                    .load(user.photoUri)
                    .circleCrop()
                    .into(imageView)
                textView.text = user.name
            }
            itemView.setOnClickListener {
                onItemClickListener.onClick(user)
            }
        }
    }
}
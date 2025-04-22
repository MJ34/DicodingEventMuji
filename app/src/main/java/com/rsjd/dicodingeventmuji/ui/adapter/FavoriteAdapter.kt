package com.rsjd.dicodingeventmuji.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rsjd.dicodingeventmuji.data.local.entity.FavoriteEvent
import com.rsjd.dicodingeventmuji.databinding.ItemEventBinding
import com.rsjd.dicodingeventmuji.utils.DateFormatter

class FavoriteAdapter : ListAdapter<FavoriteEvent, FavoriteAdapter.FavoriteViewHolder>(DIFF_CALLBACK) {

    private var onItemClickListener: ((FavoriteEvent) -> Unit)? = null

    fun setOnItemClickListener(listener: (FavoriteEvent) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favorite = getItem(position)
        holder.bind(favorite)
    }

    inner class FavoriteViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: FavoriteEvent) {
            binding.apply {
                tvEventName.text = favorite.name
                tvEventSummary.text = favorite.summary
                tvEventDate.text = DateFormatter.formatDate(favorite.beginTime)

                Glide.with(itemView.context)
                    .load(favorite.imageLogo)
                    .into(ivEventLogo)

                root.setOnClickListener {
                    onItemClickListener?.invoke(favorite)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteEvent>() {
            override fun areItemsTheSame(oldItem: FavoriteEvent, newItem: FavoriteEvent): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FavoriteEvent, newItem: FavoriteEvent): Boolean {
                return oldItem == newItem
            }
        }
    }
}
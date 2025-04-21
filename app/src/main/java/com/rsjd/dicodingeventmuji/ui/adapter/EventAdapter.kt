package com.rsjd.dicodingeventmuji.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rsjd.dicodingeventmuji.data.model.Event
import com.rsjd.dicodingeventmuji.databinding.ItemEventBinding
import com.rsjd.dicodingeventmuji.utils.DateFormatter

class EventAdapter : ListAdapter<Event, EventAdapter.EventViewHolder>(DIFF_CALLBACK) {

    private var onItemClickListener: ((Event) -> Unit)? = null

    fun setOnItemClickListener(listener: (Event) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    inner class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.apply {
                tvEventName.text = event.name
                tvEventSummary.text = event.summary

                // Format date
                tvEventDate.text = DateFormatter.formatDate(event.beginTime)

                // Load image with Glide
                Glide.with(itemView.context)
                    .load(event.imageLogo)
                    .into(ivEventLogo)

                // Set click listener
                root.setOnClickListener {
                    onItemClickListener?.invoke(event)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
                return oldItem == newItem
            }
        }
    }
}
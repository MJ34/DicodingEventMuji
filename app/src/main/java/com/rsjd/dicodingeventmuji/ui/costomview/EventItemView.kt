package com.rsjd.dicodingeventmuji.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.rsjd.dicodingeventmuji.R
import com.rsjd.dicodingeventmuji.data.model.Event
import com.rsjd.dicodingeventmuji.databinding.CustomEventItemViewBinding
import com.rsjd.dicodingeventmuji.utils.DateFormatter
import com.rsjd.dicodingeventmuji.utils.loadImage

/**
 * Custom view for displaying event item with consistent styling
 */
class EventItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: CustomEventItemViewBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = CustomEventItemViewBinding.inflate(inflater, this, true)

        // Get attributes from XML if any
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.EventItemView,
            defStyleAttr,
            0
        )

        // Apply custom styling based on attributes
        val showQuota = typedArray.getBoolean(R.styleable.EventItemView_showQuota, true)
        binding.tvQuota.visibility = if (showQuota) VISIBLE else GONE

        typedArray.recycle()
    }

    /**
     * Bind event data to the view
     * @param event The event data to display
     * @param onClick Click listener for the event item
     */
    fun bind(event: Event, onClick: (Event) -> Unit = {}) {
        with(binding) {
            tvEventName.text = event.name
            tvOrganizer.text = event.ownerName
            tvDate.text = DateFormatter.formatDate(event.beginTime)

            // Set quota text
            val quotaText = "${event.quota - event.registrant} / ${event.quota}"
            tvQuota.text = quotaText

            // Load event image
            val imageUrl = event.imageLogo ?: event.mediaCover
            ivEventImage.loadImage(imageUrl)

            // Add status indicator if needed
            statusIndicator.setBackgroundResource(
                if (event.isActive) R.drawable.indicator_active
                else R.drawable.indicator_finished
            )

            // Set click listener
            root.setOnClickListener { onClick(event) }
        }
    }
}
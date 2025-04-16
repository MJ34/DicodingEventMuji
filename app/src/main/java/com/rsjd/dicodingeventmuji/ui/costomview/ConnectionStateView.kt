package com.rsjd.dicodingeventmuji.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.rsjd.dicodingeventmuji.R
import com.rsjd.dicodingeventmuji.databinding.ViewConnectionStateBinding
import com.rsjd.dicodingeventmuji.utils.AnimationUtils
import com.rsjd.dicodingeventmuji.utils.ConnectivityObserver

/**
 * Custom view to display network connection state
 */
class ConnectionStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ViewConnectionStateBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = ViewConnectionStateBinding.inflate(inflater, this, true)
        visibility = View.GONE
    }

    /**
     * Update the connection state UI
     * @param state Network connection state
     */
    fun updateConnectionState(state: ConnectivityObserver.Status) {
        when (state) {
            ConnectivityObserver.Status.AVAILABLE -> {
                binding.connectionContainer.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.connection_available)
                )
                binding.tvConnectionStatus.text = context.getString(R.string.connection_restored)
                // Show briefly then hide
                AnimationUtils.fadeIn(this)
                postDelayed({ AnimationUtils.fadeOut(this) }, 3000)
            }
            ConnectivityObserver.Status.UNAVAILABLE,
            ConnectivityObserver.Status.LOST -> {
                binding.connectionContainer.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.connection_lost)
                )
                binding.tvConnectionStatus.text = context.getString(R.string.connection_lost)
                // Show and keep visible
                AnimationUtils.fadeIn(this)
            }
            ConnectivityObserver.Status.LOSING -> {
                binding.connectionContainer.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.connection_losing)
                )
                binding.tvConnectionStatus.text = context.getString(R.string.connection_weak)
                // Show and keep visible
                AnimationUtils.fadeIn(this)
            }
        }
    }
}
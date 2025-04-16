package com.rsjd.dicodingeventmuji.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.rsjd.dicodingeventmuji.R
import java.text.NumberFormat
import java.util.Locale

/**
 * Extension function to show Toast message
 */
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/**
 * Extension function to show long duration Toast message
 */
fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

/**
 * Extension function to load image with Glide
 */
fun ImageView.loadImage(url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(this.context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.placeholder_image)
            .into(this)
    } else {
        this.setImageResource(R.drawable.placeholder_image)
    }
}

/**
 * Extension function to set view visibility
 */
fun View.setVisible() {
    this.visibility = View.VISIBLE
}

/**
 * Extension function to set view gone
 */
fun View.setGone() {
    this.visibility = View.GONE
}

/**
 * Extension function to set view invisible
 */
fun View.setInvisible() {
    this.visibility = View.INVISIBLE
}

/**
 * Extension function to format number to readable format
 */
fun Int.formatNumber(): String {
    return NumberFormat.getNumberInstance(Locale("id", "ID")).format(this)
}
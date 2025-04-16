@file:Suppress("DEPRECATION")

package com.rsjd.dicodingeventmuji.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.rsjd.dicodingeventmuji.R

/**
 * Utility class for showing custom toasts
 */
object ToastUtils {

    /**
     * Toast types with corresponding icons and background colors
     */
    enum class Type {
        SUCCESS, ERROR, INFO, WARNING
    }

    /**
     * Show a custom toast with a specific type
     * @param context Application context
     * @param message The message to display
     * @param type The type of toast (SUCCESS, ERROR, INFO, WARNING)
     * @param duration Toast.LENGTH_SHORT or Toast.LENGTH_LONG
     */
    fun showCustomToast(
        context: Context,
        message: String,
        type: Type = Type.INFO,
        duration: Int = Toast.LENGTH_SHORT
    ) {
        val inflater = LayoutInflater.from(context)
        val layout: View = inflater.inflate(R.layout.custom_toast, null)

        // Set icon and background based on type
        val icon = layout.findViewById<ImageView>(R.id.toast_icon)
        val container = layout.findViewById<View>(R.id.toast_container)
        val textView = layout.findViewById<TextView>(R.id.toast_text)

        when (type) {
            Type.SUCCESS -> {
                icon.setImageResource(R.drawable.ic_success)
                container.setBackgroundResource(R.drawable.bg_toast_success)
            }
            Type.ERROR -> {
                icon.setImageResource(R.drawable.ic_error)
                container.setBackgroundResource(R.drawable.bg_toast_error)
            }
            Type.INFO -> {
                icon.setImageResource(R.drawable.ic_info)
                container.setBackgroundResource(R.drawable.bg_toast_info)
            }
            Type.WARNING -> {
                icon.setImageResource(R.drawable.ic_warning)
                container.setBackgroundResource(R.drawable.bg_toast_warning)
            }
        }

        textView.text = message

        val toast = Toast(context)
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 64)
        toast.duration = duration
        toast.view = layout
        toast.show()
    }

    /**
     * Show a success toast
     */
    fun showSuccess(context: Context, message: String) {
        showCustomToast(context, message, Type.SUCCESS)
    }

    /**
     * Show an error toast
     */
    fun showError(context: Context, message: String) {
        showCustomToast(context, message, Type.ERROR)
    }

    /**
     * Show an info toast
     */
    fun showInfo(context: Context, message: String) {
        showCustomToast(context, message, Type.INFO)
    }

    /**
     * Show a warning toast
     */
    fun showWarning(context: Context, message: String) {
        showCustomToast(context, message, Type.WARNING)
    }
}
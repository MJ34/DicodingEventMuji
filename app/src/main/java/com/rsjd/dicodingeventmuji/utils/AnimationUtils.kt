package com.rsjd.dicodingeventmuji.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.core.view.isVisible

/**
 * Utility class for custom animations
 */
object AnimationUtils {

    /**
     * Fade in animation for a view
     * @param view The view to animate
     * @param duration Animation duration in milliseconds
     */
    fun fadeIn(view: View, duration: Long = 300) {
        view.alpha = 0f
        view.visibility = View.VISIBLE
        view.animate()
            .alpha(1f)
            .setDuration(duration)
            .setListener(null)
    }

    /**
     * Fade out animation for a view
     * @param view The view to animate
     * @param duration Animation duration in milliseconds
     */
    fun fadeOut(view: View, duration: Long = 300) {
        view.animate()
            .alpha(0f)
            .setDuration(duration)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.GONE
                }
            })
    }

    /**
     * Expand view animation
     * @param view The view to expand
     * @param duration Animation duration in milliseconds
     */
    fun expand(view: View, duration: Int = 300) {
        if (view.isVisible && view.height > 0) return

        val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
            (view.parent as ViewGroup).width,
            View.MeasureSpec.EXACTLY
        )
        val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
            0,
            View.MeasureSpec.UNSPECIFIED
        )
        view.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        val targetHeight = view.measuredHeight

        // Initial height = 0
        view.layoutParams.height = 0
        view.visibility = View.VISIBLE

        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                view.layoutParams.height = if (interpolatedTime == 1f)
                    ViewGroup.LayoutParams.WRAP_CONTENT
                else
                    (targetHeight * interpolatedTime).toInt()
                view.requestLayout()
            }

            override fun willChangeBounds(): Boolean = true
        }

        animation.duration = duration.toLong()
        view.startAnimation(animation)
    }

    /**
     * Collapse view animation
     * @param view The view to collapse
     * @param duration Animation duration in milliseconds
     */
    fun collapse(view: View, duration: Int = 300) {
        if (!view.isVisible) return

        val initialHeight = view.measuredHeight

        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    view.visibility = View.GONE
                } else {
                    view.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    view.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean = true
        }

        animation.duration = duration.toLong()
        view.startAnimation(animation)
    }
}
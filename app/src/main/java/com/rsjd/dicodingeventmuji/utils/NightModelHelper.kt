package com.rsjd.dicodingeventmuji.utils

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

/**
 * Helper class to manage night mode settings
 */
object NightModeHelper {

    /**
     * Mode constants
     */
    object Mode {
        const val AUTO = 0
        const val LIGHT = 1
        const val DARK = 2
    }

    private const val PREF_NIGHT_MODE = "night_mode"

    /**
     * Apply the night mode based on saved preferences
     * @param context Application context
     */
    fun applyNightMode(context: Context) {
        val mode = getNightMode(context)
        setNightMode(mode)
    }

    /**
     * Set the night mode
     * @param mode The night mode to apply
     */
    fun setNightMode(mode: Int) {
        when (mode) {
            Mode.AUTO -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            Mode.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Mode.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    /**
     * Save the night mode setting
     * @param context Application context
     * @param mode The night mode to save
     */
    fun saveNightMode(context: Context, mode: Int) {
        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        editor.putInt(PREF_NIGHT_MODE, mode)
        editor.apply()
    }

    /**
     * Get the saved night mode setting
     * @param context Application context
     * @return The saved night mode
     */
    fun getNightMode(context: Context): Int {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getInt(PREF_NIGHT_MODE, Mode.AUTO)
    }

    /**
     * Check if night mode is currently active
     * @param context Application context
     * @return true if night mode is active, false otherwise
     */
    fun isNightModeActive(context: Context): Boolean {
        return when (context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }
}
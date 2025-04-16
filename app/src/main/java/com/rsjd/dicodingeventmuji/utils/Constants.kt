package com.rsjd.dicodingeventmuji.utils

/**
 * A class that contains app-wide constants
 */
object Constants {
    // API Related
    const val BASE_URL = "https://event-api.dicoding.dev/"
    const val TIMEOUT_CONNECT = 60L // in seconds
    const val TIMEOUT_READ = 60L // in seconds

    // Database Related
    const val DATABASE_NAME = "event_database"
    const val DATABASE_VERSION = 1

    // Preferences
    const val PREF_NAME = "event_app_preferences"
    const val PREF_DARK_MODE = "dark_mode"

    // Pagination
    const val PAGE_SIZE = 20

    // Featured Events Limit
    const val MAX_FEATURED_EVENTS = 5

    // Intent Extra Keys
    const val EXTRA_EVENT_ID = "extra_event_id"

    // Splash Screen Duration
    const val SPLASH_DURATION = 2000L // in milliseconds

    // Search Debounce Delay
    const val SEARCH_DEBOUNCE_TIME = 300L // in milliseconds
}
package com.rsjd.dicodingeventmuji.utils

/**
 * A sealed class to handle states of data loading in the application
 */
sealed class EventResult<out R> {
    /**
     * Loading state - when data is being fetched
     */
    object Loading : EventResult<Nothing>()

    /**
     * Success state - when data is successfully fetched
     * @param data The returned data
     */
    data class Success<out T>(val data: T) : EventResult<T>()

    /**
     * Error state - when there is an error during data fetching
     * @param error Error message
     */
    data class Error(val error: String) : EventResult<Nothing>()
}
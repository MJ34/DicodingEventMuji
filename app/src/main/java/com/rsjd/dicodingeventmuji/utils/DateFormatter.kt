package com.rsjd.dicodingeventmuji.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateFormatter {
    /**
     * Convert date string from API format (ISO 8601) to readable format
     * @param dateString Date string in format "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
     * @return Formatted date string in format "dd MMMM yyyy, HH:mm"
     */
    fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")

            val outputFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("id", "ID"))
            outputFormat.timeZone = TimeZone.getDefault()

            val date = inputFormat.parse(dateString) ?: Date()

            outputFormat.format(date)
        } catch (e: Exception) {
            // In case of parsing error, return the original string
            dateString
        }
    }
}
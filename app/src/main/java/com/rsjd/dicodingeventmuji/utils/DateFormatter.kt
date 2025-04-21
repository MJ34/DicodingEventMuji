package com.rsjd.dicodingeventmuji.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateFormatter {

    fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id"))
        outputFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")

        val date: Date = inputFormat.parse(dateString) ?: Date()
        return outputFormat.format(date)
    }
}
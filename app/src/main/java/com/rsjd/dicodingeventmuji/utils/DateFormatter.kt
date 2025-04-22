package com.rsjd.dicodingeventmuji.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateFormatter {
    fun formatDate(dateString: String?): String {
        if (dateString.isNullOrBlank()) return "Tanggal tidak tersedia"

        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id"))
        outputFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")

        return try {
            val date: Date = inputFormat.parse(dateString) ?: Date()
            outputFormat.format(date)
        } catch (e: Exception) {
            "Tanggal tidak valid"
        }
    }
}
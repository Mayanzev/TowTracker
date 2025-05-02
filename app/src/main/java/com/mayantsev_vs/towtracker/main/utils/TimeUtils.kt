package com.mayantsev_vs.towtracker.main.utils

import android.annotation.SuppressLint
import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
object TimeUtils {
    private val timeFormatter = SimpleDateFormat("HH:mm:ss")
    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
    private val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

    init {
        isoFormatter.timeZone = TimeZone.getTimeZone("GMT+3")

        timeFormatter.timeZone = TimeZone.getDefault()
        dateFormatter.timeZone = TimeZone.getDefault()
    }

    fun getTime(elapsedMillis: Long): String {
        val seconds = elapsedMillis / 1000 % 60
        val minutes = elapsedMillis / (1000 * 60) % 60
        val hours = elapsedMillis / (1000 * 60 * 60)

        return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun getDate(): String {
        val cv = Calendar.getInstance()
        return dateFormatter.format(cv.time)
    }

    fun formatIsoDateToReadable(isoDateString: String): String {
        return try {
            val date = isoFormatter.parse(isoDateString)
            if (date != null) {
                dateFormatter.format(date)
            } else {
                "Invalid date"
            }
        } catch (e: Exception) {
            "Invalid format"
        }
    }
}
package com.mayantsev_vs.towtracker.data.utils

import android.annotation.SuppressLint
import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
object TimeUtils {
    private val timeFormatter = SimpleDateFormat("HH:mm:ss")
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm")

    // converts the given time in milliseconds to a formatted string in UTC time zone
    fun getTime(timeInMillis: Long): String {
        val cv = Calendar.getInstance()
        timeFormatter.timeZone = TimeZone.getTimeZone("UTC")
        cv.timeInMillis = timeInMillis
        return timeFormatter.format(cv.time)
    }

    // gets the current date as a formatted string
    fun getDate(): String {
        val cv = Calendar.getInstance()
        return dateFormatter.format(cv.time)
    }
}
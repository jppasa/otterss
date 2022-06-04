package dev.jpvillegas.otterss.util

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateUtils {
    fun String?.parseDate(): Date? {
        if (this == null) return null

        return SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US)
            .parse(this)
    }

    fun contextualDate(
        date: Date,
        todayStr: String,
        yesterdayStr: String,
        daysAgoStr: String,
    ): String {
        val lastMidnightMillis = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        if (date.time > lastMidnightMillis) {
            return todayStr
        }

        val diffMillis = lastMidnightMillis - date.time
        val daysBetween = TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS)

        if (daysBetween < 2) {
            return yesterdayStr
        }

        if (daysBetween < 7) {
            return "$daysBetween $daysAgoStr"
        }

        if (daysBetween < 365) {
            return SimpleDateFormat("EEEE MMM dd", Locale.US).format(date)
        }

        return SimpleDateFormat("EEEE MMM dd, yyyy", Locale.US).format(date)
    }
}
package dev.jpvillegas.otterss.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun String?.parseDate(): Date? {
        if (this == null) return null

        return SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US)
            .parse(this)
    }
}
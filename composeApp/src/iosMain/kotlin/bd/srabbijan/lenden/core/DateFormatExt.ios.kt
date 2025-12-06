package bd.srabbijan.lenden.core

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.dateWithTimeIntervalSince1970

actual fun formatDateTime(epochMillis: Long, format: String): String {
    val formatter = NSDateFormatter().apply {
        dateFormat = format
        locale = NSLocale("en")
    }
    val date = NSDate.dateWithTimeIntervalSince1970(epochMillis / 1000.0)
    return formatter.stringFromDate(date)
}

actual fun formatDateString(
    dateString: String,
    inputFormat: String,
    outputFormat: String
): String {
    return try {
        val inputFormatter = NSDateFormatter().apply {
            dateFormat = inputFormat
            locale = NSLocale("en")
        }
        val outputFormatter = NSDateFormatter().apply {
            dateFormat = outputFormat
            locale = NSLocale("en")
        }
        val date = inputFormatter.dateFromString(dateString)
        date?.let { outputFormatter.stringFromDate(it) } ?: ""
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}
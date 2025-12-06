package bd.srabbijan.lenden.core

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual fun formatDateTime(epochMillis: Long, format: String): String {
    val sdf = SimpleDateFormat(format, Locale("en"))
    return sdf.format(Date(epochMillis))
}
actual fun formatDateString(
    dateString: String,
    inputFormat: String,
    outputFormat: String
): String {
    return try {
        val inputFormatter = SimpleDateFormat(inputFormat, Locale("en"))
        val outputFormatter = SimpleDateFormat(outputFormat, Locale("en"))
        val date = inputFormatter.parse(dateString)
        date?.let { outputFormatter.format(it) } ?: ""
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}
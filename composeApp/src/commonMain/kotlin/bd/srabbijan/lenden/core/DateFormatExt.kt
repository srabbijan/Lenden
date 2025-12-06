package bd.srabbijan.lenden.core

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


const val FORMAT_dd_MMM_yyyy_hh_mm_ss_aaa = "dd MMMM yyyy hh:mm:ss aaa"
const val FORMAT_yyyy_MM_dd = "yyyy-MM-dd"
const val FORMAT_yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm"
const val FORMAT_dd_MM_yyyy_with_hifen = "dd-MM-yyyy"
const val FORMAT_dd_MM_yyyy_with_bar = "dd/MM/yyyy"
const val FORMAT_dd_MMM = "dd MMM"
const val FORMAT_dd_MMMM_yyyy = "dd MMMM yyyy"
const val FORMAT_dd_MMMM_C_yyyy = "dd MMMM, yyyy"
const val FORMAT_dd_MMM_yy = "dd MMM yy"
const val FORMAT_dd_MMM_yyyy_hh_mm_aaa = "dd MMM yyyy | hh:mm aaa"
const val FORMAT_hh_mm_aaa = "hh:mm aaa" // 12hr format
const val FORMAT_kk_mm = "kk:mm" // 24hr format
const val FORMAT_MMM_yyyy = "MMM yyyy"
const val FORMAT_MMMM_yyyy = "MMMM yyyy"
const val FORMAT_EEE_dd_MMM = "EEE, dd MMM"
const val FORMAT_yyyy_MM_dd_T_HH_mm_ss_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
const val FORMAT_yyyy_MM_dd_T_HH_mm_ss_SSSXXX = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
const val FORMAT_yyyy_MM_dd_T_HH_mm_ss_SSS = "yyyy-MM-dd'T'HH:mm:ss.SSS"
const val FORMAT_yyyy_MM_dd_T_HH_mm_ss = "yyyy-MM-dd'T'HH:mm:ss"
const val FORMAT_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss"
const val FORMAT_MMM_dd_yyyy = "MMM dd yyyy"
const val FORMAT_HH_mm = "HH:mm"

expect fun formatDateTime(epochMillis: Long, format: String): String
expect fun formatDateString(dateString: String, inputFormat: String, outputFormat: String): String

@OptIn(ExperimentalTime::class)
fun getCurrentDateTime(format: String = FORMAT_yyyy_MM_dd_HH_mm_ss): String {
    val now = Clock.System.now().toEpochMilliseconds()
    return formatDateTime(now, format)
}

@OptIn(ExperimentalTime::class)
fun getTodayFirstDateTime(): String {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    return "$today 00:00:00"
}

@OptIn(ExperimentalTime::class)
fun getTodayLastDateTime(): String {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    return "$today 23:59:59"
}

@OptIn(ExperimentalTime::class)
fun getThisMonthFirstDateTime(): String {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val firstDay = today.month
    return "$firstDay 00:00:00"
}

@OptIn(ExperimentalTime::class)
fun getNthDayBeforeDateTime(daysBefore: Int): String {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val nthDayBefore = today.minus(daysBefore, DateTimeUnit.DAY)
    return "$nthDayBefore 00:00:00"
}

fun String.convertApiDateToUIDate(
    inFormat: String = FORMAT_yyyy_MM_dd_HH_mm_ss,
    outFormat: String = FORMAT_dd_MMM_yyyy_hh_mm_aaa
): String {
    return formatDateString(this, inFormat, outFormat)
}

@OptIn(ExperimentalTime::class)
fun Int.getDayIntervalDateTime(): Pair<String?,String?> {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val day = today.plus(this, DateTimeUnit.DAY)
    return Pair("$day 00:00:00", "$day 23:59:59")
}


@OptIn(ExperimentalTime::class)
fun Int.getMonthIntervalDateTime(): Pair<String, String> {
    val now = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
    val targetMonth = now.plus(this, DateTimeUnit.MONTH)

    val firstDateOfMonth = LocalDate(targetMonth.year, targetMonth.month, 1)
    val lastDateOfMonth = LocalDate(targetMonth.year, targetMonth.month, getLastDayOfMonth(targetMonth.year, targetMonth.month))

    val firstDateStr = "$firstDateOfMonth 00:00:00"
    val lastDateStr = "$lastDateOfMonth 23:59:59"

    return Pair(firstDateStr, lastDateStr)
}

@OptIn(ExperimentalTime::class)
fun Int.getYearIntervalDateTime(): Pair<String, String> {
    val now = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
    val targetYear = now.plus(this, DateTimeUnit.YEAR)

    val firstDateOfYear = LocalDate(targetYear.year, Month.JANUARY, 1)
    val lastDateOfYear = LocalDate(targetYear.year, Month.DECEMBER, 31)

    val firstDateStr = "$firstDateOfYear 00:00:00"
    val lastDateStr = "$lastDateOfYear 23:59:59"

    return Pair(firstDateStr, lastDateStr)
}

fun getLastDayOfMonth(year: Int, month: Month): Int {
    return when (month) {
        Month.FEBRUARY -> if (isLeapYear(year)) 29 else 28
        Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
        else -> 31
    }
}

fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}


fun String.convertTo12HourFormat(): String {

    val time = LocalTime.parse(this.substringBeforeLast('.'))

    val hour = when {
        time.hour == 0 || time.hour == 12 -> 12
        time.hour > 12 -> time.hour - 12
        else -> time.hour
    }

    val minute = time.minute.toString().padStart(2, '0')
    val amPm = if (time.hour < 12) "AM" else "PM"

    return "$hour:$minute $amPm"
}
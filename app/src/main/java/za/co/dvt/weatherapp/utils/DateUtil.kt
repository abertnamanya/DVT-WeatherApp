package za.co.dvt.weatherapp.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun convertDateTimeToDate(dateToFormat: String): String {
    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    var date = LocalDate.parse(dateToFormat, formatter)
    return date.toString()
}

fun dayOfTheWeek(dateToFormat: String): String {
    val dateFormat = SimpleDateFormat(
        "yyyy-MM-dd", Locale.ENGLISH
    )
    val date = dateFormat.parse(dateToFormat)
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH)
}

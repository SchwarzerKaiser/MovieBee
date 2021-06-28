package com.leewilson.moviebee.util

import java.text.SimpleDateFormat
import java.time.*
import java.util.*

fun LocalDate.asDate(): Date {
    return Date.from(atStartOfDay()
        .atZone(ZoneId.systemDefault()).toInstant())
}

fun LocalDateTime.asDate(): Date {
    return Date.from(atZone(ZoneId.systemDefault()).toInstant())
}

fun Long.asLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault()).toLocalDate()
}

fun LocalDate.asLong(): Long {
    return toEpochDay() * (1000 * 60 * 60 * 24)
}

fun Date.asLocalDate(): LocalDate {
    return Instant.ofEpochMilli(time)
        .atZone(ZoneId.systemDefault()).toLocalDate()
}

fun Date.asLocalDateTime(): LocalDateTime {
    return Instant.ofEpochMilli(time)
        .atZone(ZoneId.systemDefault()).toLocalDateTime()
}

fun Date.toFormattedString(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
    val date = sdf.format(this)
    val month = date.substring(3, 5)
    val monthStr = when (month) {
        "01" -> "Jan"
        "02" -> "Feb"
        "03" -> "Mar"
        "04" -> "Apr"
        "05" -> "May"
        "06" -> "Jun"
        "07" -> "Jul"
        "08" -> "Aug"
        "09" -> "Sept"
        "10" -> "Oct"
        "11" -> "Nov"
        "12" -> "Dec"
        else -> "ERROR"
    }
    return "${date.substring(0, 2)} $monthStr ${date.substring(6, 10)}, ${date.substring(11)}"
}
package com.leewilson.movienights.util

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
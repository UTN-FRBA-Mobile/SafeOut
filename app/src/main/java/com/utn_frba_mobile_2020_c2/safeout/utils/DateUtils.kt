package com.utn_frba_mobile_2020_c2.safeout.utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

object DateUtils {
    private const val PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    fun dateFromString(string: String): Date {
        val formatter = DateTimeFormatter.ofPattern(PATTERN)
        val localDate = LocalDateTime.parse(string, formatter)
        return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant())
    }

    fun dateToString(date: Date): String {
        val formatter = DateTimeFormatter.ofPattern(PATTERN)
        val localDate = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
        return formatter.format(localDate)
    }
}

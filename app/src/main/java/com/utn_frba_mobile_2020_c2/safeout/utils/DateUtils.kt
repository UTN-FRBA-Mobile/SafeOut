package com.utn_frba_mobile_2020_c2.safeout.utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.chrono.IsoChronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.util.*

object DateUtils {
    const val ISO = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val DISPLAY = "DISPLAY"

    private val months: HashMap<String, String> = hashMapOf(
        "January" to "enero",
        "February" to "febrero",
        "March" to "marzo",
        "April" to "abril",
        "May" to "mayo",
        "June" to "junio",
        "July" to "julio",
        "August" to "agosto",
        "September" to "septiembre",
        "October" to "octubre",
        "November" to "noviembre",
        "December" to "diciembre",
    )

    fun dateFromString(string: String): Date {
        val formatter = DateTimeFormatter.ofPattern(ISO)
        val localDate = LocalDateTime.parse(string, formatter)
        return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant())
    }

    fun dateToString(date: Date, pattern: String = ISO): String {
        val format = if (pattern == DISPLAY) {
            DateTimeFormatterBuilder.getLocalizedDateTimePattern(
                FormatStyle.LONG,
                FormatStyle.SHORT,
                IsoChronology.INSTANCE,
                Locale.forLanguageTag("es-AR")
            )
        } else {
            pattern
        }
        val formatter = DateTimeFormatter.ofPattern(format)
        val localDate = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
        var string = formatter.format(localDate)
        if (pattern === DISPLAY) {
            // No sé por qué no me auto-traducía el mes, así que tuve que hacer este hack horrible
            for ((key, value) in months) {
                string = string.replace(key, value)
            }
        }
        return string
    }
}

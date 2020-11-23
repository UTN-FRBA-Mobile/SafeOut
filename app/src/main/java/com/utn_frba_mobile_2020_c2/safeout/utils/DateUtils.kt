package com.utn_frba_mobile_2020_c2.safeout.utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.chrono.IsoChronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.util.*

object DateUtils {
    private const val ISO_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    private val UTC_ZONE_ID = ZoneId.of("UTC")

    enum class Pattern {
        ISO, DISPLAY, DISPLAY_DATE, DISPLAY_TIME
    }

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
        val formatter = DateTimeFormatter.ofPattern(ISO_PATTERN)
        val localDate = LocalDateTime.parse(string, formatter)
        return Date.from(localDate.atZone(UTC_ZONE_ID).toInstant())
    }

    private fun getPattern(displayDate: Boolean, displayTime: Boolean): String {
        return DateTimeFormatterBuilder.getLocalizedDateTimePattern(
            if (displayDate) FormatStyle.LONG else null,
            if (displayTime) FormatStyle.SHORT else null,
            IsoChronology.INSTANCE,
            Locale.forLanguageTag("es-AR")
        )
    }

    fun dateToString(date: Date, pattern: Pattern = Pattern.ISO): String {
        val format = when (pattern) {
            Pattern.ISO -> ISO_PATTERN
            Pattern.DISPLAY -> getPattern(displayDate=true, displayTime=true)
            Pattern.DISPLAY_DATE -> getPattern(displayDate=true, displayTime=false)
            Pattern.DISPLAY_TIME -> getPattern(displayDate=false, displayTime=true)
        }
        val formatter = DateTimeFormatter.ofPattern(format)
        val zoneId = if (pattern == Pattern.ISO) UTC_ZONE_ID else ZoneId.of("America/Buenos_Aires")
        val localDate = LocalDateTime.ofInstant(date.toInstant(), zoneId)
        var string = formatter.format(localDate)
        if (pattern == Pattern.DISPLAY || pattern == Pattern.DISPLAY_DATE) {
            // No sé por qué no me auto-traducía el mes, así que tuve que hacer este hack horrible
            for ((key, value) in months) {
                string = string.replace(key, value)
            }
        }
        return string
    }
}

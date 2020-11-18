package com.utn_frba_mobile_2020_c2.safeout.models

import com.google.gson.JsonObject
import com.utn_frba_mobile_2020_c2.safeout.utils.DateUtils
import java.util.Date

data class Reservation(
    val id: String,
    val date: Date,
    val section: Section,
) {
    companion object {
        fun fromObject(obj: JsonObject): Reservation {
            val id = obj.get("id").asString
            val date = DateUtils.dateFromString(obj.get("date").asString)
            val section = Section.fromObject(obj.get("section").asJsonObject)
            return Reservation(id, date, section)
        }
    }
}

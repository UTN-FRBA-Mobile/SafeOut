package com.utn_frba_mobile_2020_c2.safeout.models


import com.google.gson.JsonObject

data class Section(
    val id: String,
    val name: String,
    val occupation: Int,
    val capacity: Int,
    val place: Place,
    val reservations: Boolean,
) {
    companion object {
        fun fromObject(obj: JsonObject): Section {
            val id = obj.get("id").asString
            val name = obj.get("name").asString
            val reservations = obj.get("reservations").asBoolean
            val occupation = obj.get("occupation").asInt
            val capacity = obj.get("capacity").asInt
            val place = Place.fromObject(obj.get("place").asJsonObject)
            return Section(id, name, occupation, capacity, place, reservations)
        }
    }
}

data class SectionInfo(
    val id: String,
    val name: String,
    val occupation: Int,
    val capacity: Int,
    val reservations: Boolean,
) {
    companion object {
        fun fromObject(obj: JsonObject): SectionInfo {
            val id = obj.get("id").asString
            val name = obj.get("name").asString
            val occupation = obj.get("occupation").asInt
            val capacity = obj.get("capacity").asInt
            val reservations = obj.get("reservations").asBoolean
            return SectionInfo(id, name, occupation, capacity, reservations)
        }
    }
}

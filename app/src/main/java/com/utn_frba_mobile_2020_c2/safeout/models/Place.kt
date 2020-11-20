package com.utn_frba_mobile_2020_c2.safeout.models

import android.graphics.Bitmap
import android.location.Location
import com.google.gson.JsonObject
import kotlinx.serialization.Serializable

@Serializable
data class Place(
    val id: String? = null,
    val name: String? = null,
    val address: String? = null,
    val category: String? = null,
    val location: Location? = null,
    var imgResource: Bitmap? = null,
) {
    companion object {
        fun fromObject(obj: JsonObject): Place {
            val id = obj.get("id").asString
            val name = obj.get("name").asString
            val address = obj.get("address").asString
            val category = obj.get("category").asString
            val locationObject = obj.get("location").asJsonObject
            val location = Location("")
            location.latitude = locationObject.get("latitude").asDouble
            location.longitude = locationObject.get("longitude").asDouble
            return Place(id, name, address, category, location)
        }
    }
}

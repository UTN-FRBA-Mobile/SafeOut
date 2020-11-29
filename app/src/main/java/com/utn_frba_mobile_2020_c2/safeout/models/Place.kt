package com.utn_frba_mobile_2020_c2.safeout.models

import android.graphics.Bitmap
import android.location.Location
import com.google.gson.JsonObject
import kotlinx.serialization.Serializable
import com.google.gson.annotations.SerializedName
import com.utn_frba_mobile_2020_c2.safeout.utils.BitmapUtils

@Serializable

data class Place(
    val id: String? = null,
    val name: String? = null,
    val address: String? = null,
    val category: String? = null,
    val location: Location? = null,
    val capacity: Int? = null,
    var occupation: Int? = null,
    var imgResource: Bitmap? = null,
    ) : java.io.Serializable {
    fun toObject(): JsonObject {
        val obj = JsonObject()
        obj.addProperty("id", id)
        obj.addProperty("name", name)
        obj.addProperty("address", address)
        obj.addProperty("category", category)
        val locationObject = JsonObject()
        locationObject.addProperty("longitude", location?.longitude)
        locationObject.addProperty("latitude", location?.latitude)
        obj.add("location", locationObject)
        obj.addProperty("capacity", capacity)
        obj.addProperty("occupation", occupation)
        if (imgResource != null) {
            obj.addProperty("imgResource", BitmapUtils.bitmapToString(imgResource!!))
        }
        return obj
    }
    companion object {
        fun fromObject(obj: JsonObject): Place {
            val id = obj.get("id").asString
            val name = obj.get("name").asString
            val address = obj.get("address").asString
            val category = obj.get("category").asString
            val locationObject = obj.get("location").asJsonObject
            val longitude = locationObject.get("longitude").asDouble
            val latitude = locationObject.get("latitude").asDouble
            val location = createLocation(longitude, latitude)
            val capacity = obj.get("capacity").asInt
            val occupation = obj.get("occupation").asInt
            val imgResource = if (obj.has("imgResource")) {
                BitmapUtils.bitmapFromString(obj.get("imgResource").asString)
            } else {
                null
            }
            return Place(id, name, address, category, location, capacity, occupation, imgResource)
        }
        fun createLocation(longitude: Double, latitude: Double): Location {
            val location = Location("")
            location.longitude = longitude
            location.latitude = latitude
                return location
        }
    }
}

data class PlaceScanInfo (
    @SerializedName("placeId") var placeId : String,
    @SerializedName("sectionId") var sectionId : String
)

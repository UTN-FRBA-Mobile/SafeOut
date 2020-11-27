package com.utn_frba_mobile_2020_c2.safeout.dto

import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.serialization.Serializable
import org.json.JSONObject

@Serializable
class Bounds {

    companion object Convert{
        fun toJson(entry: LatLngBounds): JSONObject {
            var bounds = JSONObject()
            var southwest = JSONObject()
            var northeast = JSONObject()
            southwest.put("latitude", entry.southwest.latitude)
            southwest.put("longitude", entry.southwest.longitude)
            northeast.put("latitude", entry.northeast.latitude)
            northeast.put("longitude", entry.northeast.longitude)
            bounds.put("northeast", northeast)
            bounds.put("southwest", southwest)

            return bounds
        }
    }
}



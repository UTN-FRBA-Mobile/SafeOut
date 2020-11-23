package com.utn_frba_mobile_2020_c2.safeout.models

import android.location.Location
import com.google.gson.JsonObject
import kotlinx.serialization.Serializable

object ModelMaps {
    data class Places(val places: List<Place>)
    data class Place(var id: String, var name: String, var category: String, var address: String, var location: Point, var capacity: Number, var occupation: Number)
    data class Point(var latitude: Double, var longitude: Double)
}


/* {
        "address": "Ramallo 2388",
        "category": "Restorán",
        "location": {
            "longitude": -58.472411,
            "latitude": -34.5430272
        },
        "name": "La Farola de Saavedra",
        "id": "5f600c79db23bc5159a7f84c",
        "capacity": 120,
        "occupation": 43
    }*/
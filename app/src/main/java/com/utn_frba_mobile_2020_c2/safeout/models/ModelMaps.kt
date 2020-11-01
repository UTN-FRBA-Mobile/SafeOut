package com.utn_frba_mobile_2020_c2.safeout.models

import kotlinx.serialization.Serializable

object ModelMaps {
    data class Places(val places: List<Place>)
    data class Place(var name: String, var category: String, var address: String, var location: Point, var sections: Section)
    data class Point(var type: String, var coordinates: List<Number>)
    data class Section (var name: String, var capacity: Number, var occupation: Number)
}

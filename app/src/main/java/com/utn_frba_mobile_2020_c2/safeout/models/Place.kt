package com.utn_frba_mobile_2020_c2.safeout.models

import android.graphics.Bitmap
import android.location.Location
import kotlinx.serialization.Serializable

@Serializable
data class Place(val id:String? = null, val name: String? = null, val address: String? = null, val category: String? = null, var imgResource: Bitmap? = null, val location: Location? = null, val sections: List<Section>? = null)

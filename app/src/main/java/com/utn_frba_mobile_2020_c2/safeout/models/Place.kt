package com.utn_frba_mobile_2020_c2.safeout.models

import android.location.Location

data class Place(val id:Int, val name: String, val address: String, val category: String, val imgResource: Int, val location: Location, val sections: List<Section>)

package com.utn_frba_mobile_2020_c2.safeout.listeners

import com.utn_frba_mobile_2020_c2.safeout.models.Place

interface RecyclerPlaceListener {
    fun onClick(place: Place, position: Int)

}
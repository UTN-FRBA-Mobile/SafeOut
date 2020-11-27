package com.utn_frba_mobile_2020_c2.safeout.dto

import kotlinx.serialization.Serializable

@Serializable
data class Locacion (
    var latitude : Number,
    var longitude : Number
)
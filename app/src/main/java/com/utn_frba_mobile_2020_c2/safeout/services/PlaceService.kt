package com.utn_frba_mobile_2020_c2.safeout.services

import com.utn_frba_mobile_2020_c2.safeout.models.ModelMaps
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {

    @GET("places")
    fun getPlaces(
        @Query("latMax") latMax: Double,
        @Query("lngMax") lngMax: Double,
        @Query("latMin") latMin: Double,
        @Query("lngMin") lngMin: Double): Call<ModelMaps.Places>

}
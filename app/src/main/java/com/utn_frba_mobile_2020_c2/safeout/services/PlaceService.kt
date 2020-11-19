package com.utn_frba_mobile_2020_c2.safeout.services

import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.models.Place
import com.utn_frba_mobile_2020_c2.safeout.models.Section
import com.utn_frba_mobile_2020_c2.safeout.utils.RequestUtils
import com.utn_frba_mobile_2020_c2.safeout.utils.RequestUtils2
import org.json.JSONArray
import org.json.JSONObject


object PlaceService {

    fun getPlaceInfo(placeId: String, onSuccess: (JsonObject?, String?) -> Unit) {
        RequestUtils2.create()
            .url("/places/${placeId}")
            .send(onSuccess)
    }

    fun getSections(placeId: String, onSuccess: (JsonArray?, String?) -> Unit) {
        RequestUtils2.create()
            .url("/places/${placeId}/sections")
            .send(onSuccess)
    }

    fun search(
        query: String,  onSuccess: (JSONArray) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        RequestUtils.postPlaces("/places/search"
            ,mapOf(
                "query" to query
/*                ,
            "skip" to 0,
            "limit" to 0*/
            )
            ,  onSuccess, onError)

    }


    fun getImage(imageUrl: String,  onSuccess: (Bitmap) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        RequestUtils.getImage(imageUrl,  onSuccess, onError)

    }


}
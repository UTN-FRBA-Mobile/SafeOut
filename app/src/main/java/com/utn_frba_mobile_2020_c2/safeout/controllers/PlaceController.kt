package com.utn_frba_mobile_2020_c2.safeout.controllers

import android.content.Context
import com.utn_frba_mobile_2020_c2.safeout.services.PlaceService
import org.json.JSONObject

object PlaceController {
    private var context: Context? = null

    fun init(context: Context) {
        this.context = context
    }

    /* Returns all places data (limited by?)*/
    fun getPlaces(onSuccess: (JSONObject) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        PlaceService.get({ response ->
            onSuccess(response)
        }, onError)
    }

    /* Returns placeId data */
    fun getPlace(placeId: Int, onSuccess: (JSONObject) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        PlaceService.getById(placeId, { response ->
            onSuccess(response)
        }, onError)
    }

    /* Register the user checkin for placeId, and returns placeId data? */
    fun checkin(placeId: Int, onSuccess: (JSONObject) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        PlaceService.checkin(placeId, { response ->
            //todo: normalize data if needed
            onSuccess(response.getJSONObject("place"))
        }, onError)
    }

    /* Register the user checkout for placeId, and returns placeId data? */
    fun checkout(placeId: Int, onSuccess: (JSONObject) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        PlaceService.checkout(placeId, { response ->
            //todo: normalize data if needed
            onSuccess(response.getJSONObject("place"))
        }, onError)
    }

}

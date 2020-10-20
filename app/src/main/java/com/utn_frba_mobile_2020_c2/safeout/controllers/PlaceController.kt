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

    /* Register the user checkin for placeId and section, and returns placeId data? */
    fun checkin(placeId: Int, section: String, onSuccess: (JSONObject) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        PlaceService.checkin(placeId, section, { response ->
            //todo: normalize data if needed, integrate with api
            onSuccess(response.getJSONObject("place"))
        }, onError)
    }

    /* Register the user checkout for placeId and section, and returns placeId data? */
    fun checkout(placeId: Int, section: String, onSuccess: (JSONObject) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        PlaceService.checkout(placeId, section, { response ->
            //todo: normalize data if needed, integrate with apis
            onSuccess(response.getJSONObject("place"))
        }, onError)
    }

}

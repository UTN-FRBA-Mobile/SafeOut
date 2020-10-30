package com.utn_frba_mobile_2020_c2.safeout.controllers

import android.content.Context
import com.utn_frba_mobile_2020_c2.safeout.services.PlaceService
import org.json.JSONObject

object PlaceController {
    private var context: Context? = null

    fun init(context: Context) {
        this.context = context
    }

    /* Register the user checkin for placeId and section, and returns placeId data? */
    fun checkin(placeId: Int, sectionId: Int, onSuccess: (JSONObject) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        PlaceService.checkin(placeId, sectionId, { response ->
            //todo: normalize data if needed, integrate with api
            onSuccess(response.getJSONObject("place"))
        }, onError)
    }

    /* Register the user checkout for placeId and section, and returns placeId data? */
    fun checkout(placeId: Int, sectionId: Int, onSuccess: (JSONObject) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        PlaceService.checkout(placeId, sectionId, { response ->
            //todo: normalize data if needed, integrate with apis
            onSuccess(response.getJSONObject("place"))
        }, onError)
    }

}

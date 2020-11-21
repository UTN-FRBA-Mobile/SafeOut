package com.utn_frba_mobile_2020_c2.safeout.controllers

import android.content.Context
import android.graphics.Bitmap
import com.utn_frba_mobile_2020_c2.safeout.services.PlaceService
import org.json.JSONArray
import org.json.JSONObject

object PlaceController {
    private var context: Context? = null

    fun init(context: Context) {
        this.context = context
    }

    fun search(query: String, onSuccess: (JSONArray) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        PlaceService.search(query ,{ response ->
            //todo: normalize data if needed, integrate with apis

            onSuccess(response)
        }, onError)
    }

    fun getImage(imageUrl: String, onSuccess: (Bitmap) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        PlaceService.getImage(imageUrl, { response ->
            //todo: normalize data if needed, integrate with apis
            onSuccess(response)
        }, onError)
    }

}

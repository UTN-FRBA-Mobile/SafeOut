package com.utn_frba_mobile_2020_c2.safeout.utils

import android.content.Context
import androidx.core.net.toUri
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.ANRequest
import com.androidnetworking.common.ANRequest.GetRequestBuilder
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.controllers.AuthController
import org.json.JSONObject
import java.net.URL
import java.util.concurrent.Executors

object RequestUtils {
    private const val apiUrl = "https://salina.nixi.icu/"
    //private const val apiUrl = "http://localhost:3000/safeout"
    private var context: Context? = null

    fun init(context: Context) {
        this.context = context
        AndroidNetworking.initialize(context)
        AuthController.init(context)
    }

    private fun getUrl(uri: String): String {
        if (uri.startsWith("http")) {
            return uri
        }
        return apiUrl + uri.trim('/')
    }

    fun get(uri: String, onResponse: (JSONObject) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        AndroidNetworking.get(getUrl(uri))
            .addHeaders("Authentication", AuthController.loggedToken ?: "")
            .setExecutor(Executors.newSingleThreadExecutor())
            .build()
            .getAsJSONObject(object: JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    onResponse(response)
                }
                override fun onError(error: ANError) {
                    if (onError == null) {
                        return
                    }

                    var status = error.errorCode
                    var message: String = context!!.getString(R.string.backend_error)
                    try {
                        val obj = JSONObject(error.errorBody)
                        message = obj.get("message").toString()
                    } catch (e: Error) {}
                    onError(status, message)
                }
            })
    }

    fun put(uri: String, onResponse: (JSONObject) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        AndroidNetworking.put(getUrl(uri))
            .addHeaders("Authentication", AuthController.loggedToken ?: "")
            .setExecutor(Executors.newSingleThreadExecutor())
            .build()
            .getAsJSONObject(object: JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    onResponse(response)
                }
                override fun onError(error: ANError) {
                    if (onError == null) {
                        return
                    }

                    var status = error.errorCode
                    var message: String = context!!.getString(R.string.backend_error)
                    try {
                        val obj = JSONObject(error.errorBody)
                        message = obj.get("message").toString()
                    } catch (e: Error) {}
                    onError(status, message)
                }
        })
    }

    fun post(uri: String, body: Map<String, Any>, onResponse: (JSONObject) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        AndroidNetworking.post(getUrl(uri))
            .addJSONObjectBody(JSONObject(body))
            .addHeaders("Authentication", AuthController.loggedToken ?: "")
            .setExecutor(Executors.newSingleThreadExecutor())
            .build()
            .getAsJSONObject(object: JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    onResponse(response)
                }
                override fun onError(error: ANError) {
                    if (onError == null) {
                        return
                    }

                    var status = error.errorCode
                    var message: String = context!!.getString(R.string.backend_error)
                    try {
                        val obj = JSONObject(error.errorBody)
                        message = obj.get("message").toString()
                    } catch (e: Error) {}
                    onError(status, message)
                }
            })
    }
}
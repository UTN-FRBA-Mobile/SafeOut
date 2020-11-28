package com.utn_frba_mobile_2020_c2.safeout.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.core.net.toUri
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.ANRequest
import com.androidnetworking.common.ANRequest.GetRequestBuilder
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.BitmapRequestListener
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.controllers.AuthController
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.util.concurrent.Executors

@Deprecated("Use RequestUtils2")
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

    fun get(
        uri: String,
        onResponse: (JSONObject) -> Unit,
        onError: ((status: Int, message: String?) -> Unit)? = null
    ) {
        AndroidNetworking.get(getUrl(uri))
            .addHeaders("Authorization", AuthController.loggedToken ?: "")
            .setExecutor(Executors.newSingleThreadExecutor())
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
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
                    } catch (e: Error) {
                    }
                    onError(status, message)
                }
            })
    }

    fun put(
        uri: String,
        onResponse: (JSONObject) -> Unit,
        onError: ((status: Int, message: String?) -> Unit)? = null
    ) {
        AndroidNetworking.put(getUrl(uri))
            .addHeaders("Authorization", AuthController.loggedToken ?: "")
            .setExecutor(Executors.newSingleThreadExecutor())
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
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
                    } catch (e: Error) {
                    }
                    onError(status, message)
                }
            })
    }

    fun post(
        uri: String,
        body: Map<String, Any>?,
        onResponse: (JSONObject) -> Unit,
        onError: ((status: Int, message: String?) -> Unit)? = null
    ) {
        val bodyObject = if (body !== null) JSONObject(body) else JSONObject()
        AndroidNetworking.post(getUrl(uri))
            .addJSONObjectBody(bodyObject)
            .addHeaders("Authorization", AuthController.loggedToken ?: "")
            .setExecutor(Executors.newSingleThreadExecutor())
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
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
                    } catch (e: Error) {
                    }
                    onError(status, message)
                }
            })
    }

    fun postArray(
        uri: String,
        body: Map<String, Any>,
        onResponse: (JSONArray) -> Unit,
        onError: ((status: Int, message: String?) -> Unit)? = null
    ) {

        AndroidNetworking.post(getUrl(uri))
            .addJSONObjectBody(JSONObject(body))
            .addHeaders("Authorization", AuthController.loggedToken ?: "")
            //.setExecutor(Executors.newSingleThreadExecutor())
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {

                override fun onResponse(response: JSONArray) {
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
                    } catch (e: Error) {
                    }
                    onError(status, message)
                }
            })
    }


    fun getImage(
        imageUrl: String,
        onResponse: (Bitmap) -> Unit,
        callbackError: ((status: Int, message: String?) -> Unit)? = null
    ) {

        AndroidNetworking.get(imageUrl)
            .setTag("imageRequestTag")
            .setPriority(Priority.MEDIUM)
            .addHeaders("Authorization", AuthController.loggedToken ?: "")
            .setBitmapMaxHeight(200)
            .setBitmapMaxWidth(200)
            .setBitmapConfig(Bitmap.Config.ARGB_8888)
            .build()
            .getAsBitmap(object : BitmapRequestListener {

                override fun onResponse(bitmap: Bitmap) {
                    onResponse(bitmap)
                }

                override fun onError(error: ANError) {
                    if (callbackError != null) {
                        callbackError(error.errorCode,error.message)
                    }
                }
            });
    }

}
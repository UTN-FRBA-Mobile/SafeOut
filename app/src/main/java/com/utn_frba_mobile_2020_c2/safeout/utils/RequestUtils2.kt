package com.utn_frba_mobile_2020_c2.safeout.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.utn_frba_mobile_2020_c2.safeout.controllers.AuthController
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request as OkRequest
import okhttp3.RequestBody.Companion.toRequestBody
import ru.gildor.coroutines.okhttp.await


object  RequestUtils2 {
    private const val apiUrl = "https://salina.nixi.icu/"
    private val JSON = "application/json; charset=utf-8".toMediaType()

    private var context: Context? = null
    private var client: OkHttpClient? = null

    class Request {
        private var url: String? = null
        private var params: JsonObject? = null
        fun url(url: String): Request = apply {
            this.url = url
        }
        fun params(params: JsonObject): Request = apply {
            this.params = params
        }
        fun send(handler: (JsonObject, Boolean) -> Unit) {
            post<JsonObject>(url!!, params, handler,
                nullable = false,
                list = false
            )
        }
        @JvmName("send1")
        fun send(handler: (JsonObject?, Boolean) -> Unit) {
            post<JsonObject?>(url!!, params, handler,
                nullable = true,
                list = false
            )
        }
        @JvmName("send2")
        fun send(handler: (JsonArray, Boolean) -> Unit) {
            post<JsonArray>(url!!, params, handler,
                nullable = false,
                list = true
            )
        }
        @JvmName("send3")
        fun send(handler: (JsonArray?, Boolean) -> Unit) {
            post<JsonArray?>(url!!, params, handler,
                nullable = true,
                list = true
            )
        }
    }

    fun init(context: Context) {
        this.context = context
        this.client = OkHttpClient.Builder().build()
        AuthController.init(context)
    }

    fun create(): Request {
        return Request()
    }

    private fun getUrl(uri: String): String {
        if (uri.startsWith("http")) {
            return uri
        }
        return this.apiUrl + uri.trim('/')
    }

    private fun <T> post(
        uri: String,
        params: JsonObject?,
        handler: (T, Boolean) -> Unit,
        nullable: Boolean = false,
        list: Boolean = false
    ) {
        var body = JsonUtils.objectToString(params) ?: ""
        AsyncUtils.runInBackground {
            val url = getUrl(uri)
            val request = OkRequest
                .Builder()
                .url(url)
                .addHeader("Authorization", AuthController.loggedToken ?: "")
                .post(body.toRequestBody(JSON))
                .build()
            val response = client!!.newCall(request).await()
            val error = response.code / 100 != 2
            var body = response.body?.string()
            if (body == "" || body == "null") {
                body = null
            }
            val json = if (list) {
                JsonUtils.stringToArray(body)
            } else {
                JsonUtils.stringToObject(body)
            }
            val result = if (nullable) {
                json as T
            } else {
                json!! as T
            }
            AsyncUtils.updateUi {
                handler(result, error)
            }
        }

    }


}
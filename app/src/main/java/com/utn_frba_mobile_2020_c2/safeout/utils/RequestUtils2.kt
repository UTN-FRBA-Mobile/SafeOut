package com.utn_frba_mobile_2020_c2.safeout.utils

import android.content.Context
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.utn_frba_mobile_2020_c2.safeout.R
import com.utn_frba_mobile_2020_c2.safeout.controllers.AuthController
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request as OkRequest
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
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
        fun send(handler: (JsonObject, String?) -> Unit) {
            post<JsonObject>(url!!, params, handler,
                nullable = false,
                list = false
            )
        }
        @JvmName("send1")
        fun send(handler: (JsonObject?, String?) -> Unit) {
            post<JsonObject?>(url!!, params, handler,
                nullable = true,
                list = false
            )
        }
        @JvmName("send2")
        fun send(handler: (JsonArray, String?) -> Unit) {
            post<JsonArray>(url!!, params, handler,
                nullable = false,
                list = true
            )
        }
        @JvmName("send3")
        fun send(handler: (JsonArray?, String?) -> Unit) {
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
        handler: (T, String?) -> Unit,
        nullable: Boolean = false,
        list: Boolean = false
    ) {
        AsyncUtils.runInBackground {
            val requestBody = (JsonUtils.objectToString(params) ?: "").toRequestBody(JSON)
            val defaultErrorMessage = context!!.getString(R.string.backend_error)
            var errorMessage: String? = null
            var result = if (nullable) {
                null
            } else {
                if (list) {
                    JsonArray()
                } else {
                    JsonObject()
                }
            } as T
            val url = getUrl(uri)
            val request = OkRequest
                .Builder()
                .url(url)
                .addHeader("Authorization", AuthController.loggedToken ?: "")
                .post(requestBody)
                .build()
            var response: Response? = null
            try {
                response = client!!.newCall(request).await()
            } catch (_: Throwable) { }
            if (response == null) {
                errorMessage = defaultErrorMessage
            } else {
                val error = response!!.code / 100 != 2
                var responseBody = response!!.body?.string()
                if (responseBody == "" || responseBody == "null") {
                    responseBody = null
                }
                val json = if (list && !error) {
                    JsonUtils.arrayFromString(responseBody)
                } else {
                    JsonUtils.objectFromString(responseBody)
                }
                if (!error) {
                    result = if (nullable) {
                        json
                    } else {
                        json!!
                    } as T
                } else {
                    errorMessage = (json as JsonObject?)?.get("message")?.asString ?: defaultErrorMessage
                }
            }
            AsyncUtils.updateUi {
                handler(result, errorMessage)
            }
        }

    }
}

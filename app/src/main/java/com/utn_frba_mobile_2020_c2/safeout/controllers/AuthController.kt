package com.utn_frba_mobile_2020_c2.safeout.controllers

import android.content.Context
import com.utn_frba_mobile_2020_c2.safeout.services.AuthService
import com.utn_frba_mobile_2020_c2.safeout.utils.StorageUtils
import org.json.JSONObject

object AuthController {
    private var context: Context? = null

    val loggedIn: Boolean
        get(): Boolean {
            return loggedToken != null
        }

    val loggedUser: JSONObject?
        get(): JSONObject? {
            val auth = getAuthData()
            return auth?.get("user") as JSONObject?
        }

    val loggedToken: String?
        get(): String? {
            val auth = getAuthData()
            return auth?.get("token") as String?
        }

    fun init(context: Context) {
        this.context = context
        StorageUtils.init(context)
    }

    fun doAuth(action: String, username: String, password: String, onSuccess: (JSONObject) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        AuthService.doAuth(action, username, password, { response ->
            StorageUtils.set(StorageUtils.AUTH, response.toString())
            onSuccess(response)
        }, onError)
    }

    fun logout() {
        StorageUtils.unset(StorageUtils.AUTH)
    }

    private fun getAuthData(): JSONObject? {
        val serialized = StorageUtils.get(StorageUtils.AUTH)
        return if (serialized != null) JSONObject(serialized) else null
    }
}

package com.utn_frba_mobile_2020_c2.safeout.services

import android.view.View
import android.widget.Toast
import com.utn_frba_mobile_2020_c2.safeout.utils.RequestUtils
import kotlinx.android.synthetic.main.activity_auth.*
import org.json.JSONObject

object AuthService {
    fun login(username: String, password: String, onSuccess: (JSONObject) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        RequestUtils.post("/auth/login", mapOf(
            "name" to username,
            "password" to password,
        ), onSuccess, onError)
    }

    fun signup(username: String, password: String, onSuccess: (JSONObject) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        RequestUtils.post("/auth/signup", mapOf(
            "name" to username,
            "password" to password,
        ), onSuccess, onError)
    }

    fun doAuth(action: String, username: String, password: String, onSuccess: (JSONObject) -> Unit, onError: ((status: Int, message: String?) -> Unit)? = null) {
        when (action) {
            "login" -> {
                return login(username, password, onSuccess, onError)
            }
            "signup" -> {
                return signup(username, password, onSuccess, onError)
            }
        }
    }
}
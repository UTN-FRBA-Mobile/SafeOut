package com.utn_frba_mobile_2020_c2.safeout.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.JsonObject

object StorageUtils {
    private var context: Context? = null
    private var preferences: SharedPreferences? = null
    private const val fileName = "prefs"

    const val AUTH = "auth"
    const val CHECKIN = "checkin"

    fun init(context: Context) {
        this.context = context
        this.preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    }

    fun set(key: String, value: String?) {
        val editor = preferences?.edit()
        if (editor != null) {
            if (value != null) {
                editor.putString(key, value)
            } else {
                editor.remove(key)
            }
            editor.apply()
        }
    }

    fun set(key: String, value: JsonObject?) {
        val value = JsonUtils.objectToString(value)
        set(key, value)
    }

    fun unset(key: String) {
        val editor = preferences?.edit()
        if (editor != null) {
            editor.remove(key)
            editor.apply()
        }
    }

    fun get(key: String, default: String? = null): String? {
        return preferences?.getString(key, default)
    }

    fun getAsObject(key: String, default: JsonObject? = null): JsonObject? {
        val value = get(key)
        return JsonUtils.objectFromString(value) ?: default
    }
}

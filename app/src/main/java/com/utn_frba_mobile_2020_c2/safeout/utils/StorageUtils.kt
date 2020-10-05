package com.utn_frba_mobile_2020_c2.safeout.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.androidnetworking.AndroidNetworking

object StorageUtils {
    private var context: Context? = null
    private var preferences: SharedPreferences? = null
    private const val fileName = "prefs"

    const val AUTH = "auth"

    fun init(context: Context) {
        this.context = context
        this.preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    }

    fun set(key: String, value: String) {
        val editor = preferences?.edit()
        if (editor != null) {
            editor.putString(key, value)
            editor.apply()
        }
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
}

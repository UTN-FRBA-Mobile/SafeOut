package com.utn_frba_mobile_2020_c2.safeout.utils

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject

object JsonUtils {

    fun stringToObject(str: String): JsonObject {
        return Gson().fromJson<JsonObject>(str, JsonObject::class.java)
    }

    @JvmName("stringToNullableObject")
    fun stringToObject(str: String?): JsonObject? {
        return if (str == null) {
            null
        } else {
            stringToObject(str)
        }
    }

    fun objectToString(obj: JsonObject): String {
        return Gson().toJson(obj)
    }

    @JvmName("objectToNullableString")
    fun objectToString(obj: JsonObject?): String? {
        return if (obj == null) {
            null
        } else {
            objectToString(obj)
        }
    }

    fun stringToArray(str: String): JsonArray {
        return Gson().fromJson<JsonArray>(str, JsonArray::class.java)
    }

    @JvmName("stringToNullableArray")
    fun stringToArray(str: String?): JsonArray? {
        return if (str == null) {
            null
        } else {
            stringToArray(str)
        }
    }

    fun arrayToString(arr: JsonArray): String {
        return Gson().toJson(arr)
    }

    @JvmName("arrayToNullableString")
    fun arrayToString(obj: JsonArray?): String? {
        return if (obj == null) {
            null
        } else {
            arrayToString(obj)
        }
    }
}

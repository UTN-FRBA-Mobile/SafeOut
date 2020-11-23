package com.utn_frba_mobile_2020_c2.safeout.utils

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

object JsonUtils {

    fun objectFromString(str: String): JsonObject {
        return Gson().fromJson<JsonObject>(str, JsonObject::class.java)
    }

    @JvmName("objectFromNullableString")
    fun objectFromString(str: String?): JsonObject? {
        return if (str == null) {
            null
        } else {
            objectFromString(str)
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

    fun arrayFromString(str: String): JsonArray {
        return Gson().fromJson<JsonArray>(str, JsonArray::class.java)
    }

    @JvmName("arrayFromNullableString")
    fun arrayFromString(str: String?): JsonArray? {
        return if (str == null) {
            null
        } else {
            arrayFromString(str)
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

    fun <T> arrayToList(arr: JsonArray, mapper: (JsonObject) -> T): List<T> {
        val length = arr.size()
        val list = ArrayList<T>(length)
        for (i in 0 until length) {
            val obj = arr.get(i) as JsonObject
            val item = mapper(obj)
            list.add(item)
        }
        return list
    }

    fun objectFromMap(map: Map<String, String>): JsonObject {
        val obj = JsonObject()
        for ((key, value) in map) {
            obj.addProperty(key, value)
        }
        return obj
    }
}

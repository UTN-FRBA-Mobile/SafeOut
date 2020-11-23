package com.utn_frba_mobile_2020_c2.safeout.extensions

import com.google.gson.JsonElement
import com.google.gson.JsonObject

fun JsonObject.optionallyGet(memberName: String): JsonElement? {
    var element: JsonElement? = null
    if (this.has(memberName)) {
        try {
            element = this.get(memberName)
        } catch (error: Error) {}
    }
    return element
}

fun JsonObject.extend(obj: JsonObject) {
    for ((key, value) in obj.entrySet()) {
        if (this.has(key)) {
            this.remove(key)
        }
        this.add(key, value)
    }
}

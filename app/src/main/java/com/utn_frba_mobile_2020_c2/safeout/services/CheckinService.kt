package com.utn_frba_mobile_2020_c2.safeout.services

import com.google.gson.JsonObject
import com.utn_frba_mobile_2020_c2.safeout.utils.RequestUtils2

object CheckinService {
    fun getCheckedInSection(onSuccess: (JsonObject?, Boolean) -> Unit) {
        RequestUtils2.create()
            .url("/checkins")
            .send(onSuccess)
    }

    fun checkInToSection(sectionId: String, onSuccess: (JsonObject?, Boolean) -> Unit) {
        val params = JsonObject()
        params.addProperty("section", sectionId)
        RequestUtils2.create()
            .url("/checkins/add")
            .params(params)
            .send(onSuccess)
    }

    fun checkOutOfSection(sectionId: String, onSuccess: (JsonObject?, Boolean) -> Unit) {
        val params = JsonObject()
        params.addProperty("section", sectionId)
        RequestUtils2.create()
            .url("/checkins/remove")
            .params(params)
            .send(onSuccess)
    }
}

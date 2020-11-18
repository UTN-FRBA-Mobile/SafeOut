package com.utn_frba_mobile_2020_c2.safeout.services

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.utn_frba_mobile_2020_c2.safeout.utils.DateUtils
import com.utn_frba_mobile_2020_c2.safeout.utils.RequestUtils2
import java.util.Date

object ReservationService {
    fun getReservations(onSuccess: (JsonArray?, String?) -> Unit) {
        RequestUtils2.create()
            .url("/reservations")
            .send(onSuccess)
    }

    fun submitReservation(sectionId: String, date: Date, onSuccess: (JsonObject?, String?) -> Unit) {
        val params = JsonObject()
        params.addProperty("section", sectionId)
        params.addProperty("date", DateUtils.dateToString(date))
        RequestUtils2.create()
            .url("/reservations/add")
            .params(params)
            .send(onSuccess)
    }

    fun cancelReservation(reservationId: String, onSuccess: (JsonObject?, String?) -> Unit) {
        RequestUtils2.create()
            .url("/reservations/${reservationId}/remove")
            .send(onSuccess)
    }

    fun getDates(sectionId: String, onSuccess: (JsonArray?, String?) -> Unit) {
        val params = JsonObject()
        params.addProperty("section", sectionId)
        RequestUtils2.create()
            .url("/reservations/dates")
            .params(params)
            .send(onSuccess)
    }

    fun getTimes(sectionId: String, date: Date, onSuccess: (JsonArray?, String?) -> Unit) {
        val params = JsonObject()
        params.addProperty("section", sectionId)
        params.addProperty("date", DateUtils.dateToString(date))
        RequestUtils2.create()
            .url("/reservations/times")
            .params(params)
            .send(onSuccess)
    }
}

package com.utn_frba_mobile_2020_c2.safeout.services

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {

    private val client = OkHttpClient.Builder().build()
    private var apiUrlBase = "https://salina.nixi.icu/"


    private val retrofit = Retrofit.Builder()
        .baseUrl(apiUrlBase)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T {
        return retrofit.create(service)
    }
}
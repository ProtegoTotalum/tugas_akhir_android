package com.example.tugas_akhir_android

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RClient {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.51/tugas_akhir/public/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var api = retrofit.create(api::class.java)
}
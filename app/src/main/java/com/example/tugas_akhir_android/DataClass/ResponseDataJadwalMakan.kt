package com.example.tugas_akhir_android.DataClass

import com.google.gson.annotations.SerializedName

data class ResponseDataJadwalMakan(
    @SerializedName("success") val stt: Boolean,
    val message: String,
    val data: List<JadwalMakanData>
)

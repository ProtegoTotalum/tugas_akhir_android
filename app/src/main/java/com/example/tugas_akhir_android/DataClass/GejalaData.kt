package com.example.tugas_akhir_android.DataClass

import com.google.gson.annotations.SerializedName

data class GejalaData(
    @SerializedName("id") val id_gejala: Int,
    @SerializedName("nama_gejala") val nama_gejala: String,
)

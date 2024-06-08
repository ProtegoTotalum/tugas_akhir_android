package com.example.tugas_akhir_android.DataClass

import com.google.gson.annotations.SerializedName

data class KabKotData(
    @SerializedName("id_kabkot") val id_kabkot: Int,
    @SerializedName("nama_kabkot") val nama_kabkot: String,
)

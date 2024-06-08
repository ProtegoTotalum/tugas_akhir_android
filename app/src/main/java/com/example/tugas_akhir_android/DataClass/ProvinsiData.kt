package com.example.tugas_akhir_android.DataClass

import com.google.gson.annotations.SerializedName

data class ProvinsiData(
    @SerializedName("id_provinsi") val id_provinsi: Int,
    @SerializedName("nama_provinsi") val nama_provinsi: String,
)

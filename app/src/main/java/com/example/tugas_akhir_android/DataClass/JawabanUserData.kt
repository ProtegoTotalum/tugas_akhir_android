package com.example.tugas_akhir_android.DataClass

import com.google.gson.annotations.SerializedName

data class JawabanUserData(
    @SerializedName("id_gejala") val id_gejala: Int,
    @SerializedName("jawaban_user") val jawaban_user: String,
)

package com.example.tugas_akhir_android.DataClass

import com.google.gson.annotations.SerializedName

data class PertanyaanData(
    @SerializedName("id_pertanyaan") val id_pertanyaan: Int,
    @SerializedName("id_gejala") val id_gejala: Int,
    @SerializedName("nomor_diagnosa_pertanyaan") val nomor_diagnosa_pertanyaan: String? = null,
    @SerializedName("nama_gejala") val nama_gejala: String,
    @SerializedName("jawaban_user") val jawaban_user: String,
)

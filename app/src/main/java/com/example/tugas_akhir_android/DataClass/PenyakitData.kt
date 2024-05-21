package com.example.tugas_akhir_android.DataClass

import com.google.gson.annotations.SerializedName

data class PenyakitData(
    @SerializedName("id") val id: Int,
    @SerializedName("nama_penyakit") val nama_penyakit: String,
    @SerializedName("deskripsi_penyakit") val deskripsi_penyakit: String,
    @SerializedName("gejala_penyakit") val gejala_penyakit: String,
    @SerializedName("penyebab_penyakit") val penyebab_penyakit: String,
    @SerializedName("penyebaran_penyakit") val penyebaran_penyakit: String,
    @SerializedName("cara_pencegahan") val cara_pencegahan: String,
    @SerializedName("cara_penanganan") val cara_penanganan: String,
)


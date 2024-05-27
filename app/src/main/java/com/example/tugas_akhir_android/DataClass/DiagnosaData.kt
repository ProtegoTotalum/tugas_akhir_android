package com.example.tugas_akhir_android.DataClass

import com.google.gson.annotations.SerializedName

data class DiagnosaData(
    @SerializedName("id_diagnosa") val id_diagnosa: Int,
    @SerializedName("nomor_diagnosa") val nomor_diagnosa: String,
    @SerializedName("nama_user") val nama_user: String,
    @SerializedName("tgl_lahir_user") val tgl_lahir_user: String,
    @SerializedName("umur_user") val umur_user: String,
    @SerializedName("gender_user") val gender_user: String,
    @SerializedName("nama_penyakit") val nama_penyakit: String,
    @SerializedName("persentase_hasil") val persentase_hasil: Double,
    @SerializedName("tanggal_diagnosa") val tanggal_diagnosa: String,
    @SerializedName("jam_diagnosa") val jam_diagnosa: String,
)


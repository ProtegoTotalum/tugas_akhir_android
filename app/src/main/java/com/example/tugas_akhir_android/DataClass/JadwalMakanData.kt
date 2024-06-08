package com.example.tugas_akhir_android.DataClass

import com.google.gson.annotations.SerializedName

data class JadwalMakanData(
    @SerializedName("id") val id_jadwal_makan: Int,
    @SerializedName("id_user") val id_user: Int,
    @SerializedName("status_jadwal") val status_jadwal: Int,
    @SerializedName("tipe_jadwal_makan") val tipe_jadwal_makan: String,
    @SerializedName("pengulangan_jadwal_makan") val pengulangan_jadwal_makan: String,
    @SerializedName("waktu_makan") val waktu_makan: String,
    @SerializedName("senin") val senin: Int,
    @SerializedName("selasa") val selasa: Int,
    @SerializedName("rabu") val rabu: Int,
    @SerializedName("kamis") val kamis: Int,
    @SerializedName("jumat") val jumat: Int,
    @SerializedName("sabtu") val sabtu: Int,
    @SerializedName("minggu") val minggu: Int,
)

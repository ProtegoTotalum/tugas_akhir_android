package com.example.tugas_akhir_android.DataClass

import com.google.gson.annotations.SerializedName

data class ObatData(
    @SerializedName("id") val id_obat: Int,
    @SerializedName("nama_obat") val nama_obat: String,
    @SerializedName("jenis_obat") val jenis_obat: String,
    @SerializedName("kegunaan_obat") val kegunaan_obat: String,
    @SerializedName("aturan_minum_obat") val aturan_minum_obat: String,
    @SerializedName("harga_obat") val harga_obat: String,
)

package com.example.tugas_akhir_android.DataClass

import com.google.gson.annotations.SerializedName

data class BahanMakananData(
    @SerializedName("id") val id_bahan_makanan: Int,
    @SerializedName("nama_bahan_makanan") val nama_bahan_makanan: String,
    @SerializedName("takaran(g)") val takaran: String,
    @SerializedName("kalori") val kalori: Double,
    @SerializedName("karbohidrat") val karbohidrat: Double,
    @SerializedName("protein_nabati") val protein_nabati: Double,
    @SerializedName("protein_hewani") val protein_hewani: Double,
    @SerializedName("lemak") val lemak: Double,

)

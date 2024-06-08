package com.example.tugas_akhir_android.DataClass

import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("id") val id: Int,
    @SerializedName("nama_user") val nama: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("tgl_lahir_user") val tgl_lahir_user: String,
    @SerializedName("no_telp_user") val no_telp_user: String,
    @SerializedName("umur_user") val umur_user: String,
    @SerializedName("gender_user") val gender_user: String,
    @SerializedName("alamat_user") val alamat_user: String,
    @SerializedName("kota_user") val kota_user: String,
    @SerializedName("provinsi_user") val provinsi_user: String,
    @SerializedName("role_user") val role_user: String,
    @SerializedName("deaktivasi") val deaktivasi: Int? = null,
)

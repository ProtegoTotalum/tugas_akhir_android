package com.example.tugas_akhir_android.DataClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserResponse {
    @SerializedName("message")
    val message: String? = null

    @SerializedName("user")
    val user: User? = null

    class User {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("id_unique_user")
        @Expose
        var id_unique_user: String? = null

        @SerializedName("nama")
        @Expose
        var nama: String? = null

        @SerializedName("email")
        @Expose
        var email: String? = null

        @SerializedName("password")
        @Expose
        var password: String? = null

        @SerializedName("tgl_lahir_user")
        @Expose
        var tgl_lahir_user: String? = null

        @SerializedName("umur_user")
        @Expose
        var umur_user: String? = null

        @SerializedName("no_telp_user")
        @Expose
        var no_telp_user: String? = null

        @SerializedName("gender_user")
        @Expose
        var gender_user: String? = null

        @SerializedName("role_user")
        @Expose
        var role_user: String? = null

        @SerializedName("deaktivasi")
        @Expose
        var deaktivasi: Int? = null
    }
}

package com.example.tugas_akhir_android

import com.example.tugas_akhir_android.DataClass.ResponseDataPenyakit
import com.example.tugas_akhir_android.DataClass.ResponseDataUser
import com.example.tugas_akhir_android.DataClass.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface api {
    //User dan Autentikasi
    @GET("api/user")
    open fun getDataUser(): Call<ResponseDataUser?>?

    @GET("api/getuser/{id}")
    open fun getDataUser(@Path("id") id: Int): Call<ResponseDataUser>?

    @FormUrlEncoded
    @POST("api/login")
    fun login(
        @Field("email") email:String?,
        @Field("password") password:String?,
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("api/register")
    fun register(
        @Field("nama_user") nama_user: String?,
        @Field("email") email: String?,
        @Field("password") password: String?,
        @Field("tgl_lahir_user") tgl_lahir_user: String?,
        @Field("no_telp_user") no_telp_user: String?,
        @Field("gender_user") gender_user: String?,
    ): Call<UserResponse>

    @FormUrlEncoded
    @PUT("api/updateuser/{id}")
    fun update_user(
        @Field("nama_user") nama_user: String?,
        @Field("tgl_lahir_user") tgl_lahir_user: String?,
        @Field("no_telp_user") no_telp_user: String?,
        @Field("gender_user") gender_user: String?,
        @Path("id") id_user: Int?,
    ): Call<ResponseDataUser>

    @FormUrlEncoded
    @POST("api/changepas/{id}")
    fun change_pas(
        @Field("password_lama") password_lama: String?,
        @Field("password_baru") password_baru: String?,
        @Field("password_konfirmasi") password_konfirmasi: String?,
        @Path("id") id_user: Int?,
    ): Call<ResponseDataUser>

    @GET("api/deaktivasiakun/{id}")
    open fun deaktivasi_akun(@Path("id") id: Int): Call<ResponseDataUser>?





    //Penyakit
    @GET("api/getpenyakit")
    open fun getDataPenyakitAll(): Call<ResponseDataPenyakit>?

    @GET("api/getpenyakit/{id}")
    open fun getDataPenyakit(@Path("id") id: Int): Call<ResponseDataPenyakit>?

    @FormUrlEncoded
    @PUT("api/updatepenyakit/{id}")
    fun update_penyakit(
        @Field("deskripsi_penyakit") deskripsi_penyakit: String?,
        @Field("gejala_penyakit") gejala_penyakit: String?,
        @Field("penyebab_penyakit") penyebab_penyakit: String?,
        @Field("penyebaran_penyakit") penyebaran_penyakit: String?,
        @Field("cara_pencegahan") cara_pencegahan: String?,
        @Field("cara_penanganan") cara_penanganan: String?,
        @Path("id") id_penyakit: Int?,
    ): Call<ResponseDataPenyakit>
}
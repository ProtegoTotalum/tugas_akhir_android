package com.example.tugas_akhir_android

import com.example.tugas_akhir_android.DataClass.JadwalMakanData
import com.example.tugas_akhir_android.DataClass.JawabanUserData
import com.example.tugas_akhir_android.DataClass.PertanyaanData
import com.example.tugas_akhir_android.DataClass.ResponseDataBahanMakanan
import com.example.tugas_akhir_android.DataClass.ResponseDataDiagnosa
import com.example.tugas_akhir_android.DataClass.ResponseDataGejala
import com.example.tugas_akhir_android.DataClass.ResponseDataJadwalMakan
import com.example.tugas_akhir_android.DataClass.ResponseDataKabKot
import com.example.tugas_akhir_android.DataClass.ResponseDataObat
import com.example.tugas_akhir_android.DataClass.ResponseDataPenyakit
import com.example.tugas_akhir_android.DataClass.ResponseDataPertanyaan
import com.example.tugas_akhir_android.DataClass.ResponseDataProvinsi
import com.example.tugas_akhir_android.DataClass.ResponseDataStorePertanyaan
import com.example.tugas_akhir_android.DataClass.ResponseDataUpdateStatusJadwal
import com.example.tugas_akhir_android.DataClass.ResponseDataUser
import com.example.tugas_akhir_android.DataClass.StorePertanyaan
import com.example.tugas_akhir_android.DataClass.StorePertanyaanRequest
import com.example.tugas_akhir_android.DataClass.UserResponse
import retrofit2.Call
import retrofit2.Response
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
        @Field("alamat_user") alamat_user: String?,
        @Field("kota_user") kota_user: String?,
        @Field("provinsi_user") provinsi_user: String?,
    ): Call<UserResponse>

    @FormUrlEncoded
    @PUT("api/updateuser/{id}")
    fun update_user(
        @Field("nama_user") nama_user: String?,
        @Field("tgl_lahir_user") tgl_lahir_user: String?,
        @Field("no_telp_user") no_telp_user: String?,
        @Field("gender_user") gender_user: String?,
        @Field("alamat_user") alamat_user: String?,
        @Field("kota_user") kota_user: String?,
        @Field("provinsi_user") provinsi_user: String?,
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

    @GET("api/getdokter/{provinsi_user}/{kota_user}")
    open fun getDataDokter(
        @Path("provinsi_user") provinsi_user: String,
        @Path("kota_user") kota_user: String
    ): Call<ResponseDataUser>?



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



    //Diagnosa
    @GET("api/showdiagnosauserall/{id}")
    open fun getDataDiagnosaUserAll(@Path("id") id: Int): Call<ResponseDataDiagnosa>?

    @GET("api/showdiagnosauser/{id_user}/{id_diagnosa}")
    open fun getDataDiagnosaUser(
        @Path("id_user") id_user: Int,
        @Path("id_diagnosa") id_diagnosa:Int,
    ): Call<ResponseDataDiagnosa>?

    @GET("api/diagnosa/{id}")
    open fun startdiagnosa(@Path("id") id: Int): Call<ResponseDataDiagnosa>?

    @GET("api/lastdiagnosa/{id}")
    open fun getDataLastDiagnosa(@Path("id") id: Int): Call<ResponseDataDiagnosa>?



    //Pertanyaan
    @GET("api/showpertanyaan/{id_user}/{nomor_diagnosa}")
    open fun getDataJawabanPertanyaanUser(
        @Path("id_user") id_user: Int,
        @Path("nomor_diagnosa") nomor_diagnosa:String,
    ): Call<ResponseDataPertanyaan>?

    @POST("api/storepertanyaan/{id}")
    open fun storePertanyaan(
        @Path("id") id: Int,
        @Body request: StorePertanyaanRequest
    ): Call<ResponseDataStorePertanyaan>



    //BahanMakanan
    @GET("api/getbahanmakanan")
    open fun getDataBahanMakananAll(): Call<ResponseDataBahanMakanan>?

    @FormUrlEncoded
    @POST("api/storebahanmakanan")
    fun storeBahanMakanan(
        @Field("nama_bahan_makanan") nama_bahan_makanan: String?,
        @Field("takaran") takaran: String?,
        @Field("kalori") kalori: Double?,
        @Field("karbohidrat") karbohidrat: Double?,
        @Field("protein_nabati") protein_nabati: Double?,
        @Field("protein_hewani") protein_hewani: Double?,
        @Field("lemak") lemak: Double?,
    ): Call<ResponseDataBahanMakanan>

    @FormUrlEncoded
    @PUT("api/updatebahanmakanan/{id}")
    fun updateBahanMakanan(
        @Field("nama_bahan_makanan") nama_bahan_makanan: String?,
        @Field("takaran") takaran: String?,
        @Field("kalori") kalori: Double?,
        @Field("karbohidrat") karbohidrat: Double?,
        @Field("protein_nabati") protein_nabati: Double?,
        @Field("protein_hewani") protein_hewani: Double?,
        @Field("lemak") lemak: Double?,
        @Path("id") id_penyakit: Int?,
    ): Call<ResponseDataBahanMakanan>

    @GET("api/getbahanmakanandetail/{id}")
    open fun getDataBahanMakanan(@Path("id") id: Int): Call<ResponseDataBahanMakanan>?

    @GET("api/deletebahanmakanan/{id}")
    open fun getDeleteBahanMakanan(@Path("id") id: Int): Call<ResponseDataBahanMakanan>?



    //Gejala
    @GET("api/getgejalaall")
    open fun getDataGejalaAll(): Call<ResponseDataGejala>?



    //Wilayah
    @GET("api/getprovinsi")
    open fun getDataProvinsi(): Call<ResponseDataProvinsi>?

    @GET("api/getkabupatenkota/{id}")
    open fun getDataKabKot(@Path("id") id: Int): Call<ResponseDataKabKot>?



    //JadwalMakan
    @FormUrlEncoded
    @POST("api/storejadwalmakan")
    fun storeJadwalMakan(
        @Field("id_user") id_user: Int?,
        @Field("status_jadwal") status_jadwal: Int?,
        @Field("tipe_jadwal_makan") tipe_jadwal_makan: String?,
        @Field("pengulangan_jadwal_makan") pengulangan_jadwal_makan: String?,
        @Field("waktu_makan") waktu_makan: String?,
        @Field("senin") senin: Int?,
        @Field("selasa") selasa: Int?,
        @Field("rabu") rabu: Int?,
        @Field("kamis") kamis: Int?,
        @Field("jumat") jumat: Int?,
        @Field("sabtu") sabtu: Int?,
        @Field("minggu") minggu: Int?,
    ): Call<ResponseDataJadwalMakan>

    @GET("api/getjadwalmakanuserall/{id_user}")
    open fun getDataJadwalMakanUserAll(@Path("id_user") id_user: Int): Call<ResponseDataJadwalMakan>?

    @GET("api/updatestatusjadwal/{id_jadwal}/{new_status}")
    open fun updateStatusJadwal(
        @Path("id_jadwal") id_jadwal: Int,
        @Path("new_status") new_status:Int,
    ): Call<ResponseDataUpdateStatusJadwal>?

    @GET("api/getjadwalmakan/{id}")
    open fun getDataJadwalMakan(@Path("id") id: Int): Call<ResponseDataJadwalMakan>?

    @FormUrlEncoded
    @PUT("api/updatejadwalmakan/{id}")
    fun updateJadwalMakan(
        @Field("status_jadwal") status_jadwal: Int?,
        @Field("tipe_jadwal_makan") tipe_jadwal_makan: String?,
        @Field("pengulangan_jadwal_makan") pengulangan_jadwal_makan: String?,
        @Field("waktu_makan") waktu_makan: String?,
        @Field("senin") senin: Int?,
        @Field("selasa") selasa: Int?,
        @Field("rabu") rabu: Int?,
        @Field("kamis") kamis: Int?,
        @Field("jumat") jumat: Int?,
        @Field("sabtu") sabtu: Int?,
        @Field("minggu") minggu: Int?,
        @Path("id") id_jadwal_makan: Int?,
    ): Call<ResponseDataJadwalMakan>

    @GET("api/deletejadwalmakan/{id}")
    open fun deleteJadwalMakan(@Path("id") id: Int): Call<ResponseDataJadwalMakan>?



    //Obat
    @GET("api/getobatall")
    open fun getDataObatAll(): Call<ResponseDataObat>?

    @FormUrlEncoded
    @POST("api/storeobat")
    fun storeObat(
        @Field("nama_obat") nama_obat: String?,
        @Field("jenis_obat") jenis_obat: String?,
        @Field("kegunaan_obat") kegunaan_obat: String?,
        @Field("aturan_minum_obat") aturan_minum_obat: String?,
        @Field("harga_obat") harga_obat: String?,
    ): Call<ResponseDataObat>

    @FormUrlEncoded
    @PUT("api/updateobat/{id}")
    fun updateObat(
        @Field("nama_obat") nama_obat: String?,
        @Field("jenis_obat") jenis_obat: String?,
        @Field("kegunaan_obat") kegunaan_obat: String?,
        @Field("aturan_minum_obat") aturan_minum_obat: String?,
        @Field("harga_obat") harga_obat: String?,
        @Path("id") id_obat: Int?,
    ): Call<ResponseDataObat>

    @GET("api/getobat/{id}")
    open fun getDataObat(@Path("id") id: Int): Call<ResponseDataObat>?

    @GET("api/deleteobat/{id}")
    open fun deleteObatDokter(@Path("id") id: Int): Call<ResponseDataObat>?
}
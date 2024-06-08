package com.example.tugas_akhir_android.DataClass

data class StorePertanyaan(
    val id_user: Int,
    val nomor_diagnosa_pertanyan: Int,
    val jawaban_user: List<JawabanUserData>
)

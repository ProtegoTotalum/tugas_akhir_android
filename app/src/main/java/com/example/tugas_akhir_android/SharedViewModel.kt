package com.example.tugas_akhir_android

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tugas_akhir_android.DataClass.BahanMakananData
import com.example.tugas_akhir_android.DataClass.DiagnosaData
import com.example.tugas_akhir_android.DataClass.JadwalMakanData
import com.example.tugas_akhir_android.DataClass.ObatData
import com.example.tugas_akhir_android.DataClass.PenyakitData
import com.example.tugas_akhir_android.DataClass.UserData

class SharedViewModel : ViewModel() {
    val idUser = MutableLiveData<Int>()

    val roleUser = MutableLiveData<String>()

    val provinsiUser = MutableLiveData<String>()

    val kabkotUser = MutableLiveData<String>()

    val userData = MutableLiveData<UserData>()

    val penyakitData = MutableLiveData<PenyakitData>()

    val diagnosaLastData = MutableLiveData<DiagnosaData>()

    val jadwalMakanDataDetail = MutableLiveData<JadwalMakanData>()

    val bahanMakananData = MutableLiveData<BahanMakananData>()

    val obatData = MutableLiveData<ObatData>()

    val diagnosaData = MutableLiveData<List<DiagnosaData>>()

    val jadwalMakanData = MutableLiveData<List<JadwalMakanData>>()

    val bahanMakananRVData = MutableLiveData<List<BahanMakananData>>()

    val obatRVData = MutableLiveData<List<ObatData>>()

    val dataChanged = MutableLiveData<Boolean>()

    fun setDataChanged(changed: Boolean) {
        dataChanged.value = changed
    }



}
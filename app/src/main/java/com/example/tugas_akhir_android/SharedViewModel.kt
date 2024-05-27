package com.example.tugas_akhir_android

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tugas_akhir_android.DataClass.DiagnosaData
import com.example.tugas_akhir_android.DataClass.PenyakitData
import com.example.tugas_akhir_android.DataClass.UserData

class SharedViewModel : ViewModel() {
    val idUser = MutableLiveData<Int>()

    val roleUser = MutableLiveData<String>()

    val userData = MutableLiveData<UserData>()

    val penyakitData = MutableLiveData<PenyakitData>()

    val diagnosaLastData = MutableLiveData<DiagnosaData>()

    val diagnosaData = MutableLiveData<List<DiagnosaData>>()

}
package com.example.tugas_akhir_android

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tugas_akhir_android.DataClass.PenyakitData
import com.example.tugas_akhir_android.DataClass.UserData

class SharedViewModel : ViewModel() {
    val userData = MutableLiveData<UserData>()

    val penyakitData = MutableLiveData<PenyakitData>()
}
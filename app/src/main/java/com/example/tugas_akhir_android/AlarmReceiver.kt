package com.example.tugas_akhir_android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.tugas_akhir_android.DataClass.JadwalMakanData
import com.example.tugas_akhir_android.DataClass.ResponseDataJadwalMakan
import com.example.tugas_akhir_android.DataClass.ResponseDataUpdateStatusJadwal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val jadwalId = intent.getIntExtra("jadwalId", -1)

        // Fetch the JadwalMakanData from your database using jadwalId
        if (jadwalId != -1) {
            val call = RClient.api.getDataJadwalMakan(jadwalId)
            call?.enqueue(object : Callback<ResponseDataJadwalMakan> {
                override fun onResponse(
                    call: Call<ResponseDataJadwalMakan>,
                    response: Response<ResponseDataJadwalMakan>
                ) {
                    Log.d("AlarmReceiverResponse", "onResponseDetail called")
                    Log.d("AlarmReceiverResponse", "Response code: ${response.code()}")
                    Log.d("AlarmReceiverResponse", "Response message: ${response.message()}")
                    Log.d("AlarmReceiverResponse", "Response body: ${response.body()}")
                    if (response.isSuccessful) {
                        response.body()?.let {
                            if (it.message == "Data Jadwal Ditemukan") {
                                val jadwalMakanData = it.data[0] // Assuming the data is a list and taking the first element

                                // Prepare data for notification
                                val data = Data.Builder()
                                    .putString("title", "Waktu Makan")
                                    .putString("message", "Saatnya makan!")
                                    .putInt("notificationId", jadwalId)
                                    .build()

                                val notificationWork = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
                                    .setInputData(data)
                                    .build()

                                WorkManager.getInstance(context).enqueue(notificationWork)

                                // Update status to off if pengulangan_jadwal_makan is "sekali"
                                if (jadwalMakanData.pengulangan_jadwal_makan == "Sekali") {
                                    val updateCall = RClient.api.updateStatusJadwal(jadwalId, 0)
                                    updateCall?.enqueue(object : Callback<ResponseDataUpdateStatusJadwal> {
                                        override fun onResponse(call: Call<ResponseDataUpdateStatusJadwal>, response: Response<ResponseDataUpdateStatusJadwal>) {
                                            if (response.isSuccessful) {
                                                Log.d("AlarmReceiverResponse", "Status updated to off for jadwalId: $jadwalId")
                                            } else {
                                                Log.d("AlarmReceiverResponse", "Failed to update status for jadwalId: $jadwalId")
                                            }
                                        }

                                        override fun onFailure(call: Call<ResponseDataUpdateStatusJadwal>, t: Throwable) {
                                            Log.d("AlarmReceiverResponse", "API call failed to update status", t)
                                        }
                                    })
                                }
                            }
                        }
                    } else {
                        Log.d("AlarmReceiverResponse", "Response is not successful")
                    }
                }

                override fun onFailure(call: Call<ResponseDataJadwalMakan>, t: Throwable) {
                    Log.d("AlarmReceiverResponse", "API call failed", t)
                }
            })
        }
    }
}
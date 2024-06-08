package com.example.tugas_akhir_android.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugas_akhir_android.Adapter.HistoryAdapter
import com.example.tugas_akhir_android.Adapter.JadwalMakanAdapter
import com.example.tugas_akhir_android.AlarmHelper
import com.example.tugas_akhir_android.DataClass.DiagnosaData
import com.example.tugas_akhir_android.DataClass.JadwalMakanData
import com.example.tugas_akhir_android.DataClass.ResponseDataJadwalMakan
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.FragmentRecycleJadwalMakanBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FragmentRecycleJadwalMakan : Fragment() {
    private var _binding: FragmentRecycleJadwalMakanBinding? = null
    private val binding get() = _binding!!
    private val listJadwalMakan = ArrayList<JadwalMakanData>()
    private var id_user: Int? = 0
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecycleJadwalMakanBinding.inflate(inflater, container,false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        id_user = sharedViewModel.idUser.value
        Log.d("FragmentRecycleJadwalMakan", "Received id_user: $id_user")

        sharedViewModel.jadwalMakanData.observe(viewLifecycleOwner, { data ->
            updateRecyclerView(data)
        })

        if (sharedViewModel.jadwalMakanData.value == null) {
            id_user?.let { getDataJadwal(it) }
        } else {
            updateRecyclerView(sharedViewModel.jadwalMakanData.value!!)
        }
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        id_user?.let { getDataJadwal(it) }
    }
    private fun getDataJadwal(id_user:Int){
        binding.rvMealSchedule.setHasFixedSize(true)
        binding.rvMealSchedule.layoutManager = LinearLayoutManager(context)
        binding.progressBar.visibility

        val call = RClient.api.getDataJadwalMakanUserAll(id_user)
        call?.enqueue(object : Callback<ResponseDataJadwalMakan> {
            override fun onResponse(
                call: Call<ResponseDataJadwalMakan>,
                response: Response<ResponseDataJadwalMakan>
            ) {
                Log.d("RecycleJadwalMakanResponse", "onResponse called")
                Log.d("RecycleJadwalMakanResponse", "Response code: ${response.code()}")
                Log.d("RecycleJadwalMakanResponse", "Response message: ${response.message()}")
                Log.d("RecycleJadwalMakanResponse", "Response body: ${response.body()}")
                if(response.isSuccessful){
                    listJadwalMakan.clear()
                    response.body()?.let { responseData ->
                        sharedViewModel.jadwalMakanData.value = responseData.data
                    }

                    binding.progressBar.isVisible = false
                }else{
                    binding.progressBar.isVisible = false
                }
            }

            override fun onFailure(call: Call<ResponseDataJadwalMakan>, t: Throwable) {
                Log.e("RecycleJadwalMakanResponse", "API call failed", t)
                binding.progressBar.isVisible = false
            }
        })
    }

    private fun updateRecyclerView(data: List<JadwalMakanData>) {
        val adapter = JadwalMakanAdapter(ArrayList(data), requireContext(), id_user ?: 0)
        binding.rvMealSchedule.adapter = adapter
        adapter.notifyDataSetChanged()
        data.forEach { scheduleNotifications(it) }
    }

    private fun scheduleNotifications(jadwalMakanData: JadwalMakanData) {
        if (jadwalMakanData.status_jadwal == 1) {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, jadwalMakanData.waktu_makan.substring(0, 2).toInt())
                set(Calendar.MINUTE, jadwalMakanData.waktu_makan.substring(3, 5).toInt())
                set(Calendar.SECOND, 0)
            }

            val days = getSelectedDays(jadwalMakanData)
            for (day in days) {
                calendar.set(Calendar.DAY_OF_WEEK, getDayOfWeek(day))
                AlarmHelper.scheduleNotification(requireContext(), calendar.timeInMillis, jadwalMakanData.id_jadwal_makan)
            }

            if (jadwalMakanData.pengulangan_jadwal_makan == "Sekali") {
                AlarmHelper.scheduleNotification(requireContext(), calendar.timeInMillis, jadwalMakanData.id_jadwal_makan)
            }
        }
    }

    private fun getSelectedDays(jadwalMakanData: JadwalMakanData): List<String> {
        val days = mutableListOf<String>()
        if (jadwalMakanData.senin == 1) days.add("Senin")
        if (jadwalMakanData.selasa == 1) days.add("Selasa")
        if (jadwalMakanData.rabu == 1) days.add("Rabu")
        if (jadwalMakanData.kamis == 1) days.add("Kamis")
        if (jadwalMakanData.jumat == 1) days.add("Jumat")
        if (jadwalMakanData.sabtu == 1) days.add("Sabtu")
        if (jadwalMakanData.minggu == 1) days.add("Minggu")
        return days
    }

    private fun getDayOfWeek(day: String): Int {
        return when (day) {
            "Senin" -> Calendar.MONDAY
            "Selasa" -> Calendar.TUESDAY
            "Rabu" -> Calendar.WEDNESDAY
            "Kamis" -> Calendar.THURSDAY
            "Jumat" -> Calendar.FRIDAY
            "Sabtu" -> Calendar.SATURDAY
            "Minggu" -> Calendar.SUNDAY
            else -> Calendar.MONDAY
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
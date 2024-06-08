package com.example.tugas_akhir_android.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas_akhir_android.DataClass.JadwalMakanData
import com.example.tugas_akhir_android.DataClass.ResponseDataUpdateStatusJadwal
import com.example.tugas_akhir_android.DetailPenyakitActivity
import com.example.tugas_akhir_android.Fragment.FragmentDetailDiagnosaUser
import com.example.tugas_akhir_android.Fragment.FragmentDetailJadwalMakan
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.databinding.JadwalMakanAdapterBinding
import com.shashank.sony.fancytoastlib.FancyToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JadwalMakanAdapter(private val data: ArrayList<JadwalMakanData>, private val context: Context, private val id_user:Int) :
    RecyclerView.Adapter<JadwalMakanAdapter.JadwalMakanViewHolder>() {

    inner class JadwalMakanViewHolder(item: JadwalMakanAdapterBinding) : RecyclerView.ViewHolder(item.root) {
        private val binding = item

        fun bind(jadwalMakanData: JadwalMakanData) {
            with(binding) {
                val id_jadwal_makan = jadwalMakanData.id_jadwal_makan
                // Format waktu menjadi hanya jam dan menit
                val waktu = jadwalMakanData.waktu_makan.substring(0, 5)
                textWaktuMakan.text = waktu

                val pengulangan_jadwal_makan = jadwalMakanData.pengulangan_jadwal_makan

                if (pengulangan_jadwal_makan == "Custom") {
                    val days = getSelectedDays(jadwalMakanData)
                    textPengulanganJadwalMakan.text = days.joinToString(", ")
                } else {
                    textPengulanganJadwalMakan.text = jadwalMakanData.pengulangan_jadwal_makan
                }

                textTipeJadwalMakan.text = jadwalMakanData.tipe_jadwal_makan
                switchStatusJadwalMakan.isChecked = jadwalMakanData.status_jadwal == 1

                switchStatusJadwalMakan.setOnCheckedChangeListener { _, isChecked ->
                    val newStatus = if (isChecked) 1 else 0
                    updateStatusJadwal(id_jadwal_makan, newStatus)
                }

                cvJadwalMakan.setOnClickListener {
                    val fragment = FragmentDetailJadwalMakan()
                    val bundle = Bundle().apply {
                        putInt("id_jadwal_makan", id_jadwal_makan)
                        putInt("id_user", id_user)
                    }
                    fragment.arguments = bundle

                    (context as? AppCompatActivity)?.supportFragmentManager?.beginTransaction()?.apply {
                        replace(R.id.layout_fragment_user, fragment)
                        addToBackStack(null)
                        commit()
                    }
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

        private fun updateStatusJadwal(id: Int, newStatus: Int) {
            // Panggil API untuk memperbarui status_jadwal
            val call = RClient.api.updateStatusJadwal(id, newStatus)
            call?.enqueue(object : Callback<ResponseDataUpdateStatusJadwal> {
                override fun onResponse(call: Call<ResponseDataUpdateStatusJadwal>, response: Response<ResponseDataUpdateStatusJadwal>) {
                    if (response.isSuccessful) {
                        val message = response.body()?.message
                        FancyToast.makeText(
                            context,
                            message,
                            FancyToast.LENGTH_SHORT,
                            FancyToast.INFO,
                            true
                        ).show()
                    } else {
                        val message = response.body()?.message
                        FancyToast.makeText(
                            context,
                            message,
                            FancyToast.LENGTH_SHORT,
                            FancyToast.ERROR,
                            false
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseDataUpdateStatusJadwal>, t: Throwable) {
                    FancyToast.makeText(
                        context,
                        t.toString(),
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                    ).show()
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JadwalMakanViewHolder {
        return JadwalMakanViewHolder(
            JadwalMakanAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: JadwalMakanViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}



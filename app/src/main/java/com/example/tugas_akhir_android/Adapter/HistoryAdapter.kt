package com.example.tugas_akhir_android.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas_akhir_android.DataClass.DiagnosaData
import com.example.tugas_akhir_android.Fragment.FragmentDetailDiagnosaUser
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.databinding.FragmentDetailDiagnosaUserBinding
import com.example.tugas_akhir_android.databinding.HistoryAdapterBinding


class HistoryAdapter(private val data: ArrayList<DiagnosaData>, private val context: Context, private val id_user: Int) :
    RecyclerView.Adapter<HistoryAdapter.HistoryDiagnosaViewHolder>() {

    inner class HistoryDiagnosaViewHolder(item: HistoryAdapterBinding) : RecyclerView.ViewHolder(item.root) {
        private val binding = item
        fun bind(diagnosaData: DiagnosaData) {
            with(binding) {
                percentageHistoryAdapter.text = String.format("%.2f%%", diagnosaData.persentase_hasil)
                // Format and display date
                val formattedDate = formatDateString(diagnosaData.tanggal_diagnosa)
                textDateHistoryAdapter.text = formattedDate
                // Display time as is, assuming it is already in hh:mm:ss format
                textTimeHistoryAdapter.text = diagnosaData.jam_diagnosa
                textDiagnoseHistoryAdapter.text = diagnosaData.nama_penyakit
                val id_diagnosa = diagnosaData.id_diagnosa
                val nomor_diagnosa = diagnosaData.nomor_diagnosa
                cvHistoryAdapter.setOnClickListener {
                    val fragment = FragmentDetailDiagnosaUser()
                    val bundle = Bundle().apply {
                        putInt("id_diagnosa", id_diagnosa)
                        putInt("id_user", id_user)
                        putString("nomor_diagnosa", nomor_diagnosa)
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryDiagnosaViewHolder {
        return HistoryDiagnosaViewHolder(
            HistoryAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HistoryDiagnosaViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    companion object {
        const val YOUR_REQUEST_CODE = 121 // Ganti dengan kode permintaan yang sesuai
    }

    private fun formatDateString(dateString: String): String {
        // Assuming dateString is in the format yyyyMMdd
        return if (dateString.length == 8) {
            val year = dateString.substring(0, 4)
            val month = dateString.substring(4, 6)
            val day = dateString.substring(6, 8)
            "$year-$month-$day"
        } else {
            dateString // Return original if format is unexpected
        }
    }
}
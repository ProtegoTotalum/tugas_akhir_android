package com.example.tugas_akhir_android.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas_akhir_android.DataClass.PertanyaanData
import com.example.tugas_akhir_android.databinding.PertanyaanDetailHistoryAdapterBinding



class PertanyaanDetailHistoryAdapter(private val data: ArrayList<PertanyaanData>, private val context: Context) :
    RecyclerView.Adapter<PertanyaanDetailHistoryAdapter.PertanyaanDetailHistoryViewHolder>() {

    inner class PertanyaanDetailHistoryViewHolder(item: PertanyaanDetailHistoryAdapterBinding) : RecyclerView.ViewHolder(item.root) {
        private val binding = item
        fun bind(pertanyaanData: PertanyaanData) {
            with(binding) {
                namaGejalaPertanyaanDetailHistory.text = pertanyaanData.nama_gejala
                jawabanPertanyaanDetailHistory.text = pertanyaanData.jawaban_user
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PertanyaanDetailHistoryViewHolder {
        return PertanyaanDetailHistoryViewHolder(
            PertanyaanDetailHistoryAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PertanyaanDetailHistoryViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

}
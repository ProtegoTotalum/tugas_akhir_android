package com.example.tugas_akhir_android.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas_akhir_android.DataClass.GejalaData
import com.example.tugas_akhir_android.databinding.PertanyaanAdapterBinding

class PertanyaanAdapter(private val data: ArrayList<GejalaData>, private val context: Context) :
    RecyclerView.Adapter<PertanyaanAdapter.PertanyaanViewHolder>() {

    inner class PertanyaanViewHolder(item: PertanyaanAdapterBinding) : RecyclerView.ViewHolder(item.root) {
        private val binding = item
        fun bind(gejalaData: GejalaData) {
            with(binding) {
                txtNamaGejalaPertanyaanAdapter.text = gejalaData.nama_gejala
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PertanyaanViewHolder {
        return PertanyaanViewHolder(
            PertanyaanAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PertanyaanViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

}
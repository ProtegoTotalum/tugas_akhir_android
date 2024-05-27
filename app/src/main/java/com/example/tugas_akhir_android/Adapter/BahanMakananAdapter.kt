package com.example.tugas_akhir_android.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas_akhir_android.DataClass.BahanMakananData
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.databinding.BahanMakananAdapterBinding

class BahanMakananAdapter(private val data: ArrayList<BahanMakananData>, private val context: Context) :
    RecyclerView.Adapter<BahanMakananAdapter.BahanMakananViewHolder>() {

    inner class BahanMakananViewHolder(item: BahanMakananAdapterBinding) : RecyclerView.ViewHolder(item.root) {
        private val binding = item
        private lateinit var hiddenView: LinearLayout
        private lateinit var cardView: CardView
        fun bind(bahanMakananData: BahanMakananData) {
            with(binding) {
                textNamaBahanMakanan.text = bahanMakananData.nama_bahan_makanan
                cardView = binding.cvBahanMakanan
                val arrow = binding.expandFoodBtn

                hiddenView = binding.layoutHidden

                arrow.setOnClickListener {
                    if (hiddenView.visibility == View.VISIBLE) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                        }
                        hiddenView.visibility = View.GONE
                        arrow.setImageResource(R.drawable.ic_expand_more_24)
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                        }
                        hiddenView.visibility = View.VISIBLE
                        etTakaranBahanMakanan.text = bahanMakananData.takaran
                        etKaloriBahanMakanan.text = String.format("%.2f kkal", bahanMakananData.kalori)
                        etKarboBahanMakanan.text = String.format("%.2f gr", bahanMakananData.karbohidrat)
                        etProNBahanMakanan.text = String.format("%.2f gr", bahanMakananData.protein_nabati)
                        etProHBahanMakanan.text =String.format("%.2f gr", bahanMakananData.protein_hewani)
                        etLemakBahanMakanan.text = String.format("%.2f gr", bahanMakananData.lemak)
                        arrow.setImageResource(R.drawable.ic_expand_less_24)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BahanMakananViewHolder {
        return BahanMakananViewHolder(
            BahanMakananAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BahanMakananViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    companion object {
        const val YOUR_REQUEST_CODE = 123 // Ganti dengan kode permintaan yang sesuai
    }
}
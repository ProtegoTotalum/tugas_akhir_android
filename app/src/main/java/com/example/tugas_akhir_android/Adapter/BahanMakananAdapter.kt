package com.example.tugas_akhir_android.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas_akhir_android.DataClass.BahanMakananData
import com.example.tugas_akhir_android.DataClass.ResponseDataBahanMakanan
import com.example.tugas_akhir_android.Fragment.FragmentEditBahanMakananDokter
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.BahanMakananAdapterBinding
import com.shashank.sony.fancytoastlib.FancyToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class BahanMakananAdapter(private val data: ArrayList<BahanMakananData>, private val context: Context, private val id_user:Int, private val sharedViewModel: SharedViewModel):
    RecyclerView.Adapter<BahanMakananAdapter.BahanMakananViewHolder>() {

    inner class BahanMakananViewHolder(item: BahanMakananAdapterBinding) : RecyclerView.ViewHolder(item.root) {
        private val binding = item
        private lateinit var hiddenView: LinearLayout
        private lateinit var cardView: CardView

        fun bind(bahanMakananData: BahanMakananData) {
            with(binding) {
                textNamaBahanMakanan.text = bahanMakananData.nama_bahan_makanan
                val id_bahan_makanan = bahanMakananData.id_bahan_makanan

                binding.editFoodBtn.setOnClickListener{
                    val fragment = FragmentEditBahanMakananDokter()
                    val bundle = Bundle().apply {
                        putInt("id_bahan_makanan", id_bahan_makanan)
                        putInt("id_user", id_user)
                    }
                    fragment.arguments = bundle

                    (context as? AppCompatActivity)?.supportFragmentManager?.beginTransaction()?.apply {
                        replace(R.id.layout_fragment_dokter, fragment)
                        addToBackStack(null)
                        commit()
                    }
                }

                binding.deleteFoodBtn.setOnClickListener{
                    AlertDialog.Builder(context).apply {
                        setTitle("Hapus Jadwal")
                        setMessage("Apakah Anda yakin ingin menghapus bahan makanan ini?")
                        setPositiveButton("Ya") { dialog, which ->
                            id_bahan_makanan.let { hapusBahanMakanan(it,adapterPosition) }
                        }
                        setNegativeButton("Tidak") { dialog, which ->
                            dialog.dismiss()
                        }
                        create()
                        show()
                    }
                }

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

    private fun hapusBahanMakanan(id_bahan_makanan: Int,position: Int){
        val call = RClient.api.getDeleteBahanMakanan(id_bahan_makanan)

        call?.enqueue(object : Callback<ResponseDataBahanMakanan> {
            override fun onResponse(call: Call<ResponseDataBahanMakanan>, response: Response<ResponseDataBahanMakanan>) {
                if (response.isSuccessful) {
                    FancyToast.makeText(
                        context,
                        "Berhasil Delete Bahan Makanan",
                        FancyToast.LENGTH_LONG,
                        FancyToast.SUCCESS,
                        false)
                        .show()
                    data.removeAt(position)
                    notifyItemRemoved(position)
                    sharedViewModel.setDataChanged(true)
                } else {
                    FancyToast.makeText(context, "Gagal Delete Bahan Makanan: ${response.message()}", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show()
                }
            }

            override fun onFailure(call: Call<ResponseDataBahanMakanan>, t: Throwable) {
                FancyToast.makeText(
                    context,
                    "Error: ${t.message}",
                    FancyToast.LENGTH_LONG,
                    FancyToast.ERROR,
                    false)
                    .show()
                Log.e("DeleteBahanMakanan", "API call failed", t)
            }
        })
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
package com.example.tugas_akhir_android.Adapter

import android.content.Context
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
import com.example.tugas_akhir_android.DataClass.ObatData
import com.example.tugas_akhir_android.DataClass.ResponseDataObat
import com.example.tugas_akhir_android.Fragment.FragmentEditObatDokter
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.ObatDokterAdapterBinding
import com.shashank.sony.fancytoastlib.FancyToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ObatDokterAdapter(private val data: ArrayList<ObatData>, private val context: Context, private val id_user:Int, private val sharedViewModel: SharedViewModel):
    RecyclerView.Adapter<ObatDokterAdapter.ObatDokterViewHolder>() {

    inner class ObatDokterViewHolder(item: ObatDokterAdapterBinding) : RecyclerView.ViewHolder(item.root) {
        private val binding = item
        private lateinit var hiddenView: LinearLayout
        private lateinit var cardView: CardView

        fun bind(obatData: ObatData) {
            with(binding) {
                textNamaObat.text = obatData.nama_obat
                val id_obat = obatData.id_obat

                binding.editMedicineBtn.setOnClickListener{
                    val fragment = FragmentEditObatDokter()
                    val bundle = Bundle().apply {
                        putInt("id_obat", id_obat)
                        putInt("id_user", id_user)
                    }
                    fragment.arguments = bundle

                    (context as? AppCompatActivity)?.supportFragmentManager?.beginTransaction()?.apply {
                        replace(R.id.layout_fragment_dokter, fragment)
                        addToBackStack(null)
                        commit()
                    }
                }

                binding.deleteMedicineBtn.setOnClickListener{
                    AlertDialog.Builder(context).apply {
                        setTitle("Hapus Jadwal")
                        setMessage("Apakah Anda yakin ingin menghapus bahan makanan ini?")
                        setPositiveButton("Ya") { dialog, which ->
                            id_obat.let { hapusObat(it,adapterPosition) }
                        }
                        setNegativeButton("Tidak") { dialog, which ->
                            dialog.dismiss()
                        }
                        create()
                        show()
                    }
                }

                cardView = binding.cvObatDokter
                val arrow = binding.expandMedicineBtn

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
                        etJenisObat.text = obatData.jenis_obat
                        etKegunaanObat.text = obatData.kegunaan_obat
                        etAturanMinumObat.text = obatData.aturan_minum_obat
                        etHargaObat.text = obatData.harga_obat
                        arrow.setImageResource(R.drawable.ic_expand_less_24)
                    }
                }
            }
        }
    }

    private fun hapusObat(id_obat: Int,position: Int){
        val call = RClient.api.deleteObatDokter(id_obat)

        call?.enqueue(object : Callback<ResponseDataObat> {
            override fun onResponse(call: Call<ResponseDataObat>, response: Response<ResponseDataObat>) {
                if (response.isSuccessful) {
                    FancyToast.makeText(
                        context,
                        "Berhasil Delete Obat",
                        FancyToast.LENGTH_LONG,
                        FancyToast.SUCCESS,
                        false)
                        .show()
                    data.removeAt(position)
                    notifyItemRemoved(position)
                    sharedViewModel.setDataChanged(true)
                } else {
                    FancyToast.makeText(context, "Gagal Delete Obat: ${response.message()}", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show()
                }
            }

            override fun onFailure(call: Call<ResponseDataObat>, t: Throwable) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObatDokterViewHolder {
        return ObatDokterViewHolder(
            ObatDokterAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ObatDokterViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size


    companion object {
        const val YOUR_REQUEST_CODE = 123 // Ganti dengan kode permintaan yang sesuai
    }
}
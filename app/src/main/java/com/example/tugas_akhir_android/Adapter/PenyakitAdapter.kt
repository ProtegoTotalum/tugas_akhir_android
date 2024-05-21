package com.example.tugas_akhir_android.Adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas_akhir_android.DataClass.PenyakitData
import com.example.tugas_akhir_android.databinding.PenyakitAdapterBinding
import android.content.Context
import com.example.tugas_akhir_android.DetailPenyakitActivity

class PenyakitAdapter(private val data: ArrayList<PenyakitData>, private val context: Context, private val role_user: String?) :
    RecyclerView.Adapter<PenyakitAdapter.PenyakitViewHolder>() {

        inner class PenyakitViewHolder(item: PenyakitAdapterBinding) : RecyclerView.ViewHolder(item.root) {
            private val binding = item
            fun bind(penyakitData: PenyakitData) {
                with(binding) {
                    textNamaPenyakit.text = penyakitData.nama_penyakit
                    val id_penyakit = penyakitData.id
                    cvPenyakit.setOnClickListener {
                        val i = Intent(context, DetailPenyakitActivity::class.java).apply {
                            putExtra("id_penyakit", id_penyakit)
                            putExtra("role_user", role_user)
                        }
                        (context as? Activity)?.startActivityForResult(i, YOUR_REQUEST_CODE)
//                        var i = Intent(
//                            context,
//                            DetailPenyakitActivity::class.java
//                        ).apply {
//                            putExtra("id_penyakit", id_penyakit)
//                        }
//                        context.startActivity(i)
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PenyakitViewHolder {
            return PenyakitViewHolder(
                PenyakitAdapterBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: PenyakitViewHolder, position: Int) {
            holder.bind(data[position])
        }

        override fun getItemCount(): Int = data.size

        companion object {
            const val YOUR_REQUEST_CODE = 123 // Ganti dengan kode permintaan yang sesuai
        }
    }

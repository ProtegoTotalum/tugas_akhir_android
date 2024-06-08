package com.example.tugas_akhir_android.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas_akhir_android.DataClass.GejalaData
import com.example.tugas_akhir_android.DataClass.JawabanUserData
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.databinding.PertanyaanAdapterBinding

class PertanyaanAdapter(private val data: ArrayList<GejalaData>, private val context: Context) :
    RecyclerView.Adapter<PertanyaanAdapter.PertanyaanViewHolder>() {

    private val userAnswers = mutableMapOf<Int, String>()
    private var btnRGrup : RadioGroup? = null

    inner class PertanyaanViewHolder(item: PertanyaanAdapterBinding) : RecyclerView.ViewHolder(item.root) {
        private val binding = item

        fun bind(gejalaData: GejalaData) {
            with(binding) {
                txtNamaGejalaPertanyaanAdapter.text = gejalaData.nama_gejala

                // Set initial state based on previously selected answer
                val selectedAnswer = userAnswers[gejalaData.id_gejala]
                selectedAnswer?.let {
                    for (i in 0 until radioGroupPertanyaan.childCount) {
                        val radioButton = radioGroupPertanyaan.getChildAt(i) as RadioButton
                        if (radioButton.text == it) {
                            radioButton.isChecked = true
                            break
                        }
                    }
                }

                radioGroupPertanyaan.setOnCheckedChangeListener { _, checkedId ->
                    val selectedRadioButton = itemView.findViewById<RadioButton>(checkedId)
                    val jawaban_user = selectedRadioButton?.text?.toString() ?: ""
                    userAnswers[gejalaData.id_gejala] = jawaban_user
                }

//                when (userAnswers[gejalaData.id_gejala]) {
//                    "Sangat Yakin" -> radioGroupPertanyaan.check(R.id.regRadioSangatYakin)
//                    "Yakin" -> radioGroupPertanyaan.check(R.id.regRadioYakin)
//                    "Cukup Yakin" -> radioGroupPertanyaan.check(R.id.regRadioCukupYakin)
//                    "Sedikit Yakin" -> radioGroupPertanyaan.check(R.id.regRadioSedikitYakin)
//                    "Tidak Yakin" -> radioGroupPertanyaan.check(R.id.regRadioTidakYakin)
//                    "Tidak Merasakan Sama Sekali" -> radioGroupPertanyaan.check(R.id.regRadioTidakMerasakan)
//                    else -> radioGroupPertanyaan.clearCheck()
//                }
//
//                // Listen to RadioGroup changes and save the selected answer
//                radioGroupPertanyaan.setOnCheckedChangeListener { _, checkedId ->
//                    val answer = when (checkedId) {
//                        R.id.regRadioSangatYakin -> "Sangat Yakin"
//                        R.id.regRadioYakin -> "Yakin"
//                        R.id.regRadioCukupYakin -> "Cukup Yakin"
//                        R.id.regRadioSedikitYakin -> "Sedikit Yakin"
//                        R.id.regRadioTidakYakin -> "Tidak Yakin"
//                        R.id.regRadioTidakMerasakan -> "Tidak Merasakan Sama Sekali"
//                        else -> ""
//                    }
//                    userAnswers[gejalaData.id_gejala] = answer
//                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PertanyaanViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PertanyaanAdapterBinding.inflate(inflater, parent, false)
        return PertanyaanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PertanyaanViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun getUserAnswers(): List<JawabanUserData> {
        return data.map { gejalaData ->
            JawabanUserData(
                id_gejala = gejalaData.id_gejala,
                jawaban_user = userAnswers[gejalaData.id_gejala]?: ""
            )
        }
    }
}
package com.example.tugas_akhir_android.Fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugas_akhir_android.Adapter.HistoryAdapter
import com.example.tugas_akhir_android.Adapter.PertanyaanDetailHistoryAdapter
import com.example.tugas_akhir_android.DataClass.DiagnosaData
import com.example.tugas_akhir_android.DataClass.PertanyaanData
import com.example.tugas_akhir_android.DataClass.ResponseDataDiagnosa
import com.example.tugas_akhir_android.DataClass.ResponseDataPertanyaan
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.databinding.FragmentDetailDiagnosaUserBinding
import com.shashank.sony.fancytoastlib.FancyToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentDetailDiagnosaUser : Fragment() {
    private var _binding: FragmentDetailDiagnosaUserBinding? = null
    private val binding get() = _binding!!
    private val listPertanyaan = ArrayList<PertanyaanData>()
    private val listDiagnosa = ArrayList<DiagnosaData>()
    private var id_user: Int? = null
    private var id_diagnosa: Int? = null
    private var nomor_diagnosa: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailDiagnosaUserBinding.inflate(inflater, container,false)
        id_user = arguments?.getInt("id_user")
        id_diagnosa = arguments?.getInt("id_diagnosa")
        nomor_diagnosa = arguments?.getString("nomor_diagnosa")
        Log.d("DetailDiagnosaUserResponse", "Received id_diagnosa: $id_diagnosa")
        Log.d("DetailDiagnosaUserResponse", "Received id_user: $id_user")
        Log.d("DetailDiagnosaUserResponse", "Received nomor_diagnosa: $nomor_diagnosa")

        id_user?.let { id_diagnosa?.let { it1 -> getDataDetailDiagnosa(it, it1) } }
        id_user?.let { nomor_diagnosa?.let { it1 -> getDataPertanyaan(it, it1) } }

        binding.backButtonHasilDiagnosa.setOnClickListener {
            parentFragmentManager.popBackStack() // Navigate back to previous fragment
        }
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        id_user?.let { id_diagnosa?.let { it1 -> getDataDetailDiagnosa(it, it1) } }
        id_user?.let { nomor_diagnosa?.let { it1 -> getDataPertanyaan(it, it1) } }
    }


    fun getDataDetailDiagnosa(id_user: Int, id_diagnosa: Int) {

        val call = RClient.api.getDataDiagnosaUser(id_user, id_diagnosa)
        call?.enqueue(object : Callback<ResponseDataDiagnosa> {
            override fun onResponse(
                call: Call<ResponseDataDiagnosa>,
                response: Response<ResponseDataDiagnosa>
            ) {
                Log.d("DetailDiagnosaUserResponse", "onResponseDetail called")
                Log.d("DetailDiagnosaUserResponse", "Response code: ${response.code()}")
                Log.d("DetailDiagnosaUserResponse", "Response message: ${response.message()}")
                Log.d("DetailDiagnosaUserResponse", "Response body: ${response.body()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        listDiagnosa.clear() // Clear the list before adding new data
                        listDiagnosa.addAll(it.data)
                        Log.d("DetailDiagnosaUserResponse", listDiagnosa.toString())
                        Log.d("DetailDiagnosaUserResponse", "List Content: $listDiagnosa")
                        updateUIWithDetailDiagnosaData()
                    }
                }else{
                    Log.d("DetailDiagnosaUserResponse", "Response is not successful")
                }
            }

            override fun onFailure(call: Call<ResponseDataDiagnosa>, t: Throwable) {
                Log.d("DetailDiagnosaUserResponse", "API call failed")
            }
        })
    }

    private fun updateUIWithDetailDiagnosaData() {
        if (listDiagnosa.isNotEmpty()) {
            with(binding) {
                textDateDetailHistory.text = listDiagnosa[0].tanggal_diagnosa
                textTimeDetailHistory.text = listDiagnosa[0].jam_diagnosa
                textDiagnoseDetailHistory.text = listDiagnosa[0].nama_penyakit
                percentageDetailHistory.text = String.format("%.2f%%", listDiagnosa[0].persentase_hasil)
            }
        }else{
            FancyToast.makeText(
                requireContext(), "Data Kosong!", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false
            ).show()
        }
    }

    private fun getDataPertanyaan(id_user:Int, nomor_diagnosa:String){
        binding.rvQuestionDetailHistory.setHasFixedSize(true)
        binding.rvQuestionDetailHistory.layoutManager = LinearLayoutManager(context)
        binding.progressBar.visibility

        val call = RClient.api.getDataJawabanPertanyaanUser(id_user, nomor_diagnosa)
        call?.enqueue(object : Callback<ResponseDataPertanyaan> {
            override fun onResponse(
                call: Call<ResponseDataPertanyaan>,
                response: Response<ResponseDataPertanyaan>
            ) {
                Log.d("DetailDiagnosaPertanyaanResponse", "onResponse called")
                Log.d("DetailDiagnosaPertanyaanResponse", "Response code: ${response.code()}")
                Log.d("DetailDiagnosaPertanyaanResponse", "Response message: ${response.message()}")
                Log.d("DetailDiagnosaPertanyaanResponse", "Response body: ${response.body()}")
                if(response.isSuccessful){
                    listPertanyaan.clear()
                    response.body()?.let { responseData ->
                        responseData.data.forEach { dataItem ->
                            listPertanyaan.add(dataItem)
                        }
                    }
                    val adapter = PertanyaanDetailHistoryAdapter (listPertanyaan, requireContext())
                    binding.rvQuestionDetailHistory.adapter = adapter
                    adapter.notifyDataSetChanged()
                    binding.progressBar.isVisible = false
                }else{
                    Log.d("DetailDiagnosaPertanyaanResponse", "Response is not successful")
                }
            }

            override fun onFailure(call: Call<ResponseDataPertanyaan>, t: Throwable) {
                Log.e("DetailDiagnosaPertanyaanResponse", "API call failed", t)
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
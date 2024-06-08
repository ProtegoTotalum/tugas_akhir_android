package com.example.tugas_akhir_android.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugas_akhir_android.Adapter.BahanMakananAdapter
import com.example.tugas_akhir_android.Adapter.HistoryAdapter
import com.example.tugas_akhir_android.DataClass.BahanMakananData
import com.example.tugas_akhir_android.DataClass.DiagnosaData
import com.example.tugas_akhir_android.DataClass.ResponseDataBahanMakanan
import com.example.tugas_akhir_android.DataClass.ResponseDataDiagnosa
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.FragmentRVMakananDokterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentRVMakananDokter : Fragment() {
    private var _binding: FragmentRVMakananDokterBinding? = null
    private val binding get() = _binding!!
    private val listBahanMakanan = ArrayList<BahanMakananData>()
    private lateinit var sharedViewModel: SharedViewModel
    private var role_user: String? = null
    private var id_user: Int? = 0
    private lateinit var originalData: List<BahanMakananData>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRVMakananDokterBinding.inflate(inflater, container,false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        role_user = sharedViewModel.roleUser.value
        id_user = sharedViewModel.idUser.value
        Log.d("FragmentRVBahanMakanan", "Received role_user: $role_user")
        Log.d("FragmentRVBahanMakanan", "Received id_user: $id_user")


        sharedViewModel.bahanMakananRVData.observe(viewLifecycleOwner, { data ->
            originalData = data
            updateRecyclerView(data)
        })

        sharedViewModel.dataChanged.observe(viewLifecycleOwner, { changed ->
            if (changed == true) {
                getDataBahanMakanan()
                sharedViewModel.setDataChanged(false)
            }
        })

        if (sharedViewModel.bahanMakananRVData.value == null) {
            getDataBahanMakanan()
        } else {
            originalData = sharedViewModel.bahanMakananRVData.value!!
            updateRecyclerView(sharedViewModel.bahanMakananRVData.value!!)
        }
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        getDataBahanMakanan()
    }
    private fun getDataBahanMakanan(){
        _binding?.let { binding -> // Use safe call
            binding.rvFoodDoctor.setHasFixedSize(true)
            binding.rvFoodDoctor.layoutManager = LinearLayoutManager(context)
            binding.progressBar.visibility

            val call = RClient.api.getDataBahanMakananAll()
            call?.enqueue(object : Callback<ResponseDataBahanMakanan> {
                override fun onResponse(
                    call: Call<ResponseDataBahanMakanan>,
                    response: Response<ResponseDataBahanMakanan>
                ) {
                    Log.d("FragmentRVBahanMakananResponse", "onResponse called")
                    Log.d("FragmentRVBahanMakananResponse", "Response code: ${response.code()}")
                    Log.d("FragmentRVBahanMakananResponse", "Response message: ${response.message()}")
                    Log.d("FragmentRVBahanMakananResponse", "Response body: ${response.body()}")
                    if (response.isSuccessful) {
                        listBahanMakanan.clear()
                        response.body()?.let { responseData ->
                            sharedViewModel.bahanMakananRVData.value = responseData.data
                        }
                        binding.progressBar.isVisible = false
                    } else {
                        binding.progressBar.isVisible = false
                    }
                }

                override fun onFailure(call: Call<ResponseDataBahanMakanan>, t: Throwable) {
                    Log.e("FragmentRVBahanMakananResponse", "API call failed", t)
                }
            })
        }

    }

    private fun updateRecyclerView(data: List<BahanMakananData>) {
        val adapter = BahanMakananAdapter(ArrayList(data), requireContext(), id_user ?: 0, sharedViewModel)
        binding.rvFoodDoctor.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun filterData(query: String) {
        val filteredData = originalData.filter { it.nama_bahan_makanan.contains(query, true) }
        updateRecyclerView(filteredData)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
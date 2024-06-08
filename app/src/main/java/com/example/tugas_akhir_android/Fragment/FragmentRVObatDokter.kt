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
import com.example.tugas_akhir_android.Adapter.ObatDokterAdapter
import com.example.tugas_akhir_android.DataClass.ObatData
import com.example.tugas_akhir_android.DataClass.ResponseDataObat
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.FragmentRVObatDokterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentRVObatDokter : Fragment() {
    private var _binding: FragmentRVObatDokterBinding? = null
    private val binding get() = _binding!!
    private val listObat = ArrayList<ObatData>()
    private lateinit var sharedViewModel: SharedViewModel
    private var role_user: String? = null
    private var id_user: Int? = 0
    private lateinit var originalData: List<ObatData>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRVObatDokterBinding.inflate(inflater, container,false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        role_user = sharedViewModel.roleUser.value
        id_user = sharedViewModel.idUser.value
        Log.d("FragmentRVObatDokter", "Received role_user: $role_user")
        Log.d("FragmentRVObatDokter", "Received id_user: $id_user")


        sharedViewModel.obatRVData.observe(viewLifecycleOwner, { data ->
            originalData = data
            updateRecyclerView(data)
        })

        sharedViewModel.dataChanged.observe(viewLifecycleOwner, { changed ->
            if (changed == true) {
                getDataObat()
                sharedViewModel.setDataChanged(false)
            }
        })

        if (sharedViewModel.obatRVData.value == null) {
            getDataObat()
        } else {
            originalData = sharedViewModel.obatRVData.value!!
            updateRecyclerView(sharedViewModel.obatRVData.value!!)
        }
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        getDataObat()
    }
    private fun getDataObat(){
        _binding?.let { binding ->
            binding.rvMedicineDoctor.setHasFixedSize(true)
            binding.rvMedicineDoctor.layoutManager = LinearLayoutManager(context)
            binding.progressBar.visibility

            val call = RClient.api.getDataObatAll()
            call?.enqueue(object : Callback<ResponseDataObat> {
                override fun onResponse(
                    call: Call<ResponseDataObat>,
                    response: Response<ResponseDataObat>
                ) {
                    Log.d("FragmentRVObatDokterResponse", "onResponse called")
                    Log.d("FragmentRVObatDokterResponse", "Response code: ${response.code()}")
                    Log.d("FragmentRVObatDokterResponse", "Response message: ${response.message()}")
                    Log.d("FragmentRVObatDokterResponse", "Response body: ${response.body()}")
                    if (response.isSuccessful) {
                        listObat.clear()
                        response.body()?.let { responseData ->
                            sharedViewModel.obatRVData.value = responseData.data
                            sharedViewModel.obatRVData.value = responseData.data
                        }
                        binding.progressBar.isVisible = false
                    } else {
                        binding.progressBar.isVisible = false
                    }
                }

                override fun onFailure(call: Call<ResponseDataObat>, t: Throwable) {
                    Log.e("FragmentRVObatDokterResponse", "API call failed", t)
                }
            })
        }

    }

    private fun updateRecyclerView(data: List<ObatData>) {
        val adapter = ObatDokterAdapter(ArrayList(data), requireContext(), id_user ?: 0, sharedViewModel)
        binding.rvMedicineDoctor.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun filterData(query: String) {
        val filteredData = originalData.filter { it.nama_obat.contains(query, true) }
        updateRecyclerView(filteredData)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
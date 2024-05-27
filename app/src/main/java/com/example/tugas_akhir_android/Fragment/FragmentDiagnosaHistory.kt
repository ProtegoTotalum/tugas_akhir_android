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
import com.example.tugas_akhir_android.Adapter.HistoryAdapter
import com.example.tugas_akhir_android.DataClass.DiagnosaData
import com.example.tugas_akhir_android.DataClass.ResponseDataDiagnosa
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.FragmentDiagnosaHistoryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentDiagnosaHistory : Fragment() {
    private var _binding: FragmentDiagnosaHistoryBinding? = null
    private val binding get() = _binding!!
    private val listDiagnosa= ArrayList<DiagnosaData>()
    private lateinit var sharedViewModel: SharedViewModel
    private var id_user: Int? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiagnosaHistoryBinding.inflate(inflater, container,false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
//        id_user = arguments?.getInt("id_user")
        id_user = sharedViewModel.idUser.value
        Log.d("DiagnosaHistoryResponse", "Received id_user: $id_user")

        sharedViewModel.diagnosaData.observe(viewLifecycleOwner, { data ->
            updateRecyclerView(data)
        })

        if (sharedViewModel.diagnosaData.value == null) {
            id_user?.let { getDataDiagnosaAll(it) }
        } else {
            updateRecyclerView(sharedViewModel.diagnosaData.value!!)
        }
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        id_user?.let {
            getDataDiagnosaAll(it)
        }
    }
    private fun getDataDiagnosaAll(id_user:Int){
        _binding?.let { binding -> // Use safe call
            binding.rvHistoryDiagnosa.setHasFixedSize(true)
            binding.rvHistoryDiagnosa.layoutManager = LinearLayoutManager(context)
            binding.progressBar.visibility = View.VISIBLE

            val call = RClient.api.getDataDiagnosaUserAll(id_user)
            call?.enqueue(object : Callback<ResponseDataDiagnosa> {
                override fun onResponse(
                    call: Call<ResponseDataDiagnosa>,
                    response: Response<ResponseDataDiagnosa>
                ) {
                    Log.d("DiagnosaHistoryResponse", "onResponse called")
                    Log.d("DiagnosaHistoryResponse", "Response code: ${response.code()}")
                    Log.d("DiagnosaHistoryResponse", "Response message: ${response.message()}")
                    Log.d("DiagnosaHistoryResponse", "Response body: ${response.body()}")
                    if (response.isSuccessful) {
                        listDiagnosa.clear()
                        response.body()?.let { responseData ->
//                            responseData.data.forEach { dataItem ->
//                                listDiagnosa.add(dataItem)
//                            }
                            sharedViewModel.diagnosaData.value = responseData.data
                        }
//                        val adapter = HistoryAdapter(listDiagnosa, requireContext(), id_user)
//                        binding.rvHistoryDiagnosa.adapter = adapter
//                        adapter.notifyDataSetChanged()
                        binding.progressBar.isVisible = false
                    } else {
                        binding.progressBar.isVisible = false
                    }
                }

                override fun onFailure(call: Call<ResponseDataDiagnosa>, t: Throwable) {
                    Log.e("DiagnosaHistoryResponse", "API call failed", t)
                }
            })
        }
    }

    private fun updateRecyclerView(data: List<DiagnosaData>) {
        val adapter = HistoryAdapter(ArrayList(data), requireContext(), id_user ?: 0)
        binding.rvHistoryDiagnosa.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
package com.example.tugas_akhir_android.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugas_akhir_android.Adapter.BahanMakananAdapter
import com.example.tugas_akhir_android.DataClass.BahanMakananData
import com.example.tugas_akhir_android.DataClass.ResponseDataBahanMakanan
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.databinding.FragmentRVMakananDokterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentRVMakananDokter : Fragment() {
    private var _binding: FragmentRVMakananDokterBinding? = null
    private val binding get() = _binding!!
    private val listBahanMakanan = ArrayList<BahanMakananData>()
    private var role_user: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRVMakananDokterBinding.inflate(inflater, container,false)
        role_user = arguments?.getString("role_user")
        Log.d("RoleUser", "Received role_user: $role_user")
        getDataBahanMakanan()
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        getDataBahanMakanan()
    }
    private fun getDataBahanMakanan(){
        binding.rvFoodDoctor.setHasFixedSize(true)
        binding.rvFoodDoctor.layoutManager = LinearLayoutManager(context)
        binding.progressBar.visibility

        val call = RClient.api.getDataBahanMakananAll()
        call?.enqueue(object : Callback<ResponseDataBahanMakanan> {
            override fun onResponse(
                call: Call<ResponseDataBahanMakanan>,
                response: Response<ResponseDataBahanMakanan>
            ) {
                Log.d("BahanMakananResponse", "onResponse called")
                Log.d("BahanMakananResponse", "Response code: ${response.code()}")
                Log.d("BahanMakananResponse", "Response message: ${response.message()}")
                Log.d("BahanMakananResponse", "Response body: ${response.body()}")
                if(response.isSuccessful){
                    listBahanMakanan.clear()
                    response.body()?.let { responseData ->
                        responseData.data.forEach { dataItem ->
                            listBahanMakanan.add(dataItem)
                        }
                    }
                    val adapter = BahanMakananAdapter (listBahanMakanan, requireContext())
                    binding.rvFoodDoctor.adapter = adapter
                    adapter.notifyDataSetChanged()
                    binding.progressBar.isVisible = false
                }else{

                }
            }

            override fun onFailure(call: Call<ResponseDataBahanMakanan>, t: Throwable) {
                Log.e("BahanMakananResponse", "API call failed", t)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
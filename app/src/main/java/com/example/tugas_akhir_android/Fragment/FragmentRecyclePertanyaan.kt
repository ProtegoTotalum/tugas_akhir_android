package com.example.tugas_akhir_android.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugas_akhir_android.Adapter.PertanyaanAdapter
import com.example.tugas_akhir_android.DataClass.GejalaData
import com.example.tugas_akhir_android.DataClass.JawabanUserData
import com.example.tugas_akhir_android.DataClass.PertanyaanData
import com.example.tugas_akhir_android.DataClass.ResponseDataGejala
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.FragmentRecyclePertanyaanBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentRecyclePertanyaan : Fragment() {
    private var _binding: FragmentRecyclePertanyaanBinding? = null
    private val binding get() = _binding!!
    private val listGejala = ArrayList<GejalaData>()
    private lateinit var sharedViewModel: SharedViewModel
    private var id_user: Int? = null
    private lateinit var pertanyaanAdapter: PertanyaanAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecyclePertanyaanBinding.inflate(inflater, container,false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        id_user = sharedViewModel.idUser.value
        setupRecyclerView()
        getDataGejala()
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        getDataGejala()
    }

    private fun setupRecyclerView() {
        pertanyaanAdapter = PertanyaanAdapter(listGejala, requireContext())
        binding.rvGejala.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = pertanyaanAdapter
        }
    }

    private fun getDataGejala(){
        binding.rvGejala.setHasFixedSize(true)
        binding.rvGejala.layoutManager = LinearLayoutManager(context)

        val call = RClient.api.getDataGejalaAll()
        call?.enqueue(object : Callback<ResponseDataGejala> {
            override fun onResponse(
                call: Call<ResponseDataGejala>,
                response: Response<ResponseDataGejala>
            ) {
                Log.d("PertanyaanUserResponse", "onResponse called")
                Log.d("PertanyaanUserResponse", "Response code: ${response.code()}")
                Log.d("PertanyaanUserResponse", "Response message: ${response.message()}")
                Log.d("PertanyaanUserResponse", "Response body: ${response.body()}")
                if(response.isSuccessful){
                    listGejala.clear()
                    response.body()?.let { responseData ->
                        listGejala.addAll(responseData.data)
                    }
                    pertanyaanAdapter.notifyDataSetChanged()
                }else{

                }
            }

            override fun onFailure(call: Call<ResponseDataGejala>, t: Throwable) {
                Log.e("PertanyaanUserResponse", "API call failed", t)
            }
        })
    }

    fun getUserAnswers(): List<JawabanUserData> {
        return pertanyaanAdapter.getUserAnswers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
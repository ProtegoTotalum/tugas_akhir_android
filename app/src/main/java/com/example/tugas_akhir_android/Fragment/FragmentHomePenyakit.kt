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
import com.example.tugas_akhir_android.Adapter.PenyakitAdapter
import com.example.tugas_akhir_android.DataClass.PenyakitData
import com.example.tugas_akhir_android.DataClass.ResponseDataPenyakit
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.FragmentHomePenyakitBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentHomePenyakit : Fragment() {
    private var _binding: FragmentHomePenyakitBinding? = null
    private val binding get() = _binding!!
    private val listPenyakit = ArrayList<PenyakitData>()
    private var role_user: String? = null
    private lateinit var sharedViewModel: SharedViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomePenyakitBinding.inflate(inflater, container,false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        role_user = sharedViewModel.roleUser.value
//        role_user = arguments?.getString("role_user")
        Log.d("RoleUserPenyakit", "Received role_user: $role_user")
        getDataPenyakit()
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        getDataPenyakit()
    }
    private fun getDataPenyakit() {
        binding.rvHomePenyakit.setHasFixedSize(true)
        binding.rvHomePenyakit.layoutManager = LinearLayoutManager(context)
        binding.progressBar.visibility = View.VISIBLE

        val call = RClient.api.getDataPenyakitAll()
        call?.enqueue(object : Callback<ResponseDataPenyakit> {
            override fun onResponse(
                call: Call<ResponseDataPenyakit>,
                response: Response<ResponseDataPenyakit>
            ) {
                if (!isAdded) return // Pastikan fragment masih terlampir
                Log.d("PenyakitResponse", "onResponse called")
                Log.d("PenyakitResponse", "Response code: ${response.code()}")
                Log.d("PenyakitResponse", "Response message: ${response.message()}")
                Log.d("PenyakitResponse", "Response body: ${response.body()}")
                if (response.isSuccessful) {
                    listPenyakit.clear()
                    response.body()?.let { responseData ->
                        responseData.data.forEach { dataItem ->
                            listPenyakit.add(dataItem)
                        }
                    }
                    val adapter = PenyakitAdapter(listPenyakit, requireContext(), role_user)
                    binding.rvHomePenyakit.adapter = adapter
                    adapter.notifyDataSetChanged()
                    binding.progressBar.isVisible = false
                } else {
                    // Handle unsuccessful response
                    binding.progressBar.isVisible = false
                }
            }

            override fun onFailure(call: Call<ResponseDataPenyakit>, t: Throwable) {
                if (!isAdded) return // Pastikan fragment masih terlampir
                Log.e("PenyakitResponse", "API call failed", t)
                binding.progressBar.isVisible = false
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
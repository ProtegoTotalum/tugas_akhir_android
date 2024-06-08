package com.example.tugas_akhir_android.Fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tugas_akhir_android.DataClass.ResponseDataUser
import com.example.tugas_akhir_android.DataClass.UserData
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.FragmentDiagnosaUserBinding
import com.shashank.sony.fancytoastlib.FancyToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentDiagnosaUser : Fragment() {
    private var _binding: FragmentDiagnosaUserBinding? = null
    private val binding get() = _binding!!
    private var id_user: Int? = null
    private var provinsi_user: String? = null
    private var kabkot_user: String? = null
    private var newnamadokter:String? = ""
    private var newiddokter: Int? = 0
    private lateinit var sharedViewModel: SharedViewModel
    private val listDokter = ArrayList<UserData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiagnosaUserBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        id_user = sharedViewModel.idUser.value
        provinsi_user = sharedViewModel.provinsiUser.value
        kabkot_user = sharedViewModel.kabkotUser.value
        Log.d("FragmentDiagnosaUser", "Received id_user: $id_user")
        Log.d("FragmentDiagnosaUser", "Received provinsi_user: $provinsi_user")
        Log.d("FragmentDiagnosaUser", "Received kabkot_user: $kabkot_user")

        provinsi_user?.let { kabkot_user?.let { it1 -> getDataDokter(it, it1) } }

        // Inisialisasi AutoCompleteTextView untuk provinsi
        val autoCompleteDokter: AutoCompleteTextView = binding.dokterACT
        autoCompleteDokter.onItemClickListener = AdapterView.OnItemClickListener { parent, view, i, id ->
            val itemSelectedDokter = listDokter[i]
            newiddokter = itemSelectedDokter.id
            newnamadokter = itemSelectedDokter.nama
            FancyToast.makeText(
                requireContext(),
                "Selected Provinsi: ${itemSelectedDokter.nama}",
                FancyToast.LENGTH_SHORT,
                FancyToast.DEFAULT,
                false
            ).show()
        }

        binding.backButtonDiagnosaUser.setOnClickListener{
            parentFragmentManager.popBackStack()
        }

        binding.btnMulaiDiagnosa.setOnClickListener {
            val fragmentPertanyaan = FragmentPertanyaanUser()
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.layout_fragment_user, fragmentPertanyaan) // R.id.layout_fragment berasal dari HomeActivity
                addToBackStack(null)
                commit()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getDataDokter(provinsi_user:String, kota_user:String){

        val call = RClient.api.getDataDokter(provinsi_user,kota_user)
        call?.enqueue(object : Callback<ResponseDataUser> {
            override fun onResponse(
                call: Call<ResponseDataUser>,
                response: Response<ResponseDataUser>
            ){
                Log.d("FragmentDiagnosaResponse", "onResponse called")
                Log.d("FragmentDiagnosaResponse", "Response code: ${response.code()}")
                Log.d("FragmentDiagnosaResponse", "Response message: ${response.message()}")
                Log.d("FragmentDiagnosaResponse", "Response body: ${response.body()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.message == "List Data Dokter") {
                            listDokter.clear()
                            listDokter.addAll(it.data as List<UserData>)
                            newiddokter = listDokter[0].id
                            updateDokterAdapter()
                            Log.d("FragmentDiagnosaResponse", "NewIdDokter: $newiddokter")
                        } else {
                            // Handle the case when the status is false
                            Log.e("FragmentDiagnosaResponse", "API call unsuccessful: ${it.message}")
                        }
                    }
                } else {
                    // Handle the case when the API response is not successful
                    Log.e("FragmentDiagnosaResponse", "API call failed with response code: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ResponseDataUser>, t: Throwable) {
                Log.e("FragmentDiagnosaResponse", "API call failed", t)
            }
        })
    }

    private fun updateDokterAdapter() {
        val autoCompleteDokter: AutoCompleteTextView = binding.dokterACT
        val adapterDokter = ArrayAdapter(
            requireContext(),
            R.layout.list_item,
            listDokter.map { it.nama }
        )
        autoCompleteDokter.setAdapter(adapterDokter)
        autoCompleteDokter.onItemClickListener = AdapterView.OnItemClickListener { parent, view, i, id ->
            val itemSelectedDokter = listDokter[i]
            newiddokter = itemSelectedDokter.id
            newnamadokter = itemSelectedDokter.nama
            FancyToast.makeText(
                requireContext(),
                "Selected Kab/Kot: ${itemSelectedDokter.nama}",
                FancyToast.LENGTH_SHORT,
                FancyToast.DEFAULT,
                false
            ).show()
        }
    }
}
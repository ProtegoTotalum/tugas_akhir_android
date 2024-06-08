package com.example.tugas_akhir_android.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.tugas_akhir_android.Adapter.PertanyaanAdapter
import com.example.tugas_akhir_android.DataClass.JawabanUserData
import com.example.tugas_akhir_android.DataClass.PertanyaanData
import com.example.tugas_akhir_android.DataClass.ResponseDataDiagnosa
import com.example.tugas_akhir_android.DataClass.ResponseDataPertanyaan
import com.example.tugas_akhir_android.DataClass.ResponseDataStorePertanyaan
import com.example.tugas_akhir_android.DataClass.ResponseDataUser
import com.example.tugas_akhir_android.DataClass.StorePertanyaan
import com.example.tugas_akhir_android.DataClass.StorePertanyaanRequest
import com.example.tugas_akhir_android.LoginActivity
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.FragmentPertanyaanUserBinding
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentPertanyaanUser : Fragment() {
    private var _binding: FragmentPertanyaanUserBinding? = null
    private val binding get() = _binding!!
    private var id_user: Int? = null
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var fragmentRecyclePertanyaan: FragmentRecyclePertanyaan

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPertanyaanUserBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        id_user = sharedViewModel.idUser.value
        Log.d("FragmentPertanyaanUser", "Received id_user: $id_user")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViewFragment()
        setupSubmitButton()
    }

    private fun setupRecyclerViewFragment() {
        fragmentRecyclePertanyaan = FragmentRecyclePertanyaan()
        childFragmentManager.beginTransaction().apply {
            replace(R.id.fl_pertanyaan_user, fragmentRecyclePertanyaan)
            commit()
        }
    }

    private fun setupSubmitButton() {
        binding.btnDiagnosa.setOnClickListener {
            val userAnswers = fragmentRecyclePertanyaan.getUserAnswers()
            Log.d("FragmentPertanyaanUser", "User Answer: $userAnswers")
            if (validateAnswers(userAnswers)) {
                storePertanyaan(userAnswers)
            } else {
                FancyToast.makeText(
                    requireContext(),
                    "Semua Pertanyaan Wajib Diisi!",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false).show()
            }
        }
    }

    private fun validateAnswers(answers: List<JawabanUserData>): Boolean {
        return answers.all { it.jawaban_user.isNotEmpty() }
    }


    private fun storePertanyaan(answers: List<JawabanUserData>) {
        val id_user = sharedViewModel.idUser.value ?: return
        val request = StorePertanyaanRequest(jawaban = answers)

        val call = RClient.api.storePertanyaan(id_user, request)
        call.enqueue(object : Callback<ResponseDataStorePertanyaan> {
            override fun onResponse(call: Call<ResponseDataStorePertanyaan>, response: Response<ResponseDataStorePertanyaan>) {
                Log.d("FragmentPertanyaanUser", "onResponse called")
                Log.d("FragmentPertanyaanUser", "Response code: ${response.code()}")
                Log.d("FragmentPertanyaanUser", "Response message: ${response.message()}")
                Log.d("FragmentPertanyaanUser", "Response body: ${response.body()}")
                if (response.isSuccessful) {
//                    FancyToast.makeText(
//                        requireContext(),
//                        "Store Pertanyaan Berhasil",
//                        FancyToast.LENGTH_SHORT,
//                        FancyToast.SUCCESS,
//                        false).show()
                    startDiagnosa(id_user)
                } else {
                    try {
                        // Extract error message from the response body
                        val errorBody = response.errorBody()?.string()
                        val errorMessage = JSONObject(errorBody!!).getString("message")

                        // Show the specific error message
                        FancyToast.makeText(
                            requireContext(), errorMessage, FancyToast.LENGTH_LONG, FancyToast.ERROR, false
                        ).show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // Show a generic error message if unable to parse the error response
                        FancyToast.makeText(
                            requireContext(), "Error: " + response.message(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDataStorePertanyaan>, t: Throwable) {
                FancyToast.makeText(
                    requireContext(),
                    "Terjadi kesalahan: ${t.message}",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false).show()
            }
        })
    }

    private fun startDiagnosa(id:Int) {
        val call = RClient.api.startdiagnosa(id)
        call?.enqueue(object : Callback<ResponseDataDiagnosa> {
            override fun onResponse(
                call: Call<ResponseDataDiagnosa>,
                response: Response<ResponseDataDiagnosa>
            ){
                Log.d("FragmentPertanyaanUserDiagnosa", "onResponse called")
                Log.d("FragmentPertanyaanUserDiagnosa", "Response code: ${response.code()}")
                Log.d("FragmentPertanyaanUserDiagnosa", "Response message: ${response.message()}")
                Log.d("FragmentPertanyaanUserDiagnosa", "Response body: ${response.body()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        FancyToast.makeText(
                            requireContext(),
                            "${response.body()?.message}",
                            FancyToast.LENGTH_LONG,
                            FancyToast.SUCCESS,
                            false
                        ).show()
                        parentFragmentManager.popBackStack()
                    }
                } else {
                    // Handle the case when the API response is not successful
                    Log.e("FragmentPertanyaanUserDiagnosa", "API call failed with response code: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ResponseDataDiagnosa>, t: Throwable) {
                Log.e("FragmentPertanyaanUserDiagnosa", "API call failed", t)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
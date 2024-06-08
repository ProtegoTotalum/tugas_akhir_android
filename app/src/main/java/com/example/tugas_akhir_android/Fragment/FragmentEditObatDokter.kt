package com.example.tugas_akhir_android.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.tugas_akhir_android.DataClass.ObatData
import com.example.tugas_akhir_android.DataClass.ResponseDataObat
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.FragmentEditObatDokterBinding
import com.google.android.material.textfield.TextInputEditText
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentEditObatDokter : Fragment() {
    private var _binding: FragmentEditObatDokterBinding? = null
    private val binding get() = _binding!!
    private val listObat = ArrayList<ObatData>()
    private var id_user: Int? = 0
    private var id_obat: Int? = 0
    private lateinit var sharedViewModel: SharedViewModel
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditObatDokterBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        arguments?.let {
            id_obat = it.getInt("id_obat")
        }

        id_obat?.let{ getDataObat(it)}

        id_user = sharedViewModel.idUser.value
        Log.d("FragmentEditObat", "Received id_user: $id_user")
        Log.d("FragmentEditObat", "Received id_obat: $id_obat")


        binding.backButtonEditObat.setOnClickListener{
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.obatData.observe(viewLifecycleOwner, { obatData ->
            obatData?.let {
                with(binding) {
                    inputEditNamaObat.setText(it.nama_obat)
                    inputEditJenisObat.setText(it.jenis_obat)
                    inputEditKegunaanObat.setText(it.kegunaan_obat)
                    inputEditAturanMinumObat.setText(it.aturan_minum_obat)
                    inputEditHargaObat.setText(it.harga_obat)
                }
            }
        })


        binding.btnEditObat.setOnClickListener {
            with(binding) {
                val nama_obat = binding.inputEditNamaObat.text.toString()
                val jenis_obat = binding.inputEditJenisObat.text.toString()
                val kegunaan_obat = binding.inputEditKegunaanObat.text.toString()
                val aturan_minum_obat = binding.inputEditAturanMinumObat.text.toString()
                val inputHargaObat: TextInputEditText = binding.inputEditHargaObat

                inputHargaObat.addTextChangedListener(object : TextWatcher {
                    private var isEditing = false

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        // Do nothing
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        // Do nothing
                    }

                    override fun afterTextChanged(s: Editable?) {
                        if (isEditing) return

                        isEditing = true

                        var input = s.toString()
                        if (!input.startsWith("Rp ")) {
                            input = "Rp " + input.replace("Rp ", "")
                            inputHargaObat.setText(input)
                            inputHargaObat.setSelection(input.length) // Move cursor to the end
                        }

                        isEditing = false
                    }
                })

                val harga_obat = inputHargaObat.text.toString()

                if (nama_obat.isEmpty()) {
                    FancyToast.makeText(
                        requireContext(),
                        "Nama obat tidak boleh kosong",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                    ).show()
                }else if (jenis_obat.isEmpty()) {
                    FancyToast.makeText(
                        requireContext(),
                        "Jenis obat tidak boleh kosong!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                    ).show()
                }else if (kegunaan_obat.isEmpty()) {
                    FancyToast.makeText(
                        requireContext(),
                        "Kegunaan obat tidak boleh kosong!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                    ).show()
                }else if (aturan_minum_obat.isEmpty()) {
                    FancyToast.makeText(
                        requireContext(),
                        "Aturan minum obat tidak boleh kosong!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                    ).show()
                }else if (harga_obat.isEmpty()) {
                    FancyToast.makeText(
                        requireContext(),
                        "Harga obat tidak boleh kosong!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                    ).show()
                }else{
                    val call = RClient.api.updateObat(
                        nama_obat,
                        jenis_obat,
                        kegunaan_obat,
                        aturan_minum_obat,
                        harga_obat,
                        id_obat)
                    call.enqueue(object :
                        Callback<ResponseDataObat> {
                        override fun onResponse(
                            call: Call<ResponseDataObat>,
                            response: Response<ResponseDataObat>
                        ) {
                            Log.d("FragmentEditObatResponse", "onResponseDetail called")
                            Log.d("FragmentEditObatResponse", "Response code: ${response.code()}")
                            Log.d("FragmentEditObatResponse", "Response message: ${response.message()}")
                            Log.d("FragmentEditObatResponse", "Response body: ${response.body()}")
                            if (response.isSuccessful) {
                                FancyToast.makeText(
                                    requireContext(),
                                    "${response.body()?.message}",
                                    FancyToast.LENGTH_LONG,
                                    FancyToast.SUCCESS,
                                    false
                                ).show()
                                viewModel.obatData.value = viewModel.obatData.value?.copy(
                                    nama_obat = nama_obat,
                                    jenis_obat = jenis_obat,
                                    kegunaan_obat = kegunaan_obat,
                                    aturan_minum_obat = aturan_minum_obat,
                                    harga_obat = harga_obat,
                                )

                                parentFragmentManager.popBackStack()
                            }else{
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

                        override fun onFailure(call: Call<ResponseDataObat>, t: Throwable) {
                            Log.e("FragmentEditObatResponse", "API call failed", t)
                        }
                    })
                }
            }
        }

    }

    private fun getDataObat(id_obat:Int){

        val call = RClient.api.getDataObat(id_obat)
        call?.enqueue(object : Callback<ResponseDataObat> {
            override fun onResponse(
                call: Call<ResponseDataObat>,
                response: Response<ResponseDataObat>
            ){
                Log.d("FragmentEditObatResponse", "onResponse called")
                Log.d("FragmentEditObatResponse", "Response code: ${response.code()}")
                Log.d("FragmentEditObatResponse", "Response message: ${response.message()}")
                Log.d("FragmentEditObatResponse", "Response body: ${response.body()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.message == "Data Obat Ditemukan") {
                            listObat.clear()
                            listObat.addAll(it.data as List<ObatData>)
                            with(binding) {
                                inputEditNamaObat.setText(listObat[0].nama_obat)
                                inputEditJenisObat.setText(listObat[0].jenis_obat)
                                inputEditKegunaanObat.setText(listObat[0].kegunaan_obat)
                                inputEditAturanMinumObat.setText(listObat[0].aturan_minum_obat)
                                inputEditHargaObat.setText(listObat[0].harga_obat)

                            }
                        } else {
                            // Handle the case when the status is false
                            Log.e("FragmentEditObatResponse", "API call unsuccessful: ${it.message}")
                        }
                    }
                } else {
                    // Handle the case when the API response is not successful
                    Log.e("FragmentEditObatResponse", "API call failed with response code: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ResponseDataObat>, t: Throwable) {
                Log.e("FragmentEditObatResponse", "API call failed", t)
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
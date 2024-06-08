package com.example.tugas_akhir_android.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.tugas_akhir_android.DataClass.ObatData
import com.example.tugas_akhir_android.DataClass.ResponseDataObat
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.FragmentAddObatDokterBinding
import com.google.android.material.textfield.TextInputEditText
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentAddObatDokter : Fragment() {
    private var _binding: FragmentAddObatDokterBinding? = null
    private val binding get() = _binding!!
    private val listObat = ArrayList<ObatData>()
    private var id_user: Int? = null
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddObatDokterBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        id_user = sharedViewModel.idUser.value
        Log.d("FragmentAddObat", "Received id_user: $id_user")


        binding.backButtonAddObat.setOnClickListener{
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnAddObat.setOnClickListener {
            val nama_obat = binding.inputNamaObat.text.toString()
            val jenis_obat = binding.inputJenisObat.text.toString()
            val kegunaan_obat = binding.inputKegunaanObat.text.toString()
            val aturan_minum_obat = binding.inputAturanMinumObat.text.toString()
            val harga_obat = binding.inputHargaObat.text.toString()
            val inputHargaObat: TextInputEditText = binding.inputHargaObat

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
                sendObatData()
            }
        }
    }


    private fun sendObatData() {
        val nama_obat = binding.inputNamaObat.text.toString()
        val jenis_obat = binding.inputJenisObat.text.toString()
        val kegunaan_obat = binding.inputKegunaanObat.text.toString()
        val aturan_minum_obat = binding.inputAturanMinumObat.text.toString()
        val inputHargaObat: TextInputEditText = binding.inputHargaObat

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

        val call = RClient.api.storeObat(nama_obat,jenis_obat,kegunaan_obat,aturan_minum_obat,harga_obat)
        call.enqueue(object : Callback<ResponseDataObat> {
            override fun onResponse(call: Call<ResponseDataObat>, response: Response<ResponseDataObat>) {
                Log.d("FragmentAddObatResponse", "onResponseDetail called")
                Log.d("FragmentAddObatResponse", "Response code: ${response.code()}")
                Log.d("FragmentAddObatResponse", "Response message: ${response.message()}")
                Log.d("FragmentAddObatResponse", "Response body: ${response.body()}")
                if (response.isSuccessful) {
                    FancyToast.makeText(
                        requireContext(),
                        "Obat Berhasil Ditambahkan!",
                        FancyToast.LENGTH_LONG,
                        FancyToast.SUCCESS,
                        false)
                        .show()
                    parentFragmentManager.popBackStack()
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

            override fun onFailure(call: Call<ResponseDataObat>, t: Throwable) {
                FancyToast.makeText(
                    requireContext(),
                    "Error: ${t.message}",
                    FancyToast.LENGTH_LONG,
                    FancyToast.ERROR,
                    false)
                    .show()
                Log.e("FragmentAddObatResponse", "API call failed", t)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.tugas_akhir_android.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tugas_akhir_android.DataClass.BahanMakananData
import com.example.tugas_akhir_android.DataClass.JadwalMakanData
import com.example.tugas_akhir_android.DataClass.ResponseDataBahanMakanan
import com.example.tugas_akhir_android.DataClass.ResponseDataJadwalMakan
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.FragmentAddBahanMakananDokterBinding
import com.example.tugas_akhir_android.databinding.FragmentAddJadwalMakanBinding
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentAddBahanMakananDokter : Fragment() {
    private var _binding: FragmentAddBahanMakananDokterBinding? = null
    private val binding get() = _binding!!
    private val listBahan = ArrayList<BahanMakananData>()
    private var id_user: Int? = null
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var inputKaloriBahanMakanan: EditText
    private lateinit var inputKarbohidratBahanMakanan: EditText
    private lateinit var inputProteinNBahanMakanan: EditText
    private lateinit var inputProteinHBahanMakanan: EditText
    private lateinit var inputLemakBahanMakanan: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBahanMakananDokterBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        id_user = sharedViewModel.idUser.value
        Log.d("AddBahanMakanan", "Received id_user: $id_user")


        binding.backButtonAddBahanMakanan.setOnClickListener{
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inputKaloriBahanMakanan = binding.inputKaloriBahanMakanan
        inputKarbohidratBahanMakanan = binding.inputKarbohidratBahanMakanan
        inputProteinNBahanMakanan = binding.inputProteinNBahanMakanan
        inputProteinHBahanMakanan = binding.inputProteinHBahanMakanan
        inputLemakBahanMakanan = binding.inputLemakBahanMakanan

        handleDecimalInput(inputKaloriBahanMakanan)
        handleDecimalInput(inputKarbohidratBahanMakanan)
        handleDecimalInput(inputProteinNBahanMakanan)
        handleDecimalInput(inputProteinHBahanMakanan)
        handleDecimalInput(inputLemakBahanMakanan)

        binding.btnAddBahanMakanan.setOnClickListener {

            val nama_bahan_makanan = binding.inputNamaBahanMakanan.text.toString()
            val takaran = binding.inputTakaranBahanMakanan.text.toString()
            val kalori = binding.inputKaloriBahanMakanan.text.toString()
            val karbohidrat = binding.inputKarbohidratBahanMakanan.text.toString()
            val protein_nabati = binding.inputProteinNBahanMakanan.text.toString()
            val protein_hewani = binding.inputProteinHBahanMakanan.text.toString()
            val lemak = binding.inputLemakBahanMakanan.text.toString()


            if (nama_bahan_makanan.isEmpty()) {
                FancyToast.makeText(
                    requireContext(),
                    "Nama bahan makanan tidak boleh kosong",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }else if (takaran.isEmpty()) {
                FancyToast.makeText(
                    requireContext(),
                    "Takaran bahan makanan tidak boleh kosong!",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }else if (kalori.isEmpty()) {
                FancyToast.makeText(
                    requireContext(),
                    "Kalori bahan makanan harus dituliskan 0 jika kosong!",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }else if (karbohidrat.isEmpty()) {
                FancyToast.makeText(
                    requireContext(),
                    "Karbohidrat bahan makanan harus dituliskan 0 jika kosong!",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }else if (protein_nabati.isEmpty()) {
                FancyToast.makeText(
                    requireContext(),
                    "Protein nabati harus dituliskan 0 jika kosong!",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }else if (protein_hewani.isEmpty()) {
                FancyToast.makeText(
                    requireContext(),
                    "Protein hewani harus dituliskan 0 jika kosong!",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }else if (lemak.isEmpty()) {
                FancyToast.makeText(
                    requireContext(),
                    "Lemak bahan makanan harus dituliskan 0 jika kosong!",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }else{
                sendBahanMakananData()
            }
        }
    }

    fun handleDecimalInput(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                var input = editable.toString()
                if (input.contains(",")) {
                    input = input.replace(",", ".")
                    editText.setText(input)
                    editText.setSelection(input.length)
                }
            }
        })
    }

    private fun sendBahanMakananData() {
        val nama_bahan_makanan = binding.inputNamaBahanMakanan.text.toString()
        val takaran = binding.inputTakaranBahanMakanan.text.toString()
        val kaloriText = inputKaloriBahanMakanan.text.toString()
        val karbohidratText = inputKarbohidratBahanMakanan.text.toString()
        val proteinNabatiText = inputProteinNBahanMakanan.text.toString()
        val proteinHewaniText = inputProteinHBahanMakanan.text.toString()
        val lemakText = inputLemakBahanMakanan.text.toString()

        val kalori: Double? = if (kaloriText.isNotEmpty()) {
            kaloriText.toDoubleOrNull()
        } else {
            null
        }

        val karbohidrat: Double? = if (karbohidratText.isNotEmpty()) {
            karbohidratText.toDoubleOrNull()
        } else {
            null
        }

        val proteinNabati: Double? = if (proteinNabatiText.isNotEmpty()) {
            proteinNabatiText.toDoubleOrNull()
        } else {
            null
        }

        val proteinHewani: Double? = if (proteinHewaniText.isNotEmpty()) {
            proteinHewaniText.toDoubleOrNull()
        } else {
            null
        }

        val lemak: Double? = if (lemakText.isNotEmpty()) {
            lemakText.toDoubleOrNull()
        } else {
            null
        }

        val call = RClient.api.storeBahanMakanan(nama_bahan_makanan,takaran,kalori,karbohidrat,proteinNabati,proteinHewani,lemak)
        call.enqueue(object : Callback<ResponseDataBahanMakanan> {
            override fun onResponse(call: Call<ResponseDataBahanMakanan>, response: Response<ResponseDataBahanMakanan>) {
                Log.d("AddBahanMakananResponse", "onResponseDetail called")
                Log.d("AddBahanMakananResponse", "Response code: ${response.code()}")
                Log.d("AddBahanMakananResponse", "Response message: ${response.message()}")
                Log.d("AddBahanMakananResponse", "Response body: ${response.body()}")
                if (response.isSuccessful) {
                    FancyToast.makeText(
                        requireContext(),
                        "Bahan Makanan Berhasil Ditambahkan!",
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

            override fun onFailure(call: Call<ResponseDataBahanMakanan>, t: Throwable) {
                FancyToast.makeText(
                    requireContext(),
                    "Error: ${t.message}",
                    FancyToast.LENGTH_LONG,
                    FancyToast.ERROR,
                    false)
                    .show()
                Log.e("AddBahanMakananResponse", "API call failed", t)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
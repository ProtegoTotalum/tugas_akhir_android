package com.example.tugas_akhir_android.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.tugas_akhir_android.DataClass.BahanMakananData
import com.example.tugas_akhir_android.DataClass.ResponseDataBahanMakanan
import com.example.tugas_akhir_android.DataClass.UserData
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.FragmentEditBahanMakananDokterBinding
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentEditBahanMakananDokter : Fragment() {
    private var _binding: FragmentEditBahanMakananDokterBinding? = null
    private val binding get() = _binding!!
    private val listBahanMakanan = ArrayList<BahanMakananData>()
    private var id_user: Int? = 0
    private var id_bahan_makanan: Int? = 0
    private lateinit var sharedViewModel: SharedViewModel
    private val viewModel: SharedViewModel by activityViewModels()

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
        _binding = FragmentEditBahanMakananDokterBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        arguments?.let {
            id_bahan_makanan = it.getInt("id_bahan_makanan")
        }

        id_bahan_makanan?.let{ getDataBahanMakanan(it)}

        id_user = sharedViewModel.idUser.value
        Log.d("EditBahanMakanan", "Received id_user: $id_user")
        Log.d("EditBahanMakanan", "Received id_bahan_makanan: $id_bahan_makanan")


        binding.backButtonEditBahanMakanan.setOnClickListener{
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.bahanMakananData.observe(viewLifecycleOwner, { bahanMakananData ->
            bahanMakananData?.let {
                with(binding) {
                    inputEditNamaBahanMakanan.setText(it.nama_bahan_makanan)
                    inputEditTakaranBahanMakanan.setText(it.takaran)
                    inputEditKaloriBahanMakanan.setText(String.format("%.2f", it.kalori))
                    inputEditKarbohidratBahanMakanan.setText(String.format("%.2f", it.karbohidrat))
                    inputEditProteinNBahanMakanan.setText(String.format("%.2f", it.protein_nabati))
                    inputEditProteinHBahanMakanan.setText(String.format("%.2f", it.protein_hewani))
                    inputEditLemakBahanMakanan.setText(String.format("%.2f", it.lemak))
                }
            }
        })

        inputKaloriBahanMakanan = binding.inputEditKaloriBahanMakanan
        inputKarbohidratBahanMakanan = binding.inputEditKarbohidratBahanMakanan
        inputProteinNBahanMakanan = binding.inputEditProteinNBahanMakanan
        inputProteinHBahanMakanan = binding.inputEditProteinHBahanMakanan
        inputLemakBahanMakanan = binding.inputEditLemakBahanMakanan

        handleDecimalInput(inputKaloriBahanMakanan)
        handleDecimalInput(inputKarbohidratBahanMakanan)
        handleDecimalInput(inputProteinNBahanMakanan)
        handleDecimalInput(inputProteinHBahanMakanan)
        handleDecimalInput(inputLemakBahanMakanan)

        binding.btnEditBahanMakanan.setOnClickListener {
            with(binding) {
                val nama_bahan_makanan = binding.inputEditNamaBahanMakanan.text.toString()
                val takaran = binding.inputEditTakaranBahanMakanan.text.toString()
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

                if (nama_bahan_makanan.isEmpty()) {
                    FancyToast.makeText(
                        requireContext(),
                        "Nama bahan makanan harus diisi!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                    ).show()
                }else if (takaran.isEmpty()) {
                    FancyToast.makeText(
                        requireContext(),
                        "Takaran bahan makanan harus diisi!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                    ).show()
                }else if (kaloriText.isEmpty()) {
                    FancyToast.makeText(
                        requireContext(),
                        "Kalori harus dituliskan 0 jika kosong!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                    ).show()
                }else if (karbohidratText.isEmpty()) {
                    FancyToast.makeText(
                        requireContext(),
                        "Karbohidrat harus dituliskan 0 jika kosong!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                    ).show()
                }else if (proteinNabatiText.isEmpty()){
                    FancyToast.makeText(
                        requireContext(),
                        "Protein nabati harus dituliskan 0 jika kosong!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                    ).show()
                }else if (proteinHewaniText.isNullOrEmpty()){
                    FancyToast.makeText(
                        requireContext(),
                        "Protein hewani harus dituliskan 0 jika kosong!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                    ).show()
                }else if(lemakText.isNullOrEmpty()){
                    FancyToast.makeText(
                        requireContext(),
                        "Lemak harus dituliskan 0 jika kosong!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                    ).show()
                }else {
                    val call = RClient.api.updateBahanMakanan(
                        nama_bahan_makanan,
                        takaran,
                        kalori,
                        karbohidrat,
                        proteinNabati,
                        proteinHewani,
                        lemak,
                        id_bahan_makanan
                    )
                    call.enqueue(object :
                        Callback<ResponseDataBahanMakanan> {
                        override fun onResponse(
                            call: Call<ResponseDataBahanMakanan>,
                            response: Response<ResponseDataBahanMakanan>
                        ) {
                            Log.d("EditBahanMakananResponse", "onResponseDetail called")
                            Log.d("EditBahanMakananResponse", "Response code: ${response.code()}")
                            Log.d("EditBahanMakananResponse", "Response message: ${response.message()}")
                            Log.d("EditBahanMakananResponse", "Response body: ${response.body()}")
                            if (response.isSuccessful) {
                                FancyToast.makeText(
                                    requireContext(),
                                    "${response.body()?.message}",
                                    FancyToast.LENGTH_LONG,
                                    FancyToast.SUCCESS,
                                    false
                                ).show()
                                // Update the SharedViewModel with new user data
                                viewModel.bahanMakananData.value = viewModel.bahanMakananData.value?.copy(
                                    nama_bahan_makanan = nama_bahan_makanan,
                                    takaran = takaran,
                                    kalori = kalori!!,
                                    karbohidrat = karbohidrat!!,
                                    protein_nabati = proteinNabati!!,
                                    protein_hewani =  proteinHewani!!,
                                    lemak = lemak!!,
                                )

                                // Kembali ke FragmentProfilAdmin
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

                        override fun onFailure(call: Call<ResponseDataBahanMakanan>, t: Throwable) {
                            Log.e("EditBahanMakananResponse", "API call failed", t)
                        }
                    })
                }
            }
        }

    }

    private fun getDataBahanMakanan(id_bahan_makanan:Int){

        val call = RClient.api.getDataBahanMakanan(id_bahan_makanan)
        call?.enqueue(object : Callback<ResponseDataBahanMakanan> {
            override fun onResponse(
                call: Call<ResponseDataBahanMakanan>,
                response: Response<ResponseDataBahanMakanan>
            ){
                Log.d("EditBahanMakananResponse", "onResponse called")
                Log.d("EditBahanMakananResponse", "Response code: ${response.code()}")
                Log.d("EditBahanMakananResponse", "Response message: ${response.message()}")
                Log.d("EditBahanMakananResponse", "Response body: ${response.body()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.message == "Data Bahan Makanan Ditemukan") {
                            listBahanMakanan.clear()
                            listBahanMakanan.addAll(it.data as List<BahanMakananData>)
                            with(binding) {
                                inputEditNamaBahanMakanan.setText(listBahanMakanan[0].nama_bahan_makanan)
                                inputEditTakaranBahanMakanan.setText(listBahanMakanan[0].takaran)
                                inputEditKaloriBahanMakanan.setText(String.format("%.2f", listBahanMakanan[0].kalori))
                                inputEditKarbohidratBahanMakanan.setText(String.format("%.2f", listBahanMakanan[0].karbohidrat))
                                inputEditProteinNBahanMakanan.setText(String.format("%.2f", listBahanMakanan[0].protein_nabati))
                                inputEditProteinHBahanMakanan.setText(String.format("%.2f", listBahanMakanan[0].protein_hewani))
                                inputEditLemakBahanMakanan.setText(String.format("%.2f", listBahanMakanan[0].lemak))

                            }
                        } else {
                            // Handle the case when the status is false
                            Log.e("EditBahanMakananResponse", "API call unsuccessful: ${it.message}")
                        }
                    }
                } else {
                    // Handle the case when the API response is not successful
                    Log.e("EditBahanMakananResponse", "API call failed with response code: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ResponseDataBahanMakanan>, t: Throwable) {
                Log.e("EditBahanMakananResponse", "API call failed", t)
            }
        })
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
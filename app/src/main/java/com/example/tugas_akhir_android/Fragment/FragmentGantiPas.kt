package com.example.tugas_akhir_android.Fragment

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.example.tugas_akhir_android.DataClass.ResponseDataUser
import com.example.tugas_akhir_android.DataClass.UserData
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.FragmentEditProfilBinding
import com.example.tugas_akhir_android.databinding.FragmentGantiPasBinding
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale


class FragmentGantiPas : Fragment() {
    private var _binding: FragmentGantiPasBinding? = null
    private val binding get() = _binding!!
    private var id_user: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGantiPasBinding.inflate(inflater, container, false)

        arguments?.let {
            id_user = it.getInt("id_user")
        }

        binding.backButtonGantiPas.setOnClickListener{
            parentFragmentManager.popBackStack()
        }

        Log.d("idUserFragmentGantiPas", "Received id_user: $id_user")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGantiPas.setOnClickListener {
            with(binding) {
                val password_lama = inputPasLama.text.toString()
                val password_baru = inputPasBaru.text.toString()
                val password_konfirmasi = inputConfirmPas.text.toString()
                val call = RClient.api.change_pas(
                    password_lama,
                    password_baru,
                    password_konfirmasi,
                    id_user
                )
                call.enqueue(object :
                    Callback<ResponseDataUser> {
                    override fun onResponse(
                        call: Call<ResponseDataUser>,
                        response: Response<ResponseDataUser>
                    ) {
                        Log.d("ChangePasResponse", "onResponseDetail called")
                        Log.d("ChangePasResponse", "Response code: ${response.code()}")
                        Log.d("ChangePasResponse", "Response message: ${response.message()}")
                        Log.d("ChangePasResponse", "Response body: ${response.body()}")
                        if (response.isSuccessful) {
                            FancyToast.makeText(
                                requireContext(),
                                "${response.body()?.message}",
                                FancyToast.LENGTH_LONG,
                                FancyToast.SUCCESS,
                                false
                            ).show()

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

                    override fun onFailure(call: Call<ResponseDataUser>, t: Throwable) {
                        Log.e("ChangePasResponse", "API call failed", t)
                    }
                })
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
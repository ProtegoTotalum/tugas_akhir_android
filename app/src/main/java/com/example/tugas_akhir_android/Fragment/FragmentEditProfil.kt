package com.example.tugas_akhir_android.Fragment

import com.example.tugas_akhir_android.R
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.example.tugas_akhir_android.DataClass.KabKotData
import com.example.tugas_akhir_android.DataClass.ProvinsiData
import com.example.tugas_akhir_android.DataClass.ResponseDataKabKot
import com.example.tugas_akhir_android.DataClass.ResponseDataProvinsi
import com.example.tugas_akhir_android.DataClass.ResponseDataUser
import com.example.tugas_akhir_android.DataClass.UserData
import com.example.tugas_akhir_android.LoginActivity
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.FragmentEditProfilBinding
import com.google.android.material.textfield.TextInputLayout
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale


class FragmentEditProfil : Fragment() {

    private var _binding: FragmentEditProfilBinding? = null
    private val binding get() = _binding!!
    private val listUser = ArrayList<UserData>()
    private var id_user: Int? = null
    private var newgender_user:String? = null
    private var newprov_user:String? = ""
    private var newkabkot_user:String? = ""
    private val listProvinsi = ArrayList<ProvinsiData>()
    private val listKabKot = ArrayList<KabKotData>()
    private var id_provinsi : Int? = 0
    private val viewModel: SharedViewModel by activityViewModels()
    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfilBinding.inflate(inflater, container, false)

        arguments?.let {
            id_user = it.getInt("id_user")
        }
        getDataProvinsi()

        binding.prInputTglLahirUser.setOnClickListener {

            val callback = SingleDayPickCallback { singleDay ->
                binding.prInputTglLahirUser.text =
                    dateToString(singleDay.dayOfMonth, singleDay.month, singleDay.year).toEditable()
            }
            val today = CivilCalendar()
            val datePicker = PrimeDatePicker.dialogWith(today)
                .pickSingleDay(callback)
                .initiallyPickedSingleDay(today)
                .build()

            datePicker.show(parentFragmentManager, "DD/MM/YYYY")
        }

        binding.backButtonEditProfilUser.setOnClickListener{
            parentFragmentManager.popBackStack()
        }

        id_user?.let{ getDataUser(it)}
        Log.d("idUserFragmentEditProfil", "Received id_user: $id_user")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val autoComplete : AutoCompleteTextView = binding.prInputGenderUser
        val options = listOf("Pria", "Wanita")
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.list_item,
            options
        )

        autoComplete.setAdapter(adapter)

        // Inisialisasi AutoCompleteTextView untuk provinsi
        val autoCompleteProvinsi: AutoCompleteTextView = binding.prProvinsiACT
        autoCompleteProvinsi.onItemClickListener = AdapterView.OnItemClickListener { parent, view, i, id ->
            val itemSelectedProvinsi = listProvinsi[i]
            newprov_user = itemSelectedProvinsi.nama_provinsi
            FancyToast.makeText(
                requireContext(),
                "Selected Provinsi: ${itemSelectedProvinsi.nama_provinsi}",
                FancyToast.LENGTH_SHORT,
                FancyToast.DEFAULT,
                false
            ).show()
            id_provinsi?.let { getDataKabKot(it) }
        }

        // Inisialisasi AutoCompleteTextView untuk kabupaten/kota
        val autoCompleteKabKot: AutoCompleteTextView = binding.prKabKotACT
        autoCompleteKabKot.onItemClickListener = AdapterView.OnItemClickListener { parent, view, i, id ->
            val itemSelectedKabKot = listKabKot[i]
            newprov_user = itemSelectedKabKot.nama_kabkot
            FancyToast.makeText(
                requireContext(),
                "Selected Kab/Kot: ${itemSelectedKabKot.nama_kabkot}",
                FancyToast.LENGTH_SHORT,
                FancyToast.DEFAULT,
                false
            ).show()
        }
        viewModel.userData.observe(viewLifecycleOwner, { userData ->
            userData?.let {
                with(binding) {
                    prInputNamaUser.setText(it.nama)
                    prInputEmailUser.setText(it.email)
                    prInputTglLahirUser.setText(it.tgl_lahir_user)
                    prInputNoTelpUser.setText(it.no_telp_user)
                    prInputGenderUser.setText(it.gender_user,false)
                    prInputAlamatUser.setText(it.alamat_user)
                    prProvinsiACT.setText(it.provinsi_user,false)
                    prKabKotACT.setText(it.kota_user,false)// Set the initial value of gender
                }
            }
        })

        autoComplete.onItemClickListener = AdapterView.OnItemClickListener{
                parent, view, i, id ->

            val itemSelected = parent.getItemAtPosition(i)
            FancyToast.makeText(
                requireContext(),
                "Item : $itemSelected",
                FancyToast.LENGTH_SHORT,
                FancyToast.DEFAULT,
                false
            ).show()

        }

        binding.btnEditUser.setOnClickListener {
            with(binding) {
                val nama_user = prInputNamaUser.text.toString()
                val tgl_lahir_user = prInputTglLahirUser.text.toString()
                val no_telp_user = prInputNoTelpUser.text.toString()
                val alamat_user = prInputAlamatUser.text.toString()
                if (nama_user.isEmpty()) {
                    FancyToast.makeText(
                        requireContext(),
                        "Nama tidak boleh kosong!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                    ).show()
                }else if (tgl_lahir_user.isEmpty()) {
                    FancyToast.makeText(
                        requireContext(),
                        "Tanggal lahir tidak boleh kosong!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                    ).show()
                }else if (no_telp_user.isEmpty()) {
                    FancyToast.makeText(
                        requireContext(),
                        "Nomor telepon tidak boleh kosong!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                    ).show()
                }else if (newgender_user.isNullOrEmpty()) {
                    FancyToast.makeText(
                        requireContext(),
                        "Jenis Kelamin tidak boleh kosong!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                    ).show()
                }else if (alamat_user.isNullOrEmpty()){
                    FancyToast.makeText(
                        requireContext(),
                        "Alamat tidak boleh kosong!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                    ).show()
                }else if (newprov_user.isNullOrEmpty()){
                    FancyToast.makeText(
                        requireContext(),
                        "Provinsi harus dipilih!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                    ).show()
                }else if(newkabkot_user.isNullOrEmpty()){
                    FancyToast.makeText(
                        requireContext(),
                        "Kabupaten atau Kota harus dipilih!",
                        FancyToast.LENGTH_SHORT,
                        FancyToast.ERROR,
                        false
                    ).show()
                }else {
//                    val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//                    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//                    val date = inputFormat.parse(tgl_lahir_user)
//                    val formattedDate = outputFormat.format(date)

                    val call = RClient.api.update_user(
                        nama_user,
                        tgl_lahir_user,
                        no_telp_user,
                        newgender_user,
                        alamat_user,
                        newkabkot_user,
                        newprov_user,
                        id_user
                    )
                    call.enqueue(object :
                        Callback<ResponseDataUser> {
                        override fun onResponse(
                            call: Call<ResponseDataUser>,
                            response: Response<ResponseDataUser>
                        ) {
                            Log.d("EditProfilResponse", "onResponseDetail called")
                            Log.d("EditProfilResponse", "Response code: ${response.code()}")
                            Log.d("EditProfilResponse", "Response message: ${response.message()}")
                            Log.d("EditProfilResponse", "Response body: ${response.body()}")
                            if (response.isSuccessful) {
                                FancyToast.makeText(
                                    requireContext(),
                                    "${response.body()?.message}",
                                    FancyToast.LENGTH_LONG,
                                    FancyToast.SUCCESS,
                                    false
                                ).show()
                                // Update the SharedViewModel with new user data
                                viewModel.userData.value = viewModel.userData.value?.copy(
                                    nama = nama_user,
                                    tgl_lahir_user = tgl_lahir_user,
                                    no_telp_user = no_telp_user,
                                    gender_user = newgender_user?:"",
                                    alamat_user = alamat_user,
                                    kota_user =  newkabkot_user?:"",
                                    provinsi_user = newprov_user?:"",
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

                        override fun onFailure(call: Call<ResponseDataUser>, t: Throwable) {
                            Log.e("EditProfilResponse", "API call failed", t)
                        }
                    })
                }
            }
        }

    }

    private fun dateToString(dayofMonth: Int, month: Int, year: Int): String {
        return year.toString()+"-"+(month+1)+"-"+dayofMonth.toString()
    }

    private fun getDataUser(id:Int){

        val call = RClient.api.getDataUser(id)
        call?.enqueue(object : Callback<ResponseDataUser> {
            override fun onResponse(
                call: Call<ResponseDataUser>,
                response: Response<ResponseDataUser>
            ){
                Log.d("EditUserResponse", "onResponse called")
                Log.d("EditUserResponse", "Response code: ${response.code()}")
                Log.d("EditUserResponse", "Response message: ${response.message()}")
                Log.d("EditUserResponse", "Response body: ${response.body()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.message == "Data User Ditemukan") {
                            listUser.clear()
                            listUser.addAll(it.data as List<UserData>)
                            with(binding) {
                                prInputNamaUser.setText(listUser[0].nama)
                                prInputEmailUser.setText(listUser[0].email)
                                prInputTglLahirUser.setText(listUser[0].tgl_lahir_user)
                                prInputNoTelpUser.setText(listUser[0].no_telp_user)
                                prInputGenderUser.setText(listUser[0].gender_user,false)
                                prInputAlamatUser.setText(listUser[0].alamat_user)
                                prProvinsiACT.setText(listUser[0].provinsi_user,false)
                                prKabKotACT.setText(listUser[0].kota_user,false)
                                newgender_user = listUser[0].gender_user
                                newprov_user = listUser[0].provinsi_user
                                newkabkot_user = listUser[0].kota_user
                            }
                            Log.d("FragmentEditProfil", "NewGenderUser: $newgender_user")
                        } else {
                            // Handle the case when the status is false
                            Log.e("EditUserResponse", "API call unsuccessful: ${it.message}")
                        }
                    }
                } else {
                    // Handle the case when the API response is not successful
                    Log.e("EditUserResponse", "API call failed with response code: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ResponseDataUser>, t: Throwable) {
                Log.e("EditUserResponse", "API call failed", t)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getDataProvinsi(){
        val call = RClient.api.getDataProvinsi()
        call?.enqueue(object : Callback<ResponseDataProvinsi> {
            override fun onResponse(call: Call<ResponseDataProvinsi>, response: Response<ResponseDataProvinsi>) {
                Log.d("EditProvinsiResponse", "On Response called")
                Log.d("EditProvinsiResponse", "Response code: ${response.code()}")
                Log.d("EditProvinsiResponse", "Response message: ${response.message()}")
                Log.d("EditProvinsiResponse", "Response body: ${response.body()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.message == "List Data Provinsi") {
                            listProvinsi.clear()
                            listProvinsi.addAll(it.data as List<ProvinsiData>)
                            id_provinsi = listProvinsi[0].id_provinsi
                            updateProvinsiAdapter()
                        } else {
                            // Handle the case when the status is false
                            Log.e("EditProvinsiResponse", "API call unsuccessful: ${it.message}")
                        }
                    }

                } else {
                    Log.d("EditProvinsiResponse", "Failed called")
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

            override fun onFailure(call: Call<ResponseDataProvinsi>, t: Throwable) {
                FancyToast.makeText(
                    requireContext(),
                    "Provinsi Failed",
                    FancyToast.LENGTH_LONG,
                    FancyToast.ERROR,
                    false
                ).show()
            }
        })
    }

    private fun getDataKabKot(id_provinsi:Int){
        val call = RClient.api.getDataKabKot(id_provinsi)
        call?.enqueue(object : Callback<ResponseDataKabKot> {
            override fun onResponse(call: Call<ResponseDataKabKot>, response: Response<ResponseDataKabKot>) {
                Log.d("EditKabKotResponse", "On Response called")
                Log.d("EditKabKotResponse", "Response code: ${response.code()}")
                Log.d("EditKabKotResponse", "Response message: ${response.message()}")
                Log.d("EditKabKotResponse", "Response body: ${response.body()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.message == "List Data Kabupaten/Kota") {
                            listKabKot.clear()
                            listKabKot.addAll(it.data as List<KabKotData>)
                            updateKabKotAdapter()
                        } else {
                            // Handle the case when the status is false
                            Log.e("EditKabKotResponse", "API call unsuccessful: ${it.message}")
                        }
                    }

                } else {
                    Log.d("EditKabKotResponse", "Failed called")
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

            override fun onFailure(call: Call<ResponseDataKabKot>, t: Throwable) {
                FancyToast.makeText(
                    requireContext(),
                    "Kabupaten Kota Failed",
                    FancyToast.LENGTH_LONG,
                    FancyToast.ERROR,
                    false
                ).show()
            }
        })
    }

    private fun updateProvinsiAdapter() {
        val autoCompleteProvinsi: AutoCompleteTextView = binding.prProvinsiACT
        val adapterProv = ArrayAdapter(
            requireContext(),
            R.layout.list_item,
            listProvinsi.map { it.nama_provinsi }
        )
        autoCompleteProvinsi.setAdapter(adapterProv)
        autoCompleteProvinsi.onItemClickListener = AdapterView.OnItemClickListener { parent, view, i, id ->
            val itemSelectedProvinsi = listProvinsi[i]
            id_provinsi = itemSelectedProvinsi.id_provinsi
            newprov_user = itemSelectedProvinsi.nama_provinsi
            FancyToast.makeText(
                requireContext(),
                "Selected Provinsi: ${itemSelectedProvinsi.nama_provinsi}",
                FancyToast.LENGTH_SHORT,
                FancyToast.DEFAULT,
                false
            ).show()
            id_provinsi?.let { getDataKabKot(it) }
        }
    }

    private fun updateKabKotAdapter() {
        val autoCompleteKabKot: AutoCompleteTextView = binding.prKabKotACT
        val adapterKabKot = ArrayAdapter(
            requireContext(),
            R.layout.list_item,
            listKabKot.map { it.nama_kabkot }
        )
        autoCompleteKabKot.setAdapter(adapterKabKot)
        autoCompleteKabKot.onItemClickListener = AdapterView.OnItemClickListener { parent, view, i, id ->
            val itemSelectedKabKot = listKabKot[i]
            newkabkot_user = itemSelectedKabKot.nama_kabkot
            FancyToast.makeText(
                requireContext(),
                "Selected Kab/Kot: ${itemSelectedKabKot.nama_kabkot}",
                FancyToast.LENGTH_SHORT,
                FancyToast.DEFAULT,
                false
            ).show()
        }
    }

}
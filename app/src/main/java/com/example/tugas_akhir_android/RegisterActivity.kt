package com.example.tugas_akhir_android

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.RadioButton
import android.widget.RadioGroup
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.example.tugas_akhir_android.DataClass.KabKotData
import com.example.tugas_akhir_android.DataClass.ProvinsiData
import com.example.tugas_akhir_android.DataClass.ResponseDataKabKot
import com.example.tugas_akhir_android.DataClass.ResponseDataProvinsi
import com.example.tugas_akhir_android.DataClass.ResponseDataUser
import com.example.tugas_akhir_android.DataClass.UserData
import com.example.tugas_akhir_android.DataClass.UserResponse
import com.example.tugas_akhir_android.databinding.ActivityRegisterBinding
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
    private var btnRGrup : RadioGroup? = null
    private lateinit var btnRButton : RadioButton
    private var id_provinsi : Int? = 0
    private val listProvinsi = ArrayList<ProvinsiData>()
    private var id_kabkot : Int? = 0
    private var nama_provinsi: String = ""
    private var nama_kabkot: String = ""
    private val listKabKot = ArrayList<KabKotData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        getDataProvinsi()

        val autoCompleteProvinsi: AutoCompleteTextView = binding.provinsiACT
        autoCompleteProvinsi.onItemClickListener = AdapterView.OnItemClickListener { parent, view, i, id ->
            val itemSelectedProvinsi = listProvinsi[i]
            id_provinsi = itemSelectedProvinsi.id_provinsi
            nama_provinsi = itemSelectedProvinsi.nama_provinsi
            FancyToast.makeText(
                this,
                "Selected Provinsi: ${itemSelectedProvinsi.nama_provinsi}",
                FancyToast.LENGTH_SHORT,
                FancyToast.DEFAULT,
                false
            ).show()
            id_provinsi?.let { getDataKabKot(it) }
        }

        // Inisialisasi AutoCompleteTextView untuk kabupaten/kota
        val autoCompleteKabKot: AutoCompleteTextView = binding.kabkotACT
        autoCompleteKabKot.onItemClickListener = AdapterView.OnItemClickListener { parent, view, i, id ->
            val itemSelectedKabKot = listKabKot[i]
            id_kabkot = itemSelectedKabKot.id_kabkot
            nama_kabkot = itemSelectedKabKot.nama_kabkot
            FancyToast.makeText(
                this,
                "Selected Kab/Kot: ${itemSelectedKabKot.nama_kabkot}",
                FancyToast.LENGTH_SHORT,
                FancyToast.DEFAULT,
                false
            ).show()
        }

        binding.regInputTglLahir.setOnClickListener {
            val callback = SingleDayPickCallback { singleDay ->
                binding.regInputTglLahir.text =
                    dateToString(singleDay.dayOfMonth, singleDay.month, singleDay.year).toEditable()
            }
            val today = CivilCalendar()
            val datePicker = PrimeDatePicker.dialogWith(today)
                .pickSingleDay(callback)
                .initiallyPickedSingleDay(today)
                .build()

            datePicker.show(supportFragmentManager, "DD/MM/YYYY")
        }

        binding.btnRegister.setOnClickListener {
            val nama_user = binding.regInputNama.text.toString()
            val email = binding.regInputEmail.text.toString()
            val password = binding.regInputPassword.text.toString()
            val tgl_lahir_user = binding.regInputTglLahir.text.toString()
            val no_telp_user = binding.regInputTelp.text.toString()
            val alamat_user = binding.regInputAlamat.text.toString()
            btnRGrup = binding.radioGroupJnsKelamin
            val rbc: Int = btnRGrup!!.checkedRadioButtonId
            val gender_user: String



            if (rbc != -1) {
                val selectedRadioButton = findViewById<RadioButton>(rbc)
                if (selectedRadioButton != null) {
                    gender_user = selectedRadioButton.text.toString()
                } else {
                    gender_user = ""
                }
            } else {
                gender_user = ""
            }

            if (nama_user.isEmpty()) {
                FancyToast.makeText(
                    this@RegisterActivity,
                    "Nama tidak boleh kosong!",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }else if (email.isEmpty()) {
                FancyToast.makeText(
                    this@RegisterActivity,
                    "Email tidak boleh kosong!",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }else if (tgl_lahir_user.isEmpty()) {
                FancyToast.makeText(
                    this@RegisterActivity,
                    "Tanggal lahir tidak boleh kosong!",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }else if (no_telp_user.isEmpty()) {
                FancyToast.makeText(
                    this@RegisterActivity,
                    "Nomor telepon tidak boleh kosong!",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }else if (gender_user.isEmpty()) {
                FancyToast.makeText(
                    this@RegisterActivity,
                    "Jenis Kelamin harus dipilih!",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }else if(alamat_user.isEmpty()){
                FancyToast.makeText(
                    this@RegisterActivity,
                    "Alamat tidak boleh kosong!",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }else if(nama_provinsi.isEmpty()){
                FancyToast.makeText(
                    this@RegisterActivity,
                    "Provinsi harus dipilih!",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }else if(nama_kabkot.isEmpty()){
                FancyToast.makeText(
                    this@RegisterActivity,
                    "Kabupaten atau Kota harus dipilih!",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }else if (password.isEmpty()) {
                FancyToast.makeText(
                    this@RegisterActivity,
                    "Password tidak boleh kosong!",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }else {
                saveData()
            }
        }

        binding.backButton.setOnClickListener {
            val cancel = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(cancel)
        }

    }

    private fun dateToString(dayofMonth: Int, month: Int, year: Int): String {
        return dayofMonth.toString()+"/"+(month+1)+"/"+year.toString()
    }

    fun saveData() {
        Log.d("RegisterResponse", "Save Data called")
        with(binding) {
            val nama_user = regInputNama.text.toString()
            val email = regInputEmail.text.toString()
            val password = regInputPassword.text.toString()
            val tgl_lahir_user = regInputTglLahir.text.toString()
            val no_telp_user = regInputTelp.text.toString()
            val alamat_user = regInputAlamat.text.toString()
            btnRGrup = binding.radioGroupJnsKelamin
            val rbc: Int = btnRGrup!!.checkedRadioButtonId
            val gender_user: String

            if (rbc != -1) {
                val selectedRadioButton = findViewById<RadioButton>(rbc)
                if (selectedRadioButton != null) {
                    gender_user = selectedRadioButton.text.toString()
                } else {
                    gender_user = ""
                }
            } else {
                gender_user = ""
            }
            // Konversi format tanggal dari dd/MM/yyyy ke YYYY-MM-DD
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(tgl_lahir_user)
            val formattedDate = outputFormat.format(date)
            Log.d("RegisterResponse", "Before API call")
            val call = RClient.api.register(nama_user,email, password, formattedDate, no_telp_user, gender_user, alamat_user, nama_kabkot, nama_provinsi )
            Log.d("RegisterResponse", "After API call")
            call.enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    Log.d("RegisterResponse", "On Response called")
                    if (response.isSuccessful) {
                        FancyToast.makeText(
                            applicationContext,
                            "${response.body()?.message}",
                            FancyToast.LENGTH_LONG,
                            FancyToast.SUCCESS,
                            false
                        ).show()
                        val moveLogin = Intent(
                            this@RegisterActivity, LoginActivity::class.java
                        )
                        startActivity(moveLogin)
                    } else {
                        Log.d("RegisterResponse", "Failed called")
                        try {
                            // Extract error message from the response body
                            val errorBody = response.errorBody()?.string()
                            val errorMessage = JSONObject(errorBody!!).getString("message")

                            // Show the specific error message
                            FancyToast.makeText(
                                this@RegisterActivity, errorMessage, FancyToast.LENGTH_LONG, FancyToast.ERROR, false
                            ).show()
                        } catch (e: Exception) {
                            e.printStackTrace()
                            // Show a generic error message if unable to parse the error response
                            FancyToast.makeText(
                                this@RegisterActivity, "Error: " + response.message(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    FancyToast.makeText(
                        applicationContext,
                        "Gagal Register",
                        FancyToast.LENGTH_LONG,
                        FancyToast.ERROR,
                        false
                    ).show()
                }
            })
        }
    }

    private fun getDataProvinsi(){
        val call = RClient.api.getDataProvinsi()
        call?.enqueue(object : Callback<ResponseDataProvinsi> {
            override fun onResponse(call: Call<ResponseDataProvinsi>, response: Response<ResponseDataProvinsi>) {
                Log.d("ProvinsiResponse", "On Response called")
                Log.d("ProvinsiResponse", "Response code: ${response.code()}")
                Log.d("ProvinsiResponse", "Response message: ${response.message()}")
                Log.d("ProvinsiResponse", "Response body: ${response.body()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.message == "List Data Provinsi") {
                            listProvinsi.clear()
                            listProvinsi.addAll(it.data as List<ProvinsiData>)
                            id_provinsi = listProvinsi[0].id_provinsi
                            updateProvinsiAdapter()
                        } else {
                            // Handle the case when the status is false
                            Log.e("ProvinsiResponse", "API call unsuccessful: ${it.message}")
                        }
                    }

                } else {
                    Log.d("ProvinsiResponse", "Failed called")
                    try {
                        // Extract error message from the response body
                        val errorBody = response.errorBody()?.string()
                        val errorMessage = JSONObject(errorBody!!).getString("message")

                        // Show the specific error message
                        FancyToast.makeText(
                            this@RegisterActivity, errorMessage, FancyToast.LENGTH_LONG, FancyToast.ERROR, false
                        ).show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // Show a generic error message if unable to parse the error response
                        FancyToast.makeText(
                            this@RegisterActivity, "Error: " + response.message(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDataProvinsi>, t: Throwable) {
                FancyToast.makeText(
                    applicationContext,
                    "Provinsi Failed",
                    FancyToast.LENGTH_LONG,
                    FancyToast.ERROR,
                    false
                ).show()
            }
        })
    }

    private fun updateProvinsiAdapter() {
        val autoCompleteProvinsi: AutoCompleteTextView = binding.provinsiACT
        val adapterProv = ArrayAdapter(
            this,
            R.layout.list_item,
            listProvinsi.map { it.nama_provinsi }
        )
        autoCompleteProvinsi.setAdapter(adapterProv)
        autoCompleteProvinsi.onItemClickListener = AdapterView.OnItemClickListener { parent, view, i, id ->
            val itemSelectedProvinsi = listProvinsi[i]
            id_provinsi = itemSelectedProvinsi.id_provinsi
            nama_provinsi = itemSelectedProvinsi.nama_provinsi
            FancyToast.makeText(
                this,
                "Selected Provinsi: ${itemSelectedProvinsi.nama_provinsi}",
                FancyToast.LENGTH_SHORT,
                FancyToast.DEFAULT,
                false
            ).show()
            id_provinsi?.let { getDataKabKot(it) }
        }
    }

    private fun getDataKabKot(id_provinsi:Int){
        val call = RClient.api.getDataKabKot(id_provinsi)
        call?.enqueue(object : Callback<ResponseDataKabKot> {
            override fun onResponse(call: Call<ResponseDataKabKot>, response: Response<ResponseDataKabKot>) {
                Log.d("KabKotResponse", "On Response called")
                Log.d("KabKotResponse", "Response code: ${response.code()}")
                Log.d("KabKotResponse", "Response message: ${response.message()}")
                Log.d("KabKotResponse", "Response body: ${response.body()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.message == "List Data Kabupaten/Kota") {
                            listKabKot.clear()
                            listKabKot.addAll(it.data as List<KabKotData>)
                            id_kabkot = listKabKot[0].id_kabkot
                            updateKabKotAdapter()
                        } else {
                            // Handle the case when the status is false
                            Log.e("KabKotResponse", "API call unsuccessful: ${it.message}")
                        }
                    }

                } else {
                    Log.d("KabKotResponse", "Failed called")
                    try {
                        // Extract error message from the response body
                        val errorBody = response.errorBody()?.string()
                        val errorMessage = JSONObject(errorBody!!).getString("message")

                        // Show the specific error message
                        FancyToast.makeText(
                            this@RegisterActivity, errorMessage, FancyToast.LENGTH_LONG, FancyToast.ERROR, false
                        ).show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // Show a generic error message if unable to parse the error response
                        FancyToast.makeText(
                            this@RegisterActivity, "Error: " + response.message(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDataKabKot>, t: Throwable) {
                FancyToast.makeText(
                    applicationContext,
                    "Kabupaten Kota Failed",
                    FancyToast.LENGTH_LONG,
                    FancyToast.ERROR,
                    false
                ).show()
            }
        })
    }

    private fun updateKabKotAdapter() {
        val autoCompleteKabKot: AutoCompleteTextView = binding.kabkotACT
        val adapterKabKot = ArrayAdapter(
            this,
            R.layout.list_item,
            listKabKot.map { it.nama_kabkot }
        )
        autoCompleteKabKot.setAdapter(adapterKabKot)
        autoCompleteKabKot.onItemClickListener = AdapterView.OnItemClickListener { parent, view, i, id ->
            val itemSelectedKabKot = listKabKot[i]
            id_kabkot = itemSelectedKabKot.id_kabkot
            nama_kabkot = itemSelectedKabKot.nama_kabkot
            FancyToast.makeText(
                this,
                "Selected Kab/Kot: ${itemSelectedKabKot.nama_kabkot}",
                FancyToast.LENGTH_SHORT,
                FancyToast.DEFAULT,
                false
            ).show()
        }
    }
}
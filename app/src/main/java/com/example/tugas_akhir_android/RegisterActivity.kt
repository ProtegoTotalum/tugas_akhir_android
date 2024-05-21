package com.example.tugas_akhir_android

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.example.tugas_akhir_android.DataClass.UserResponse
import com.example.tugas_akhir_android.databinding.ActivityRegisterBinding
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_register)
//    }
    private lateinit var binding: ActivityRegisterBinding
    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
    private var btnRGrup : RadioGroup? = null
    private lateinit var btnRButton : RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.regInputTglLahir.setOnClickListener {

//            val callback = SingleDayPickCallback {
//                    singleDay -> binding.regInputTglLahir.text =
//                dateToString(singleDay.dayOfMonth,singleDay.month,singleDay.year)
//            }
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
                FancyToast.makeText(this@RegisterActivity, "Nama tidak boleh kosong!", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
            } else if (email.isEmpty()) {
                FancyToast.makeText(this@RegisterActivity, "Email tidak boleh kosong!", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
            } else if (tgl_lahir_user.isEmpty()) {
                FancyToast.makeText(this@RegisterActivity, "Tanggal lahir tidak boleh kosong!", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
            } else if (no_telp_user.isEmpty()) {
                FancyToast.makeText(this@RegisterActivity, "Nomor telepon tidak boleh kosong!", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
            } else if (gender_user.isEmpty()) {
                FancyToast.makeText(this@RegisterActivity, "Jenis Kelamin tidak boleh kosong!", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
            } else if (password.isEmpty()) {
                FancyToast.makeText(this@RegisterActivity, "Password tidak boleh kosong!", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
            } else {
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
            val call = RClient.api.register(nama_user,email, password, formattedDate, no_telp_user, gender_user)
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
}
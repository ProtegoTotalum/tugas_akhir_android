package com.example.tugas_akhir_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tugas_akhir_android.DataClass.UserResponse
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var inputEmail: TextInputEditText
    private lateinit var inputPassword: TextInputEditText
    private lateinit var mainLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mainLayout = findViewById(R.id.mainLayout)
        val btnRegister: Button = findViewById(R.id.btnLoginRegister)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val lupaPas: TextView = findViewById(R.id.lupaPassword)


        inputEmail = findViewById(R.id.input_email)
        inputPassword = findViewById(R.id.input_password)

        btnRegister.setOnClickListener(View.OnClickListener {
            val moveRegister = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(moveRegister)
        })

        lupaPas.setOnClickListener {
            val moveLupaPas = Intent(this@LoginActivity, LupaPasswordActivity::class.java)
            startActivity(moveLupaPas)
        }

        btnLogin.setOnClickListener(View.OnClickListener {
            val email: String = inputEmail.text.toString().trim()
            val password: String = inputPassword.text.toString().trim()

            Log.d("LoginRequest", "Email: $email")
            Log.d("LoginRequest", "Password: $password")

            if (email.isEmpty()) {
                FancyToast.makeText(this@LoginActivity, "Email tidak boleh kosong!", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
            }else if (password.isEmpty()) {
                FancyToast.makeText(this@LoginActivity, "Password tidak boleh kosong!", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show()
            }else{
                login()
            }
        })
    }

    private fun login() {
        Log.d("LoginResponse", "login() function called")
        val email = inputEmail.text.toString().trim()
        val password = inputPassword.text.toString().trim()


        val call = RClient.api.login(email, password)
        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                Log.d("LoginResponse", "onResponse called")
                if(response.isSuccessful){

                    val login = response.body()!!
                    FancyToast.makeText(
                        this@LoginActivity, "Login Success!", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false
                    ).show()

                    val idUser = login.user?.id
                    val role = login.user?.role_user
                    val deaktivasi = login.user?.deaktivasi
                    Log.d("cekLogin", "id_user:$idUser")
                    Log.d("cekLogin", "role:$role")
                    Log.d("cekLogin", "deaktivasi:$deaktivasi")

                    if(deaktivasi == 1 || deaktivasi != null){
                        FancyToast.makeText(
                            this@LoginActivity, "Akun Ini Sudah Deaktivasi!", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false
                        ).show()
                    }else{
                        if(role == "admin"){
                            val moveHome = Intent(
                                this@LoginActivity, HomeAdminActivity::class.java
                            ).apply {
                                putExtra("id_user", idUser)
                                putExtra("role_user", role)
                            }
                            startActivity(moveHome)
                        }else {
                            if (role == "dokter") {
                                val moveHome = Intent(
                                    this@LoginActivity, HomeDokterActivity::class.java
                                ).apply {
                                    putExtra("id_user", idUser)
                                    putExtra("role_user", role)
                                }
                                startActivity(moveHome)
                            } else {
                                val moveHome = Intent(
                                    this@LoginActivity, HomeUserActivity::class.java
                                ).apply {
                                    putExtra("id_user", idUser)
                                    putExtra("role_user", role)
                                }
                                startActivity(moveHome)
                            }
                        }
                    }
                }else{
                    Log.d("LoginResponse", "Failed called")
                    try {
                        // Extract error message from the response body
                        val errorBody = response.errorBody()?.string()
                        val errorMessage = JSONObject(errorBody!!).getString("message")

                        // Show the specific error message
                        FancyToast.makeText(
                            this@LoginActivity, errorMessage, FancyToast.LENGTH_LONG, FancyToast.ERROR, false
                        ).show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // Show a generic error message if unable to parse the error response
                        FancyToast.makeText(
                            this@LoginActivity, "Error: " + response.message(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("LoginResponse", "API call failed", t)
            }
        })
    }
}
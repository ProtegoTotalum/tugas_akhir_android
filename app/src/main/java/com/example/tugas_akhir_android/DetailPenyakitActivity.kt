package com.example.tugas_akhir_android

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tugas_akhir_android.databinding.ActivityDetailPenyakitBinding
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.tugas_akhir_android.SharedViewModel
import androidx.fragment.app.activityViewModels
import com.example.tugas_akhir_android.DataClass.PenyakitData
import com.example.tugas_akhir_android.DataClass.ResponseDataPenyakit
import com.google.android.material.button.MaterialButton
import com.shashank.sony.fancytoastlib.FancyToast
import jp.wasabeef.richeditor.RichEditor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailPenyakitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPenyakitBinding
    private var b: Bundle? = null
    private val listPenyakit = ArrayList<PenyakitData>()
    private lateinit var tvEditDeskripsiPenyakit: TextView
    private lateinit var editorDeskripsi: RichEditor
    private lateinit var tvEditGejalaPenyakit: TextView
    private lateinit var editorGejala: RichEditor
    private lateinit var tvEditPenyebabPenyakit: TextView
    private lateinit var editorPenyebab: RichEditor
    private lateinit var tvEditPenyebaranPenyakit: TextView
    private lateinit var editorPenyebaran: RichEditor
    private lateinit var tvEditCaraPencegahanPenyakit: TextView
    private lateinit var editorCaraPencegahan: RichEditor
    private lateinit var tvEditCaraPenangananPenyakit: TextView
    private lateinit var editorCaraPenanganan: RichEditor
    private lateinit var editButton: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPenyakitBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        tvEditDeskripsiPenyakit = binding.tvEditDeskripsiPenyakit
        editorDeskripsi = binding.editorDeskripsi
        tvEditGejalaPenyakit = binding.tvEditGejalaPenyakit
        editorGejala = binding.editorGejala
        tvEditPenyebabPenyakit = binding.tvEditPenyebabPenyakit
        editorPenyebab = binding.editorPenyebab
        tvEditPenyebaranPenyakit = binding.tvEditPenyebaranPenyakit
        editorPenyebaran = binding.editorPenyebaran
        tvEditCaraPencegahanPenyakit = binding.tvEditCaraPencegahanPenyakit
        editorCaraPencegahan = binding.editorCaraPencegahan
        tvEditCaraPenangananPenyakit = binding.tvEditCaraPenangananPenyakit
        editorCaraPenanganan = binding.editorCaraPenanganan
        editButton = binding.editButtonDetailPenyakit

        b = intent.extras
        val id_penyakit = b?.getInt("id_penyakit",0)
        val role_user = intent.getStringExtra("role_user")
        Log.d("RoleUser", "Received role_user: $role_user")
        Log.d("DetailPenyakitResponse", "Received id_penyakit: $id_penyakit")

        id_penyakit?.let { getDataDetail(it) }

        binding.backButtonDetailPenyakit.setOnClickListener {
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

//        editButton.setOnClickListener {
//            toggleEditorsVisibility(View.GONE, View.VISIBLE)
//            populateEditors()
//
//            editButton.setOnClickListener {
//                saveEditedText()
//            }
//        }
        if(role_user == "user"){
            editButton.visibility = View.GONE
        }else{
            editButton.setOnClickListener {
                if ((editButton as MaterialButton).text == "Edit") {
                    // Jika tombol "Edit" diklik, tampilkan editor dan ubah teks tombol menjadi "Save"
                    toggleEditorsVisibility(View.GONE, View.VISIBLE)
                    populateEditors()
                    (editButton as MaterialButton).text = "Save"
                } else {
                    // Jika tombol "Save" diklik, simpan perubahan dan kembalikan ke mode tampilan teks biasa
                    saveEditedText()
                }
            }
        }
    }

    private fun getDataDetail(id_penyakit: Int) {

        val call = RClient.api.getDataPenyakit(id_penyakit)
        call?.enqueue(object : Callback<ResponseDataPenyakit> {
            override fun onResponse(
                call: Call<ResponseDataPenyakit>,
                response: Response<ResponseDataPenyakit>
            ) {
                Log.d("DetailPenyakitResponse", "onResponseDetail called")
                Log.d("DetailPenyakitResponse", "Response code: ${response.code()}")
                Log.d("DetailPenyakitResponse", "Response message: ${response.message()}")
                Log.d("DetailPenyakitResponse", "Response body: ${response.body()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        listPenyakit.clear() // Clear the list before adding new data
                        listPenyakit.addAll(it.data)
                        Log.d("DetailPenyakitResponse", listPenyakit.toString())
                        Log.d("DetailPenyakitResponse", "List Content: $listPenyakit")
                        updateUIWithPenyakitData()
                    }
                } else {
                    Log.d("DetailPenyakitResponse", "Response is not successful")
                }
            }

            override fun onFailure(call: Call<ResponseDataPenyakit>, t: Throwable) {
                Log.d("DetailPenyakitResponse", "API Call Failed")
            }
        })
    }

    private fun updateUIWithPenyakitData() {
        if (listPenyakit.isNotEmpty()) {
            with(binding) {
                tvNamaPenyakit.text = listPenyakit[0].nama_penyakit
                tvEditDeskripsiPenyakit.text = listPenyakit[0].deskripsi_penyakit
                tvEditGejalaPenyakit.text = listPenyakit[0].gejala_penyakit
                tvEditPenyebabPenyakit.text = listPenyakit[0].penyebab_penyakit
                tvEditPenyebaranPenyakit.text = listPenyakit[0].penyebaran_penyakit
                tvEditCaraPencegahanPenyakit.text = listPenyakit[0].cara_pencegahan
                tvEditCaraPenangananPenyakit.text = listPenyakit[0].cara_penanganan
            }
        }else{
            FancyToast.makeText(
                this@DetailPenyakitActivity, "Data Kosong!", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false
            ).show()
        }
    }

    private fun toggleEditorsVisibility(textViewVisibility: Int, editorVisibility: Int) {
        tvEditDeskripsiPenyakit.visibility = textViewVisibility
        editorDeskripsi.visibility = editorVisibility
        tvEditGejalaPenyakit.visibility = textViewVisibility
        editorGejala.visibility = editorVisibility
        tvEditPenyebabPenyakit.visibility = textViewVisibility
        editorPenyebab.visibility = editorVisibility
        tvEditPenyebaranPenyakit.visibility = textViewVisibility
        editorPenyebaran.visibility = editorVisibility
        tvEditCaraPencegahanPenyakit.visibility = textViewVisibility
        editorCaraPencegahan.visibility = editorVisibility
        tvEditCaraPenangananPenyakit.visibility = textViewVisibility
        editorCaraPenanganan.visibility = editorVisibility
    }

    private fun populateEditors() {
        editorDeskripsi.html = tvEditDeskripsiPenyakit.text.toString()
        editorGejala.html = tvEditGejalaPenyakit.text.toString()
        editorPenyebab.html = tvEditPenyebabPenyakit.text.toString()
        editorPenyebaran.html = tvEditPenyebaranPenyakit.text.toString()
        editorCaraPencegahan.html = tvEditCaraPencegahanPenyakit.text.toString()
        editorCaraPenanganan.html = tvEditCaraPenangananPenyakit.text.toString()
    }

    private fun saveEditedText() {
        b?.let { bundle ->
            val id_penyakit = bundle.getInt("id_penyakit", 0)
            val call = RClient.api.update_penyakit(
                editorDeskripsi.html,
                editorGejala.html,
                editorPenyebab.html,
                editorPenyebaran.html,
                editorCaraPencegahan.html,
                editorCaraPenanganan.html,
                id_penyakit
            )

            call.enqueue(object : Callback<ResponseDataPenyakit> {
                override fun onResponse(call: Call<ResponseDataPenyakit>, response: Response<ResponseDataPenyakit>) {
                    if (response.isSuccessful) {
                        tvEditDeskripsiPenyakit.text = editorDeskripsi.html
                        tvEditGejalaPenyakit.text = editorGejala.html
                        tvEditPenyebabPenyakit.text = editorPenyebab.html
                        tvEditPenyebaranPenyakit.text = editorPenyebaran.html
                        tvEditCaraPencegahanPenyakit.text = editorCaraPencegahan.html
                        tvEditCaraPenangananPenyakit.text = editorCaraPenanganan.html

                        toggleEditorsVisibility(View.VISIBLE, View.GONE)
                        (editButton as MaterialButton).text = "Edit"
                    }else{
                        Log.d("DetailPenyakitResponse", "Response Save is not successful")
                    }
                }

                override fun onFailure(call: Call<ResponseDataPenyakit>, t: Throwable) {
                    Log.d("DetailPenyakitResponse", "API Save Call Failed")
                }
            })
        }
    }
}
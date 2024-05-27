package com.example.tugas_akhir_android.Fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.tugas_akhir_android.Adapter.PenyakitAdapter
import com.example.tugas_akhir_android.DataClass.DiagnosaData
import com.example.tugas_akhir_android.DataClass.ResponseDataDiagnosa
import com.example.tugas_akhir_android.HomeUserActivity
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.FragmentHomeUserBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale


class FragmentHomeUser : Fragment() {
    private var _binding: FragmentHomeUserBinding? = null
    private val binding get() = _binding!!
    private var id_user: Int? = null
    private var id_user_view: Int? = null
    private var role_user: String? = null
    private val listDiagnosa = ArrayList<DiagnosaData>()
    private lateinit var sharedViewModel: SharedViewModel
    private var cardView: CardView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity: HomeUserActivity? = activity as HomeUserActivity?
        _binding = FragmentHomeUserBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
//        return inflater.inflate(R.layout.fragment_home, container, false)

        id_user_view = activity?.getIdUserLogin() ?: 0
        id_user = activity?.getIdUserLogin() ?: 0

        role_user = activity?.getRoleUserLogin() ?: null
        sharedViewModel.roleUser.value = role_user
        sharedViewModel.idUser.value = id_user_view
        id_user?.let{ getDataLastDiagnosa(it)}
        Log.d("idUserFragmentHome", "Received id_user: $id_user")
        Log.d("idUserFragmentHome", "Received role_user: $role_user")

        cardView = binding.cvHistory

        showDataFragment()

        binding.diagnoseBtn.setOnClickListener {
            id_user?.let { userId ->
                val fragmentDiagnosa = FragmentDiagnosaUser().apply {
                    arguments = Bundle().apply {
                        putInt("id_user", userId)
                    }
                }
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.layout_fragment_user, fragmentDiagnosa) // R.id.layout_fragment berasal dari HomeActivity
                    addToBackStack(null) // Tambahkan transaksi ke back stack jika ingin bisa kembali ke fragment sebelumnya
                    commit()
                }
            }
        }

        return binding.root
    }

//    fun showDataFragment (){
//        val childFragmentManager = childFragmentManager
//        val fragmentTransaction = childFragmentManager.beginTransaction()
//        val fragment = FragmentHomePenyakit()
//        val bundle = Bundle()
//        bundle.putString("role_user", role_user)
//        fragment.arguments = bundle
//        fragmentTransaction.replace(R.id.fl_home_penyakit, fragment).commit()
//    }
    fun showDataFragment(){
        val childFragmentManager = childFragmentManager
        val fragmentTransaction = childFragmentManager.beginTransaction()
        val fragment = FragmentHomePenyakit()
        fragmentTransaction.replace(R.id.fl_home_penyakit, fragment).commit()
    }

    private fun getDataLastDiagnosa(id:Int){
        val call = RClient.api.getDataLastDiagnosa(id)
        call?.enqueue(object : Callback<ResponseDataDiagnosa> {
            override fun onResponse(
                call: Call<ResponseDataDiagnosa>,
                response: Response<ResponseDataDiagnosa>
            ){
                Log.d("LastDiagnosaResponse", "onResponse called")
                Log.d("LastDiagnosaResponse", "Response code: ${response.code()}")
                Log.d("LastDiagnosaResponse", "Response message: ${response.message()}")
                Log.d("LastDiagnosaResponse", "Response body: ${response.body()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.message == "Data Diagnosa Ditemukan") {
                            listDiagnosa.clear()
                            listDiagnosa.addAll(it.data as List<DiagnosaData>)
                            with(binding) {
                                try {
                                    val percentageValue = listDiagnosa[0].persentase_hasil
                                    val formattedPercentage = String.format(Locale.US, "%.2f%%", percentageValue)
                                    percentage.setText(formattedPercentage)
                                } catch (e: Exception) {
                                    Log.e("FormatError", "Failed to format percentage: ${e.message}")
                                    percentage.setText("N/A")
                                }
                                textDate.setText(listDiagnosa[0].tanggal_diagnosa)
                                textTime.setText(listDiagnosa[0].jam_diagnosa)
                                textDiagnose.setText(listDiagnosa[0].nama_penyakit)
                            }
                        } else {
                            // Handle the case when the status is false
                            Log.e("LastDiagnosaResponse", "API call unsuccessful: ${it.message}")
                        }
                    }
                } else {
                    // Handle the case when the API response is not successful
                    Log.e("LastDiagnosaResponse", "API call failed with response code: ${response.code()}")
                    cardView!!.visibility = View.GONE
                }
            }
            override fun onFailure(call: Call<ResponseDataDiagnosa>, t: Throwable) {
                Log.e("LastDiagnosaResponse", "API call failed", t)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PenyakitAdapter.YOUR_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Navigasi kembali ke FragmentHome
            val fragmentHomeUser = FragmentHomeUser()
            parentFragmentManager.beginTransaction()
                .replace(R.id.layout_fragment_user, fragmentHomeUser)
                .commit()
            sharedViewModel.idUser.value?.let{ getDataLastDiagnosa(it)}
        }
    }



}
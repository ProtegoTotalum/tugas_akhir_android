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
import com.example.tugas_akhir_android.Adapter.PenyakitAdapter
import com.example.tugas_akhir_android.HomeAdminActivity
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.databinding.FragmentHomeBinding
import com.google.android.material.button.MaterialButton


class FragmentHome : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var id_user: Int? = null
    private var role_user: String? = null
    private lateinit var diagnoseBtn: MaterialButton
    private lateinit var foodBtn: MaterialButton
    private lateinit var medicineBtn: MaterialButton
    private lateinit var scheduleBtn: MaterialButton
    private lateinit var geolocationBtn: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity:HomeAdminActivity? = activity as HomeAdminActivity?
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        return inflater.inflate(R.layout.fragment_home, container, false)

        id_user = activity?.getIdUserLogin() ?: 0

        role_user = activity?.getRoleUserLogin() ?: null

        Log.d("idUserFragmentHome", "Received id_user: $id_user")
        Log.d("idUserFragmentHome", "Received role_user: $role_user")
        showDataFragment()

        diagnoseBtn = binding.diagnoseBtn
        foodBtn = binding.foodBtn
        medicineBtn = binding.medicineBtn
        geolocationBtn = binding.geolocationBtn
        scheduleBtn = binding.scheduleBtn

        if(role_user == "admin"){
            disableButtons()
        }


        return binding.root
    }
    fun showDataFragment (){
        val childFragmentManager = childFragmentManager
        val fragmentTransaction = childFragmentManager.beginTransaction()
        val fragment = FragmentHomePenyakit()
        fragmentTransaction.replace(R.id.fl_home_penyakit, fragment).commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PenyakitAdapter.YOUR_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Navigasi kembali ke FragmentHome
            val fragmentHome = FragmentHome()
            parentFragmentManager.beginTransaction()
                .replace(R.id.layout_fragment, fragmentHome)
                .commit()
        }
    }

    private fun disableButtons() {
        diagnoseBtn.isEnabled = false
        diagnoseBtn.isClickable = false
        foodBtn.isEnabled = false
        foodBtn.isClickable = false
        medicineBtn.isEnabled = false
        medicineBtn.isClickable = false
        scheduleBtn.isEnabled = false
        scheduleBtn.isClickable = false
        geolocationBtn.isEnabled = false
        geolocationBtn.isClickable = false
    }

}
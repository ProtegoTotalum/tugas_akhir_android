package com.example.tugas_akhir_android.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.tugas_akhir_android.DataClass.JadwalMakanData
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.FragmentJadwalMakanBinding
import com.shashank.sony.fancytoastlib.FancyToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentJadwalMakan : Fragment() {
    private var _binding: FragmentJadwalMakanBinding? = null
    private val binding get() = _binding!!
    private var id_user: Int? = null
    private lateinit var sharedViewModel: SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJadwalMakanBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        id_user = sharedViewModel.idUser.value
        Log.d("FragmentDiagnosaUser", "Received id_user: $id_user")

        showDataFragment()
        setupListeners()


        return binding.root
    }

    private fun setupListeners() {
        binding.backButtonJadwalMakan.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.tambahJadwalMakanButton.setOnClickListener {
            val fragmentAddJadwalMakan = FragmentAddJadwalMakan()
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.layout_fragment_user, fragmentAddJadwalMakan)
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun showDataFragment() {
        val fragment = FragmentRecycleJadwalMakan()
        childFragmentManager.beginTransaction().apply {
            replace(R.id.fl_jadwal_makan, fragment)
            addToBackStack(null)
            commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
package com.example.tugas_akhir_android.Fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.tugas_akhir_android.Adapter.PertanyaanAdapter
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.FragmentPertanyaanUserBinding


class FragmentPertanyaanUser : Fragment() {
    private var _binding: FragmentPertanyaanUserBinding? = null
    private val binding get() = _binding!!
    private var id_user: Int? = null
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPertanyaanUserBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

//        arguments?.let {
//            id_user = it.getInt("id_user")
//        }
        id_user = sharedViewModel.idUser.value
        Log.d("FragmentPertanyaanUser", "Received id_user: $id_user")
        showDataFragment()

        return binding.root
    }

    private fun showDataFragment() {
        val fragment = FragmentRecyclePertanyaan()
        childFragmentManager.beginTransaction().apply {
            replace(R.id.fl_pertanyaan_user, fragment)
            addToBackStack(null)
            commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
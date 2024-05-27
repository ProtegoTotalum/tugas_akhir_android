package com.example.tugas_akhir_android.Fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.FragmentDiagnosaUserBinding



class FragmentDiagnosaUser : Fragment() {
    private var _binding: FragmentDiagnosaUserBinding? = null
    private val binding get() = _binding!!
    private var id_user: Int? = null
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiagnosaUserBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

//        arguments?.let {
//            id_user = it.getInt("id_user")
//        }

        id_user = sharedViewModel.idUser.value
        Log.d("FragmentDiagnosaUser", "Received id_user: $id_user")

        binding.backButtonDiagnosaUser.setOnClickListener{
            parentFragmentManager.popBackStack()
        }

        binding.btnMulaiDiagnosa.setOnClickListener {
            val fragmentPertanyaan = FragmentPertanyaanUser()
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.layout_fragment_user, fragmentPertanyaan) // R.id.layout_fragment berasal dari HomeActivity
                addToBackStack(null)
                commit()
            }
//            id_user?.let { userId ->
//                val fragmentPertanyaan = FragmentPertanyaanUser().apply {
//                    arguments = Bundle().apply {
//                        putInt("id_user", userId)
//                    }
//                }
//                parentFragmentManager.beginTransaction().apply {
//                    replace(R.id.layout_fragment_user, fragmentPertanyaan) // R.id.layout_fragment berasal dari HomeActivity
//                    addToBackStack(null)
//                    commit()
//                }
//            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
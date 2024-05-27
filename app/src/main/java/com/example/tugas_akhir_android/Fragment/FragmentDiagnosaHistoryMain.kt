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
import com.example.tugas_akhir_android.Adapter.HistoryAdapter
import com.example.tugas_akhir_android.DataClass.UserData
import com.example.tugas_akhir_android.HomeUserActivity
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.FragmentDiagnosaHistoryMainBinding



class FragmentDiagnosaHistoryMain : Fragment() {
    private var _binding: FragmentDiagnosaHistoryMainBinding? = null
    private val binding get() = _binding!!
    private var id_user: Int? = null
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity: HomeUserActivity? = activity as HomeUserActivity?
        _binding = FragmentDiagnosaHistoryMainBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        id_user = activity?.getIdUserLogin() ?: 0
        sharedViewModel.idUser.value = id_user
        Log.d("DiagnosaHistoryMain", "Received id_user: $id_user")
        showDataFragment()

        return binding.root
    }
//    fun showDataFragment (){
//        val childFragmentManager = childFragmentManager
//        val fragmentTransaction = childFragmentManager.beginTransaction()
//        val fragment = FragmentDiagnosaHistory()
//        val bundle = Bundle()
//        id_user?.let { bundle.putInt("id_user", it) }
//        fragment.arguments = bundle
//        childFragmentManager.beginTransaction().apply {
//            replace(R.id.fl_diagnosa_history_main, fragment)
//            addToBackStack(null)
//            commit()
//        }
//    }

    private fun showDataFragment() {
        val fragment = FragmentDiagnosaHistory()
        childFragmentManager.beginTransaction().apply {
            replace(R.id.fl_diagnosa_history_main, fragment)
            addToBackStack(null)
            commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == HistoryAdapter.YOUR_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Navigasi kembali ke FragmentDiagnosaHistoryMain
            val fragmentDiagnosaHistoryMain = FragmentDiagnosaHistoryMain()
            parentFragmentManager.beginTransaction()
                .replace(R.id.layout_fragment_user, fragmentDiagnosaHistoryMain)
                .commit()
        }
    }
}
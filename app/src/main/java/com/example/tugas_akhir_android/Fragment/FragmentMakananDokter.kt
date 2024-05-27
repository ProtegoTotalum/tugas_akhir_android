package com.example.tugas_akhir_android.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tugas_akhir_android.HomeDokterActivity
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.databinding.FragmentMakananDokterBinding


class FragmentMakananDokter : Fragment() {
    private var _binding: FragmentMakananDokterBinding? = null
    private val binding get() = _binding!!
    private var id_user: Int? = null
    private var role_user: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity: HomeDokterActivity? = activity as HomeDokterActivity?
        _binding = FragmentMakananDokterBinding.inflate(inflater, container, false)

        id_user = activity?.getIdUserLogin() ?: 0

        role_user = activity?.getRoleUserLogin() ?: null
        Log.d("idUserFragmentMakananDokter", "Received id_user: $id_user")
        Log.d("idUserFragmentMakananDokter", "Received role_user: $role_user")

        showDataFragment()

        return binding.root
    }
    fun showDataFragment (){
        val childFragmentManager = childFragmentManager
        val fragmentTransaction = childFragmentManager.beginTransaction()
        val fragment = FragmentRVMakananDokter()
        val bundle = Bundle()
        bundle.putString("role_user", role_user)
        fragment.arguments = bundle
        fragmentTransaction.replace(R.id.fl_bahan_makanan_dokter, fragment).commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
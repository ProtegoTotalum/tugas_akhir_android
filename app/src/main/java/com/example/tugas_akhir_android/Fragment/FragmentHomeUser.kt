package com.example.tugas_akhir_android.Fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tugas_akhir_android.Adapter.PenyakitAdapter
import com.example.tugas_akhir_android.HomeUserActivity
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.databinding.FragmentHomeUserBinding


class FragmentHomeUser : Fragment() {
    private var _binding: FragmentHomeUserBinding? = null
    private val binding get() = _binding!!
    private var id_user: Int? = null
    private var role_user: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity: HomeUserActivity? = activity as HomeUserActivity?
        _binding = FragmentHomeUserBinding.inflate(inflater, container, false)
//        return inflater.inflate(R.layout.fragment_home, container, false)

        id_user = activity?.getIdUserLogin() ?: 0

        role_user = activity?.getRoleUserLogin() ?: null

        Log.d("idUserFragmentHome", "Received id_user: $id_user")
        Log.d("idUserFragmentHome", "Received role_user: $role_user")
        showDataFragment()

        return binding.root
    }
    fun showDataFragment (){
        val childFragmentManager = childFragmentManager
        val fragmentTransaction = childFragmentManager.beginTransaction()
        val fragment = FragmentHomePenyakit()
        val bundle = Bundle()
        bundle.putString("role_user", role_user)
        fragment.arguments = bundle
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
            val fragmentHomeUser = FragmentHomeUser()
            parentFragmentManager.beginTransaction()
                .replace(R.id.layout_fragment_user, fragmentHomeUser)
                .commit()
        }
    }

}
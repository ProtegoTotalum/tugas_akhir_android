package com.example.tugas_akhir_android.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.tugas_akhir_android.Adapter.BahanMakananAdapter
import com.example.tugas_akhir_android.HomeDokterActivity
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.FragmentMakananDokterBinding


class FragmentMakananDokter : Fragment() {
    private var _binding: FragmentMakananDokterBinding? = null
    private val binding get() = _binding!!
    private var id_user: Int? = null
    private var id_user_view: Int? = null
    private var role_user: String? = null
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var bahanMakananAdapter: BahanMakananAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity: HomeDokterActivity? = activity as HomeDokterActivity?
        _binding = FragmentMakananDokterBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        id_user = activity?.getIdUserLogin() ?: 0
        id_user_view = activity?.getIdUserLogin() ?: 0

        role_user = activity?.getRoleUserLogin() ?: null

        sharedViewModel.roleUser.value = role_user
        sharedViewModel.idUser.value = id_user_view

        Log.d("idUserFragmentMakananDokter", "Received id_user: $id_user")
        Log.d("idUserFragmentMakananDokter", "Received role_user: $role_user")

        binding.tambahBahanMakananButton.setOnClickListener{
            val fragmentTambahMakanan = FragmentAddBahanMakananDokter()
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.layout_fragment_dokter, fragmentTambahMakanan) // R.id.layout_fragment berasal dari HomeActivity
                addToBackStack(null) // Tambahkan transaksi ke back stack jika ingin bisa kembali ke fragment sebelumnya
                commit()
            }
        }

        binding.txtCariBahanMakananDokter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterData(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })


        showDataFragment()
        return binding.root
    }

    private fun filterData(query: String) {
        val fragment = childFragmentManager.findFragmentById(R.id.fl_bahan_makanan_dokter) as? FragmentRVMakananDokter
        fragment?.filterData(query)
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
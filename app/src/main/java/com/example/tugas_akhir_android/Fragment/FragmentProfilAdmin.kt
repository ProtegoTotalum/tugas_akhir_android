package com.example.tugas_akhir_android.Fragment

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import com.example.tugas_akhir_android.DataClass.ResponseDataUser
import com.example.tugas_akhir_android.DataClass.UserData
import com.example.tugas_akhir_android.HomeAdminActivity
import com.example.tugas_akhir_android.LoginActivity
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.FragmentProfilAdminBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentProfilAdmin : Fragment() {
    private var _binding: FragmentProfilAdminBinding? = null
    private val binding get() = _binding!!
    private val listAdmin = ArrayList<UserData>()
    private var id_user: Int? = null
    private val viewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity:HomeAdminActivity? = activity as HomeAdminActivity?
        _binding = FragmentProfilAdminBinding.inflate(inflater, container, false)

        id_user = activity?.getIdUserLogin() ?: 0

        id_user?.let{ getDataAdmin(it)}
        Log.d("idAdmin", "Received id_user: $id_user")


        // Set up click listener for the menu button
        binding.menuAdminBtn.setOnClickListener {
            showPopupMenu(it)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userData.observe(viewLifecycleOwner, { userData ->
            userData?.let {
                with(binding) {
                    prInputNamaAdmin.setText(it.nama)
                    prInputEmailAdmin.setText(it.email)
                    prInputTglLahirAdmin.setText(it.tgl_lahir_user)
                    prInputUmurAdmin.setText(it.umur_user)
                    prInputNoTelpAdmin.setText(it.no_telp_user)
                    prInputGenderAdmin.setText(it.gender_user)
                    prInputRoleAdmin.setText(it.role_user)
                }
            }
        })

    }


    private fun getDataAdmin(id:Int){

        val call = RClient.api.getDataUser(id)
        call?.enqueue(object : Callback<ResponseDataUser> {
            override fun onResponse(
                call: Call<ResponseDataUser>,
                response: Response<ResponseDataUser>
            ){
                Log.d("AdminResponse", "onResponse called")
                Log.d("AdminResponse", "Response code: ${response.code()}")
                Log.d("AdminResponse", "Response message: ${response.message()}")
                Log.d("AdminResponse", "Response body: ${response.body()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.message == "Data User Ditemukan") {
                            listAdmin.clear()
                            listAdmin.addAll(it.data as List<UserData>)
                            with(binding) {
                                prInputNamaAdmin.setText(listAdmin[0].nama)
                                prInputEmailAdmin.setText(listAdmin[0].email)
                                prInputTglLahirAdmin.setText(listAdmin[0].tgl_lahir_user)
                                prInputUmurAdmin.setText(listAdmin[0].umur_user)
                                prInputNoTelpAdmin.setText(listAdmin[0].no_telp_user)
                                prInputGenderAdmin.setText(listAdmin[0].gender_user)
                                prInputRoleAdmin.setText(listAdmin[0].role_user)
                            }
                        } else {
                            // Handle the case when the status is false
                            Log.e("AdminResponse", "API call unsuccessful: ${it.message}")
                        }
                    }
                } else {
                    // Handle the case when the API response is not successful
                    Log.e("AdminResponse", "API call failed with response code: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ResponseDataUser>, t: Throwable) {
                Log.e("AdminResponse", "API call failed", t)
            }
        })
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_admin, popupMenu.menu)


        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.tab_edit_admin -> {
                    id_user?.let { userId ->
                        val fragmentEditProfil = FragmentEditProfil().apply {
                            arguments = Bundle().apply {
                                putInt("id_user", userId)
                            }
                        }
                        parentFragmentManager.beginTransaction().apply {
                            replace(R.id.layout_fragment, fragmentEditProfil) // R.id.layout_fragment berasal dari HomeActivity
                            addToBackStack(null) // Tambahkan transaksi ke back stack jika ingin bisa kembali ke fragment sebelumnya
                            commit()
                        }
                    }
                    true
                }
                R.id.tab_gantipas_admin -> {
                    id_user?.let { userId ->
                        val fragmentGantiPas = FragmentGantiPas().apply {
                            arguments = Bundle().apply {
                                putInt("id_user", userId)
                            }
                        }
                        parentFragmentManager.beginTransaction().apply {
                            replace(R.id.layout_fragment, fragmentGantiPas) // R.id.layout_fragment berasal dari HomeActivity
                            addToBackStack(null) // Tambahkan transaksi ke back stack jika ingin bisa kembali ke fragment sebelumnya
                            commit()
                        }
                    }
                    true
                }
                R.id.tab_logout_admin -> {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                    builder.setMessage("Apakah anda yakin ingin keluar?")
                        .setPositiveButton("YES") { DialogInterface, _ ->
                            val intent = Intent(requireContext(), LoginActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }
                        .setNegativeButton("NO") { DialogInterface, _ ->
                            DialogInterface.dismiss()
                        }
                        .show()
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.tugas_akhir_android.Fragment

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
import com.example.tugas_akhir_android.HomeUserActivity
import com.example.tugas_akhir_android.LoginActivity
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.FragmentProfilUserBinding
import com.shashank.sony.fancytoastlib.FancyToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentProfilUser : Fragment() {
    private var _binding: FragmentProfilUserBinding? = null
    private val binding get() = _binding!!
    private val listUser = ArrayList<UserData>()
    private var id_user: Int? = null
    private val viewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity: HomeUserActivity? = activity as HomeUserActivity?
        _binding = FragmentProfilUserBinding.inflate(inflater, container, false)

        id_user = activity?.getIdUserLogin() ?: 0

        id_user?.let{ getDataUser(it)}
        Log.d("idUser", "Received id_user: $id_user")


        // Set up click listener for the menu button
        binding.menuUserBtn.setOnClickListener {
            showPopupMenu(it)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userData.observe(viewLifecycleOwner, { userData ->
            userData?.let {
                with(binding) {
                    prInputNamaUser.setText(it.nama)
                    prInputEmailUser.setText(it.email)
                    prInputTglLahirUser.setText(it.tgl_lahir_user)
                    prInputUmurUser.setText(it.umur_user)
                    prInputNoTelpUser.setText(it.no_telp_user)
                    prInputGenderUser.setText(it.gender_user)
                }
            }
        })

    }


    private fun getDataUser(id:Int){

        val call = RClient.api.getDataUser(id)
        call?.enqueue(object : Callback<ResponseDataUser> {
            override fun onResponse(
                call: Call<ResponseDataUser>,
                response: Response<ResponseDataUser>
            ){
                Log.d("UserResponse", "onResponse called")
                Log.d("UserResponse", "Response code: ${response.code()}")
                Log.d("UserResponse", "Response message: ${response.message()}")
                Log.d("UserResponse", "Response body: ${response.body()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.message == "Data User Ditemukan") {
                            listUser.clear()
                            listUser.addAll(it.data as List<UserData>)
                            with(binding) {
                                prInputNamaUser.setText(listUser[0].nama)
                                prInputEmailUser.setText(listUser[0].email)
                                prInputTglLahirUser.setText(listUser[0].tgl_lahir_user)
                                prInputUmurUser.setText(listUser[0].umur_user)
                                prInputNoTelpUser.setText(listUser[0].no_telp_user)
                                prInputGenderUser.setText(listUser[0].gender_user)
                            }
                        } else {
                            // Handle the case when the status is false
                            Log.e("UserResponse", "API call unsuccessful: ${it.message}")
                        }
                    }
                } else {
                    // Handle the case when the API response is not successful
                    Log.e("UserResponse", "API call failed with response code: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ResponseDataUser>, t: Throwable) {
                Log.e("UserResponse", "API call failed", t)
            }
        })
    }

    private fun deaktivasiAkun(id:Int){

        val call = RClient.api.deaktivasi_akun(id)
        call?.enqueue(object : Callback<ResponseDataUser> {
            override fun onResponse(
                call: Call<ResponseDataUser>,
                response: Response<ResponseDataUser>
            ){
                Log.d("UserResponse", "onResponse called")
                Log.d("UserResponse", "Response code: ${response.code()}")
                Log.d("UserResponse", "Response message: ${response.message()}")
                Log.d("UserResponse", "Response body: ${response.body()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        FancyToast.makeText(
                            requireContext(),
                            "${response.body()?.message}",
                            FancyToast.LENGTH_LONG,
                            FancyToast.ERROR,
                            false
                        ).show()
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                } else {
                    // Handle the case when the API response is not successful
                    Log.e("UserResponse", "API call failed with response code: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ResponseDataUser>, t: Throwable) {
                Log.e("UserResponse", "API call failed", t)
            }
        })
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.menu_user, popupMenu.menu)


        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.tab_edit_user -> {
                    id_user?.let { userId ->
                        val fragmentEditProfil = FragmentEditProfil().apply {
                            arguments = Bundle().apply {
                                putInt("id_user", userId)
                            }
                        }
                        parentFragmentManager.beginTransaction().apply {
                            replace(R.id.layout_fragment_user, fragmentEditProfil) // R.id.layout_fragment berasal dari HomeActivity
                            addToBackStack(null) // Tambahkan transaksi ke back stack jika ingin bisa kembali ke fragment sebelumnya
                            commit()
                        }
                    }
                    true
                }
                R.id.tab_gantipas_user -> {
                    id_user?.let { userId ->
                        val fragmentGantiPas = FragmentGantiPas().apply {
                            arguments = Bundle().apply {
                                putInt("id_user", userId)
                            }
                        }
                        parentFragmentManager.beginTransaction().apply {
                            replace(R.id.layout_fragment_user, fragmentGantiPas) // R.id.layout_fragment berasal dari HomeActivity
                            addToBackStack(null) // Tambahkan transaksi ke back stack jika ingin bisa kembali ke fragment sebelumnya
                            commit()
                        }
                    }
                    true
                }
                R.id.tab_deaktivasi_akun_user -> {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                    builder.setMessage("Apakah anda yakin ingin deaktivasi akun? Akun anda tidak akan bisa kembali")
                        .setPositiveButton("YES") { DialogInterface, _ ->
                            id_user?.let { userId ->
                                deaktivasiAkun(userId)
                            }
                        }
                        .setNegativeButton("NO") { DialogInterface, _ ->
                            DialogInterface.dismiss()
                        }
                        .show()
                    true
                }
                R.id.tab_logout_user -> {
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
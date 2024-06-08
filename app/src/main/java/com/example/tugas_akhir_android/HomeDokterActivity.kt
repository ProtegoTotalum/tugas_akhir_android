package com.example.tugas_akhir_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.tugas_akhir_android.Fragment.FragmentKonsultasiDokter
import com.example.tugas_akhir_android.Fragment.FragmentMakananDokter
import com.example.tugas_akhir_android.Fragment.FragmentProfilDokter
import nl.joery.animatedbottombar.AnimatedBottomBar

class HomeDokterActivity : AppCompatActivity() {
    private var id_user: Int = 0
    private var role_user: String? = null
    private var provinsi_user: String? = null
    private var kabkot_user: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_dokter)

        id_user = intent.extras!!.getInt("id_user",0)
        role_user = intent.extras!!.getString("role_user",null)
        provinsi_user = intent.extras!!.getString("provinsi_user",null)
        kabkot_user = intent.extras!!.getString("kota_user",null)


        Log.d("idUserHomeDokter", "Received id_user: $id_user")
        Log.d("idUserHomeDokter", "Received role_user: $role_user")
        Log.d("idUserHomeDokter", "Received provinsi_user: $provinsi_user")
        Log.d("idUserHomeDokter", "Received kabkotkot_user: $kabkot_user")

        val firstFragment = FragmentKonsultasiDokter()
        val secondFragment = FragmentMakananDokter()
//        val thirdFragment = FragmentObatDokter()
        val fourthFragment = FragmentProfilDokter()


        setCurrentFragment(firstFragment)


        val bottomNavigationViewMember =
            findViewById<AnimatedBottomBar>(R.id.bottomNavigationViewDokter)

        bottomNavigationViewMember.onTabSelected = {
            Log.d("bottomNavigationView", "Selected tab: " + it.title)
        }
        bottomNavigationViewMember.onTabReselected = {
            Log.d("bottomNavigationView", "Reselected tab: " + it.title)
        }

        bottomNavigationViewMember.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                when(newTab.id) {
                    R.id.tab_konsultasi_dokter -> setCurrentFragment(firstFragment)
                    R.id.tab_makanan_dokter -> setCurrentFragment(secondFragment)
//                    R.id.tab_obat_dokter ->setCurrentFragment(thirdFragment)
                    R.id.tab_profil_dokter ->setCurrentFragment(fourthFragment)
                }
            }
        })
    }
    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.layout_fragment_dokter, fragment)
            commit()
        }

    fun getIdUserLogin(): Int? {
        return id_user
    }

    fun getRoleUserLogin(): String? {
        return role_user
    }

    fun getProvinsiUserLogin(): String? {
        return provinsi_user
    }

    fun getKabKotUserLogin(): String? {
        return kabkot_user
    }
}
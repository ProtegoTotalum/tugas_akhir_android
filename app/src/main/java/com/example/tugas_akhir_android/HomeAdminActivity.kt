package com.example.tugas_akhir_android

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.tugas_akhir_android.Fragment.FragmentHome
import com.example.tugas_akhir_android.Fragment.FragmentProfilAdmin
import nl.joery.animatedbottombar.AnimatedBottomBar

class HomeAdminActivity : AppCompatActivity() {
    private var id_user: Int = 0
    private var role_user: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)

        id_user = intent.extras!!.getInt("id_user",0)
        role_user = intent.extras!!.getString("role_user",null)


        Log.d("idUserHomeAdmin", "Received id_user: $id_user")
        Log.d("idUserHomeAdmin", "Received role_user: $role_user")
//
        val firstFragment = FragmentHome()
//        val secondFragment = HomeUserActivity()
        val thirdFragment = FragmentProfilAdmin()
//        val fourthFragment = FragmentObat()


        setCurrentFragment(firstFragment)


        val bottomNavigationViewMember =
            findViewById<AnimatedBottomBar>(R.id.bottomNavigationView)

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
                    R.id.tab_home_admin -> setCurrentFragment(firstFragment)
                    R.id.tab_profil_admin ->setCurrentFragment(thirdFragment)

//                    R.id.tab_profil_member -> setCurrentFragment(secondFragment)
//                    R.id.tab_profil_member -> {
//                        val intent = Intent(this@HomeMemberActivity, PresensiInstrukturActivity::class.java)
//                        startActivity(intent)
//                    }

//                    R.id.tab_obat -> {
//                        val intent = Intent(this@HomeActivity, ObatActivity::class.java)
//                        startActivity(intent)
//                    }
//
//                    R.id.tab_supplier -> {
//                        val intent = Intent(this@HomeActivity, SupplierActivity::class.java)
//                        startActivity(intent)
//                    }
//
//                    R.id.tab_profil -> setCurrentFragment(thirdFragment)
//                    R.id.tab_profil -> {
//                        val intent = Intent(this@HomeActivity, UserActivity::class.java)
//                        startActivity(intent)
//                    }

//                    R.id.logout -> {
//                        val builder: AlertDialog.Builder = AlertDialog.Builder(this@HomeAdminActivity)
//                        builder.setMessage("Are you sure want to exit?")
//                            .setPositiveButton("YES", object : DialogInterface.OnClickListener {
//                                override fun onClick(dialogInterface: DialogInterface, i: Int) {
//
//                                    //Keluar dari aplikasi
//                                    finishAndRemoveTask()
//                                }
//                            })
//                            .show()
//                    }
                }
            }
        })
    }
    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.layout_fragment, fragment)
            commit()
        }

    fun getIdUserLogin(): Int? {
        return id_user
    }

    fun getRoleUserLogin(): String? {
        return role_user
    }
}
package com.example.tugas_akhir_android.Fragment

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.tugas_akhir_android.DataClass.JadwalMakanData
import com.example.tugas_akhir_android.DataClass.ResponseDataJadwalMakan
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.FragmentAddJadwalMakanBinding
import com.example.tugas_akhir_android.databinding.FragmentJadwalMakanBinding
import com.shashank.sony.fancytoastlib.FancyToast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentAddJadwalMakan : Fragment() {
    private var _binding: FragmentAddJadwalMakanBinding? = null
    private val binding get() = _binding!!
    private val listJadwal = ArrayList<JadwalMakanData>()
    private var id_user: Int? = null
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddJadwalMakanBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        id_user = sharedViewModel.idUser.value
        Log.d("FragmentAddJadwalMakan", "Received id_user: $id_user")


        binding.backButtonAddJadwalMakan.setOnClickListener{
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.timePicker.setIs24HourView(false)

        // Tipe Jadwal Makan
        val autoCompleteTipe: AutoCompleteTextView = binding.tipeJadwalMakanACT
        val optionsTipe = listOf("Sarapan", "Snack Pagi", "Makan Siang", "Snack Siang", "Snack Sore", "Makan Malam", "Snack Malam")
        val adapterTipe = ArrayAdapter(requireContext(), R.layout.list_item, optionsTipe)
        autoCompleteTipe.setAdapter(adapterTipe)
        autoCompleteTipe.onItemClickListener = AdapterView.OnItemClickListener { parent, view, i, id ->
            val itemSelectedTipe = parent.getItemAtPosition(i)
            FancyToast.makeText(requireContext(), "Item : $itemSelectedTipe", FancyToast.LENGTH_SHORT, FancyToast.DEFAULT, false).show()
        }

        // Pengulangan Jadwal Makan
        val autoCompletePengulangan: AutoCompleteTextView = binding.pengulanganJadwalMakanACT
        val optionsPengulangan = listOf("Sekali", "Setiap Hari", "Custom")
        val adapterPengulangan = ArrayAdapter(requireContext(), R.layout.list_item, optionsPengulangan)
        autoCompletePengulangan.setAdapter(adapterPengulangan)
        autoCompletePengulangan.onItemClickListener = AdapterView.OnItemClickListener { parent, view, i, id ->
            val itemSelectedPengulangan = parent.getItemAtPosition(i)
            FancyToast.makeText(requireContext(), "Item : $itemSelectedPengulangan", FancyToast.LENGTH_SHORT, FancyToast.DEFAULT, false).show()

            // Handle auto-selection of days
            when (itemSelectedPengulangan.toString()) {
                "Setiap Hari" -> selectAllDays(true)
                "Sekali" -> selectAllDays(false)
                "Custom" -> clearAllDays()
            }
        }

        // Setup chip listeners
        setupChipListeners()

        // Button click to create JadwalMakanData
        binding.btnTambahJadwalMakan.setOnClickListener {
            val hour = binding.timePicker.hour
            val minute = binding.timePicker.minute
            val isPM = hour >= 12

            val formattedTime = convertTo24HourFormat(hour % 12, minute, isPM)
            val tipe_jadwal_makan = binding.tipeJadwalMakanACT.text.toString()
            val pengulangan_jadwal_makan = binding.pengulanganJadwalMakanACT.text.toString()
            val waktu_makan = formattedTime
            val senin = if (binding.chipSenin.isChecked) 1 else 0
            val selasa = if (binding.chipSelasa.isChecked) 1 else 0
            val rabu = if (binding.chipRabu.isChecked) 1 else 0
            val kamis = if (binding.chipKamis.isChecked) 1 else 0
            val jumat = if (binding.chipJumat.isChecked) 1 else 0
            val sabtu = if (binding.chipSabtu.isChecked) 1 else 0
            val minggu = if (binding.chipMinggu.isChecked) 1 else 0
            if (tipe_jadwal_makan.isEmpty()) {
                FancyToast.makeText(
                    requireContext(),
                    "Sesi makan tidak boleh kosong",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }else if (pengulangan_jadwal_makan.isEmpty()) {
                FancyToast.makeText(
                    requireContext(),
                    "Pengulangan tidak boleh kosong!",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }else if (waktu_makan.isEmpty()) {
                FancyToast.makeText(
                    requireContext(),
                    "Waktu makan tidak boleh kosong!",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }else if (pengulangan_jadwal_makan == "Custom" && listOf(senin, selasa, rabu, kamis, jumat, sabtu, minggu).count { it == 1 } < 2) {
                FancyToast.makeText(
                    requireContext(),
                    "Pengulangan custom harus memilih setidaknya 2 hari berbeda!",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }else{
                sendJadwalMakanData()
            }
        }
    }

    private fun setupChipListeners() {
        val chips = listOf(binding.chipSenin, binding.chipSelasa, binding.chipRabu, binding.chipKamis, binding.chipJumat, binding.chipSabtu, binding.chipMinggu)
        for (chip in chips) {
            chip.setOnCheckedChangeListener { _, _ ->
                updatePengulanganJadwalMakan()
            }
        }
    }

    private fun updatePengulanganJadwalMakan() {
        val chips = listOf(binding.chipSenin, binding.chipSelasa, binding.chipRabu, binding.chipKamis, binding.chipJumat, binding.chipSabtu, binding.chipMinggu)
        when {
            chips.all { it.isChecked } -> {
                binding.pengulanganJadwalMakanACT.setText("Setiap Hari", false)
            }
            chips.any { it.isChecked } -> {
                binding.pengulanganJadwalMakanACT.setText("Custom", false)
            }
            else -> {
                binding.pengulanganJadwalMakanACT.setText("", false)
            }
        }
    }

    private fun selectAllDays(select: Boolean) {
        binding.chipSenin.isChecked = select
        binding.chipSelasa.isChecked = select
        binding.chipRabu.isChecked = select
        binding.chipKamis.isChecked = select
        binding.chipJumat.isChecked = select
        binding.chipSabtu.isChecked = select
        binding.chipMinggu.isChecked = select
    }

    private fun clearAllDays() {
        selectAllDays(false)
    }


    private fun convertTo24HourFormat(hour: Int, minute: Int, isPM: Boolean): String {
        val hour24 = if (isPM && hour < 12) {
            hour + 12
        } else if (!isPM && hour == 12) {
            0
        } else {
            hour
        }
        return String.format("%02d:%02d", hour24, minute)
    }

    private fun sendJadwalMakanData() {
        val hour = binding.timePicker.hour
        val minute = binding.timePicker.minute
        val isPM = hour >= 12

        val formattedTime = convertTo24HourFormat(hour % 12, minute, isPM)
        val status_jadwal = 1
        val tipe_jadwal_makan = binding.tipeJadwalMakanACT.text.toString()
        val pengulangan_jadwal_makan = binding.pengulanganJadwalMakanACT.text.toString()
        val waktu_makan = formattedTime
        val senin = if (binding.chipSenin.isChecked) 1 else 0
        val selasa = if (binding.chipSelasa.isChecked) 1 else 0
        val rabu = if (binding.chipRabu.isChecked) 1 else 0
        val kamis = if (binding.chipKamis.isChecked) 1 else 0
        val jumat = if (binding.chipJumat.isChecked) 1 else 0
        val sabtu = if (binding.chipSabtu.isChecked) 1 else 0
        val minggu = if (binding.chipMinggu.isChecked) 1 else 0

        val call = RClient.api.storeJadwalMakan(id_user, status_jadwal, tipe_jadwal_makan, pengulangan_jadwal_makan, waktu_makan, senin, selasa, rabu, kamis, jumat, sabtu, minggu)
        call.enqueue(object : Callback<ResponseDataJadwalMakan> {
            override fun onResponse(call: Call<ResponseDataJadwalMakan>, response: Response<ResponseDataJadwalMakan>) {
                Log.d("AddJadwalMakanResponse", "onResponseDetail called")
                Log.d("AddJadwalMakanResponse", "Response code: ${response.code()}")
                Log.d("AddJadwalMakanResponse", "Response message: ${response.message()}")
                Log.d("AddJadwalMakanResponse", "Response body: ${response.body()}")
                if (response.isSuccessful) {
                    FancyToast.makeText(
                        requireContext(),
                        "Jadwal Makan Berhasil Ditambahkan!",
                        FancyToast.LENGTH_LONG,
                        FancyToast.SUCCESS,
                        false)
                        .show()
                    parentFragmentManager.popBackStack()
                } else {
                    FancyToast.makeText(requireContext(), "Gagal menambahkan jadwal makan: ${response.message()}", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show()
                }
            }

            override fun onFailure(call: Call<ResponseDataJadwalMakan>, t: Throwable) {
                FancyToast.makeText(
                    requireContext(),
                    "Error: ${t.message}",
                    FancyToast.LENGTH_LONG,
                    FancyToast.ERROR,
                    false)
                    .show()
                Log.e("AddJadwalMakanResponse", "API call failed", t)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.tugas_akhir_android.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugas_akhir_android.DataClass.JadwalMakanData
import com.example.tugas_akhir_android.DataClass.ResponseDataJadwalMakan
import com.example.tugas_akhir_android.R
import com.example.tugas_akhir_android.RClient
import com.example.tugas_akhir_android.SharedViewModel
import com.example.tugas_akhir_android.databinding.FragmentDetailJadwalMakanBinding
import com.shashank.sony.fancytoastlib.FancyToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentDetailJadwalMakan : Fragment() {
    private var _binding: FragmentDetailJadwalMakanBinding? = null
    private val binding get() = _binding!!
    private var id_jadwal_makan: Int? = null
    private val listJadwalMakan = ArrayList<JadwalMakanData>()
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailJadwalMakanBinding.inflate(inflater, container, false)
        id_jadwal_makan = arguments?.getInt("id_jadwal_makan")

        id_jadwal_makan?.let { getDataDetailJadwalMakan(it) }

        binding.backButtonDetailJadwalMakan.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnEditJadwalMakan.setOnClickListener{
            id_jadwal_makan?.let { editJadwalMakan(it) }
        }

        binding.btnDeleteJadwalMakan.setOnClickListener{
            AlertDialog.Builder(requireContext()).apply {
                setTitle("Hapus Jadwal")
                setMessage("Apakah Anda yakin ingin menghapus jadwal makan ini?")
                setPositiveButton("Ya") { dialog, which ->
                    id_jadwal_makan?.let { hapusJadwalMakan(it) }
                }
                setNegativeButton("Tidak") { dialog, which ->
                    dialog.dismiss()
                }
                create()
                show()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.detailTimePicker.setIs24HourView(false)

        val autoCompleteTipe: AutoCompleteTextView = binding.detailTipeJadwalMakanACT
        val optionsTipe = listOf(
            "Sarapan",
            "Snack Pagi",
            "Makan Siang",
            "Snack Siang",
            "Snack Sore",
            "Makan Malam",
            "Snack Malam"
        )
        val adapterTipe = ArrayAdapter(requireContext(), R.layout.list_item, optionsTipe)
        autoCompleteTipe.setAdapter(adapterTipe)

        val autoCompletePengulangan: AutoCompleteTextView = binding.detailPengulanganJadwalMakanACT
        val optionsPengulangan = listOf("Sekali", "Setiap Hari", "Custom")
        val adapterPengulangan = ArrayAdapter(requireContext(), R.layout.list_item, optionsPengulangan)
        autoCompletePengulangan.setAdapter(adapterPengulangan)
        autoCompletePengulangan.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, i, id ->
                val itemSelectedPengulangan = parent.getItemAtPosition(i).toString()

                when (itemSelectedPengulangan) {
                    "Setiap Hari" -> selectAllDays(true)
                    "Sekali" -> selectAllDays(false)
                    "Custom" -> clearAllDays()
                }
            }

        viewModel.jadwalMakanDataDetail.observe(viewLifecycleOwner, { jadwalMakanData ->
            jadwalMakanData?.let {
                with(binding) {
                    val timeParts = it.waktu_makan.split(":")
                    val hour24 = timeParts[0].toInt()
                    val minute = timeParts[1].toInt()
                    val isPM = hour24 >= 12
                    val hour12 = if (hour24 % 12 == 0) 12 else hour24 % 12

                    detailTimePicker.hour = hour12
                    detailTimePicker.minute = minute
                    detailTimePicker.setIs24HourView(false)
                    detailTimePicker.setHour(hour12)
                    detailTimePicker.setMinute(minute)


                    detailTipeJadwalMakanACT.setText(it.tipe_jadwal_makan, false)
                    detailPengulanganJadwalMakanACT.setText(it.pengulangan_jadwal_makan, false)

                    chipDetailSenin.isChecked = it.senin == 1
                    chipDetailSelasa.isChecked = it.selasa == 1
                    chipDetailRabu.isChecked = it.rabu == 1
                    chipDetailKamis.isChecked = it.kamis == 1
                    chipDetailJumat.isChecked = it.jumat == 1
                    chipDetailSabtu.isChecked = it.sabtu == 1
                    chipDetailMinggu.isChecked = it.minggu == 1
                }
            }
        })

        setupChipListeners()
    }


    private fun setupChipListeners() {
        val chips = listOf(
            binding.chipDetailSenin,
            binding.chipDetailSelasa,
            binding.chipDetailRabu,
            binding.chipDetailKamis,
            binding.chipDetailJumat,
            binding.chipDetailSabtu,
            binding.chipDetailMinggu
        )
        for (chip in chips) {
            chip.setOnCheckedChangeListener { _, _ ->
                updatePengulanganJadwalMakan()
            }
        }
    }

    private fun updatePengulanganJadwalMakan() {
        val chips = listOf(
            binding.chipDetailSenin,
            binding.chipDetailSelasa,
            binding.chipDetailRabu,
            binding.chipDetailKamis,
            binding.chipDetailJumat,
            binding.chipDetailSabtu,
            binding.chipDetailMinggu
        )
        when {
            chips.all { it.isChecked } -> {
                binding.detailPengulanganJadwalMakanACT.setText("Setiap Hari", false)
            }
            chips.any { it.isChecked } -> {
                binding.detailPengulanganJadwalMakanACT.setText("Custom", false)
            }
            else -> {
                binding.detailPengulanganJadwalMakanACT.setText("", false)
            }
        }
    }

    private fun selectAllDays(select: Boolean) {
        binding.chipDetailSenin.isChecked = select
        binding.chipDetailSelasa.isChecked = select
        binding.chipDetailRabu.isChecked = select
        binding.chipDetailKamis.isChecked = select
        binding.chipDetailJumat.isChecked = select
        binding.chipDetailSabtu.isChecked = select
        binding.chipDetailMinggu.isChecked = select
    }

    private fun clearAllDays() {
        selectAllDays(false)
    }

    private fun getDataDetailJadwalMakan(id_jadwal_makan: Int) {
        val call = RClient.api.getDataJadwalMakan(id_jadwal_makan)
        call?.enqueue(object : Callback<ResponseDataJadwalMakan> {
            override fun onResponse(
                call: Call<ResponseDataJadwalMakan>,
                response: Response<ResponseDataJadwalMakan>
            ) {
                Log.d("DetailJadwalMakanResponse", "onResponseDetail called")
                Log.d("DetailJadwalMakanResponse", "Response code: ${response.code()}")
                Log.d("DetailJadwalMakanResponse", "Response message: ${response.message()}")
                Log.d("DetailJadwalMakanResponse", "Response body: ${response.body()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.message == "Data Jadwal Ditemukan") {
                            listJadwalMakan.clear()
                            listJadwalMakan.addAll(it.data as List<JadwalMakanData>)
                            with(binding) {
                                val timeParts = listJadwalMakan[0].waktu_makan.split(":")
                                val hour24 = timeParts[0].toInt()
                                val minute = timeParts[1].toInt()
                                val isPM = hour24 >= 12
                                val hour12 = if (hour24 % 12 == 0) 12 else hour24 % 12

                                detailTimePicker.hour = hour12
                                detailTimePicker.minute = minute
                                detailTimePicker.setIs24HourView(false)
                                detailTimePicker.setHour(hour12)
                                detailTimePicker.setMinute(minute)


                                detailTipeJadwalMakanACT.setText(listJadwalMakan[0].tipe_jadwal_makan, false)
                                detailPengulanganJadwalMakanACT.setText(listJadwalMakan[0].pengulangan_jadwal_makan, false)

                                chipDetailSenin.isChecked = listJadwalMakan[0].senin == 1
                                chipDetailSelasa.isChecked = listJadwalMakan[0].selasa == 1
                                chipDetailRabu.isChecked = listJadwalMakan[0].rabu == 1
                                chipDetailKamis.isChecked = listJadwalMakan[0].kamis == 1
                                chipDetailJumat.isChecked = listJadwalMakan[0].jumat == 1
                                chipDetailSabtu.isChecked = listJadwalMakan[0].sabtu == 1
                                chipDetailMinggu.isChecked = listJadwalMakan[0].minggu == 1
                            }
                        }
                    }
                } else {
                    Log.d("DetailJadwalMakanResponse", "Response is not successful")
                }
            }

            override fun onFailure(call: Call<ResponseDataJadwalMakan>, t: Throwable) {
                Log.d("DetailJadwalMakanResponse", "API call failed",t)
            }
        })
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

    private fun editJadwalMakan(id_jadwal_makan: Int) {
        val hour = binding.detailTimePicker.hour
        val minute = binding.detailTimePicker.minute
        val isPM = hour >= 12

        val formattedTime = convertTo24HourFormat(hour % 12, minute, isPM)
        val status_jadwal = 1
        val waktuMakan = formattedTime
        val tipeJadwalMakan = binding.detailTipeJadwalMakanACT.text.toString()
        val pengulanganJadwalMakan = binding.detailPengulanganJadwalMakanACT.text.toString()
        val senin = if (binding.chipDetailSenin.isChecked) 1 else 0
        val selasa = if (binding.chipDetailSelasa.isChecked) 1 else 0
        val rabu = if (binding.chipDetailRabu.isChecked) 1 else 0
        val kamis = if (binding.chipDetailKamis.isChecked) 1 else 0
        val jumat = if (binding.chipDetailJumat.isChecked) 1 else 0
        val sabtu = if (binding.chipDetailSabtu.isChecked) 1 else 0
        val minggu = if (binding.chipDetailMinggu.isChecked) 1 else 0



        val call = RClient.api.updateJadwalMakan(
            status_jadwal,
            tipeJadwalMakan,
            pengulanganJadwalMakan,
            waktuMakan,
            senin, selasa, rabu, kamis, jumat, sabtu, minggu,
            id_jadwal_makan
            )
        call.enqueue(object : Callback<ResponseDataJadwalMakan> {
            override fun onResponse(call: Call<ResponseDataJadwalMakan>, response: Response<ResponseDataJadwalMakan>) {
                if (response.isSuccessful) {
                    FancyToast.makeText(
                        requireContext(),
                        "Berhasil Mengedit Jadwal",
                        FancyToast.LENGTH_LONG,
                        FancyToast.SUCCESS,
                        false)
                        .show()
                    viewModel.jadwalMakanDataDetail.value = viewModel.jadwalMakanDataDetail.value?.copy(
                        tipe_jadwal_makan = tipeJadwalMakan,
                        pengulangan_jadwal_makan = pengulanganJadwalMakan,
                        waktu_makan = waktuMakan,
                        senin = senin,
                        selasa = selasa,
                        rabu = rabu,
                        kamis = kamis,
                        jumat = jumat,
                        sabtu = sabtu,
                        minggu = minggu
                    )
                    parentFragmentManager.popBackStack()
                } else {
                    FancyToast.makeText(requireContext(), "Gagal memperbarui jadwal: ${response.message()}", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show()
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
                Log.e("DetailJadwalMakanResponse", "API call failed", t)
            }
        })
    }

    private fun hapusJadwalMakan(id_jadwal_makan: Int){
        val call = RClient.api.deleteJadwalMakan(id_jadwal_makan)

        call?.enqueue(object : Callback<ResponseDataJadwalMakan> {
            override fun onResponse(call: Call<ResponseDataJadwalMakan>, response: Response<ResponseDataJadwalMakan>) {
                if (response.isSuccessful) {
                    FancyToast.makeText(
                        requireContext(),
                        "Berhasil Delete Jadwal",
                        FancyToast.LENGTH_LONG,
                        FancyToast.SUCCESS,
                        false)
                        .show()
                    parentFragmentManager.popBackStack()
                } else {
                    FancyToast.makeText(requireContext(), "Gagal Delete Jadwal: ${response.message()}", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show()
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
                Log.e("DetailJadwalMakanResponse", "API call failed", t)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.myapplication.fragments

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import java.util.Calendar

class MutasiFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mutasi, container, false)

        val tabMutasi = view.findViewById<TextView>(R.id.tabMutasi)
        val tabStatement = view.findViewById<TextView>(R.id.tabStatement)
        val layoutMutasiTransaksiContainer = view.findViewById<View>(R.id.layoutMutasiTransaksiContainer)
        val layoutEStatementContainer = view.findViewById<View>(R.id.layoutEStatementContainer)
        val btnDateFilter = view.findViewById<TextView>(R.id.btnDateFilter)
        val btnCreateEStatement = view.findViewById<Button>(R.id.btnCreateEStatement)

        // Toggle Tab Listener: Mutasi Transaksi <-> e-Statement
        tabMutasi.setOnClickListener {
            layoutMutasiTransaksiContainer.visibility = View.VISIBLE
            layoutEStatementContainer.visibility = View.GONE

            tabMutasi.setBackgroundResource(R.drawable.bg_pill_button)
            tabMutasi.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.WHITE)
            tabMutasi.setTextColor(Color.parseColor("#1888E7"))
            tabMutasi.setTypeface(null, android.graphics.Typeface.BOLD)

            tabStatement.background = null
            tabStatement.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            tabStatement.setTypeface(null, android.graphics.Typeface.NORMAL)
        }

        tabStatement.setOnClickListener {
            layoutMutasiTransaksiContainer.visibility = View.GONE
            layoutEStatementContainer.visibility = View.VISIBLE

            tabStatement.setBackgroundResource(R.drawable.bg_pill_button)
            tabStatement.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.WHITE)
            tabStatement.setTextColor(Color.parseColor("#1888E7"))
            tabStatement.setTypeface(null, android.graphics.Typeface.BOLD)

            tabMutasi.background = null
            tabMutasi.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            tabMutasi.setTypeface(null, android.graphics.Typeface.NORMAL)
        }

        // Date Range Popup Filter
        btnDateFilter.setOnClickListener { v ->
            val popup = PopupMenu(requireContext(), v)
            popup.menu.add(0, 1, 0, "Hari Ini")
            popup.menu.add(0, 2, 0, "7 Hari Terakhir")
            popup.menu.add(0, 3, 0, "Pilih Bulan")
            popup.menu.add(0, 4, 0, "Pilih Tanggal")

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    1 -> {
                        btnDateFilter.text = "Hari Ini"
                        Toast.makeText(requireContext(), "Menampilkan Mutasi: Hari Ini", Toast.LENGTH_SHORT).show()
                        true
                    }
                    2 -> {
                        btnDateFilter.text = "7 Hari Terakhir"
                        Toast.makeText(requireContext(), "Menampilkan Mutasi: 7 Hari Terakhir", Toast.LENGTH_SHORT).show()
                        true
                    }
                    3 -> {
                        btnDateFilter.text = "Pilih Bulan"
                        Toast.makeText(requireContext(), "Fitur Pilih Bulan (Dialog Bulan)", Toast.LENGTH_SHORT).show()
                        true
                    }
                    4 -> {
                        val calendar = Calendar.getInstance()
                        DatePickerDialog(
                            requireContext(),
                            { _, year, month, dayOfMonth ->
                                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                                btnDateFilter.text = selectedDate
                                Toast.makeText(requireContext(), "Mutasi tanggal: $selectedDate", Toast.LENGTH_SHORT).show()
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

        // Create e-Statement Button Action
        btnCreateEStatement.setOnClickListener {
            Toast.makeText(requireContext(), "Membuka formulir pembuatan e-Statement baru", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}

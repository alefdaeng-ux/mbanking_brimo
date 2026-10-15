package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.R

class AktivitasFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_aktivitas, container, false)

        val headerAktivitas = view.findViewById<View>(R.id.headerAktivitas)
        val btnFilterAktivitas = view.findViewById<ImageView>(R.id.btnFilterAktivitas)

        ViewCompat.setOnApplyWindowInsetsListener(headerAktivitas) { v, insets ->
            val statusBarInset = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val density = resources.displayMetrics.density
            val safeTopPadding = if (statusBarInset.top > 0) statusBarInset.top + (6 * density).toInt() else (40 * density).toInt()
            val lp = v.layoutParams
            lp.height = (48 * density).toInt() + safeTopPadding
            v.layoutParams = lp
            v.setPadding(
                v.paddingLeft,
                safeTopPadding,
                v.paddingRight,
                (12 * density).toInt()
            )
            insets
        }

        btnFilterAktivitas.setOnClickListener { v ->
            val popup = PopupMenu(requireContext(), v)
            popup.menu.add(0, 1, 0, "Semua Aktivitas")
            popup.menu.add(0, 2, 0, "Pembelian QRIS")
            popup.menu.add(0, 3, 0, "Transfer")
            popup.menu.add(0, 4, 0, "Pembelian & Tagihan")

            popup.setOnMenuItemClickListener { item ->
                Toast.makeText(requireContext(), "Filter: ${item.title}", Toast.LENGTH_SHORT).show()
                true
            }
            popup.show()
        }

        return view
    }
}

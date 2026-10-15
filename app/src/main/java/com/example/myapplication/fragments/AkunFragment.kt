package com.example.myapplication.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.LoginActivity
import com.example.myapplication.R
import com.example.myapplication.SharedPrefManager

class AkunFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_akun, container, false)

        val tvAkunTitle = view.findViewById<TextView>(R.id.tvAkunTitle)
        val headerAkunBg = view.findViewById<View>(R.id.headerAkunBg)
        val tvAccountName = view.findViewById<TextView>(R.id.tvAccountName)
        val tvAvatarInitials = view.findViewById<TextView>(R.id.tvAvatarInitials)

        // Status bar WindowInsets padding
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            val statusBarInset = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val density = resources.displayMetrics.density
            val safeTopMargin = if (statusBarInset.top > 0) statusBarInset.top + (10 * density).toInt() else (36 * density).toInt()

            if (tvAkunTitle != null) {
                val titleParams = tvAkunTitle.layoutParams as android.widget.RelativeLayout.LayoutParams
                titleParams.topMargin = safeTopMargin
                tvAkunTitle.layoutParams = titleParams
            }

            if (headerAkunBg != null) {
                val bgParams = headerAkunBg.layoutParams as android.widget.RelativeLayout.LayoutParams
                bgParams.height = (130 * density).toInt() + safeTopMargin
                headerAkunBg.layoutParams = bgParams
            }

            insets
        }

        // Retrieve user name from activity intent or default
        val userName = activity?.intent?.getStringExtra("USER_NAME") ?: "ARIL PRATAMA"
        tvAccountName?.text = userName.uppercase()

        // Calculate initials (e.g. ARIL PRATAMA -> AP, Moh Nur -> MN)
        val parts = userName.trim().split("\\s+".toRegex())
        val initials = when {
            parts.size >= 2 -> "${parts[0].first()}${parts[1].first()}".uppercase()
            parts.isNotEmpty() && parts[0].isNotEmpty() -> parts[0].take(2).uppercase()
            else -> "AP"
        }
        tvAvatarInitials?.text = initials

        // Click listeners for Profile & Informasi Pribadi
        view.findViewById<View>(R.id.cardUserProfile)?.setOnClickListener {
            showToast("Membuka Detail Profil $userName")
        }
        view.findViewById<View>(R.id.btnDataDiri)?.setOnClickListener {
            showToast("Membuka Data Diri & Pekerjaan")
        }

        // Pengaturan
        view.findViewById<View>(R.id.btnFastMenuSetting)?.setOnClickListener {
            showToast("Membuka Pengaturan Fast Menu")
        }
        view.findViewById<View>(R.id.btnUpdateRekening)?.setOnClickListener {
            showToast("Membuka Halaman Update Rekening")
        }
        view.findViewById<View>(R.id.btnCardManagement)?.setOnClickListener {
            showToast("Membuka Pengelolaan Kartu Debit")
        }
        view.findViewById<View>(R.id.btnQrisFund)?.setOnClickListener {
            showToast("Membuka Pengaturan Sumber Dana QRIS")
        }
        view.findViewById<View>(R.id.btnLanguage)?.setOnClickListener {
            showToast("Pengaturan Bahasa / Language")
        }

        // Keamanan
        view.findViewById<View>(R.id.btnChangePin)?.setOnClickListener {
            showToast("Membuka Halaman Ubah PIN")
        }
        view.findViewById<View>(R.id.btnChangePassword)?.setOnClickListener {
            showToast("Membuka Halaman Ubah Password")
        }
        val switchFingerprint = view.findViewById<SwitchCompat>(R.id.switchFingerprint)
        if (switchFingerprint != null) {
            switchFingerprint.isChecked = SharedPrefManager.isFingerprintEnabled(requireContext())
            switchFingerprint.setOnCheckedChangeListener { _, isChecked ->
                SharedPrefManager.setFingerprintEnabled(requireContext(), isChecked)
                if (isChecked) {
                    showToast("Login Fingerprint Diaktifkan")
                } else {
                    showToast("Login Fingerprint Dinonaktifkan")
                }
            }
        }

        // Contact BRI
        view.findViewById<View>(R.id.btnHelpCenter)?.setOnClickListener {
            showToast("Membuka Pusat Bantuan BRI")
        }
        view.findViewById<View>(R.id.btnChatBanking)?.setOnClickListener {
            showToast("Membuka Layanan Chat Banking BRI")
        }
        view.findViewById<View>(R.id.btnTollFree)?.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:14017")
            }
            startActivity(intent)
        }
        view.findViewById<View>(R.id.btnContactUs)?.setOnClickListener {
            showToast("Membuka Kontak Kami BRI")
        }

        // Informasi (6 Item)
        view.findViewById<View>(R.id.btnTxLimit)?.setOnClickListener {
            showToast("Membuka Jenis & Limit Transaksi")
        }
        view.findViewById<View>(R.id.btnInfoKurs)?.setOnClickListener {
            showToast("Membuka Info Kurs Mata Uang")
        }
        view.findViewById<View>(R.id.btnInfoSaham)?.setOnClickListener {
            showToast("Membuka Info Saham BRI")
        }
        view.findViewById<View>(R.id.btnAtmLocation)?.setOnClickListener {
            showToast("Membuka Peta Lokasi ATM BRI")
        }
        view.findViewById<View>(R.id.btnOfficeLocation)?.setOnClickListener {
            showToast("Membuka Peta Lokasi Kantor BRI")
        }
        view.findViewById<View>(R.id.btnAboutBrimo)?.setOnClickListener {
            showToast("BRImo versi 2.100.0 - Aplikasi Perbankan Digital BRI")
        }

        // Kotak Putih Logout
        view.findViewById<View>(R.id.btnLogoutAccount)?.setOnClickListener {
            showToast("Berhasil Keluar dari Akun")
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            activity?.finish()
        }

        return view
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.fragments.AkunFragment
import com.example.myapplication.fragments.AktivitasFragment
import com.example.myapplication.fragments.MutasiFragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

class DashboardActivity : AppCompatActivity() {

    private var isBalanceVisible = false
    private val actualBalance = "Rp 1.500.000"
    private val maskedBalance = "● ● ● ● ● ● ●"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val layoutBottomNavContainer = findViewById<View>(R.id.layoutBottomNavContainer)
        val btnQrisCenter = findViewById<View>(R.id.btnQrisCenter)

        ViewCompat.setOnApplyWindowInsetsListener(layoutBottomNavContainer) { view, insets ->
            val navBarInset = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            val density = resources.displayMetrics.density

            // Solid white background reaches bottom edge
            view.setPadding(
                view.paddingLeft,
                view.paddingTop,
                view.paddingRight,
                navBarInset.bottom
            )
            val lp = view.layoutParams as android.widget.RelativeLayout.LayoutParams
            lp.height = (68 * density).toInt() + navBarInset.bottom
            view.layoutParams = lp

            // Position QRIS button 50% protruding above navbar
            val qrisParams = btnQrisCenter.layoutParams as android.widget.RelativeLayout.LayoutParams
            qrisParams.bottomMargin = navBarInset.bottom + (38 * density).toInt()
            btnQrisCenter.layoutParams = qrisParams

            insets
        }

        val stickyTopBar = findViewById<View>(R.id.stickyTopBar)
        ViewCompat.setOnApplyWindowInsetsListener(stickyTopBar) { view, insets ->
            val statusBarInset = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val density = resources.displayMetrics.density
            val safeTopPadding = if (statusBarInset.top > 0) statusBarInset.top + (6 * density).toInt() else (38 * density).toInt()
            view.setPadding(
                view.paddingLeft,
                safeTopPadding,
                view.paddingRight,
                (10 * density).toInt()
            )
            insets
        }

        val tvWelcomeTime = findViewById<TextView>(R.id.tvWelcomeTime)
        val tvUserName = findViewById<TextView>(R.id.tvUserName)
        val tvBalance = findViewById<TextView>(R.id.tvBalance)
        val ivToggleBalance = findViewById<ImageView>(R.id.ivToggleBalance)

        val btnHelp = findViewById<ImageView>(R.id.btnHelp)
        val btnNotification = findViewById<View>(R.id.btnNotification)
        val btnLogoutHeader = findViewById<ImageView>(R.id.btnLogoutHeader)

        // Banner Carousel Scroll Listener
        val hsvBanner = findViewById<HorizontalScrollView>(R.id.hsvBanner)
        val dot1 = findViewById<View>(R.id.dot1)
        val dot2 = findViewById<View>(R.id.dot2)
        val dot3 = findViewById<View>(R.id.dot3)

        hsvBanner.setOnScrollChangeListener { _, scrollX, _, _, _ ->
            val child = hsvBanner.getChildAt(0)
            if (child != null) {
                val maxScroll = child.width - hsvBanner.width
                if (maxScroll > 0) {
                    val progress = scrollX.toFloat() / maxScroll
                    when {
                        progress < 0.33f -> updateDots(active = dot1, inactive1 = dot2, inactive2 = dot3)
                        progress < 0.66f -> updateDots(active = dot2, inactive1 = dot1, inactive2 = dot3)
                        else -> updateDots(active = dot3, inactive1 = dot1, inactive2 = dot2)
                    }
                }
            }
        }

        // Fitur Utama Atas
        val btnTopTransfer = findViewById<LinearLayout>(R.id.btnTopTransfer)
        val btnTopBriva = findViewById<LinearLayout>(R.id.btnTopBriva)
        val btnTopGold = findViewById<LinearLayout>(R.id.btnTopGold)
        val btnTopPulsa = findViewById<LinearLayout>(R.id.btnTopPulsa)

        // Fitur Tambahan Grid Expandable Container
        val layoutFeatureContainer = findViewById<android.view.ViewGroup>(R.id.layoutFeatureContainer)
        val layoutExtraRows = findViewById<View>(R.id.layoutExtraRows)
        val btnToggleLainnya = findViewById<View>(R.id.btnToggleLainnya)
        val tvLainnyaText = findViewById<TextView>(R.id.tvLainnyaText)
        val ivLainnyaIcon = findViewById<ImageView>(R.id.ivLainnyaIcon)

        val btnTopUp = findViewById<LinearLayout>(R.id.btnTopUp)
        val btnBrizzi = findViewById<LinearLayout>(R.id.btnBrizzi)
        val btnTagihan = findViewById<LinearLayout>(R.id.btnTagihan)
        val btnDonasi = findViewById<LinearLayout>(R.id.btnDonasi)
        val btnVirtualDebit = findViewById<LinearLayout>(R.id.btnVirtualDebit)
        val btnInvestment = findViewById<LinearLayout>(R.id.btnInvestment)
        val btnInvestasi = findViewById<LinearLayout>(R.id.btnInvestasi)
        val btnCashDeposit = findViewById<LinearLayout>(R.id.btnCashDeposit)
        val btnLifestyle = findViewById<LinearLayout>(R.id.btnLifestyle)

        val btnForex = findViewById<LinearLayout>(R.id.btnForex)
        val btnCreditCard = findViewById<LinearLayout>(R.id.btnCreditCard)
        val btnInsurance = findViewById<LinearLayout>(R.id.btnInsurance)
        val btnLoan = findViewById<LinearLayout>(R.id.btnLoan)
        val btnBriProducts = findViewById<LinearLayout>(R.id.btnBriProducts)

        // Elemen Seksi A, B & Qita
        val btnQitaBanner = findViewById<View>(R.id.btnQitaBanner)
        val btnScheduleDonation = findViewById<View>(R.id.btnScheduleDonation)
        val tvSeeAllPromos = findViewById<TextView>(R.id.tvSeeAllPromos)

        // Bottom Nav & Floating QRIS
        val navHome = findViewById<LinearLayout>(R.id.navHome)
        val navMutasi = findViewById<LinearLayout>(R.id.navMutasi)
        val navActivity = findViewById<LinearLayout>(R.id.navActivity)
        val navAccount = findViewById<LinearLayout>(R.id.navAccount)

        // 1. Set Sapaan Waktu Otomatis
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greeting = when (hour) {
            in 4..11 -> "Selamat Pagi,"
            in 12..14 -> "Selamat Siang,"
            in 15..18 -> "Selamat Sore,"
            else -> "Selamat Malam,"
        }
        tvWelcomeTime.text = greeting

        // 2. Set Nama User dari Intent / SharedPreferences
        val userName = intent.getStringExtra("USER_NAME") ?: SharedPrefManager.getUserName(this)
        tvUserName.text = userName

        // 3. Sensor / Tampilkan Saldo Toggle via SharedPreferences
        isBalanceVisible = SharedPrefManager.isBalanceVisible(this)
        updateBalanceDisplay()

        ivToggleBalance.setOnClickListener {
            isBalanceVisible = !isBalanceVisible
            SharedPrefManager.setBalanceVisible(this, isBalanceVisible)
            updateBalanceDisplay()
        }

        // 4. Action Klik Fitur Utama Atas (Dinamis SharedPreferences)
        btnTopTransfer.setOnClickListener {
            if (SharedPrefManager.subtractSaldo(this, 100000L)) {
                updateBalanceDisplay()
                val newFormatted = SharedPrefManager.getFormattedSaldo(this)
                showToast("Transfer Berhasil Rp 100.000! Saldo Baru: $newFormatted")
            } else {
                showToast("Saldo tidak cukup!")
            }
        }
        btnTopBriva.setOnClickListener { showToast("Fitur BRIVA terpilih") }
        btnTopGold.setOnClickListener { showToast("Fitur Investasi Emas terpilih") }
        btnTopPulsa.setOnClickListener {
            if (SharedPrefManager.subtractSaldo(this, 50000L)) {
                updateBalanceDisplay()
                val newFormatted = SharedPrefManager.getFormattedSaldo(this)
                showToast("Pembelian Pulsa Berhasil Rp 50.000! Saldo Baru: $newFormatted")
            } else {
                showToast("Saldo tidak cukup!")
            }
        }

        // 5. Action Klik Fitur Tambahan Baru & Toggle Expandable Grid
        var isFeaturesExpanded = false

        btnToggleLainnya?.setOnClickListener {
            if (layoutFeatureContainer != null) {
                android.transition.TransitionManager.beginDelayedTransition(
                    layoutFeatureContainer,
                    android.transition.AutoTransition()
                )
            }
            isFeaturesExpanded = !isFeaturesExpanded

            if (isFeaturesExpanded) {
                layoutExtraRows?.visibility = View.VISIBLE
                tvLainnyaText?.text = "Tutup"
                ivLainnyaIcon?.setImageResource(R.drawable.ic_chevron_up)
            } else {
                layoutExtraRows?.visibility = View.GONE
                tvLainnyaText?.text = "Lainnya"
                ivLainnyaIcon?.setImageResource(R.drawable.ic_chevron_down)
            }
        }

        // Top Up Custom Dialog (Bebas Masukkan Nominal Top Up Berapapun)
        btnTopUp?.setOnClickListener {
            showTopUpDialog()
        }
        btnBrizzi?.setOnClickListener { showToast("Fitur BRIZZI terpilih") }
        btnTagihan?.setOnClickListener { showToast("Fitur Pembayaran Tagihan") }
        btnDonasi?.setOnClickListener { showToast("Fitur Donasi & Zakat terpilih") }
        btnVirtualDebit?.setOnClickListener { showToast("Fitur Virtual Debit Card") }
        btnInvestment?.setOnClickListener { showToast("Fitur Catatan Keuangan terpilih") }
        btnInvestasi?.setOnClickListener { showToast("Fitur Investasi terpilih") }

        // Setor Tunai
        btnCashDeposit?.setOnClickListener {
            showTopUpDialog()
        }
        btnLifestyle?.setOnClickListener { showToast("Fitur Lifestyle & Belanja") }

        btnForex?.setOnClickListener { showToast("Fitur Konversi Valas terpilih") }
        btnCreditCard?.setOnClickListener { showToast("Fitur Kartu Kredit terpilih") }
        btnInsurance?.setOnClickListener { showToast("Fitur Asuransi BRImo terpilih") }
        btnLoan?.setOnClickListener { showToast("Fitur Pinjaman BRImo terpilih") }
        btnBriProducts?.setOnClickListener {
            val intent = Intent(this, TaskMenuActivity::class.java)
            startActivity(intent)
        }

        // Action Seksi Baru (Qita, Donasi & Promo)
        btnQitaBanner.setOnClickListener { showToast("Membuka Layanan Qita by BRI") }
        btnScheduleDonation.setOnClickListener { showToast("Membuka Jadwal Donasi Rutin") }
        tvSeeAllPromos.setOnClickListener { showToast("Menampilkan Halaman Semua Promo") }

        // 6. Action Top Header
        btnNotification.setOnClickListener { showToast("Menampilkan 9+ Notifikasi Terbaru") }
        btnHelp.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:14017")
            }
            startActivity(intent)
        }
        btnLogoutHeader.setOnClickListener {
            showToast("Berhasil Keluar dari M-Banking")
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        // 7. Floating QRIS & Bottom Nav
        btnQrisCenter.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivity(cameraIntent)
        }
        val scrollViewHome = findViewById<ScrollView>(R.id.scrollViewHome)
        val fragmentContainer = findViewById<View>(R.id.fragmentContainer)

        navHome.setOnClickListener {
            scrollViewHome.visibility = View.VISIBLE
            fragmentContainer.visibility = View.GONE
            updateActiveTab(0)
        }
        navMutasi.setOnClickListener {
            scrollViewHome.visibility = View.GONE
            fragmentContainer.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MutasiFragment())
                .commit()
            updateActiveTab(1)
        }
        navActivity.setOnClickListener {
            scrollViewHome.visibility = View.GONE
            fragmentContainer.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AktivitasFragment())
                .commit()
            updateActiveTab(2)
        }
        navAccount.setOnClickListener {
            scrollViewHome.visibility = View.GONE
            fragmentContainer.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AkunFragment())
                .commit()
            updateActiveTab(3)
        }
    }

    override fun onResume() {
        super.onResume()
        updateBalanceDisplay()
    }

    private fun updateBalanceDisplay() {
        val tvBalance = findViewById<TextView>(R.id.tvBalance)
        val ivToggleBalance = findViewById<ImageView>(R.id.ivToggleBalance)
        val formattedSaldo = SharedPrefManager.getFormattedSaldo(this)

        if (isBalanceVisible) {
            tvBalance?.text = formattedSaldo
            ivToggleBalance?.setImageResource(R.drawable.ic_eye_white)
        } else {
            tvBalance?.text = "● ● ● ● ● ● ●"
            ivToggleBalance?.setImageResource(R.drawable.ic_eye_off_white)
        }
    }

    private fun showTopUpDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_topup, null)
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setView(dialogView)
        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val etAmount = dialogView.findViewById<EditText>(R.id.etTopUpAmount)
        val btn100k = dialogView.findViewById<Button>(R.id.btnChip100k)
        val btn500k = dialogView.findViewById<Button>(R.id.btnChip500k)
        val btn1m = dialogView.findViewById<Button>(R.id.btnChip1m)
        val btn2m = dialogView.findViewById<Button>(R.id.btnChip2m)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btnConfirmTopUp)
        val btnCancel = dialogView.findViewById<TextView>(R.id.btnCancelTopUp)

        btn100k?.setOnClickListener { etAmount?.setText("100000") }
        btn500k?.setOnClickListener { etAmount?.setText("500000") }
        btn1m?.setOnClickListener { etAmount?.setText("1000000") }
        btn2m?.setOnClickListener { etAmount?.setText("2000000") }

        btnCancel?.setOnClickListener { alertDialog.dismiss() }

        btnConfirm?.setOnClickListener {
            val inputStr = etAmount?.text.toString().trim()
            val amount = inputStr.toLongOrNull()

            if (amount != null && amount > 0) {
                SharedPrefManager.addSaldo(this, amount)
                updateBalanceDisplay()
                val formattedNew = SharedPrefManager.getFormattedSaldo(this)
                val formattedAmount = String.format("Rp %,d", amount).replace(',', '.')
                showToast("Top Up $formattedAmount Berhasil! Saldo Baru: $formattedNew")
                alertDialog.dismiss()
            } else {
                Toast.makeText(this, "Masukkan nominal Top Up yang valid!", Toast.LENGTH_SHORT).show()
            }
        }

        alertDialog.show()
    }

    private fun updateActiveTab(selectedTab: Int) {
        val activeColor = Color.parseColor("#00529C")
        val inactiveColor = Color.parseColor("#757575")

        val ivNavHome = findViewById<ImageView>(R.id.ivNavHome)
        val tvNavHome = findViewById<TextView>(R.id.tvNavHome)
        val ivNavMutasi = findViewById<ImageView>(R.id.ivNavMutasi)
        val tvNavMutasi = findViewById<TextView>(R.id.tvNavMutasi)
        val ivNavActivity = findViewById<ImageView>(R.id.ivNavActivity)
        val tvNavActivity = findViewById<TextView>(R.id.tvNavActivity)
        val ivNavAccount = findViewById<ImageView>(R.id.ivNavAccount)
        val tvNavAccount = findViewById<TextView>(R.id.tvNavAccount)

        val navHome = findViewById<View>(R.id.navHome)
        val navMutasi = findViewById<View>(R.id.navMutasi)
        val navActivity = findViewById<View>(R.id.navActivity)
        val navAccount = findViewById<View>(R.id.navAccount)

        // Reset all tabs
        ivNavHome.imageTintList = android.content.res.ColorStateList.valueOf(inactiveColor)
        tvNavHome.setTextColor(inactiveColor)
        tvNavHome.setTypeface(null, android.graphics.Typeface.NORMAL)

        ivNavMutasi.imageTintList = android.content.res.ColorStateList.valueOf(inactiveColor)
        tvNavMutasi.setTextColor(inactiveColor)
        tvNavMutasi.setTypeface(null, android.graphics.Typeface.NORMAL)

        ivNavActivity.imageTintList = android.content.res.ColorStateList.valueOf(inactiveColor)
        tvNavActivity.setTextColor(inactiveColor)
        tvNavActivity.setTypeface(null, android.graphics.Typeface.NORMAL)

        ivNavAccount.imageTintList = android.content.res.ColorStateList.valueOf(inactiveColor)
        tvNavAccount.setTextColor(inactiveColor)
        tvNavAccount.setTypeface(null, android.graphics.Typeface.NORMAL)

        // Highlight selected tab with blue color + bold + bounce animation
        when (selectedTab) {
            0 -> {
                ivNavHome.imageTintList = android.content.res.ColorStateList.valueOf(activeColor)
                tvNavHome.setTextColor(activeColor)
                tvNavHome.setTypeface(null, android.graphics.Typeface.BOLD)
                animateTab(navHome)
            }
            1 -> {
                ivNavMutasi.imageTintList = android.content.res.ColorStateList.valueOf(activeColor)
                tvNavMutasi.setTextColor(activeColor)
                tvNavMutasi.setTypeface(null, android.graphics.Typeface.BOLD)
                animateTab(navMutasi)
            }
            2 -> {
                ivNavActivity.imageTintList = android.content.res.ColorStateList.valueOf(activeColor)
                tvNavActivity.setTextColor(activeColor)
                tvNavActivity.setTypeface(null, android.graphics.Typeface.BOLD)
                animateTab(navActivity)
            }
            3 -> {
                ivNavAccount.imageTintList = android.content.res.ColorStateList.valueOf(activeColor)
                tvNavAccount.setTextColor(activeColor)
                tvNavAccount.setTypeface(null, android.graphics.Typeface.BOLD)
                animateTab(navAccount)
            }
        }
    }

    private fun animateTab(view: View) {
        view.animate().scaleX(1.08f).scaleY(1.08f).setDuration(90).withEndAction {
            view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(90).start()
        }.start()
    }

    private fun updateDots(active: View, inactive1: View, inactive2: View) {
        val density = resources.displayMetrics.density
        active.layoutParams.width = (16 * density).toInt()
        active.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#00529C"))

        inactive1.layoutParams.width = (6 * density).toInt()
        inactive1.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#CBD5E1"))

        inactive2.layoutParams.width = (6 * density).toInt()
        inactive2.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#CBD5E1"))

        active.requestLayout()
        inactive1.requestLayout()
        inactive2.requestLayout()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
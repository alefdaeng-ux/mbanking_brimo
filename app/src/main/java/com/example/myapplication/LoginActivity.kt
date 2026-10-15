package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import com.example.myapplication.api.ApiResponse
import com.example.myapplication.api.RetrofitClient
import com.example.myapplication.api.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetDialog

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val layoutBottomSection = findViewById<View>(R.id.layoutBottomSection)
        ViewCompat.setOnApplyWindowInsetsListener(layoutBottomSection) { view, insets ->
            val navBarInset = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.setPadding(
                view.paddingLeft,
                view.paddingTop,
                view.paddingRight,
                navBarInset.bottom + (16 * resources.displayMetrics.density).toInt()
            )
            insets
        }

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnFingerprint = findViewById<View>(R.id.btnFingerprint)
        val hsvFastMenu = findViewById<HorizontalScrollView>(R.id.hsvFastMenu)
        val indicatorThumb = findViewById<View>(R.id.indicatorThumb)

        hsvFastMenu.setOnScrollChangeListener { _, scrollX, _, _, _ ->
            val child = hsvFastMenu.getChildAt(0)
            if (child != null) {
                val maxScroll = child.width - hsvFastMenu.width
                if (maxScroll > 0) {
                    val progress = (scrollX.toFloat() / maxScroll).coerceIn(0f, 1f)
                    val density = resources.displayMetrics.density
                    val maxThumbTranslation = 36 * density
                    indicatorThumb.translationX = progress * maxThumbTranslation
                }
            }
        }

        btnLogin.setOnClickListener { v ->
            v.animate().scaleX(0.96f).scaleY(0.96f).setDuration(80).withEndAction {
                v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(80).start()
                showLoginBottomSheet()
            }.start()
        }

        btnFingerprint.setOnClickListener { v ->
            v.animate().scaleX(0.92f).scaleY(0.92f).setDuration(80).withEndAction {
                v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(80).start()
                if (SharedPrefManager.isFingerprintEnabled(this)) {
                    val savedName = SharedPrefManager.getUserName(this)
                    Toast.makeText(this, "Autentikasi Sidik Jari Berhasil!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, DashboardActivity::class.java)
                    intent.putExtra("USER_NAME", savedName)
                    startActivity(intent)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                } else {
                    Toast.makeText(this, "Login Fingerprint dinonaktifkan di Pengaturan Akun!", Toast.LENGTH_SHORT).show()
                }
            }.start()
        }
    }

    private fun showLoginBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_login, null)
        bottomSheetDialog.setContentView(view)

        val tvCancel = view.findViewById<TextView>(R.id.tvCancel)
        val tvRegisterPrompt = view.findViewById<TextView>(R.id.tvRegisterPrompt)
        val etUserId = view.findViewById<EditText>(R.id.etUserId)
        val etMpin = view.findViewById<EditText>(R.id.etMpin)
        val ivTogglePassword = view.findViewById<ImageView>(R.id.ivTogglePassword)
        val btnSubmitLogin = view.findViewById<Button>(R.id.btnSubmitLogin)
        val tvForgotPassword = view.findViewById<TextView>(R.id.tvForgotPassword)

        // Underline effect for "Belum punya akun?"
        if (tvRegisterPrompt != null) {
            tvRegisterPrompt.paintFlags = tvRegisterPrompt.paintFlags or android.graphics.Paint.UNDERLINE_TEXT_FLAG
            tvRegisterPrompt.setOnClickListener {
                bottomSheetDialog.dismiss()
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }

        // Initial state: Light grayish white background with gray text when empty
        btnSubmitLogin.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#E5E7EB"))
        btnSubmitLogin.setTextColor(Color.parseColor("#9CA3AF"))

        val textWatcher = object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val hasUser = etUserId.text.toString().isNotEmpty()
                val hasPass = etMpin.text.toString().isNotEmpty()

                if (hasUser && hasPass) {
                    btnSubmitLogin.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#00529C"))
                    btnSubmitLogin.setTextColor(Color.WHITE)
                } else {
                    btnSubmitLogin.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#E5E7EB"))
                    btnSubmitLogin.setTextColor(Color.parseColor("#9CA3AF"))
                }
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        }

        etUserId.addTextChangedListener(textWatcher)
        etMpin.addTextChangedListener(textWatcher)

        var isPasswordVisible = false

        ivTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                etMpin.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                ivTogglePassword.setImageResource(R.drawable.ic_eye_off)
            } else {
                etMpin.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                ivTogglePassword.setImageResource(R.drawable.ic_eye)
            }
            etMpin.setSelection(etMpin.text.length)
        }

        tvCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Membuka halaman pemulihan akun", Toast.LENGTH_SHORT).show()
        }

        btnSubmitLogin.setOnClickListener { v ->
            v.animate().scaleX(0.96f).scaleY(0.96f).setDuration(80).withEndAction {
                v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(80).start()
                val userId = etUserId.text.toString().trim()
                val password = etMpin.text.toString().trim()

                if (userId.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Username dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                    return@withEndAction
                }

                Toast.makeText(this, "Memverifikasi login ke Database MySQL...", Toast.LENGTH_SHORT).show()

                // Verifikasi Kredensial Langsung ke Database XAMPP MySQL (Read)
                RetrofitClient.instance.loginUser(userId, password)
                    .enqueue(object : retrofit2.Callback<ApiResponse<UserData>> {
                        override fun onResponse(
                            call: retrofit2.Call<ApiResponse<UserData>>,
                            response: retrofit2.Response<ApiResponse<UserData>>
                        ) {
                            val apiResp = response.body()
                            if (response.isSuccessful && apiResp?.success == true) {
                                // LOGIN MYSQL BERHASIL
                                val nameFromDb = apiResp.data?.username ?: userId
                                SharedPrefManager.saveUserData(this@LoginActivity, nameFromDb, userId)

                                Toast.makeText(this@LoginActivity, "Login Database MySQL Berhasil!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                                intent.putExtra("USER_NAME", nameFromDb)
                                startActivity(intent)
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                finish()
                                bottomSheetDialog.dismiss()
                            } else {
                                // LOGIN MYSQL GAGAL (Kredensial Salah)
                                val errorMsg = apiResp?.message ?: "Username atau password salah!"
                                Toast.makeText(this@LoginActivity, "Gagal Login: $errorMsg", Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<ApiResponse<UserData>>, t: Throwable) {
                            // Server Offline / Tidak Terhubung
                            val savedUser = SharedPrefManager.getUsername(this@LoginActivity)
                            if (userId.equals(savedUser, ignoreCase = true)) {
                                Toast.makeText(this@LoginActivity, "Login Offline Berhasil (Server XAMPP Unreachable)", Toast.LENGTH_SHORT).show()
                                val savedName = SharedPrefManager.getUserName(this@LoginActivity)
                                val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                                intent.putExtra("USER_NAME", savedName)
                                startActivity(intent)
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                finish()
                                bottomSheetDialog.dismiss()
                            } else {
                                Toast.makeText(this@LoginActivity, "Gagal Login: Server MySQL XAMPP Offline atau Kredensial Salah!", Toast.LENGTH_LONG).show()
                            }
                        }
                    })
            }.start()
        }

        bottomSheetDialog.show()
    }
}

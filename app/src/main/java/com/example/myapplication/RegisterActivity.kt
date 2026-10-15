package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import com.example.myapplication.api.ApiResponse
import com.example.myapplication.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private var isPasswordVisible = false

    private lateinit var etNik: EditText
    private lateinit var etRegName: EditText
    private lateinit var etRegPhone: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPin: EditText
    private lateinit var btnSubmitRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnUploadKtp = findViewById<Button>(R.id.btnUploadKtp)

        etNik = findViewById(R.id.etNik)
        etRegName = findViewById(R.id.etRegName)
        etRegPhone = findViewById(R.id.etRegPhone)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        val ivTogglePassword = findViewById<ImageView>(R.id.ivTogglePassword)
        etPin = findViewById(R.id.etPin)

        btnSubmitRegister = findViewById(R.id.btnSubmitRegister)

        // TextWatcher untuk memantau semua input
        val registerTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkRegisterInputs()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        etNik.addTextChangedListener(registerTextWatcher)
        etRegName.addTextChangedListener(registerTextWatcher)
        etRegPhone.addTextChangedListener(registerTextWatcher)
        etUsername.addTextChangedListener(registerTextWatcher)
        etPassword.addTextChangedListener(registerTextWatcher)
        etPin.addTextChangedListener(registerTextWatcher)

        // Tombol Kembali
        btnBack.setOnClickListener {
            finish()
        }

        // Tombol Upload KTP dengan Animasi & Pemicu Membuka Galeri
        btnUploadKtp.setOnClickListener { v ->
            v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(80).withEndAction {
                v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(80).start()
                Toast.makeText(this, "Membuka Galeri Foto untuk Upload e-KTP...", Toast.LENGTH_SHORT).show()
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                try {
                    startActivity(galleryIntent)
                } catch (e: Exception) {
                    val fallbackIntent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
                    startActivity(fallbackIntent)
                }
            }.start()
        }

        // Toggle Password Visibility
        ivTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                ivTogglePassword.setImageResource(R.drawable.ic_eye)
            } else {
                etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                ivTogglePassword.setImageResource(R.drawable.ic_eye_off)
            }
            etPassword.setSelection(etPassword.text.length)
        }

        // Tombol Buat Rekening Sekarang (Bentuk Kotak & Animasi Membal)
        btnSubmitRegister.setOnClickListener { v ->
            v.animate().scaleX(0.96f).scaleY(0.96f).setDuration(80).withEndAction {
                v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(80).start()

                val nik = etNik.text.toString().trim()
                val name = etRegName.text.toString().trim()
                val phone = etRegPhone.text.toString().trim()
                val username = etUsername.text.toString().trim()
                val password = etPassword.text.toString().trim()
                val pin = etPin.text.toString().trim()

                // Validasi Data
                if (nik.length < 16) {
                    Toast.makeText(this, "NIK harus 16 angka!", Toast.LENGTH_SHORT).show()
                    return@withEndAction
                }
                if (name.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Harap isi semua data formulir!", Toast.LENGTH_SHORT).show()
                    return@withEndAction
                }
                if (pin.length < 6) {
                    Toast.makeText(this, "PIN harus 6 angka!", Toast.LENGTH_SHORT).show()
                    return@withEndAction
                }

                // Simpan Data User & Saldo Awal di SharedPreferences
                SharedPrefManager.saveUserData(this, name, username, nik, phone)
                SharedPrefManager.setSaldo(this, 1500000L)

                // Kirim Data Registrasi ke XAMPP MySQL REST API
                RetrofitClient.instance.registerUser(username, password, pin)
                    .enqueue(object : retrofit2.Callback<ApiResponse<Any>> {
                        override fun onResponse(
                            call: retrofit2.Call<ApiResponse<Any>>,
                            response: retrofit2.Response<ApiResponse<Any>>
                        ) {
                            if (response.isSuccessful && response.body()?.success == true) {
                                Toast.makeText(this@RegisterActivity, "Data Tersimpan di Database MySQL XAMPP!", Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: retrofit2.Call<ApiResponse<Any>>, t: Throwable) {
                            // Server XAMPP Offline
                        }
                    })

                Toast.makeText(this, "Pembuatan Rekening Berhasil!", Toast.LENGTH_LONG).show()

                val intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra("USER_NAME", name)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }.start()
        }
    }

    private fun checkRegisterInputs() {
        val nik = etNik.text.toString().trim()
        val name = etRegName.text.toString().trim()
        val phone = etRegPhone.text.toString().trim()
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val pin = etPin.text.toString().trim()

        if (nik.length == 16 && name.isNotEmpty() && phone.isNotEmpty() &&
            username.isNotEmpty() && password.isNotEmpty() && pin.length == 6) {
            btnSubmitRegister.isEnabled = true
            btnSubmitRegister.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#1888E7"))
        } else {
            btnSubmitRegister.isEnabled = false
            btnSubmitRegister.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
        }
    }
}

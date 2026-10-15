package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences

object SharedPrefManager {

    private const val PREF_NAME = "brimo_shared_prefs"

    private const val KEY_SALDO = "key_saldo"
    private const val KEY_USER_NAME = "key_user_name"
    private const val KEY_USERNAME = "key_username"
    private const val KEY_NIK = "key_nik"
    private const val KEY_PHONE = "key_phone"
    private const val KEY_FINGERPRINT_ENABLED = "key_fingerprint_enabled"
    private const val KEY_IS_BALANCE_VISIBLE = "key_is_balance_visible"

    private fun getPref(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // ==========================================
    // 1. SALDO MANAGEMENT (Top Up, Transfer, DLL)
    // ==========================================
    fun getSaldo(context: Context): Long {
        return getPref(context).getLong(KEY_SALDO, 1500000L) // Default Rp 1.500.000
    }

    fun setSaldo(context: Context, newSaldo: Long) {
        getPref(context).edit().putLong(KEY_SALDO, newSaldo).apply()
    }

    fun addSaldo(context: Context, amount: Long) {
        val current = getSaldo(context)
        setSaldo(context, current + amount)
    }

    fun subtractSaldo(context: Context, amount: Long): Boolean {
        val current = getSaldo(context)
        if (current >= amount) {
            setSaldo(context, current - amount)
            return true
        }
        return false
    }

    // Format Saldo Rp 1.500.000
    fun getFormattedSaldo(context: Context): String {
        val saldo = getSaldo(context)
        return String.format("Rp %,d", saldo).replace(',', '.')
    }

    // ==========================================
    // 2. USER PROFILE MANAGEMENT
    // ==========================================
    fun saveUserData(context: Context, name: String, username: String, nik: String = "", phone: String = "") {
        getPref(context).edit().apply {
            putString(KEY_USER_NAME, name)
            putString(KEY_USERNAME, username)
            if (nik.isNotEmpty()) putString(KEY_NIK, nik)
            if (phone.isNotEmpty()) putString(KEY_PHONE, phone)
            apply()
        }
    }

    fun getUserName(context: Context): String {
        return getPref(context).getString(KEY_USER_NAME, "ARIL PRATAMA") ?: "ARIL PRATAMA"
    }

    fun getUsername(context: Context): String {
        return getPref(context).getString(KEY_USERNAME, "arilpratama") ?: "arilpratama"
    }

    fun getUserInitials(context: Context): String {
        val name = getUserName(context).trim()
        val parts = name.split("\\s+".toRegex())
        return when {
            parts.size >= 2 -> "${parts[0].first()}${parts[1].first()}".uppercase()
            parts.isNotEmpty() && parts[0].isNotEmpty() -> parts[0].take(2).uppercase()
            else -> "AP"
        }
    }

    // ==========================================
    // 3. FINGERPRINT & BALANCE VISIBILITY
    // ==========================================
    fun isFingerprintEnabled(context: Context): Boolean {
        return getPref(context).getBoolean(KEY_FINGERPRINT_ENABLED, true)
    }

    fun setFingerprintEnabled(context: Context, enabled: Boolean) {
        getPref(context).edit().putBoolean(KEY_FINGERPRINT_ENABLED, enabled).apply()
    }

    fun isBalanceVisible(context: Context): Boolean {
        return getPref(context).getBoolean(KEY_IS_BALANCE_VISIBLE, false)
    }

    fun setBalanceVisible(context: Context, visible: Boolean) {
        getPref(context).edit().putBoolean(KEY_IS_BALANCE_VISIBLE, visible).apply()
    }
}

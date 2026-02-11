package com.simats.farmmillet

import android.content.Context
import android.content.SharedPreferences

/**
 * Simple Token Manager to store JWT token in SharedPreferences
 */
object TokenManager {
    private const val PREF_NAME = "MilletPrefs"
    private const val KEY_TOKEN = "jwt_token"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_ROLE = "user_role"
    private const val KEY_USER_CITY = "user_city"
    private const val KEY_USER_STREET = "user_street"
    private const val KEY_MOBILE = "user_mobile"


    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(context: Context, token: String) {
        getPrefs(context).edit().putString(KEY_TOKEN, token).apply()
    }

    fun saveUserDetails(context: Context, user: UserData) {
        getPrefs(context).edit().apply {
            putString(KEY_USER_NAME, user.name)
            putString(KEY_USER_EMAIL, user.email)
            putString(KEY_MOBILE, user.mobile)
            putString(KEY_USER_ROLE, user.role)
            putString(KEY_USER_CITY, user.city)
            putString(KEY_USER_STREET, user.street)
            apply()
        }
    }

    fun getToken(context: Context): String? = getPrefs(context).getString(KEY_TOKEN, null)
    fun getUserName(context: Context): String = getPrefs(context).getString(KEY_USER_NAME, "") ?: ""
    fun getUserMobile(context: Context): String = getPrefs(context).getString(KEY_MOBILE, "") ?: ""
    fun getUserEmail(context: Context): String = getPrefs(context).getString(KEY_USER_EMAIL, "") ?: ""
    fun getUserRole(context: Context): String = getPrefs(context).getString(KEY_USER_ROLE, "") ?: ""
    fun getUserCity(context: Context): String = getPrefs(context).getString(KEY_USER_CITY, "") ?: ""
    fun getUserStreet(context: Context): String = getPrefs(context).getString(KEY_USER_STREET, "") ?: ""

    fun clearToken(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}

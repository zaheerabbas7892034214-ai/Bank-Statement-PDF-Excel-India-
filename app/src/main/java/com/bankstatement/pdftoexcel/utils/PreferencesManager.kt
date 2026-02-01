package com.bankstatement.pdftoexcel.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager private constructor(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )
    
    var isPremiumUnlocked: Boolean
        get() = prefs.getBoolean(KEY_PREMIUM_UNLOCKED, false)
        set(value) = prefs.edit().putBoolean(KEY_PREMIUM_UNLOCKED, value).apply()
    
    companion object {
        private const val PREFS_NAME = "bank_statement_prefs"
        private const val KEY_PREMIUM_UNLOCKED = "premium_unlocked"
        
        @Volatile
        private var INSTANCE: PreferencesManager? = null
        
        fun getInstance(context: Context): PreferencesManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PreferencesManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }
}

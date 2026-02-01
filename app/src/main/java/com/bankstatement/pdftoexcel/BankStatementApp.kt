package com.bankstatement.pdftoexcel

import android.app.Application

class BankStatementApp : Application() {
    
    companion object {
        lateinit var instance: BankStatementApp
            private set
    }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}

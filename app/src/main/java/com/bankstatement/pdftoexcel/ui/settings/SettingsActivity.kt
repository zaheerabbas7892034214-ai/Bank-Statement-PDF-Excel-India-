package com.bankstatement.pdftoexcel.ui.settings

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bankstatement.pdftoexcel.R
import com.bankstatement.pdftoexcel.billing.BillingManager
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {
    
    private lateinit var toolbar: MaterialToolbar
    private lateinit var privacyPolicyItem: View
    private lateinit var restorePurchasesItem: View
    private lateinit var versionText: TextView
    
    private lateinit var billingManager: BillingManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        
        initializeViews()
        setupToolbar()
        initializeManagers()
        setupListeners()
        displayVersion()
    }
    
    private fun initializeViews() {
        toolbar = findViewById(R.id.toolbar)
        privacyPolicyItem = findViewById(R.id.privacyPolicyItem)
        restorePurchasesItem = findViewById(R.id.restorePurchasesItem)
        versionText = findViewById(R.id.versionText)
    }
    
    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
    
    private fun initializeManagers() {
        billingManager = BillingManager(this)
    }
    
    private fun setupListeners() {
        privacyPolicyItem.setOnClickListener {
            openPrivacyPolicy()
        }
        
        restorePurchasesItem.setOnClickListener {
            restorePurchases()
        }
        
        // Observe purchase state
        lifecycleScope.launch {
            billingManager.purchaseState.collect { state ->
                when (state) {
                    is BillingManager.PurchaseState.Purchased -> {
                        Toast.makeText(
                            this@SettingsActivity,
                            getString(R.string.restore_success),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    is BillingManager.PurchaseState.Error -> {
                        Toast.makeText(
                            this@SettingsActivity,
                            getString(R.string.restore_failed),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> { }
                }
            }
        }
    }
    
    private fun displayVersion() {
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            versionText.text = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            versionText.text = "1.0.0"
        }
    }
    
    private fun openPrivacyPolicy() {
        val url = getString(R.string.privacy_policy_url)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
    
    private fun restorePurchases() {
        billingManager.restorePurchases()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        billingManager.endConnection()
    }
}

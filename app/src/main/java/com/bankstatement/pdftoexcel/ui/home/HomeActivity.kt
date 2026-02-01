package com.bankstatement.pdftoexcel.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bankstatement.pdftoexcel.R
import com.bankstatement.pdftoexcel.billing.BillingManager
import com.bankstatement.pdftoexcel.data.BankTransaction
import com.bankstatement.pdftoexcel.ui.preview.PreviewActivity
import com.bankstatement.pdftoexcel.ui.settings.SettingsActivity
import com.bankstatement.pdftoexcel.utils.PdfProcessor
import com.bankstatement.pdftoexcel.utils.PreferencesManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity() {
    
    private lateinit var toolbar: MaterialToolbar
    private lateinit var selectPdfButton: MaterialButton
    private lateinit var convertButton: MaterialButton
    private lateinit var selectedFileText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView
    private lateinit var premiumBadgeCard: MaterialCardView
    
    private lateinit var prefsManager: PreferencesManager
    private lateinit var billingManager: BillingManager
    private lateinit var pdfProcessor: PdfProcessor
    
    private var selectedPdfUri: Uri? = null
    
    private val pdfPickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedPdfUri = uri
                val fileName = getFileName(uri)
                selectedFileText.text = fileName
                convertButton.isEnabled = true
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        
        initializeViews()
        setupToolbar()
        initializeManagers()
        setupListeners()
        updatePremiumBadge()
    }
    
    private fun initializeViews() {
        toolbar = findViewById(R.id.toolbar)
        selectPdfButton = findViewById(R.id.selectPdfButton)
        convertButton = findViewById(R.id.convertButton)
        selectedFileText = findViewById(R.id.selectedFileText)
        progressBar = findViewById(R.id.progressBar)
        progressText = findViewById(R.id.progressText)
        premiumBadgeCard = findViewById(R.id.premiumBadgeCard)
    }
    
    private fun setupToolbar() {
        setSupportActionBar(toolbar)
    }
    
    private fun initializeManagers() {
        prefsManager = PreferencesManager.getInstance(this)
        billingManager = BillingManager(this)
        pdfProcessor = PdfProcessor(this)
    }
    
    private fun setupListeners() {
        selectPdfButton.setOnClickListener {
            openPdfPicker()
        }
        
        convertButton.setOnClickListener {
            selectedPdfUri?.let { uri ->
                convertPdf(uri)
            }
        }
    }
    
    private fun openPdfPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
        }
        pdfPickerLauncher.launch(intent)
    }
    
    private fun convertPdf(uri: Uri) {
        showProgress(true, getString(R.string.processing_pdf))
        convertButton.isEnabled = false
        
        lifecycleScope.launch {
            try {
                val transactions = withContext(Dispatchers.IO) {
                    pdfProcessor.processPdf(uri)
                }
                
                if (transactions.isEmpty()) {
                    showError(getString(R.string.error_no_transactions))
                } else {
                    navigateToPreview(transactions)
                }
            } catch (e: Exception) {
                showError(getString(R.string.error_pdf_processing, e.message ?: "Unknown error"))
            } finally {
                showProgress(false)
                convertButton.isEnabled = true
            }
        }
    }
    
    private fun navigateToPreview(transactions: List<BankTransaction>) {
        val intent = Intent(this, PreviewActivity::class.java).apply {
            putParcelableArrayListExtra("transactions", ArrayList(transactions))
        }
        startActivity(intent)
    }
    
    private fun showProgress(show: Boolean, message: String = "") {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        progressText.visibility = if (show) View.VISIBLE else View.GONE
        progressText.text = message
    }
    
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    
    private fun updatePremiumBadge() {
        premiumBadgeCard.visibility = if (prefsManager.isPremiumUnlocked) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
    
    private fun getFileName(uri: Uri): String {
        var fileName = "Unknown"
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1 && cursor.moveToFirst()) {
                fileName = cursor.getString(nameIndex)
            }
        }
        return fileName
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    override fun onResume() {
        super.onResume()
        updatePremiumBadge()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        billingManager.endConnection()
    }
}

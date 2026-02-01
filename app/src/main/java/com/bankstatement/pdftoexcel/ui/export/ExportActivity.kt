package com.bankstatement.pdftoexcel.ui.export

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bankstatement.pdftoexcel.R
import com.bankstatement.pdftoexcel.data.BankTransaction
import com.bankstatement.pdftoexcel.utils.CsvExporter
import com.bankstatement.pdftoexcel.utils.ExcelExporter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExportActivity : AppCompatActivity() {
    
    private lateinit var toolbar: MaterialToolbar
    private lateinit var exportCsvButton: MaterialButton
    private lateinit var exportExcelButton: MaterialButton
    private lateinit var shareButton: MaterialButton
    private lateinit var progressBar: ProgressBar
    private lateinit var statusText: TextView
    
    private lateinit var csvExporter: CsvExporter
    private lateinit var excelExporter: ExcelExporter
    
    private var transactions: List<BankTransaction> = emptyList()
    private var lastExportedFileUri: Uri? = null
    
    private val csvExportLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                exportToCsv(uri)
            }
        }
    }
    
    private val excelExportLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                exportToExcel(uri)
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_export)
        
        initializeViews()
        setupToolbar()
        initializeExporters()
        loadTransactions()
        setupListeners()
    }
    
    private fun initializeViews() {
        toolbar = findViewById(R.id.toolbar)
        exportCsvButton = findViewById(R.id.exportCsvButton)
        exportExcelButton = findViewById(R.id.exportExcelButton)
        shareButton = findViewById(R.id.shareButton)
        progressBar = findViewById(R.id.progressBar)
        statusText = findViewById(R.id.statusText)
    }
    
    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
    
    private fun initializeExporters() {
        csvExporter = CsvExporter(this)
        excelExporter = ExcelExporter(this)
    }
    
    private fun loadTransactions() {
        @Suppress("DEPRECATION")
        transactions = intent.getParcelableArrayListExtra("transactions") ?: emptyList()
    }
    
    private fun setupListeners() {
        exportCsvButton.setOnClickListener {
            openCsvSaveDialog()
        }
        
        exportExcelButton.setOnClickListener {
            openExcelSaveDialog()
        }
        
        shareButton.setOnClickListener {
            shareFile()
        }
    }
    
    private fun openCsvSaveDialog() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/csv"
            putExtra(Intent.EXTRA_TITLE, "bank_statement_${System.currentTimeMillis()}.csv")
        }
        csvExportLauncher.launch(intent)
    }
    
    private fun openExcelSaveDialog() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            putExtra(Intent.EXTRA_TITLE, "bank_statement_${System.currentTimeMillis()}.xlsx")
        }
        excelExportLauncher.launch(intent)
    }
    
    private fun exportToCsv(uri: Uri) {
        showProgress(true, getString(R.string.exporting_file))
        
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    csvExporter.exportToCsv(transactions, uri)
                }
                lastExportedFileUri = uri
                showSuccess(getString(R.string.export_success))
                shareButton.isEnabled = true
            } catch (e: Exception) {
                showError(getString(R.string.export_failed, e.message ?: "Unknown error"))
            } finally {
                showProgress(false)
            }
        }
    }
    
    private fun exportToExcel(uri: Uri) {
        showProgress(true, getString(R.string.exporting_file))
        
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    excelExporter.exportToExcel(transactions, uri)
                }
                lastExportedFileUri = uri
                showSuccess(getString(R.string.export_success))
                shareButton.isEnabled = true
            } catch (e: Exception) {
                showError(getString(R.string.export_failed, e.message ?: "Unknown error"))
            } finally {
                showProgress(false)
            }
        }
    }
    
    private fun shareFile() {
        lastExportedFileUri?.let { uri ->
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = contentResolver.getType(uri)
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_file)))
        }
    }
    
    private fun showProgress(show: Boolean, message: String = "") {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        statusText.visibility = if (show) View.VISIBLE else View.GONE
        statusText.text = message
        exportCsvButton.isEnabled = !show
        exportExcelButton.isEnabled = !show
    }
    
    private fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}

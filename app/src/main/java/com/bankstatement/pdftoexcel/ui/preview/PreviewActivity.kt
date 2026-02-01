package com.bankstatement.pdftoexcel.ui.preview

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bankstatement.pdftoexcel.R
import com.bankstatement.pdftoexcel.billing.BillingManager
import com.bankstatement.pdftoexcel.data.BankTransaction
import com.bankstatement.pdftoexcel.ui.export.ExportActivity
import com.bankstatement.pdftoexcel.utils.PreferencesManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.launch

class PreviewActivity : AppCompatActivity() {
    
    private lateinit var toolbar: MaterialToolbar
    private lateinit var transactionsRecyclerView: RecyclerView
    private lateinit var totalTransactionsText: TextView
    private lateinit var lockedCard: MaterialCardView
    private lateinit var lockedText: TextView
    private lateinit var unlockButton: MaterialButton
    private lateinit var exportButton: MaterialButton
    
    private lateinit var prefsManager: PreferencesManager
    private lateinit var billingManager: BillingManager
    private lateinit var adapter: TransactionAdapter
    
    private var transactions: List<BankTransaction> = emptyList()
    private val freeRowsLimit = 10
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        
        initializeViews()
        setupToolbar()
        initializeManagers()
        loadTransactions()
        setupRecyclerView()
        setupListeners()
        updateUI()
    }
    
    private fun initializeViews() {
        toolbar = findViewById(R.id.toolbar)
        transactionsRecyclerView = findViewById(R.id.transactionsRecyclerView)
        totalTransactionsText = findViewById(R.id.totalTransactionsText)
        lockedCard = findViewById(R.id.lockedCard)
        lockedText = findViewById(R.id.lockedText)
        unlockButton = findViewById(R.id.unlockButton)
        exportButton = findViewById(R.id.exportButton)
    }
    
    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
    
    private fun initializeManagers() {
        prefsManager = PreferencesManager.getInstance(this)
        billingManager = BillingManager(this)
    }
    
    private fun loadTransactions() {
        @Suppress("DEPRECATION")
        transactions = intent.getParcelableArrayListExtra("transactions") ?: emptyList()
    }
    
    private fun setupRecyclerView() {
        adapter = TransactionAdapter(
            transactions = transactions,
            isPremium = prefsManager.isPremiumUnlocked,
            freeRowsLimit = freeRowsLimit
        )
        
        transactionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@PreviewActivity)
            adapter = this@PreviewActivity.adapter
        }
    }
    
    private fun setupListeners() {
        unlockButton.setOnClickListener {
            launchPurchaseFlow()
        }
        
        exportButton.setOnClickListener {
            navigateToExport()
        }
        
        // Observe purchase state
        lifecycleScope.launch {
            billingManager.purchaseState.collect { state ->
                when (state) {
                    is BillingManager.PurchaseState.Purchased -> {
                        Toast.makeText(
                            this@PreviewActivity,
                            getString(R.string.purchase_success),
                            Toast.LENGTH_LONG
                        ).show()
                        updateUI()
                        refreshAdapter()
                    }
                    is BillingManager.PurchaseState.Error -> {
                        Toast.makeText(
                            this@PreviewActivity,
                            getString(R.string.purchase_failed, state.message),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    is BillingManager.PurchaseState.Cancelled -> {
                        // User cancelled, do nothing
                    }
                    else -> { }
                }
            }
        }
    }
    
    private fun updateUI() {
        val isPremium = prefsManager.isPremiumUnlocked
        val totalCount = transactions.size
        
        totalTransactionsText.text = getString(R.string.total_transactions, totalCount)
        
        if (isPremium || totalCount <= freeRowsLimit) {
            lockedCard.visibility = View.GONE
        } else {
            lockedCard.visibility = View.VISIBLE
            lockedText.text = getString(R.string.preview_locked, freeRowsLimit + 1)
        }
    }
    
    private fun refreshAdapter() {
        adapter = TransactionAdapter(
            transactions = transactions,
            isPremium = prefsManager.isPremiumUnlocked,
            freeRowsLimit = freeRowsLimit
        )
        transactionsRecyclerView.adapter = adapter
    }
    
    private fun launchPurchaseFlow() {
        billingManager.launchPurchaseFlow(this)
    }
    
    private fun navigateToExport() {
        val intent = Intent(this, ExportActivity::class.java).apply {
            putParcelableArrayListExtra("transactions", ArrayList(transactions))
        }
        startActivity(intent)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        billingManager.endConnection()
    }
}

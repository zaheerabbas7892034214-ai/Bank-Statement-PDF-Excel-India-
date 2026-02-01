package com.bankstatement.pdftoexcel.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import com.bankstatement.pdftoexcel.utils.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BillingManager(private val context: Context) {
    
    companion object {
        const val PREMIUM_PRODUCT_ID = "premium_unlock"
    }
    
    private val _purchaseState = MutableStateFlow<PurchaseState>(PurchaseState.NotPurchased)
    val purchaseState: StateFlow<PurchaseState> = _purchaseState
    
    private val prefsManager = PreferencesManager.getInstance(context)
    
    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            _purchaseState.value = PurchaseState.Cancelled
        } else {
            _purchaseState.value = PurchaseState.Error(billingResult.debugMessage)
        }
    }
    
    private val billingClient = BillingClient.newBuilder(context)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases()
        .build()
    
    init {
        startConnection()
    }
    
    private fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // Query existing purchases
                    queryPurchases()
                }
            }
            
            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request
            }
        })
    }
    
    private fun queryPurchases() {
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        ) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                for (purchase in purchases) {
                    if (purchase.products.contains(PREMIUM_PRODUCT_ID) && 
                        purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        handlePurchase(purchase)
                        return@queryPurchasesAsync
                    }
                }
            }
        }
    }
    
    fun launchPurchaseFlow(activity: Activity) {
        if (!billingClient.isReady) {
            _purchaseState.value = PurchaseState.Error("Billing service not ready")
            return
        }
        
        val queryProductDetailsParams = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(PREMIUM_PRODUCT_ID)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                )
            )
            .build()
        
        billingClient.queryProductDetailsAsync(queryProductDetailsParams) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList.isNotEmpty()) {
                val productDetails = productDetailsList[0]
                
                val productDetailsParamsList = listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails)
                        .build()
                )
                
                val billingFlowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(productDetailsParamsList)
                    .build()
                
                billingClient.launchBillingFlow(activity, billingFlowParams)
            } else {
                _purchaseState.value = PurchaseState.Error("Product not found")
            }
        }
    }
    
    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                
                billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        unlockPremium()
                    }
                }
            } else {
                unlockPremium()
            }
        }
    }
    
    private fun unlockPremium() {
        prefsManager.isPremiumUnlocked = true
        _purchaseState.value = PurchaseState.Purchased
    }
    
    fun restorePurchases() {
        queryPurchases()
    }
    
    fun isPremiumUnlocked(): Boolean {
        return prefsManager.isPremiumUnlocked
    }
    
    fun endConnection() {
        billingClient.endConnection()
    }
    
    sealed class PurchaseState {
        object NotPurchased : PurchaseState()
        object Purchased : PurchaseState()
        object Cancelled : PurchaseState()
        data class Error(val message: String) : PurchaseState()
    }
}

package com.bankstatement.pdftoexcel.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BankTransaction(
    val date: String,
    val description: String,
    val debit: String,
    val credit: String,
    val balance: String
) : Parcelable {
    companion object {
        fun getHeader(): BankTransaction {
            return BankTransaction(
                date = "Date",
                description = "Description",
                debit = "Debit",
                credit = "Credit",
                balance = "Balance"
            )
        }
    }
}

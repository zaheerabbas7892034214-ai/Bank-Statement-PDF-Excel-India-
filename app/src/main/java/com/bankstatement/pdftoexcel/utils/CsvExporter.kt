package com.bankstatement.pdftoexcel.utils

import android.content.Context
import android.net.Uri
import com.bankstatement.pdftoexcel.data.BankTransaction
import java.io.OutputStreamWriter

class CsvExporter(private val context: Context) {
    
    fun exportToCsv(transactions: List<BankTransaction>, outputUri: Uri) {
        try {
            context.contentResolver.openOutputStream(outputUri)?.use { outputStream ->
                OutputStreamWriter(outputStream).use { writer ->
                    // Write header
                    writer.write("Date,Description,Debit,Credit,Balance\n")
                    
                    // Write transactions
                    for (transaction in transactions) {
                        writer.write(
                            "${escapeCsvField(transaction.date)}," +
                            "${escapeCsvField(transaction.description)}," +
                            "${escapeCsvField(transaction.debit)}," +
                            "${escapeCsvField(transaction.credit)}," +
                            "${escapeCsvField(transaction.balance)}\n"
                        )
                    }
                }
            } ?: throw Exception("Cannot open output stream")
        } catch (e: Exception) {
            throw ExportException("Failed to export CSV: ${e.message}", e)
        }
    }
    
    private fun escapeCsvField(field: String): String {
        return if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            "\"${field.replace("\"", "\"\"")}\""
        } else {
            field
        }
    }
    
    class ExportException(message: String, cause: Throwable? = null) : Exception(message, cause)
}

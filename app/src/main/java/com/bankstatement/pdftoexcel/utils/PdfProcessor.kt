package com.bankstatement.pdftoexcel.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.bankstatement.pdftoexcel.data.BankTransaction
import java.io.IOException

class PdfProcessor(private val context: Context) {
    
    suspend fun processPdf(uri: Uri): List<BankTransaction> {
        return try {
            // Try text extraction first
            val textTransactions = extractTextFromPdf(uri)
            if (textTransactions.isNotEmpty()) {
                return textTransactions
            }
            
            // If text extraction fails, try OCR
            extractTextWithOcr(uri)
        } catch (e: Exception) {
            throw PdfProcessingException("Failed to process PDF: ${e.message}", e)
        }
    }
    
    private fun extractTextFromPdf(uri: Uri): List<BankTransaction> {
        val transactions = mutableListOf<BankTransaction>()
        var pfd: ParcelFileDescriptor? = null
        var renderer: PdfRenderer? = null
        
        try {
            pfd = context.contentResolver.openFileDescriptor(uri, "r")
                ?: throw IOException("Cannot open PDF file")
            
            renderer = PdfRenderer(pfd)
            val pageCount = renderer.pageCount
            
            val allText = StringBuilder()
            for (i in 0 until pageCount) {
                val page = renderer.openPage(i)
                // Note: PdfRenderer doesn't directly extract text
                // This is a placeholder - in production, use a library like PDFBox or iText
                page.close()
            }
            
            // Parse the extracted text
            transactions.addAll(parseTransactions(allText.toString()))
            
        } catch (e: Exception) {
            // Text extraction failed, will try OCR
        } finally {
            renderer?.close()
            pfd?.close()
        }
        
        return transactions
    }
    
    private suspend fun extractTextWithOcr(uri: Uri): List<BankTransaction> {
        val transactions = mutableListOf<BankTransaction>()
        var pfd: ParcelFileDescriptor? = null
        var renderer: PdfRenderer? = null
        
        try {
            pfd = context.contentResolver.openFileDescriptor(uri, "r")
                ?: throw IOException("Cannot open PDF file")
            
            renderer = PdfRenderer(pfd)
            val pageCount = renderer.pageCount
            
            val ocrExtractor = OcrExtractor(context)
            
            for (i in 0 until pageCount) {
                val page = renderer.openPage(i)
                
                // Render page to bitmap
                val bitmap = Bitmap.createBitmap(
                    page.width * 2, // Higher resolution for better OCR
                    page.height * 2,
                    Bitmap.Config.ARGB_8888
                )
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                
                // Extract text using OCR
                val pageText = ocrExtractor.extractText(bitmap)
                transactions.addAll(parseTransactions(pageText))
                
                bitmap.recycle()
                page.close()
            }
            
        } finally {
            renderer?.close()
            pfd?.close()
        }
        
        return transactions
    }
    
    private fun parseTransactions(text: String): List<BankTransaction> {
        val transactions = mutableListOf<BankTransaction>()
        val lines = text.lines()
        
        for (line in lines) {
            // Skip empty lines and headers
            if (line.isBlank() || isHeaderOrFooter(line)) {
                continue
            }
            
            // Try to parse transaction from line
            val transaction = parseTransactionLine(line)
            if (transaction != null) {
                transactions.add(transaction)
            }
        }
        
        return transactions
    }
    
    private fun isHeaderOrFooter(line: String): Boolean {
        val lowerLine = line.lowercase()
        return lowerLine.contains("date") && lowerLine.contains("description") ||
                lowerLine.contains("page") ||
                lowerLine.contains("statement") ||
                lowerLine.contains("balance") && lowerLine.contains("opening") ||
                lowerLine.contains("balance") && lowerLine.contains("closing")
    }
    
    private fun parseTransactionLine(line: String): BankTransaction? {
        // Indian date patterns: DD/MM/YYYY, DD-MM-YYYY, DD.MM.YYYY
        val datePattern = Regex("""(\d{2}[/.-]\d{2}[/.-]\d{4})""")
        val dateMatch = datePattern.find(line) ?: return null
        
        val date = dateMatch.value
        val remainingText = line.substring(dateMatch.range.last + 1).trim()
        
        // Amount pattern: matches Indian currency format with optional ₹
        val amountPattern = Regex("""₹?\s*(\d{1,3}(?:,\d{2,3})*(?:\.\d{2})?)""")
        val amounts = amountPattern.findAll(remainingText).map { 
            it.groupValues[1].replace(",", "")
        }.toList()
        
        if (amounts.isEmpty()) return null
        
        // Extract description (text before amounts)
        val firstAmountIndex = amountPattern.find(remainingText)?.range?.first ?: 0
        val description = remainingText.substring(0, firstAmountIndex).trim()
        
        // Determine debit, credit, balance based on number of amounts
        val debit: String
        val credit: String
        val balance: String
        
        when (amounts.size) {
            1 -> {
                debit = ""
                credit = ""
                balance = amounts[0]
            }
            2 -> {
                debit = amounts[0]
                credit = ""
                balance = amounts[1]
            }
            3 -> {
                debit = amounts[0]
                credit = amounts[1]
                balance = amounts[2]
            }
            else -> {
                // Take last 3 amounts
                debit = amounts[amounts.size - 3]
                credit = amounts[amounts.size - 2]
                balance = amounts[amounts.size - 1]
            }
        }
        
        return BankTransaction(
            date = normalizeDate(date),
            description = cleanDescription(description),
            debit = debit,
            credit = credit,
            balance = balance
        )
    }
    
    private fun normalizeDate(date: String): String {
        // Convert DD/MM/YYYY or DD-MM-YYYY to DD/MM/YYYY
        return date.replace("-", "/").replace(".", "/")
    }
    
    private fun cleanDescription(description: String): String {
        return description
            .replace(Regex("""\s+"""), " ") // Multiple spaces to single
            .trim()
    }
    
    class PdfProcessingException(message: String, cause: Throwable? = null) : Exception(message, cause)
}

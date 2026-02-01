package com.bankstatement.pdftoexcel.utils

import android.content.Context
import android.net.Uri
import com.bankstatement.pdftoexcel.data.BankTransaction
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.IOException

class ExcelExporter(private val context: Context) {
    
    fun exportToExcel(transactions: List<BankTransaction>, outputUri: Uri) {
        try {
            val workbook = XSSFWorkbook()
            val sheet = workbook.createSheet("Bank Statement")
            
            // Create header style
            val headerStyle = workbook.createCellStyle().apply {
                fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
                fillPattern = FillPatternType.SOLID_FOREGROUND
                val font = workbook.createFont().apply {
                    bold = true
                }
                setFont(font)
            }
            
            // Create header row
            val headerRow = sheet.createRow(0)
            val headers = listOf("Date", "Description", "Debit", "Credit", "Balance")
            headers.forEachIndexed { index, header ->
                val cell = headerRow.createCell(index)
                cell.setCellValue(header)
                cell.cellStyle = headerStyle
            }
            
            // Create data rows
            transactions.forEachIndexed { index, transaction ->
                val row = sheet.createRow(index + 1)
                row.createCell(0).setCellValue(transaction.date)
                row.createCell(1).setCellValue(transaction.description)
                row.createCell(2).setCellValue(transaction.debit)
                row.createCell(3).setCellValue(transaction.credit)
                row.createCell(4).setCellValue(transaction.balance)
            }
            
            // Auto-size columns
            for (i in 0 until headers.size) {
                sheet.autoSizeColumn(i)
            }
            
            // Write to output stream
            context.contentResolver.openOutputStream(outputUri)?.use { outputStream ->
                workbook.write(outputStream)
            } ?: throw IOException("Cannot open output stream")
            
            workbook.close()
            
        } catch (e: Exception) {
            throw ExportException("Failed to export Excel: ${e.message}", e)
        }
    }
    
    class ExportException(message: String, cause: Throwable? = null) : Exception(message, cause)
}

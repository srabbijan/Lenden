package bd.srabbijan.lenden.core

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import bd.srabbijan.lenden.data.local.entity.TransactionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import android.graphics.Typeface

actual class PdfGenerator(private val context: Context) {
    
    actual suspend fun generateTransactionPdf(
        customerName: String,
        balance: Double,
        transactions: List<TransactionEntity>,
        dateRange: String,
        totalGiven: Double,
        totalTaken: Double
    ): String = withContext(Dispatchers.IO) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        
        var yPosition = 50f
        val leftMargin = 40f
        val rightMargin = 555f
        
        // Title Paint
        val titlePaint = Paint().apply {
            textSize = 24f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            color = android.graphics.Color.BLACK
        }
        
        // Header Paint
        val headerPaint = Paint().apply {
            textSize = 16f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            color = android.graphics.Color.BLACK
        }
        
        // Normal Paint
        val normalPaint = Paint().apply {
            textSize = 12f
            color = android.graphics.Color.BLACK
        }
        
        // Small Paint
        val smallPaint = Paint().apply {
            textSize = 10f
            color = android.graphics.Color.GRAY
        }
        
        // Draw Title
        canvas.drawText(customerName, leftMargin, yPosition, titlePaint)
        yPosition += 40f

        
        // Draw Date Range
        canvas.drawText(dateRange, leftMargin, yPosition, normalPaint)
        yPosition += 25f
        
        // Draw Balance
        val balanceText = if (balance >= 0) {
            "৳${String.format("%.2f", balance)} (পাবো)"
        } else {
            "৳${String.format("%.2f", -balance)} (দিবো)"
        }
        val balanceColor = if (balance >= 0) android.graphics.Color.GREEN else android.graphics.Color.RED
        val balancePaint = Paint().apply {
            textSize = 14f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            color = balanceColor
        }
        canvas.drawText(balanceText, leftMargin, yPosition, balancePaint)
        yPosition += 35f
        
        // Draw Summary
        canvas.drawText("Summary:", leftMargin, yPosition, headerPaint)
        yPosition += 25f
        canvas.drawText("Total Given: ৳${String.format("%.2f", totalGiven)}", leftMargin + 20f, yPosition, normalPaint)
        yPosition += 20f
        canvas.drawText("Total Taken: ৳${String.format("%.2f", totalTaken)}", leftMargin + 20f, yPosition, normalPaint)
        yPosition += 35f
        
        // Draw line separator
        canvas.drawLine(leftMargin, yPosition, rightMargin, yPosition, normalPaint)
        yPosition += 20f
        
        // Table Header
        canvas.drawText("Date", leftMargin, yPosition, normalPaint)
        canvas.drawText("Type", leftMargin + 150f, yPosition, normalPaint)
        canvas.drawText("Amount", leftMargin + 250f, yPosition, normalPaint)
        canvas.drawText("Note", leftMargin + 350f, yPosition, normalPaint)
        yPosition += 5f
        canvas.drawLine(leftMargin, yPosition, rightMargin, yPosition, smallPaint)
        yPosition += 15f
        
        // Draw Transactions
        if (transactions.isEmpty()) {
            canvas.drawText("No transactions found", leftMargin, yPosition, smallPaint)
        } else {
            for (transaction in transactions) {
                // Check if we need a new page
                if (yPosition > 780f) {
                    pdfDocument.finishPage(page)
                    val newPage = pdfDocument.startPage(pageInfo)
                    canvas.drawText("(Continued...)", leftMargin, 50f, smallPaint)
                    yPosition = 80f
                }
                
                // Date
                val date = transaction.date.take(10) // Extract date part
                canvas.drawText(date, leftMargin, yPosition, smallPaint)
                
                // Type
                val typeText = if (transaction.type == "GIVEN") "দিয়েছি" else "নিয়েছি"
                val typeColor = if (transaction.type == "GIVEN") 
                    android.graphics.Color.RED else android.graphics.Color.GREEN
                val typePaint = Paint().apply {
                    textSize = 10f
                    color = typeColor
                }
                canvas.drawText(typeText, leftMargin + 150f, yPosition, typePaint)
                
                // Amount
                canvas.drawText("৳${String.format("%.2f", transaction.amount)}", 
                    leftMargin + 250f, yPosition, smallPaint)
                
                // Note (truncate if too long)
                val note = if (transaction.note.length > 20) 
                    transaction.note.take(17) + "..." else transaction.note
                canvas.drawText(note, leftMargin + 350f, yPosition, smallPaint)
                
                yPosition += 20f
            }
        }
        
        pdfDocument.finishPage(page)
        
        // Save PDF
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileName = "Transaction_${customerName.replace(" ", "_")}_${System.currentTimeMillis()}.pdf"
        val file = File(downloadsDir, fileName)
        
        FileOutputStream(file).use { outputStream ->
            pdfDocument.writeTo(outputStream)
        }
        
        pdfDocument.close()
        
        file.absolutePath
    }
}

actual fun createPdfGenerator(): PdfGenerator {
    // This will be called from DI with context
    throw IllegalStateException("Use DI to create PdfGenerator")
}

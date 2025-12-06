package bd.srabbijan.lenden.core

import bd.srabbijan.lenden.data.local.entity.TransactionEntity
import kotlinx.cinterop.*
import platform.CoreGraphics.*
import platform.Foundation.*
import platform.UIKit.*
import platform.PDFKit.*

actual class PdfGenerator {
    
    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun generateTransactionPdf(
        customerName: String,
        balance: Double,
        transactions: List<TransactionEntity>,
        dateRange: String,
        totalGiven: Double,
        totalTaken: Double
    ): String {
        val pdfData = NSMutableData()
        UIGraphicsBeginPDFContextToData(pdfData, CGRectMake(0.0, 0.0, 595.0, 842.0), null)
        UIGraphicsBeginPDFPage()
        
        val context = UIGraphicsGetCurrentContext()
        var yPosition = 50.0
        val leftMargin = 40.0
        val rightMargin = 555.0
        
        // Helper function to draw text
        fun drawText(text: String, x: Double, y: Double, fontSize: Double, bold: Boolean = false) {
            val font = if (bold) {
                UIFont.boldSystemFontOfSize(fontSize)
            } else {
                UIFont.systemFontOfSize(fontSize)
            }
            
            val attributes = mapOf<Any?, Any?>(
                NSFontAttributeName to font,
                NSForegroundColorAttributeName to UIColor.blackColor
            )
            
            (text as NSString).drawAtPoint(
                CGPointMake(x, y),
                withAttributes = attributes as Map<Any?, *>
            )
        }
        
        // Draw Title
        drawText("Transaction Report", leftMargin, yPosition, 24.0, bold = true)
        yPosition += 40.0
        
        // Draw Customer Name
        drawText("Customer: $customerName", leftMargin, yPosition, 16.0, bold = true)
        yPosition += 30.0
        
        // Draw Date Range
        drawText("Period: $dateRange", leftMargin, yPosition, 12.0)
        yPosition += 25.0
        
        // Draw Balance
        val balanceText = if (balance >= 0) {
            "Balance: ৳${balance} (পাবো)"
        } else {
            "Balance: ৳${-balance} (দিবো)"
        }
        drawText(balanceText, leftMargin, yPosition, 14.0, bold = true)
        yPosition += 35.0
        
        // Draw Summary
        drawText("Summary:", leftMargin, yPosition, 16.0, bold = true)
        yPosition += 25.0
        drawText("Total Given: ৳${ totalGiven}", leftMargin + 20.0, yPosition, 12.0)
        yPosition += 20.0
        drawText("Total Taken: ৳${totalTaken}", leftMargin + 20.0, yPosition, 12.0)
        yPosition += 35.0
        
        // Draw line separator
        context?.let {
            CGContextSetRGBStrokeColor(it, 0.0, 0.0, 0.0, 1.0)
            CGContextMoveToPoint(it, leftMargin, yPosition)
            CGContextAddLineToPoint(it, rightMargin, yPosition)
            CGContextStrokePath(it)
        }
        yPosition += 20.0
        
        // Draw Transactions Header
        drawText("Transactions:", leftMargin, yPosition, 16.0, bold = true)
        yPosition += 25.0
        
        // Table Header
        drawText("Date", leftMargin, yPosition, 12.0)
        drawText("Type", leftMargin + 150.0, yPosition, 12.0)
        drawText("Amount", leftMargin + 250.0, yPosition, 12.0)
        drawText("Note", leftMargin + 350.0, yPosition, 12.0)
        yPosition += 5.0
        
        context?.let {
            CGContextSetRGBStrokeColor(it, 0.5, 0.5, 0.5, 1.0)
            CGContextMoveToPoint(it, leftMargin, yPosition)
            CGContextAddLineToPoint(it, rightMargin, yPosition)
            CGContextStrokePath(it)
        }
        yPosition += 15.0
        
        // Draw Transactions
        if (transactions.isEmpty()) {
            drawText("No transactions found", leftMargin, yPosition, 10.0)
        } else {
            for (transaction in transactions) {
                // Check if we need a new page
                if (yPosition > 780.0) {
                    UIGraphicsBeginPDFPage()
                    drawText("(Continued...)", leftMargin, 50.0, 10.0)
                    yPosition = 80.0
                }
                
                // Date
                val date = transaction.date.take(10)
                drawText(date, leftMargin, yPosition, 10.0)
                
                // Type
                val typeText = if (transaction.type == "GIVEN") "দিয়েছি" else "নিয়েছি"
                drawText(typeText, leftMargin + 150.0, yPosition, 10.0)
                
                // Amount
                drawText("৳${transaction.amount}",
                    leftMargin + 250.0, yPosition, 10.0)
                
                // Note
                val note = if (transaction.note.length > 20) 
                    transaction.note.take(17) + "..." else transaction.note
                drawText(note, leftMargin + 350.0, yPosition, 10.0)
                
                yPosition += 20.0
            }
        }
        
        UIGraphicsEndPDFContext()
        
        // Save PDF
        val documentsPath = NSSearchPathForDirectoriesInDomains(
            NSDocumentDirectory,
            NSUserDomainMask,
            true
        ).firstOrNull() as? String ?: ""
        
        val fileName = "Transaction_${customerName.replace(" ", "_")}_${NSDate().timeIntervalSince1970.toLong()}.pdf"
        val filePath = "$documentsPath/$fileName"
        
        pdfData.writeToFile(filePath, atomically = true)
        
        return filePath
    }
}

actual fun createPdfGenerator(): PdfGenerator = PdfGenerator()

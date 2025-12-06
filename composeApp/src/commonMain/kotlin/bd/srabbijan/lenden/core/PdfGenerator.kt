package bd.srabbijan.lenden.core

import bd.srabbijan.lenden.data.local.entity.TransactionEntity

expect class PdfGenerator {
    suspend fun generateTransactionPdf(
        customerName: String,
        balance: Double,
        transactions: List<TransactionEntity>,
        dateRange: String,
        totalGiven: Double,
        totalTaken: Double
    ): String // Returns file path
}

expect fun createPdfGenerator(): PdfGenerator

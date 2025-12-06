/*
package bd.srabbijan.lenden.presentation.home.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.graphics.withTranslation
import bd.srabbijan.smartpos.core.utils.convertApiDateToUIDate
import bd.srabbijan.smartpos.core.utils.getCurrentDateTime
import bd.srabbijan.smartpos.core.utils.toCurrencyFormat
import bd.srabbijan.smartpos.core.utils.toPositive
import bd.srabbijan.smartpos.feature.customer.domain.Customer
import java.io.ByteArrayOutputStream


fun generateCustomerListPdf(
    context: Context,
    data: List<Customer>,
    totalGivenAmount:Double?,
    totalTakenAmount:Double?,
): ByteArray {

    val document = PdfDocument()
    val pageWidth = 384
    val pageHeight = 600
    var pageNumber = 1
    var y = 0

    val margin = 20

    lateinit var canvas: Canvas
    lateinit var paint: Paint
    lateinit var page: PdfDocument.Page

    fun startNewPage() {
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber++).create()
        page = document.startPage(pageInfo)
        canvas = page.canvas
        paint = Paint().apply {
            typeface = Typeface.createFromAsset(context.assets, "solaimanlipi.ttf")
            textSize = 12f
            color = Color.BLACK
            isSubpixelText = true
        }
        y = 20
    }

    fun finishCurrentPage() {
        document.finishPage(page)
    }

    fun checkPageSpace(lines: Int = 1, lineHeight: Int = 20) {
        if (y + (lines * lineHeight) > pageHeight) {
            finishCurrentPage()
            startNewPage()
        }
    }

    fun drawCentered(text: String) {
        checkPageSpace()
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(text, (pageWidth / 2f), y.toFloat(), paint)
        y += 20
    }

    fun drawLeft(text: String) {
        checkPageSpace()
        paint.textAlign = Paint.Align.LEFT
        canvas.drawText(text, margin.toFloat(), y.toFloat(), paint)
        y += 20
    }

    fun drawRight(text: String) {
        checkPageSpace()
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText(text, (pageWidth - margin).toFloat(), y.toFloat(), paint)
        y += 20
    }
    fun drawMultilineText(text: String, x: Float, y: Float, maxWidth: Int): Int {
        val textPaint = TextPaint().apply {
            typeface = paint.typeface
            textSize = paint.textSize
            color = paint.color
        }

        val staticLayout = StaticLayout.Builder.obtain(text, 0, text.length, textPaint, maxWidth)
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(0f, 1f)
            .build()

        canvas.withTranslation(x, y) {
            staticLayout.draw(this)
        }

        return staticLayout.height
    }

    fun drawLine() {
        checkPageSpace()
        canvas.drawLine(
            margin.toFloat(), y.toFloat(),
            (pageWidth - margin).toFloat(),
            y.toFloat(),
            paint
        )
        y += 20
    }
    // Start first page
    startNewPage()

    // Header

    drawCentered("কাস্টমার লিস্ট")

    y += 10
    drawLeft("বাকি পাবো : ${totalGivenAmount.toCurrencyFormat()}")
    drawLeft("বাকি দিবো : ${totalTakenAmount.toPositive().toCurrencyFormat()}")

    y += 10

    // Table headers
    paint.textAlign = Paint.Align.LEFT
    canvas.drawText("কাস্টমার", margin.toFloat(), y.toFloat(), paint)

    paint.textAlign = Paint.Align.RIGHT
    canvas.drawText("বাকি", (pageWidth - margin).toFloat(), y.toFloat(), paint)
    y += 10
    drawLine()

    // list of items

    data.onEach {
        checkPageSpace()
        paint.textAlign = Paint.Align.LEFT
        val nameHeight = drawMultilineText(it.name, margin.toFloat(), y.toFloat(), 200)
        val centerY = y + (nameHeight / 2) + (paint.textSize / 3)
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText(it.amount.toCurrencyFormat(), (pageWidth - margin).toFloat(), centerY, paint)

        y += nameHeight.coerceAtLeast(10)
    }

    y += 10
    drawLine()


    drawCentered(getCurrentDateTime().convertApiDateToUIDate())
    drawCentered("Software Developed By : sPOS")

    finishCurrentPage()

    val outputStream = ByteArrayOutputStream()
    document.writeTo(outputStream)
    document.close()
    return outputStream.toByteArray()
}




*/

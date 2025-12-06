package bd.srabbijan.lenden.core

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

actual class PdfViewer(private val context: Context) {
    actual fun openPdf(filePath: String) {
        try {
            val file = File(filePath)
            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback: try to open with any available app
            try {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(Uri.parse(filePath), "application/pdf")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            } catch (e2: Exception) {
                e2.printStackTrace()
            }
        }
    }
}

package bd.srabbijan.lenden.core

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentInteractionController

actual class PdfViewer {
    @OptIn(ExperimentalForeignApi::class)
    actual fun openPdf(filePath: String) {
        val url = NSURL.fileURLWithPath(filePath)
        
        // Use UIDocumentInteractionController for better PDF handling
        val documentController = UIDocumentInteractionController.interactionControllerWithURL(url)
        
        // Present options menu (Open in, Share, etc.)
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.let {
            documentController.presentOptionsMenuFromRect(
                rect = it.view.bounds,
                inView = it.view,
                animated = true
            )
        }
    }
}

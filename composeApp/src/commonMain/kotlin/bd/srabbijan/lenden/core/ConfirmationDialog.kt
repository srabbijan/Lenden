package bd.srabbijan.lenden.core

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import bd.srabbijan.lenden.theme.AppTheme

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmText: String = "নিশ্চিত করুন",
    cancelText: String = "বাতিল",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = AppTheme.typography.titleNormal
            )
        },
        text = {
            Text(
                text = message,
                style = AppTheme.typography.paragraph
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = confirmText,
                    color = AppTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = cancelText,
                    color = AppTheme.colorScheme.text
                )
            }
        }
    )
}

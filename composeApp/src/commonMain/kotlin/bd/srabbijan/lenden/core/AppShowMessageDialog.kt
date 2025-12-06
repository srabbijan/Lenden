package bd.srabbijan.lenden.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import bd.srabbijan.lenden.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class MessageDialogType {
    SUCCESS,
    ERROR,
    WARNING
}

@Composable
fun AppShowMessageDialog(
    title: String = "Failed",
    message: String,
    positiveButtonText: String = "Ok",
    negativeButtonText: String = "Close",
    onPositiveButtonClick: (() -> Unit)? = null,
    type: MessageDialogType = MessageDialogType.WARNING,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        AppCard {
            Column(
                Modifier
                    .background(Color.Transparent)
                    .padding(AppTheme.size.normal),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = title,
                    style = AppTheme.typography.titleLarge,
                    color = when(type){
                        MessageDialogType.SUCCESS -> AppTheme.colorScheme.success
                        MessageDialogType.ERROR -> AppTheme.colorScheme.error
                        MessageDialogType.WARNING -> AppTheme.colorScheme.text
                    }
                )
                AppHorizontalDivider()

                Spacer(modifier = Modifier.height(AppTheme.size.normal))

                // Error Message
                Text(
                    text = message,
                    style = AppTheme.typography.paragraph,
                    textAlign = TextAlign.Center,
                    color = AppTheme.colorScheme.text
                )

                Spacer(modifier = Modifier.height(AppTheme.size.normal))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    // Dismiss Button
                    AppTextButton(
                        label = negativeButtonText,
                    ) {
                        onDismiss()
                    }

                    if (onPositiveButtonClick != null) {
                        Spacer(modifier = Modifier.width(AppTheme.size.small))
                        // Retry Button
                        AppTextButton(
                            label = positiveButtonText,
                        ) {
                            onPositiveButtonClick.invoke()
                        }
                    }
                }
            }
        }
    }
}
@Preview
@Composable
fun AppShowMessageDialogPreview(

) {
    AppShowMessageDialog(
        title = "Title",
        message = "Message",
        positiveButtonText = "Positive",
        negativeButtonText = "Negative",
        onPositiveButtonClick = {},
        type = MessageDialogType.WARNING,
    ) {

    }
}
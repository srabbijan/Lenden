package bd.srabbijan.lenden.core

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import bd.srabbijan.lenden.theme.AppTheme
import network.chaintech.sdpcomposemultiplatform.sdp

@Composable
fun AppLoadingDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(8.sdp),
            modifier = Modifier,
            colors = CardDefaults.cardColors(
                containerColor = AppTheme.colorScheme.cardBgColor
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.sdp
            )
        ) {
            Column(
                Modifier.padding(12.sdp)
            ) {
                CircularProgressIndicator(
                    strokeWidth = 4.sdp,
                    color = AppTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(8.sdp)
                )
            }
        }
    }
}


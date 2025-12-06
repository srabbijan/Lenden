package bd.srabbijan.lenden.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import bd.srabbijan.lenden.theme.AppTheme
import lenden.composeapp.generated.resources.Res
import lenden.composeapp.generated.resources.ic_arrow_left
import network.chaintech.sdpcomposemultiplatform.sdp
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbarWithBack(
    label: String,
    actions: @Composable (RowScope.() -> Unit)? = null,
    onClick: () -> Unit,
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(48.sdp)
            .clip(
                RoundedCornerShape(
                    bottomStart = 12.sdp,
                    bottomEnd = 12.sdp
                )
            )
            .background(AppTheme.colorScheme.primary),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { onClick() },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = AppTheme.colorScheme.primary,
                    contentColor = AppTheme.colorScheme.background,
                )
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_left),
                    contentDescription = "Localized description",
                    modifier = Modifier.size(24.sdp)
                )
            }
            Text(
                label,
                style = AppTheme.typography.paragraph.copy(
                    color = AppTheme.colorScheme.background
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        actions?.let {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppTheme.size.small)
            ) {
                actions()
            }
        }

    }
}

@Composable
fun AppToolbarWithBackPreview() {
    AppToolbarWithBack(
        label = "Title",
        onClick = {}
    )
}
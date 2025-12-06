package bd.srabbijan.lenden.core

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import bd.srabbijan.lenden.theme.AppTheme
import network.chaintech.sdpcomposemultiplatform.sdp


@Composable
fun AppHorizontalDivider(
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colorScheme.border,
    thickness: Dp = 1.sdp
) {
    HorizontalDivider(
        modifier = modifier.padding(vertical = 2.sdp),
        thickness = thickness,
        color = color
    )
}

@Composable
fun AppVerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colorScheme.border,
    thickness: Dp = 1.sdp
) {
    VerticalDivider(
        modifier = modifier
            .padding(horizontal = 2.sdp),
        thickness = thickness,
        color = color
    )
}

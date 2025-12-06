package bd.srabbijan.lenden.core

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import bd.srabbijan.lenden.theme.AppTheme
import network.chaintech.sdpcomposemultiplatform.sdp

@Composable
fun AppOutLineCard(
    modifier: Modifier = Modifier,
    shape: Shape = AppTheme.shape.container,
    colors: CardColors = CardDefaults.cardColors(
        containerColor = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.text
    ),
    border: BorderStroke = BorderStroke(
        width = 1.sdp,
        AppTheme.colorScheme.border.copy(
            alpha = .5f
        )
    ),
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(AppTheme.size.small),
        shape = shape,
        colors = colors,
        border = border
    ) {
        content(

        )
    }
}

@Composable
fun AppElevatedCard(
    modifier: Modifier = Modifier,
    shape: Shape = AppTheme.shape.container,
    colors: CardColors = CardDefaults.cardColors(
        containerColor = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.text
    ),
    content: @Composable () -> Unit
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.sdp
        ),
        shape = shape,
        colors = colors,
        modifier = modifier
            .padding(AppTheme.size.small)
            .fillMaxWidth(),
    ) {
        content()
    }
}

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.sdp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.cardBgColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.sdp
        )
    ) {
        content()
    }
}

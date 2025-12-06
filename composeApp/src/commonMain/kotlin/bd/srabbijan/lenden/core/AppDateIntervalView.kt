package bd.srabbijan.lenden.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import bd.srabbijan.lenden.theme.AppTheme
import lenden.composeapp.generated.resources.Res
import lenden.composeapp.generated.resources.ic_arrow_left
import lenden.composeapp.generated.resources.ic_arrow_right
import network.chaintech.sdpcomposemultiplatform.sdp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AppDateIntervalView(
    showingDate: String,
    offset: Int,
    onIntervalType: (TabKey) -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    content: @Composable (RowScope.() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppTheme.size.normal)
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomEnd = 8.dp, bottomStart = 8.dp))
            .background(AppTheme.colorScheme.primary.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            Tabs(
                tabList = listOf(
                    TabItem("Day", TabKey.TODAY),
                    TabItem("Month", TabKey.MONTH),
                    TabItem("Year", TabKey.YEAR),
                )
            ) {
                onIntervalType(it.key)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.sdp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onPrevious() }
            ){
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_left),
                    contentDescription = null
                )
            }

            Text(text = showingDate, style = AppTheme.typography.paragraph)

            IconButton(
                onClick = { onNext() }
            ){
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = if (offset < 0) Color.Black else AppTheme.colorScheme.border

                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.sdp),
            horizontalArrangement = Arrangement.SpaceAround,
        ){
            content?.let {
                content()
            }
        }
    }
}

@Preview
@Composable
fun PreviewAppDateIntervalView(){
    AppTheme {
        Surface(
            color = AppTheme.colorScheme.background
        ) {
            AppDateIntervalView(
                showingDate = "2025",
                offset = 0,
                onIntervalType = {},
                onNext = {},
                onPrevious = {}
            ) {}
        }
    }
}
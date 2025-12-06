package bd.srabbijan.lenden.core


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import bd.srabbijan.lenden.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Tabs(
    tabList: List<TabItem>,
    onTabSelected: (TabItem) -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(1) }

    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = AppTheme.colorScheme.primary,
        contentColor = Color.Black,
        modifier = Modifier
            .height(42.dp)
            .clip(RoundedCornerShape(50)),
        indicator = {},
        divider = {}
    ) {
        tabList.forEachIndexed { index, text ->
            val selected = selectedIndex == index
            Tab(
                modifier = if (selected) Modifier
                    .height(40.dp)
                    .padding(1.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        Color.White
                    )
                else Modifier
                    .height(40.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        Color.Transparent
                    ),
                selected = selected,
                onClick = {
                    selectedIndex = index
                    onTabSelected.invoke(text)
                },
                text = {
                    Text(
                        text = text.title,
                        color = if (selected) Color.Black else Color.White,
                        style = AppTheme.typography.labelLarge
                    )
                }
            )
        }
    }
}

data class TabItem(
    val title: String,
    val key: TabKey,
)

enum class TabKey {
    TODAY, MONTH, YEAR
}

@Composable
@Preview
fun TabsPreview() {
    AppTheme {
        Surface(color = AppTheme.colorScheme.background) {
            Tabs(
                tabList = listOf(
                    TabItem("Day", TabKey.TODAY),
                    TabItem("Month", TabKey.MONTH),
                    TabItem("Year", TabKey.YEAR),
                )
            ) {}
        }
    }
}
package bd.srabbijan.lenden.core


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import bd.srabbijan.lenden.theme.AppTheme
import lenden.composeapp.generated.resources.Res
import lenden.composeapp.generated.resources.ic_home
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AppNavigationBar(
    items: List<AppNavigationItem>,
    onClick: (Int) -> Unit,
) {
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }

    NavigationBar(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        containerColor = Color.White
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors().copy(
                    selectedIconColor = AppTheme.colorScheme.icon,
                    selectedTextColor = AppTheme.colorScheme.primary,
                    selectedIndicatorColor = AppTheme.colorScheme.primary,
                    unselectedIconColor = AppTheme.colorScheme.border,
                    unselectedTextColor = AppTheme.colorScheme.border,
                ),
                icon = {
                    Icon(
                        modifier = if (selectedItem == index) Modifier.size(22.dp) else Modifier.size(
                           20.dp
                        ),
                        painter = painterResource(item.icon),
                        contentDescription = item.label,
                    )
                },
                label = {
                    Text(
                        item.label,
                        style = AppTheme.typography.labelLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    onClick(index)
                }
            )
        }
    }

}

data class AppNavigationItem(
    val label: String,
    val icon: DrawableResource
)

@Preview
@Composable
fun PreviewAppNavigationBar(){
    AppTheme{
        AppNavigationBar(
            items = listOf(
                AppNavigationItem(
                    label = "Home",
                    icon = Res.drawable.ic_home
                ),
                AppNavigationItem(
                    label = "Home",
                    icon = Res.drawable.ic_home
                )
            )
        ){}
    }
}
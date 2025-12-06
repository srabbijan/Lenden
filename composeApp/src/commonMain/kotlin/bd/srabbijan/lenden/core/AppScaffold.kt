package bd.srabbijan.lenden.core


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import bd.srabbijan.lenden.theme.AppTheme

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    containerColor: Color = AppTheme.colorScheme.background,
    surfaceColor: Color = AppTheme.colorScheme.background,
    setPadding: Boolean = true,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    snackBarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            topBar = topBar,
            bottomBar = bottomBar,
            containerColor = containerColor,
            floatingActionButton = floatingActionButton,
            modifier = Modifier.fillMaxSize()
        ) { padding ->
            if (setPadding){
                Surface(
                    modifier = Modifier.padding(
                        top = padding.calculateTopPadding(),
                        bottom = padding.calculateBottomPadding(),
                        start = AppTheme.size.small,
                        end = AppTheme.size.small
                    ),
                    color = surfaceColor
                ) {
                    content(padding)
                }
            }
            else{
                content(padding)
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .safeContentPadding()
        ) {
            snackBarHost()
        }
    }
}
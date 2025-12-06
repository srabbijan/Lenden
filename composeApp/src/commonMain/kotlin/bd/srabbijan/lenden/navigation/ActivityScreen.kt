package bd.srabbijan.lenden.navigation

import androidx.compose.runtime.Composable

abstract class ActivityScreen<T> {

    @Composable
    abstract fun InitView(
        navKey: T,
        navController: NavController
    )
}
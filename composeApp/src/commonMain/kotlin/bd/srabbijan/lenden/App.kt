package bd.srabbijan.lenden

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import bd.srabbijan.lenden.core.AppNavigationBar
import bd.srabbijan.lenden.core.AppNavigationItem
import bd.srabbijan.lenden.core.AppScaffold
import bd.srabbijan.lenden.navigation.ActivityScreen
import bd.srabbijan.lenden.navigation.Animations.popTransitionSpec
import bd.srabbijan.lenden.navigation.Animations.transitionSpec
import bd.srabbijan.lenden.navigation.AppScreens
import bd.srabbijan.lenden.navigation.NavController
import bd.srabbijan.lenden.navigation.ScreenFactory
import bd.srabbijan.lenden.presentation.home.HomeScreen
import bd.srabbijan.lenden.presentation.home.HomeViewModel
import bd.srabbijan.lenden.presentation.settings.SettingsScreen
import bd.srabbijan.lenden.presentation.settings.SettingsViewModel
import bd.srabbijan.lenden.theme.AppTheme
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import lenden.composeapp.generated.resources.Res
import lenden.composeapp.generated.resources.ic_home
import lenden.composeapp.generated.resources.ic_settings
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    AppTheme {

        val applicationStack = rememberNavBackStack(
            configuration = SavedStateConfiguration {
                serializersModule = SerializersModule {
                    polymorphic(NavKey::class) {
                        subclass(AppScreens.Splash::class, AppScreens.Splash.serializer())
                        subclass(AppScreens.Dashboard::class, AppScreens.Dashboard.serializer())
                        subclass(AppScreens.AddCustomer::class, AppScreens.AddCustomer.serializer())
                        subclass(AppScreens.Transaction::class, AppScreens.Transaction.serializer())
                    }
                }
            }, AppScreens.Splash
        )
        val navController = remember { NavController(applicationStack) }

        NavDisplay(
            modifier = Modifier.fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .background(
                    color = AppTheme.colorScheme.background
                ),
            backStack = applicationStack,
            transitionSpec = { transitionSpec },
            popTransitionSpec = { popTransitionSpec },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
        ) { route ->
            NavEntry(route) {
                ScreenFactory.createScreen(route as AppScreens).InitView(
                    navKey = route,
                    navController = navController
                )
            }
        }
    }
}

class DashboardScreen : ActivityScreen<AppScreens.Dashboard>() {
    @Composable
    override fun InitView(
        navKey: AppScreens.Dashboard,
        navController: NavController
    ) {
        var selectedTab by androidx.compose.runtime.remember {
            androidx.compose.runtime.mutableStateOf(
                0
            )
        }

        AppScaffold(
            bottomBar = {
                AppNavigationBar(
                    items = listOf(
                        AppNavigationItem(
                            label = "Home",
                            icon = Res.drawable.ic_home,
                        ),
                        AppNavigationItem(
                            label = "Settings",
                            icon = Res.drawable.ic_settings,
                        ),

                        )
                ) {
                    selectedTab = it
                }

            }
        ) {
            Box {
                if (selectedTab == 0) {
                    val viewModel = koinViewModel<HomeViewModel>()
                    HomeScreen(
                        viewModel = viewModel,
                        onNavigateToAddCustomer = {
                            navController.navigate(AppScreens.AddCustomer)
                        },
                        onNavigateToTransaction = {
                            navController.navigate(AppScreens.Transaction(it))
                        }
                    )
                } else {
                    val viewModel = koinViewModel<SettingsViewModel>()
                    SettingsScreen(viewModel = viewModel)
                }
            }
        }
    }

}

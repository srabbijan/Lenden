package bd.srabbijan.lenden.navigation

import bd.srabbijan.lenden.DashboardScreen
import bd.srabbijan.lenden.presentation.addcustomer.AddCustomerScreen
import bd.srabbijan.lenden.presentation.splash.SplashScreen
import bd.srabbijan.lenden.presentation.transaction.TransactionScreen


object ScreenFactory {
    @Suppress("UNCHECKED_CAST")
    fun createScreen(appScreen: AppScreens): ActivityScreen<AppScreens> {
        return when (appScreen) {
            AppScreens.Splash -> SplashScreen() as ActivityScreen<AppScreens>
            AppScreens.Dashboard -> DashboardScreen() as ActivityScreen<AppScreens>
            AppScreens.AddCustomer -> AddCustomerScreen() as ActivityScreen<AppScreens>
            is AppScreens.Transaction -> TransactionScreen() as ActivityScreen<AppScreens>
        }
    }
}
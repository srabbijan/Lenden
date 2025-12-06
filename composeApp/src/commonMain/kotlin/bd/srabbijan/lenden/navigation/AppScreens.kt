package bd.srabbijan.lenden.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class AppScreens : NavKey {
    @Serializable
    data object Splash :  AppScreens()

    @Serializable
    data object Dashboard : AppScreens()

    @Serializable
    data object AddCustomer : AppScreens()

    @Serializable
    data class Transaction(val customerId: Long) : AppScreens()
}
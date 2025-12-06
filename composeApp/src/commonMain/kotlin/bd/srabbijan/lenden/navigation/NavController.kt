package bd.srabbijan.lenden.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey


class NavController(
    private val backStack: NavBackStack<NavKey>,
) {

    fun back() {
        if (backStack.size > 1)
            backStack.removeLastOrNull()
    }

    fun navigate(navKey: NavKey, finish: Boolean = false) {
        backStack.add(navKey)
        if (finish)
            backStack.removeAt(backStack.size - 2)
    }

}
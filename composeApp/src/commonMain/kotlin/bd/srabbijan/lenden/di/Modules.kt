package bd.srabbijan.lenden.di

import bd.srabbijan.lenden.presentation.addcustomer.AddCustomerViewModel
import bd.srabbijan.lenden.presentation.home.HomeViewModel
import bd.srabbijan.lenden.presentation.settings.SettingsViewModel
import bd.srabbijan.lenden.presentation.splash.SplashViewModel
import bd.srabbijan.lenden.presentation.transaction.TransactionViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {
    viewModel { SplashViewModel() }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { SettingsViewModel() }
    viewModel { AddCustomerViewModel(get(), get()) }
    viewModel { (customerId: Long) -> TransactionViewModel(customerId, get(), get(), get(), get()) }
}

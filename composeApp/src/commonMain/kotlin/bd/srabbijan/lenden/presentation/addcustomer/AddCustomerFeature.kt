package bd.srabbijan.lenden.presentation.addcustomer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import bd.srabbijan.lenden.core.AppAmountInput
import bd.srabbijan.lenden.core.AppLoadingDialog
import bd.srabbijan.lenden.core.AppScaffold
import bd.srabbijan.lenden.core.AppShowMessageDialog
import bd.srabbijan.lenden.core.AppTextInput
import bd.srabbijan.lenden.core.AppToolbarWithBack
import bd.srabbijan.lenden.core.BaseViewModel
import bd.srabbijan.lenden.core.MessageDialogType
import bd.srabbijan.lenden.core.PhoneInput
import bd.srabbijan.lenden.core.PrimaryButton
import bd.srabbijan.lenden.core.UiEffect
import bd.srabbijan.lenden.core.UiEvent
import bd.srabbijan.lenden.core.UiState
import bd.srabbijan.lenden.core.UiText
import bd.srabbijan.lenden.core.getCurrentDateTime
import bd.srabbijan.lenden.data.local.dao.CustomerDao
import bd.srabbijan.lenden.data.local.dao.TransactionDao
import bd.srabbijan.lenden.data.local.entity.CustomerEntity
import bd.srabbijan.lenden.data.local.entity.TransactionEntity
import bd.srabbijan.lenden.navigation.ActivityScreen
import bd.srabbijan.lenden.navigation.AppScreens
import bd.srabbijan.lenden.navigation.NavController
import bd.srabbijan.lenden.theme.AppTheme
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lenden.composeapp.generated.resources.Res
import lenden.composeapp.generated.resources.ic_user_plus
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel


data class AddCustomerState(
    /*
    * Loan Type
    * true -> loan given
    * false -> loan taken
     */
    val isLoanGiven: Boolean = true,

    val personName: String = "",
    val personNumber: String = "",
    val amount: String = "",

    val customer: CustomerEntity? = null
) : UiState

sealed interface AddCustomerEvent : UiEvent {
    data class OnCustomerEdit(val id: String) : AddCustomerEvent
    data object OnSave : AddCustomerEvent
    data class OnNameChange(val name: String) : AddCustomerEvent
    data class OnNumberChange(val number: String) : AddCustomerEvent
    data class OnAmountChange(val amount: String) : AddCustomerEvent
    data class OnLoanTypeChange(val isLoanGiven: Boolean) : AddCustomerEvent
}

sealed interface AddCustomerEffect : UiEffect {
    data object NavigateBack : AddCustomerEffect
}

class AddCustomerViewModel(
    private val customerDao: CustomerDao,
    private val transactionDao: TransactionDao
) : BaseViewModel<AddCustomerState, AddCustomerEvent, AddCustomerEffect>(AddCustomerState()) {

    override fun onEvent(event: AddCustomerEvent) {
        when (event) {
            is AddCustomerEvent.OnSave -> {
                saveCustomer()
            }

            is AddCustomerEvent.OnNameChange -> {
                setState { copy(personName = event.name) }
            }

            is AddCustomerEvent.OnNumberChange -> {
                setState { copy(personNumber = event.number) }
            }

            is AddCustomerEvent.OnAmountChange -> {
                setState { copy(amount = event.amount) }
            }

            is AddCustomerEvent.OnCustomerEdit -> {
//                fetchById(action.id)
            }

            is AddCustomerEvent.OnLoanTypeChange -> {
                setState { copy(isLoanGiven = event.isLoanGiven) }
            }
        }
    }

    private fun saveCustomer() {
        val currentState = uiState.value
        if (currentState.personName.isBlank()) return

        viewModelScope.launch {
            val balance = currentState.amount.toDoubleOrNull() ?: 0.0
            val customerId = customerDao.insertCustomer(
                CustomerEntity(
                    name = currentState.personName,
                    phoneNumber = currentState.personNumber,
                    balance = if (currentState.isLoanGiven) balance else balance * -1,
                    lastActivity = getCurrentDateTime()
                )
            )
            
            // Create a transaction if amount is not zero
            if (balance != 0.0) {
                transactionDao.insertTransaction(
                    TransactionEntity(
                        customerId = customerId,
                        amount = balance,
                        type = if (currentState.isLoanGiven) "GIVEN" else "TAKEN",
                        note = "Initial balance",
                        date = getCurrentDateTime()
                    )
                )
            }
            
            setEffect { AddCustomerEffect.NavigateBack }
        }
    }
}

class AddCustomerScreen : ActivityScreen<AppScreens.AddCustomer>() {
    @Composable
    override fun InitView(
        navKey: AppScreens.AddCustomer,
        navController: NavController
    ) {
        val viewModel: AddCustomerViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsState()

        androidx.compose.runtime.LaunchedEffect(Unit) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    AddCustomerEffect.NavigateBack -> {
                        navController.back()
                    }
                }
            }
        }

        AppScaffold(
            topBar = {
                AppToolbarWithBack(
                    label = if (uiState.customer != null) "ব্যক্তি আপডেট" else "নতুন ব্যক্তি যুক্ত"
                ) {
                    navController.back()
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(AppTheme.size.normal),
                verticalArrangement = Arrangement.spacedBy(AppTheme.size.small)
            ) {

                AppTextInput(
                    label = "নাম",
                    hint = "নাম লিখুন",
                    text = uiState.personName,
                    leadingIcon = painterResource(Res.drawable.ic_user_plus),
                    isMandatory = true
                ) {
                    viewModel.onEvent(AddCustomerEvent.OnNameChange(it))
                }

                PhoneInput(
                    phone = uiState.personNumber,
                    label = "মোবাইল নাম্বার",
                ) {
                    viewModel.onEvent(AddCustomerEvent.OnNumberChange(it))
                }

                AppAmountInput(
                    label = "বর্তমান ব্যালেন্স",
                    hint = "কত টাকা পাবেন / দিবেন ?",
                    amount = uiState.amount
                ) {
                    viewModel.onEvent(AddCustomerEvent.OnAmountChange(it))
                }
                if (uiState.amount.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                colors = RadioButtonColors(
                                    selectedColor = AppTheme.colorScheme.primary,
                                    unselectedColor = AppTheme.colorScheme.border,
                                    disabledSelectedColor = AppTheme.colorScheme.border,
                                    disabledUnselectedColor = AppTheme.colorScheme.border,
                                ),
                                onClick = {
                                    viewModel.onEvent(AddCustomerEvent.OnLoanTypeChange(true))
                                },
                                selected = uiState.isLoanGiven
                            )
                            Text(
                                "পাবো"
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                colors = RadioButtonColors(
                                    selectedColor = AppTheme.colorScheme.primary,
                                    unselectedColor = AppTheme.colorScheme.border,
                                    disabledSelectedColor = AppTheme.colorScheme.border,
                                    disabledUnselectedColor = AppTheme.colorScheme.border,
                                ),
                                onClick = {
                                    viewModel.onEvent(AddCustomerEvent.OnLoanTypeChange(false))
                                },
                                selected = !uiState.isLoanGiven
                            )
                            Text(
                                "দিবো"
                            )
                        }
                    }
                }


                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    PrimaryButton(
                        isEnable = uiState.personName.isNotEmpty(),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth()
                            .padding(horizontal = AppTheme.size.large),
                        label = if (uiState.customer != null) "আপডেট" else "সেভ",
                    ) {
                        viewModel.onEvent(AddCustomerEvent.OnSave)
                    }
                }

            }
        }
    }

}


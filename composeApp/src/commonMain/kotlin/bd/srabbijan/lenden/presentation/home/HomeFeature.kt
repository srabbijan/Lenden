package bd.srabbijan.lenden.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import bd.srabbijan.lenden.core.AppElevatedCard
import bd.srabbijan.lenden.core.AppFloatingButton
import bd.srabbijan.lenden.core.AppIconButton
import bd.srabbijan.lenden.core.AppOutLineCard
import bd.srabbijan.lenden.core.AppScaffold
import bd.srabbijan.lenden.core.AppSearchField
import bd.srabbijan.lenden.core.BaseViewModel
import bd.srabbijan.lenden.core.UiEffect
import bd.srabbijan.lenden.core.UiEvent
import bd.srabbijan.lenden.core.UiState
import bd.srabbijan.lenden.core.toCurrencyFormat
import bd.srabbijan.lenden.core.toPositive
import bd.srabbijan.lenden.data.local.dao.CustomerDao
import bd.srabbijan.lenden.data.local.dao.TransactionDao
import bd.srabbijan.lenden.data.local.entity.CustomerEntity
import bd.srabbijan.lenden.presentation.home.components.LoanPersonList
import bd.srabbijan.lenden.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import lenden.composeapp.generated.resources.Res
import lenden.composeapp.generated.resources.ic_filter
import lenden.composeapp.generated.resources.ic_menu
import lenden.composeapp.generated.resources.ic_user_plus
import network.chaintech.sdpcomposemultiplatform.sdp
import org.jetbrains.compose.resources.painterResource

enum class FilterType {
    ALL, GIVEN, TAKEN
}

data class HomeState(
    val customers: List<CustomerEntity> = emptyList(),
    val totalGiven: Double = 0.0,
    val totalTaken: Double = 0.0,
    val searchQuery: String = "",
    val filterType: FilterType = FilterType.ALL,
    val showFilterMenu: Boolean = false,
    val customerToDelete: CustomerEntity? = null,
    val showDeleteConfirmation: Boolean = false,
    val swipeResetId: Int = 0
) : UiState

sealed interface HomeEvent : UiEvent {
    data object OnAddCustomerClick : HomeEvent
    data class OnCustomerClick(val customerId: Long) : HomeEvent
    data class OnSearchQueryChange(val query: String) : HomeEvent
    data class OnFilterTypeChange(val filterType: FilterType) : HomeEvent
    data object OnFilterMenuToggle : HomeEvent
    data class OnDeleteCustomerRequest(val customer: CustomerEntity) : HomeEvent
    data object OnDeleteConfirmation : HomeEvent
    data object OnDeleteDismiss : HomeEvent
}

sealed interface HomeEffect : UiEffect {
    data object NavigateToAddCustomer : HomeEffect
    data class NavigateToTransaction(val customerId: Long) : HomeEffect
}

class HomeViewModel(
    private val customerDao: CustomerDao,
    private val transactionDao: TransactionDao
) : BaseViewModel<HomeState, HomeEvent, HomeEffect>(HomeState()) {

    init {
        loadCustomers()
        loadTotals()
    }

    private fun loadCustomers() {
        viewModelScope.launch {
            getCustomerFlow().collectLatest { customers ->
                setState { copy(customers = customers) }
            }
        }
    }

    private fun loadTotals() {
        viewModelScope.launch {
            customerDao.getTotalGiven().collectLatest { total ->
                setState { copy(totalGiven = total) }
            }
        }
        viewModelScope.launch {
            customerDao.getTotalTaken().collectLatest { total ->
                setState { copy(totalTaken = total) }
            }
        }
    }

    private fun getCustomerFlow(): Flow<List<CustomerEntity>> {
        val query = uiState.value.searchQuery
        val filter = uiState.value.filterType
        
        return when {
            // No search query
            query.isBlank() -> {
                when (filter) {
                    FilterType.ALL -> customerDao.getAllCustomers()
                    FilterType.GIVEN -> customerDao.getCustomersWithPositiveBalance()
                    FilterType.TAKEN -> customerDao.getCustomersWithNegativeBalance()
                }
            }
            // With search query
            else -> {
                when (filter) {
                    FilterType.ALL -> customerDao.searchCustomers(query)
                    FilterType.GIVEN -> customerDao.searchCustomersWithPositiveBalance(query)
                    FilterType.TAKEN -> customerDao.searchCustomersWithNegativeBalance(query)
                }
            }
        }
    }

    override fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.OnAddCustomerClick -> setEffect { HomeEffect.NavigateToAddCustomer }
            is HomeEvent.OnCustomerClick -> {
                setState { copy(swipeResetId = swipeResetId + 1) }
                setEffect { HomeEffect.NavigateToTransaction(event.customerId) }
            }
            is HomeEvent.OnSearchQueryChange -> {
                setState { copy(searchQuery = event.query) }
                loadCustomers()
            }
            is HomeEvent.OnFilterTypeChange -> {
                setState { copy(filterType = event.filterType, showFilterMenu = false) }
                loadCustomers()
            }
            HomeEvent.OnFilterMenuToggle -> {
                setState { copy(showFilterMenu = !showFilterMenu) }
            }
            is HomeEvent.OnDeleteCustomerRequest -> {
                setState { 
                    copy(
                        customerToDelete = event.customer,
                        showDeleteConfirmation = true
                    )
                }
            }
            HomeEvent.OnDeleteConfirmation -> {
                val customer = uiState.value.customerToDelete
                if (customer != null) {
                    viewModelScope.launch {
                        // Delete all transactions first
                        transactionDao.deleteAllTransactionsForCustomer(customer.id)
                        // Then delete the customer
                        customerDao.deleteCustomer(customer.id)
                        // Reset state
                        setState { 
                            copy(
                                customerToDelete = null,
                                showDeleteConfirmation = false
                            )
                        }
                    }
                }
            }
            HomeEvent.OnDeleteDismiss -> {
                setState { 
                    copy(
                        customerToDelete = null,
                        showDeleteConfirmation = false,
                        swipeResetId = swipeResetId + 1
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToAddCustomer: () -> Unit,
    onNavigateToTransaction: (Long) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                HomeEffect.NavigateToAddCustomer -> onNavigateToAddCustomer()
                is HomeEffect.NavigateToTransaction -> onNavigateToTransaction(effect.customerId)
            }
        }
    }
    val searchResultsListState = rememberLazyListState()

    LaunchedEffect(state.customers) {
        searchResultsListState.animateScrollToItem(0)
    }

    AppScaffold(
        setPadding = false,
        floatingActionButton = {
            AppFloatingButton(
                icon = painterResource(Res.drawable.ic_user_plus)
            ) {
                viewModel.onEvent(HomeEvent.OnAddCustomerClick)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppElevatedCard {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppTheme.size.normal),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("টাকা পাবো")
                        Text(
                            text = state.totalGiven.toCurrencyFormat(),
                            style = AppTheme.typography.titleNormal,
                            color = AppTheme.colorScheme.success
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("টাকা দিবো")
                        Text(
                            text = state.totalTaken.toPositive().toCurrencyFormat(),
                            style = AppTheme.typography.titleNormal,
                            color = AppTheme.colorScheme.error
                        )
                    }
                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.size.small),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppSearchField(
                    modifier = Modifier.weight(1f),
                    value = state.searchQuery
                ) {
                    viewModel.onEvent(HomeEvent.OnSearchQueryChange(it))
                }
                
                androidx.compose.foundation.layout.Box {
                    AppOutLineCard(
                        modifier = Modifier.size(48.sdp),
                    ) {
                        AppIconButton(
                            modifier = Modifier
                                .fillMaxSize()
                                .size(24.sdp),
                            painter = painterResource(Res.drawable.ic_filter)
                        ) {
                            viewModel.onEvent(HomeEvent.OnFilterMenuToggle)
                        }
                    }
                    
                    androidx.compose.material3.DropdownMenu(
                        expanded = state.showFilterMenu,
                        onDismissRequest = { viewModel.onEvent(HomeEvent.OnFilterMenuToggle) }
                    ) {
                        androidx.compose.material3.DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("সব")
                                    if (state.filterType == FilterType.ALL) {
                                        androidx.compose.foundation.layout.Spacer(Modifier.weight(1f))
                                        Text("✓", color = AppTheme.colorScheme.primary)
                                    }
                                }
                            },
                            onClick = { viewModel.onEvent(HomeEvent.OnFilterTypeChange(FilterType.ALL)) }
                        )
                        androidx.compose.material3.DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("টাকা পাবো")
                                    if (state.filterType == FilterType.GIVEN) {
                                        androidx.compose.foundation.layout.Spacer(Modifier.weight(1f))
                                        Text("✓", color = AppTheme.colorScheme.primary)
                                    }
                                }
                            },
                            onClick = { viewModel.onEvent(HomeEvent.OnFilterTypeChange(FilterType.GIVEN)) }
                        )
                        androidx.compose.material3.DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("টাকা দিবো")
                                    if (state.filterType == FilterType.TAKEN) {
                                        androidx.compose.foundation.layout.Spacer(Modifier.weight(1f))
                                        Text("✓", color = AppTheme.colorScheme.primary)
                                    }
                                }
                            },
                            onClick = { viewModel.onEvent(HomeEvent.OnFilterTypeChange(FilterType.TAKEN)) }
                        )
                    }
                }


            }

            LoanPersonList(
                dataList = state.customers,
                swipeResetId = state.swipeResetId,
                onItemClick = {
                    viewModel.onEvent(HomeEvent.OnCustomerClick(it.id))
                },
                onDeleteClick = {
                    viewModel.onEvent(HomeEvent.OnDeleteCustomerRequest(it))
                },
                modifier = Modifier.fillMaxSize(),
                scrollState = searchResultsListState
            )

           /* if (state.isLoading) {
                EmptyStateScreen(
                    type = EmptyStateType.Loading
                )
            } else {
                when {
                    state.errorMessage != UiText.Idle -> {
                        Text(
                            text = state.errorMessage.asString(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    state.dataList.isEmpty() -> {
                        EmptyStateScreen(
                            title = "কাস্টমার খুজে পাওয়া যায় নি।",
                            message = "কাস্টমার যুক্ত করতে নিচের + বাটনে ক্লিক করুন",
                            type = EmptyStateType.NoData
                        )
                    }

                    else -> {
                        LoanPersonList(
                            dataList = state.customers,
                            onItemClick = {
                                viewModel.onEvent(HomeEvent.OnCustomerClick(it.id))
                            },
                            modifier = Modifier.fillMaxSize(),
                            scrollState = searchResultsListState
                        )
                    }
                }
            }*/
        }
        
        // Delete Confirmation Dialog
        if (state.showDeleteConfirmation && state.customerToDelete != null) {
            val customer = state.customerToDelete
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { viewModel.onEvent(HomeEvent.OnDeleteDismiss) },
                title = { Text("কাস্টমার মুছে ফেলুন") },
                text = {
                    Column {
                        Text("আপনি কি নিশ্চিত যে আপনি '${customer?.name}' এবং তার সমস্ত লেনদেন মুছে ফেলতে চান?")
                        androidx.compose.foundation.layout.Spacer(Modifier.padding(AppTheme.size.small))
                        Text(
                            "এই কাজটি পূর্বাবস্থায় ফেরানো যাবে না।",
                            style = AppTheme.typography.labelSmall,
                            color = AppTheme.colorScheme.error
                        )
                    }
                },
                confirmButton = {
                    androidx.compose.material3.TextButton(
                        onClick = { viewModel.onEvent(HomeEvent.OnDeleteConfirmation) }
                    ) {
                        Text("মুছে ফেলুন", color = AppTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(
                        onClick = { viewModel.onEvent(HomeEvent.OnDeleteDismiss) }
                    ) {
                        Text("বাতিল করুন")
                    }
                }
            )
        }
    }

}

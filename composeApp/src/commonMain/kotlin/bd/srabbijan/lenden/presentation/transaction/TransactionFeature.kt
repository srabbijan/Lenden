package bd.srabbijan.lenden.presentation.transaction

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import bd.srabbijan.lenden.core.AppAmountInput
import bd.srabbijan.lenden.core.AppDateIntervalView
import bd.srabbijan.lenden.core.AppElevatedCard
import bd.srabbijan.lenden.core.AppIconButton
import bd.srabbijan.lenden.core.AppScaffold
import bd.srabbijan.lenden.core.AppTextInput
import bd.srabbijan.lenden.core.AppToolbarWithBack
import bd.srabbijan.lenden.core.BaseViewModel
import bd.srabbijan.lenden.core.ConfirmationDialog
import bd.srabbijan.lenden.core.EmptyStateScreen
import bd.srabbijan.lenden.core.EmptyStateType
import bd.srabbijan.lenden.core.FORMAT_dd_MMMM_yyyy
import bd.srabbijan.lenden.core.FORMAT_dd_MMM_yy
import bd.srabbijan.lenden.core.IconChip
import bd.srabbijan.lenden.core.PdfGenerator
import bd.srabbijan.lenden.core.PdfViewer
import bd.srabbijan.lenden.core.PrimaryButton
import bd.srabbijan.lenden.core.TabKey
import bd.srabbijan.lenden.core.UiEffect
import bd.srabbijan.lenden.core.UiEvent
import bd.srabbijan.lenden.core.UiState
import bd.srabbijan.lenden.core.convertApiDateToUIDate
import bd.srabbijan.lenden.core.getCurrentDateTime
import bd.srabbijan.lenden.core.getDayIntervalDateTime
import bd.srabbijan.lenden.core.getMonthIntervalDateTime
import bd.srabbijan.lenden.core.getYearIntervalDateTime
import bd.srabbijan.lenden.core.isPositive
import bd.srabbijan.lenden.core.toCurrencyFormat
import bd.srabbijan.lenden.core.toPositive
import bd.srabbijan.lenden.data.local.dao.CustomerDao
import bd.srabbijan.lenden.data.local.dao.TransactionDao
import bd.srabbijan.lenden.data.local.entity.CustomerEntity
import bd.srabbijan.lenden.data.local.entity.TransactionEntity
import bd.srabbijan.lenden.navigation.ActivityScreen
import bd.srabbijan.lenden.navigation.AppScreens
import bd.srabbijan.lenden.navigation.NavController
import bd.srabbijan.lenden.presentation.transaction.TransactionEvent
import bd.srabbijan.lenden.theme.AppTheme
import bd.srabbijan.lenden.presentation.transaction.components.LoanHistoryList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import lenden.composeapp.generated.resources.Res
import lenden.composeapp.generated.resources.ic_dots_vertical
import lenden.composeapp.generated.resources.ic_download
import lenden.composeapp.generated.resources.ic_trash
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

data class TransactionState(
    val customer: CustomerEntity? = null,
    val transactions: List<TransactionEntity> = emptyList(),
    val amount: String = "",
    val note: String = "",
    val isGiven: Boolean = true, // true = Given, false = Taken
    val showDeleteConfirmDialog: Boolean = false
) : UiState

sealed interface TransactionEvent : UiEvent {
    data class OnAmountChanged(val amount: String) : TransactionEvent
    data class OnNoteChanged(val note: String) : TransactionEvent
    data class OnTypeChanged(val isGiven: Boolean) : TransactionEvent
    data object OnAddTransactionClick : TransactionEvent

    data object NextInterval : TransactionEvent
    data object PreInterval : TransactionEvent
    data class IntervalType(val value: TabKey) : TransactionEvent
    
    data object OnDeleteAllClick : TransactionEvent
    data object OnDeleteConfirmed : TransactionEvent
    data object OnDeleteCancelled : TransactionEvent
    data object OnDownloadPdfClick : TransactionEvent
}

sealed interface TransactionEffect : UiEffect

class TransactionViewModel(
    private val customerId: Long,
    private val customerDao: CustomerDao,
    private val transactionDao: TransactionDao,
    private val pdfGenerator: PdfGenerator,
    private val pdfViewer: PdfViewer
) : BaseViewModel<TransactionState, TransactionEvent, TransactionEffect>(TransactionState()) {

    //    private val _state = MutableStateFlow(ExpenseState())
//    val uiState = _state.onStart {
//        generateDate()
//    }.stateIn(
//        viewModelScope,
//        SharingStarted.WhileSubscribed(5000L),
//        _state.value
//    )
    private val intervalType = mutableStateOf(TabKey.MONTH)
    val offset = mutableIntStateOf(0)
    val showingDate = mutableStateOf("")

    private var transactionJob: kotlinx.coroutines.Job? = null

    init {
        viewModelScope.launch {
            customerDao.getCustomerById(customerId)?.let { customer ->
                setState { copy(customer = customer) }
            }
        }
        generateDate()
    }

    override fun onEvent(event: TransactionEvent) {
        when (event) {
            is TransactionEvent.OnAmountChanged -> setState { copy(amount = event.amount) }
            is TransactionEvent.OnNoteChanged -> setState { copy(note = event.note) }
            is TransactionEvent.OnTypeChanged -> setState { copy(isGiven = event.isGiven) }
            TransactionEvent.OnAddTransactionClick -> addTransaction()
            is TransactionEvent.IntervalType -> {
                offset.intValue = 0
                intervalType.value = event.value
                generateDate()
            }

            TransactionEvent.NextInterval -> {
                if (offset.intValue < 0) {
                    offset.intValue++
                    generateDate()
                }
            }

            TransactionEvent.PreInterval -> {
                offset.intValue--
                generateDate()
            }
            
            TransactionEvent.OnDeleteAllClick -> {
                setState { copy(showDeleteConfirmDialog = true) }
            }
            
            TransactionEvent.OnDeleteConfirmed -> {
                deleteAllTransactions()
                setState { copy(showDeleteConfirmDialog = false) }
            }
            
            TransactionEvent.OnDeleteCancelled -> {
                setState { copy(showDeleteConfirmDialog = false) }
            }
            
            TransactionEvent.OnDownloadPdfClick -> {
                downloadPdf()
            }
        }
    }

    // time interval Calculation


    private fun generateDate() {
        when (intervalType.value) {
            TabKey.TODAY -> {
                val date = offset.intValue.getDayIntervalDateTime()
                showingDate.value =
                    "${date.first?.convertApiDateToUIDate(outFormat = FORMAT_dd_MMM_yy)}"
                if (date.first != null && date.second != null) {
                    fetchActivityData(date.first!!, date.second!!)
                }
            }

            TabKey.MONTH -> {
                val date = offset.intValue.getMonthIntervalDateTime()
                showingDate.value =
                    "${date.first.convertApiDateToUIDate(outFormat = FORMAT_dd_MMM_yy)} - ${
                        date.second.convertApiDateToUIDate(outFormat = FORMAT_dd_MMM_yy)
                    }"
                fetchActivityData(date.first, date.second)
            }

            TabKey.YEAR -> {
                val date = offset.intValue.getYearIntervalDateTime()
                showingDate.value =
                    "${date.first.convertApiDateToUIDate(outFormat = FORMAT_dd_MMM_yy)} - ${
                        date.second.convertApiDateToUIDate(outFormat = FORMAT_dd_MMM_yy)
                    }"
                fetchActivityData(date.first, date.second)
            }
        }
    }

    private fun fetchActivityData(startDate: String, endDate: String) {
        // Cancel previous transaction collection job if exists
        transactionJob?.cancel()

        // Start new transaction collection with date filter
        transactionJob = viewModelScope.launch {
            transactionDao.getTransactionsForCustomerByDateRange(
                customerId = customerId,
                startDate = startDate,
                endDate = endDate
            ).collectLatest { filteredTransactions ->
                setState { copy(transactions = filteredTransactions) }
            }
        }
    }


    private fun addTransaction() {
        val currentState = uiState.value
        val amountVal = currentState.amount.toDoubleOrNull() ?: return
        val customer = currentState.customer ?: return

        viewModelScope.launch {
            val type = if (currentState.isGiven) "GIVEN" else "TAKEN"
            // Logic: Given means I gave money, so balance decreases (more negative/less positive). Taken means I took money, so balance increases.
            // Wait, let's stick to the user request: "current amount (taken/given)".
            // Let's assume: Positive balance = Customer owes me (I gave). Negative balance = I owe customer (I took).
            // If I GIVE money, they owe me more -> Balance increases.
            // If I TAKE money, they owe me less -> Balance decreases.
            // Let's re-read: "current amount (taken/given)".
            // Usually in these apps:
            // "You Gave" -> You lent money -> +Balance (Green)
            // "You Got" -> You borrowed/received money -> -Balance (Red)

            // Let's define:
            // GIVEN: I gave money to customer. Customer owes me. Balance increases.
            // TAKEN: I took money from customer. Customer owes me less. Balance decreases.

            val transactionAmount = if (currentState.isGiven) amountVal else -amountVal
            val newBalance = customer.balance + transactionAmount

            val transaction = TransactionEntity(
                customerId = customerId,
                amount = amountVal,
                type = type,
                note = currentState.note,
                date = getCurrentDateTime()
            )

            transactionDao.insertTransaction(transaction)
            customerDao.updateCustomer(
                customer.copy(
                    balance = newBalance,
                    lastActivity = getCurrentDateTime()
                )
            )

            // Refresh customer
            val updatedCustomer = customerDao.getCustomerById(customerId)
            setState { copy(customer = updatedCustomer, amount = "", note = "") }
        }
    }
    
    private fun deleteAllTransactions() {
        val customer = uiState.value.customer ?: return
        
        viewModelScope.launch {
            // Delete all transactions for this customer
            transactionDao.deleteAllTransactionsForCustomer(customerId)
            
            // Reset customer balance to 0
            customerDao.updateCustomer(
                customer.copy(
                    balance = 0.0,
                    lastActivity = getCurrentDateTime()
                )
            )
            
            // Refresh customer
            val updatedCustomer = customerDao.getCustomerById(customerId)
            setState { copy(customer = updatedCustomer) }
        }
    }
    
    private fun downloadPdf() {
        val customer = uiState.value.customer ?: return
        val transactions = uiState.value.transactions
        
        viewModelScope.launch {
            try {
                val totalGiven = transactions.filter { it.type == "GIVEN" }.sumOf { it.amount }
                val totalTaken = transactions.filter { it.type == "TAKEN" }.sumOf { it.amount }
                
                val filePath = pdfGenerator.generateTransactionPdf(
                    customerName = customer.name,
                    balance = customer.balance,
                    transactions = transactions,
                    dateRange = showingDate.value,
                    totalGiven = totalGiven,
                    totalTaken = totalTaken
                )
                
                // Open the PDF after generation
                pdfViewer.openPdf(filePath)
                
                println("PDF generated and opened: $filePath")
            } catch (e: Exception) {
                // TODO: Show error message to user
                println("Error generating PDF: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}

class TransactionScreen : ActivityScreen<AppScreens.Transaction>() {
    @Composable
    override fun InitView(
        navKey: AppScreens.Transaction,
        navController: NavController
    ) {
        val viewModel = koinViewModel<TransactionViewModel> { parametersOf(navKey.customerId) }
        val state by viewModel.uiState.collectAsState()

        val searchResultsListState = rememberLazyListState()

        LaunchedEffect(state.transactions) {
            searchResultsListState.animateScrollToItem(0)
        }

        AppScaffold(
            topBar = {
                AppToolbarWithBack(
                    label = state.customer?.name ?: "লেনদেন সমূহ",
                    actions = {
                        IconButton(
                            modifier = Modifier.clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            onClick = {
                                viewModel.onEvent(TransactionEvent.OnDownloadPdfClick)
                            }
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_download),
                                contentDescription = null,
                                tint = AppTheme.colorScheme.icon
                            )
                        }

                        IconButton(
                            modifier = Modifier.clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            onClick = {
                                viewModel.onEvent(TransactionEvent.OnDeleteAllClick)
                            }
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_trash),
                                contentDescription = null,
                                tint = AppTheme.colorScheme.icon
                            )
                        }
                    }
                ) {
                    navController.back()
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                if (state.isLoading) {
//                    EmptyStateScreen(
//                        type = EmptyStateType.Loading
//                    )
//                }
//                if (state.errorMessage != UiText.Idle) {
//                    Text(
//                        text = state.errorMessage.asString(),
//                        textAlign = TextAlign.Center,
//                        style = MaterialTheme.typography.headlineSmall,
//                        color = MaterialTheme.colorScheme.error
//                    )
//                }

                AppDateIntervalView(
                    showingDate = viewModel.showingDate.value,
                    offset = viewModel.offset.intValue,
                    onIntervalType = {
                        viewModel.onEvent(TransactionEvent.IntervalType(it))
                    },
                    onNext = {
                        viewModel.onEvent(TransactionEvent.NextInterval)
                    },
                    onPrevious = {
                        viewModel.onEvent(TransactionEvent.PreInterval)
                    }
                ) {
                    state.customer?.let {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = it.balance.toPositive().toCurrencyFormat(),
                                style = AppTheme.typography.titleNormal,
                                color = if (it.balance.isPositive()) AppTheme.colorScheme.success else AppTheme.colorScheme.error
                            )
                            Text(
                                text = if (it.balance.isPositive()) " /পাবো" else " /দিবো",
                                style = AppTheme.typography.labelSmall
                            )
                        }
                    }
                }


                if (state.transactions.isEmpty()) {
                    EmptyStateScreen(
                        modifier = Modifier.weight(1f),
                        type = EmptyStateType.NoData,
                        title = "No Data Found with this date range"
                    )
                } else {
                    AppElevatedCard {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(AppTheme.size.normal),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                text = "তারিখ",
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Start
                            )
                            Text(
                                text = "দিয়েছি ${
                                    state.transactions.filter { it.type == "GIVEN" }
                                        .sumOf { it.amount }.toCurrencyFormat()
                                }",
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                color = AppTheme.colorScheme.error
                            )
                            Text(
                                text = "নিয়েছি ${
                                    state.transactions.filter { it.type == "TAKEN" }
                                        .sumOf { it.amount }.toCurrencyFormat()
                                }",
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End,
                                color = AppTheme.colorScheme.success
                            )
                        }
                    }
                    LoanHistoryList(
                        dataList = state.transactions,
                        modifier = Modifier.weight(1f),
                        scrollState = searchResultsListState
                    )
                }

                Box {
                    val brush = Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            AppTheme.colorScheme.border
                        )
                    )
                    Canvas(
                        modifier = Modifier.fillMaxWidth()
                            .height(24.dp),
                        onDraw = {
                            drawRoundRect(
                                brush
                            )
                        }
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        colors = CardColors(
                            contentColor = Color.Black,
                            containerColor = Color.White,
                            disabledContainerColor = AppTheme.colorScheme.border,
                            disabledContentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
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
                                            viewModel.onEvent(TransactionEvent.OnTypeChanged(true))
                                        },
                                        selected = state.isGiven
                                    )
                                    Text(
                                        "দিচ্ছি"
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
                                            viewModel.onEvent(TransactionEvent.OnTypeChanged(false))
                                        },
                                        selected = !state.isGiven
                                    )
                                    Text(
                                        "নিচ্ছি"
                                    )
                                }
                            }

                            Row {
                                Box(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    AppAmountInput(
                                        label = "টাকা",
                                        hint = "টাকার সংখ্যা লিখুন",
                                        amount = state.amount,
                                        isMandatory = true
                                    ) {
                                        viewModel.onEvent(TransactionEvent.OnAmountChanged(it))
                                    }
                                }
                                Spacer(modifier = Modifier.width(AppTheme.size.small))
                                Box(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    AppTextInput(
                                        label = "মন্তব্য",
                                        hint = "মন্তব্য লিখুন",
                                        text = state.note
                                    ) {
                                        viewModel.onEvent(TransactionEvent.OnNoteChanged(it))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(AppTheme.size.medium))

                            PrimaryButton(
                                isEnable = state.amount.isNotEmpty(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = AppTheme.size.large),
                                label = "সেভ",
                            ) {
                                viewModel.onEvent(TransactionEvent.OnAddTransactionClick)
                            }
                        }
                    }
                }
            }
        }
        
        // Delete confirmation dialog
        if (state.showDeleteConfirmDialog) {
            ConfirmationDialog(
                title = "সব লেনদেন মুছে ফেলুন?",
                message = "আপনি কি নিশ্চিত যে আপনি ${state.customer?.name ?: "এই গ্রাহকের"} সমস্ত লেনদেন মুছে ফেলতে এবং ব্যালেন্স শূন্য করতে চান? এই কাজটি পূর্বাবস্থায় ফেরানো যাবে না।",
                confirmText = "হ্যাঁ, মুছে ফেলুন",
                cancelText = "না, বাতিল করুন",
                onConfirm = {
                    viewModel.onEvent(TransactionEvent.OnDeleteConfirmed)
                },
                onDismiss = {
                    viewModel.onEvent(TransactionEvent.OnDeleteCancelled)
                }
            )
        }

    }
}

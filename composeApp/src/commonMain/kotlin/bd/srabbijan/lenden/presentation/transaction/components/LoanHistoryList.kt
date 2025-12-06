package bd.srabbijan.lenden.presentation.transaction.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import bd.srabbijan.lenden.core.AppOutLineCard
import bd.srabbijan.lenden.core.convertApiDateToUIDate
import bd.srabbijan.lenden.core.toCurrencyFormat
import bd.srabbijan.lenden.core.toPositive
import bd.srabbijan.lenden.data.local.entity.TransactionEntity
import bd.srabbijan.lenden.theme.AppTheme

@Composable
fun LoanHistoryList(
    dataList: List<TransactionEntity>,
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = modifier,
        state = scrollState,
        verticalArrangement = Arrangement.spacedBy(AppTheme.size.small),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            items = dataList,
            key = { it.id }
        ) { bank ->
            LoanHistoryItem(
                data = bank,
            )
        }
    }
}

@Composable
fun LoanHistoryItem(
    data: TransactionEntity,
) {
    AppOutLineCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    all = AppTheme.size.medium
                )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = data.date.convertApiDateToUIDate(),
                        style = AppTheme.typography.labelSmall,
                    )
                    if (data.note.isNotEmpty()){
                        Text(
                            text = data.note,
                            style = AppTheme.typography.labelSmall,
                        )
                    }
                }


                Text(
                    text = if (data.type == "GIVEN")
                        data.amount.toCurrencyFormat()
                    else "",
                    style = AppTheme.typography.paragraph,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = AppTheme.colorScheme.error
                )
                Text(
                    text = if (data.type == "TAKEN") data.amount.toPositive()
                        .toCurrencyFormat() else "",
                    style = AppTheme.typography.paragraph,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    color = AppTheme.colorScheme.success
                )
            }

        }
    }
}
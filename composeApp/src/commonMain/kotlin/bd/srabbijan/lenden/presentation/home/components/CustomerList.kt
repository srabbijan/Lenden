package bd.srabbijan.lenden.presentation.home.components

import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import bd.srabbijan.lenden.core.AppOutLineCard
import bd.srabbijan.lenden.core.convertApiDateToUIDate
import bd.srabbijan.lenden.core.isPositive
import bd.srabbijan.lenden.core.toCurrencyFormat
import bd.srabbijan.lenden.core.toPositive
import bd.srabbijan.lenden.data.local.entity.CustomerEntity
import bd.srabbijan.lenden.theme.AppTheme
import kotlinx.coroutines.launch
import lenden.composeapp.generated.resources.Res
import lenden.composeapp.generated.resources.ic_trash
import network.chaintech.sdpcomposemultiplatform.sdp
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt

enum class DragValue { Start, End }

@Composable
fun LoanPersonList(
    dataList: List<CustomerEntity>,
    swipeResetId: Int,
    onItemClick: (CustomerEntity) -> Unit,
    onDeleteClick: (CustomerEntity) -> Unit,
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
            key = { "${it.id}_$swipeResetId" }
        ) { customer ->
            LoanPersonListItem(
                data = customer,
                onItemDetailsClick = {
                    onItemClick(customer)
                },
                onDeleteClick = {
                    onDeleteClick(customer)
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoanPersonListItem(
    data: CustomerEntity,
    onItemDetailsClick: (CustomerEntity) -> Unit,
    onDeleteClick: (CustomerEntity) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    
    val actionSize = 100.sdp // Approximate width of 2 buttons + padding
    val actionSizePx = with(density) { actionSize.toPx() }
    
    val anchors = DraggableAnchors<DragValue> {
        DragValue.Start at 0f
        DragValue.End at -actionSizePx
    }

    val state = remember {
        AnchoredDraggableState(
            initialValue = DragValue.Start,
            anchors = anchors,
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            snapAnimationSpec = spring(),
            decayAnimationSpec = exponentialDecay()
        )
    }
    
    SideEffect {
        state.updateAnchors(anchors)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min) // Ensure height matches content
    ) {
        // Background Actions
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(actionSize)
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.size.small, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cross/Close Button
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        state.animateTo(DragValue.Start)
                    }
                },
                modifier = Modifier
                    .size(40.sdp)
                    .background(Color.Gray.copy(alpha = 0.2f), androidx.compose.foundation.shape.CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Cancel",
                    tint = AppTheme.colorScheme.icon
                )
            }

            // Delete Button
            IconButton(
                onClick = {
                    onDeleteClick(data)
                },
                modifier = Modifier
                    .size(40.sdp)
                    .background(AppTheme.colorScheme.error, androidx.compose.foundation.shape.CircleShape)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_trash),
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        }

        // Foreground Content
        AppOutLineCard(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = state.requireOffset().roundToInt(),
                        y = 0
                    )
                }
                .anchoredDraggable(state, Orientation.Horizontal)
        ) {
            Column(
                modifier = Modifier
                    .clickable {
                        onItemDetailsClick.invoke(data)
                    }
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
                    Text(
                        text = data.name,
                        style = AppTheme.typography.titleNormal
                    )
                    Text(
                        text = data.balance.toPositive().toCurrencyFormat(),
                        style = AppTheme.typography.titleNormal,
                        color = if (data.balance.isPositive()) AppTheme.colorScheme.success else AppTheme.colorScheme.error
                    )
                }
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ){
                    Text(
                        text = "সর্বশেষ লেনদেন: ${data.lastActivity.convertApiDateToUIDate()}",
                        style = AppTheme.typography.labelSmall
                    )
                    Text(
                        text = if (data.balance.isPositive()) "/পাবো" else "/দিবো",
                        style = AppTheme.typography.labelNormal
                    )
                }
            }
        }
    }
}
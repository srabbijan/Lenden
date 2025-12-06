package bd.srabbijan.lenden.core

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bd.srabbijan.lenden.theme.AppTheme
import lenden.composeapp.generated.resources.Res
import lenden.composeapp.generated.resources.ic_search
import org.jetbrains.compose.resources.painterResource

sealed class EmptyStateType {
    data object Loading : EmptyStateType()
    data object NoData : EmptyStateType()
    data object NoResults : EmptyStateType()
    data object EmptyCart : EmptyStateType()
}

@Composable
fun EmptyStateScreen(
    modifier: Modifier = Modifier,
    title: String = "Something Went Wrong",
    message: String = "An error occurred while loading the data",
    btnText: String = "Retry",
    type: EmptyStateType,
    onActionClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (type) {
            EmptyStateType.Loading -> LoadingContent()
            EmptyStateType.NoData -> {
                EmptyStateContent(
                    icon = painterResource(Res.drawable.ic_search),
                    title = title,
                    message = message,
                    actionLabel = btnText,
                    onActionClick = onActionClick
                )
            }

            EmptyStateType.NoResults -> NoResultsContent(onActionClick)
            EmptyStateType.EmptyCart -> {
                EmptyStateContent(
                    icon = painterResource(Res.drawable.ic_search),
                    title = title,
                    message = message,
                    actionLabel = btnText,
                    onActionClick = onActionClick
                )
            }
        }
    }
}


@Composable
private fun NoResultsContent(onActionClick: (() -> Unit)?) {
    EmptyStateContent(
        icon = painterResource(Res.drawable.ic_search),
        title = "No Results Found",
        message = "Try adjusting your search or filters",
        actionLabel = "Clear Filters",
        onActionClick = onActionClick
    )
}


@Composable
private fun EmptyStateContent(
    icon: Painter,
    title: String,
    message: String,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Icon(
        painter = icon,
        contentDescription = null,
        modifier = Modifier.size(72.dp),
        tint = AppTheme.colorScheme.border
    )

    Spacer(modifier = Modifier.height(16.dp))

    if (title.isNotEmpty()){
        Text(
            text = title,
            style = AppTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
            color = AppTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
    if (message.isNotEmpty()){
        Text(
            text = message,
            style = AppTheme.typography.paragraph,
            textAlign = TextAlign.Center,
            color = AppTheme.colorScheme.cardBgColor
        )
    }

    if (!actionLabel.isNullOrEmpty() && onActionClick != null) {
        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            label = actionLabel
        ) {
            onActionClick()
        }
    }
}

@Composable
private fun LoadingContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(10) {
            ShimmerListItem()
        }
    }
}

@Composable
fun ShimmerListItem(
    baseColor: Color = AppTheme.colorScheme.cardBgColor,
    highlightColor: Color = AppTheme.colorScheme.background
) {
    // Animation setup
    val shimmerAnimation = rememberInfiniteTransition(label = "")
    val xShimmer = shimmerAnimation.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2500,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    // Gradient for shimmer effect
    val brush = Brush.linearGradient(
        colors = listOf(baseColor, highlightColor, baseColor),
        start = Offset(xShimmer.value, 0f),
        end = Offset(xShimmer.value + 150f, 0f)
    )

    // Apply shimmer effect to a composable
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(brush, RoundedCornerShape(8.dp))
    )
}


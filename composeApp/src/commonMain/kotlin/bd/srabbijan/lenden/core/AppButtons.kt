package bd.srabbijan.lenden.core

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import bd.srabbijan.lenden.theme.AppTheme
import lenden.composeapp.generated.resources.Res
import lenden.composeapp.generated.resources.ic_dots_vertical
import lenden.composeapp.generated.resources.ic_user_plus
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp
import org.jetbrains.compose.resources.painterResource

enum class ButtonIconPosition {
    START, END
}
@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    label: String,
    icon: Painter? = null,
    isEnable: Boolean = true,
    height: Dp = 40.sdp,
    iconGravity: ButtonIconPosition = ButtonIconPosition.START,
    onClick: () -> Unit
) {
    val shape = AppTheme.shape.button

    val backgroundModifier = if (isEnable) {
        Modifier.background(color = AppTheme.colorScheme.primary, shape = shape)
    } else {
        Modifier.background(color = AppTheme.colorScheme.border, shape = shape)
    }

    Box(
        modifier = modifier
            .heightIn(min = height)
            .clip(shape)
            .then(backgroundModifier)
            .then(
                if (isEnable) {
                    Modifier.clickable(
                        onClick = onClick
                    )
                } else {
                    Modifier // no clickable when disabled
                }
            )
            .padding(horizontal = 16.sdp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon == null) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = label,
                    color = Color.White,
                    style = AppTheme.typography.labelLarge
                )
            } else {
                when (iconGravity) {
                    ButtonIconPosition.START -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(AppTheme.size.small)
                        ) {
                            Icon(
                                modifier = Modifier.size(24.sdp),
                                painter = icon,
                                contentDescription = null,
                                tint = AppTheme.colorScheme.text
                            )
                            Text(
                                text = label,
                                color = Color.White,
                                style = AppTheme.typography.labelLarge
                            )
                        }
                    }

                    ButtonIconPosition.END -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(AppTheme.size.small)
                        ) {
                            Text(
                                text = label,
                                color = Color.White,
                                style = AppTheme.typography.labelLarge
                            )
                            Icon(
                                modifier = Modifier.size(24.sdp),
                                painter = icon,
                                contentDescription = null,
                                tint = AppTheme.colorScheme.text
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    label: String,
    icon: Painter? = null,
    iconGravity: ButtonIconPosition = ButtonIconPosition.START,
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = modifier.heightIn(min = 40.sdp),
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = AppTheme.colorScheme.cardBgColor,
            contentColor = AppTheme.colorScheme.primary
        ),
        shape = AppTheme.shape.button,
        border = BorderStroke(1.sdp, AppTheme.colorScheme.primary)
    ) {
        if (icon == null) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = label,
                style = AppTheme.typography.labelLarge
            )
        } else {
            when (iconGravity) {
                ButtonIconPosition.START -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.size.small)
                    ) {
                        Icon(
                            modifier = Modifier.size(24.sdp),
                            painter = icon,
                            contentDescription = null
                        )

                        Text(
                            text = label,
                            style = AppTheme.typography.labelLarge
                        )
                    }
                }

                ButtonIconPosition.END -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AppTheme.size.small)
                    ) {
                        Text(
                            text = label,
                            style = AppTheme.typography.labelLarge
                        )
                        Icon(
                            modifier = Modifier.size(24.sdp),
                            painter = icon,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppTextButton(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,

        ) {
        Text(
            text = label,
            style = AppTheme.typography.labelLarge.copy(
                color = AppTheme.colorScheme.primary
            )
        )
    }
}



@Composable
fun AppThreeDotButton(
    onClick: () -> Unit
) {
    IconButton(
        onClick = { onClick() }
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_dots_vertical),
            contentDescription = null,
            tint = AppTheme.colorScheme.text
        )
    }
}


@Composable
fun IconChip(
    icon: Painter,
    modifier: Modifier = Modifier,
    iconColor: Color = AppTheme.colorScheme.icon,
    backgroundColor: Color = AppTheme.colorScheme.primary.copy(alpha = .1f),
    onClick: (() -> Unit)?,
) {
    Box(
        modifier = modifier
            .size(48.sdp)
            .clip(RoundedCornerShape(16.sdp))
            .background(backgroundColor)
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.Center)
                .clickable { onClick?.invoke() },
            painter = icon,
            contentDescription = null,
            tint = iconColor
        )
    }
}

@Composable
fun IconWithBadge(
    count:Int,
    icon: Painter,
    modifier: Modifier = Modifier,
    iconColor: Color = AppTheme.colorScheme.icon,
    backgroundColor: Color = AppTheme.colorScheme.primary,
    onClick: () -> Unit,
) {
    Box(modifier = modifier.clickable { onClick() }) {
        Icon(
            painter = icon,
            contentDescription = "Notifications",
            tint = iconColor,
            modifier = Modifier.size(24.sdp)
        )

        if (count > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 4.sdp, y = (-4).sdp)
                    .size(18.sdp)
                    .background(Color.Red, shape = CircleShape)
                    .border(1.sdp, Color.White, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (count > 99) "99+" else count.toString(),
                    color = Color.White,
                    fontSize = 10.ssp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
@Composable
fun AppFloatingButton(
    modifier: Modifier = Modifier,
    icon: Painter = painterResource(Res .drawable.ic_user_plus),
    onClick: () -> Unit
) {
    FloatingActionButton(
        containerColor = AppTheme.colorScheme.primary,
        contentColor = AppTheme.colorScheme.icon,
        modifier = modifier,
        onClick = {
            onClick()
        },
    ) {
        Icon(icon, "Floating action button.")
    }
}

@Composable
fun AppIconButton(
    modifier: Modifier = Modifier,
    painter: Painter = painterResource(Res.drawable.ic_dots_vertical),
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = { onClick() }
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            tint = AppTheme.colorScheme.text
        )
    }
}

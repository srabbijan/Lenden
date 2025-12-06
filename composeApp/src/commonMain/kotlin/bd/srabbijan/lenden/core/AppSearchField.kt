package bd.srabbijan.lenden.core

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import bd.srabbijan.lenden.theme.AppTheme
import lenden.composeapp.generated.resources.Res
import lenden.composeapp.generated.resources.ic_search
import network.chaintech.sdpcomposemultiplatform.sdp
import org.jetbrains.compose.resources.painterResource


@Composable
fun AppSearchField(
    modifier: Modifier = Modifier,
    hint: String = "Search",
    value: String = "",
    borderColor: Color = AppTheme.colorScheme.border.copy(alpha = .8f),
    selectedBorderColor: Color = AppTheme.colorScheme.border,
    onActionSearch: (String) -> Unit = {},
    onTypeSearch: (String) -> Unit = {},
) {
    var query by remember(value) { mutableStateOf(value) }
    var actualBorderColor by remember { mutableStateOf(borderColor) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(AppTheme.shape.container)
            .border(
                width = 1.sdp,
                color = actualBorderColor,
                shape = AppTheme.shape.container
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .weight(1f)
                .heightIn(40.sdp),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.size.small)
                    .onFocusChanged { state ->
                        val actualColor = if (state.isFocused) selectedBorderColor else borderColor
                        actualBorderColor = actualColor
                    }
                    .padding(horizontal = AppTheme.size.small),
                value = query,
                onValueChange = {
                    query = it
                    if (query.isEmpty()) {
                        keyboardController?.hide()
                        onTypeSearch(query)
                        onActionSearch(query)
                    } else if (query.length % 2 == 0) {
                        onTypeSearch(query)
                    }
                },
                textStyle = TextStyle.Default.copy(
                    color = AppTheme.colorScheme.text,
                ),
                cursorBrush = SolidColor(AppTheme.colorScheme.text),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search,
                ),
                keyboardActions = KeyboardActions(onSearch = {
                    keyboardController?.hide()
                    defaultKeyboardAction(ImeAction.Search)
                    onActionSearch(query)
                    onTypeSearch(query)
                }),
            )
            if (query.isBlank()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.size.medium),
                    text = hint,
                    style = AppTheme.typography.labelLarge,
                    color = AppTheme.colorScheme.labelColor
                )
            }
        }

        IconButton(
            onClick = {
                keyboardController?.hide()
                onTypeSearch(query)
                onActionSearch(query)
            },

            ) {
            Icon(
                painter = painterResource(Res.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier
                    .padding(AppTheme.size.small)
                    .size(26.sdp),
                tint = AppTheme.colorScheme.border
            )
        }
    }
}


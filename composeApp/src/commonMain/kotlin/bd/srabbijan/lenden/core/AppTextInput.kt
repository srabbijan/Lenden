package bd.srabbijan.lenden.core

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import bd.srabbijan.lenden.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AppTextInput(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    hint: String = "",
    singleLine: Boolean = true,
    isEnable: Boolean = true,
    maxLength: Int = 250,
    borderColor: Color = AppTheme.colorScheme.border,
    selectedBorderColor: Color = AppTheme.colorScheme.text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leadingIcon: Painter? = null,
    trailingIcon: Painter? = null,
    trailingIconClicked: () -> Unit = {},
    isError: Boolean = false,
    isMandatory: Boolean = false,
    error: (@Composable () -> Unit)? = null,
    onTextChanged: (newValue: String) -> Unit,
) {
    var actualBorderColor by remember { mutableStateOf(borderColor) }
    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.size.small)
    ) {
        if (label.isNotEmpty()) {
            Spacer(modifier = Modifier.height(AppTheme.size.small))
            Row {
                Text(
                    text = label,
                    style = AppTheme.typography.labelNormal,
                    color = AppTheme.colorScheme.text
                )
                if (isMandatory) {
                    Text(
                        text = " *",
                        style = AppTheme.typography.labelNormal,
                        color = AppTheme.colorScheme.error
                    )
                }
            }
        }

        if (isError) {
            actualBorderColor = AppTheme.colorScheme.error
            error?.invoke()
        } else {
            actualBorderColor = borderColor
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(AppTheme.shape.container)
                .border(
                    width = 1.dp,
                    color = actualBorderColor,
                    shape = AppTheme.shape.container
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingIcon?.let { icon ->
                Icon(
                    modifier = modifier
                        .size(24.dp)
                        .padding(AppTheme.size.small),
                    painter = icon,
                    contentDescription = null,
                    tint = borderColor
                )
            }
            Box(
                modifier = modifier
                    .weight(1f)
                    .padding(horizontal = AppTheme.size.normal)
                    .heightIn(48.dp),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .onFocusChanged { state ->
                            val actualColor =
                                if (state.isFocused) selectedBorderColor else borderColor
                            actualBorderColor = actualColor
                        },
                    value = text,
                    enabled = isEnable,
                    singleLine = singleLine,
                    onValueChange = { newValue ->
                        if (newValue.length <= maxLength) {
                            onTextChanged(newValue)
                        }
                    },
                    textStyle = AppTheme.typography.paragraph,
                    cursorBrush = SolidColor(AppTheme.colorScheme.primary),
                    visualTransformation = visualTransformation,
                    keyboardActions = keyboardActions,
                    keyboardOptions = keyboardOptions
                )
                if (text.isBlank()) {
                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = AppTheme.size.small),
                        text = hint,
                        style = AppTheme.typography.labelNormal,
                        color = borderColor
                    )
                }
            }
            trailingIcon?.let { icon ->
                Icon(
                    modifier = modifier
                        .padding(AppTheme.size.small)
                        .clickable { trailingIconClicked() },
                    painter = icon,
                    contentDescription = null,
                    tint = borderColor
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAppTextInput() {
    AppTheme {
        Surface(color = AppTheme.colorScheme.background) {
            AppTextInput(
                label = "",
                text = "",
                hint = ""
            ){

            }
        }
    }
}
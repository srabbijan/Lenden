package bd.srabbijan.lenden.core

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import lenden.composeapp.generated.resources.Res
import lenden.composeapp.generated.resources.taka
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppAmountInput(
    modifier: Modifier = Modifier,
    label: String,
    hint: String,
    amount: String,
    maxLength: Int = 11,
    isMandatory:Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
    onPasswordChanged: (newValue: String) -> Unit,
) {
    AppTextInput(
        modifier = modifier,
        text = amount,
        label = label,
        hint = hint,
        isMandatory = isMandatory,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        onTextChanged = onPasswordChanged,
        maxLength = maxLength,
        leadingIcon = painterResource(Res.drawable.taka)
    )
}

@Composable
fun AppNumberInput(
    modifier: Modifier = Modifier,
    label: String,
    hint: String,
    number: String,
    maxLength: Int = 6,
    isMandatory:Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
    onNumberChanged: (newValue: String) -> Unit,
) {
    AppTextInput(
        modifier = modifier,
        text = number,
        label = label,
        hint = hint,
        isMandatory = isMandatory,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        onTextChanged = onNumberChanged,
        maxLength = maxLength,
    )
}
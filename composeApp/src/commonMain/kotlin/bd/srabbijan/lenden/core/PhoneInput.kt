package bd.srabbijan.lenden.core

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import bd.srabbijan.lenden.theme.AppTheme
import lenden.composeapp.generated.resources.Res
import lenden.composeapp.generated.resources.enter_phone
import lenden.composeapp.generated.resources.error_bad_phone_format
import lenden.composeapp.generated.resources.ic_phone_call
import lenden.composeapp.generated.resources.phone_hint
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PhoneInput(
    modifier: Modifier = Modifier,
    phone: String,
    label: String = stringResource(Res.string.enter_phone),
    hint: String = stringResource(Res.string.phone_hint),
    maxLength: Int = 11,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
    onPasswordChanged: (newValue: String) -> Unit,
) {
    AppTextInput(
        modifier = modifier,
        text = phone,
        label = label,
        hint = hint,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        onTextChanged = onPasswordChanged,
        maxLength = maxLength,
        leadingIcon = painterResource(Res.drawable.ic_phone_call),
        error = {
            Text(
                text = stringResource(Res.string.error_bad_phone_format),
                color = AppTheme.colorScheme.error
            )
        }
    )
}
package bd.srabbijan.lenden.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bd.srabbijan.lenden.core.BaseViewModel
import bd.srabbijan.lenden.core.UiEffect
import bd.srabbijan.lenden.core.UiEvent
import bd.srabbijan.lenden.core.UiState

data class SettingsState(
    val isDarkTheme: Boolean = false,
    val isEnglish: Boolean = true
) : UiState

sealed interface SettingsEvent : UiEvent {
    data class OnThemeChanged(val isDark: Boolean) : SettingsEvent
    data class OnLanguageChanged(val isEnglish: Boolean) : SettingsEvent
}

sealed interface SettingsEffect : UiEffect

class SettingsViewModel : BaseViewModel<SettingsState, SettingsEvent, SettingsEffect>(SettingsState()) {
    override fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnThemeChanged -> setState { copy(isDarkTheme = event.isDark) }
            is SettingsEvent.OnLanguageChanged -> setState { copy(isEnglish = event.isEnglish) }
        }
    }
}

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dark Theme", modifier = Modifier.weight(1f))
            Switch(
                checked = state.isDarkTheme,
                onCheckedChange = { viewModel.onEvent(SettingsEvent.OnThemeChanged(it)) }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Language (EN/BN)", modifier = Modifier.weight(1f))
            Switch(
                checked = state.isEnglish,
                onCheckedChange = { viewModel.onEvent(SettingsEvent.OnLanguageChanged(it)) }
            )
        }
    }
}

package bd.srabbijan.lenden.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

interface UiState

interface UiEvent

interface UiEffect

abstract class BaseViewModel<State : UiState, Event : UiEvent, Effect : UiEffect>(
    initialState: State
) : ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    private val _effect = Channel<Effect>()
    val effect = _effect.receiveAsFlow()

    abstract fun onEvent(event: Event)

    protected fun setState(reduce: State.() -> State) {
        _uiState.value = _uiState.value.reduce()
    }

    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch {
            _effect.send(effectValue)
        }
    }
}

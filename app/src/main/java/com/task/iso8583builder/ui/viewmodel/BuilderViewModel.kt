package com.task.iso8583builder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.iso8583builder.ui.events.BuilderEvents
import com.task.iso8583builder.ui.states.BuilderState
import com.task.iso8583builder.ui.states.GeneratedIsoMessage
import com.task.iso8583builder.domain.usecase.BuildIsoMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuilderViewModel @Inject constructor(
    private val buildIsoMessageUseCase: BuildIsoMessageUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(BuilderState())
    val state: StateFlow<BuilderState> = _state.asStateFlow()

    fun handleEvents(event: BuilderEvents) {
        when (event) {
            BuilderEvents.BuildMessage -> buildMessage()
            BuilderEvents.ClearError -> clearError()
            is BuilderEvents.UpdateAmount -> updateAmount(event.amount)
            is BuilderEvents.UpdateCardNumber -> updateCardNumber(event.cardNumber)
        }
    }

    private fun updateCardNumber(cardNumber: String) {
        val filtered = cardNumber.filter { it.isDigit() }.take(16)
        _state.update { it.copy(cardNumber = filtered) }
    }

    private fun updateAmount(amount: String) {
        val filtered = amount.filter { it.isDigit() }
        _state.update { it.copy(amount = filtered) }
    }

    private fun buildMessage() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            val cardNumber = _state.value.cardNumber
            val amount = _state.value.amount
            when {
                cardNumber.length != 16 -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Card number must be 16 digits!"
                        )
                    }
                    return@launch
                }

                amount.isEmpty() || amount.toLongOrNull() == null -> {
                    _state.update {
                        it.copy(isLoading = false, errorMessage = "Enter a Valid amount!")
                    }
                    return@launch
                }
            }
            try {
                val result = buildIsoMessageUseCase(
                    cardNumber = cardNumber,
                    amount = amount.toLong()
                )
                _state.update {
                    it.copy(
                        isLoading = false,
                        generatedMessage = GeneratedIsoMessage(
                            field2 = result.field2,
                            field4 = result.field4,
                            field7 = result.field7,
                            field11 = result.field11,
                            fullMessage = result.fullMessage
                        )
                    )
                }
            }catch (e : Exception){
                _state.update {
                    it.copy(
                        isLoading = false ,
                        errorMessage = "An error building message: ${e.message}"
                    )
                }
            }
        }
    }
    private fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }

}
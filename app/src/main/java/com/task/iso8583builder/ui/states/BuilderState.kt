package com.task.iso8583builder.ui.states


data class BuilderState(
    val cardNumber: String = "",
    val amount: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val generatedMessage: GeneratedIsoMessage? = null
)

data class GeneratedIsoMessage(
    val field2: String,
    val field4: String,
    val field7: String,
    val field11: String,
    val fullMessage: String
)
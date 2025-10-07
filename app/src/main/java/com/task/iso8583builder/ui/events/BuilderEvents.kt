package com.task.iso8583builder.ui.events

sealed class BuilderEvents {
    data class UpdateCardNumber(val cardNumber : String) : BuilderEvents()
    data class UpdateAmount(val amount : String) : BuilderEvents()
    object BuildMessage : BuilderEvents()
    object ClearError : BuilderEvents()
}
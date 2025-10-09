package com.task.iso8583builder.domain.usecase

import com.task.iso8583builder.domain.model.IsoMessageResult
import com.task.iso8583builder.domain.repository.IsoMessageRepository
import javax.inject.Inject

class BuildIsoMessageUseCase @Inject constructor(
    private val repository: IsoMessageRepository
) {
    suspend operator fun invoke(cardNumber : String, amount : Long) : IsoMessageResult {
        return repository.buildMessage(cardNumber , amount)
    }
}
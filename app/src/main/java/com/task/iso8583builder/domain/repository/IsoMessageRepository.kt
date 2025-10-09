package com.task.iso8583builder.domain.repository

import com.task.iso8583builder.domain.model.IsoMessageResult

interface IsoMessageRepository {
    suspend fun buildMessage(cardNumber : String , amount : Long) : IsoMessageResult
    suspend fun parseMessage(message : String) : IsoMessageResult
}
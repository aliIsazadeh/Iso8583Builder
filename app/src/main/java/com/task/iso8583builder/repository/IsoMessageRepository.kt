package com.task.iso8583builder.repository

import com.task.iso8583builder.model.IsoMessageResult

interface IsoMessageRepository {
    suspend fun buildMessage(cardNumber : String , amount : Long) : IsoMessageResult
    suspend fun parseMessage(message : String) : IsoMessageResult
}
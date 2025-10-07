package com.task.iso8583builder.repository

import com.solab.iso8583.IsoMessage
import com.solab.iso8583.IsoType
import com.solab.iso8583.MessageFactory
import com.task.iso8583builder.model.IsoMessageResult
import com.task.iso8583builder.provider.StanManager
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class IsoMessageRepositoryImpl @Inject constructor(
    private val stanManager: StanManager
) : IsoMessageRepository {

    private val messageFactory = MessageFactory<IsoMessage>().apply {
        characterEncoding = "UTF-8"
        isUseBinaryBitmap = false
    }

    override suspend fun buildMessage(
        cardNumber: String,
        amount: Long
    ): IsoMessageResult =
        withContext(Dispatchers.IO) {
            val message = messageFactory.newMessage(0x0200)
            val field2 = cardNumber
            message.setValue(2, field2, IsoType.LLVAR, 19)
            val field4 = String.format(Locale.getDefault(), "%012d", amount)
            message.setValue(4, field4, IsoType.NUMERIC, 12)

            val sdf = SimpleDateFormat("MMddHHmmss", Locale.getDefault())
            val field7 = sdf.format(Date())
            message.setValue(7, field7, IsoType.NUMERIC, 10)

            val field11 = stanManager.getNextStan()
            message.setValue(11, field11, IsoType.NUMERIC, 6)

            val fullMessage = String(message.writeData())

            IsoMessageResult(
                field2 = field2,
                field4 = field4,
                field7 = field7,
                field11 = field11,
                fullMessage = fullMessage
            )
        }


    override suspend fun parseMessage(message: String): IsoMessageResult =
        withContext(Dispatchers.IO) {
            val parsed = messageFactory.parseMessage(message.toByteArray(), 0)
            IsoMessageResult(
                field2 = parsed.getObjectValue(2) ?: "",
                field4 = parsed.getObjectValue(4) ?: "",
                field7 = parsed.getObjectValue(7) ?: "",
                field11 = parsed.getObjectValue(11) ?: "",
                fullMessage = message
            )
        }
}
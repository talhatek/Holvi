package com.tek.database.domain

import com.tek.database.dao.CardDao
import com.tek.database.data.CardDto

class AddCardUseCase(private val cardDao: CardDao) {

    suspend operator fun invoke(
        number: String,
        holder: String,
        exp: String,
        cvv: String,
        company: String,
        provider: String,
        cardColor: String,
        textColor: String
    ) {
        cardDao.addCard(
            CardDto(
                id = 0,
                number = number,
                holder = holder,
                expiration = exp,
                cvv = cvv,
                company = company,
                provider = provider,
                color = cardColor,
                textColor = textColor
            )
        )
    }
}
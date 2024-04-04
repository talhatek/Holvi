package com.tek.database.domain

import com.tek.database.dao.CardDao
import com.tek.database.data.CardDto
import com.tek.database.model.Card

class UpdateCardUseCase(private val cardDao: CardDao) {

    suspend operator fun invoke(card: Card) {
        cardDao.updateCard(
            with(card) {
                CardDto(
                    id = 0,
                    cardNumber = number,
                    cardHolder = holderName,
                    expiration = exp,
                    cvv = cvv,
                    color = color.value
                )
            }
        )
    }
}
package com.tek.database.domain

import com.tek.database.dao.CardDao
import com.tek.database.domain.mapper.CardDtoToCardMapper
import kotlinx.coroutines.flow.map

class ObserveCardUseCase(private val cardDao: CardDao, private val mapper: CardDtoToCardMapper) {

    operator fun invoke() =
        cardDao.observeAllCards().map { cardDtoList -> cardDtoList.map { mapper.invoke(it) } }
}
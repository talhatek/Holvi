package com.tek.database.domain.mapper

import androidx.compose.ui.graphics.Color
import com.tek.database.data.CardDto
import com.tek.database.model.Card


class CardDtoToCardMapper {

    operator fun invoke(dto: CardDto) = with(dto) {
        Card(
            id = id,
            number = number,
            exp = expiration,
            cvv = cvv,
            holder = holder,
            provider = provider,
            company = company,
            cardColor = Color(dto.color.toLong(radix = 16)),
            textColor = Color(dto.textColor.toLong(radix = 16)),
        )
    }
}
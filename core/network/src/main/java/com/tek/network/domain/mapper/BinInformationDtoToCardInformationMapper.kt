package com.tek.network.domain.mapper

import com.tek.network.data.BinInformationDto
import com.tek.network.model.CardInformation
import com.tek.network.model.CardProvider

class BinInformationDtoToCardInformationMapper {

    operator fun invoke(dto: BinInformationDto) = with(dto) {
        CardInformation(bin?.brand?.let { CardProvider.valueOf(it) }, bin?.issuer?.name)
    }
}
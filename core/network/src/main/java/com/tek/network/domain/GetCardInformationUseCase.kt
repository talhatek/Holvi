package com.tek.network.domain

import com.tek.network.data.BinApi
import com.tek.network.domain.mapper.BinInformationDtoToCardInformationMapper

class GetCardInformationUseCase(
    private val binApi: BinApi,
    private val mapper: BinInformationDtoToCardInformationMapper
) {
    suspend operator fun invoke(bin: String) = mapper.invoke(binApi.binInformation(bin))
}
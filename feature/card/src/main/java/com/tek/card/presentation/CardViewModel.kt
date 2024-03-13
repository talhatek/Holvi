package com.tek.card.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tek.network.domain.GetCardInformationUseCase
import com.tek.util.AppDispatchers
import kotlinx.coroutines.launch

class CardViewModel(
    private val getCardInformation: GetCardInformationUseCase,
    private val appDispatchers: AppDispatchers
) : ViewModel() {

    fun getBinInfo(bin: String) {
        viewModelScope.launch(appDispatchers.IO) {
            getCardInformation.invoke("456933")

        }
    }
}
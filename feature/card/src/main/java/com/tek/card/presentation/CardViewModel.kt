package com.tek.card.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tek.network.domain.GetCardInformationUseCase
import com.tek.util.AppDispatchers
import kotlinx.coroutines.launch

class CardViewModel(
    private val getCardInformation: GetCardInformationUseCase,
    appDispatchers: AppDispatchers
) : ViewModel() {


    init {
        viewModelScope.launch(appDispatchers.IO) {
            getCardInformation.invoke("456933")

        }
    }
}
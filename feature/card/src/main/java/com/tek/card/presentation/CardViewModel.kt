package com.tek.card.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tek.database.model.Card
import com.tek.network.domain.GetCardInformationUseCase
import com.tek.util.AppDispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CardViewModel(
    private val getCardInformation: GetCardInformationUseCase,
    private val appDispatchers: AppDispatchers
) : ViewModel() {

    private val _cards = mutableStateOf<CardState>(CardState.Initial)
    val cards: State<CardState> get() = _cards

    init {
        viewModelScope.launch {
            delay(3000)
            _cards.value = CardState.Loaded(
                data = listOf(
                    Card(
                        id = 0,
                        number = "1111 1111 1111 1111",
                        exp = "04/24",
                        cvv = "456",
                        holderName = "Talha Tek",
                        color = Color.Red
                    ),
                    Card(
                        id = 0,
                        number = "1111 1111 1111 1111",
                        exp = "04/24",
                        cvv = "456",
                        holderName = "Talha Tek",
                        color = Color.Yellow
                    ),
                    Card(
                        id = 0,
                        number = "1111 1111 1111 1111",
                        exp = "04/24",
                        cvv = "456",
                        holderName = "Talha Tek",
                        color = Color.Green
                    ),
                    Card(
                        id = 0,
                        number = "1111 1111 1111 1111",
                        exp = "04/24",
                        cvv = "456",
                        holderName = "Talha Tek",
                        color = Color.Cyan
                    ),
                    Card(
                        id = 0,
                        number = "1111 1111 1111 1111",
                        exp = "04/24",
                        cvv = "456",
                        holderName = "Talha Tek",
                        color = Color.Red
                    ),
                    Card(
                        id = 0,
                        number = "1111 1111 1111 1111",
                        exp = "04/24",
                        cvv = "456",
                        holderName = "Talha Tek",
                        color = Color.Yellow
                    ),
                    Card(
                        id = 0,
                        number = "1111 1111 1111 1111",
                        exp = "04/24",
                        cvv = "456",
                        holderName = "Talha Tek",
                        color = Color.Green
                    ),
                    Card(
                        id = 0,
                        number = "1111 1111 1111 1111",
                        exp = "04/24",
                        cvv = "456",
                        holderName = "Talha Tek",
                        color = Color.Cyan
                    ),
                )
            )
        }
    }

    fun getBinInfo(bin: String) {
        viewModelScope.launch(appDispatchers.IO) {
            getCardInformation.invoke("456933")

        }
    }
}

sealed class CardState {

    data class Loaded(val data: List<Card>) : CardState()
    data object Initial : CardState()
    data object Loading : CardState()
}
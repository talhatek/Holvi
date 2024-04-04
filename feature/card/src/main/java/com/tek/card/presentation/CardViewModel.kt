package com.tek.card.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tek.database.domain.AddCardUseCase
import com.tek.database.domain.UpdateCardUseCase
import com.tek.database.model.Card
import com.tek.network.domain.GetCardInformationUseCase
import com.tek.util.AppDispatchers
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CrudCardViewModel(
    private val addCard: AddCardUseCase,
    private val updateCard: UpdateCardUseCase,
    private val getCardInformation: GetCardInformationUseCase,
    private val appDispatchers: AppDispatchers
) : ViewModel() {

    private val _cardState = MutableStateFlow<CardState>(CardState.Initial)
    val cardState
        get() = _cardState.asStateFlow()

    private val _cardEffect = MutableSharedFlow<CardEffect>()
    val cardEffect
        get() = _cardEffect.asSharedFlow()

    init {
        viewModelScope.launch {
            delay(3000)
            _cardState.value = CardState.Loaded(
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

    private suspend fun getBinInfo(bin: String) = getCardInformation.invoke("456933")


    internal fun add(cardHolder: String, cardNumber: String, cvv: String, exp: String) {
        val exceptionHandler = CoroutineExceptionHandler { _, ex ->
            viewModelScope.launch(appDispatchers.Main) {
                _cardEffect.emit(
                    CardEffect.Error(
                        type = TYPE.ADD,
                        message = ex.message.orEmpty()
                    )
                )
            }
        }
        viewModelScope.launch(appDispatchers.IO + exceptionHandler) {
            val info = getBinInfo(cardNumber.takeLast(6))

            _cardEffect.emit(
                CardEffect.Success(
                    type = TYPE.ADD,
                    message = "Successfully added!"
                )
            )
        }
    }

    internal fun update(card: Card) {
        val exceptionHandler = CoroutineExceptionHandler { _, ex ->
            viewModelScope.launch(appDispatchers.Main) {
                _cardEffect.emit(
                    CardEffect.Error(
                        type = TYPE.UPDATE,
                        message = ex.message.orEmpty()
                    )
                )
            }
        }
        viewModelScope.launch(appDispatchers.IO + exceptionHandler) {
            updateCard.invoke(card)
            _cardEffect.emit(
                CardEffect.Success(
                    type = TYPE.UPDATE,
                    message = "Successfully updated!"
                )
            )
        }
    }
}

sealed class CardEffect {
    data class Success(val type: TYPE, val message: String) : CardEffect()
    data class Error(val type: TYPE, val message: String) : CardEffect()

}

enum class TYPE {
    ADD,
    UPDATE,
    DELETE
}

sealed class CardState {

    data class Loaded(val data: List<Card>) : CardState()
    data object Initial : CardState()
    data object Loading : CardState()
}
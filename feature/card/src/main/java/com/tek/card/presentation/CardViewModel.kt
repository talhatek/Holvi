package com.tek.card.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tek.database.domain.AddCardUseCase
import com.tek.database.domain.ObserveCardUseCase
import com.tek.database.domain.UpdateCardUseCase
import com.tek.database.model.Card
import com.tek.network.domain.GetCardInformationUseCase
import com.tek.util.AppDispatchers
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CrudCardViewModel(
    private val addCard: AddCardUseCase,
    private val observeCard: ObserveCardUseCase,
    private val updateCard: UpdateCardUseCase,
    private val getCardInformation: GetCardInformationUseCase,
    private val appDispatchers: AppDispatchers
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val cardState
        get() = observeCard().onStart {
            flowOf(CardState.Initial)
        }.flatMapLatest {
            flowOf(CardState.Loaded(it))
        }

    private val _cardEffect = MutableSharedFlow<CardEffect>()
    val cardEffect
        get() = _cardEffect.asSharedFlow()


    private suspend fun getBinInfo(bin: String) = getCardInformation.invoke(bin)


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
            if (!isValidated(cardHolder, cardNumber, cvv, exp)) {
                _cardEffect.emit(
                    CardEffect.Error(
                        type = TYPE.ADD,
                        message = "Make sure you are not missing any information!"
                    )
                )
            }
            _cardEffect.emit(
                CardEffect.Info(
                    type = TYPE.ADD,
                    message = "In progress..."
                )
            )
            val info = getBinInfo(cardNumber.takeLast(6))
            // TODO: fetch somehow bank colors and icon
            addCard.invoke(
                number = cardNumber,
                exp = exp,
                cvv = cvv,
                holder = cardHolder,
                company = info.company.orEmpty(),
                provider = info.cardProvider?.name.orEmpty(),
                cardColor = "ff212529",
                textColor = "ffE5E4E2"
            )

            _cardEffect.emit(
                CardEffect.Success(
                    type = TYPE.ADD,
                    message = "Successfully added!"
                )
            )
        }
    }

    private fun isValidated(
        cardHolder: String,
        cardNumber: String,
        cvv: String,
        exp: String
    ): Boolean {
        return cardHolder.isNotBlank() and (cardNumber.length >= 2) and (cvv.length == 3) and (exp.length == 5) and (exp[2] == '/') and
                (exp.replace("/", "").all { it.isDigit() })
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

sealed class CardEffect(open val type: TYPE, open val message: String) {
    data class Success(override val type: TYPE, override val message: String) :
        CardEffect(type, message)

    data class Info(
        override val type: TYPE,
        override val message: String,
        val action: ACTION? = null
    ) :
        CardEffect(type, message)

    data class Error(override val type: TYPE, override val message: String) :
        CardEffect(type, message)

}

enum class TYPE {
    ADD,
    UPDATE,
    DELETE
}

enum class ACTION {
    ADDITIONAL_INFO
}


sealed class CardState {

    data class Loaded(val data: List<Card>) : CardState()
    data object Initial : CardState()
    data object Loading : CardState()
}
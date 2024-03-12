package com.tek.network.model


enum class CardProvider {
    VISA,
    MASTER
}

data class CardInformation(val cardProvider: CardProvider?, val company: String?)
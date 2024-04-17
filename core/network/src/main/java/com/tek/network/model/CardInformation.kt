package com.tek.network.model


enum class CardProvider(val text: String) {
    VISA("Visa"),
    MASTER("Master"),
    AMERICAN_EXPRESS("American Express")
}

data class CardInformation(val cardProvider: CardProvider?, val company: String?)
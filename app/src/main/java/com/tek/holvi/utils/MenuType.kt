package com.tek.holvi.utils


import androidx.annotation.StringDef


@StringDef(MenuType.CARD, MenuType.PASSWORD)
@Target(
    AnnotationTarget.TYPE, AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER,
)
@Retention(AnnotationRetention.SOURCE)
annotation class MenuType {
    companion object {
        const val CARD = "Card"
        const val PASSWORD = "Password"

    }
}








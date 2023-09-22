package com.example.holvi.utils


import androidx.annotation.StringDef


@StringDef(MenuType.SEE_ALL, MenuType.GENERATE, MenuType.PORT)
@Target(
    AnnotationTarget.TYPE, AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER,
)
@Retention(AnnotationRetention.SOURCE)
annotation class MenuType {
    companion object {
        const val SEE_ALL = "See All"
        const val GENERATE = "Generate"
        const val PORT = "Export / Import"

    }
}








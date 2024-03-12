package com.tek.holvi.utils


import androidx.annotation.StringDef


@StringDef(PasswordMenuType.SEE_ALL, PasswordMenuType.GENERATE, PasswordMenuType.PORT)
@Target(
    AnnotationTarget.TYPE, AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER,
)
@Retention(AnnotationRetention.SOURCE)
annotation class PasswordMenuType {
    companion object {
        const val SEE_ALL = "See All"
        const val GENERATE = "Generate"
        const val PORT = "Export / Import"

    }
}








package com.example.holvi.utils


import androidx.annotation.StringDef


@StringDef(MenuType.ADD, MenuType.UPDATE)
@Target(
    AnnotationTarget.TYPE, AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER,
)
@Retention(AnnotationRetention.SOURCE)
annotation class MenuType {
    companion object {
        const val ADD = "Add"
        const val UPDATE = "Update"
        const val SEE_ALL = "See All"
        const val DELETE = "Delete"
        const val GENERATE = "Generate"

    }
}





package com.example.holvi

import androidx.compose.ui.test.SemanticsNodeInteraction

fun SemanticsNodeInteraction.currentText(): String? {
    for ((key, value) in fetchSemanticsNode().config) {
        if (key.name == "EditableText") {
            return value?.toString()

        }
    }
    return null
}
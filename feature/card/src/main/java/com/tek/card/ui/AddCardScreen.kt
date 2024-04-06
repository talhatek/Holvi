package com.tek.card.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getTextAfterSelection
import androidx.compose.ui.text.input.getTextBeforeSelection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tek.card.presentation.CardEffect
import com.tek.card.presentation.CrudCardViewModel
import com.tek.ui.HolviScaffold
import com.tek.ui.HolviTheme
import com.tek.ui.TopAppBarBackWithLogo
import com.tek.ui.holviButtonColors
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@Composable
fun AddCardScreen(navController: NavController) {
    val crudCardViewModel = get<CrudCardViewModel>()

    var cardNumber by remember { mutableStateOf(TextFieldValue("")) }
    var cardHolderName by remember { mutableStateOf(TextFieldValue("")) }
    var cardCvv by remember { mutableStateOf(TextFieldValue("")) }
    var cardExp by remember { mutableStateOf(TextFieldValue("")) }

    val snackState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(true) {
        crudCardViewModel.cardEffect.collectLatest {
            scope.launch {
                snackState.showSnackbar(
                    it.message
                )
            }
            if (it is CardEffect.Success) {
                cardNumber = TextFieldValue("")
            }
        }
    }
    HolviScaffold(
        topBar = {
            TopAppBarBackWithLogo(navController = navController)
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .padding(top = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Input(
                modifier = Modifier.fillMaxWidth(.7f),
                value = cardNumber,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = "Card Number",
                onValueChanged = { value ->
                    if (remover(cardNumber.text, value.text)) {
                        cardNumber = if (cardNumber.text.last() == ' ') {
                            value.copy(
                                text = value.text.dropLast(1),
                                selection = TextRange(value.text.length.minus(1))
                            )
                        } else {
                            value
                        }
                        return@Input
                    }
                    if (value.text.replace(" ", "").last().isDigit().not()) {
                        return@Input
                    }
                    cardNumber = if ((value.text.replace(
                            " ",
                            ""
                        ).length.mod(4) == 1) and (value.text.length > 1)
                    ) {
                        TextFieldValue(
                            text = value.text.dropLast(1) + " " + value.text.last(),
                            selection = TextRange(value.text.length.plus(1))
                        )
                    } else {
                        value
                    }
                }
            )

            Input(
                modifier = Modifier.fillMaxWidth(.7f),
                value = cardHolderName,
                label = "Card Holder Name",
                onValueChanged = { value -> cardHolderName = value }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(.7f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Row(
                    modifier = Modifier
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Input(
                        modifier = Modifier,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        value = cardExp,
                        label = "Exp",
                        onValueChanged = { tmpValue ->
                            if (remover(cardExp.text, tmpValue.text)) {
                                cardExp = tmpValue
                                return@Input
                            }
                            if (tmpValue.text.last().isDigit().not()) {
                                return@Input
                            }

                            val length = tmpValue.text.replace("/", "").length

                            when (length) {
                                1 -> {
                                    cardExp = if (tmpValue.text.toInt() > 2) {
                                        TextFieldValue(
                                            text = "0$tmpValue.text/",
                                            selection = TextRange(3)
                                        )
                                    } else {
                                        TextFieldValue(
                                            text = tmpValue.text,
                                            selection = TextRange(tmpValue.text.length)
                                        )
                                    }
                                }

                                2 -> {
                                    if (tmpValue.text.all { it.isDigit() }) {
                                        cardExp = TextFieldValue(
                                            text = tmpValue.text.take(2) + "/",
                                            selection = TextRange(tmpValue.text.length.plus(1))
                                        )
                                    }
                                }

                                3 -> {
                                    cardExp = if (tmpValue.text.all { it.isDigit() }) {
                                        TextFieldValue(
                                            text = tmpValue.text.take(2) + "/" + tmpValue.text.last(),
                                            selection = TextRange(tmpValue.text.length.plus(1))
                                        )
                                    } else {
                                        TextFieldValue(
                                            text = tmpValue.text,
                                            selection = TextRange(tmpValue.text.length)
                                        )
                                    }
                                }

                                4 -> {
                                    cardExp = TextFieldValue(
                                        text = tmpValue.text,
                                        selection = TextRange(tmpValue.text.length)
                                    )
                                }
                            }
                        }
                    )
                }

                Row(
                    modifier = Modifier
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Input(
                        modifier = Modifier,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        value = cardCvv,
                        label = "Cvv",
                        onValueChanged = { value ->
                            if (value.text.any { !it.isDigit() }) {
                                return@Input
                            }
                            if (value.text.length <= 3) {
                                cardCvv = value
                            }
                        }
                    )
                }
            }

            Button(modifier = Modifier
                .padding(top = 40.dp)
                .testTag("addButton"),
                colors = holviButtonColors(),
                onClick = {
                    crudCardViewModel.add(
                        cardHolder = cardHolderName.text.trim(),
                        cardNumber = cardNumber.text.replace(" ", ""),
                        cvv = cardCvv.text,
                        exp = cardExp.text
                    )
                }) {
                Text(text = "Add", style = HolviTheme.typography.body)
            }

            SnackbarHost(hostState = snackState, Modifier)

        }
    }
}

private fun remover(oldValue: String, newValue: String): Boolean {
    return oldValue.length > newValue.length
}

private fun insertText(textFieldValue: TextFieldValue, insertText: String): TextFieldValue {
    val maxChars = textFieldValue.text.length
    val textBeforeSelection = textFieldValue.getTextBeforeSelection(maxChars)
    val textAfterSelection = textFieldValue.getTextAfterSelection(maxChars)
    val newText = "$textBeforeSelection$insertText$textAfterSelection"
    val newCursorPosition = textBeforeSelection.length + insertText.length
    return TextFieldValue(
        text = newText,
        selection = TextRange(newCursorPosition)
    )
}

@Composable
fun Input(
    modifier: Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    value: TextFieldValue,
    label: String,
    onValueChanged: (input: TextFieldValue) -> Unit
) {

    OutlinedTextField(
        modifier = modifier
            .testTag(label),
        value = value,
        onValueChange = {
            onValueChanged.invoke(it)
        },
        label = {
            Text(
                text = label,
                style = HolviTheme.typography.body,
                color = HolviTheme.colors.appForeground,
            )
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = HolviTheme.colors.appForeground,
            unfocusedTextColor = HolviTheme.colors.appForeground,
            focusedIndicatorColor = HolviTheme.colors.appForeground,
            unfocusedIndicatorColor = HolviTheme.colors.appForeground,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            cursorColor = HolviTheme.colors.primaryBackground,
        ),
        singleLine = true,
        keyboardOptions = keyboardOptions
    )
}
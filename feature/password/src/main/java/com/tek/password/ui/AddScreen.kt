package com.tek.password.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tek.database.model.Password
import com.tek.password.presentation.AddPasswordState
import com.tek.password.presentation.AddViewModel
import com.tek.password.presentation.ClearFocus
import com.tek.ui.HolviScaffold
import com.tek.ui.HolviTheme
import com.tek.ui.TopAppBarBackWithLogo
import com.tek.ui.holviButtonColors
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@Composable
fun AddScreen(navController: NavController) {
    val addViewModel = get<AddViewModel>()
    var siteName by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val snackState = remember { SnackbarHostState() }

    val snackScope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        addViewModel.passwordAddState.collectLatest {
            when (it) {
                is AddPasswordState.Success -> {
                    snackScope.launch {
                        snackState.showSnackbar(
                            "Password inserted successfully!"
                        )
                    }
                }

                is AddPasswordState.Failure -> {
                    snackScope.launch {
                        snackState.showSnackbar(
                            it.message
                        )
                    }
                }

                else -> Unit
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
                .padding(top = paddingValues.calculateTopPadding()),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Input(
                label = "Site Name",
                viewModel = addViewModel,
                onValueChanged = { value -> siteName = value }
            )
            Input(
                label = "User Name",
                viewModel = addViewModel,
                onValueChanged = { value -> userName = value }
            )
            PasswordInput(
                label = "Password",
                viewModel = addViewModel,
                onValueChanged = { value -> password = value }
            )

            Button(modifier = Modifier
                .testTag("addButton"),
                colors = holviButtonColors(),
                onClick = {
                    addViewModel.addPassword(
                        Password(
                            id = 0,
                            siteName = siteName,
                            password = password,
                            userName = userName
                        )
                    )
                }) {
                Text(text = "Add", style = HolviTheme.typography.body)
            }

            SnackbarHost(hostState = snackState, Modifier)
        }
    }
}

@Composable
fun Input(
    label: String,
    viewModel: AddViewModel,
    onValueChanged: (input: String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var value by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.clearInputsSharedFlow.collect {
            if (it is ClearFocus.Clear) {
                focusManager.clearFocus()
                value = ""
            }
        }
    }

    OutlinedTextField(
        modifier = Modifier
            .testTag(label)
            .fillMaxWidth(.7f),
        value = value,
        onValueChange = {
            value = it
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
        singleLine = true
    )
}

@Composable
fun PasswordInput(
    label: String,
    viewModel: AddViewModel,
    onValueChanged: (input: String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var value by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.clearInputsSharedFlow.collect {
            if (it is ClearFocus.Clear) {
                focusManager.clearFocus()
                value = ""
            }
        }
    }
    LaunchedEffect(true) {
        viewModel.passwordStateFlow.collectLatest {
            if (it.isNotEmpty()) {
                value = it
                focusManager.clearFocus()
            }
        }
    }
    OutlinedTextField(
        modifier = Modifier
            .testTag(label)
            .fillMaxWidth(.7f),
        value = value,
        onValueChange = {
            value = it
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
        trailingIcon = {
            Box(
                modifier = Modifier
                    .testTag("generateIcon")
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        color = HolviTheme.colors.primaryBackground,
                        shape = CircleShape,
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(
                            color = Color.Black,
                            radius = 24.dp
                        ),
                        onClick = {
                            value = viewModel
                                .generatePassword()
                                .also(onValueChanged)
                        }
                    ),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "G",
                    color = HolviTheme.colors.primaryDarkBackground,
                    style = HolviTheme.typography.body
                )
            }
        }
    )
}

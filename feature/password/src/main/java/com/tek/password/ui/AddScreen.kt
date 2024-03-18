package com.tek.password.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import com.tek.database.model.Password
import com.tek.password.presentation.AddPasswordState
import com.tek.password.presentation.CrudViewModel
import com.tek.ui.HolviScaffold
import com.tek.ui.HolviTheme
import com.tek.ui.TopAppBarBackWithLogo
import com.tek.ui.holviButtonColors
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@Composable
fun AddScreen(navController: NavController) {
    val crudViewModel = get<CrudViewModel>()
    var siteName by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val snackState = remember { SnackbarHostState() }

    val snackScope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        crudViewModel.passwordAddState.collectLatest {
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
                viewModel = crudViewModel,
                onValueChanged = { value -> siteName = value }
            )
            Input(
                label = "User Name",
                viewModel = crudViewModel,
                onValueChanged = { value -> userName = value }
            )
            PasswordInput(
                label = "Password",
                viewModel = crudViewModel,
                onValueChanged = { value -> password = value }
            )

            Button(modifier = Modifier
                .testTag("addButton"),
                colors = holviButtonColors(),
                onClick = {
                    crudViewModel.add(
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

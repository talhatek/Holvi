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
fun UpdateScreen(navController: NavController, item: Password) {
    val crudViewModel = get<CrudViewModel>()
    var siteName by remember { mutableStateOf(item.siteName) }
    var userName by remember { mutableStateOf(item.userName) }
    var password by remember { mutableStateOf(item.password) }
    val snackState = remember { SnackbarHostState() }

    val snackScope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        crudViewModel.passwordAddState.collectLatest {
            when (it) {
                is AddPasswordState.Success -> {
                    snackScope.launch {
                        snackState.showSnackbar(
                            "Password updated successfully!"
                        )
                    }
                    navController.popBackStack()
                }

                is AddPasswordState.Failure -> {
                    snackScope.launch {
                        snackState.showSnackbar(
                            it.message
                        )
                    }
                }
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
                defaultValue = siteName,
                label = "Site Name",
                viewModel = crudViewModel,
                onValueChanged = { value -> siteName = value }
            )
            Input(
                defaultValue = userName,
                label = "User Name",
                viewModel = crudViewModel,
                onValueChanged = { value -> userName = value }
            )
            PasswordInput(
                defaultValue = password,
                label = "Password",
                viewModel = crudViewModel,
                onValueChanged = { value -> password = value }
            )

            Button(modifier = Modifier
                .testTag("updateButton"),
                colors = holviButtonColors(),
                onClick = {
                    crudViewModel.add(
                        Password(
                            id = item.id,
                            siteName = siteName,
                            password = password,
                            userName = userName
                        )
                    )
                }) {
                Text(text = "Update", style = HolviTheme.typography.body)
            }

            SnackbarHost(hostState = snackState, Modifier)
        }
    }
}

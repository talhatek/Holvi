package com.example.holvi.ui.deleteActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.holvi.theme.HolviTheme
import com.example.holvi.ui.common.composable.BottomButton
import com.example.holvi.ui.common.composable.TopAppBarBackWithLogo
import com.example.holvi.ui.deleteActivity.composable.HolviChooseSiteDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

class DeleteActivity : ComponentActivity() {

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HolviTheme {
                val scaffoldState = rememberScaffoldState()
                var siteChooser by remember { mutableStateOf(false) }
                var selectedSiteName by remember { mutableStateOf("Choose a site") }
                val deleteViewModel = get<DeleteViewModel>()
                val scope = rememberCoroutineScope()
                val siteNames =
                    deleteViewModel.getAllSiteNames().collectAsState(initial = listOf()).value
                Scaffold(
                    topBar = {
                        TopAppBarBackWithLogo {
                            this.finish()
                        }
                    },
                    bottomBar = {
                        BottomButton(text = "Delete") {
                            scope.launch(Dispatchers.IO) {
                                deleteViewModel.delete(selectedSiteName)
                            }
                        }
                    },
                    scaffoldState = scaffoldState
                ) {
                    LaunchedEffect(key1 = true) {
                        deleteViewModel.passwordDeleteState.collect {
                            when (it) {
                                is DeleteViewModel.DeletePasswordState.Success -> {
                                    scaffoldState.snackbarHostState.showSnackbar("Site deleted successfully.")
                                    selectedSiteName = "Choose another site"
                                }
                                is DeleteViewModel.DeletePasswordState.Failure -> {
                                    scaffoldState.snackbarHostState.showSnackbar("Site could not deleted.")
                                }
                                is DeleteViewModel.DeletePasswordState.SuccessEmpty -> {
                                    scaffoldState.snackbarHostState.showSnackbar("Something went wrong.")

                                }
                                else -> Unit
                            }
                        }
                    }
                    if (siteChooser) {
                        HolviChooseSiteDialog(
                            siteList = siteNames,
                            onItemSelected = {
                                selectedSiteName = it
                                siteChooser = false
                            },
                            onDismiss = { siteChooser = false })
                    }
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                modifier = Modifier
                                    .padding(vertical = 4.dp)
                                    .clickable { siteChooser = true },
                                text = selectedSiteName
                            )
                            Divider(Modifier.fillMaxWidth(.7f), color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
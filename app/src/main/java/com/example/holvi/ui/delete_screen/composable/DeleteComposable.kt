package com.example.holvi.ui.delete_screen.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.holvi.theme.HolviTheme
import com.example.holvi.theme.PoppinsRegular
import com.example.holvi.theme.SecondPrimary
import com.example.holvi.ui.common.composable.BottomButton
import com.example.holvi.ui.common.composable.TopAppBarBackWithLogo
import com.example.holvi.ui.delete_screen.DeleteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@ExperimentalComposeUiApi
@Composable
fun DeleteScreen(navController: NavController) {
    val scaffoldState = rememberScaffoldState()
    var siteChooser by remember { mutableStateOf(false) }
    var selectedSiteName by remember { mutableStateOf("Choose a site") }
    val deleteViewModel = get<DeleteViewModel>()
    val scope = rememberCoroutineScope()
    val siteNames =
        deleteViewModel.getAllSiteNames().collectAsState(initial = listOf()).value
    HolviTheme {
        Scaffold(
            topBar = {
                TopAppBarBackWithLogo {
                    navController.popBackStack()
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
            LaunchedEffect(key1 = true) {
                deleteViewModel.isEmptyState.collect {
                    when (it) {
                        is DeleteViewModel.IsEmptyState.Empty -> {
                            scaffoldState.snackbarHostState.showSnackbar("You don't have any password.")

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
            Box(contentAlignment = Center, modifier = Modifier.fillMaxSize()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .clickable {
                                if (siteNames.isEmpty())
                                    deleteViewModel.warnUi()
                                else {
                                    siteChooser = true
                                }

                            },
                        text = selectedSiteName
                    )
                    Divider(Modifier.fillMaxWidth(.7f), color = Color.White)
                }
            }
        }
    }
}

@Composable
fun HolviDropdown(
    data: List<Int>,
    defaultHint: MutableState<String>,
    onItemSelected: (length: Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val tmpData = data.toMutableList()
    var selectedTmpData by remember { defaultHint }
    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.clickable {
        expanded = !expanded
    }) { // Anchor view
        Text(
            text = selectedTmpData, style = TextStyle(
                fontFamily = PoppinsRegular,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
            )
        ) // City name label
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            "Expand collapse",
            tint = Color.White,
            modifier = Modifier
                .size(24.dp)
                .align(alignment = CenterVertically)
        )
        DropdownMenu(
            expanded = expanded,
            modifier = Modifier.background(SecondPrimary),
            onDismissRequest = {
                expanded = false
            }) {
            tmpData.forEach {
                if (selectedTmpData == it.toString())
                    return@forEach
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        selectedTmpData = it.toString()
                        onItemSelected.invoke(it)
                    },
                ) {
                    Text(
                        text = it.toString(),
                        style = TextStyle(
                            fontFamily = PoppinsRegular,
                            fontWeight = FontWeight.Normal,
                            fontSize = 24.sp,
                            color = Color.White
                        ),
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }

            }

        }
    }

}

@ExperimentalComposeUiApi
@Composable
fun HolviChooseSiteDialog(
    siteList: List<String>,
    onItemSelected: (name: String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss.invoke() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(contentAlignment = BottomCenter) {
            LazyColumn(content = {
                items(siteList) { site ->
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onItemSelected.invoke(site) }
                    ) {
                        Text(modifier = Modifier.align(Center), text = site)
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .background(color = Color.White)
                    )
                }
            })
        }

    }
}
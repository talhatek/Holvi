package com.example.holvi.ui.port_screen

import android.content.ClipData
import android.content.ClipboardManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.holvi.ui.common.composable.TopAppBarBackWithLogo
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortScreen(navController: NavController) {
    val viewModel = get<PortViewModel>()

    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val modalSheet = rememberModalBottomSheetState(
        confirmValueChange = { true },
        skipPartiallyExpanded = true,
    )
    var exportDialogVisible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = true, block = {
        viewModel.portResult.collectLatest {
            when (it) {
                is PortViewModel.PortResult.ImportSuccess -> {
                    val clipData =
                        ClipData.newPlainText("label", it.id)
                    (context.getSystemService(ComponentActivity.CLIPBOARD_SERVICE) as ClipboardManager)
                        .setPrimaryClip(clipData)
                    snackbarHostState.showSnackbar(it.id + " is your key to export again. Copied to clipboard!")
                }

                is PortViewModel.PortResult.ExportSuccess -> snackbarHostState.showSnackbar(it.message)
                is PortViewModel.PortResult.Error -> snackbarHostState.showSnackbar(it.message)
            }
        }
    })
    Scaffold(
        topBar = {
            TopAppBarBackWithLogo {
                navController.popBackStack()
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()),
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(onClick = { viewModel.onEvent(PortViewModel.PortEvent.Import) }) {
                Text(text = "Import", color = Color.White)

            }
            Button(onClick = {
                exportDialogVisible = true

            }) {
                Text(text = "Export", color = Color.White)
            }
        }
        if (exportDialogVisible) {
            ExportModelSheet(
                sheetState = modalSheet,
                onDismiss = { exportDialogVisible = false },
                onExport = { path ->
                    exportDialogVisible = false
                    viewModel.onEvent(PortViewModel.PortEvent.Export(pathId = path))
                })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportModelSheet(
    sheetState: SheetState,
    onDismiss: (isItemUpdated: Boolean) -> Unit,
    onExport: (value: String) -> Unit,
) {
    val itemWidth = LocalConfiguration.current.screenWidthDp.div(5).dp

    val snackState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val firstFocusRequester = remember { FocusRequester() }
    val secondFocusRequester = remember { FocusRequester() }
    val thirdFocusRequester = remember { FocusRequester() }
    val fourthFocusRequester = remember { FocusRequester() }
    var pathIndexFirst by remember {
        mutableStateOf("")
    }
    var pathIndexSecond by remember {
        mutableStateOf("")
    }
    var pathIndexThird by remember {
        mutableStateOf("")
    }
    var pathIndexFourth by remember {
        mutableStateOf("")
    }

    fun findValue(old: String, new: String): String {
        return when {
            new.isBlank() or (old == new) -> new
            else -> new.filterNot { it in old }.ifBlank { old }
        }
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss.invoke(false) },
        sheetState = sheetState,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .imePadding(),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val focusManager = LocalFocusManager.current
                OutlinedTextField(
                    modifier = Modifier
                        .size(itemWidth)
                        .focusRequester(firstFocusRequester),
                    value = pathIndexFirst,
                    onValueChange = {
                        pathIndexFirst = findValue(pathIndexFirst, it)
                        if (pathIndexFirst.isNotBlank())
                            secondFocusRequester.requestFocus()

                    },
                    textStyle = TextStyle(color = Color.White, textAlign = TextAlign.Center),
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Color.Transparent,
                        selectionColors = TextSelectionColors(Color.Transparent, Color.Transparent)
                    ),
                    singleLine = true,
                )
                OutlinedTextField(
                    modifier = Modifier
                        .size(itemWidth)
                        .focusRequester(secondFocusRequester),
                    value = pathIndexSecond,
                    onValueChange = {
                        pathIndexSecond = findValue(pathIndexSecond, it)
                        if (pathIndexSecond.isNotBlank())
                            thirdFocusRequester.requestFocus()
                    },
                    textStyle = TextStyle(color = Color.White, textAlign = TextAlign.Center),
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Color.Transparent,
                        selectionColors = TextSelectionColors(Color.Transparent, Color.Transparent)
                    ),
                    singleLine = true,
                )
                OutlinedTextField(
                    modifier = Modifier
                        .size(itemWidth)
                        .focusRequester(thirdFocusRequester),
                    value = pathIndexThird,
                    onValueChange = {
                        pathIndexThird = findValue(pathIndexThird, it)
                        if (pathIndexThird.isNotBlank())
                            fourthFocusRequester.requestFocus()
                    },
                    textStyle = TextStyle(color = Color.White, textAlign = TextAlign.Center),
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Color.Transparent,
                        selectionColors = TextSelectionColors(Color.Transparent, Color.Transparent)
                    ),
                    singleLine = true,
                )

                OutlinedTextField(
                    modifier = Modifier
                        .size(itemWidth)
                        .focusRequester(fourthFocusRequester),
                    value = pathIndexFourth,
                    onValueChange = {
                        pathIndexFourth = findValue(pathIndexFourth, it)
                        if (pathIndexSecond.isNotBlank()) {
                            fourthFocusRequester.freeFocus()
                            focusManager.clearFocus()
                        }
                    },
                    textStyle = TextStyle(color = Color.White, textAlign = TextAlign.Center),
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Color.Transparent,
                        selectionColors = TextSelectionColors(Color.Transparent, Color.Transparent)
                    ),
                    singleLine = true,
                )
            }

            Button(
                modifier = Modifier
                    .padding(
                        bottom = 128.dp,
                        top = 64.dp
                    ),
                onClick = {
                    listOf(
                        pathIndexFirst,
                        pathIndexSecond,
                        pathIndexThird,
                        pathIndexFourth
                    ).joinToString { "" }.also { path ->
                        if (path.length != 4) {
                            scope.launch {
                                snackState.showSnackbar("Please control your key.")
                            }
                        } else {
                            onExport.invoke(path)
                        }
                    }
                }) {
                Text(text = "Export")
            }

            SnackbarHost(hostState = snackState, Modifier)

        }
    }
}
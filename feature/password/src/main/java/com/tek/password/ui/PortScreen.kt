package com.tek.password.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.view.KeyEvent
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tek.password.presentation.PortEvent
import com.tek.password.presentation.PortResult
import com.tek.password.presentation.PortViewModel
import com.tek.ui.HolviScaffold
import com.tek.ui.HolviTheme
import com.tek.ui.TopAppBarBackWithLogo
import com.tek.ui.holviButtonColors
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
                is PortResult.ImportSuccess -> {
                    val clipData =
                        ClipData.newPlainText("label", it.id)
                    (context.getSystemService(ComponentActivity.CLIPBOARD_SERVICE) as ClipboardManager)
                        .setPrimaryClip(clipData)
                    snackbarHostState.showSnackbar(it.id + " is your key to export again. Copied to clipboard!")
                }

                is PortResult.ExportSuccess -> snackbarHostState.showSnackbar(it.message)
                is PortResult.Error -> snackbarHostState.showSnackbar(it.message)
            }
        }
    })
    HolviScaffold(
        topBar = {
            TopAppBarBackWithLogo(navController = navController)
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

            Button(
                onClick = { viewModel.onEvent(PortEvent.Import(shouldAwait = true)) },
                colors = holviButtonColors()
            ) {
                Text(
                    text = "Import",
                    color = HolviTheme.colors.primaryForeground,
                    style = HolviTheme.typography.body
                )

            }
            Button(
                onClick = {
                    exportDialogVisible = true

                }, colors = holviButtonColors()
            ) {
                Text(
                    text = "Export",
                    color = HolviTheme.colors.primaryForeground,
                    style = HolviTheme.typography.body
                )
            }
        }
        if (exportDialogVisible) {
            ExportModelSheet(
                pathLength = 4,
                sheetState = modalSheet,
                onDismiss = { exportDialogVisible = false },
                onExport = { path ->
                    exportDialogVisible = false
                    viewModel.onEvent(PortEvent.Export(pathId = path))
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportModelSheet(
    pathLength: Int,
    sheetState: SheetState,
    onDismiss: (isItemUpdated: Boolean) -> Unit,
    onExport: (value: String) -> Unit,
) {

    val itemWidth = LocalConfiguration.current.screenWidthDp.div(pathLength.plus(1)).dp
    val snackState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var path by remember {
        mutableStateOf(" ".repeat(pathLength))
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss.invoke(false) },
        sheetState = sheetState,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        containerColor = HolviTheme.colors.appBackground
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

                for (i in 0..<pathLength) {
                    CodeBox(
                        itemWidth = itemWidth,
                        onValueChanged = {
                            path = path.toCharArray().also { charArray ->
                                charArray[i] = it.firstOrNull() ?: " ".first()
                            }.concatToString()

                            if (it.isNotBlank()) {
                                if (i == pathLength - 1) {
                                    focusManager.clearFocus()
                                } else {
                                    focusManager.moveFocus(FocusDirection.Next)
                                }
                            }
                        }
                    )
                }
            }

            Button(
                modifier = Modifier
                    .padding(
                        bottom = 128.dp,
                        top = 64.dp
                    ),
                onClick = {
                    if (path.trim().length != 4) {
                        scope.launch {
                            snackState.showSnackbar("Please control your key.")
                        }
                    } else {
                        onExport.invoke(path)
                    }

                },
                colors = holviButtonColors()
            ) {
                Text(
                    text = "Export",
                    style = HolviTheme.typography.body,
                    color = HolviTheme.colors.primaryForeground
                )
            }

            SnackbarHost(hostState = snackState, Modifier)

        }
    }
}

@Composable
fun CodeBox(itemWidth: Dp, onValueChanged: (value: String) -> Unit) {
    var value by remember {
        mutableStateOf("")
    }


    OutlinedTextField(
        modifier = Modifier
            .imePadding()
            .onKeyEvent {
                if (it.key.nativeKeyCode == KeyEvent.KEYCODE_DEL) {
                    value = ""
                    onValueChanged(value)
                }
                true
            }
            .size(itemWidth),
        value = value,
        onValueChange = {
            value = findValue(value, it)
            onValueChanged(value)

        },
        textStyle = TextStyle(
            color = HolviTheme.colors.appForeground, textAlign = TextAlign.Center
        ),
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = Color.Transparent,
            selectionColors = TextSelectionColors(Color.Transparent, Color.Transparent),
            focusedBorderColor = HolviTheme.colors.appForeground,
            unfocusedBorderColor = HolviTheme.colors.appForeground.copy(alpha = 0.4f)
        ),
        singleLine = true,
    )
}

fun findValue(old: String, new: String): String {
    return when {
        new.isBlank() or (old == new) -> new
        else -> new.filterNot { it in old }.ifBlank { old }.run { this }
    }
}

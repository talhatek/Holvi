package com.example.holvi.ui.all_screen.composable

import android.content.ClipData
import android.content.ClipboardManager
import androidx.activity.ComponentActivity
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.holvi.R
import com.example.holvi.db.model.Password
import com.example.holvi.theme.*
import com.example.holvi.ui.all_screen.AllViewModel
import com.example.holvi.ui.all_screen.PasswordsState
import com.example.holvi.ui.common.composable.TopAppBarBackWithLogo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@Composable
fun AllScreen(navController: NavController) {

    val allViewModel = get<AllViewModel>()
    val passwordsState = allViewModel.allPasswords.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    HolviTheme {
        Scaffold(
            topBar = {
                TopAppBarBackWithLogo {
                    navController.popBackStack()
                }
            },
            scaffoldState = scaffoldState
        ) {
            when (passwordsState.value) {
                is PasswordsState.Loading -> {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator()
                    }
                }

                is PasswordsState.Success -> {
                    var isOpen by remember {
                        mutableStateOf(false)
                    }
                    val textFocusRequester = remember {
                        FocusRequester()
                    }

                    var searchQuery by remember {
                        mutableStateOf("")
                    }
                    LaunchedEffect(key1 = searchQuery) {
                        allViewModel.searchQuery.emit(searchQuery)
                    }
                    LaunchedEffect(key1 = isOpen) {
                        if (isOpen) {
                            delay(200L)
                            textFocusRequester.requestFocus()
                        }
                    }

                    val screenSizeDp = DpSize(
                        LocalConfiguration.current.screenWidthDp.dp,
                        LocalConfiguration.current.screenHeightDp.dp
                    )
                    val searchSize by animateSizeAsState(
                        targetValue = if (isOpen)
                            Size(screenSizeDp.width.value, 60f)
                        else
                            Size(40f, 40f),
                        animationSpec = tween(200, easing = LinearEasing)
                    )
                    Column(
                        modifier = Modifier
                            .padding(8.dp, 8.dp)
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {

                        Box(
                            modifier = Modifier
                                .align(Alignment.End)
                                .size(searchSize.width.dp, searchSize.height.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colors.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (isOpen)
                                    TextField(
                                        modifier = Modifier
                                            .focusRequester(textFocusRequester),
                                        value = searchQuery,
                                        onValueChange = { searchQuery = it },
                                        colors = TextFieldDefaults.textFieldColors(
                                            backgroundColor = Color.Transparent,
                                            cursorColor = Color.White,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent
                                        ),
                                        singleLine = true,
                                    )
                                Crossfade(targetState = isOpen) { state ->
                                    if (state) {
                                        Icon(
                                            modifier = Modifier
                                                .clickable {
                                                    if (isOpen)
                                                        allViewModel.getAll()
                                                    searchQuery = ""
                                                    isOpen = isOpen.not()

                                                },
                                            painter = painterResource(id = R.drawable.ic_close),
                                            contentDescription = "search"
                                        )
                                    } else {
                                        Icon(
                                            modifier = Modifier
                                                .clickable {
                                                    if (isOpen)
                                                        allViewModel.getAll()
                                                    searchQuery = ""
                                                    isOpen = isOpen.not()

                                                },
                                            painter = painterResource(id = R.drawable.ic_search_24),
                                            contentDescription = "search"
                                        )
                                    }
                                }

                            }

                        }
                        if ((passwordsState.value is PasswordsState.Success)) {
                            if ((passwordsState.value as PasswordsState.Success).data.isEmpty()) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(text = "You don't have any saved password.")
                                }
                            } else if ((passwordsState.value as PasswordsState.Success).data.isNotEmpty()) {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(),
                                    content = {
                                        if (passwordsState.value is PasswordsState.Success) {
                                            items((passwordsState.value as PasswordsState.Success).data) { item ->
                                                PasswordItem(password = item) {
                                                    coroutineScope.launch {
                                                        scaffoldState.snackbarHostState.showSnackbar(
                                                            "Password copied to clipboard!"
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                    },
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(
                                        16.dp,
                                        Alignment.Top
                                    ),
                                    contentPadding = PaddingValues(top = 16.dp)

                                )
                            }
                        }
                    }
                }

                is PasswordsState.Error -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = (passwordsState.value as PasswordsState.Error).message,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else -> Unit
            }


        }

    }
}

@Composable
fun PasswordItem(password: Password, onCopied: () -> Unit) {
    var passwordText by remember { mutableStateOf("*".repeat(password.password.length)) }
    var resId by remember { mutableStateOf(R.drawable.ic_invisible) }
    var visible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth(.8f),
        elevation = 12.dp,
        backgroundColor = if (password.id % 2 == 0) SecondPrimaryDark else PrimaryGreen
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = password.siteName,
                style = TextStyle(
                    fontFamily = PoppinsSemiBold,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                ),
                color = Color.White,
                maxLines = 1,
            )
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth(.75f)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(.45f),
                        text = passwordText,
                        style = TextStyle(
                            fontFamily = PoppinsRegular,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                        ),
                        color = Color.White,
                        maxLines = 1
                    )
                    Spacer(Modifier.fillMaxWidth(.05f))
                    IconButton(
                        onClick = {
                            if (visible) {
                                passwordText = "*".repeat(password.password.length)
                                resId = R.drawable.ic_invisible
                            } else {
                                passwordText = password.password
                                resId = R.drawable.ic_visible
                            }
                            visible = !visible

                        },
                        enabled = true,
                        modifier = Modifier
                            .fillMaxWidth(.15f)
                    ) {
                        Icon(
                            painter = painterResource(id = resId),
                            contentDescription = "hiddenOrShown",
                            tint = Color.White
                        )
                    }
                    Spacer(Modifier.fillMaxWidth(.05f))
                    IconButton(
                        onClick = {
                            try {
                                val clipboardManager =
                                    context.getSystemService(ComponentActivity.CLIPBOARD_SERVICE) as ClipboardManager
                                val clipData =
                                    ClipData.newPlainText(
                                        "label",
                                        password.password
                                    )
                                clipboardManager.setPrimaryClip(clipData)
                                onCopied.invoke()
                            } catch (ex: Exception) {

                            }
                        },
                        enabled = true,
                        modifier = Modifier
                            .fillMaxWidth(.15f)

                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_copy),
                            contentDescription = "hiddenOrShown",
                            tint = Color.White
                        )
                    }
                }
            }
            Text(
                text = password.userName,
                style = TextStyle(
                    fontFamily = PoppinsRegular,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                ),
                color = Color.White,
                maxLines = 1,
            )

        }
    }

}
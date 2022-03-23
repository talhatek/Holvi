package com.example.holvi.ui.all_screen.composable

import android.content.ClipData
import android.content.ClipboardManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.holvi.R
import com.example.holvi.db.model.Password
import com.example.holvi.theme.*
import com.example.holvi.ui.all_screen.AllViewModel
import com.example.holvi.ui.all_screen.PasswordsState
import com.example.holvi.ui.common.composable.TopAppBarBackWithLogo
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

@Composable
fun AllScreen(navController: NavController) {

    val allViewModel = get<AllViewModel>()
    val passwordsState = allViewModel.allPasswords.value
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
            when (passwordsState) {
                is PasswordsState.Loading -> {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator()
                    }
                }
                is PasswordsState.Success -> {
                    if (passwordsState.data.isEmpty()) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(text = "You don't have any saved password.")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            content = {
                                items(passwordsState.data) { item ->
                                    PasswordItem(password = item) {
                                        coroutineScope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar("Password copied to clipboard!")
                                        }
                                    }
                                }
                            },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(
                                16.dp,
                                Alignment.CenterVertically
                            ),
                            contentPadding = PaddingValues(top = 16.dp)

                        )
                    }
                }
                is PasswordsState.Error -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(text = passwordsState.message, textAlign = TextAlign.Center)
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
                color = Color.White
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
                        modifier = Modifier.fillMaxWidth(.35f),
                        text = passwordText,
                        style = TextStyle(
                            fontFamily = PoppinsRegular,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        ),


                        color = Color.White
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
                    Spacer(Modifier.fillMaxWidth(.15f))
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
                color = Color.White
            )

        }
    }

}
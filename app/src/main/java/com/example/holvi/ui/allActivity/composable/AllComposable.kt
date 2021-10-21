package com.example.holvi.ui.allActivity.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.holvi.R
import com.example.holvi.db.Password
import com.example.holvi.theme.PoppinsRegular
import com.example.holvi.theme.PoppinsSemiBold
import com.example.holvi.theme.PrimaryGreen
import com.example.holvi.theme.SecondPrimaryDark


@Composable
fun PasswordItem(password: Password) {
    var passwordText by remember { mutableStateOf("*".repeat(password.password.length)) }
    var resId by remember { mutableStateOf(R.drawable.ic_invisible) }
    var visible by remember { mutableStateOf(false) }
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
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(.5f)
                ) {
                    Text(
                        text = passwordText,
                        style = TextStyle(
                            fontFamily = PoppinsRegular,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        ),


                        color = Color.White
                    )
                    Spacer(Modifier.weight(.1f))
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
                            .fillMaxWidth()
                            .then(Modifier.size(28.dp))
                    ) {
                        Icon(
                            painter = painterResource(id = resId),
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
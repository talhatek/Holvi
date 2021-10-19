package com.example.holvi.ui.allActivity.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.holvi.R
import com.example.holvi.theme.PoppinsRegular
import com.example.holvi.theme.PoppinsSemiBold

@Preview(showSystemUi = true)
@Composable
fun PasswordItem() {
    Card(
        modifier = Modifier
            .fillMaxWidth(.8f),
        elevation = 12.dp,
        backgroundColor = Color(0xFF2F2A34)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Facebook",
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
                        text = "**********",
                        style = TextStyle(
                            fontFamily = PoppinsRegular,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,


                            ),


                        color = Color.White
                    )
                    Spacer(Modifier.weight(.1f))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_invisible),
                        contentDescription = "hiddenOrShown",
                        tint = Color.White
                    )
                }
            }
            Text(
                text = "talhatek",
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
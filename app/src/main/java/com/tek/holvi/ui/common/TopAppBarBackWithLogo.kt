package com.tek.holvi.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tek.holvi.R
import com.tek.holvi.theme.PoppinsBold
import com.tek.holvi.theme.PrimaryTextColor

@Composable
fun TopAppBarBackWithLogo(onBackClicked: () -> Unit) {
    CenterTopAppBar(
        title = {
            Text(
                text = "Holvi",
                style = TextStyle(
                    color = PrimaryTextColor,
                    fontFamily = PoppinsBold,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { onBackClicked.invoke() },
                enabled = true,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    tint = PrimaryTextColor
                )
            }
        }
    )

}


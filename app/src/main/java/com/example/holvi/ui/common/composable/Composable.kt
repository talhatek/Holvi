package com.example.holvi.ui.common.composable

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.holvi.R
import com.example.holvi.theme.PoppinsBold
import com.example.holvi.theme.PoppinsLight
import com.example.holvi.theme.PoppinsRegular
import com.example.holvi.theme.SecondPrimary


@Composable
fun CircleTextButton(text: String, percentage: Int, onClicked: () -> Unit) {
    BoxWithConstraints {
        Button(
            onClick = {
                onClicked.invoke()
            },
            modifier = Modifier.size((if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) this.maxHeight else this.maxWidth / 100) * percentage),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
            elevation = ButtonDefaults.elevation(defaultElevation = 8.dp, pressedElevation = 12.dp)
        ) {
            BoxWithConstraints {
                Text(
                    text = text.toUpperCase(Locale.current),
                    fontSize = ((if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) this.maxHeight else this.maxWidth / 100) * 50).value.sp,
                    style = TextStyle(
                        color = Color.Black,
                        fontFamily = PoppinsBold,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                )

            }

        }
    }
}

@Composable
fun TopAppBarOnlyIcon(@DrawableRes res: Int, onIconClicked: () -> Unit) {
    CenterTopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxSize()) {
                IconButton(
                    onClick = { onIconClicked.invoke() },
                    enabled = true,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = res),
                        contentDescription = "Back"
                    )
                }
            }
        }
    )

}

@Composable
fun TopAppBarBackWithLogo(onBackClicked: () -> Unit) {
    CenterTopAppBar(
        title = {
            Text(
                text = "Holvi",
                textDecoration = TextDecoration.Underline,
                style = TextStyle(
                    color = Color.Black,
                    fontFamily = PoppinsRegular,
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
                    contentDescription = "Back"
                )
            }
        }
    )

}


@Composable
fun BottomButton(text: String, onClicked: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.1f), onClick = {
            onClicked.invoke()
        }, shape = RectangleShape
    ) {
        Text(
            text = text,
            style = TextStyle(
                color = SecondPrimary,
                fontWeight = FontWeight.Light,
                fontFamily = PoppinsLight,
                fontSize = 24.sp
            )
        )

    }
}

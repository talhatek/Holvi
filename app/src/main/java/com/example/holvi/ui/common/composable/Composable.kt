package com.example.holvi.ui.common.composable

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
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
import com.example.holvi.utils.rememberWindowInfo


@Composable
fun CircleTextButton(text: String, percentage: Int, onClicked: () -> Unit) {
    val windowInfo = rememberWindowInfo()

    BoxWithConstraints {
        val buttonSize =
            windowInfo.minDimension.div(10f)

        Button(
            onClick = {
                onClicked.invoke()
            },
            modifier = Modifier
                .size(buttonSize)
                .testTag("G"),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
            elevation = ButtonDefaults.elevation(defaultElevation = 8.dp, pressedElevation = 12.dp)
        ) {
            BoxWithConstraints {
                val fontSize =
                    ((this.maxWidth / 100) * 50).value.sp
                Log.e("sizeDebug", "font size ${fontSize.value}")

                Text(
                    text = text.toUpperCase(Locale.current),
                    fontSize = fontSize,
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
fun CircleIconButton(@DrawableRes iconIdRes: Int, percentage: Int, onClicked: () -> Unit) {
    val windowInfo = rememberWindowInfo()

    val degree = remember { mutableStateOf(60f) }
    val angle: Float by animateFloatAsState(
        targetValue = degree.value,
        animationSpec = tween(
            durationMillis = 400, // duration
            easing = FastOutSlowInEasing
        ),
    )
    BoxWithConstraints {
        IconButton(
            modifier = Modifier
                .size(windowInfo.minDimension.div(10f))
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary),
            onClick = {
                degree.value = degree.value + 180f
                onClicked.invoke()
            }) {
            Icon(
                modifier = Modifier
                    .rotate(angle),
                painter = painterResource(id = iconIdRes),
                contentDescription = "Renew"
            )
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

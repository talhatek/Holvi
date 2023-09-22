package com.example.holvi.ui.common.composable

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.sp
import com.example.holvi.R
import com.example.holvi.theme.PoppinsBold
import com.example.holvi.theme.PoppinsLight
import com.example.holvi.theme.PoppinsRegular
import com.example.holvi.theme.SecondPrimary
import com.example.holvi.utils.rememberWindowInfo


@Composable
fun CircleTextButton(text: String, onClicked: () -> Unit) {
    val windowInfo = rememberWindowInfo()
    val buttonSize =
        windowInfo.minDimension.div(10f)
    BoxWithConstraints {
        IconButton(
            onClick = {
                onClicked.invoke()
            },
            modifier = Modifier
                .size(buttonSize)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .testTag("G"),
        ) {
            BoxWithConstraints {
                val fontSize =
                    maxOf(this.maxHeight, this.maxWidth).value.div(2).sp
                Text(
                    text = text.toUpperCase(Locale.current),
                    fontSize = fontSize,
                    style = TextStyle(
                        color = Color.Black,
                        fontFamily = PoppinsBold,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }
        }
    }
}

@Composable
fun CircleIconButton(@DrawableRes iconIdRes: Int, onClicked: () -> Unit) {
    val windowInfo = rememberWindowInfo()

    val degree = remember { mutableStateOf(90f) }
    val angle: Float by animateFloatAsState(
        targetValue = degree.value,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        ),
    )
    BoxWithConstraints {
        IconButton(
            modifier = Modifier
                .size(windowInfo.minDimension.div(10f))
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .testTag("Renew"),
            onClick = {
                degree.value = degree.value + 180f % 360
                onClicked.invoke()
            }) {
            Icon(
                modifier = Modifier
                    .rotate(angle),
                painter = painterResource(id = iconIdRes),
                contentDescription = "Renew",
                tint = Color.White
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
                        contentDescription = "Back",
                        tint = Color.White
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
                    contentDescription = "Back",
                    tint = Color.White
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

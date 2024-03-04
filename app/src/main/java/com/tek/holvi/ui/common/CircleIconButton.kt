package com.tek.holvi.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import com.tek.holvi.theme.PrimaryTextColor
import com.tek.holvi.utils.rememberWindowInfo

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
        label = "generate_icon_angle"
    )
    Box {
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
                tint = PrimaryTextColor
            )
        }
    }
}

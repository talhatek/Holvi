package com.tek.card.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tek.card.R
import com.tek.ui.HolviTheme
import kotlin.math.abs

@Composable
fun DetailedCard() {

    var axisY by remember { mutableFloatStateOf(0f) }

    var isCompletingAnimationActive by remember { mutableStateOf(false) }
    var isQuickDragAnimationActive by remember { mutableStateOf(false) }

    var animationDragAmount by remember { mutableFloatStateOf(0f) }

    CardContent(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = { _ ->
                        isCompletingAnimationActive = false
                    },
                    onDragEnd = {

                        isCompletingAnimationActive = false
                        isQuickDragAnimationActive = false


                        if (abs(animationDragAmount) > 12f) {
                            isQuickDragAnimationActive = true
                        } else {
                            isCompletingAnimationActive = true
                        }
                    },
                    onDragCancel = {

                    },
                    onHorizontalDrag = { _, dragAmount ->
                        val amount = dragAmount.div(2f)

                        axisY = if (amount < 0) {
                            (axisY - abs(amount)) % 360
                        } else {
                            (axisY + abs(amount)) % 360
                        }

                        animationDragAmount = amount
                    }
                )
            },
        frontSide = {
            CardFrontSide(
                bankColor = Color(0xff9fe870),
                number = "1111 1111 1111 1111",
                R.drawable.visa
            )
        },
        backSide = {
            CardBackSide(bankColor = Color(0xff9fe870), cvv = "111", exp = "01/01")
        },
        positionAxisY = if (isCompletingAnimationActive) {
            val completeTurningAnimation = remember { Animatable(axisY) }

            LaunchedEffect(isCompletingAnimationActive) {
                if (isCompletingAnimationActive) {
                    completeTurningAnimation.animateTo(
                        targetValue = if (abs(axisY.toInt()) % 360 <= 90) {
                            0f
                        } else if (abs(axisY.toInt()) % 360 in 91..270) {
                            if (abs(axisY.toInt()) % 360 <= 270f) {

                                if (axisY > 0) 180f else -180f
                            } else {
                                if (axisY > 0) 360f else -360f
                            }
                        } else {
                            if (axisY > 0) 360f else -360f
                        },
                        animationSpec = tween(300, easing = FastOutLinearInEasing)
                    ).endState
                }
            }
            axisY = completeTurningAnimation.value
            completeTurningAnimation.value
        } else if (isQuickDragAnimationActive) {
            val completeQuickDragAnimation = remember { Animatable(axisY) }

            LaunchedEffect(isQuickDragAnimationActive) {
                if (isQuickDragAnimationActive) {

                    val completeTurningAnimationState = completeQuickDragAnimation.animateTo(
                        targetValue = if (animationDragAmount > 0) {
                            360f * 2
                        } else {
                            -360f * 2
                        },
                        animationSpec = tween(1250, easing = LinearEasing)
                    ).endState

                    if (!completeTurningAnimationState.isRunning) {
                        isQuickDragAnimationActive = false
                    }
                }
            }
            axisY = completeQuickDragAnimation.value
            completeQuickDragAnimation.value
        } else {
            axisY
        },
    )
}


@Composable
fun CardContent(
    modifier: Modifier = Modifier,
    positionAxisY: Float,
    frontSide: @Composable () -> Unit = {},
    backSide: @Composable () -> Unit = {},
) {
    Card(
        modifier = modifier
            .graphicsLayer {
                rotationY = positionAxisY // Move card according to value of customY.
                cameraDistance = 14f * density
            },
    ) {

        // Here, logic is about coordinate system such as [0..90], [91..270], [270..360].
        if (abs(positionAxisY.toInt()) % 360 <= 90) {
            Box(
            ) {
                frontSide()
            }
        } else if (abs(positionAxisY.toInt()) % 360 in 91..270) {
            Box(
                Modifier
                    .graphicsLayer {
                        rotationY = 180f // Important to avoid mirror effect.
                    },
            ) {
                backSide()
            }
        } else {
            Box(
            ) {
                frontSide()
            }
        }
    }
}


@Composable
fun CardFrontSide(bankColor: Color, number: String, provider: Int) {

    Column(
        modifier = Modifier
            .size(400.dp, 200.dp)
            .background(bankColor)
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp),
        verticalArrangement = Arrangement.SpaceBetween,

        ) {
        Icon(
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.End),
            painter = painterResource(id = R.drawable.wise),
            contentDescription = "bank_icon"
        )

        Spacer(modifier = Modifier.wrapContentSize())

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = number, style = HolviTheme.typography.body)

            Icon(
                modifier = Modifier.size(48.dp),
                painter = painterResource(id = provider),
                contentDescription = "payment_icon"
            )
        }


    }
}

@Composable
fun CardBackSide(bankColor: Color, cvv: String, exp: String) {

    Column(
        modifier = Modifier
            .size(400.dp, 200.dp)
            .background(bankColor),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .padding(top = 8.dp)
                .background(Color.Black)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, start = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start)
        ) {
            Text(text = exp, style = HolviTheme.typography.body)
            Text(text = cvv, style = HolviTheme.typography.body)
        }

    }
}
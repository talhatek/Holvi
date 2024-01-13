package com.example.holvi.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.holvi.ui.extension.toPx
import kotlin.math.abs
import kotlin.math.max

val AppBarHeight = 56.dp
val AppBarHorizontalPadding = 4.dp
val TitleIconModifier = Modifier.fillMaxHeight()
var iconWidth = 72.dp - AppBarHorizontalPadding
var withoutIconWidth = 16.dp - AppBarHorizontalPadding

@Composable
fun CenterTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = 12.dp

) {
    val defLeftSectionWidth = if (navigationIcon == null) withoutIconWidth else iconWidth
    var leftSectionWidth by remember { mutableStateOf(defLeftSectionWidth) }
    var rightSectionWidth by remember { mutableStateOf(-1f) }
    var rightSectionPadding by remember { mutableStateOf(0f) }

    AppBar(
        backgroundColor,
        contentColor,
        elevation,
        PaddingValues(),
        RectangleShape,
        modifier
    ) {
        if (navigationIcon == null) {
            Spacer(Modifier.width(leftSectionWidth))
        } else {
            Row(
                TitleIconModifier.width(leftSectionWidth),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides (LocalContentColor.current.copy(alpha = 0.4f)),
                    content = navigationIcon
                )
            }
        }

        Row(
            Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leftSectionWidth != defLeftSectionWidth
                || rightSectionPadding != 0f
            ) {
                ProvideTextStyle(value = MaterialTheme.typography.titleSmall) {
                    CompositionLocalProvider(
                        LocalContentColor provides (LocalContentColor.current.copy(alpha = 0.4f)),

                        content = title
                    )
                }
            }
        }

        CompositionLocalProvider(
            LocalContentColor provides (LocalContentColor.current.copy(alpha = 0.4f)),
        ) {
            with(LocalDensity.current) {
                Row(
                    Modifier
                        .fillMaxHeight()
                        .padding(start = rightSectionPadding.toDp())
                        .onGloballyPositioned {
                            rightSectionWidth = it.size.width.toFloat()
                            if (leftSectionWidth == defLeftSectionWidth
                                && rightSectionWidth != -1f
                                && rightSectionPadding == 0f
                            ) {

                                val maxWidth = max(leftSectionWidth.value.toPx, rightSectionWidth)
                                leftSectionWidth = maxWidth.toDp()
                                rightSectionPadding = abs(rightSectionWidth - maxWidth)
                            }
                        },
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    content = actions
                )
            }
        }
    }
}

@Composable
fun AppBar(
    backgroundColor: Color,
    contentColor: Color,
    elevation: Dp,
    contentPadding: PaddingValues,
    shape: Shape,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        shape = shape,
        modifier = modifier,
        shadowElevation = elevation
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .height(AppBarHeight),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

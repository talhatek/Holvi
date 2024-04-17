package com.tek.card.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tek.card.R
import com.tek.database.model.Card
import com.tek.network.model.CardProvider
import com.tek.ui.HolviTheme
import com.tek.ui.holviButtonColors

@Composable
fun CardStack(modifier: Modifier, cards: List<Card>) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp, end = 16.dp, start = 16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        CardStackContent(
            cards = cards
        )
    }
}

@Composable
private fun BoxScope.CardStackContent(cards: List<Card>) {
    val density = LocalDensity.current
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp
    val cardHeightDp = 200.dp

    val selected = remember {
        mutableStateOf(false to -1)
    }
    val cardCount by remember {
        mutableIntStateOf(cards.size)
    }

    val detailVisibilityState by remember {
        derivedStateOf {
            mutableStateOf(selected.value.first)
        }
    }


    val offsetListState = remember(cardCount) {
        mutableStateListOf<Dp>().apply {
            repeat(cardCount) { index ->
                add((48.dp * index).coerceAtMost(screenHeightDp))
            }
        }
    }

    for (i in offsetListState.indices) {
        val offsetYState = animateDpAsState(
            targetValue = offsetListState[i],
            animationSpec = spring(
                visibilityThreshold = Dp.VisibilityThreshold,
                stiffness = StiffnessMediumLow
            ),
            label = "offset y"
        )

        Card(
            shape = RoundedCornerShape(16.0.dp),
            onClick = {
                val newOffsetList = mutableListOf<Dp>()
                if (selected.value.first) {
                    repeat(offsetListState.size) { index ->
                        newOffsetList.add(48.dp.times(index))
                    }
                } else {
                    repeat(offsetListState.size) { index ->
                        if (index <= i) {
                            newOffsetList.add(16.dp.times(index))
                        } else {
                            newOffsetList.add(cardHeightDp + 16.dp.times(index))
                        }
                    }
                }
                offsetListState.clear()
                offsetListState.addAll(newOffsetList)
                selected.value = if (selected.value.first) {
                    false to -1
                } else {
                    true to i
                }
            },
            modifier = Modifier
                .offset(y = offsetYState.value)

        ) {
            CardContent(card = cards[i], i.mod(2) == 0)
        }
    }

    AnimatedVisibility(
        detailVisibilityState.value,
        enter = slideInVertically {
            with(density) { 48.dp.roundToPx() }
        },
        exit = slideOutVertically {
            with(density) { 48.times(2).dp.roundToPx() }
        },
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 48.dp)
    ) {
        Button(modifier = Modifier
            .testTag("detailsButton"),
            colors = holviButtonColors(),
            onClick = {

            }) {
            Text(text = "Details", style = HolviTheme.typography.title)
        }
    }


}

@Composable
fun CardContent(card: Card, isEvenIndex: Boolean) {
    Column(
        modifier = Modifier
            .size(DpSize(400.dp, 200.dp))
            .background(
                if (isEvenIndex) card.cardColor else card.textColor,
                RoundedCornerShape(16.0.dp)
            )
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
            .clipToBounds(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.End)
                .padding(all = 12.dp),
            text = card.company,
            color = if (isEvenIndex) card.textColor else card.cardColor,
            style = HolviTheme.typography.title.copy(letterSpacing = 12.sp)
        )

        Spacer(modifier = Modifier.wrapContentSize())

        Text(
            text = card.number.chunked(4).joinToString(separator = " "),
            color = if (isEvenIndex) card.textColor else card.cardColor,

            style = HolviTheme.typography.card
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.Start)
                ) {
                    Text(
                        text = card.exp,
                        color = if (isEvenIndex) card.textColor else card.cardColor,

                        style = HolviTheme.typography.card
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = card.cvv,
                        color = if (isEvenIndex) card.textColor else card.cardColor,

                        style = HolviTheme.typography.card
                    )

                }
                Text(
                    text = card.holder,
                    color = if (isEvenIndex) card.textColor else card.cardColor,

                    style = HolviTheme.typography.body.copy(letterSpacing = 3.sp)
                )

            }

            Icon(
                modifier = Modifier.size(48.dp),
                painter = painterResource(id = providerResource(CardProvider.valueOf(card.provider))),
                contentDescription = "payment_icon",
                tint = if (isEvenIndex) card.textColor else card.cardColor,
            )
        }
    }
}

fun providerResource(provider: CardProvider): Int {
    return when (provider) {
        CardProvider.MASTER -> R.drawable.master
        else -> R.drawable.visa

    }
}

package com.tek.card.ui

import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tek.card.R
import com.tek.database.model.Card
import com.tek.ui.HolviTheme

@Composable
fun CardStack(modifier: Modifier, cards: List<Card>) {
    Box(
        modifier = modifier
            .padding(top = 16.dp, end = 16.dp, start = 16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        CardStackContent(
            cards = cards
        )
    }
}

@Composable
private fun CardStackContent(cards: List<Card>) {
    val cardCount by remember {
        mutableIntStateOf(cards.size)
    }
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp
    val cardHeightDp = 200.dp
    val offsetListState = remember(cardCount) {
        mutableStateListOf<Dp>().apply {
            repeat(cardCount) { index ->
                add((48.dp * index).coerceAtMost(screenHeightDp))
            }
        }
    }
    val selected = remember {
        mutableStateOf(false to -1)
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

            CardContent(card = cards[i])
        }
    }
}

@Composable
fun CardContent(card: Card) {
    Column(
        modifier = Modifier
            .size(DpSize(400.dp, 200.dp))
            .background(card.color, RoundedCornerShape(16.0.dp))
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
            .clipToBounds(),
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

        Text(
            text = card.number,
            style = HolviTheme.typography.title.copy(letterSpacing = 12.sp)
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
                    Text(text = "07/2027", style = HolviTheme.typography.body)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "033", style = HolviTheme.typography.body)

                }
                Text(
                    text = card.holderName,
                    style = HolviTheme.typography.body.copy(letterSpacing = 3.sp)
                )

            }

            Icon(
                modifier = Modifier.size(48.dp),
                painter = painterResource(id = R.drawable.visa),
                contentDescription = "payment_icon"
            )
        }
    }
}

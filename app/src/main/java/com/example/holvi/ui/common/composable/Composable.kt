package com.example.holvi.ui.common.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.holvi.R


@Composable
fun CircleTextButton(text: String, percentage: Int, onClicked: () -> Unit) {
    BoxWithConstraints() {
        Button(
            onClick = {
                onClicked.invoke()
            },
            modifier = Modifier.size((this.maxWidth / 100) * percentage),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
        ) {
            BoxWithConstraints() {
                Text(
                    text = text.toUpperCase(Locale.current),
                    fontSize = ((this.maxWidth / 100) * percentage).value.sp
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

@Preview(showBackground = true)
@Composable
fun TopAppBarBackWithLogo() {
    CenterTopAppBar(
        title = {
            Text(text = "Holvi", textDecoration = TextDecoration.Underline)
        },
        navigationIcon = {
            IconButton(
                onClick = { /*onBackClicked.invoke()*/ },
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
fun BottomButton(text: String) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.1f), onClick = {}, shape = RectangleShape
    ) {
        Text(text = text)

    }
}

package com.example.holvi.ui.deleteActivity.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.holvi.theme.PoppinsRegular
import com.example.holvi.theme.SecondPrimary


@Composable
fun HolviDropdown(
    data: List<Int>,
    defaultHint: MutableState<String>,
    onItemSelected: (length: Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val tmpData = data.toMutableList()
    var selectedTmpData by remember { defaultHint }
    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.clickable {
        expanded = !expanded
    }) { // Anchor view
        Text(
            text = selectedTmpData, style = TextStyle(
                fontFamily = PoppinsRegular,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
            )
        ) // City name label
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            "Expand collapse",
            tint = Color.White,
            modifier = Modifier
                .size(24.dp)
                .align(alignment = CenterVertically)
        )
        DropdownMenu(
            expanded = expanded,
            modifier = Modifier.background(SecondPrimary),
            onDismissRequest = {
                expanded = false
            }) {
            tmpData.forEach {
                if (selectedTmpData == it.toString())
                    return@forEach
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        selectedTmpData = it.toString()
                        onItemSelected.invoke(it)
                    },
                ) {
                    Text(
                        text = it.toString(),
                        style = TextStyle(
                            fontFamily = PoppinsRegular,
                            fontWeight = FontWeight.Normal,
                            fontSize = 24.sp,
                            color = Color.White
                        ),
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }

            }

        }
    }

}

@ExperimentalComposeUiApi
@Composable
fun HolviChooseSiteDialog(
    siteList: List<String>,
    onItemSelected: (name: String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss.invoke() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(contentAlignment = BottomCenter) {
            LazyColumn(content = {
                items(siteList) { site ->
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onItemSelected.invoke(site) }
                    ) {
                        Text(modifier = Modifier.align(Center), text = site)
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .background(color = Color.White)
                    )
                }
            })
        }

    }
}
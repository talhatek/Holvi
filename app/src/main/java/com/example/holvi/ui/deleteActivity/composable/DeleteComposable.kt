package com.example.holvi.ui.deleteActivity.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.holvi.theme.PoppinsRegular
import com.example.holvi.theme.SecondPrimary


@Composable
fun HolviDropdown(siteList: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    val siteNames = siteList.toMutableList()
    var selectedSiteName by remember { mutableStateOf("Choose a site") }
    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.clickable {
        expanded = !expanded
    }) { // Anchor view
        Text(
            text = selectedSiteName, style = TextStyle(
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
            siteNames.forEach {
                if (selectedSiteName == it)
                    return@forEach
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        selectedSiteName = it
                    },
                ) {
                    Text(
                        text = it,
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
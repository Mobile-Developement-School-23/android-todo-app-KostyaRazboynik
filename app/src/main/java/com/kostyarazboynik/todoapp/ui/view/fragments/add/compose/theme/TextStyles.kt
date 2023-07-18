package com.kostyarazboynik.todoapp.ui.view.fragments.add.compose.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

object TextStyles {
    val largeTitle = TextStyle(
        fontSize = 32.sp,
        lineHeight = 38.sp,
        fontWeight = FontWeight.Medium
    )

    val title = TextStyle(
        fontSize = 20.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.5.sp
    )

    val button = TextStyle(
        fontSize = 14.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center,
        letterSpacing = 0.16.sp
    )

    val body = TextStyle(
        fontSize = 16.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Normal
    )

    val subhead = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Normal
    )
}

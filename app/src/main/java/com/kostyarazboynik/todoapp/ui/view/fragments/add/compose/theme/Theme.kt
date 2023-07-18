package com.kostyarazboynik.todoapp.ui.view.fragments.add.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Stable
class AppColors(
    supportSeparator: Color,
    supportOverlay: Color,
    labelPrimary: Color,
    labelSecondary: Color,
    labelTertiary: Color,
    labelDisable: Color,
    colorRed: Color,
    colorGreen: Color,
    colorBlue: Color,
    colorGray: Color,
    colorGrayLight: Color,
    colorWhite: Color,
    backPrimary: Color,
    backSecondary: Color,
    backElevated: Color,
    taskToolBarColor: Color
){
    var supportSeparator by mutableStateOf(supportSeparator)
        private set
    var supportOverlay by mutableStateOf(supportOverlay)
        private set
    var labelPrimary by mutableStateOf(labelPrimary)
        private set
    var labelSecondary by mutableStateOf(labelSecondary)
        private set
    var labelTertiary by mutableStateOf(labelTertiary)
        private set
    var labelDisable by mutableStateOf(labelDisable)
        private set
    var colorRed by mutableStateOf(colorRed)
        private set
    var colorGreen by mutableStateOf(colorGreen)
        private set
    var colorBlue by mutableStateOf(colorBlue)
        private set
    var colorGray by mutableStateOf(colorGray)
        private set
    var colorGrayLight by mutableStateOf(colorGrayLight)
        private set
    var colorWhite by mutableStateOf(colorWhite)
        private set
    var backPrimary by mutableStateOf(backPrimary)
        private set
    var backSecondary by mutableStateOf(backSecondary)
        private set
    var backElevated by mutableStateOf(backElevated)
        private set
    var taskToolBarColor by mutableStateOf(taskToolBarColor)
        private set

    fun update(other: AppColors){
        supportSeparator = other.supportSeparator
        supportOverlay = other.supportOverlay
        labelPrimary = other.labelPrimary
        labelSecondary = other.labelSecondary
        labelTertiary = other.labelTertiary
        labelDisable = other.labelDisable
        colorRed = other.colorRed
        colorGreen = other.colorGreen
        colorBlue = other.colorBlue
        colorGray = other.colorGray
        colorGrayLight = other.colorGrayLight
        colorWhite = other.colorWhite
        backPrimary = other.backPrimary
        backSecondary = other.backSecondary
        backElevated = other.backElevated
    }

}

private val DarkColorScheme = AppColors(
    supportSeparator = Colors.DarkSupportSeparator,
    supportOverlay = Colors.DarkSupportOverlay,
    labelPrimary = Colors.DarkLabelPrimary,
    labelSecondary = Colors.DarkLabelSecondary,
    labelTertiary = Colors.DarkLabelTertiary,
    labelDisable = Colors.DarkLabelDisable,
    colorRed = Colors.Red,
    colorGreen = Colors.Green,
    colorBlue = Colors.Blue,
    colorGray = Colors.Gray,
    colorGrayLight = Colors.GrayLight,
    colorWhite = Colors.White,
    backPrimary = Colors.DarkBackPrimary,
    backSecondary = Colors.DarkBackSecondary,
    backElevated = Colors.DarkBackElevated,
    taskToolBarColor = Colors.DarkTaskToolbarColor
)


private val LightColorScheme = AppColors(
    supportSeparator = Colors.LightSupportSeparator,
    supportOverlay = Colors.LightSupportOverlay,
    labelPrimary = Colors.LightLabelPrimary,
    labelSecondary = Colors.LightLabelSecondary,
    labelTertiary = Colors.LightLabelTertiary,
    labelDisable = Colors.LightLabelDisable,
    colorRed = Colors.Red,
    colorGreen = Colors.Green,
    colorBlue = Colors.Blue,
    colorGray = Colors.Gray,
    colorGrayLight = Colors.GrayLight,
    colorWhite = Colors.White,
    backPrimary = Colors.LightBackPrimary,
    backSecondary = Colors.LightBackSecondary,
    backElevated = Colors.LightBackElevated,
    taskToolBarColor = Colors.LightTaskToolbarColor
)

internal val LocalCustomColors = staticCompositionLocalOf<AppColors> { error("No colors provided") }

@Composable
fun ProvideAppTheme(
    colors: AppColors,
    content: @Composable () -> Unit
) {
    val colorPalette = remember { colors }
    colorPalette.update(colors)
    CompositionLocalProvider(LocalCustomColors provides colorPalette, content = content)
}

object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalCustomColors.current
    val typography: TextStyles
        get() = TextStyles

}

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    ProvideAppTheme(colors = colorScheme) {
        MaterialTheme(
            content = content
        )
    }
}

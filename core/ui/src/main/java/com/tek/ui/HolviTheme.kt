package com.tek.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class HolviColors(
    appBackground: Color,
    appForeground: Color,
    primaryBackground: Color,
    primaryForeground: Color,
    primaryDarkBackground: Color,
    error: Color
) {
    var appBackground by mutableStateOf(appBackground)
        private set

    var appForeground by mutableStateOf(appForeground)
        private set
    var primaryBackground by mutableStateOf(primaryBackground)
        private set
    var primaryForeground by mutableStateOf(primaryForeground)
        private set
    var primaryDarkBackground by mutableStateOf(primaryDarkBackground)
        private set
    var error by mutableStateOf(error)
        private set

    fun copy(
        appBackground: Color = this.appBackground,
        appForeground: Color = this.appForeground,
        primaryBackground: Color = this.primaryBackground,
        primaryForeground: Color = this.primaryDarkBackground,
        primaryDarkBackground: Color = this.primaryDarkBackground,
        error: Color = this.error,
    ): HolviColors = HolviColors(
        appBackground,
        appForeground,
        primaryBackground,
        primaryForeground,
        primaryDarkBackground,
        error,
    )

    fun updateColorsFrom(other: HolviColors) {
        primaryBackground = other.primaryBackground
        primaryForeground = other.primaryForeground
        primaryDarkBackground = other.primaryDarkBackground
        appBackground = other.appBackground
        appForeground = other.appForeground
        error = other.error
    }
}

private val colorAppBackground = Color(0xff4A5759)
private val colorAppForeground = Color(0xffffffff)
private val colorPrimaryBackground = Color(0xffDCDCDD)
private val colorPrimaryForeground = Color(0xff495057)
private val colorPrimaryDarkBackground = Color(0xFF2F2A34)
private val colorError = Color(0xFFD62222)

fun lightColors(
    appBackground: Color = colorAppBackground,
    appForeground: Color = colorAppForeground,
    primaryBackground: Color = colorPrimaryBackground,
    primaryForeground: Color = colorPrimaryForeground,
    primaryDarkBackground: Color = colorPrimaryDarkBackground,
    error: Color = colorError
): HolviColors = HolviColors(
    appBackground = appBackground,
    appForeground = appForeground,
    primaryBackground = primaryBackground,
    primaryForeground = primaryForeground,
    primaryDarkBackground = primaryDarkBackground,
    error = error
)


val LocalColors = staticCompositionLocalOf { lightColors() }

data class AppDimensions(
    val paddingSmall: Dp = 4.dp,
    val paddingMedium: Dp = 8.dp,
    val paddingLarge: Dp = 24.dp
)

internal val LocalDimensions = staticCompositionLocalOf { AppDimensions() }

private val poppinsMedium = FontFamily(
    Font(R.font.poppins_medium, FontWeight.Medium)
)
private val poppinsSemiBold = FontFamily(
    Font(R.font.poppins_semi_bold, FontWeight.SemiBold)
)
private val poppinsBold = FontFamily(
    Font(R.font.poppins_bold, FontWeight.Bold)
)
private val poppinsRegular = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal)
)

data class AppTypography(
    val largestHeader: TextStyle = TextStyle(
        fontFamily = poppinsBold,
        fontSize = 34.sp,
        letterSpacing = 0.25.sp
    ),
    val title: TextStyle = TextStyle(
        fontFamily = poppinsSemiBold,
        fontSize = 16.sp
    ),
    val body: TextStyle = TextStyle(
        fontFamily = poppinsRegular,
        fontSize = 16.sp
    ),
    val button: TextStyle = TextStyle(
        fontFamily = poppinsRegular,
        fontSize = 16.sp
    ),
    val caption: TextStyle = TextStyle(
        fontFamily = poppinsRegular,
        fontSize = 12.sp
    )
)

internal val LocalTypography = staticCompositionLocalOf { AppTypography() }


object HolviTheme {

    val colors: HolviColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    val dimensions: AppDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalDimensions.current
}

@Composable
fun HolviTheme(
    colors: HolviColors = HolviTheme.colors,
    typography: AppTypography = HolviTheme.typography,
    dimensions: AppDimensions = HolviTheme.dimensions,
    content: @Composable () -> Unit
) {
    val rememberedColors = remember { colors.copy() }.apply { updateColorsFrom(colors) }
    CompositionLocalProvider(
        LocalColors provides rememberedColors,
        LocalDimensions provides dimensions,
        LocalTypography provides typography
    ) {
        content()
    }
}
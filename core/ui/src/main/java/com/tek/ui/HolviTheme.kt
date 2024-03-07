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
    mainBackground: Color,
    primaryBackgroundColor: Color,
    primaryDarkBackgroundColor: Color,
    primaryTextColor: Color,
    bottomBackground: Color,
    error: Color,
    isLight: Boolean
) {
    var mainBackground by mutableStateOf(mainBackground)
        private set
    var primaryBackground by mutableStateOf(primaryBackgroundColor)
        private set
    var primaryDarkBackground by mutableStateOf(primaryDarkBackgroundColor)
        private set
    var primaryTextColor by mutableStateOf(primaryTextColor)
        private set
    var bottomBackground by mutableStateOf(bottomBackground)
        private set
    var error by mutableStateOf(error)
        private set
    var isLight by mutableStateOf(isLight)
        internal set

    fun copy(
        mainBackground: Color = this.mainBackground,
        primaryBackgroundColor: Color = this.primaryBackground,
        primaryDarkBackgroundColor: Color = this.primaryDarkBackground,
        primaryTextColor: Color = this.primaryTextColor,
        bottomBackground: Color = this.bottomBackground,
        error: Color = this.error,
        isLight: Boolean = this.isLight
    ): HolviColors = HolviColors(
        mainBackground,
        primaryBackgroundColor,
        primaryDarkBackgroundColor,
        primaryTextColor,
        bottomBackground,
        error,
        isLight
    )

    fun updateColorsFrom(other: HolviColors) {
        primaryBackground = other.primaryBackground
        primaryDarkBackground = other.primaryDarkBackground
        primaryTextColor = other.primaryTextColor
        bottomBackground = other.bottomBackground
        error = other.error
    }
}

private val colorBackground = Color(red = 28, green = 27, blue = 31)
private val colorPrimaryBackgroundColor = Color(0xFF084227)
private val colorPrimaryDarkBackgroundColor = Color(0xFF2F2A34)
private val colorPrimaryTextColor = Color(0xFFced9bf)
private val colorBottomBackground = Color(0xFF25202B)
private val colorError = Color(0xFFD62222)


fun lightColors(
    mainBackground: Color = colorBackground,
    primaryBackgroundColor: Color = colorPrimaryBackgroundColor,
    primaryDarkBackgroundColor: Color = colorPrimaryDarkBackgroundColor,
    primaryTextColor: Color = colorPrimaryTextColor,
    bottomBackground: Color = colorBottomBackground,
    error: Color = colorError
): HolviColors = HolviColors(
    mainBackground = mainBackground,
    primaryBackgroundColor = primaryBackgroundColor,
    primaryDarkBackgroundColor = primaryDarkBackgroundColor,
    primaryTextColor = primaryTextColor,
    bottomBackground = bottomBackground,
    error = error,
    isLight = true
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
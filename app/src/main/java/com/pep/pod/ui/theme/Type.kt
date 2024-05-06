package com.pep.pod.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.sp
import com.pep.pod.R

private val IranSans = FontFamily(
    Font(R.font.iran_sans),
    Font(R.font.iran_sans_bold),
    Font(R.font.iran_sans_light),
    Font(R.font.iran_sans_medium),
    Font(R.font.iran_sans_ultralight),
)

@Suppress("DEPRECATION")
val defaultTextStyle = TextStyle(
    fontFamily = IranSans,
    platformStyle = PlatformTextStyle(includeFontPadding = false),
    lineHeightStyle = LineHeightStyle(alignment = LineHeightStyle.Alignment.Center, trim = LineHeightStyle.Trim.None)
)

val PODTypography = Typography(
    displayLarge = defaultTextStyle.copy(fontSize = 57.sp, lineHeight = 64.sp, letterSpacing = (-0.25).sp, textDirection = TextDirection.Content),
    displayMedium = defaultTextStyle.copy(fontSize = 45.sp, lineHeight = 52.sp, letterSpacing = 0.sp, textDirection = TextDirection.Content),
    displaySmall = defaultTextStyle.copy(fontSize = 36.sp, lineHeight = 44.sp, letterSpacing = 0.sp, textDirection = TextDirection.Content),
    headlineLarge = defaultTextStyle.copy(fontSize = 32.sp, lineHeight = 40.sp, letterSpacing = 0.sp, textDirection = TextDirection.Content),
    headlineMedium = defaultTextStyle.copy(fontSize = 28.sp, lineHeight = 36.sp, letterSpacing = 0.sp, textDirection = TextDirection.Content,),
    headlineSmall = defaultTextStyle.copy(fontSize = 24.sp, lineHeight = 32.sp, letterSpacing = 0.sp, textDirection = TextDirection.Content),
    titleLarge = defaultTextStyle.copy(fontSize = 20.sp, lineHeight = 28.sp, letterSpacing = 0.sp, textDirection = TextDirection.Content),
    titleMedium = defaultTextStyle.copy(fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.15.sp, fontWeight = FontWeight.Medium, textDirection = TextDirection.Content),
    titleSmall = defaultTextStyle.copy(fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp, fontWeight = FontWeight.Medium, textDirection = TextDirection.Content),
    labelLarge = defaultTextStyle.copy(fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp, fontWeight = FontWeight.Medium, textDirection = TextDirection.Content),
    labelMedium = defaultTextStyle.copy(fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp, fontWeight = FontWeight.Medium, textDirection = TextDirection.Content),
    labelSmall = defaultTextStyle.copy(fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp, fontWeight = FontWeight.ExtraLight, textDirection = TextDirection.Content),
    bodyLarge = defaultTextStyle.copy(fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.5.sp, textDirection = TextDirection.Content),
    bodyMedium = defaultTextStyle.copy(fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.25.sp, textDirection = TextDirection.Content),
    bodySmall = defaultTextStyle.copy(fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.4.sp, textDirection = TextDirection.Content),
)
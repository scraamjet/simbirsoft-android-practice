package com.example.core.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.example.core.R

val Roboto = FontFamily(Font(R.font.roboto_regular))
val OfficinaSansExtraBold = FontFamily(Font(R.font.officina_sans_extra_bold))

val BodyTextRegularBlackDeep = TextStyle(
    fontFamily = Roboto,
    fontSize = 16.sp,
    color = BlackSolid,
)

val BodyTextInputGrayFullWidth = TextStyle(
    fontFamily = Roboto,
    fontSize = 18.sp,
    color = BlackTranslucent
)

val CaptionSmallBlackSoft = TextStyle(
    fontFamily = Roboto,
    fontSize = 14.sp,
    color = BlackTranslucent,
    lineHeight = 16.sp
)

val BodyTextLinkGreen = TextStyle(
    fontFamily = Roboto,
    fontSize = 14.sp,
    color = Green,
    textDecoration = TextDecoration.Underline
)

val BodyTextMediumWhiteCenter = TextStyle(
    fontFamily = Roboto,
    fontSize = 16.sp,
    color = Color.White,
    textAlign = TextAlign.Center
)

val HeadingMediumWhite = TextStyle(
    fontFamily = OfficinaSansExtraBold,
    fontSize = 21.sp,
    color = White
)
val HeadingMediumBlueGreyCenter = TextStyle(
    fontFamily = OfficinaSansExtraBold,
    fontSize = 21.sp,
    color = Color(0xFF607D8B), // blue_grey
    textAlign = TextAlign.Center,
    lineHeight = 32.sp
)

val BodyTextMediumBlackDeep = TextStyle(
    fontFamily = Roboto,
    fontSize = 16.sp,
    color = BlackSolid,
    textAlign = TextAlign.Center
)

val CaptionSmallWhite = TextStyle(
    fontFamily = Roboto,
    fontSize = 12.sp,
    color = White,
    textAlign = TextAlign.Center
)

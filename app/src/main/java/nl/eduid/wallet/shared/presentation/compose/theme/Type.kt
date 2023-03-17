package nl.eduid.wallet.shared.presentation.compose.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import nl.eduid.wallet.R

val nunitoFamily = FontFamily(
    Font(R.font.nunito_regular),
    Font(R.font.nunito_semibold, weight = FontWeight.SemiBold),
    Font(R.font.nunito_light, weight = FontWeight.Light),
)

val sourceSansProFamily = FontFamily(
    Font(R.font.sourcesanspro_regular),
    Font(R.font.sourcesanspro_semibold, weight = FontWeight.Medium),
)

val h1 = TextStyle(
    fontFamily = nunitoFamily,
    fontWeight = FontWeight.Light,
    fontSize = 96.sp,
    letterSpacing = (-1.5).sp
)

val h2 = TextStyle(
    fontFamily = nunitoFamily,
    fontWeight = FontWeight.Light,
    fontSize = 60.sp,
    letterSpacing = (-0.5).sp
)

val h3 = TextStyle(
    fontFamily = nunitoFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 48.sp,
    letterSpacing = 0.sp
)

val h4 = TextStyle(
    fontFamily = nunitoFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 28.sp,
    lineHeight = 34.sp
)

val h5 = TextStyle(
    fontFamily = nunitoFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 24.sp,
    letterSpacing = 0.sp,
    lineHeight = 34.sp
)

val h6 = TextStyle(
    fontFamily = nunitoFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 20.sp,
    letterSpacing = 0.15.sp
)

val subtitle1 = TextStyle(
    fontFamily = sourceSansProFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
    lineHeight = 34.sp
)

val subtitle2 = TextStyle(
    fontFamily = sourceSansProFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    letterSpacing = 0.1.sp
)

val body1 = TextStyle(
    fontFamily = sourceSansProFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    letterSpacing = 0.19.sp,
    lineHeight = 24.sp
)

val body2 = TextStyle(
    fontFamily = sourceSansProFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    letterSpacing = 0.25.sp
)

val button = TextStyle(
    fontFamily = sourceSansProFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
)

val caption = TextStyle(
    fontFamily = sourceSansProFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
)

val overline = TextStyle(
    fontFamily = sourceSansProFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 10.sp,
    letterSpacing = 1.5.sp
)

val AppTypography = Typography(
    h1 = h1,
    h2 = h2,
    h3 = h3,
    h4 = h4,
    h5 = h5,
    h6 = h6,
    subtitle1 = subtitle1,
    subtitle2 = subtitle2,
    body1 = body1,
    body2 = body2,
    button = button,
    caption = caption,
    overline = overline
)

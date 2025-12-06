package bd.srabbijan.lenden.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp

private val lightColorScheme = AppColorScheme(
    primary = PrimaryColor,
    cardBgColor = CardBgColor,
    background = BackgroundColor,
    border = BorderColor,
    text = TextColor,
    textBox = TextBoxColor,
    textBoxHint = TextBoxHintColor,
    labelColor = LabelTextColor,
    icon = IconColor,
    error = ErrorColor,
    success = SuccessColor,
    shimmer = LightShimmerColors
)

private val darkColorScheme = AppColorScheme(
    primary = DarkPrimaryColor,
    cardBgColor = DarkCardBgColor,
    background = DarkBackgroundColor,
    border = DarkBorderColor,
    labelColor = DarkLabelTextColor,
    text = DarkTextColor,
    textBox = DarkTextBoxColor,
    textBoxHint = DarkTextBoxHintColor,
    icon = DarkIconColor,
    error = DarkErrorColor,
    success = DarkSuccessColor,
    shimmer = DarkShimmerColors
)


@Composable
fun AppTheme(
    isDarkTheme: Boolean = false,
    content: @Composable () -> Unit
) {

    val colorScheme = if (isDarkTheme) darkColorScheme else lightColorScheme

    val typography = AppTypography(
        titleLarge = TextStyle(
            fontFamily = appFont(),
            fontWeight = FontWeight.Bold,
            fontSize = 20.ssp
        ),
        titleNormal = TextStyle(
            fontFamily = appFont(),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.ssp
        ),
        paragraph = TextStyle(
            fontFamily = appFont(),
            fontWeight = FontWeight.Normal,
            fontSize = 16.ssp
        ),
        labelLarge = TextStyle(
            fontFamily = appFont(),
            fontWeight = FontWeight.Normal,
            fontSize = 14.ssp
        ),
        labelNormal = TextStyle(
            fontFamily = appFont(),
            fontWeight = FontWeight.Normal,
            fontSize = 12.ssp
        ),
        labelSmall = TextStyle(
            fontFamily = appFont(),
            fontWeight = FontWeight.Light,
            fontSize = 10.ssp
        )
    )

    val shape = AppShape(
        container = RoundedCornerShape(12.sdp),
        button = RoundedCornerShape(20),
        circular = RoundedCornerShape(50),
    )

    val size = AppSize(
        large = 16.sdp,
        medium = 12.sdp,
        normal = 8.sdp,
        small = 4.sdp
    )

    CompositionLocalProvider(
        LocalAppColorScheme provides colorScheme,
        LocalAppTypography provides typography,
        LocalAppShape provides shape,
        LocalAppSize provides size,
        LocalIndication provides ripple(),
        content = content
    )
}

object AppTheme {

    val colorScheme: AppColorScheme
        @Composable get() = LocalAppColorScheme.current

    val typography: AppTypography
        @Composable get() = LocalAppTypography.current

    val shape: AppShape
        @Composable get() = LocalAppShape.current

    val size: AppSize
        @Composable get() = LocalAppSize.current
}
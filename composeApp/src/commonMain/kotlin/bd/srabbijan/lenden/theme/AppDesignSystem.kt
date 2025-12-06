package bd.srabbijan.lenden.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

@Stable
data class AppColorScheme(
    val primary: Color,
    val cardBgColor: Color,
    val background: Color,
    val border: Color,
    val text: Color,
    val textBox : Color,
    val textBoxHint: Color,
    val labelColor: Color,
    val icon: Color,
    val error: Color,
    val success: Color,
    val shimmer: List<Color>
)

@Stable
data class AppTypography(
    val titleLarge: TextStyle,
    val titleNormal: TextStyle,
    val paragraph: TextStyle,
    val labelLarge: TextStyle,
    val labelNormal: TextStyle,
    val labelSmall: TextStyle
)

@Stable
data class AppShape(
    val container: Shape,
    val button: Shape,
    val circular: Shape
)

@Stable
data class AppSize(
    val large: Dp,
    val medium: Dp,
    val normal: Dp,
    val small: Dp
)

val LocalAppColorScheme = staticCompositionLocalOf {
    AppColorScheme(
        primary = Color.Unspecified,
        cardBgColor = Color.Unspecified,
        background = Color.Unspecified,
        border = Color.Unspecified,
        labelColor = Color.Unspecified,
        text = Color.Unspecified,
        textBox = Color.Unspecified,
        textBoxHint = Color.Unspecified,
        icon = Color.Unspecified,
        error = Color.Unspecified,
        success = Color.Unspecified,
        shimmer = emptyList()
    )
}

val LocalAppTypography = staticCompositionLocalOf {
    AppTypography(
        titleLarge = TextStyle.Default,
        titleNormal = TextStyle.Default,
        paragraph = TextStyle.Default,
        labelLarge = TextStyle.Default,
        labelNormal = TextStyle.Default,
        labelSmall = TextStyle.Default
    )
}

val LocalAppShape = staticCompositionLocalOf {
    AppShape(
        container = RectangleShape,
        button = RectangleShape,
        circular = CircleShape
    )
}

val LocalAppSize = staticCompositionLocalOf {
    AppSize(
        large = Dp.Unspecified,
        medium = Dp.Unspecified,
        normal = Dp.Unspecified,
        small = Dp.Unspecified
    )
}
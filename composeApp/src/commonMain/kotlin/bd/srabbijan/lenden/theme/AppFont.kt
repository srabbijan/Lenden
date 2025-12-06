package bd.srabbijan.lenden.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import lenden.composeapp.generated.resources.lexenddeca_regular
import lenden.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
fun appFont() = Font(Res.font.lexenddeca_regular, FontWeight.Normal, FontStyle.Normal).toFontFamily()

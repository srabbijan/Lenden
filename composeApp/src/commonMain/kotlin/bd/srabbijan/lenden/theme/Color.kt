package bd.srabbijan.lenden.theme

import androidx.compose.material3.RadioButtonColors
import androidx.compose.ui.graphics.Color

//val PrimaryColor = Color(0xFF3182ce)  //  (App Bar, Primary Buttons)
val CardBgColor = Color(0xFFf7fafc)
//val BackgroundColor = Color(0xFFF8F9FA) // (Page Background)
//val BorderColor = Color(0xFFe2e8f0) // (Borders, Dividers)
//val TextColor = Color(0xFF2d3748)
val TextBoxColor = Color(0xFFE5E7EB)
val TextBoxHintColor = Color(0xFFCBD5E1)
val LabelTextColor = Color(0xFF4a5568)
//val IconColor = Color(0xFF2d3748) // White (Icons, Text on Dark Buttons)
//val ErrorColor = Color(0xFFFF0000) // Red (Error Messages, Alerts)
//val SuccessColor = Color(0xFF10B981) // Green (Success Messages)

val PrimaryColor = Color(0xFFD70F64)  // (App Bar, Primary Buttons)
val SecondaryColor = Color(0xFFFFECF0) // (Accent, Secondary Buttons)
val BackgroundColor = Color(0xFFF8F8F8) // (Page Background)
val BorderColor = Color(0xFF9CA3AF) // (Borders, Dividers)
val TextColor = Color(0xFF212121) // (Text)
val IconColor = Color(0xFFFFFFFF) // White (Icons, Text on Dark Buttons)
val ErrorColor = Color(0xFFEF4444) // Red (Error Messages, Alerts)
val SuccessColor = Color(0xFF10B981) // Green (Success Messages)
val ButtonColor = PrimaryColor

val AppRadioButtonColor = RadioButtonColors(
    selectedColor = PrimaryColor,
    unselectedColor = BorderColor,
    disabledSelectedColor = BorderColor,
    disabledUnselectedColor = BorderColor,
)
val LightShimmerColors = listOf (
    Color(0xFFE8E8E8),
    Color(0xFFF5F5F5),
    Color(0xFFFFFFFF),
    Color(0xFFF5F5F5),
    Color(0xFFE8E8E8),
)

// Dark Theme Colors
val DarkPrimaryColor = Color(0xFF3182ce)  // Slightly lighter blue for better visibility
val DarkCardBgColor = Color(0xFF28282B)   // Dark gray for cards28282B
val DarkBackgroundColor = Color(0xFF1a202c) // Very dark background
val DarkBorderColor = Color(0xFF4a5568)   // Medium gray for borders
val DarkTextColor = Color(0xFFf7fafc)     // Light text (almost white)
val DarkTextBoxColor = Color(0xFF3A3B3C)
val DarkTextBoxHintColor = Color(0xFFA0AEC0)
val DarkLabelTextColor = Color(0xFFa0aec0) // Muted light gray for labels
val DarkIconColor = Color(0xFFf7fafc)     // Light color for icons
val DarkErrorColor = Color(0xFFfc8181)    // Softer red for dark theme
val DarkSuccessColor = Color(0xFF68d391)  // Softer green for dark theme

val DarkShimmerColors = listOf (
    Color(0xFF2d3748),
    Color(0xFF3A3A3A),
    Color(0xFF4A4A4A),
    Color(0xFF3A3A3A),
    Color(0xFF2d3748)
)
package pkg.maid_to_order.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    secondary = DarkSecondary,
    tertiary = DarkSecondary,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = DarkBackground,
    onSecondary = DarkBackground,
    onTertiary = DarkBackground,
    onBackground = DarkOnBackground,
    onSurface = DarkOnSurface
)

private val LightColorScheme = lightColorScheme(
    primary = CelestePrimary,
    secondary = CelesteSecondary,
    tertiary = CelesteLight,
    background = WhiteBackground,
    surface = WhiteSurface,
    surfaceVariant = CelesteLight.copy(alpha = 0.3f),
    onPrimary = WhiteBackground,
    onSecondary = WhiteBackground,
    onTertiary = BlackPrimary,
    onBackground = BlackPrimary,
    onSurface = BlackPrimary,
    primaryContainer = CelesteLight.copy(alpha = 0.5f),
    onPrimaryContainer = CelesteAccent
)

@Composable
fun MaidtoOrderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
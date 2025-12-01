package pkg.maid_to_order.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp

// =========================================================
//               PALETA DE COLORES MAID TO ORDER
// =========================================================

// MODO CLARO
private val LightColors = lightColorScheme(
    primary = CelestePrimary,
    onPrimary = WhiteBackground,

    primaryContainer = CelesteLight,
    onPrimaryContainer = BlackPrimary,

    secondary = CelesteSecondary,
    onSecondary = BlackPrimary,

    tertiary = CelesteAccent,
    onTertiary = WhiteBackground,

    background = WhiteBackground,
    onBackground = BlackPrimary,

    surface = WhiteSurface,
    onSurface = BlackSecondary,

    surfaceVariant = LightGreyBackground,
    onSurfaceVariant = BlackSecondary,

    error = Color(0xFFB00020),
    onError = Color.White
)

// MODO OSCURO
private val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnSurface,

    primaryContainer = DarkSurface,
    onPrimaryContainer = DarkOnBackground,

    secondary = DarkSecondary,
    onSecondary = DarkOnSurface,

    tertiary = CelesteAccent,
    onTertiary = DarkOnSurface,

    background = DarkBackground,
    onBackground = DarkOnBackground,

    surface = DarkSurface,
    onSurface = DarkOnSurface,

    surfaceVariant = DarkSurface,
    onSurfaceVariant = DarkOnBackground,

    error = Color(0xFFCF6679),
    onError = Color.Black
)

// =========================================================
//                    SHAPES PERSONALIZADAS
// =========================================================

private val AppShapes = Shapes(
    small = androidx.compose.foundation.shape.RoundedCornerShape(8),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(12),
    large = androidx.compose.foundation.shape.RoundedCornerShape(20)
)

// =========================================================
//                     TIPOLOGRAFÍA M3
// =========================================================

private val AppTypography = Typography(
    titleLarge = Typography().titleLarge.copy(
        fontSize = 24.sp,
        lineHeight = 30.sp
    ),
    titleMedium = Typography().titleMedium.copy(
        fontSize = 20.sp,
        lineHeight = 26.sp
    ),
    bodyLarge = Typography().bodyLarge.copy(
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    bodyMedium = Typography().bodyMedium.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
)

// =========================================================
//                    TEMA GLOBAL M3
// =========================================================

@Composable
fun MaidToOrderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val colors = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}

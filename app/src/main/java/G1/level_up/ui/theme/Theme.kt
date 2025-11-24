package G1.level_up.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * `DarkColorScheme` define la paleta de colores para el tema oscuro.
 * Estos son los colores por defecto generados por Android Studio.
 */
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

/**
 * `LightColorScheme` define la paleta de colores para el tema claro.
 * Estos son los colores por defecto generados por Android Studio.
 */
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* 
    Aquí se podrían sobrescribir otros colores por defecto para el tema claro.
    Por ejemplo:
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

/**
 * `LevelUpTheme` es el Composable principal que aplica el tema visual a toda la aplicación.
 * Envuelve el contenido de la app y le proporciona los colores, la tipografía y las formas definidas
 * en el sistema de Material Design.
 *
 * @param darkTheme Booleano que indica si se debe usar el tema oscuro. Por defecto, sigue la configuración del sistema.
 * @param dynamicColor Booleano que indica si se debe usar el color dinámico (Material You) en Android 12+. Es opcional.
 * @param content El contenido Composable al que se le aplicará el tema.
 */
@Composable
fun LevelUpTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // El color dinámico está disponible a partir de Android 12 (API 31).
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // Se selecciona la paleta de colores correcta según las condiciones.
    val colorScheme = when {
        // Si el color dinámico está habilitado y el dispositivo es Android 12+.
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            // Se usa el esquema de color dinámico claro u oscuro del sistema.
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // Si no, se usa el esquema de color personalizado oscuro.
        darkTheme -> DarkColorScheme
        // O el esquema de color personalizado claro.
        else -> LightColorScheme
    }

    // `MaterialTheme` es el Composable que aplica el tema a sus descendientes.
    MaterialTheme(
        colorScheme = colorScheme, // Aplica la paleta de colores seleccionada.
        typography = Typography,   // Aplica los estilos de tipografía definidos en Type.kt.
        content = content          // Renderiza el contenido de la aplicación dentro de este tema.
    )
}

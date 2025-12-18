package G1.level_up.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * `Typography` define el conjunto de estilos de texto para la aplicación, siguiendo las guías de Material Design.
 * Aquí se pueden personalizar los estilos de fuente para títulos, cuerpos de texto, etiquetas, etc.
 *
 * Aunque muchos textos en la aplicación definen su estilo de forma localizada (en cada Composable),
 * este objeto `Typography` se pasa al `MaterialTheme` en `Theme.kt` y sirve como el estilo base
 * para los componentes de Material que no tienen un estilo explícitamente definido.
 */
val Typography = Typography(
    // `bodyLarge` es el estilo por defecto para el texto principal del cuerpo.
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default, // Fuente por defecto del sistema.
        fontWeight = FontWeight.Normal, // Peso de la fuente normal.
        fontSize = 16.sp, // Tamaño de la fuente.
        lineHeight = 24.sp, // Altura de la línea.
        letterSpacing = 0.5.sp // Espaciado entre caracteres.
    )
    
    /* 
    Aquí se podrían sobrescribir otros estilos de texto por defecto si fuera necesario.
    Por ejemplo:
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

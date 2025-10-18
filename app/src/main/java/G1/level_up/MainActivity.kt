package G1.level_up

import G1.level_up.Navigation.AppNavigation
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

/**
 * `MainActivity` es la única actividad en la aplicación y sirve como el punto de entrada principal.
 * Sigue el enfoque de "Actividad Única" recomendado para aplicaciones de Jetpack Compose.
 *
 * Su principal responsabilidad es configurar el entorno de Compose y cargar el componente de navegación principal.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bloquea la orientación de la pantalla en modo vertical.
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // `enableEdgeToEdge` permite que la aplicación se dibuje a pantalla completa, es decir,
        // detrás de las barras de estado y navegación del sistema. Esto es clave para diseños modernos
        // e inmersivos. Los `insets` (espacios de las barras) se manejan luego en los Composables
        // individuales usando `Scaffold` o modificadores de padding.
        enableEdgeToEdge()

        // `setContent` es donde se define la interfaz de usuario de la actividad utilizando Jetpack Compose.
        // Todo el contenido de la UI de la app se renderiza dentro de este bloque.
        setContent {
            // `AppNavigation` es el Composable que contiene el `NavHost` y define todas las rutas
            // y pantallas de la aplicación. Es el componente raíz de la UI.
            AppNavigation()
        }
    }
}

package G1.level_up.ui

import G1.level_up.repository.UserRepository
import G1.level_up.ui.theme.PrimaryAccent
import G1.level_up.ui.theme.PrimaryDark
import G1.level_up.ui.theme.TextColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * `AccountScreen` muestra los detalles de la cuenta del usuario que ha iniciado sesión.
 * Presenta la información en un diseño limpio y profesional utilizando Material Design 3.
 *
 * @param username El nombre de usuario del usuario actual, usado para obtener sus datos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(username: String) {
    // Se obtiene el contexto actual para poder instanciar el repositorio.
    val context = LocalContext.current
    // Se crea una instancia de UserRepository para acceder a los datos del usuario.
    val userRepository = UserRepository(context)
    // Se busca al usuario en la base de datos por su nombre de usuario.
    val user = userRepository.getUserByUsername(username)
    // Estado para controlar si la contraseña es visible o no.
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = PrimaryDark, // Color de fondo de la pantalla.
        topBar = {
            // Barra superior que muestra el título de la pantalla.
            TopAppBar(
                title = { Text("Mi Cuenta") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryDark,
                    titleContentColor = TextColor
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Aplica el padding para no solaparse con la TopAppBar.
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Centra el contenido horizontalmente.
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Ícono grande que representa al usuario.
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "User Avatar",
                tint = TextColor,
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Si el usuario se encontró, se muestran sus datos.
            user?.let {
                // Campo de texto delineado para mostrar el nombre de usuario (no editable).
                OutlinedTextField(
                    value = it.username,
                    onValueChange = {},
                    label = { Text("Usuario") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = outlinedTextFieldColors()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de texto para la contraseña con un ícono para alternar la visibilidad.
                OutlinedTextField(
                    value = it.pass,
                    onValueChange = {},
                    label = { Text("Contraseña") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    // Cambia la transformación visual entre texto normal y contraseña.
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = { // Ícono al final del campo de texto.
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, description, tint = TextColor)
                        }
                    },
                    colors = outlinedTextFieldColors()
                )
            }
        }
    }
}

/**
 * `outlinedTextFieldColors` es una función Composable privada que centraliza la configuración
 * de colores para los `OutlinedTextField` de esta pantalla, promoviendo la consistencia.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun outlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = PrimaryAccent,
    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
    cursorColor = PrimaryAccent,
    focusedTextColor = TextColor,
    unfocusedTextColor = TextColor,
    disabledTextColor = TextColor.copy(alpha = 0.7f),
    focusedLabelColor = TextColor,
    unfocusedLabelColor = TextColor.copy(alpha = 0.7f)
)

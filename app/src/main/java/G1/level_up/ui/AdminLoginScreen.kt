package G1.level_up.ui

import G1.level_up.Navigation.Screen
import G1.level_up.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

/**
 * Pantalla de inicio de sesión para administradores.
 *
 * @param navController El controlador de navegación para manejar las transiciones entre pantallas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminLoginScreen(navController: NavController) {
    // Estados para almacenar el correo electrónico, la contraseña y los mensajes de error.
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Diseño de la pantalla en una columna que ocupa todo el espacio disponible.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryDark) // Color de fondo oscuro.
            .padding(32.dp), // Espaciado interno.
        horizontalAlignment = Alignment.CenterHorizontally, // Alineación horizontal al centro.
        verticalArrangement = Arrangement.Center // Alineación vertical al centro.
    ) {
        // Muestra el logo de la aplicación.
        Image(
            painter = painterResource(id = R.drawable.logolevelup),
            contentDescription = "Logo de Level-Up Gamer",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp)) // Espacio vertical.

        // Título de la pantalla.
        Text(
            text = "Inicio de Sesión de Administrador",
            color = TextColor, // Color del texto.
            fontSize = 32.sp, // Tamaño de la fuente.
            fontWeight = FontWeight.Bold, // Fuente en negrita.
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campo de texto para el correo electrónico.
        LevelUpTextField(
            value = email,
            onValueChange = { email = it },
            label = "Correo Electrónico",
            keyboardType = KeyboardType.Email // Tipo de teclado para correo electrónico.
        )

        Spacer(modifier = Modifier.height(16.dp)) // Espacio vertical.

        // Campo de texto para la contraseña.
        LevelUpTextField(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            keyboardType = KeyboardType.Password // Tipo de teclado para contraseña.
        )

        Spacer(modifier = Modifier.height(16.dp)) // Espacio vertical.

        // Muestra un mensaje de error si existe.
        errorMessage?.let {
            Text(text = it, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Botón para iniciar sesión.
        Button(
            onClick = {
                // Comprueba si el correo electrónico termina en "@level.com".
                if (email.endsWith("@level.com", ignoreCase = true)) {
                    // Navega a la pantalla de productos de administrador si es válido.
                    navController.navigate(Screen.AdminProducts.route)
                } else {
                    // Muestra un mensaje de error si no es válido.
                    errorMessage = "Correo electrónico inválido. Debe ser un correo @level.com"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp), // Bordes redondeados.
            colors = ButtonDefaults.buttonColors(containerColor = ButtonAccent) // Color del botón.
        ) {
            Text(
                text = "INICIAR SESIÓN",
                color = PrimaryDark,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
        Spacer(modifier = Modifier.height(24.dp)) // Espacio vertical.
        // Botón de texto para volver a la pantalla de inicio de sesión de usuario.
        TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
            Text("Volver al Inicio de Sesión", color = TextColor)
        }
    }
}
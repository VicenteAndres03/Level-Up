package G1.level_up.ui

import G1.level_up.Navigation.Screen
import G1.level_up.R
import G1.level_up.repository.UserRepository
import G1.level_up.ui.theme.ButtonAccent
import G1.level_up.ui.theme.PrimaryAccent
import G1.level_up.ui.theme.PrimaryDark
import G1.level_up.ui.theme.TextColor
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    // Estados para almacenar el correo electrónico (usuario) y la contraseña introducidos por el administrador.
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    // Se obtiene el contexto actual para usarlo en el UserRepository y en los Toasts.
    val context = LocalContext.current
    // Se crea una instancia del UserRepository para poder interactuar con la base de datos de usuarios.
    val userRepository = remember { UserRepository(context) }

    // Columna principal que organiza la pantalla verticalmente y la centra.
    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa todo el tamaño de la pantalla.
            .background(PrimaryDark) // Establece el color de fondo.
            .padding(32.dp), // Añade espaciado interno.
        horizontalAlignment = Alignment.CenterHorizontally, // Centra los elementos horizontalmente.
        verticalArrangement = Arrangement.Center // Centra los elementos verticalmente.
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
            color = TextColor,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campo de texto para que el administrador introduzca su usuario (email).
        LevelUpTextField(
            value = email,
            onValueChange = { email = it },
            label = "Usuario",
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de texto para la contraseña.
        LevelUpTextField(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            keyboardType = KeyboardType.Password
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Botón para ejecutar la lógica de inicio de sesión.
        Button(
            onClick = {
                // 1. Busca al usuario en la base de datos usando el email introducido.
                val user = userRepository.getUserByUsername(email)
                // 2. Comprueba las credenciales:
                //    - El usuario debe existir (`user != null`).
                //    - La contraseña debe coincidir (`user.pass == password`).
                //    - El email debe terminar en "@level.com" para ser considerado administrador.
                if (user != null && user.pass == password && email.endsWith("@level.com", ignoreCase = true)) {
                    // Si las credenciales son correctas, navega a la pantalla de gestión de productos.
                    navController.navigate(Screen.AdminProducts.route) {
                        // Limpia la pila de navegación para que el admin no pueda volver a la pantalla de login.
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                } else {
                    // Si las credenciales son incorrectas, muestra un mensaje de error.
                    Toast.makeText(context, "Credenciales de administrador incorrectas", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonAccent)
        ) {
            Text(
                text = "INICIAR SESIÓN",
                color = PrimaryDark,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de texto para permitir al usuario volver a la pantalla de login normal.
        TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
            Text("Volver al Inicio de Sesión", color = PrimaryAccent)
        }
    }
}

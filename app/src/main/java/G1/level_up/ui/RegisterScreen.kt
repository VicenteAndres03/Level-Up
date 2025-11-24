package G1.level_up.ui

import G1.level_up.Navigation.Screen
import G1.level_up.model.User
import G1.level_up.repository.UserRepository
import G1.level_up.ui.theme.PrimaryAccent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

/**
 * Pantalla de registro de nuevos usuarios.
 *
 * @param navController El controlador de navegación para manejar las transiciones entre pantallas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    // Estados para almacenar el nombre de usuario y la contraseña.
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    // Obtiene el contexto actual.
    val context = LocalContext.current
    // Instancia del repositorio de usuarios.
    val userRepository = UserRepository(context)

    // Diseño de la pantalla en una columna que ocupa todo el espacio disponible.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A1931)) // Color de fondo oscuro.
            .padding(16.dp), // Espaciado interno.
        horizontalAlignment = Alignment.CenterHorizontally, // Alineación horizontal al centro.
        verticalArrangement = Arrangement.Center // Alineación vertical al centro.
    ) {
        // Título de la pantalla.
        Text(
            text = "Crea tu cuenta",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp)) // Espacio vertical.

        // Campo de texto para el nombre de usuario.
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFF8C00),
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color(0xFFFF8C00),
                unfocusedLabelColor = Color.Gray,
                cursorColor = Color(0xFFFF8C00),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(8.dp)) // Espacio vertical.

        // Campo de texto para la contraseña.
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFF8C00),
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color(0xFFFF8C00),
                unfocusedLabelColor = Color.Gray,
                cursorColor = Color(0xFFFF8C00),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(16.dp)) // Espacio vertical.

        // Botón para registrar al usuario.
        Button(
            onClick = {
                // Comprueba que los campos no estén vacíos.
                if (username.isNotBlank() && password.isNotBlank()) {
                    // Comprueba si el nombre de usuario ya existe.
                    val existingUser = userRepository.getUserByUsername(username)
                    if (existingUser == null) {
                        // Crea un nuevo usuario y lo añade a la base de datos.
                        val user = User(0, username, password)
                        userRepository.addUser(user)
                        // Muestra un mensaje de éxito.
                        Toast.makeText(context, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                        // Vuelve a la pantalla de inicio de sesión.
                        navController.popBackStack()
                    } else {
                        // Muestra un mensaje si el usuario ya existe.
                        Toast.makeText(context, "El nombre de usuario ya está en uso", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Muestra un mensaje si los campos están vacíos.
                    Toast.makeText(context, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            },
            shape = RoundedCornerShape(12.dp), // Bordes redondeados.
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8C00)) // Color del botón.
        ) {
            Text("Registrarse", color = Color(0xFF0A1931), fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp)) // Espacio vertical.

        // Botón de texto para ir a la pantalla de inicio de sesión.
        TextButton(onClick = {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Register.route) { inclusive = true }
            }
        }) {
            Text(
                text = "¿Ya tienes una cuenta? Inicia sesión",
                color = PrimaryAccent, // Este color es de login.kt.
                fontSize = 14.sp
            )
        }
    }
}
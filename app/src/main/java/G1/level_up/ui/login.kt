package G1.level_up.ui

import G1.level_up.Navigation.Screen
import G1.level_up.R
import G1.level_up.repository.UserRepository
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// Definición de colores para la interfaz de usuario.
val PrimaryDark = Color(0xFF0A1931)
val PrimaryAccent = Color(0xFF00A9E0)
val ButtonAccent = Color(0xFFFF8C00)
val TextColor = Color.White

/**
 * Pantalla de inicio de sesión para usuarios.
 *
 * @param navController El controlador de navegación para manejar las transiciones entre pantallas.
 * @param onLoginSuccess Una función lambda que se ejecuta cuando el inicio de sesión es exitoso, pasando el nombre de usuario.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, onLoginSuccess: (String) -> Unit) {
    // Estados para almacenar el correo electrónico y la contraseña.
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    // Obtiene el contexto actual.
    val context = LocalContext.current
    // Instancia del repositorio de usuarios.
    val userRepository = UserRepository(context)

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
            contentDescription = "Logo Level-Up Gamer",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp)) // Espacio vertical.

        // Título de la pantalla.
        Text(
            text = "Iniciar Sesión",
            color = TextColor,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campo de texto para el nombre de usuario.
        LevelUpTextField(
            value = email,
            onValueChange = { email = it },
            label = "Usuario",
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(16.dp)) // Espacio vertical.

        // Campo de texto para la contraseña.
        LevelUpTextField(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            keyboardType = KeyboardType.Password
        )

        Spacer(modifier = Modifier.height(48.dp)) // Espacio vertical.

        // Botón para iniciar sesión.
        Button(
            onClick = {
                // Busca al usuario por su nombre de usuario.
                val user = userRepository.getUserByUsername(email)
                // Comprueba si el usuario existe y la contraseña es correcta.
                if (user != null && user.pass == password) {
                    // Llama a la función de éxito de inicio de sesión.
                    onLoginSuccess(email)
                } else {
                    // Muestra un mensaje de error si las credenciales son incorrectas.
                    Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp), // Bordes redondeados.
            colors = ButtonDefaults.buttonColors(containerColor = ButtonAccent) // Color del botón.
        ) {
            Text(
                text = "INGRESAR",
                color = PrimaryDark,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp)) // Espacio vertical.

        // Botón de texto para ir a la pantalla de inicio de sesión de administrador.
        TextButton(onClick = {
            navController.navigate(Screen.AdminLogin.route)
        }) {
            Text(
                text = "¿Eres administrador? Inicia sesión aquí",
                color = PrimaryAccent,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp)) // Espacio vertical.

        // Botón de texto para ir a la pantalla de registro.
        TextButton(onClick = {
            navController.navigate(Screen.Register.route)
        }) {
            Text(
                text = "¿No tienes una cuenta? Regístrate",
                color = PrimaryAccent,
                fontSize = 14.sp
            )
        }
    }
}

/**
 * Un campo de texto personalizado para la aplicación Level-Up.
 *
 * @param value El valor actual del campo de texto.
 * @param onValueChange La función que se llama cuando el valor cambia.
 * @param label La etiqueta que se muestra en el campo de texto.
 * @param keyboardType El tipo de teclado que se muestra para este campo de texto.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelUpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true, // El campo de texto solo puede tener una línea.
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp), // Bordes redondeados.
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryAccent, // Color del borde cuando está enfocado.
            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f), // Color del borde cuando no está enfocado.
            cursorColor = PrimaryAccent, // Color del cursor.
            focusedLabelColor = PrimaryAccent, // Color de la etiqueta cuando está enfocado.
            unfocusedLabelColor = Color.Gray, // Color de la etiqueta cuando no está enfocado.
            focusedTextColor = TextColor, // Color del texto cuando está enfocado.
            unfocusedTextColor = TextColor // Color del texto cuando no está enfocado.
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType), // Opciones del teclado.
        visualTransformation = if (keyboardType == KeyboardType.Password) PasswordVisualTransformation() else VisualTransformation.None // Oculta el texto si es una contraseña.
    )
}

/**
 * Una vista previa de la pantalla de inicio de sesión.
 */
@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(rememberNavController(), onLoginSuccess = {})
}

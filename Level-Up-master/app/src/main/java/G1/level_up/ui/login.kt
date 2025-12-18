package G1.level_up.ui

import G1.level_up.Navigation.Screen
import G1.level_up.R
import G1.level_up.model.User
import G1.level_up.repository.UserRepository
import G1.level_up.ui.theme.*
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
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

/**
 * Pantalla de inicio de sesión para usuarios.
 *
 * @param navController El controlador de navegación para manejar las transiciones entre pantallas.
 * @param onLoginSuccess Una función lambda que se ejecuta cuando el inicio de sesión es exitoso, pasando el objeto User.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, onLoginSuccess: (User) -> Unit) {
    // Estados para almacenar el nombre de usuario (email) y la contraseña introducidos.
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    // Se obtiene el contexto actual, necesario para el UserRepository y para mostrar Toasts.
    val context = LocalContext.current
    // Se crea una instancia del UserRepository para interactuar con la base de datos de usuarios.
    val userRepository = UserRepository(context)

    // Columna principal que organiza toda la pantalla de inicio de sesión.
    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa todo el espacio disponible.
            .background(PrimaryDark) // Establece el color de fondo.
            .padding(32.dp), // Añade espaciado alrededor del contenido.
        horizontalAlignment = Alignment.CenterHorizontally, // Centra los elementos horizontalmente.
        verticalArrangement = Arrangement.Center // Centra los elementos verticalmente.
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

        // Campo de texto personalizado para el nombre de usuario.
        LevelUpTextField(
            value = email,
            onValueChange = { email = it },
            label = "Usuario",
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de texto personalizado para la contraseña.
        LevelUpTextField(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            keyboardType = KeyboardType.Password
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Botón principal para iniciar sesión.
        Button(
            onClick = {
                // 1. Busca al usuario en la base de datos a través del repositorio.
                val user = userRepository.getUserByUsername(email)
                // 2. Comprueba si el usuario existe y si la contraseña coincide.
                if (user != null && user.pass == password) {
                    // 3. Si las credenciales son correctas, llama a la función de éxito.
                    onLoginSuccess(user)
                } else {
                    // 4. Si son incorrectas, muestra un mensaje de error.
                    Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonAccent)
        ) {
            Text(
                text = "INGRESAR",
                color = PrimaryDark,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de texto para redirigir al inicio de sesión de administrador.
        TextButton(onClick = {
            navController.navigate(Screen.AdminLogin.route)
        }) {
            Text(
                text = "¿Eres administrador? Inicia sesión aquí",
                color = PrimaryAccent,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de texto para redirigir a la pantalla de registro.
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
 * Un campo de texto `OutlinedTextField` personalizado y reutilizable para la aplicación.
 *
 * @param value El valor actual del campo de texto.
 * @param onValueChange La función que se llama cuando el valor del texto cambia.
 * @param label El texto que se muestra como etiqueta del campo.
 * @param keyboardType El tipo de teclado que se debe mostrar (Email, Password, etc.).
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
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryAccent,
            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
            cursorColor = PrimaryAccent,
            focusedLabelColor = PrimaryAccent,
            unfocusedLabelColor = Color.Gray,
            focusedTextColor = TextColor,
            unfocusedTextColor = TextColor
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        // Oculta el texto si el tipo de teclado es para contraseñas.
        visualTransformation = if (keyboardType == KeyboardType.Password) PasswordVisualTransformation() else VisualTransformation.None
    )
}

/**
 * Una función de vista previa (Preview) para mostrar cómo se ve el `LoginScreen` en el editor de Android Studio.
 */
@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(rememberNavController(), onLoginSuccess = {})
}

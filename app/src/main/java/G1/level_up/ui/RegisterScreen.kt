package G1.level_up.ui

import G1.level_up.Navigation.Screen
import G1.level_up.model.User
import G1.level_up.repository.UserRepository
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val userRepository = UserRepository(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A1931))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Crea tu cuenta",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
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
        Spacer(modifier = Modifier.height(8.dp))
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
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (username.isNotBlank() && password.isNotBlank()) {
                    val existingUser = userRepository.getUserByUsername(username)
                    if (existingUser == null) {
                        val user = User(0, username, password)
                        userRepository.addUser(user)
                        Toast.makeText(context, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                        navController.popBackStack() // Go back to login after registration
                    } else {
                        Toast.makeText(context, "El nombre de usuario ya está en uso", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8C00))
        ) {
            Text("Registrarse", color = Color(0xFF0A1931), fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))
        TextButton(onClick = {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Register.route) { inclusive = true }
            }
        }) {
            Text(
                text = "¿Ya tienes una cuenta? Inicia sesión",
                color = PrimaryAccent, // This color is from login.kt
                fontSize = 14.sp
            )
        }
    }
}
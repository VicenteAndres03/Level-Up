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

val PrimaryDark = Color(0xFF0A1931)
val PrimaryAccent = Color(0xFF00A9E0)
val ButtonAccent = Color(0xFFFF8C00)
val TextColor = Color.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, onLoginSuccess: (String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val userRepository = UserRepository(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryDark)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logolevelup),
            contentDescription = "Logo Level-Up Gamer",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Iniciar Sesión",
            color = TextColor,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        LevelUpTextField(
            value = email,
            onValueChange = { email = it },
            label = "Usuario",
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(16.dp))

        LevelUpTextField(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            keyboardType = KeyboardType.Password
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                val user = userRepository.getUserByUsername(email)
                if (user != null && user.pass == password) {
                    onLoginSuccess(email)
                } else {
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
        singleLine = true,
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
        visualTransformation = if (keyboardType == KeyboardType.Password) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(rememberNavController(), onLoginSuccess = {})
}

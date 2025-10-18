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
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val userRepository = remember { UserRepository(context) }

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
            contentDescription = "Logo de Level-Up Gamer",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Inicio de Sesión de Administrador",
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
                if (user != null && user.pass == password && email.endsWith("@level.com", ignoreCase = true)) {
                    navController.navigate(Screen.AdminProducts.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                } else {
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

        TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
            Text("Volver al Inicio de Sesión", color = PrimaryAccent)
        }
    }
}

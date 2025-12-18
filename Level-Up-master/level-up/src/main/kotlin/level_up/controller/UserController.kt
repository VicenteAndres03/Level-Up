package level_up.controller

import level_up.model.User
import level_up.repository.UserRepository
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/api/usuarios")
class UserController(private val repository: UserRepository) {

    // Para el Registro de nuevos usuarios
    @PostMapping("/registro")
    fun registrar(@RequestBody user: User): User = repository.save(user)

    // Para el Login (Sustituye la lógica de getUserByUsername de Android)
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: User): ResponseEntity<Any> {
        val user = repository.findByUsername(loginRequest.username)
        return if (user != null && user.pass == loginRequest.pass) {
            ResponseEntity.ok(user)
        } else {
            ResponseEntity.status(401).body("Credenciales incorrectas")
        }
    }
}
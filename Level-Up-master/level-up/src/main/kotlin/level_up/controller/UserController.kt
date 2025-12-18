package level_up.controller

import level_up.entity.User
import level_up.repository.UserRepository
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = ["*"])
class UserController(private val repository: UserRepository) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: User): ResponseEntity<Any> {
        val user = repository.findByUsername(loginRequest.username)
        return if (user != null && user.pass == loginRequest.pass) {
            ResponseEntity.ok(user)
        } else {
            ResponseEntity.status(401).body("Credenciales incorrectas")
        }
    }

    @PostMapping("/registro")
    fun registrar(@RequestBody user: User): User = repository.save(user)
}
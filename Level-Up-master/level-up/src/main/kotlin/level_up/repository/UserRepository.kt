package level_up.repository

import level_up.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    // Esto permite buscar por nombre de usuario automáticamente
    fun findByUsername(username: String): User?
}
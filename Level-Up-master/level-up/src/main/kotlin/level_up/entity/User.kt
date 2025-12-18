package level_up.model

import jakarta.persistence.*

@Entity
@Table(name = "usuarios")
data class User(
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(unique = true)
    val username: String,
    val pass: String
)
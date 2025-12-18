package level_up.entity

import jakarta.persistence.*

@Entity
@Table(name = "productos")
data class Productos(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Int = 0,
    val categoria: String = "",
    val stock: Int = 0,
    val imagen: String = "",
    val caracteristicas: String = "",
    val proveedor: String = ""
)
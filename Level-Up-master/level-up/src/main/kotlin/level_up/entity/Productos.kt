package level_up.entity

import jakarta.persistence.*

@Entity
@Table(name = "productos")
class Producto(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val categoria: String,
    val stock: Int,
    val imagen: String,
    val caracteristicas: String,
    val proveedor: String
)
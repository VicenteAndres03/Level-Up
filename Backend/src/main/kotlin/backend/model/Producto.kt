package backend.model

/**
 * Modelo de datos para el microservicio de productos.
 * Es idéntico al modelo en la aplicación móvil.
 */
data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val categoria: String,
    val stock: Int,
    val imagen: String,
    val caracteristicas: String,
    val proveedor: String
)
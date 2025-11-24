package G1.level_up.model

/**
 * `Producto` es una clase de datos (data class) que representa el modelo de un producto en la tienda.
 * Proporciona una estructura para almacenar toda la información relevante de un artículo.
 * Como `data class`, obtiene automáticamente métodos como `equals()`, `hashCode()`, `toString()` y `copy()`.
 *
 * @property id El identificador único del producto en la base de datos (Int).
 * @property nombre El nombre del producto.
 * @property descripcion Una descripción detallada del producto.
 * @property precio El precio del producto en la moneda local (Int).
 * @property categoria La categoría a la que pertenece el producto (ej. "Consolas", "Periféricos").
 * @property stock La cantidad de unidades disponibles en el inventario.
 * @property imagen La URL que apunta a la imagen del producto.
 * @property caracteristicas Una lista de características o especificaciones técnicas.
 * @property proveedor El nombre del proveedor o fabricante del producto.
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

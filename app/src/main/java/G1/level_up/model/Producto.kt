package G1.level_up.model

data class Producto(val id: Int,
                    val nombre: String,
                    val descripcion: String,
                    val precio: Int,
                    val categoria: String,
                    val stock: Int,
                    val imagen: String,
                    val caracteristicas: String,
                    val proveedor : String) {

}
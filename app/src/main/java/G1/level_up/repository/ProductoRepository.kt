package G1.level_up.repository
import G1.level_up.model.Producto

class ProductoRepository {

    fun obtenerProductos(): List<Producto> {
        return listOf(
            Producto(
                id = 1,
                nombre = "Headset Gamer HyperX Cloud Alpha",
                descripcion = "Audífonos con micrófono y almohadillas de espuma viscoelástica...",
                precio = 99990,
                categoria = "Accesorios",
                stock = 25,
                imagen = "url_ficticia_hyperx",
                caracteristicas = "Accesorio innalambrico auditivo con micrófono y almohadillas de espuma viscoelástica...",
                proveedor = "Hyperx",

                ),
            Producto(
                id = 2,
                nombre = "PlayStation 5",
                descripcion = "La consola de última generación de Sony, que ofrece gráficos impresionantes...",
                precio = 590000,
                categoria = "Consolas",
                stock = 10,
                imagen = "url_ficticia_ps5",
                caracteristicas = "Consola de videojuegos ps5 con memoria de 1TB + FC26",
                proveedor = "Sony"

            ),
            Producto(
                id = 3,
                nombre = "Silla Gamer Secretlab Titan",
                descripcion = "Diseñada para el máximo confort, esta silla ofrece un soporte ergonómico...",
                precio = 350990,
                categoria = "Sillas",
                stock = 5,
                imagen = "url_ficticia_silla",
                caracteristicas = "Espuma de curado en frío pendiente de patente," +
                        "Apoyo óptimo independientemente de cómo te sientes",
                proveedor = "Secret lab"
            ),
            Producto(
                id = 4,
                nombre = "Teclado Gamer",
                descripcion = "Teclado rgb con teclas mecanicas...",
                precio = 35000,
                categoria = "Teclado",
                stock = 5,
                imagen = "url_ficticia_silla",
                caracteristicas = "Espuma de curado en frío pendiente de patente," +
                        "Apoyo óptimo independientemente de cómo te sientes",
                proveedor = "LogiTech"
            )
        )
    }
}
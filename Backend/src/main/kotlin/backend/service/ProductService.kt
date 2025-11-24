package backend.service

import backend.model.Producto
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicInteger

/**
 * Servicio que simula la base de datos de productos (en memoria).
 * Utiliza un mapa mutable para el almacenamiento y un contador atómico para los IDs.
 */
@Service
class ProductService {

    private val products = mutableMapOf<Int, Producto>()
    private val idCounter = AtomicInteger(0)

    init {
        // Datos iniciales para que la aplicación no esté vacía al iniciar el backend
        addProduct(Producto(0, "Headset Gamer HyperX Cloud Alpha", "Audífonos con tecnología HyperX Dual Chamber.", 99990, "Audio", 3, "https://media.solotodo.com/media/products/133461_picture_1652988450.webp", "Sonido 7.1", "HyperX"))
        addProduct(Producto(0, "Mouse Gamer Logitech G502", "Mouse de alto rendimiento con sensor HERO 25K.", 69990, "Periféricos", 25, "https://media.solotodo.com/media/products/56793_picture_1583595568.webp", "11 botones", "Logitech"))
    }

    fun getAllProducts(): List<Producto> = products.values.toList()

    fun addProduct(product: Producto): Producto {
        val newId = idCounter.incrementAndGet()
        // Spring Boot simula que el ID se genera en el servidor
        val newProduct = product.copy(id = newId) 
        products[newId] = newProduct
        return newProduct
    }

    fun updateProduct(id: Int, updatedProduct: Producto): Producto? {
        if (!products.containsKey(id)) return null
        
        // Asegura que el ID del producto actualizado sea el mismo que el de la URL
        val productToSave = updatedProduct.copy(id = id)
        products[id] = productToSave
        return productToSave
    }

    fun deleteProduct(id: Int): Boolean {
        return products.remove(id) != null
    }

    fun getProductById(id: Int): Producto? {
        return products[id]
    }
}
package G1.level_up.repository

import G1.level_up.model.Producto
import G1.level_up.network.ProductApiService
import G1.level_up.network.RetrofitClient
import android.content.Context
import android.util.Log

class ProductoRepository(context: Context) {

    private val apiService: ProductApiService = RetrofitClient.getService(ProductApiService::class.java)
    private val dbHelper = DatabaseHelper(context)

    suspend fun obtenerProductos(): List<Producto> {
        return try {
            val response = apiService.getAllProducts()
            if (response.isSuccessful && response.body() != null) {
                val products = response.body()!!
                dbHelper.syncProducts(products)
                products
            } else {
                Log.e("ProductoRepository", "Error al obtener productos: ${response.code()} ${response.message()}. Usando datos locales.")
                // [FALLBACK INICIAL] Si la API responde con un error HTTP, usamos el caché.
                dbHelper.obtenerProductosLocal()
            }
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Excepción al obtener productos (Fallo de red): ${e.message}. Usando datos locales.")
            // [FALLBACK DE RED] Si falla la conexión, usamos el caché.
            dbHelper.obtenerProductosLocal()
        }
    }

    // Retorna el producto con el ID generado, o null si falla.
    suspend fun addProduct(product: Producto): Producto? {
        return try {
            val response = apiService.addProduct(product)
            if (response.isSuccessful && response.body() != null) {
                return response.body() // Éxito con el backend
            } else {
                Log.e("ProductoRepository", "Error al añadir producto: ${response.code()} ${response.message()}")
                return null
            }
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Excepción al añadir producto (Fallo de red): ${e.message}. Guardando en local.")

            // [MODIFICACIÓN CLAVE] FALLBACK A LOCAL: Guardar en SQLite.
            // Creamos un producto temporal (id=0) para que SQLite genere el ID.
            val newLocalId = dbHelper.addProductLocal(product.copy(id = 0))

            // Devolvemos el producto recién guardado con el ID local
            return dbHelper.getProductByIdLocal(newLocalId.toInt())
        }
    }

    suspend fun updateProduct(product: Producto): Boolean {
        return try {
            val response = apiService.updateProduct(product.id, product)
            if (!response.isSuccessful) {
                Log.e("ProductoRepository", "Error al actualizar producto: ${response.code()} ${response.message()}")
            }
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Excepción al actualizar producto: ${e.message}")
            false
        }
    }

    suspend fun deleteProduct(productId: Int): Boolean {
        return try {
            val response = apiService.deleteProduct(productId)
            if (!response.isSuccessful) {
                Log.e("ProductoRepository", "Error al eliminar producto: ${response.code()} ${response.message()}")
            }
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Excepción al eliminar producto: ${e.message}")
            false
        }
    }

    fun getProductById(productId: Int): Producto? {
        return dbHelper.getProductByIdLocal(productId)
    }
}
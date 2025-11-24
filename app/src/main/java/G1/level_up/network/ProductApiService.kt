package G1.level_up.network

// Ruta: app/src/main/java/G1/level_up/network/ProductApiService.kt

import G1.level_up.model.Producto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.Response

/**
 * Interfaz de Retrofit para definir los endpoints del microservicio de Productos.
 * Estos métodos representan las operaciones CRUD remotas.
 */
interface ProductApiService {

    // Endpoint para obtener todos los productos.
    @GET("products")
    suspend fun getAllProducts(): Response<List<Producto>>

    // Endpoint para añadir un nuevo producto.
    @POST("products")
    suspend fun addProduct(@Body producto: Producto): Response<Producto>

    // Endpoint para actualizar un producto existente.
    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body producto: Producto): Response<Producto>

    // Endpoint para eliminar un producto.
    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<Void>
}
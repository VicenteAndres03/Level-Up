package G1.level_up.network

import G1.level_up.model.Producto
import retrofit2.Response
import retrofit2.http.*

/**
 * Interfaz de Retrofit para definir los endpoints del microservicio de Productos.
 * Se añade el prefijo "api/" a todas las rutas para que coincidan con el controlador del backend.
 */
interface ProductApiService {

    @GET("api/products")
    suspend fun getAllProducts(): Response<List<Producto>>

    @POST("api/products")
    suspend fun addProduct(@Body producto: Producto): Response<Producto>

    @PUT("api/products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body producto: Producto): Response<Producto>

    @DELETE("api/products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<Void>
}
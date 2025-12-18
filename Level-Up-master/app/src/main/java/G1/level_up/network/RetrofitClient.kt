package G1.level_up.network

import G1.level_up.model.Producto
import G1.level_up.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LevelUpApiService {
    // Para obtener los productos del backend
    @GET("api/productos")
    suspend fun obtenerProductos(): List<Producto>

    // Para el login
    @POST("api/usuarios/login")
    suspend fun login(@Body user: User): retrofit2.Response<User>
}
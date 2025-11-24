package G1.level_up.network

// Ruta: app/src/main/java/G1/level_up/network/RetrofitClient.kt

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objeto singleton que configura y provee la instancia de Retrofit.
 * Es el punto de partida para todas las llamadas a los microservicios.
 */
object RetrofitClient {
    // IMPORTANTE: Cambia esta URL por la dirección base de tu microservicio (ej: http://10.0.2.2:8080/api/)
    // El emulador de Android usa 10.0.2.2 para acceder a localhost.
    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    /**
     * Instancia de Retrofit, usa Gson para convertir JSON a objetos Kotlin.
     */
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Función genérica para obtener una instancia de cualquier interfaz de servicio.
     */
    fun <T> getService(service: Class<T>): T {
        return retrofit.create(service)
    }
}
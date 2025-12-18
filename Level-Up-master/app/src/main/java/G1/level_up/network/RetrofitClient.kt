package G1.level_up.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Esta es tu IP de Ethernet. El puerto 8080 es el estándar de Spring Boot.
    private const val BASE_URL = "http://192.168.1.165:8080/"

    val instance: LevelUpApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Convierte JSON a clases Kotlin
            .build()

        retrofit.create(LevelUpApiService::class.java)
    }
}
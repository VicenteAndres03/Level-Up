package G1.level_up.network

import G1.level_up.model.CryptoPrice
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.Response

// 1. Interfaz de la API Externa (ej. Coindesk para el precio de Bitcoin)
interface CryptoApiService {
    @GET("v1/bpi/currentprice/USD.json")
    suspend fun getCurrentBitcoinPrice(): Response<CryptoPrice>
}

// 2. Cliente Retrofit Dedicado a la API Externa
object ExternalApiRetrofitClient {
    private const val BASE_URL = "https://api.coindesk.com/"

    val cryptoService: CryptoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoApiService::class.java)
    }
}
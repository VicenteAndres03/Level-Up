package G1.level_up.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos para consumir el precio de una criptomoneda (ej. Bitcoin).
 * Se simula un formato de respuesta simple.
 */
data class CryptoPrice(
    @SerializedName("bpi")
    val bpi: BpiPrice
)

data class BpiPrice(
    @SerializedName("USD")
    val usd: CurrencyPrice
)

data class CurrencyPrice(
    @SerializedName("rate_float")
    val rateFloat: Float
)
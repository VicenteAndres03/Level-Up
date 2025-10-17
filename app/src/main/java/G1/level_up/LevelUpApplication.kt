package G1.level_up

import android.app.Application
import G1.level_up.repository.ProductoRepository

class LevelUpApplication : Application() {
    val productoRepository: ProductoRepository by lazy {
        ProductoRepository(this)
    }
}
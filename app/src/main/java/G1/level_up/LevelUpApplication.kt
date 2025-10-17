package G1.level_up

import android.app.Application
import G1.level_up.repository.ProductoRepository

/**
 * `LevelUpApplication` es una clase personalizada que extiende `Application`.
 * Se utiliza para mantener el estado global y las dependencias que necesitan vivir
 * durante todo el ciclo de vida de la aplicación.
 *
 * En este caso, se usa para inicializar y proveer una instancia única (singleton)
 * de `ProductoRepository`, que puede ser accedida desde cualquier parte de la app.
 *
 * IMPORTANTE: Para que esta clase sea utilizada por el sistema, debe ser declarada
 * en el archivo `AndroidManifest.xml` dentro de la etiqueta `<application>`:
 * android:name=".LevelUpApplication"
 */
class LevelUpApplication : Application() {
    /**
     * `productoRepository` es la instancia del repositorio de productos.
     * Se inicializa de forma perezosa (`lazy`), lo que significa que la instancia de `ProductoRepository`
     * no se creará hasta que se acceda a ella por primera vez. Esto es eficiente en términos de recursos.
     *
     * Este enfoque proporciona un mecanismo simple de inyección de dependencias, permitiendo que
     * diferentes partes de la app (como los ViewModels) accedan a la misma y única instancia del repositorio.
     */
    val productoRepository: ProductoRepository by lazy {
        ProductoRepository(this)
    }
}

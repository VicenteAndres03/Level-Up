package level_up.controller

import level_up.entity.Productos
import level_up.repository.ProductoRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = ["*"])
class ProductoController(private val repository: ProductoRepository) {

    @GetMapping
    fun obtenerTodos(): List<Productos> = repository.findAll()

    @PostMapping
    fun guardar(@RequestBody producto: Productos): Productos = repository.save(producto)

    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Int) = repository.deleteById(id)
}
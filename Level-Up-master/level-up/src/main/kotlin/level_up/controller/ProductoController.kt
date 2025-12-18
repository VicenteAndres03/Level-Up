package level_up.controller

import level_up.entity.Producto
import level_up.repository.ProductoRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/productos")
class ProductoController(private val repository: ProductoRepository) {

    @GetMapping
    fun listarTodo(): List<Producto> = repository.findAll()

    @PostMapping
    fun guardar(@RequestBody producto: Producto): Producto = repository.save(producto)

    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Int) = repository.deleteById(id)
}
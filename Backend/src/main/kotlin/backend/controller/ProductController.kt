package backend.controller

import backend.model.Producto
import backend.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api") // Ruta base para toda la API
class ProductController(private val productService: ProductService) {

    // GET /api/products
    @GetMapping("/products")
    fun getAllProducts(): ResponseEntity<List<Producto>> {
        val products = productService.getAllProducts()
        return ResponseEntity.ok(products)
    }

    // POST /api/products (Añadir un producto)
    @PostMapping("/products")
    fun addProduct(@RequestBody product: Producto): ResponseEntity<Producto> {
        val newProduct = productService.addProduct(product)
        return ResponseEntity(newProduct, HttpStatus.CREATED)
    }

    // PUT /api/products/{id} (Actualizar un producto)
    @PutMapping("/products/{id}")
    fun updateProduct(@PathVariable id: Int, @RequestBody product: Producto): ResponseEntity<Producto> {
        val updatedProduct = productService.updateProduct(id, product)
        return if (updatedProduct != null) {
            ResponseEntity.ok(updatedProduct)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    // DELETE /api/products/{id} (Eliminar un producto)
    @DeleteMapping("/products/{id}")
    fun deleteProduct(@PathVariable id: Int): ResponseEntity<Void> {
        val wasDeleted = productService.deleteProduct(id)
        return if (wasDeleted) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
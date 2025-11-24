package backend.controller

import backend.model.Producto
import backend.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Controlador REST para el recurso 'products'.
 * Mapea la URL base: /api/products (Como espera la app Android)
 */
@RestController
@RequestMapping("/api/products")
class ProductController(private val productService: ProductService) {

    // GET /api/products
    @GetMapping
    fun getAllProducts(): ResponseEntity<List<Producto>> {
        val products = productService.getAllProducts()
        return ResponseEntity.ok(products)
    }

    // POST /api/products (Añadir un producto)
    @PostMapping
    fun addProduct(@RequestBody product: Producto): ResponseEntity<Producto> {
        val newProduct = productService.addProduct(product)
        // Retorna 201 Created
        return ResponseEntity(newProduct, HttpStatus.CREATED) 
    }

    // PUT /api/products/{id} (Actualizar un producto)
    @PutMapping("/{id}")
    fun updateProduct(@PathVariable id: Int, @RequestBody product: Producto): ResponseEntity<Producto> {
        val updatedProduct = productService.updateProduct(id, product)
        return if (updatedProduct != null) {
            ResponseEntity.ok(updatedProduct)
        } else {
            // Retorna 404 Not Found si el ID no existe
            ResponseEntity.notFound().build() 
        }
    }

    // DELETE /api/products/{id} (Eliminar un producto)
    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Int): ResponseEntity<Void> {
        val wasDeleted = productService.deleteProduct(id)
        return if (wasDeleted) {
            // Retorna 204 No Content
            ResponseEntity.noContent().build() 
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
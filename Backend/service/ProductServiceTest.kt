package backend.service

import backend.model.Producto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Clase de prueba unitaria para validar la lógica de negocio en ProductService.
 * Nota: Como ProductService usa un repositorio en memoria, NO NECESITA mocking
 * para sus dependencias. El test es directo (integración ligera).
 */
class ProductServiceTest {

    private lateinit var productService: ProductService

    // Se ejecuta antes de cada prueba para asegurar que el servicio está limpio
    @BeforeEach
    fun setUp() {
        productService = ProductService()
        // Después de la inicialización, la lista debe contener los dos productos del 'init' block
        // (por ejemplo, Mouse y Headset).
    }

    /**
     * Prueba el método getAllProducts para verificar que carga los productos iniciales.
     * (Verifica la funcionalidad principal de lectura/obtención).
     */
    @Test
    fun `getAllProducts should return initial products`() {
        // La lista debe tener al menos los 2 productos que se añaden en el init block
        val allProducts = productService.getAllProducts()
        assertTrue(allProducts.size >= 2, "La lista debe contener al menos 2 productos iniciales")
    }

    /**
     * Prueba el método addProduct para verificar que se añade un nuevo elemento
     * y el ID es asignado por el servicio.
     * (Verifica la funcionalidad de inserción POST).
     */
    @Test
    fun `addProduct should successfully add a new product`() {
        val initialSize = productService.getAllProducts().size
        val newProduct = Producto(0, "Monitor Gaming", "Monitor 144hz", 300000, "Monitores", 5, "url", "specs", "Asus")
        
        val addedProduct = productService.addProduct(newProduct)

        // 1. Verificar que se le asignó un ID nuevo (> 0)
        assertTrue(addedProduct.id > 0, "El producto añadido debe tener un ID asignado")
        
        // 2. Verificar que la lista de productos creció
        assertEquals(initialSize + 1, productService.getAllProducts().size, "La lista debe tener un producto extra")
    }

    /**
     * Prueba el método updateProduct para cambiar el precio de un producto existente.
     * (Verifica la funcionalidad de actualización PUT).
     */
    @Test
    fun `updateProduct should modify an existing product price`() {
        // Obtener el primer producto inicial (ID 1)
        val initialProduct = productService.getProductById(1)!!
        val newPrice = 50000
        
        // Crear una copia con el nuevo precio, manteniendo el ID
        val updatedProduct = initialProduct.copy(precio = newPrice)
        
        val result = productService.updateProduct(1, updatedProduct)

        assertNotNull(result, "El resultado de la actualización no debe ser nulo")
        assertEquals(newPrice, productService.getProductById(1)?.precio, "El precio del producto debe haberse actualizado")
    }
    
    /**
     * Prueba el método deleteProduct para eliminar un producto existente.
     * (Verifica la funcionalidad de eliminación DELETE).
     */
    @Test
    fun `deleteProduct should remove an existing product`() {
        // El producto con ID 1 existe en el setup
        val wasDeleted = productService.deleteProduct(1)
        
        // Verificar que la eliminación fue exitosa
        assertTrue(wasDeleted, "La eliminación debe retornar true")
        // Verificar que el producto ya no existe
        assertNull(productService.getProductById(1), "El producto con ID 1 debe ser nulo después de la eliminación")
    }

    /**
     * Prueba que el intento de eliminar un producto inexistente falla.
     */
    @Test
    fun `deleteProduct should fail for non-existent product`() {
        val nonExistentId = 9999
        val wasDeleted = productService.deleteProduct(nonExistentId)
        
        assertFalse(wasDeleted, "La eliminación de un ID inexistente debe retornar false")
    }
}
package level_up.repository

import level_up.entity.Productos // Asegúrate de que tu entidad se llame Productos
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductoRepository : JpaRepository<Productos, Int>
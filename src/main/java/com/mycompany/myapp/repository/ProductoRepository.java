package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Producto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    @Query("SELECT p FROM Producto p WHERE p.cantidad > 0")
    List<Producto> productosDisponibles();

    @Query("SELECT COUNT(p.id) FROM Producto p")
    int productosTotales();
}

package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Producto;
import com.mycompany.myapp.domain.ProductosComprados;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductosCompradosRepository extends JpaRepository<ProductosComprados, Long> {
    @Query("SELECT c FROM ProductosComprados c WHERE c.idCompra=:idCompra")
    List<ProductosComprados> productosComradosPorCompra(@Param("idCompra") Long idCompra);

    @Query("SELECT p FROM Producto p WHERE p.id=:id")
    Producto productoPorId(@Param("id") Long id);
}

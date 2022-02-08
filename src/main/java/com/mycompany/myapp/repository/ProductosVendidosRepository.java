package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Producto;
import com.mycompany.myapp.domain.ProductosVendidos;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductosVendidosRepository extends JpaRepository<ProductosVendidos, Long> {
    @Query("SELECT pv FROM ProductosVendidos pv WHERE pv.idVenta=:idVenta")
    List<ProductosVendidos> productosVendidosPorVenta(@Param("idVenta") Long idVenta);

    @Query("SELECT p FROM Producto p WHERE p.id=:id")
    Producto productoByID(@Param("id") Long id);
}

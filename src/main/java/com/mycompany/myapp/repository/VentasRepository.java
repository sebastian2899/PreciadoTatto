package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Ventas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Ventas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VentasRepository extends JpaRepository<Ventas, Long> {
    @Query("SELECT v FROM Ventas v WHERE v.id=:id")
    Ventas ventaPorId(@Param("id") Long id);
}

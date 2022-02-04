package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Compras;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Compras entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComprasRepository extends JpaRepository<Compras, Long> {
    @Query("SELECT c FROM Compras c WHERE c.id=:id ")
    Compras compraPorId(@Param("id") Long id);
}

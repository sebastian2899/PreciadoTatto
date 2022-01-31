package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CajaTattos;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CajaTattos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CajaTattosRepository extends JpaRepository<CajaTattos, Long> {
    @Query("SELECT SUM(c.valorPagado) FROM CitaTatto c WHERE TO_CHAR(c.fechaCreacion, 'dd/MM/yyyy')=:fechaCreacion")
    BigDecimal valorTattooDia(@Param("fechaCreacion") String fechaCreacion);
}

package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CajaTattos;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
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
    @Query(
        "SELECT SUM(a.valorAbono) FROM Abono a WHERE TO_CHAR(a.fechaAbono, 'dd/MM/yyyy')=:fechaAbono" + " AND a.tipoCita = 'Cita Preciado'"
    )
    BigDecimal valorAbonoDia(@Param("fechaAbono") String fechaAbono);

    /*
     * @Query("SELECT SUM(c.valorPagado) FROM CitaTatto c WHERE TO_CHAR(c.fechaCreacion, 'dd/MM/yyyy')=:fechaCita"
     * ) BigDecimal valorTattoDia(@Param("fechaCita")String fechaCita);
     */

    @Query("SELECT c FROM CajaTattos c WHERE c.fechaCreacion BETWEEN :fechaInicio AND :fechaFin")
    List<CajaTattos> cajaTattoFecha(@Param("fechaInicio") Instant fechaInicio, @Param("fechaFin") Instant fechaFin);
}

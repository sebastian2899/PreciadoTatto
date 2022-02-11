package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Egreso;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Egreso entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EgresoRepository extends JpaRepository<Egreso, Long> {
    @Query("SELECT SUM (e.valor) FROM Egreso e WHERE TO_CHAR(e.fechaCreacion,'dd/MM/yyyy')= :fecha ")
    BigDecimal valorEgresoDiario(@Param("fecha") String fecha);

    @Query("SELECT e FROM Egreso e WHERE TO_CHAR(e.fechaCreacion,'dd/MM/yyyy')= :fecha ")
    List<Egreso> listaEgresoDiario(@Param("fecha") String fecha);

    @Query("SELECT SUM(e.valor) FROM Egreso e WHERE e.fechaCreacion BETWEEN :fechaInicio AND :fechaFin ")
    BigDecimal egresoMensual(@Param("fechaInicio") Instant fechaInicio, @Param("fechaFin") Instant fechaFin);
}

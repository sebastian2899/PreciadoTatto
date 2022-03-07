package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CajaIngresos;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CajaIngresos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CajaIngresosRepository extends JpaRepository<CajaIngresos, Long> {
    @Query("SELECT SUM(c.valorCaja) FROM CitaPerforacion c WHERE TO_CHAR(c.fechaCreacion, 'dd/MM/yyyy')=:fecha")
    BigDecimal valorCitas(@Param("fecha") String fecha);

    @Query(
        "SELECT SUM(a.valorAbono) FROM Abono a WHERE TO_CHAR(a.fechaAbono, 'dd/MM/yyyy') =:fecha" +
        " AND (a.tipoCita = 'Cita Tattoo' OR a.tipoCita = 'Cita Perforacion')"
    )
    BigDecimal valorTotalAbonos(@Param("fecha") String fecha);

    @Query("SELECT SUM(v.valorVenta) FROM Ventas v WHERE TO_CHAR(v.fechaCreacion, 'dd/MM/yyyy') =:fecha")
    BigDecimal valorVentaDia(@Param("fecha") String fecha);

    @Query("SELECT c FROM CajaIngresos c WHERE c.fechaCreacion BETWEEN :fechaInicio AND :fechaFin")
    List<CajaIngresos> cajaFechas(@Param("fechaInicio") Instant fechaInicio, @Param("fechaFin") Instant fechaFin);
}

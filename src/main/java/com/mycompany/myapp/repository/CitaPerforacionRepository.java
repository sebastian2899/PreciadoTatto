package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CitaPerforacion;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CitaPerforacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CitaPerforacionRepository extends JpaRepository<CitaPerforacion, Long> {
    @Query("SELECT c FROM CitaPerforacion c WHERE TO_CHAR(c.fechaCita, 'dd/MM/yyyy') = :fecha")
    List<CitaPerforacion> citasHoy(@Param("fecha") String fecha);

    @Query("SELECT c FROM CitaPerforacion c WHERE c.fechaCita BETWEEN :fechaInicio AND :fechaFin")
    List<CitaPerforacion> citasPorFechas(@Param("fechaInicio") Instant fechaInicio, @Param("fechaFin") Instant fechaFin);

    @Query("SELECT c FROM CitaPerforacion c WHERE c.id=:id")
    CitaPerforacion citaPorId(@Param("id") Long id);
}

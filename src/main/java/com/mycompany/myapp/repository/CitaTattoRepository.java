package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CitaTatto;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CitaTatto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CitaTattoRepository extends JpaRepository<CitaTatto, Long> {
    @Query("SELECT c FROM CitaTatto c WHERE c.id=:id")
    CitaTatto citaById(@Param("id") Long id);

    @Query("SELECT c FROM CitaTatto c WHERE TO_CHAR(c.fechaCita, 'dd/MM/yyyy') = :fecha")
    List<CitaTatto> citaDia(@Param("fecha") String fecha);

    @Query("SELECT c FROM CitaTatto c WHERE c.fechaCreacion BETWEEN :fechaInicio AND :fechaFin")
    List<CitaTatto> citasMensuales(@Param("fechaInicio") Instant fechaInicio, @Param("fechaFin") Instant fechaFin);

    @Query("SELECT c FROM CitaTatto c")
    List<CitaTatto> recordatorioCitas();
}

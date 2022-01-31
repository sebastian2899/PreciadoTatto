package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CitaTatto;
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
    @Query("SELECT c.nombre FROM Cliente c WHERE c.id =:id")
    String nombreCliente(@Param("id") Long id);
}

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
    @Query("SELECT c FROM CitaTatto c WHERE c.id=:id")
    CitaTatto citaById(@Param("id") Long id);
}

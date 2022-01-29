package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CitaPerforacion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CitaPerforacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CitaPerforacionRepository extends JpaRepository<CitaPerforacion, Long> {}

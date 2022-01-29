package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CitaTatto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CitaTatto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CitaTattoRepository extends JpaRepository<CitaTatto, Long> {}

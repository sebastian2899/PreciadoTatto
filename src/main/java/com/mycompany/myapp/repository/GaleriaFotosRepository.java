package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.GaleriaFotos;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the GaleriaFotos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GaleriaFotosRepository extends JpaRepository<GaleriaFotos, Long> {}

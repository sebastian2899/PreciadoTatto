package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CajaIngresos;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CajaIngresos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CajaIngresosRepository extends JpaRepository<CajaIngresos, Long> {}

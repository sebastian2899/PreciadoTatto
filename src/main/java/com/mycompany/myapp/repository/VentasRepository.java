package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Ventas;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Ventas entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VentasRepository extends JpaRepository<Ventas, Long> {}

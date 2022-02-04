package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Cliente;
import com.mycompany.myapp.domain.Ventas;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cliente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    @Query("SELECT v FROM Ventas v WHERE v.idCliente = :idCliente")
    List<Ventas> ventasPendientes(@Param("idCliente") Long idCliente);
}

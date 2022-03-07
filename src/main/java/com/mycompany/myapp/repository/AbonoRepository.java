package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Abono;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Abono entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AbonoRepository extends JpaRepository<Abono, Long> {
    @Query("SELECT c.deuda FROM CitaTatto c WHERE c.id=:idCita")
    BigDecimal valorDeuda(@Param("idCita") Long idCita);

    @Query("SELECT c.valorDeuda FROM CitaPerforacion c WHERE c.id=:id")
    BigDecimal valorDeudaPerfo(@Param("id") Long id);

    @Query("SELECT a FROM Abono a WHERE a.idCita=:idCita")
    List<Abono> abonosPorCita(@Param("idCita") Long idCita);

    @Query("SELECT a FROM Abono a WHERE a.id=:id")
    Abono findId(@Param("id") Long id);

    @Query("SELECT SUM(a.valorAbono) FROM Abono a WHERE a.id = :id")
    BigDecimal valorAbono(@Param("id") Long id);
}

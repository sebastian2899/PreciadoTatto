package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.EgresoDTO;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Egreso}.
 */
public interface EgresoService {
    /**
     * Save a egreso.
     *
     * @param egresoDTO the entity to save.
     * @return the persisted entity.
     */
    EgresoDTO save(EgresoDTO egresoDTO);

    /**
     * Partially updates a egreso.
     *
     * @param egresoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EgresoDTO> partialUpdate(EgresoDTO egresoDTO);

    /**
     * Get all the egresos.
     *
     * @return the list of entities.
     */
    List<EgresoDTO> findAll();

    List<EgresoDTO> egresosDiarios();

    /**
     * Get the "id" egreso.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EgresoDTO> findOne(Long id);

    /**
     * Delete the "id" egreso.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    BigDecimal valorEgresoDia();

    BigDecimal egresoMensual(Instant fechaInicio, Instant fechaFin);

    byte[] generarReporteEgresoMensual();
}

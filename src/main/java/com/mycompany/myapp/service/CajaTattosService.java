package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CajaTattosDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.CajaTattos}.
 */
public interface CajaTattosService {
    /**
     * Save a cajaTattos.
     *
     * @param cajaTattosDTO the entity to save.
     * @return the persisted entity.
     */
    CajaTattosDTO save(CajaTattosDTO cajaTattosDTO);

    /**
     * Partially updates a cajaTattos.
     *
     * @param cajaTattosDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CajaTattosDTO> partialUpdate(CajaTattosDTO cajaTattosDTO);

    /**
     * Get all the cajaTattos.
     *
     * @return the list of entities.
     */
    List<CajaTattosDTO> findAll();

    /**
     * Get the "id" cajaTattos.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CajaTattosDTO> findOne(Long id);

    BigDecimal consultarValoresDiariosTattos();

    /**
     * Delete the "id" cajaTattos.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

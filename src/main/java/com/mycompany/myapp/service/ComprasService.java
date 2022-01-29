package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ComprasDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Compras}.
 */
public interface ComprasService {
    /**
     * Save a compras.
     *
     * @param comprasDTO the entity to save.
     * @return the persisted entity.
     */
    ComprasDTO save(ComprasDTO comprasDTO);

    /**
     * Partially updates a compras.
     *
     * @param comprasDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ComprasDTO> partialUpdate(ComprasDTO comprasDTO);

    /**
     * Get all the compras.
     *
     * @return the list of entities.
     */
    List<ComprasDTO> findAll();

    /**
     * Get the "id" compras.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ComprasDTO> findOne(Long id);

    /**
     * Delete the "id" compras.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

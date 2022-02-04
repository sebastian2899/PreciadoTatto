package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.VentasDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Ventas}.
 */
public interface VentasService {
    /**
     * Save a ventas.
     *
     * @param ventasDTO the entity to save.
     * @return the persisted entity.
     */
    VentasDTO save(VentasDTO ventasDTO);

    /**
     * Partially updates a ventas.
     *
     * @param ventasDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VentasDTO> partialUpdate(VentasDTO ventasDTO);

    /**
     * Get all the ventas.
     *
     * @return the list of entities.
     */
    List<VentasDTO> findAll();

    /**
     * Get the "id" ventas.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    VentasDTO findOne(Long id);

    /**
     * Delete the "id" ventas.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CajaIngresosDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.CajaIngresos}.
 */
public interface CajaIngresosService {
    /**
     * Save a cajaIngresos.
     *
     * @param cajaIngresosDTO the entity to save.
     * @return the persisted entity.
     */
    CajaIngresosDTO save(CajaIngresosDTO cajaIngresosDTO);

    /**
     * Partially updates a cajaIngresos.
     *
     * @param cajaIngresosDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CajaIngresosDTO> partialUpdate(CajaIngresosDTO cajaIngresosDTO);

    /**
     * Get all the cajaIngresos.
     *
     * @return the list of entities.
     */
    List<CajaIngresosDTO> findAll();

    /**
     * Get the "id" cajaIngresos.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CajaIngresosDTO> findOne(Long id);

    /**
     * Delete the "id" cajaIngresos.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

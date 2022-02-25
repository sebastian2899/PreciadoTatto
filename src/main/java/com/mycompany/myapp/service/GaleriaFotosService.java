package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.GaleriaFotosDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.GaleriaFotos}.
 */
public interface GaleriaFotosService {
    /**
     * Save a galeriaFotos.
     *
     * @param galeriaFotosDTO the entity to save.
     * @return the persisted entity.
     */
    GaleriaFotosDTO save(GaleriaFotosDTO galeriaFotosDTO);

    /**
     * Partially updates a galeriaFotos.
     *
     * @param galeriaFotosDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GaleriaFotosDTO> partialUpdate(GaleriaFotosDTO galeriaFotosDTO);

    /**
     * Get all the galeriaFotos.
     *
     * @return the list of entities.
     */
    List<GaleriaFotosDTO> findAll();

    /**
     * Get the "id" galeriaFotos.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GaleriaFotosDTO> findOne(Long id);

    /**
     * Delete the "id" galeriaFotos.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.AbonoDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Abono}.
 */
public interface AbonoService {
    /**
     * Save a abono.
     *
     * @param abonoDTO the entity to save.
     * @return the persisted entity.
     */
    AbonoDTO save(AbonoDTO abonoDTO);

    /**
     * Partially updates a abono.
     *
     * @param abonoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AbonoDTO> partialUpdate(AbonoDTO abonoDTO);

    /**
     * Get all the abonos.
     *
     * @return the list of entities.
     */
    List<AbonoDTO> findAll();

    /**
     * Get the "id" abono.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AbonoDTO> findOne(Long id);

    BigDecimal valorDeuda(Long id);

    List<AbonoDTO> consultarAbonoPorCita(Long idCita);

    /**
     * Delete the "id" abono.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

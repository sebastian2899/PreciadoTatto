package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CitaTattoDTO;
import com.mycompany.myapp.service.dto.MensajeValidacionCitaDTO;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.CitaTatto}.
 */
public interface CitaTattoService {
    /**
     * Save a citaTatto.
     *
     * @param citaTattoDTO the entity to save.
     * @return the persisted entity.
     */
    CitaTattoDTO save(CitaTattoDTO citaTattoDTO);

    /**
     * Partially updates a citaTatto.
     *
     * @param citaTattoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CitaTattoDTO> partialUpdate(CitaTattoDTO citaTattoDTO);

    /**
     * Get all the citaTattos.
     *
     * @return the list of entities.
     */
    List<CitaTattoDTO> findAll();

    /**
     * Get the "id" citaTatto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CitaTattoDTO> findOne(Long id);

    /**
     * Delete the "id" citaTatto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<CitaTattoDTO> citaDia();

    List<CitaTattoDTO> citasPorFiltro(CitaTattoDTO citaTatto);

    byte[] generarReporteCitas();

    MensajeValidacionCitaDTO validarFechaCita(CitaTattoDTO citaTatto);

    List<CitaTattoDTO> citasPorFecha(String fechaCita);
}

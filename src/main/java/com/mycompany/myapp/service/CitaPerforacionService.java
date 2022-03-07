package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CitaPerforacionDTO;
import com.mycompany.myapp.service.dto.MensajeValidacionCitaDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.CitaPerforacion}.
 */
public interface CitaPerforacionService {
    /**
     * Save a citaPerforacion.
     *
     * @param citaPerforacionDTO the entity to save.
     * @return the persisted entity.
     */
    CitaPerforacionDTO save(CitaPerforacionDTO citaPerforacionDTO);

    /**
     * Partially updates a citaPerforacion.
     *
     * @param citaPerforacionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CitaPerforacionDTO> partialUpdate(CitaPerforacionDTO citaPerforacionDTO);

    /**
     * Get all the citaPerforacions.
     *
     * @return the list of entities.
     */
    List<CitaPerforacionDTO> findAll();

    /**
     * Get the "id" citaPerforacion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CitaPerforacionDTO> findOne(Long id);

    /**
     * Delete the "id" citaPerforacion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    byte[] generarReporteCitasPerfo();

    List<CitaPerforacionDTO> findCitasToday();

    List<CitaPerforacionDTO> citasPerfoPorFiltro(CitaPerforacionDTO citaPerfo);

    int consultarTipoCita(Long id);

    MensajeValidacionCitaDTO mensajeValidacion(CitaPerforacionDTO citaPerforacionDTO);

    List<CitaPerforacionDTO> citasPorFecha(String fechaCita);
}

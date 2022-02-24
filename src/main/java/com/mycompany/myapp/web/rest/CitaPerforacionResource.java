package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.CitaPerforacionRepository;
import com.mycompany.myapp.service.CitaPerforacionService;
import com.mycompany.myapp.service.dto.CitaPerforacionDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.CitaPerforacion}.
 */
@RestController
@RequestMapping("/api")
public class CitaPerforacionResource {

    private final Logger log = LoggerFactory.getLogger(CitaPerforacionResource.class);

    private static final String ENTITY_NAME = "citaPerforacion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CitaPerforacionService citaPerforacionService;

    private final CitaPerforacionRepository citaPerforacionRepository;

    public CitaPerforacionResource(CitaPerforacionService citaPerforacionService, CitaPerforacionRepository citaPerforacionRepository) {
        this.citaPerforacionService = citaPerforacionService;
        this.citaPerforacionRepository = citaPerforacionRepository;
    }

    /**
     * {@code POST  /cita-perforacions} : Create a new citaPerforacion.
     *
     * @param citaPerforacionDTO the citaPerforacionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new citaPerforacionDTO, or with status {@code 400 (Bad Request)} if the citaPerforacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cita-perforacions")
    public ResponseEntity<CitaPerforacionDTO> createCitaPerforacion(@RequestBody CitaPerforacionDTO citaPerforacionDTO)
        throws URISyntaxException {
        log.debug("REST request to save CitaPerforacion : {}", citaPerforacionDTO);
        if (citaPerforacionDTO.getId() != null) {
            throw new BadRequestAlertException("A new citaPerforacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CitaPerforacionDTO result = citaPerforacionService.save(citaPerforacionDTO);
        return ResponseEntity
            .created(new URI("/api/cita-perforacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("citaPerfoFiltro")
    public ResponseEntity<List<CitaPerforacionDTO>> citasPorFiltro(@RequestBody CitaPerforacionDTO citaPerfo) throws URISyntaxException {
        log.debug("REST request to get all citas per filtro");

        List<CitaPerforacionDTO> citas = citaPerforacionService.citasPerfoPorFiltro(citaPerfo);

        return ResponseEntity.ok().body(citas);
    }

    /**
     * {@code PUT  /cita-perforacions/:id} : Updates an existing citaPerforacion.
     *
     * @param id the id of the citaPerforacionDTO to save.
     * @param citaPerforacionDTO the citaPerforacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated citaPerforacionDTO,
     * or with status {@code 400 (Bad Request)} if the citaPerforacionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the citaPerforacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cita-perforacions/{id}")
    public ResponseEntity<CitaPerforacionDTO> updateCitaPerforacion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CitaPerforacionDTO citaPerforacionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CitaPerforacion : {}, {}", id, citaPerforacionDTO);
        if (citaPerforacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, citaPerforacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!citaPerforacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CitaPerforacionDTO result = citaPerforacionService.save(citaPerforacionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, citaPerforacionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cita-perforacions/:id} : Partial updates given fields of an existing citaPerforacion, field will ignore if it is null
     *
     * @param id the id of the citaPerforacionDTO to save.
     * @param citaPerforacionDTO the citaPerforacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated citaPerforacionDTO,
     * or with status {@code 400 (Bad Request)} if the citaPerforacionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the citaPerforacionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the citaPerforacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cita-perforacions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CitaPerforacionDTO> partialUpdateCitaPerforacion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CitaPerforacionDTO citaPerforacionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CitaPerforacion partially : {}, {}", id, citaPerforacionDTO);
        if (citaPerforacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, citaPerforacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!citaPerforacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CitaPerforacionDTO> result = citaPerforacionService.partialUpdate(citaPerforacionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, citaPerforacionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cita-perforacions} : get all the citaPerforacions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of citaPerforacions in body.
     */
    @GetMapping("/cita-perforacions")
    public List<CitaPerforacionDTO> getAllCitaPerforacions() {
        log.debug("REST request to get all CitaPerforacions");
        return citaPerforacionService.findAll();
    }

    @GetMapping("/cita-perforacions-day")
    public List<CitaPerforacionDTO> getAllCitaPerforacionsDay() {
        log.debug("REST request to get all CitaPerforacions");
        return citaPerforacionService.findCitasToday();
    }

    /**
     * {@code GET  /cita-perforacions/:id} : get the "id" citaPerforacion.
     *
     * @param id the id of the citaPerforacionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the citaPerforacionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cita-perforacions/{id}")
    public ResponseEntity<CitaPerforacionDTO> getCitaPerforacion(@PathVariable Long id) {
        log.debug("REST request to get CitaPerforacion : {}", id);
        Optional<CitaPerforacionDTO> citaPerforacionDTO = citaPerforacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(citaPerforacionDTO);
    }

    @GetMapping("/generarReportePerfo")
    public byte[] generarReporte() {
        log.debug("Rest Request to generate report cita perfo");
        byte[] reporte = citaPerforacionService.generarReporteCitasPerfo();
        return reporte;
    }

    /**
     * {@code DELETE  /cita-perforacions/:id} : delete the "id" citaPerforacion.
     *
     * @param id the id of the citaPerforacionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cita-perforacions/{id}")
    public ResponseEntity<Void> deleteCitaPerforacion(@PathVariable Long id) {
        log.debug("REST request to delete CitaPerforacion : {}", id);
        citaPerforacionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

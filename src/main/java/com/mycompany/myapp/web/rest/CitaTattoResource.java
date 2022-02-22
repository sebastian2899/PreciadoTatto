package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CitaTatto;
import com.mycompany.myapp.repository.CitaTattoRepository;
import com.mycompany.myapp.service.CitaTattoService;
import com.mycompany.myapp.service.dto.CitaTattoDTO;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.CitaTatto}.
 */
@RestController
@RequestMapping("/api")
public class CitaTattoResource {

    private final Logger log = LoggerFactory.getLogger(CitaTattoResource.class);

    private static final String ENTITY_NAME = "citaTatto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CitaTattoService citaTattoService;

    private final CitaTattoRepository citaTattoRepository;

    public CitaTattoResource(CitaTattoService citaTattoService, CitaTattoRepository citaTattoRepository) {
        this.citaTattoService = citaTattoService;
        this.citaTattoRepository = citaTattoRepository;
    }

    /**
     * {@code POST  /cita-tattos} : Create a new citaTatto.
     *
     * @param citaTattoDTO the citaTattoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new citaTattoDTO, or with status {@code 400 (Bad Request)} if the citaTatto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cita-tattos")
    public ResponseEntity<CitaTattoDTO> createCitaTatto(@RequestBody CitaTattoDTO citaTattoDTO) throws URISyntaxException {
        log.debug("REST request to save CitaTatto : {}", citaTattoDTO);
        if (citaTattoDTO.getId() != null) {
            throw new BadRequestAlertException("A new citaTatto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CitaTattoDTO result = citaTattoService.save(citaTattoDTO);
        return ResponseEntity
            .created(new URI("/api/cita-tattos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/citaTattoFiltro")
    public ResponseEntity<List<CitaTattoDTO>> citasPorFiltro(@RequestBody CitaTattoDTO citaTatto) {
        log.debug("REST request to get citas tattos per filter");

        List<CitaTattoDTO> citas = citaTattoService.citasPorFiltro(citaTatto);

        return ResponseEntity.ok().body(citas);
    }

    /**
     * {@code PUT  /cita-tattos/:id} : Updates an existing citaTatto.
     *
     * @param id the id of the citaTattoDTO to save.
     * @param citaTattoDTO the citaTattoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated citaTattoDTO,
     * or with status {@code 400 (Bad Request)} if the citaTattoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the citaTattoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cita-tattos/{id}")
    public ResponseEntity<CitaTattoDTO> updateCitaTatto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CitaTattoDTO citaTattoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CitaTatto : {}, {}", id, citaTattoDTO);
        if (citaTattoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, citaTattoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!citaTattoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CitaTattoDTO result = citaTattoService.save(citaTattoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, citaTattoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cita-tattos/:id} : Partial updates given fields of an existing citaTatto, field will ignore if it is null
     *
     * @param id the id of the citaTattoDTO to save.
     * @param citaTattoDTO the citaTattoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated citaTattoDTO,
     * or with status {@code 400 (Bad Request)} if the citaTattoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the citaTattoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the citaTattoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cita-tattos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CitaTattoDTO> partialUpdateCitaTatto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CitaTattoDTO citaTattoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CitaTatto partially : {}, {}", id, citaTattoDTO);
        if (citaTattoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, citaTattoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!citaTattoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CitaTattoDTO> result = citaTattoService.partialUpdate(citaTattoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, citaTattoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cita-tattos} : get all the citaTattos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of citaTattos in body.
     */
    @GetMapping("/cita-tattos")
    public List<CitaTattoDTO> getAllCitaTattos() {
        log.debug("REST request to get all CitaTattos");
        return citaTattoService.findAll();
    }

    @GetMapping("/cita-tattos-dia")
    public List<CitaTattoDTO> getAllCitaTattosDia() {
        log.debug("REST request to get all CitaTattos Day");
        return citaTattoService.citaDia();
    }

    @GetMapping("/generarReport")
    public byte[] generarReporteCita() {
        log.debug("REST request to generate Report cita");
        return citaTattoService.generarReporteCitas();
    }

    /**
     * {@code GET  /cita-tattos/:id} : get the "id" citaTatto.
     *
     * @param id the id of the citaTattoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the citaTattoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cita-tattos/{id}")
    public ResponseEntity<CitaTattoDTO> getCitaTatto(@PathVariable Long id) {
        log.debug("REST request to get CitaTatto : {}", id);
        Optional<CitaTattoDTO> citaTattoDTO = citaTattoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(citaTattoDTO);
    }

    /**
     * {@code DELETE  /cita-tattos/:id} : delete the "id" citaTatto.
     *
     * @param id the id of the citaTattoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cita-tattos/{id}")
    public ResponseEntity<Void> deleteCitaTatto(@PathVariable Long id) {
        log.debug("REST request to delete CitaTatto : {}", id);
        citaTattoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

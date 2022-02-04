package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.VentasRepository;
import com.mycompany.myapp.service.VentasService;
import com.mycompany.myapp.service.dto.VentasDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Ventas}.
 */
@RestController
@RequestMapping("/api")
public class VentasResource {

    private final Logger log = LoggerFactory.getLogger(VentasResource.class);

    private static final String ENTITY_NAME = "ventas";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VentasService ventasService;

    private final VentasRepository ventasRepository;

    public VentasResource(VentasService ventasService, VentasRepository ventasRepository) {
        this.ventasService = ventasService;
        this.ventasRepository = ventasRepository;
    }

    /**
     * {@code POST  /ventas} : Create a new ventas.
     *
     * @param ventasDTO the ventasDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ventasDTO, or with status {@code 400 (Bad Request)} if the ventas has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ventas")
    public ResponseEntity<VentasDTO> createVentas(@RequestBody VentasDTO ventasDTO) throws URISyntaxException {
        log.debug("REST request to save Ventas : {}", ventasDTO);
        if (ventasDTO.getId() != null) {
            throw new BadRequestAlertException("A new ventas cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VentasDTO result = ventasService.save(ventasDTO);
        return ResponseEntity
            .created(new URI("/api/ventas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ventas/:id} : Updates an existing ventas.
     *
     * @param id the id of the ventasDTO to save.
     * @param ventasDTO the ventasDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ventasDTO,
     * or with status {@code 400 (Bad Request)} if the ventasDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ventasDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ventas/{id}")
    public ResponseEntity<VentasDTO> updateVentas(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VentasDTO ventasDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Ventas : {}, {}", id, ventasDTO);
        if (ventasDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ventasDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ventasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VentasDTO result = ventasService.save(ventasDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ventasDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ventas/:id} : Partial updates given fields of an existing ventas, field will ignore if it is null
     *
     * @param id the id of the ventasDTO to save.
     * @param ventasDTO the ventasDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ventasDTO,
     * or with status {@code 400 (Bad Request)} if the ventasDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ventasDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ventasDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ventas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VentasDTO> partialUpdateVentas(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VentasDTO ventasDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ventas partially : {}, {}", id, ventasDTO);
        if (ventasDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ventasDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ventasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VentasDTO> result = ventasService.partialUpdate(ventasDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ventasDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ventas} : get all the ventas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ventas in body.
     */
    @GetMapping("/ventas")
    public List<VentasDTO> getAllVentas() {
        log.debug("REST request to get all Ventas");
        return ventasService.findAll();
    }

    /**
     * {@code GET  /ventas/:id} : get the "id" ventas.
     *
     * @param id the id of the ventasDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ventasDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ventas/{id}")
    public ResponseEntity<VentasDTO> getVentas(@PathVariable Long id) {
        log.debug("REST request to get Ventas : {}", id);
        VentasDTO ventasDTO = ventasService.findOne(id);
        return ResponseEntity.ok().body(ventasDTO);
    }

    /**
     * {@code DELETE  /ventas/:id} : delete the "id" ventas.
     *
     * @param id the id of the ventasDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ventas/{id}")
    public ResponseEntity<Void> deleteVentas(@PathVariable Long id) {
        log.debug("REST request to delete Ventas : {}", id);
        ventasService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

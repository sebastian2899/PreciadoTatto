package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.CajaTattosRepository;
import com.mycompany.myapp.service.CajaTattosService;
import com.mycompany.myapp.service.dto.CajaTattosDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.math.BigDecimal;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.CajaTattos}.
 */
@RestController
@RequestMapping("/api")
public class CajaTattosResource {

    private final Logger log = LoggerFactory.getLogger(CajaTattosResource.class);

    private static final String ENTITY_NAME = "cajaTattos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CajaTattosService cajaTattosService;

    private final CajaTattosRepository cajaTattosRepository;

    public CajaTattosResource(CajaTattosService cajaTattosService, CajaTattosRepository cajaTattosRepository) {
        this.cajaTattosService = cajaTattosService;
        this.cajaTattosRepository = cajaTattosRepository;
    }

    /**
     * {@code POST  /caja-tattos} : Create a new cajaTattos.
     *
     * @param cajaTattosDTO the cajaTattosDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cajaTattosDTO, or with status {@code 400 (Bad Request)} if the cajaTattos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/caja-tattos")
    public ResponseEntity<CajaTattosDTO> createCajaTattos(@RequestBody CajaTattosDTO cajaTattosDTO) throws URISyntaxException {
        log.debug("REST request to save CajaTattos : {}", cajaTattosDTO);
        if (cajaTattosDTO.getId() != null) {
            throw new BadRequestAlertException("A new cajaTattos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CajaTattosDTO result = cajaTattosService.save(cajaTattosDTO);
        return ResponseEntity
            .created(new URI("/api/caja-tattos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /caja-tattos/:id} : Updates an existing cajaTattos.
     *
     * @param id the id of the cajaTattosDTO to save.
     * @param cajaTattosDTO the cajaTattosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cajaTattosDTO,
     * or with status {@code 400 (Bad Request)} if the cajaTattosDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cajaTattosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/caja-tattos/{id}")
    public ResponseEntity<CajaTattosDTO> updateCajaTattos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CajaTattosDTO cajaTattosDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CajaTattos : {}, {}", id, cajaTattosDTO);
        if (cajaTattosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cajaTattosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cajaTattosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CajaTattosDTO result = cajaTattosService.save(cajaTattosDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cajaTattosDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /caja-tattos/:id} : Partial updates given fields of an existing cajaTattos, field will ignore if it is null
     *
     * @param id the id of the cajaTattosDTO to save.
     * @param cajaTattosDTO the cajaTattosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cajaTattosDTO,
     * or with status {@code 400 (Bad Request)} if the cajaTattosDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cajaTattosDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cajaTattosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/caja-tattos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CajaTattosDTO> partialUpdateCajaTattos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CajaTattosDTO cajaTattosDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CajaTattos partially : {}, {}", id, cajaTattosDTO);
        if (cajaTattosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cajaTattosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cajaTattosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CajaTattosDTO> result = cajaTattosService.partialUpdate(cajaTattosDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cajaTattosDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /caja-tattos} : get all the cajaTattos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cajaTattos in body.
     */
    @GetMapping("/caja-tattos")
    public List<CajaTattosDTO> getAllCajaTattos() {
        log.debug("REST request to get all CajaTattos");
        return cajaTattosService.findAll();
    }

    @GetMapping("/consultarValorDia")
    public BigDecimal consultarValorDia() {
        log.debug("REST request to get valor vendido dia");
        return cajaTattosService.consultarValoresDiariosTattos();
    }

    /**
     * {@code GET  /caja-tattos/:id} : get the "id" cajaTattos.
     *
     * @param id the id of the cajaTattosDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cajaTattosDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/caja-tattos/{id}")
    public ResponseEntity<CajaTattosDTO> getCajaTattos(@PathVariable Long id) {
        log.debug("REST request to get CajaTattos : {}", id);
        Optional<CajaTattosDTO> cajaTattosDTO = cajaTattosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cajaTattosDTO);
    }

    /**
     * {@code DELETE  /caja-tattos/:id} : delete the "id" cajaTattos.
     *
     * @param id the id of the cajaTattosDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/caja-tattos/{id}")
    public ResponseEntity<Void> deleteCajaTattos(@PathVariable Long id) {
        log.debug("REST request to delete CajaTattos : {}", id);
        cajaTattosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

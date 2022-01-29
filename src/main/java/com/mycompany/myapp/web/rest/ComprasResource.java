package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ComprasRepository;
import com.mycompany.myapp.service.ComprasService;
import com.mycompany.myapp.service.dto.ComprasDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Compras}.
 */
@RestController
@RequestMapping("/api")
public class ComprasResource {

    private final Logger log = LoggerFactory.getLogger(ComprasResource.class);

    private static final String ENTITY_NAME = "compras";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComprasService comprasService;

    private final ComprasRepository comprasRepository;

    public ComprasResource(ComprasService comprasService, ComprasRepository comprasRepository) {
        this.comprasService = comprasService;
        this.comprasRepository = comprasRepository;
    }

    /**
     * {@code POST  /compras} : Create a new compras.
     *
     * @param comprasDTO the comprasDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new comprasDTO, or with status {@code 400 (Bad Request)} if the compras has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/compras")
    public ResponseEntity<ComprasDTO> createCompras(@RequestBody ComprasDTO comprasDTO) throws URISyntaxException {
        log.debug("REST request to save Compras : {}", comprasDTO);
        if (comprasDTO.getId() != null) {
            throw new BadRequestAlertException("A new compras cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComprasDTO result = comprasService.save(comprasDTO);
        return ResponseEntity
            .created(new URI("/api/compras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /compras/:id} : Updates an existing compras.
     *
     * @param id the id of the comprasDTO to save.
     * @param comprasDTO the comprasDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comprasDTO,
     * or with status {@code 400 (Bad Request)} if the comprasDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the comprasDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/compras/{id}")
    public ResponseEntity<ComprasDTO> updateCompras(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ComprasDTO comprasDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Compras : {}, {}", id, comprasDTO);
        if (comprasDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, comprasDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!comprasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ComprasDTO result = comprasService.save(comprasDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, comprasDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /compras/:id} : Partial updates given fields of an existing compras, field will ignore if it is null
     *
     * @param id the id of the comprasDTO to save.
     * @param comprasDTO the comprasDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comprasDTO,
     * or with status {@code 400 (Bad Request)} if the comprasDTO is not valid,
     * or with status {@code 404 (Not Found)} if the comprasDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the comprasDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/compras/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ComprasDTO> partialUpdateCompras(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ComprasDTO comprasDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Compras partially : {}, {}", id, comprasDTO);
        if (comprasDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, comprasDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!comprasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ComprasDTO> result = comprasService.partialUpdate(comprasDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, comprasDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /compras} : get all the compras.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of compras in body.
     */
    @GetMapping("/compras")
    public List<ComprasDTO> getAllCompras() {
        log.debug("REST request to get all Compras");
        return comprasService.findAll();
    }

    /**
     * {@code GET  /compras/:id} : get the "id" compras.
     *
     * @param id the id of the comprasDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the comprasDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/compras/{id}")
    public ResponseEntity<ComprasDTO> getCompras(@PathVariable Long id) {
        log.debug("REST request to get Compras : {}", id);
        Optional<ComprasDTO> comprasDTO = comprasService.findOne(id);
        return ResponseUtil.wrapOrNotFound(comprasDTO);
    }

    /**
     * {@code DELETE  /compras/:id} : delete the "id" compras.
     *
     * @param id the id of the comprasDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/compras/{id}")
    public ResponseEntity<Void> deleteCompras(@PathVariable Long id) {
        log.debug("REST request to delete Compras : {}", id);
        comprasService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

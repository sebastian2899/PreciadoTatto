package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.GaleriaFotosRepository;
import com.mycompany.myapp.service.GaleriaFotosService;
import com.mycompany.myapp.service.dto.GaleriaFotosDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.GaleriaFotos}.
 */
@RestController
@RequestMapping("/api")
public class GaleriaFotosResource {

    private final Logger log = LoggerFactory.getLogger(GaleriaFotosResource.class);

    private static final String ENTITY_NAME = "galeriaFotos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GaleriaFotosService galeriaFotosService;

    private final GaleriaFotosRepository galeriaFotosRepository;

    public GaleriaFotosResource(GaleriaFotosService galeriaFotosService, GaleriaFotosRepository galeriaFotosRepository) {
        this.galeriaFotosService = galeriaFotosService;
        this.galeriaFotosRepository = galeriaFotosRepository;
    }

    /**
     * {@code POST  /galeria-fotos} : Create a new galeriaFotos.
     *
     * @param galeriaFotosDTO the galeriaFotosDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new galeriaFotosDTO, or with status {@code 400 (Bad Request)} if the galeriaFotos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/galeria-fotos")
    public ResponseEntity<GaleriaFotosDTO> createGaleriaFotos(@RequestBody GaleriaFotosDTO galeriaFotosDTO) throws URISyntaxException {
        log.debug("REST request to save GaleriaFotos : {}", galeriaFotosDTO);
        if (galeriaFotosDTO.getId() != null) {
            throw new BadRequestAlertException("A new galeriaFotos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GaleriaFotosDTO result = galeriaFotosService.save(galeriaFotosDTO);
        return ResponseEntity
            .created(new URI("/api/galeria-fotos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /galeria-fotos/:id} : Updates an existing galeriaFotos.
     *
     * @param id the id of the galeriaFotosDTO to save.
     * @param galeriaFotosDTO the galeriaFotosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated galeriaFotosDTO,
     * or with status {@code 400 (Bad Request)} if the galeriaFotosDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the galeriaFotosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/galeria-fotos/{id}")
    public ResponseEntity<GaleriaFotosDTO> updateGaleriaFotos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GaleriaFotosDTO galeriaFotosDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GaleriaFotos : {}, {}", id, galeriaFotosDTO);
        if (galeriaFotosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, galeriaFotosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!galeriaFotosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GaleriaFotosDTO result = galeriaFotosService.save(galeriaFotosDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, galeriaFotosDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /galeria-fotos/:id} : Partial updates given fields of an existing galeriaFotos, field will ignore if it is null
     *
     * @param id the id of the galeriaFotosDTO to save.
     * @param galeriaFotosDTO the galeriaFotosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated galeriaFotosDTO,
     * or with status {@code 400 (Bad Request)} if the galeriaFotosDTO is not valid,
     * or with status {@code 404 (Not Found)} if the galeriaFotosDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the galeriaFotosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/galeria-fotos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GaleriaFotosDTO> partialUpdateGaleriaFotos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GaleriaFotosDTO galeriaFotosDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GaleriaFotos partially : {}, {}", id, galeriaFotosDTO);
        if (galeriaFotosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, galeriaFotosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!galeriaFotosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GaleriaFotosDTO> result = galeriaFotosService.partialUpdate(galeriaFotosDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, galeriaFotosDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /galeria-fotos} : get all the galeriaFotos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of galeriaFotos in body.
     */
    @GetMapping("/galeria-fotos")
    public List<GaleriaFotosDTO> getAllGaleriaFotos() {
        log.debug("REST request to get all GaleriaFotos");
        return galeriaFotosService.findAll();
    }

    /**
     * {@code GET  /galeria-fotos/:id} : get the "id" galeriaFotos.
     *
     * @param id the id of the galeriaFotosDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the galeriaFotosDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/galeria-fotos/{id}")
    public ResponseEntity<GaleriaFotosDTO> getGaleriaFotos(@PathVariable Long id) {
        log.debug("REST request to get GaleriaFotos : {}", id);
        Optional<GaleriaFotosDTO> galeriaFotosDTO = galeriaFotosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(galeriaFotosDTO);
    }

    /**
     * {@code DELETE  /galeria-fotos/:id} : delete the "id" galeriaFotos.
     *
     * @param id the id of the galeriaFotosDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/galeria-fotos/{id}")
    public ResponseEntity<Void> deleteGaleriaFotos(@PathVariable Long id) {
        log.debug("REST request to delete GaleriaFotos : {}", id);
        galeriaFotosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

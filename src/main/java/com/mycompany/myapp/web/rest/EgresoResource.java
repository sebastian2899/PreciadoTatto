package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.EgresoRepository;
import com.mycompany.myapp.service.EgresoService;
import com.mycompany.myapp.service.dto.EgresoDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Egreso}.
 */
@RestController
@RequestMapping("/api")
public class EgresoResource {

    private final Logger log = LoggerFactory.getLogger(EgresoResource.class);

    private static final String ENTITY_NAME = "egreso";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EgresoService egresoService;

    private final EgresoRepository egresoRepository;

    public EgresoResource(EgresoService egresoService, EgresoRepository egresoRepository) {
        this.egresoService = egresoService;
        this.egresoRepository = egresoRepository;
    }

    /**
     * {@code POST  /egresos} : Create a new egreso.
     *
     * @param egresoDTO the egresoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new egresoDTO, or with status {@code 400 (Bad Request)} if the egreso has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/egresos")
    public ResponseEntity<EgresoDTO> createEgreso(@RequestBody EgresoDTO egresoDTO) throws URISyntaxException {
        log.debug("REST request to save Egreso : {}", egresoDTO);
        if (egresoDTO.getId() != null) {
            throw new BadRequestAlertException("A new egreso cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EgresoDTO result = egresoService.save(egresoDTO);
        return ResponseEntity
            .created(new URI("/api/egresos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /egresos/:id} : Updates an existing egreso.
     *
     * @param id the id of the egresoDTO to save.
     * @param egresoDTO the egresoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated egresoDTO,
     * or with status {@code 400 (Bad Request)} if the egresoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the egresoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/egresos/{id}")
    public ResponseEntity<EgresoDTO> updateEgreso(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EgresoDTO egresoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Egreso : {}, {}", id, egresoDTO);
        if (egresoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, egresoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!egresoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EgresoDTO result = egresoService.save(egresoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, egresoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /egresos/:id} : Partial updates given fields of an existing egreso, field will ignore if it is null
     *
     * @param id the id of the egresoDTO to save.
     * @param egresoDTO the egresoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated egresoDTO,
     * or with status {@code 400 (Bad Request)} if the egresoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the egresoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the egresoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/egresos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EgresoDTO> partialUpdateEgreso(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EgresoDTO egresoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Egreso partially : {}, {}", id, egresoDTO);
        if (egresoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, egresoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!egresoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EgresoDTO> result = egresoService.partialUpdate(egresoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, egresoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /egresos} : get all the egresos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of egresos in body.
     */
    @GetMapping("/egresos")
    public List<EgresoDTO> getAllEgresos() {
        log.debug("REST request to get all Egresos");
        return egresoService.findAll();
    }

    @GetMapping("/egresos-dia")
    public List<EgresoDTO> getAllEgresosDia() {
        log.debug("REST request to get all Egresos");
        return egresoService.egresosDiarios();
    }

    @GetMapping("/egresos-diarios")
    public BigDecimal getEgresosDayly() {
        log.debug("REST request to get Dayly Egresos");
        return egresoService.valorEgresoDia();
    }

    @GetMapping("/egresosMensual/{fechaInicio}/{fechaFin}")
    public BigDecimal getEgresosMonthly(@PathVariable String fechaInicio, @PathVariable String fechaFin) {
        log.debug("REST request to get monthly Egresos");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        BigDecimal valorMensual = BigDecimal.ZERO;

        try {
            Instant fechaIni = format.parse(fechaInicio.substring(0, fechaInicio.indexOf("T"))).toInstant();
            Instant fechaF = format.parse(fechaFin.substring(0, fechaFin.indexOf("T"))).toInstant();
            valorMensual = egresoService.egresoMensual(fechaIni, fechaF);
        } catch (ParseException e) {
            e.printStackTrace();
            valorMensual = null;
        }

        return valorMensual;
    }

    @GetMapping("/reporteEgreso")
    public ResponseEntity<byte[]> generarReporteEgreso() {
        log.debug("REST request to generate report egreso");
        byte[] reporte = egresoService.generarReporteEgresoMensual();
        return ResponseEntity.ok().body(reporte);
    }

    /**
     * {@code GET  /egresos/:id} : get the "id" egreso.
     *
     * @param id the id of the egresoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the egresoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/egresos/{id}")
    public ResponseEntity<EgresoDTO> getEgreso(@PathVariable Long id) {
        log.debug("REST request to get Egreso : {}", id);
        Optional<EgresoDTO> egresoDTO = egresoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(egresoDTO);
    }

    /**
     * {@code DELETE  /egresos/:id} : delete the "id" egreso.
     *
     * @param id the id of the egresoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/egresos/{id}")
    public ResponseEntity<Void> deleteEgreso(@PathVariable Long id) {
        log.debug("REST request to delete Egreso : {}", id);
        egresoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

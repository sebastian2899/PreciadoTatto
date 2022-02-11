package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.CajaIngresosRepository;
import com.mycompany.myapp.service.CajaIngresosService;
import com.mycompany.myapp.service.dto.CajaIngresosDTO;
import com.mycompany.myapp.service.dto.RegistroHistoricoCajaDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.CajaIngresos}.
 */
@RestController
@RequestMapping("/api")
public class CajaIngresosResource {

    private final Logger log = LoggerFactory.getLogger(CajaIngresosResource.class);

    private static final String ENTITY_NAME = "cajaIngresos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CajaIngresosService cajaIngresosService;

    private final CajaIngresosRepository cajaIngresosRepository;

    public CajaIngresosResource(CajaIngresosService cajaIngresosService, CajaIngresosRepository cajaIngresosRepository) {
        this.cajaIngresosService = cajaIngresosService;
        this.cajaIngresosRepository = cajaIngresosRepository;
    }

    /**
     * {@code POST  /caja-ingresos} : Create a new cajaIngresos.
     *
     * @param cajaIngresosDTO the cajaIngresosDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cajaIngresosDTO, or with status {@code 400 (Bad Request)} if the cajaIngresos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/caja-ingresos")
    public ResponseEntity<CajaIngresosDTO> createCajaIngresos(@RequestBody CajaIngresosDTO cajaIngresosDTO) throws URISyntaxException {
        log.debug("REST request to save CajaIngresos : {}", cajaIngresosDTO);
        if (cajaIngresosDTO.getId() != null) {
            throw new BadRequestAlertException("A new cajaIngresos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CajaIngresosDTO result = cajaIngresosService.save(cajaIngresosDTO);
        return ResponseEntity
            .created(new URI("/api/caja-ingresos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /caja-ingresos/:id} : Updates an existing cajaIngresos.
     *
     * @param id the id of the cajaIngresosDTO to save.
     * @param cajaIngresosDTO the cajaIngresosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cajaIngresosDTO,
     * or with status {@code 400 (Bad Request)} if the cajaIngresosDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cajaIngresosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/caja-ingresos/{id}")
    public ResponseEntity<CajaIngresosDTO> updateCajaIngresos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CajaIngresosDTO cajaIngresosDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CajaIngresos : {}, {}", id, cajaIngresosDTO);
        if (cajaIngresosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cajaIngresosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cajaIngresosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CajaIngresosDTO result = cajaIngresosService.save(cajaIngresosDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cajaIngresosDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /caja-ingresos/:id} : Partial updates given fields of an existing cajaIngresos, field will ignore if it is null
     *
     * @param id the id of the cajaIngresosDTO to save.
     * @param cajaIngresosDTO the cajaIngresosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cajaIngresosDTO,
     * or with status {@code 400 (Bad Request)} if the cajaIngresosDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cajaIngresosDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cajaIngresosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/caja-ingresos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CajaIngresosDTO> partialUpdateCajaIngresos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CajaIngresosDTO cajaIngresosDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CajaIngresos partially : {}, {}", id, cajaIngresosDTO);
        if (cajaIngresosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cajaIngresosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cajaIngresosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CajaIngresosDTO> result = cajaIngresosService.partialUpdate(cajaIngresosDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cajaIngresosDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /caja-ingresos} : get all the cajaIngresos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cajaIngresos in body.
     */
    @GetMapping("/caja-ingresos")
    public List<CajaIngresosDTO> getAllCajaIngresos() {
        log.debug("REST request to get all CajaIngresos");
        return cajaIngresosService.findAll();
    }

    @GetMapping("cajaIngresosFecha/{fechaInicio}/{fechaFin}")
    public RegistroHistoricoCajaDTO cajaIngresoFechas(@PathVariable String fechaInicio, @PathVariable String fechaFin) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        RegistroHistoricoCajaDTO rhcd = null;

        try {
            Instant fechaIni = format.parse(fechaInicio.substring(0, fechaInicio.indexOf("T"))).toInstant();
            Instant fechaF = format.parse(fechaFin.substring(0, fechaFin.indexOf("T"))).toInstant();
            rhcd = cajaIngresosService.cajaIngresosFecha(fechaIni, fechaF);
        } catch (ParseException e) {
            rhcd = null;
        }

        return rhcd;
    }

    @GetMapping("/caja-ingresosDia")
    public BigDecimal valorCajaDia() {
        log.debug("REST request to get all CajaIngresos");
        return cajaIngresosService.valoresDia();
    }

    /**
     * {@code GET  /caja-ingresos/:id} : get the "id" cajaIngresos.
     *
     * @param id the id of the cajaIngresosDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cajaIngresosDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/caja-ingresos/{id}")
    public ResponseEntity<CajaIngresosDTO> getCajaIngresos(@PathVariable Long id) {
        log.debug("REST request to get CajaIngresos : {}", id);
        Optional<CajaIngresosDTO> cajaIngresosDTO = cajaIngresosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cajaIngresosDTO);
    }

    /**
     * {@code DELETE  /caja-ingresos/:id} : delete the "id" cajaIngresos.
     *
     * @param id the id of the cajaIngresosDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/caja-ingresos/{id}")
    public ResponseEntity<Void> deleteCajaIngresos(@PathVariable Long id) {
        log.debug("REST request to delete CajaIngresos : {}", id);
        cajaIngresosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

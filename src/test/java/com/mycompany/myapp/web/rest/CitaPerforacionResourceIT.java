package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CitaPerforacion;
import com.mycompany.myapp.repository.CitaPerforacionRepository;
import com.mycompany.myapp.service.dto.CitaPerforacionDTO;
import com.mycompany.myapp.service.mapper.CitaPerforacionMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CitaPerforacionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CitaPerforacionResourceIT {

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_CITA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CITA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_HORA = "AAAAAAAAAA";
    private static final String UPDATED_HORA = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE_CLIENTE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_CLIENTE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALOR_PERFORACION = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_PERFORACION = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_PAGADO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_PAGADO = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_DEUDA = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_DEUDA = new BigDecimal(2);

    private static final BigDecimal DEFAULT_ESTADO = new BigDecimal(1);
    private static final BigDecimal UPDATED_ESTADO = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/cita-perforacions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CitaPerforacionRepository citaPerforacionRepository;

    @Autowired
    private CitaPerforacionMapper citaPerforacionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCitaPerforacionMockMvc;

    private CitaPerforacion citaPerforacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CitaPerforacion createEntity(EntityManager em) {
        CitaPerforacion citaPerforacion = new CitaPerforacion()
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .fechaCita(DEFAULT_FECHA_CITA)
            .hora(DEFAULT_HORA)
            .nombreCliente(DEFAULT_NOMBRE_CLIENTE)
            .valorPerforacion(DEFAULT_VALOR_PERFORACION)
            .valorPagado(DEFAULT_VALOR_PAGADO)
            .valorDeuda(DEFAULT_VALOR_DEUDA)
            .estado(DEFAULT_ESTADO);
        return citaPerforacion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CitaPerforacion createUpdatedEntity(EntityManager em) {
        CitaPerforacion citaPerforacion = new CitaPerforacion()
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .fechaCita(UPDATED_FECHA_CITA)
            .hora(UPDATED_HORA)
            .nombreCliente(UPDATED_NOMBRE_CLIENTE)
            .valorPerforacion(UPDATED_VALOR_PERFORACION)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .valorDeuda(UPDATED_VALOR_DEUDA)
            .estado(UPDATED_ESTADO);
        return citaPerforacion;
    }

    @BeforeEach
    public void initTest() {
        citaPerforacion = createEntity(em);
    }

    @Test
    @Transactional
    void createCitaPerforacion() throws Exception {
        int databaseSizeBeforeCreate = citaPerforacionRepository.findAll().size();
        // Create the CitaPerforacion
        CitaPerforacionDTO citaPerforacionDTO = citaPerforacionMapper.toDto(citaPerforacion);
        restCitaPerforacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(citaPerforacionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CitaPerforacion in the database
        List<CitaPerforacion> citaPerforacionList = citaPerforacionRepository.findAll();
        assertThat(citaPerforacionList).hasSize(databaseSizeBeforeCreate + 1);
        CitaPerforacion testCitaPerforacion = citaPerforacionList.get(citaPerforacionList.size() - 1);
        assertThat(testCitaPerforacion.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testCitaPerforacion.getFechaCita()).isEqualTo(DEFAULT_FECHA_CITA);
        assertThat(testCitaPerforacion.getHora()).isEqualTo(DEFAULT_HORA);
        assertThat(testCitaPerforacion.getNombreCliente()).isEqualTo(DEFAULT_NOMBRE_CLIENTE);
        assertThat(testCitaPerforacion.getValorPerforacion()).isEqualByComparingTo(DEFAULT_VALOR_PERFORACION);
        assertThat(testCitaPerforacion.getValorPagado()).isEqualByComparingTo(DEFAULT_VALOR_PAGADO);
        assertThat(testCitaPerforacion.getValorDeuda()).isEqualByComparingTo(DEFAULT_VALOR_DEUDA);
        assertThat(testCitaPerforacion.getEstado()).isEqualByComparingTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createCitaPerforacionWithExistingId() throws Exception {
        // Create the CitaPerforacion with an existing ID
        citaPerforacion.setId(1L);
        CitaPerforacionDTO citaPerforacionDTO = citaPerforacionMapper.toDto(citaPerforacion);

        int databaseSizeBeforeCreate = citaPerforacionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCitaPerforacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(citaPerforacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CitaPerforacion in the database
        List<CitaPerforacion> citaPerforacionList = citaPerforacionRepository.findAll();
        assertThat(citaPerforacionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCitaPerforacions() throws Exception {
        // Initialize the database
        citaPerforacionRepository.saveAndFlush(citaPerforacion);

        // Get all the citaPerforacionList
        restCitaPerforacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(citaPerforacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].fechaCita").value(hasItem(DEFAULT_FECHA_CITA.toString())))
            .andExpect(jsonPath("$.[*].hora").value(hasItem(DEFAULT_HORA)))
            .andExpect(jsonPath("$.[*].nombreCliente").value(hasItem(DEFAULT_NOMBRE_CLIENTE)))
            .andExpect(jsonPath("$.[*].valorPerforacion").value(hasItem(sameNumber(DEFAULT_VALOR_PERFORACION))))
            .andExpect(jsonPath("$.[*].valorPagado").value(hasItem(sameNumber(DEFAULT_VALOR_PAGADO))))
            .andExpect(jsonPath("$.[*].valorDeuda").value(hasItem(sameNumber(DEFAULT_VALOR_DEUDA))))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(sameNumber(DEFAULT_ESTADO))));
    }

    @Test
    @Transactional
    void getCitaPerforacion() throws Exception {
        // Initialize the database
        citaPerforacionRepository.saveAndFlush(citaPerforacion);

        // Get the citaPerforacion
        restCitaPerforacionMockMvc
            .perform(get(ENTITY_API_URL_ID, citaPerforacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(citaPerforacion.getId().intValue()))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.fechaCita").value(DEFAULT_FECHA_CITA.toString()))
            .andExpect(jsonPath("$.hora").value(DEFAULT_HORA))
            .andExpect(jsonPath("$.nombreCliente").value(DEFAULT_NOMBRE_CLIENTE))
            .andExpect(jsonPath("$.valorPerforacion").value(sameNumber(DEFAULT_VALOR_PERFORACION)))
            .andExpect(jsonPath("$.valorPagado").value(sameNumber(DEFAULT_VALOR_PAGADO)))
            .andExpect(jsonPath("$.valorDeuda").value(sameNumber(DEFAULT_VALOR_DEUDA)))
            .andExpect(jsonPath("$.estado").value(sameNumber(DEFAULT_ESTADO)));
    }

    @Test
    @Transactional
    void getNonExistingCitaPerforacion() throws Exception {
        // Get the citaPerforacion
        restCitaPerforacionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCitaPerforacion() throws Exception {
        // Initialize the database
        citaPerforacionRepository.saveAndFlush(citaPerforacion);

        int databaseSizeBeforeUpdate = citaPerforacionRepository.findAll().size();

        // Update the citaPerforacion
        CitaPerforacion updatedCitaPerforacion = citaPerforacionRepository.findById(citaPerforacion.getId()).get();
        // Disconnect from session so that the updates on updatedCitaPerforacion are not directly saved in db
        em.detach(updatedCitaPerforacion);
        updatedCitaPerforacion
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .fechaCita(UPDATED_FECHA_CITA)
            .hora(UPDATED_HORA)
            .nombreCliente(UPDATED_NOMBRE_CLIENTE)
            .valorPerforacion(UPDATED_VALOR_PERFORACION)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .valorDeuda(UPDATED_VALOR_DEUDA)
            .estado(UPDATED_ESTADO);
        CitaPerforacionDTO citaPerforacionDTO = citaPerforacionMapper.toDto(updatedCitaPerforacion);

        restCitaPerforacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, citaPerforacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(citaPerforacionDTO))
            )
            .andExpect(status().isOk());

        // Validate the CitaPerforacion in the database
        List<CitaPerforacion> citaPerforacionList = citaPerforacionRepository.findAll();
        assertThat(citaPerforacionList).hasSize(databaseSizeBeforeUpdate);
        CitaPerforacion testCitaPerforacion = citaPerforacionList.get(citaPerforacionList.size() - 1);
        assertThat(testCitaPerforacion.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testCitaPerforacion.getFechaCita()).isEqualTo(UPDATED_FECHA_CITA);
        assertThat(testCitaPerforacion.getHora()).isEqualTo(UPDATED_HORA);
        assertThat(testCitaPerforacion.getNombreCliente()).isEqualTo(UPDATED_NOMBRE_CLIENTE);
        assertThat(testCitaPerforacion.getValorPerforacion()).isEqualTo(UPDATED_VALOR_PERFORACION);
        assertThat(testCitaPerforacion.getValorPagado()).isEqualTo(UPDATED_VALOR_PAGADO);
        assertThat(testCitaPerforacion.getValorDeuda()).isEqualTo(UPDATED_VALOR_DEUDA);
        assertThat(testCitaPerforacion.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingCitaPerforacion() throws Exception {
        int databaseSizeBeforeUpdate = citaPerforacionRepository.findAll().size();
        citaPerforacion.setId(count.incrementAndGet());

        // Create the CitaPerforacion
        CitaPerforacionDTO citaPerforacionDTO = citaPerforacionMapper.toDto(citaPerforacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCitaPerforacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, citaPerforacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(citaPerforacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CitaPerforacion in the database
        List<CitaPerforacion> citaPerforacionList = citaPerforacionRepository.findAll();
        assertThat(citaPerforacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCitaPerforacion() throws Exception {
        int databaseSizeBeforeUpdate = citaPerforacionRepository.findAll().size();
        citaPerforacion.setId(count.incrementAndGet());

        // Create the CitaPerforacion
        CitaPerforacionDTO citaPerforacionDTO = citaPerforacionMapper.toDto(citaPerforacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitaPerforacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(citaPerforacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CitaPerforacion in the database
        List<CitaPerforacion> citaPerforacionList = citaPerforacionRepository.findAll();
        assertThat(citaPerforacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCitaPerforacion() throws Exception {
        int databaseSizeBeforeUpdate = citaPerforacionRepository.findAll().size();
        citaPerforacion.setId(count.incrementAndGet());

        // Create the CitaPerforacion
        CitaPerforacionDTO citaPerforacionDTO = citaPerforacionMapper.toDto(citaPerforacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitaPerforacionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(citaPerforacionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CitaPerforacion in the database
        List<CitaPerforacion> citaPerforacionList = citaPerforacionRepository.findAll();
        assertThat(citaPerforacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCitaPerforacionWithPatch() throws Exception {
        // Initialize the database
        citaPerforacionRepository.saveAndFlush(citaPerforacion);

        int databaseSizeBeforeUpdate = citaPerforacionRepository.findAll().size();

        // Update the citaPerforacion using partial update
        CitaPerforacion partialUpdatedCitaPerforacion = new CitaPerforacion();
        partialUpdatedCitaPerforacion.setId(citaPerforacion.getId());

        partialUpdatedCitaPerforacion.fechaCreacion(UPDATED_FECHA_CREACION).valorPerforacion(UPDATED_VALOR_PERFORACION);

        restCitaPerforacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCitaPerforacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCitaPerforacion))
            )
            .andExpect(status().isOk());

        // Validate the CitaPerforacion in the database
        List<CitaPerforacion> citaPerforacionList = citaPerforacionRepository.findAll();
        assertThat(citaPerforacionList).hasSize(databaseSizeBeforeUpdate);
        CitaPerforacion testCitaPerforacion = citaPerforacionList.get(citaPerforacionList.size() - 1);
        assertThat(testCitaPerforacion.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testCitaPerforacion.getFechaCita()).isEqualTo(DEFAULT_FECHA_CITA);
        assertThat(testCitaPerforacion.getHora()).isEqualTo(DEFAULT_HORA);
        assertThat(testCitaPerforacion.getNombreCliente()).isEqualTo(DEFAULT_NOMBRE_CLIENTE);
        assertThat(testCitaPerforacion.getValorPerforacion()).isEqualByComparingTo(UPDATED_VALOR_PERFORACION);
        assertThat(testCitaPerforacion.getValorPagado()).isEqualByComparingTo(DEFAULT_VALOR_PAGADO);
        assertThat(testCitaPerforacion.getValorDeuda()).isEqualByComparingTo(DEFAULT_VALOR_DEUDA);
        assertThat(testCitaPerforacion.getEstado()).isEqualByComparingTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdateCitaPerforacionWithPatch() throws Exception {
        // Initialize the database
        citaPerforacionRepository.saveAndFlush(citaPerforacion);

        int databaseSizeBeforeUpdate = citaPerforacionRepository.findAll().size();

        // Update the citaPerforacion using partial update
        CitaPerforacion partialUpdatedCitaPerforacion = new CitaPerforacion();
        partialUpdatedCitaPerforacion.setId(citaPerforacion.getId());

        partialUpdatedCitaPerforacion
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .fechaCita(UPDATED_FECHA_CITA)
            .hora(UPDATED_HORA)
            .nombreCliente(UPDATED_NOMBRE_CLIENTE)
            .valorPerforacion(UPDATED_VALOR_PERFORACION)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .valorDeuda(UPDATED_VALOR_DEUDA)
            .estado(UPDATED_ESTADO);

        restCitaPerforacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCitaPerforacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCitaPerforacion))
            )
            .andExpect(status().isOk());

        // Validate the CitaPerforacion in the database
        List<CitaPerforacion> citaPerforacionList = citaPerforacionRepository.findAll();
        assertThat(citaPerforacionList).hasSize(databaseSizeBeforeUpdate);
        CitaPerforacion testCitaPerforacion = citaPerforacionList.get(citaPerforacionList.size() - 1);
        assertThat(testCitaPerforacion.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testCitaPerforacion.getFechaCita()).isEqualTo(UPDATED_FECHA_CITA);
        assertThat(testCitaPerforacion.getHora()).isEqualTo(UPDATED_HORA);
        assertThat(testCitaPerforacion.getNombreCliente()).isEqualTo(UPDATED_NOMBRE_CLIENTE);
        assertThat(testCitaPerforacion.getValorPerforacion()).isEqualByComparingTo(UPDATED_VALOR_PERFORACION);
        assertThat(testCitaPerforacion.getValorPagado()).isEqualByComparingTo(UPDATED_VALOR_PAGADO);
        assertThat(testCitaPerforacion.getValorDeuda()).isEqualByComparingTo(UPDATED_VALOR_DEUDA);
        assertThat(testCitaPerforacion.getEstado()).isEqualByComparingTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingCitaPerforacion() throws Exception {
        int databaseSizeBeforeUpdate = citaPerforacionRepository.findAll().size();
        citaPerforacion.setId(count.incrementAndGet());

        // Create the CitaPerforacion
        CitaPerforacionDTO citaPerforacionDTO = citaPerforacionMapper.toDto(citaPerforacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCitaPerforacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, citaPerforacionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(citaPerforacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CitaPerforacion in the database
        List<CitaPerforacion> citaPerforacionList = citaPerforacionRepository.findAll();
        assertThat(citaPerforacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCitaPerforacion() throws Exception {
        int databaseSizeBeforeUpdate = citaPerforacionRepository.findAll().size();
        citaPerforacion.setId(count.incrementAndGet());

        // Create the CitaPerforacion
        CitaPerforacionDTO citaPerforacionDTO = citaPerforacionMapper.toDto(citaPerforacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitaPerforacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(citaPerforacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CitaPerforacion in the database
        List<CitaPerforacion> citaPerforacionList = citaPerforacionRepository.findAll();
        assertThat(citaPerforacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCitaPerforacion() throws Exception {
        int databaseSizeBeforeUpdate = citaPerforacionRepository.findAll().size();
        citaPerforacion.setId(count.incrementAndGet());

        // Create the CitaPerforacion
        CitaPerforacionDTO citaPerforacionDTO = citaPerforacionMapper.toDto(citaPerforacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitaPerforacionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(citaPerforacionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CitaPerforacion in the database
        List<CitaPerforacion> citaPerforacionList = citaPerforacionRepository.findAll();
        assertThat(citaPerforacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCitaPerforacion() throws Exception {
        // Initialize the database
        citaPerforacionRepository.saveAndFlush(citaPerforacion);

        int databaseSizeBeforeDelete = citaPerforacionRepository.findAll().size();

        // Delete the citaPerforacion
        restCitaPerforacionMockMvc
            .perform(delete(ENTITY_API_URL_ID, citaPerforacion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CitaPerforacion> citaPerforacionList = citaPerforacionRepository.findAll();
        assertThat(citaPerforacionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

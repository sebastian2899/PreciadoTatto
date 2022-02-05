package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CitaTatto;
import com.mycompany.myapp.repository.CitaTattoRepository;
import com.mycompany.myapp.service.dto.CitaTattoDTO;
import com.mycompany.myapp.service.mapper.CitaTattoMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link CitaTattoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CitaTattoResourceIT {

    private static final Long DEFAULT_ID_CLIENTE = 1L;
    private static final Long UPDATED_ID_CLIENTE = 2L;

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_CITA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CITA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_HORA = "AAAAAAAAAA";
    private static final String UPDATED_HORA = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_CLIENTE = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_CLIENTE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FOTO_DISENO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FOTO_DISENO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FOTO_DISENO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FOTO_DISENO_CONTENT_TYPE = "image/png";

    private static final BigDecimal DEFAULT_VALOR_TATTO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_TATTO = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_PAGADO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_PAGADO = new BigDecimal(2);

    private static final BigDecimal DEFAULT_ABONO = new BigDecimal(1);
    private static final BigDecimal UPDATED_ABONO = new BigDecimal(2);

    private static final BigDecimal DEFAULT_DEUDA = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEUDA = new BigDecimal(2);

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cita-tattos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CitaTattoRepository citaTattoRepository;

    @Autowired
    private CitaTattoMapper citaTattoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCitaTattoMockMvc;

    private CitaTatto citaTatto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CitaTatto createEntity(EntityManager em) {
        CitaTatto citaTatto = new CitaTatto()
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .fechaCita(DEFAULT_FECHA_CITA)
            .hora(DEFAULT_HORA)
            .emailCliente(DEFAULT_EMAIL_CLIENTE)
            .fotoDiseno(DEFAULT_FOTO_DISENO)
            .fotoDisenoContentType(DEFAULT_FOTO_DISENO_CONTENT_TYPE)
            .valorTatto(DEFAULT_VALOR_TATTO)
            .valorPagado(DEFAULT_VALOR_PAGADO)
            .deuda(DEFAULT_DEUDA)
            .estado(DEFAULT_ESTADO)
            .descripcion(DEFAULT_DESCRIPCION);
        return citaTatto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CitaTatto createUpdatedEntity(EntityManager em) {
        CitaTatto citaTatto = new CitaTatto()
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .fechaCita(UPDATED_FECHA_CITA)
            .hora(UPDATED_HORA)
            .emailCliente(UPDATED_EMAIL_CLIENTE)
            .fotoDiseno(UPDATED_FOTO_DISENO)
            .fotoDisenoContentType(UPDATED_FOTO_DISENO_CONTENT_TYPE)
            .valorTatto(UPDATED_VALOR_TATTO)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .deuda(UPDATED_DEUDA)
            .estado(UPDATED_ESTADO)
            .descripcion(UPDATED_DESCRIPCION);
        return citaTatto;
    }

    @BeforeEach
    public void initTest() {
        citaTatto = createEntity(em);
    }

    @Test
    @Transactional
    void createCitaTatto() throws Exception {
        int databaseSizeBeforeCreate = citaTattoRepository.findAll().size();
        // Create the CitaTatto
        CitaTattoDTO citaTattoDTO = citaTattoMapper.toDto(citaTatto);
        restCitaTattoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(citaTattoDTO)))
            .andExpect(status().isCreated());

        // Validate the CitaTatto in the database
        List<CitaTatto> citaTattoList = citaTattoRepository.findAll();
        assertThat(citaTattoList).hasSize(databaseSizeBeforeCreate + 1);
        CitaTatto testCitaTatto = citaTattoList.get(citaTattoList.size() - 1);
        assertThat(testCitaTatto.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testCitaTatto.getFechaCita()).isEqualTo(DEFAULT_FECHA_CITA);
        assertThat(testCitaTatto.getHora()).isEqualTo(DEFAULT_HORA);
        assertThat(testCitaTatto.getEmailCliente()).isEqualTo(DEFAULT_EMAIL_CLIENTE);
        assertThat(testCitaTatto.getFotoDiseno()).isEqualTo(DEFAULT_FOTO_DISENO);
        assertThat(testCitaTatto.getFotoDisenoContentType()).isEqualTo(DEFAULT_FOTO_DISENO_CONTENT_TYPE);
        assertThat(testCitaTatto.getValorTatto()).isEqualByComparingTo(DEFAULT_VALOR_TATTO);
        assertThat(testCitaTatto.getValorPagado()).isEqualByComparingTo(DEFAULT_VALOR_PAGADO);
        assertThat(testCitaTatto.getDeuda()).isEqualByComparingTo(DEFAULT_DEUDA);
        assertThat(testCitaTatto.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testCitaTatto.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void createCitaTattoWithExistingId() throws Exception {
        // Create the CitaTatto with an existing ID
        citaTatto.setId(1L);
        CitaTattoDTO citaTattoDTO = citaTattoMapper.toDto(citaTatto);

        int databaseSizeBeforeCreate = citaTattoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCitaTattoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(citaTattoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CitaTatto in the database
        List<CitaTatto> citaTattoList = citaTattoRepository.findAll();
        assertThat(citaTattoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCitaTattos() throws Exception {
        // Initialize the database
        citaTattoRepository.saveAndFlush(citaTatto);

        // Get all the citaTattoList
        restCitaTattoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(citaTatto.getId().intValue())))
            .andExpect(jsonPath("$.[*].idCliente").value(hasItem(DEFAULT_ID_CLIENTE.intValue())))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].fechaCita").value(hasItem(DEFAULT_FECHA_CITA.toString())))
            .andExpect(jsonPath("$.[*].hora").value(hasItem(DEFAULT_HORA)))
            .andExpect(jsonPath("$.[*].emailCliente").value(hasItem(DEFAULT_EMAIL_CLIENTE)))
            .andExpect(jsonPath("$.[*].fotoDisenoContentType").value(hasItem(DEFAULT_FOTO_DISENO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fotoDiseno").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTO_DISENO))))
            .andExpect(jsonPath("$.[*].valorTatto").value(hasItem(sameNumber(DEFAULT_VALOR_TATTO))))
            .andExpect(jsonPath("$.[*].valorPagado").value(hasItem(sameNumber(DEFAULT_VALOR_PAGADO))))
            .andExpect(jsonPath("$.[*].abono").value(hasItem(sameNumber(DEFAULT_ABONO))))
            .andExpect(jsonPath("$.[*].deuda").value(hasItem(sameNumber(DEFAULT_DEUDA))))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }

    @Test
    @Transactional
    void getCitaTatto() throws Exception {
        // Initialize the database
        citaTattoRepository.saveAndFlush(citaTatto);

        // Get the citaTatto
        restCitaTattoMockMvc
            .perform(get(ENTITY_API_URL_ID, citaTatto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(citaTatto.getId().intValue()))
            .andExpect(jsonPath("$.idCliente").value(DEFAULT_ID_CLIENTE.intValue()))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.fechaCita").value(DEFAULT_FECHA_CITA.toString()))
            .andExpect(jsonPath("$.hora").value(DEFAULT_HORA))
            .andExpect(jsonPath("$.emailCliente").value(DEFAULT_EMAIL_CLIENTE))
            .andExpect(jsonPath("$.fotoDisenoContentType").value(DEFAULT_FOTO_DISENO_CONTENT_TYPE))
            .andExpect(jsonPath("$.fotoDiseno").value(Base64Utils.encodeToString(DEFAULT_FOTO_DISENO)))
            .andExpect(jsonPath("$.valorTatto").value(sameNumber(DEFAULT_VALOR_TATTO)))
            .andExpect(jsonPath("$.valorPagado").value(sameNumber(DEFAULT_VALOR_PAGADO)))
            .andExpect(jsonPath("$.abono").value(sameNumber(DEFAULT_ABONO)))
            .andExpect(jsonPath("$.deuda").value(sameNumber(DEFAULT_DEUDA)))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    void getNonExistingCitaTatto() throws Exception {
        // Get the citaTatto
        restCitaTattoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCitaTatto() throws Exception {
        // Initialize the database
        citaTattoRepository.saveAndFlush(citaTatto);

        int databaseSizeBeforeUpdate = citaTattoRepository.findAll().size();

        // Update the citaTatto
        CitaTatto updatedCitaTatto = citaTattoRepository.findById(citaTatto.getId()).get();
        // Disconnect from session so that the updates on updatedCitaTatto are not directly saved in db
        em.detach(updatedCitaTatto);
        updatedCitaTatto
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .fechaCita(UPDATED_FECHA_CITA)
            .hora(UPDATED_HORA)
            .emailCliente(UPDATED_EMAIL_CLIENTE)
            .fotoDiseno(UPDATED_FOTO_DISENO)
            .fotoDisenoContentType(UPDATED_FOTO_DISENO_CONTENT_TYPE)
            .valorTatto(UPDATED_VALOR_TATTO)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .deuda(UPDATED_DEUDA)
            .estado(UPDATED_ESTADO)
            .descripcion(UPDATED_DESCRIPCION);
        CitaTattoDTO citaTattoDTO = citaTattoMapper.toDto(updatedCitaTatto);

        restCitaTattoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, citaTattoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(citaTattoDTO))
            )
            .andExpect(status().isOk());

        // Validate the CitaTatto in the database
        List<CitaTatto> citaTattoList = citaTattoRepository.findAll();
        assertThat(citaTattoList).hasSize(databaseSizeBeforeUpdate);
        CitaTatto testCitaTatto = citaTattoList.get(citaTattoList.size() - 1);
        assertThat(testCitaTatto.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testCitaTatto.getFechaCita()).isEqualTo(UPDATED_FECHA_CITA);
        assertThat(testCitaTatto.getHora()).isEqualTo(UPDATED_HORA);
        assertThat(testCitaTatto.getEmailCliente()).isEqualTo(UPDATED_EMAIL_CLIENTE);
        assertThat(testCitaTatto.getFotoDiseno()).isEqualTo(UPDATED_FOTO_DISENO);
        assertThat(testCitaTatto.getFotoDisenoContentType()).isEqualTo(UPDATED_FOTO_DISENO_CONTENT_TYPE);
        assertThat(testCitaTatto.getValorTatto()).isEqualTo(UPDATED_VALOR_TATTO);
        assertThat(testCitaTatto.getValorPagado()).isEqualTo(UPDATED_VALOR_PAGADO);
        assertThat(testCitaTatto.getDeuda()).isEqualTo(UPDATED_DEUDA);
        assertThat(testCitaTatto.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testCitaTatto.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void putNonExistingCitaTatto() throws Exception {
        int databaseSizeBeforeUpdate = citaTattoRepository.findAll().size();
        citaTatto.setId(count.incrementAndGet());

        // Create the CitaTatto
        CitaTattoDTO citaTattoDTO = citaTattoMapper.toDto(citaTatto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCitaTattoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, citaTattoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(citaTattoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CitaTatto in the database
        List<CitaTatto> citaTattoList = citaTattoRepository.findAll();
        assertThat(citaTattoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCitaTatto() throws Exception {
        int databaseSizeBeforeUpdate = citaTattoRepository.findAll().size();
        citaTatto.setId(count.incrementAndGet());

        // Create the CitaTatto
        CitaTattoDTO citaTattoDTO = citaTattoMapper.toDto(citaTatto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitaTattoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(citaTattoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CitaTatto in the database
        List<CitaTatto> citaTattoList = citaTattoRepository.findAll();
        assertThat(citaTattoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCitaTatto() throws Exception {
        int databaseSizeBeforeUpdate = citaTattoRepository.findAll().size();
        citaTatto.setId(count.incrementAndGet());

        // Create the CitaTatto
        CitaTattoDTO citaTattoDTO = citaTattoMapper.toDto(citaTatto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitaTattoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(citaTattoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CitaTatto in the database
        List<CitaTatto> citaTattoList = citaTattoRepository.findAll();
        assertThat(citaTattoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCitaTattoWithPatch() throws Exception {
        // Initialize the database
        citaTattoRepository.saveAndFlush(citaTatto);

        int databaseSizeBeforeUpdate = citaTattoRepository.findAll().size();

        // Update the citaTatto using partial update
        CitaTatto partialUpdatedCitaTatto = new CitaTatto();
        partialUpdatedCitaTatto.setId(citaTatto.getId());

        partialUpdatedCitaTatto
            .fechaCita(UPDATED_FECHA_CITA)
            .emailCliente(UPDATED_EMAIL_CLIENTE)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .deuda(UPDATED_DEUDA)
            .estado(UPDATED_ESTADO);

        restCitaTattoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCitaTatto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCitaTatto))
            )
            .andExpect(status().isOk());

        // Validate the CitaTatto in the database
        List<CitaTatto> citaTattoList = citaTattoRepository.findAll();
        assertThat(citaTattoList).hasSize(databaseSizeBeforeUpdate);
        CitaTatto testCitaTatto = citaTattoList.get(citaTattoList.size() - 1);
        assertThat(testCitaTatto.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testCitaTatto.getFechaCita()).isEqualTo(UPDATED_FECHA_CITA);
        assertThat(testCitaTatto.getHora()).isEqualTo(DEFAULT_HORA);
        assertThat(testCitaTatto.getEmailCliente()).isEqualTo(UPDATED_EMAIL_CLIENTE);
        assertThat(testCitaTatto.getFotoDiseno()).isEqualTo(DEFAULT_FOTO_DISENO);
        assertThat(testCitaTatto.getFotoDisenoContentType()).isEqualTo(DEFAULT_FOTO_DISENO_CONTENT_TYPE);
        assertThat(testCitaTatto.getValorTatto()).isEqualByComparingTo(DEFAULT_VALOR_TATTO);
        assertThat(testCitaTatto.getValorPagado()).isEqualByComparingTo(UPDATED_VALOR_PAGADO);
        assertThat(testCitaTatto.getDeuda()).isEqualByComparingTo(UPDATED_DEUDA);
        assertThat(testCitaTatto.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testCitaTatto.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void fullUpdateCitaTattoWithPatch() throws Exception {
        // Initialize the database
        citaTattoRepository.saveAndFlush(citaTatto);

        int databaseSizeBeforeUpdate = citaTattoRepository.findAll().size();

        // Update the citaTatto using partial update
        CitaTatto partialUpdatedCitaTatto = new CitaTatto();
        partialUpdatedCitaTatto.setId(citaTatto.getId());

        partialUpdatedCitaTatto
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .fechaCita(UPDATED_FECHA_CITA)
            .hora(UPDATED_HORA)
            .emailCliente(UPDATED_EMAIL_CLIENTE)
            .fotoDiseno(UPDATED_FOTO_DISENO)
            .fotoDisenoContentType(UPDATED_FOTO_DISENO_CONTENT_TYPE)
            .valorTatto(UPDATED_VALOR_TATTO)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .deuda(UPDATED_DEUDA)
            .estado(UPDATED_ESTADO)
            .descripcion(UPDATED_DESCRIPCION);

        restCitaTattoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCitaTatto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCitaTatto))
            )
            .andExpect(status().isOk());

        // Validate the CitaTatto in the database
        List<CitaTatto> citaTattoList = citaTattoRepository.findAll();
        assertThat(citaTattoList).hasSize(databaseSizeBeforeUpdate);
        CitaTatto testCitaTatto = citaTattoList.get(citaTattoList.size() - 1);
        assertThat(testCitaTatto.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testCitaTatto.getFechaCita()).isEqualTo(UPDATED_FECHA_CITA);
        assertThat(testCitaTatto.getHora()).isEqualTo(UPDATED_HORA);
        assertThat(testCitaTatto.getEmailCliente()).isEqualTo(UPDATED_EMAIL_CLIENTE);
        assertThat(testCitaTatto.getFotoDiseno()).isEqualTo(UPDATED_FOTO_DISENO);
        assertThat(testCitaTatto.getFotoDisenoContentType()).isEqualTo(UPDATED_FOTO_DISENO_CONTENT_TYPE);
        assertThat(testCitaTatto.getValorTatto()).isEqualByComparingTo(UPDATED_VALOR_TATTO);
        assertThat(testCitaTatto.getValorPagado()).isEqualByComparingTo(UPDATED_VALOR_PAGADO);
        assertThat(testCitaTatto.getDeuda()).isEqualByComparingTo(UPDATED_DEUDA);
        assertThat(testCitaTatto.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testCitaTatto.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void patchNonExistingCitaTatto() throws Exception {
        int databaseSizeBeforeUpdate = citaTattoRepository.findAll().size();
        citaTatto.setId(count.incrementAndGet());

        // Create the CitaTatto
        CitaTattoDTO citaTattoDTO = citaTattoMapper.toDto(citaTatto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCitaTattoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, citaTattoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(citaTattoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CitaTatto in the database
        List<CitaTatto> citaTattoList = citaTattoRepository.findAll();
        assertThat(citaTattoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCitaTatto() throws Exception {
        int databaseSizeBeforeUpdate = citaTattoRepository.findAll().size();
        citaTatto.setId(count.incrementAndGet());

        // Create the CitaTatto
        CitaTattoDTO citaTattoDTO = citaTattoMapper.toDto(citaTatto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitaTattoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(citaTattoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CitaTatto in the database
        List<CitaTatto> citaTattoList = citaTattoRepository.findAll();
        assertThat(citaTattoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCitaTatto() throws Exception {
        int databaseSizeBeforeUpdate = citaTattoRepository.findAll().size();
        citaTatto.setId(count.incrementAndGet());

        // Create the CitaTatto
        CitaTattoDTO citaTattoDTO = citaTattoMapper.toDto(citaTatto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCitaTattoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(citaTattoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CitaTatto in the database
        List<CitaTatto> citaTattoList = citaTattoRepository.findAll();
        assertThat(citaTattoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCitaTatto() throws Exception {
        // Initialize the database
        citaTattoRepository.saveAndFlush(citaTatto);

        int databaseSizeBeforeDelete = citaTattoRepository.findAll().size();

        // Delete the citaTatto
        restCitaTattoMockMvc
            .perform(delete(ENTITY_API_URL_ID, citaTatto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CitaTatto> citaTattoList = citaTattoRepository.findAll();
        assertThat(citaTattoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

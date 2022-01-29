package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CajaIngresos;
import com.mycompany.myapp.repository.CajaIngresosRepository;
import com.mycompany.myapp.service.dto.CajaIngresosDTO;
import com.mycompany.myapp.service.mapper.CajaIngresosMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link CajaIngresosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CajaIngresosResourceIT {

    private static final BigDecimal DEFAULT_VALOR_VENDIDO_DIA = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_VENDIDO_DIA = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_REGISTRADO_DIA = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_REGISTRADO_DIA = new BigDecimal(2);

    private static final BigDecimal DEFAULT_DIFERENCIA = new BigDecimal(1);
    private static final BigDecimal UPDATED_DIFERENCIA = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/caja-ingresos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CajaIngresosRepository cajaIngresosRepository;

    @Autowired
    private CajaIngresosMapper cajaIngresosMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCajaIngresosMockMvc;

    private CajaIngresos cajaIngresos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CajaIngresos createEntity(EntityManager em) {
        CajaIngresos cajaIngresos = new CajaIngresos()
            .valorVendidoDia(DEFAULT_VALOR_VENDIDO_DIA)
            .valorRegistradoDia(DEFAULT_VALOR_REGISTRADO_DIA)
            .diferencia(DEFAULT_DIFERENCIA);
        return cajaIngresos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CajaIngresos createUpdatedEntity(EntityManager em) {
        CajaIngresos cajaIngresos = new CajaIngresos()
            .valorVendidoDia(UPDATED_VALOR_VENDIDO_DIA)
            .valorRegistradoDia(UPDATED_VALOR_REGISTRADO_DIA)
            .diferencia(UPDATED_DIFERENCIA);
        return cajaIngresos;
    }

    @BeforeEach
    public void initTest() {
        cajaIngresos = createEntity(em);
    }

    @Test
    @Transactional
    void createCajaIngresos() throws Exception {
        int databaseSizeBeforeCreate = cajaIngresosRepository.findAll().size();
        // Create the CajaIngresos
        CajaIngresosDTO cajaIngresosDTO = cajaIngresosMapper.toDto(cajaIngresos);
        restCajaIngresosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cajaIngresosDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CajaIngresos in the database
        List<CajaIngresos> cajaIngresosList = cajaIngresosRepository.findAll();
        assertThat(cajaIngresosList).hasSize(databaseSizeBeforeCreate + 1);
        CajaIngresos testCajaIngresos = cajaIngresosList.get(cajaIngresosList.size() - 1);
        assertThat(testCajaIngresos.getValorVendidoDia()).isEqualByComparingTo(DEFAULT_VALOR_VENDIDO_DIA);
        assertThat(testCajaIngresos.getValorRegistradoDia()).isEqualByComparingTo(DEFAULT_VALOR_REGISTRADO_DIA);
        assertThat(testCajaIngresos.getDiferencia()).isEqualByComparingTo(DEFAULT_DIFERENCIA);
    }

    @Test
    @Transactional
    void createCajaIngresosWithExistingId() throws Exception {
        // Create the CajaIngresos with an existing ID
        cajaIngresos.setId(1L);
        CajaIngresosDTO cajaIngresosDTO = cajaIngresosMapper.toDto(cajaIngresos);

        int databaseSizeBeforeCreate = cajaIngresosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCajaIngresosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cajaIngresosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CajaIngresos in the database
        List<CajaIngresos> cajaIngresosList = cajaIngresosRepository.findAll();
        assertThat(cajaIngresosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCajaIngresos() throws Exception {
        // Initialize the database
        cajaIngresosRepository.saveAndFlush(cajaIngresos);

        // Get all the cajaIngresosList
        restCajaIngresosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cajaIngresos.getId().intValue())))
            .andExpect(jsonPath("$.[*].valorVendidoDia").value(hasItem(sameNumber(DEFAULT_VALOR_VENDIDO_DIA))))
            .andExpect(jsonPath("$.[*].valorRegistradoDia").value(hasItem(sameNumber(DEFAULT_VALOR_REGISTRADO_DIA))))
            .andExpect(jsonPath("$.[*].diferencia").value(hasItem(sameNumber(DEFAULT_DIFERENCIA))));
    }

    @Test
    @Transactional
    void getCajaIngresos() throws Exception {
        // Initialize the database
        cajaIngresosRepository.saveAndFlush(cajaIngresos);

        // Get the cajaIngresos
        restCajaIngresosMockMvc
            .perform(get(ENTITY_API_URL_ID, cajaIngresos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cajaIngresos.getId().intValue()))
            .andExpect(jsonPath("$.valorVendidoDia").value(sameNumber(DEFAULT_VALOR_VENDIDO_DIA)))
            .andExpect(jsonPath("$.valorRegistradoDia").value(sameNumber(DEFAULT_VALOR_REGISTRADO_DIA)))
            .andExpect(jsonPath("$.diferencia").value(sameNumber(DEFAULT_DIFERENCIA)));
    }

    @Test
    @Transactional
    void getNonExistingCajaIngresos() throws Exception {
        // Get the cajaIngresos
        restCajaIngresosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCajaIngresos() throws Exception {
        // Initialize the database
        cajaIngresosRepository.saveAndFlush(cajaIngresos);

        int databaseSizeBeforeUpdate = cajaIngresosRepository.findAll().size();

        // Update the cajaIngresos
        CajaIngresos updatedCajaIngresos = cajaIngresosRepository.findById(cajaIngresos.getId()).get();
        // Disconnect from session so that the updates on updatedCajaIngresos are not directly saved in db
        em.detach(updatedCajaIngresos);
        updatedCajaIngresos
            .valorVendidoDia(UPDATED_VALOR_VENDIDO_DIA)
            .valorRegistradoDia(UPDATED_VALOR_REGISTRADO_DIA)
            .diferencia(UPDATED_DIFERENCIA);
        CajaIngresosDTO cajaIngresosDTO = cajaIngresosMapper.toDto(updatedCajaIngresos);

        restCajaIngresosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cajaIngresosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cajaIngresosDTO))
            )
            .andExpect(status().isOk());

        // Validate the CajaIngresos in the database
        List<CajaIngresos> cajaIngresosList = cajaIngresosRepository.findAll();
        assertThat(cajaIngresosList).hasSize(databaseSizeBeforeUpdate);
        CajaIngresos testCajaIngresos = cajaIngresosList.get(cajaIngresosList.size() - 1);
        assertThat(testCajaIngresos.getValorVendidoDia()).isEqualTo(UPDATED_VALOR_VENDIDO_DIA);
        assertThat(testCajaIngresos.getValorRegistradoDia()).isEqualTo(UPDATED_VALOR_REGISTRADO_DIA);
        assertThat(testCajaIngresos.getDiferencia()).isEqualTo(UPDATED_DIFERENCIA);
    }

    @Test
    @Transactional
    void putNonExistingCajaIngresos() throws Exception {
        int databaseSizeBeforeUpdate = cajaIngresosRepository.findAll().size();
        cajaIngresos.setId(count.incrementAndGet());

        // Create the CajaIngresos
        CajaIngresosDTO cajaIngresosDTO = cajaIngresosMapper.toDto(cajaIngresos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCajaIngresosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cajaIngresosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cajaIngresosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CajaIngresos in the database
        List<CajaIngresos> cajaIngresosList = cajaIngresosRepository.findAll();
        assertThat(cajaIngresosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCajaIngresos() throws Exception {
        int databaseSizeBeforeUpdate = cajaIngresosRepository.findAll().size();
        cajaIngresos.setId(count.incrementAndGet());

        // Create the CajaIngresos
        CajaIngresosDTO cajaIngresosDTO = cajaIngresosMapper.toDto(cajaIngresos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCajaIngresosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cajaIngresosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CajaIngresos in the database
        List<CajaIngresos> cajaIngresosList = cajaIngresosRepository.findAll();
        assertThat(cajaIngresosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCajaIngresos() throws Exception {
        int databaseSizeBeforeUpdate = cajaIngresosRepository.findAll().size();
        cajaIngresos.setId(count.incrementAndGet());

        // Create the CajaIngresos
        CajaIngresosDTO cajaIngresosDTO = cajaIngresosMapper.toDto(cajaIngresos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCajaIngresosMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cajaIngresosDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CajaIngresos in the database
        List<CajaIngresos> cajaIngresosList = cajaIngresosRepository.findAll();
        assertThat(cajaIngresosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCajaIngresosWithPatch() throws Exception {
        // Initialize the database
        cajaIngresosRepository.saveAndFlush(cajaIngresos);

        int databaseSizeBeforeUpdate = cajaIngresosRepository.findAll().size();

        // Update the cajaIngresos using partial update
        CajaIngresos partialUpdatedCajaIngresos = new CajaIngresos();
        partialUpdatedCajaIngresos.setId(cajaIngresos.getId());

        partialUpdatedCajaIngresos
            .valorVendidoDia(UPDATED_VALOR_VENDIDO_DIA)
            .valorRegistradoDia(UPDATED_VALOR_REGISTRADO_DIA)
            .diferencia(UPDATED_DIFERENCIA);

        restCajaIngresosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCajaIngresos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCajaIngresos))
            )
            .andExpect(status().isOk());

        // Validate the CajaIngresos in the database
        List<CajaIngresos> cajaIngresosList = cajaIngresosRepository.findAll();
        assertThat(cajaIngresosList).hasSize(databaseSizeBeforeUpdate);
        CajaIngresos testCajaIngresos = cajaIngresosList.get(cajaIngresosList.size() - 1);
        assertThat(testCajaIngresos.getValorVendidoDia()).isEqualByComparingTo(UPDATED_VALOR_VENDIDO_DIA);
        assertThat(testCajaIngresos.getValorRegistradoDia()).isEqualByComparingTo(UPDATED_VALOR_REGISTRADO_DIA);
        assertThat(testCajaIngresos.getDiferencia()).isEqualByComparingTo(UPDATED_DIFERENCIA);
    }

    @Test
    @Transactional
    void fullUpdateCajaIngresosWithPatch() throws Exception {
        // Initialize the database
        cajaIngresosRepository.saveAndFlush(cajaIngresos);

        int databaseSizeBeforeUpdate = cajaIngresosRepository.findAll().size();

        // Update the cajaIngresos using partial update
        CajaIngresos partialUpdatedCajaIngresos = new CajaIngresos();
        partialUpdatedCajaIngresos.setId(cajaIngresos.getId());

        partialUpdatedCajaIngresos
            .valorVendidoDia(UPDATED_VALOR_VENDIDO_DIA)
            .valorRegistradoDia(UPDATED_VALOR_REGISTRADO_DIA)
            .diferencia(UPDATED_DIFERENCIA);

        restCajaIngresosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCajaIngresos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCajaIngresos))
            )
            .andExpect(status().isOk());

        // Validate the CajaIngresos in the database
        List<CajaIngresos> cajaIngresosList = cajaIngresosRepository.findAll();
        assertThat(cajaIngresosList).hasSize(databaseSizeBeforeUpdate);
        CajaIngresos testCajaIngresos = cajaIngresosList.get(cajaIngresosList.size() - 1);
        assertThat(testCajaIngresos.getValorVendidoDia()).isEqualByComparingTo(UPDATED_VALOR_VENDIDO_DIA);
        assertThat(testCajaIngresos.getValorRegistradoDia()).isEqualByComparingTo(UPDATED_VALOR_REGISTRADO_DIA);
        assertThat(testCajaIngresos.getDiferencia()).isEqualByComparingTo(UPDATED_DIFERENCIA);
    }

    @Test
    @Transactional
    void patchNonExistingCajaIngresos() throws Exception {
        int databaseSizeBeforeUpdate = cajaIngresosRepository.findAll().size();
        cajaIngresos.setId(count.incrementAndGet());

        // Create the CajaIngresos
        CajaIngresosDTO cajaIngresosDTO = cajaIngresosMapper.toDto(cajaIngresos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCajaIngresosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cajaIngresosDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cajaIngresosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CajaIngresos in the database
        List<CajaIngresos> cajaIngresosList = cajaIngresosRepository.findAll();
        assertThat(cajaIngresosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCajaIngresos() throws Exception {
        int databaseSizeBeforeUpdate = cajaIngresosRepository.findAll().size();
        cajaIngresos.setId(count.incrementAndGet());

        // Create the CajaIngresos
        CajaIngresosDTO cajaIngresosDTO = cajaIngresosMapper.toDto(cajaIngresos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCajaIngresosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cajaIngresosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CajaIngresos in the database
        List<CajaIngresos> cajaIngresosList = cajaIngresosRepository.findAll();
        assertThat(cajaIngresosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCajaIngresos() throws Exception {
        int databaseSizeBeforeUpdate = cajaIngresosRepository.findAll().size();
        cajaIngresos.setId(count.incrementAndGet());

        // Create the CajaIngresos
        CajaIngresosDTO cajaIngresosDTO = cajaIngresosMapper.toDto(cajaIngresos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCajaIngresosMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cajaIngresosDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CajaIngresos in the database
        List<CajaIngresos> cajaIngresosList = cajaIngresosRepository.findAll();
        assertThat(cajaIngresosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCajaIngresos() throws Exception {
        // Initialize the database
        cajaIngresosRepository.saveAndFlush(cajaIngresos);

        int databaseSizeBeforeDelete = cajaIngresosRepository.findAll().size();

        // Delete the cajaIngresos
        restCajaIngresosMockMvc
            .perform(delete(ENTITY_API_URL_ID, cajaIngresos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CajaIngresos> cajaIngresosList = cajaIngresosRepository.findAll();
        assertThat(cajaIngresosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CajaTattos;
import com.mycompany.myapp.repository.CajaTattosRepository;
import com.mycompany.myapp.service.dto.CajaTattosDTO;
import com.mycompany.myapp.service.mapper.CajaTattosMapper;
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
 * Integration tests for the {@link CajaTattosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CajaTattosResourceIT {

    private static final BigDecimal DEFAULT_VALOR_TATTO_DIA = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_TATTO_DIA = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_REGISTRADO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_REGISTRADO = new BigDecimal(2);

    private static final BigDecimal DEFAULT_DIFERENCIA = new BigDecimal(1);
    private static final BigDecimal UPDATED_DIFERENCIA = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/caja-tattos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CajaTattosRepository cajaTattosRepository;

    @Autowired
    private CajaTattosMapper cajaTattosMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCajaTattosMockMvc;

    private CajaTattos cajaTattos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CajaTattos createEntity(EntityManager em) {
        CajaTattos cajaTattos = new CajaTattos()
            .valorTattoDia(DEFAULT_VALOR_TATTO_DIA)
            .valorRegistrado(DEFAULT_VALOR_REGISTRADO)
            .diferencia(DEFAULT_DIFERENCIA);
        return cajaTattos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CajaTattos createUpdatedEntity(EntityManager em) {
        CajaTattos cajaTattos = new CajaTattos()
            .valorTattoDia(UPDATED_VALOR_TATTO_DIA)
            .valorRegistrado(UPDATED_VALOR_REGISTRADO)
            .diferencia(UPDATED_DIFERENCIA);
        return cajaTattos;
    }

    @BeforeEach
    public void initTest() {
        cajaTattos = createEntity(em);
    }

    @Test
    @Transactional
    void createCajaTattos() throws Exception {
        int databaseSizeBeforeCreate = cajaTattosRepository.findAll().size();
        // Create the CajaTattos
        CajaTattosDTO cajaTattosDTO = cajaTattosMapper.toDto(cajaTattos);
        restCajaTattosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cajaTattosDTO)))
            .andExpect(status().isCreated());

        // Validate the CajaTattos in the database
        List<CajaTattos> cajaTattosList = cajaTattosRepository.findAll();
        assertThat(cajaTattosList).hasSize(databaseSizeBeforeCreate + 1);
        CajaTattos testCajaTattos = cajaTattosList.get(cajaTattosList.size() - 1);
        assertThat(testCajaTattos.getValorTattoDia()).isEqualByComparingTo(DEFAULT_VALOR_TATTO_DIA);
        assertThat(testCajaTattos.getValorRegistrado()).isEqualByComparingTo(DEFAULT_VALOR_REGISTRADO);
        assertThat(testCajaTattos.getDiferencia()).isEqualByComparingTo(DEFAULT_DIFERENCIA);
    }

    @Test
    @Transactional
    void createCajaTattosWithExistingId() throws Exception {
        // Create the CajaTattos with an existing ID
        cajaTattos.setId(1L);
        CajaTattosDTO cajaTattosDTO = cajaTattosMapper.toDto(cajaTattos);

        int databaseSizeBeforeCreate = cajaTattosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCajaTattosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cajaTattosDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CajaTattos in the database
        List<CajaTattos> cajaTattosList = cajaTattosRepository.findAll();
        assertThat(cajaTattosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCajaTattos() throws Exception {
        // Initialize the database
        cajaTattosRepository.saveAndFlush(cajaTattos);

        // Get all the cajaTattosList
        restCajaTattosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cajaTattos.getId().intValue())))
            .andExpect(jsonPath("$.[*].valorTattoDia").value(hasItem(sameNumber(DEFAULT_VALOR_TATTO_DIA))))
            .andExpect(jsonPath("$.[*].valorRegistrado").value(hasItem(sameNumber(DEFAULT_VALOR_REGISTRADO))))
            .andExpect(jsonPath("$.[*].diferencia").value(hasItem(sameNumber(DEFAULT_DIFERENCIA))));
    }

    @Test
    @Transactional
    void getCajaTattos() throws Exception {
        // Initialize the database
        cajaTattosRepository.saveAndFlush(cajaTattos);

        // Get the cajaTattos
        restCajaTattosMockMvc
            .perform(get(ENTITY_API_URL_ID, cajaTattos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cajaTattos.getId().intValue()))
            .andExpect(jsonPath("$.valorTattoDia").value(sameNumber(DEFAULT_VALOR_TATTO_DIA)))
            .andExpect(jsonPath("$.valorRegistrado").value(sameNumber(DEFAULT_VALOR_REGISTRADO)))
            .andExpect(jsonPath("$.diferencia").value(sameNumber(DEFAULT_DIFERENCIA)));
    }

    @Test
    @Transactional
    void getNonExistingCajaTattos() throws Exception {
        // Get the cajaTattos
        restCajaTattosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCajaTattos() throws Exception {
        // Initialize the database
        cajaTattosRepository.saveAndFlush(cajaTattos);

        int databaseSizeBeforeUpdate = cajaTattosRepository.findAll().size();

        // Update the cajaTattos
        CajaTattos updatedCajaTattos = cajaTattosRepository.findById(cajaTattos.getId()).get();
        // Disconnect from session so that the updates on updatedCajaTattos are not directly saved in db
        em.detach(updatedCajaTattos);
        updatedCajaTattos.valorTattoDia(UPDATED_VALOR_TATTO_DIA).valorRegistrado(UPDATED_VALOR_REGISTRADO).diferencia(UPDATED_DIFERENCIA);
        CajaTattosDTO cajaTattosDTO = cajaTattosMapper.toDto(updatedCajaTattos);

        restCajaTattosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cajaTattosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cajaTattosDTO))
            )
            .andExpect(status().isOk());

        // Validate the CajaTattos in the database
        List<CajaTattos> cajaTattosList = cajaTattosRepository.findAll();
        assertThat(cajaTattosList).hasSize(databaseSizeBeforeUpdate);
        CajaTattos testCajaTattos = cajaTattosList.get(cajaTattosList.size() - 1);
        assertThat(testCajaTattos.getValorTattoDia()).isEqualTo(UPDATED_VALOR_TATTO_DIA);
        assertThat(testCajaTattos.getValorRegistrado()).isEqualTo(UPDATED_VALOR_REGISTRADO);
        assertThat(testCajaTattos.getDiferencia()).isEqualTo(UPDATED_DIFERENCIA);
    }

    @Test
    @Transactional
    void putNonExistingCajaTattos() throws Exception {
        int databaseSizeBeforeUpdate = cajaTattosRepository.findAll().size();
        cajaTattos.setId(count.incrementAndGet());

        // Create the CajaTattos
        CajaTattosDTO cajaTattosDTO = cajaTattosMapper.toDto(cajaTattos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCajaTattosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cajaTattosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cajaTattosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CajaTattos in the database
        List<CajaTattos> cajaTattosList = cajaTattosRepository.findAll();
        assertThat(cajaTattosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCajaTattos() throws Exception {
        int databaseSizeBeforeUpdate = cajaTattosRepository.findAll().size();
        cajaTattos.setId(count.incrementAndGet());

        // Create the CajaTattos
        CajaTattosDTO cajaTattosDTO = cajaTattosMapper.toDto(cajaTattos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCajaTattosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cajaTattosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CajaTattos in the database
        List<CajaTattos> cajaTattosList = cajaTattosRepository.findAll();
        assertThat(cajaTattosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCajaTattos() throws Exception {
        int databaseSizeBeforeUpdate = cajaTattosRepository.findAll().size();
        cajaTattos.setId(count.incrementAndGet());

        // Create the CajaTattos
        CajaTattosDTO cajaTattosDTO = cajaTattosMapper.toDto(cajaTattos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCajaTattosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cajaTattosDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CajaTattos in the database
        List<CajaTattos> cajaTattosList = cajaTattosRepository.findAll();
        assertThat(cajaTattosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCajaTattosWithPatch() throws Exception {
        // Initialize the database
        cajaTattosRepository.saveAndFlush(cajaTattos);

        int databaseSizeBeforeUpdate = cajaTattosRepository.findAll().size();

        // Update the cajaTattos using partial update
        CajaTattos partialUpdatedCajaTattos = new CajaTattos();
        partialUpdatedCajaTattos.setId(cajaTattos.getId());

        partialUpdatedCajaTattos.valorTattoDia(UPDATED_VALOR_TATTO_DIA);

        restCajaTattosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCajaTattos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCajaTattos))
            )
            .andExpect(status().isOk());

        // Validate the CajaTattos in the database
        List<CajaTattos> cajaTattosList = cajaTattosRepository.findAll();
        assertThat(cajaTattosList).hasSize(databaseSizeBeforeUpdate);
        CajaTattos testCajaTattos = cajaTattosList.get(cajaTattosList.size() - 1);
        assertThat(testCajaTattos.getValorTattoDia()).isEqualByComparingTo(UPDATED_VALOR_TATTO_DIA);
        assertThat(testCajaTattos.getValorRegistrado()).isEqualByComparingTo(DEFAULT_VALOR_REGISTRADO);
        assertThat(testCajaTattos.getDiferencia()).isEqualByComparingTo(DEFAULT_DIFERENCIA);
    }

    @Test
    @Transactional
    void fullUpdateCajaTattosWithPatch() throws Exception {
        // Initialize the database
        cajaTattosRepository.saveAndFlush(cajaTattos);

        int databaseSizeBeforeUpdate = cajaTattosRepository.findAll().size();

        // Update the cajaTattos using partial update
        CajaTattos partialUpdatedCajaTattos = new CajaTattos();
        partialUpdatedCajaTattos.setId(cajaTattos.getId());

        partialUpdatedCajaTattos
            .valorTattoDia(UPDATED_VALOR_TATTO_DIA)
            .valorRegistrado(UPDATED_VALOR_REGISTRADO)
            .diferencia(UPDATED_DIFERENCIA);

        restCajaTattosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCajaTattos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCajaTattos))
            )
            .andExpect(status().isOk());

        // Validate the CajaTattos in the database
        List<CajaTattos> cajaTattosList = cajaTattosRepository.findAll();
        assertThat(cajaTattosList).hasSize(databaseSizeBeforeUpdate);
        CajaTattos testCajaTattos = cajaTattosList.get(cajaTattosList.size() - 1);
        assertThat(testCajaTattos.getValorTattoDia()).isEqualByComparingTo(UPDATED_VALOR_TATTO_DIA);
        assertThat(testCajaTattos.getValorRegistrado()).isEqualByComparingTo(UPDATED_VALOR_REGISTRADO);
        assertThat(testCajaTattos.getDiferencia()).isEqualByComparingTo(UPDATED_DIFERENCIA);
    }

    @Test
    @Transactional
    void patchNonExistingCajaTattos() throws Exception {
        int databaseSizeBeforeUpdate = cajaTattosRepository.findAll().size();
        cajaTattos.setId(count.incrementAndGet());

        // Create the CajaTattos
        CajaTattosDTO cajaTattosDTO = cajaTattosMapper.toDto(cajaTattos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCajaTattosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cajaTattosDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cajaTattosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CajaTattos in the database
        List<CajaTattos> cajaTattosList = cajaTattosRepository.findAll();
        assertThat(cajaTattosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCajaTattos() throws Exception {
        int databaseSizeBeforeUpdate = cajaTattosRepository.findAll().size();
        cajaTattos.setId(count.incrementAndGet());

        // Create the CajaTattos
        CajaTattosDTO cajaTattosDTO = cajaTattosMapper.toDto(cajaTattos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCajaTattosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cajaTattosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CajaTattos in the database
        List<CajaTattos> cajaTattosList = cajaTattosRepository.findAll();
        assertThat(cajaTattosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCajaTattos() throws Exception {
        int databaseSizeBeforeUpdate = cajaTattosRepository.findAll().size();
        cajaTattos.setId(count.incrementAndGet());

        // Create the CajaTattos
        CajaTattosDTO cajaTattosDTO = cajaTattosMapper.toDto(cajaTattos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCajaTattosMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cajaTattosDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CajaTattos in the database
        List<CajaTattos> cajaTattosList = cajaTattosRepository.findAll();
        assertThat(cajaTattosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCajaTattos() throws Exception {
        // Initialize the database
        cajaTattosRepository.saveAndFlush(cajaTattos);

        int databaseSizeBeforeDelete = cajaTattosRepository.findAll().size();

        // Delete the cajaTattos
        restCajaTattosMockMvc
            .perform(delete(ENTITY_API_URL_ID, cajaTattos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CajaTattos> cajaTattosList = cajaTattosRepository.findAll();
        assertThat(cajaTattosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

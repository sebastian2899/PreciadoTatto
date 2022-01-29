package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Compras;
import com.mycompany.myapp.repository.ComprasRepository;
import com.mycompany.myapp.service.dto.ComprasDTO;
import com.mycompany.myapp.service.mapper.ComprasMapper;
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
 * Integration tests for the {@link ComprasResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ComprasResourceIT {

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_VALOR_COMPRA = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_COMPRA = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_PAGADO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_PAGADO = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_DEUDA = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_DEUDA = new BigDecimal(2);

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/compras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ComprasRepository comprasRepository;

    @Autowired
    private ComprasMapper comprasMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComprasMockMvc;

    private Compras compras;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compras createEntity(EntityManager em) {
        Compras compras = new Compras()
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .valorCompra(DEFAULT_VALOR_COMPRA)
            .valorPagado(DEFAULT_VALOR_PAGADO)
            .valorDeuda(DEFAULT_VALOR_DEUDA)
            .estado(DEFAULT_ESTADO);
        return compras;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compras createUpdatedEntity(EntityManager em) {
        Compras compras = new Compras()
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .valorCompra(UPDATED_VALOR_COMPRA)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .valorDeuda(UPDATED_VALOR_DEUDA)
            .estado(UPDATED_ESTADO);
        return compras;
    }

    @BeforeEach
    public void initTest() {
        compras = createEntity(em);
    }

    @Test
    @Transactional
    void createCompras() throws Exception {
        int databaseSizeBeforeCreate = comprasRepository.findAll().size();
        // Create the Compras
        ComprasDTO comprasDTO = comprasMapper.toDto(compras);
        restComprasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comprasDTO)))
            .andExpect(status().isCreated());

        // Validate the Compras in the database
        List<Compras> comprasList = comprasRepository.findAll();
        assertThat(comprasList).hasSize(databaseSizeBeforeCreate + 1);
        Compras testCompras = comprasList.get(comprasList.size() - 1);
        assertThat(testCompras.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testCompras.getValorCompra()).isEqualByComparingTo(DEFAULT_VALOR_COMPRA);
        assertThat(testCompras.getValorPagado()).isEqualByComparingTo(DEFAULT_VALOR_PAGADO);
        assertThat(testCompras.getValorDeuda()).isEqualByComparingTo(DEFAULT_VALOR_DEUDA);
        assertThat(testCompras.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createComprasWithExistingId() throws Exception {
        // Create the Compras with an existing ID
        compras.setId(1L);
        ComprasDTO comprasDTO = comprasMapper.toDto(compras);

        int databaseSizeBeforeCreate = comprasRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComprasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comprasDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Compras in the database
        List<Compras> comprasList = comprasRepository.findAll();
        assertThat(comprasList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompras() throws Exception {
        // Initialize the database
        comprasRepository.saveAndFlush(compras);

        // Get all the comprasList
        restComprasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compras.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].valorCompra").value(hasItem(sameNumber(DEFAULT_VALOR_COMPRA))))
            .andExpect(jsonPath("$.[*].valorPagado").value(hasItem(sameNumber(DEFAULT_VALOR_PAGADO))))
            .andExpect(jsonPath("$.[*].valorDeuda").value(hasItem(sameNumber(DEFAULT_VALOR_DEUDA))))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)));
    }

    @Test
    @Transactional
    void getCompras() throws Exception {
        // Initialize the database
        comprasRepository.saveAndFlush(compras);

        // Get the compras
        restComprasMockMvc
            .perform(get(ENTITY_API_URL_ID, compras.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compras.getId().intValue()))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.valorCompra").value(sameNumber(DEFAULT_VALOR_COMPRA)))
            .andExpect(jsonPath("$.valorPagado").value(sameNumber(DEFAULT_VALOR_PAGADO)))
            .andExpect(jsonPath("$.valorDeuda").value(sameNumber(DEFAULT_VALOR_DEUDA)))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO));
    }

    @Test
    @Transactional
    void getNonExistingCompras() throws Exception {
        // Get the compras
        restComprasMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCompras() throws Exception {
        // Initialize the database
        comprasRepository.saveAndFlush(compras);

        int databaseSizeBeforeUpdate = comprasRepository.findAll().size();

        // Update the compras
        Compras updatedCompras = comprasRepository.findById(compras.getId()).get();
        // Disconnect from session so that the updates on updatedCompras are not directly saved in db
        em.detach(updatedCompras);
        updatedCompras
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .valorCompra(UPDATED_VALOR_COMPRA)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .valorDeuda(UPDATED_VALOR_DEUDA)
            .estado(UPDATED_ESTADO);
        ComprasDTO comprasDTO = comprasMapper.toDto(updatedCompras);

        restComprasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, comprasDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comprasDTO))
            )
            .andExpect(status().isOk());

        // Validate the Compras in the database
        List<Compras> comprasList = comprasRepository.findAll();
        assertThat(comprasList).hasSize(databaseSizeBeforeUpdate);
        Compras testCompras = comprasList.get(comprasList.size() - 1);
        assertThat(testCompras.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testCompras.getValorCompra()).isEqualTo(UPDATED_VALOR_COMPRA);
        assertThat(testCompras.getValorPagado()).isEqualTo(UPDATED_VALOR_PAGADO);
        assertThat(testCompras.getValorDeuda()).isEqualTo(UPDATED_VALOR_DEUDA);
        assertThat(testCompras.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingCompras() throws Exception {
        int databaseSizeBeforeUpdate = comprasRepository.findAll().size();
        compras.setId(count.incrementAndGet());

        // Create the Compras
        ComprasDTO comprasDTO = comprasMapper.toDto(compras);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComprasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, comprasDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comprasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compras in the database
        List<Compras> comprasList = comprasRepository.findAll();
        assertThat(comprasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompras() throws Exception {
        int databaseSizeBeforeUpdate = comprasRepository.findAll().size();
        compras.setId(count.incrementAndGet());

        // Create the Compras
        ComprasDTO comprasDTO = comprasMapper.toDto(compras);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComprasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comprasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compras in the database
        List<Compras> comprasList = comprasRepository.findAll();
        assertThat(comprasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompras() throws Exception {
        int databaseSizeBeforeUpdate = comprasRepository.findAll().size();
        compras.setId(count.incrementAndGet());

        // Create the Compras
        ComprasDTO comprasDTO = comprasMapper.toDto(compras);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComprasMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comprasDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compras in the database
        List<Compras> comprasList = comprasRepository.findAll();
        assertThat(comprasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateComprasWithPatch() throws Exception {
        // Initialize the database
        comprasRepository.saveAndFlush(compras);

        int databaseSizeBeforeUpdate = comprasRepository.findAll().size();

        // Update the compras using partial update
        Compras partialUpdatedCompras = new Compras();
        partialUpdatedCompras.setId(compras.getId());

        partialUpdatedCompras.estado(UPDATED_ESTADO);

        restComprasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompras.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompras))
            )
            .andExpect(status().isOk());

        // Validate the Compras in the database
        List<Compras> comprasList = comprasRepository.findAll();
        assertThat(comprasList).hasSize(databaseSizeBeforeUpdate);
        Compras testCompras = comprasList.get(comprasList.size() - 1);
        assertThat(testCompras.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testCompras.getValorCompra()).isEqualByComparingTo(DEFAULT_VALOR_COMPRA);
        assertThat(testCompras.getValorPagado()).isEqualByComparingTo(DEFAULT_VALOR_PAGADO);
        assertThat(testCompras.getValorDeuda()).isEqualByComparingTo(DEFAULT_VALOR_DEUDA);
        assertThat(testCompras.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdateComprasWithPatch() throws Exception {
        // Initialize the database
        comprasRepository.saveAndFlush(compras);

        int databaseSizeBeforeUpdate = comprasRepository.findAll().size();

        // Update the compras using partial update
        Compras partialUpdatedCompras = new Compras();
        partialUpdatedCompras.setId(compras.getId());

        partialUpdatedCompras
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .valorCompra(UPDATED_VALOR_COMPRA)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .valorDeuda(UPDATED_VALOR_DEUDA)
            .estado(UPDATED_ESTADO);

        restComprasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompras.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompras))
            )
            .andExpect(status().isOk());

        // Validate the Compras in the database
        List<Compras> comprasList = comprasRepository.findAll();
        assertThat(comprasList).hasSize(databaseSizeBeforeUpdate);
        Compras testCompras = comprasList.get(comprasList.size() - 1);
        assertThat(testCompras.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testCompras.getValorCompra()).isEqualByComparingTo(UPDATED_VALOR_COMPRA);
        assertThat(testCompras.getValorPagado()).isEqualByComparingTo(UPDATED_VALOR_PAGADO);
        assertThat(testCompras.getValorDeuda()).isEqualByComparingTo(UPDATED_VALOR_DEUDA);
        assertThat(testCompras.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingCompras() throws Exception {
        int databaseSizeBeforeUpdate = comprasRepository.findAll().size();
        compras.setId(count.incrementAndGet());

        // Create the Compras
        ComprasDTO comprasDTO = comprasMapper.toDto(compras);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComprasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, comprasDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(comprasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compras in the database
        List<Compras> comprasList = comprasRepository.findAll();
        assertThat(comprasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompras() throws Exception {
        int databaseSizeBeforeUpdate = comprasRepository.findAll().size();
        compras.setId(count.incrementAndGet());

        // Create the Compras
        ComprasDTO comprasDTO = comprasMapper.toDto(compras);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComprasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(comprasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compras in the database
        List<Compras> comprasList = comprasRepository.findAll();
        assertThat(comprasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompras() throws Exception {
        int databaseSizeBeforeUpdate = comprasRepository.findAll().size();
        compras.setId(count.incrementAndGet());

        // Create the Compras
        ComprasDTO comprasDTO = comprasMapper.toDto(compras);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComprasMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(comprasDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compras in the database
        List<Compras> comprasList = comprasRepository.findAll();
        assertThat(comprasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompras() throws Exception {
        // Initialize the database
        comprasRepository.saveAndFlush(compras);

        int databaseSizeBeforeDelete = comprasRepository.findAll().size();

        // Delete the compras
        restComprasMockMvc
            .perform(delete(ENTITY_API_URL_ID, compras.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Compras> comprasList = comprasRepository.findAll();
        assertThat(comprasList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

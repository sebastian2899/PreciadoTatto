package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Ventas;
import com.mycompany.myapp.repository.VentasRepository;
import com.mycompany.myapp.service.dto.VentasDTO;
import com.mycompany.myapp.service.mapper.VentasMapper;
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
 * Integration tests for the {@link VentasResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VentasResourceIT {

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_ID_CLIENTE = 1L;
    private static final Long UPDATED_ID_CLIENTE = 2L;

    private static final BigDecimal DEFAULT_VALOR_VENTA = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_VENTA = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_PAGADO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_PAGADO = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_DEUDA = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_DEUDA = new BigDecimal(2);

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ventas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VentasRepository ventasRepository;

    @Autowired
    private VentasMapper ventasMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVentasMockMvc;

    private Ventas ventas;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ventas createEntity(EntityManager em) {
        Ventas ventas = new Ventas()
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .idCliente(DEFAULT_ID_CLIENTE)
            .valorVenta(DEFAULT_VALOR_VENTA)
            .valorPagado(DEFAULT_VALOR_PAGADO)
            .valorDeuda(DEFAULT_VALOR_DEUDA)
            .estado(DEFAULT_ESTADO);
        return ventas;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ventas createUpdatedEntity(EntityManager em) {
        Ventas ventas = new Ventas()
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .idCliente(UPDATED_ID_CLIENTE)
            .valorVenta(UPDATED_VALOR_VENTA)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .valorDeuda(UPDATED_VALOR_DEUDA)
            .estado(UPDATED_ESTADO);
        return ventas;
    }

    @BeforeEach
    public void initTest() {
        ventas = createEntity(em);
    }

    @Test
    @Transactional
    void createVentas() throws Exception {
        int databaseSizeBeforeCreate = ventasRepository.findAll().size();
        // Create the Ventas
        VentasDTO ventasDTO = ventasMapper.toDto(ventas);
        restVentasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ventasDTO)))
            .andExpect(status().isCreated());

        // Validate the Ventas in the database
        List<Ventas> ventasList = ventasRepository.findAll();
        assertThat(ventasList).hasSize(databaseSizeBeforeCreate + 1);
        Ventas testVentas = ventasList.get(ventasList.size() - 1);
        assertThat(testVentas.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testVentas.getIdCliente()).isEqualTo(DEFAULT_ID_CLIENTE);
        assertThat(testVentas.getValorVenta()).isEqualByComparingTo(DEFAULT_VALOR_VENTA);
        assertThat(testVentas.getValorPagado()).isEqualByComparingTo(DEFAULT_VALOR_PAGADO);
        assertThat(testVentas.getValorDeuda()).isEqualByComparingTo(DEFAULT_VALOR_DEUDA);
        assertThat(testVentas.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createVentasWithExistingId() throws Exception {
        // Create the Ventas with an existing ID
        ventas.setId(1L);
        VentasDTO ventasDTO = ventasMapper.toDto(ventas);

        int databaseSizeBeforeCreate = ventasRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVentasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ventasDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ventas in the database
        List<Ventas> ventasList = ventasRepository.findAll();
        assertThat(ventasList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVentas() throws Exception {
        // Initialize the database
        ventasRepository.saveAndFlush(ventas);

        // Get all the ventasList
        restVentasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ventas.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION)))
            .andExpect(jsonPath("$.[*].idCliente").value(hasItem(DEFAULT_ID_CLIENTE.intValue())))
            .andExpect(jsonPath("$.[*].valorVenta").value(hasItem(sameNumber(DEFAULT_VALOR_VENTA))))
            .andExpect(jsonPath("$.[*].valorPagado").value(hasItem(sameNumber(DEFAULT_VALOR_PAGADO))))
            .andExpect(jsonPath("$.[*].valorDeuda").value(hasItem(sameNumber(DEFAULT_VALOR_DEUDA))))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)));
    }

    @Test
    @Transactional
    void getVentas() throws Exception {
        // Initialize the database
        ventasRepository.saveAndFlush(ventas);

        // Get the ventas
        restVentasMockMvc
            .perform(get(ENTITY_API_URL_ID, ventas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ventas.getId().intValue()))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION))
            .andExpect(jsonPath("$.idCliente").value(DEFAULT_ID_CLIENTE.intValue()))
            .andExpect(jsonPath("$.valorVenta").value(sameNumber(DEFAULT_VALOR_VENTA)))
            .andExpect(jsonPath("$.valorPagado").value(sameNumber(DEFAULT_VALOR_PAGADO)))
            .andExpect(jsonPath("$.valorDeuda").value(sameNumber(DEFAULT_VALOR_DEUDA)))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO));
    }

    @Test
    @Transactional
    void getNonExistingVentas() throws Exception {
        // Get the ventas
        restVentasMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVentas() throws Exception {
        // Initialize the database
        ventasRepository.saveAndFlush(ventas);

        int databaseSizeBeforeUpdate = ventasRepository.findAll().size();

        // Update the ventas
        Ventas updatedVentas = ventasRepository.findById(ventas.getId()).get();
        // Disconnect from session so that the updates on updatedVentas are not directly saved in db
        em.detach(updatedVentas);
        updatedVentas
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .idCliente(UPDATED_ID_CLIENTE)
            .valorVenta(UPDATED_VALOR_VENTA)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .valorDeuda(UPDATED_VALOR_DEUDA)
            .estado(UPDATED_ESTADO);
        VentasDTO ventasDTO = ventasMapper.toDto(updatedVentas);

        restVentasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ventasDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ventasDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ventas in the database
        List<Ventas> ventasList = ventasRepository.findAll();
        assertThat(ventasList).hasSize(databaseSizeBeforeUpdate);
        Ventas testVentas = ventasList.get(ventasList.size() - 1);
        assertThat(testVentas.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testVentas.getIdCliente()).isEqualTo(UPDATED_ID_CLIENTE);
        assertThat(testVentas.getValorVenta()).isEqualTo(UPDATED_VALOR_VENTA);
        assertThat(testVentas.getValorPagado()).isEqualTo(UPDATED_VALOR_PAGADO);
        assertThat(testVentas.getValorDeuda()).isEqualTo(UPDATED_VALOR_DEUDA);
        assertThat(testVentas.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingVentas() throws Exception {
        int databaseSizeBeforeUpdate = ventasRepository.findAll().size();
        ventas.setId(count.incrementAndGet());

        // Create the Ventas
        VentasDTO ventasDTO = ventasMapper.toDto(ventas);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVentasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ventasDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ventasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ventas in the database
        List<Ventas> ventasList = ventasRepository.findAll();
        assertThat(ventasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVentas() throws Exception {
        int databaseSizeBeforeUpdate = ventasRepository.findAll().size();
        ventas.setId(count.incrementAndGet());

        // Create the Ventas
        VentasDTO ventasDTO = ventasMapper.toDto(ventas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ventasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ventas in the database
        List<Ventas> ventasList = ventasRepository.findAll();
        assertThat(ventasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVentas() throws Exception {
        int databaseSizeBeforeUpdate = ventasRepository.findAll().size();
        ventas.setId(count.incrementAndGet());

        // Create the Ventas
        VentasDTO ventasDTO = ventasMapper.toDto(ventas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentasMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ventasDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ventas in the database
        List<Ventas> ventasList = ventasRepository.findAll();
        assertThat(ventasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVentasWithPatch() throws Exception {
        // Initialize the database
        ventasRepository.saveAndFlush(ventas);

        int databaseSizeBeforeUpdate = ventasRepository.findAll().size();

        // Update the ventas using partial update
        Ventas partialUpdatedVentas = new Ventas();
        partialUpdatedVentas.setId(ventas.getId());

        partialUpdatedVentas
            .valorVenta(UPDATED_VALOR_VENTA)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .valorDeuda(UPDATED_VALOR_DEUDA)
            .estado(UPDATED_ESTADO);

        restVentasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVentas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVentas))
            )
            .andExpect(status().isOk());

        // Validate the Ventas in the database
        List<Ventas> ventasList = ventasRepository.findAll();
        assertThat(ventasList).hasSize(databaseSizeBeforeUpdate);
        Ventas testVentas = ventasList.get(ventasList.size() - 1);
        assertThat(testVentas.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testVentas.getIdCliente()).isEqualTo(DEFAULT_ID_CLIENTE);
        assertThat(testVentas.getValorVenta()).isEqualByComparingTo(UPDATED_VALOR_VENTA);
        assertThat(testVentas.getValorPagado()).isEqualByComparingTo(UPDATED_VALOR_PAGADO);
        assertThat(testVentas.getValorDeuda()).isEqualByComparingTo(UPDATED_VALOR_DEUDA);
        assertThat(testVentas.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdateVentasWithPatch() throws Exception {
        // Initialize the database
        ventasRepository.saveAndFlush(ventas);

        int databaseSizeBeforeUpdate = ventasRepository.findAll().size();

        // Update the ventas using partial update
        Ventas partialUpdatedVentas = new Ventas();
        partialUpdatedVentas.setId(ventas.getId());

        partialUpdatedVentas
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .idCliente(UPDATED_ID_CLIENTE)
            .valorVenta(UPDATED_VALOR_VENTA)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .valorDeuda(UPDATED_VALOR_DEUDA)
            .estado(UPDATED_ESTADO);

        restVentasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVentas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVentas))
            )
            .andExpect(status().isOk());

        // Validate the Ventas in the database
        List<Ventas> ventasList = ventasRepository.findAll();
        assertThat(ventasList).hasSize(databaseSizeBeforeUpdate);
        Ventas testVentas = ventasList.get(ventasList.size() - 1);
        assertThat(testVentas.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testVentas.getIdCliente()).isEqualTo(UPDATED_ID_CLIENTE);
        assertThat(testVentas.getValorVenta()).isEqualByComparingTo(UPDATED_VALOR_VENTA);
        assertThat(testVentas.getValorPagado()).isEqualByComparingTo(UPDATED_VALOR_PAGADO);
        assertThat(testVentas.getValorDeuda()).isEqualByComparingTo(UPDATED_VALOR_DEUDA);
        assertThat(testVentas.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingVentas() throws Exception {
        int databaseSizeBeforeUpdate = ventasRepository.findAll().size();
        ventas.setId(count.incrementAndGet());

        // Create the Ventas
        VentasDTO ventasDTO = ventasMapper.toDto(ventas);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVentasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ventasDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ventasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ventas in the database
        List<Ventas> ventasList = ventasRepository.findAll();
        assertThat(ventasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVentas() throws Exception {
        int databaseSizeBeforeUpdate = ventasRepository.findAll().size();
        ventas.setId(count.incrementAndGet());

        // Create the Ventas
        VentasDTO ventasDTO = ventasMapper.toDto(ventas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ventasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ventas in the database
        List<Ventas> ventasList = ventasRepository.findAll();
        assertThat(ventasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVentas() throws Exception {
        int databaseSizeBeforeUpdate = ventasRepository.findAll().size();
        ventas.setId(count.incrementAndGet());

        // Create the Ventas
        VentasDTO ventasDTO = ventasMapper.toDto(ventas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVentasMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ventasDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ventas in the database
        List<Ventas> ventasList = ventasRepository.findAll();
        assertThat(ventasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVentas() throws Exception {
        // Initialize the database
        ventasRepository.saveAndFlush(ventas);

        int databaseSizeBeforeDelete = ventasRepository.findAll().size();

        // Delete the ventas
        restVentasMockMvc
            .perform(delete(ENTITY_API_URL_ID, ventas.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ventas> ventasList = ventasRepository.findAll();
        assertThat(ventasList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

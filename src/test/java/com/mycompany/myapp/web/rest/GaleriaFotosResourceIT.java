package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.GaleriaFotos;
import com.mycompany.myapp.repository.GaleriaFotosRepository;
import com.mycompany.myapp.service.dto.GaleriaFotosDTO;
import com.mycompany.myapp.service.mapper.GaleriaFotosMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link GaleriaFotosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GaleriaFotosResourceIT {

    private static final String DEFAULT_NOMBRE_DISENIO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_DISENIO = "BBBBBBBBBB";

    private static final byte[] DEFAULT_DISENIO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DISENIO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DISENIO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DISENIO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRECIO_DISENIO = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRECIO_DISENIO = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/galeria-fotos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GaleriaFotosRepository galeriaFotosRepository;

    @Autowired
    private GaleriaFotosMapper galeriaFotosMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGaleriaFotosMockMvc;

    private GaleriaFotos galeriaFotos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GaleriaFotos createEntity(EntityManager em) {
        GaleriaFotos galeriaFotos = new GaleriaFotos()
            .nombreDisenio(DEFAULT_NOMBRE_DISENIO)
            .disenio(DEFAULT_DISENIO)
            .disenioContentType(DEFAULT_DISENIO_CONTENT_TYPE)
            .descripcion(DEFAULT_DESCRIPCION)
            .precioDisenio(DEFAULT_PRECIO_DISENIO);
        return galeriaFotos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GaleriaFotos createUpdatedEntity(EntityManager em) {
        GaleriaFotos galeriaFotos = new GaleriaFotos()
            .nombreDisenio(UPDATED_NOMBRE_DISENIO)
            .disenio(UPDATED_DISENIO)
            .disenioContentType(UPDATED_DISENIO_CONTENT_TYPE)
            .descripcion(UPDATED_DESCRIPCION)
            .precioDisenio(UPDATED_PRECIO_DISENIO);
        return galeriaFotos;
    }

    @BeforeEach
    public void initTest() {
        galeriaFotos = createEntity(em);
    }

    @Test
    @Transactional
    void createGaleriaFotos() throws Exception {
        int databaseSizeBeforeCreate = galeriaFotosRepository.findAll().size();
        // Create the GaleriaFotos
        GaleriaFotosDTO galeriaFotosDTO = galeriaFotosMapper.toDto(galeriaFotos);
        restGaleriaFotosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(galeriaFotosDTO))
            )
            .andExpect(status().isCreated());

        // Validate the GaleriaFotos in the database
        List<GaleriaFotos> galeriaFotosList = galeriaFotosRepository.findAll();
        assertThat(galeriaFotosList).hasSize(databaseSizeBeforeCreate + 1);
        GaleriaFotos testGaleriaFotos = galeriaFotosList.get(galeriaFotosList.size() - 1);
        assertThat(testGaleriaFotos.getNombreDisenio()).isEqualTo(DEFAULT_NOMBRE_DISENIO);
        assertThat(testGaleriaFotos.getDisenio()).isEqualTo(DEFAULT_DISENIO);
        assertThat(testGaleriaFotos.getDisenioContentType()).isEqualTo(DEFAULT_DISENIO_CONTENT_TYPE);
        assertThat(testGaleriaFotos.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testGaleriaFotos.getPrecioDisenio()).isEqualByComparingTo(DEFAULT_PRECIO_DISENIO);
    }

    @Test
    @Transactional
    void createGaleriaFotosWithExistingId() throws Exception {
        // Create the GaleriaFotos with an existing ID
        galeriaFotos.setId(1L);
        GaleriaFotosDTO galeriaFotosDTO = galeriaFotosMapper.toDto(galeriaFotos);

        int databaseSizeBeforeCreate = galeriaFotosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGaleriaFotosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(galeriaFotosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GaleriaFotos in the database
        List<GaleriaFotos> galeriaFotosList = galeriaFotosRepository.findAll();
        assertThat(galeriaFotosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGaleriaFotos() throws Exception {
        // Initialize the database
        galeriaFotosRepository.saveAndFlush(galeriaFotos);

        // Get all the galeriaFotosList
        restGaleriaFotosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(galeriaFotos.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreDisenio").value(hasItem(DEFAULT_NOMBRE_DISENIO)))
            .andExpect(jsonPath("$.[*].disenioContentType").value(hasItem(DEFAULT_DISENIO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].disenio").value(hasItem(Base64Utils.encodeToString(DEFAULT_DISENIO))))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].precioDisenio").value(hasItem(sameNumber(DEFAULT_PRECIO_DISENIO))));
    }

    @Test
    @Transactional
    void getGaleriaFotos() throws Exception {
        // Initialize the database
        galeriaFotosRepository.saveAndFlush(galeriaFotos);

        // Get the galeriaFotos
        restGaleriaFotosMockMvc
            .perform(get(ENTITY_API_URL_ID, galeriaFotos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(galeriaFotos.getId().intValue()))
            .andExpect(jsonPath("$.nombreDisenio").value(DEFAULT_NOMBRE_DISENIO))
            .andExpect(jsonPath("$.disenioContentType").value(DEFAULT_DISENIO_CONTENT_TYPE))
            .andExpect(jsonPath("$.disenio").value(Base64Utils.encodeToString(DEFAULT_DISENIO)))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.precioDisenio").value(sameNumber(DEFAULT_PRECIO_DISENIO)));
    }

    @Test
    @Transactional
    void getNonExistingGaleriaFotos() throws Exception {
        // Get the galeriaFotos
        restGaleriaFotosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGaleriaFotos() throws Exception {
        // Initialize the database
        galeriaFotosRepository.saveAndFlush(galeriaFotos);

        int databaseSizeBeforeUpdate = galeriaFotosRepository.findAll().size();

        // Update the galeriaFotos
        GaleriaFotos updatedGaleriaFotos = galeriaFotosRepository.findById(galeriaFotos.getId()).get();
        // Disconnect from session so that the updates on updatedGaleriaFotos are not directly saved in db
        em.detach(updatedGaleriaFotos);
        updatedGaleriaFotos
            .nombreDisenio(UPDATED_NOMBRE_DISENIO)
            .disenio(UPDATED_DISENIO)
            .disenioContentType(UPDATED_DISENIO_CONTENT_TYPE)
            .descripcion(UPDATED_DESCRIPCION)
            .precioDisenio(UPDATED_PRECIO_DISENIO);
        GaleriaFotosDTO galeriaFotosDTO = galeriaFotosMapper.toDto(updatedGaleriaFotos);

        restGaleriaFotosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, galeriaFotosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(galeriaFotosDTO))
            )
            .andExpect(status().isOk());

        // Validate the GaleriaFotos in the database
        List<GaleriaFotos> galeriaFotosList = galeriaFotosRepository.findAll();
        assertThat(galeriaFotosList).hasSize(databaseSizeBeforeUpdate);
        GaleriaFotos testGaleriaFotos = galeriaFotosList.get(galeriaFotosList.size() - 1);
        assertThat(testGaleriaFotos.getNombreDisenio()).isEqualTo(UPDATED_NOMBRE_DISENIO);
        assertThat(testGaleriaFotos.getDisenio()).isEqualTo(UPDATED_DISENIO);
        assertThat(testGaleriaFotos.getDisenioContentType()).isEqualTo(UPDATED_DISENIO_CONTENT_TYPE);
        assertThat(testGaleriaFotos.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testGaleriaFotos.getPrecioDisenio()).isEqualTo(UPDATED_PRECIO_DISENIO);
    }

    @Test
    @Transactional
    void putNonExistingGaleriaFotos() throws Exception {
        int databaseSizeBeforeUpdate = galeriaFotosRepository.findAll().size();
        galeriaFotos.setId(count.incrementAndGet());

        // Create the GaleriaFotos
        GaleriaFotosDTO galeriaFotosDTO = galeriaFotosMapper.toDto(galeriaFotos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGaleriaFotosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, galeriaFotosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(galeriaFotosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GaleriaFotos in the database
        List<GaleriaFotos> galeriaFotosList = galeriaFotosRepository.findAll();
        assertThat(galeriaFotosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGaleriaFotos() throws Exception {
        int databaseSizeBeforeUpdate = galeriaFotosRepository.findAll().size();
        galeriaFotos.setId(count.incrementAndGet());

        // Create the GaleriaFotos
        GaleriaFotosDTO galeriaFotosDTO = galeriaFotosMapper.toDto(galeriaFotos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGaleriaFotosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(galeriaFotosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GaleriaFotos in the database
        List<GaleriaFotos> galeriaFotosList = galeriaFotosRepository.findAll();
        assertThat(galeriaFotosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGaleriaFotos() throws Exception {
        int databaseSizeBeforeUpdate = galeriaFotosRepository.findAll().size();
        galeriaFotos.setId(count.incrementAndGet());

        // Create the GaleriaFotos
        GaleriaFotosDTO galeriaFotosDTO = galeriaFotosMapper.toDto(galeriaFotos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGaleriaFotosMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(galeriaFotosDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GaleriaFotos in the database
        List<GaleriaFotos> galeriaFotosList = galeriaFotosRepository.findAll();
        assertThat(galeriaFotosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGaleriaFotosWithPatch() throws Exception {
        // Initialize the database
        galeriaFotosRepository.saveAndFlush(galeriaFotos);

        int databaseSizeBeforeUpdate = galeriaFotosRepository.findAll().size();

        // Update the galeriaFotos using partial update
        GaleriaFotos partialUpdatedGaleriaFotos = new GaleriaFotos();
        partialUpdatedGaleriaFotos.setId(galeriaFotos.getId());

        partialUpdatedGaleriaFotos
            .nombreDisenio(UPDATED_NOMBRE_DISENIO)
            .disenio(UPDATED_DISENIO)
            .disenioContentType(UPDATED_DISENIO_CONTENT_TYPE)
            .descripcion(UPDATED_DESCRIPCION)
            .precioDisenio(UPDATED_PRECIO_DISENIO);

        restGaleriaFotosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGaleriaFotos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGaleriaFotos))
            )
            .andExpect(status().isOk());

        // Validate the GaleriaFotos in the database
        List<GaleriaFotos> galeriaFotosList = galeriaFotosRepository.findAll();
        assertThat(galeriaFotosList).hasSize(databaseSizeBeforeUpdate);
        GaleriaFotos testGaleriaFotos = galeriaFotosList.get(galeriaFotosList.size() - 1);
        assertThat(testGaleriaFotos.getNombreDisenio()).isEqualTo(UPDATED_NOMBRE_DISENIO);
        assertThat(testGaleriaFotos.getDisenio()).isEqualTo(UPDATED_DISENIO);
        assertThat(testGaleriaFotos.getDisenioContentType()).isEqualTo(UPDATED_DISENIO_CONTENT_TYPE);
        assertThat(testGaleriaFotos.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testGaleriaFotos.getPrecioDisenio()).isEqualByComparingTo(UPDATED_PRECIO_DISENIO);
    }

    @Test
    @Transactional
    void fullUpdateGaleriaFotosWithPatch() throws Exception {
        // Initialize the database
        galeriaFotosRepository.saveAndFlush(galeriaFotos);

        int databaseSizeBeforeUpdate = galeriaFotosRepository.findAll().size();

        // Update the galeriaFotos using partial update
        GaleriaFotos partialUpdatedGaleriaFotos = new GaleriaFotos();
        partialUpdatedGaleriaFotos.setId(galeriaFotos.getId());

        partialUpdatedGaleriaFotos
            .nombreDisenio(UPDATED_NOMBRE_DISENIO)
            .disenio(UPDATED_DISENIO)
            .disenioContentType(UPDATED_DISENIO_CONTENT_TYPE)
            .descripcion(UPDATED_DESCRIPCION)
            .precioDisenio(UPDATED_PRECIO_DISENIO);

        restGaleriaFotosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGaleriaFotos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGaleriaFotos))
            )
            .andExpect(status().isOk());

        // Validate the GaleriaFotos in the database
        List<GaleriaFotos> galeriaFotosList = galeriaFotosRepository.findAll();
        assertThat(galeriaFotosList).hasSize(databaseSizeBeforeUpdate);
        GaleriaFotos testGaleriaFotos = galeriaFotosList.get(galeriaFotosList.size() - 1);
        assertThat(testGaleriaFotos.getNombreDisenio()).isEqualTo(UPDATED_NOMBRE_DISENIO);
        assertThat(testGaleriaFotos.getDisenio()).isEqualTo(UPDATED_DISENIO);
        assertThat(testGaleriaFotos.getDisenioContentType()).isEqualTo(UPDATED_DISENIO_CONTENT_TYPE);
        assertThat(testGaleriaFotos.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testGaleriaFotos.getPrecioDisenio()).isEqualByComparingTo(UPDATED_PRECIO_DISENIO);
    }

    @Test
    @Transactional
    void patchNonExistingGaleriaFotos() throws Exception {
        int databaseSizeBeforeUpdate = galeriaFotosRepository.findAll().size();
        galeriaFotos.setId(count.incrementAndGet());

        // Create the GaleriaFotos
        GaleriaFotosDTO galeriaFotosDTO = galeriaFotosMapper.toDto(galeriaFotos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGaleriaFotosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, galeriaFotosDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(galeriaFotosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GaleriaFotos in the database
        List<GaleriaFotos> galeriaFotosList = galeriaFotosRepository.findAll();
        assertThat(galeriaFotosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGaleriaFotos() throws Exception {
        int databaseSizeBeforeUpdate = galeriaFotosRepository.findAll().size();
        galeriaFotos.setId(count.incrementAndGet());

        // Create the GaleriaFotos
        GaleriaFotosDTO galeriaFotosDTO = galeriaFotosMapper.toDto(galeriaFotos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGaleriaFotosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(galeriaFotosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GaleriaFotos in the database
        List<GaleriaFotos> galeriaFotosList = galeriaFotosRepository.findAll();
        assertThat(galeriaFotosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGaleriaFotos() throws Exception {
        int databaseSizeBeforeUpdate = galeriaFotosRepository.findAll().size();
        galeriaFotos.setId(count.incrementAndGet());

        // Create the GaleriaFotos
        GaleriaFotosDTO galeriaFotosDTO = galeriaFotosMapper.toDto(galeriaFotos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGaleriaFotosMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(galeriaFotosDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GaleriaFotos in the database
        List<GaleriaFotos> galeriaFotosList = galeriaFotosRepository.findAll();
        assertThat(galeriaFotosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGaleriaFotos() throws Exception {
        // Initialize the database
        galeriaFotosRepository.saveAndFlush(galeriaFotos);

        int databaseSizeBeforeDelete = galeriaFotosRepository.findAll().size();

        // Delete the galeriaFotos
        restGaleriaFotosMockMvc
            .perform(delete(ENTITY_API_URL_ID, galeriaFotos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GaleriaFotos> galeriaFotosList = galeriaFotosRepository.findAll();
        assertThat(galeriaFotosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

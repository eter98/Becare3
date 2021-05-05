package com.be4tech.becare3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.Farmaceutica;
import com.be4tech.becare3.repository.FarmaceuticaRepository;
import com.be4tech.becare3.service.criteria.FarmaceuticaCriteria;
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
 * Integration tests for the {@link FarmaceuticaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FarmaceuticaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_PROPIETARIO = "AAAAAAAAAA";
    private static final String UPDATED_PROPIETARIO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/farmaceuticas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FarmaceuticaRepository farmaceuticaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFarmaceuticaMockMvc;

    private Farmaceutica farmaceutica;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Farmaceutica createEntity(EntityManager em) {
        Farmaceutica farmaceutica = new Farmaceutica().nombre(DEFAULT_NOMBRE).direccion(DEFAULT_DIRECCION).propietario(DEFAULT_PROPIETARIO);
        return farmaceutica;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Farmaceutica createUpdatedEntity(EntityManager em) {
        Farmaceutica farmaceutica = new Farmaceutica().nombre(UPDATED_NOMBRE).direccion(UPDATED_DIRECCION).propietario(UPDATED_PROPIETARIO);
        return farmaceutica;
    }

    @BeforeEach
    public void initTest() {
        farmaceutica = createEntity(em);
    }

    @Test
    @Transactional
    void createFarmaceutica() throws Exception {
        int databaseSizeBeforeCreate = farmaceuticaRepository.findAll().size();
        // Create the Farmaceutica
        restFarmaceuticaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(farmaceutica))
            )
            .andExpect(status().isCreated());

        // Validate the Farmaceutica in the database
        List<Farmaceutica> farmaceuticaList = farmaceuticaRepository.findAll();
        assertThat(farmaceuticaList).hasSize(databaseSizeBeforeCreate + 1);
        Farmaceutica testFarmaceutica = farmaceuticaList.get(farmaceuticaList.size() - 1);
        assertThat(testFarmaceutica.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testFarmaceutica.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testFarmaceutica.getPropietario()).isEqualTo(DEFAULT_PROPIETARIO);
    }

    @Test
    @Transactional
    void createFarmaceuticaWithExistingId() throws Exception {
        // Create the Farmaceutica with an existing ID
        farmaceutica.setId(1L);

        int databaseSizeBeforeCreate = farmaceuticaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFarmaceuticaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(farmaceutica))
            )
            .andExpect(status().isBadRequest());

        // Validate the Farmaceutica in the database
        List<Farmaceutica> farmaceuticaList = farmaceuticaRepository.findAll();
        assertThat(farmaceuticaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFarmaceuticas() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        // Get all the farmaceuticaList
        restFarmaceuticaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(farmaceutica.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].propietario").value(hasItem(DEFAULT_PROPIETARIO)));
    }

    @Test
    @Transactional
    void getFarmaceutica() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        // Get the farmaceutica
        restFarmaceuticaMockMvc
            .perform(get(ENTITY_API_URL_ID, farmaceutica.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(farmaceutica.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.propietario").value(DEFAULT_PROPIETARIO));
    }

    @Test
    @Transactional
    void getFarmaceuticasByIdFiltering() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        Long id = farmaceutica.getId();

        defaultFarmaceuticaShouldBeFound("id.equals=" + id);
        defaultFarmaceuticaShouldNotBeFound("id.notEquals=" + id);

        defaultFarmaceuticaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFarmaceuticaShouldNotBeFound("id.greaterThan=" + id);

        defaultFarmaceuticaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFarmaceuticaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFarmaceuticasByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        // Get all the farmaceuticaList where nombre equals to DEFAULT_NOMBRE
        defaultFarmaceuticaShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the farmaceuticaList where nombre equals to UPDATED_NOMBRE
        defaultFarmaceuticaShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllFarmaceuticasByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        // Get all the farmaceuticaList where nombre not equals to DEFAULT_NOMBRE
        defaultFarmaceuticaShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the farmaceuticaList where nombre not equals to UPDATED_NOMBRE
        defaultFarmaceuticaShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllFarmaceuticasByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        // Get all the farmaceuticaList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultFarmaceuticaShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the farmaceuticaList where nombre equals to UPDATED_NOMBRE
        defaultFarmaceuticaShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllFarmaceuticasByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        // Get all the farmaceuticaList where nombre is not null
        defaultFarmaceuticaShouldBeFound("nombre.specified=true");

        // Get all the farmaceuticaList where nombre is null
        defaultFarmaceuticaShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmaceuticasByNombreContainsSomething() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        // Get all the farmaceuticaList where nombre contains DEFAULT_NOMBRE
        defaultFarmaceuticaShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the farmaceuticaList where nombre contains UPDATED_NOMBRE
        defaultFarmaceuticaShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllFarmaceuticasByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        // Get all the farmaceuticaList where nombre does not contain DEFAULT_NOMBRE
        defaultFarmaceuticaShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the farmaceuticaList where nombre does not contain UPDATED_NOMBRE
        defaultFarmaceuticaShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllFarmaceuticasByDireccionIsEqualToSomething() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        // Get all the farmaceuticaList where direccion equals to DEFAULT_DIRECCION
        defaultFarmaceuticaShouldBeFound("direccion.equals=" + DEFAULT_DIRECCION);

        // Get all the farmaceuticaList where direccion equals to UPDATED_DIRECCION
        defaultFarmaceuticaShouldNotBeFound("direccion.equals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllFarmaceuticasByDireccionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        // Get all the farmaceuticaList where direccion not equals to DEFAULT_DIRECCION
        defaultFarmaceuticaShouldNotBeFound("direccion.notEquals=" + DEFAULT_DIRECCION);

        // Get all the farmaceuticaList where direccion not equals to UPDATED_DIRECCION
        defaultFarmaceuticaShouldBeFound("direccion.notEquals=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllFarmaceuticasByDireccionIsInShouldWork() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        // Get all the farmaceuticaList where direccion in DEFAULT_DIRECCION or UPDATED_DIRECCION
        defaultFarmaceuticaShouldBeFound("direccion.in=" + DEFAULT_DIRECCION + "," + UPDATED_DIRECCION);

        // Get all the farmaceuticaList where direccion equals to UPDATED_DIRECCION
        defaultFarmaceuticaShouldNotBeFound("direccion.in=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllFarmaceuticasByDireccionIsNullOrNotNull() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        // Get all the farmaceuticaList where direccion is not null
        defaultFarmaceuticaShouldBeFound("direccion.specified=true");

        // Get all the farmaceuticaList where direccion is null
        defaultFarmaceuticaShouldNotBeFound("direccion.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmaceuticasByDireccionContainsSomething() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        // Get all the farmaceuticaList where direccion contains DEFAULT_DIRECCION
        defaultFarmaceuticaShouldBeFound("direccion.contains=" + DEFAULT_DIRECCION);

        // Get all the farmaceuticaList where direccion contains UPDATED_DIRECCION
        defaultFarmaceuticaShouldNotBeFound("direccion.contains=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllFarmaceuticasByDireccionNotContainsSomething() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        // Get all the farmaceuticaList where direccion does not contain DEFAULT_DIRECCION
        defaultFarmaceuticaShouldNotBeFound("direccion.doesNotContain=" + DEFAULT_DIRECCION);

        // Get all the farmaceuticaList where direccion does not contain UPDATED_DIRECCION
        defaultFarmaceuticaShouldBeFound("direccion.doesNotContain=" + UPDATED_DIRECCION);
    }

    @Test
    @Transactional
    void getAllFarmaceuticasByPropietarioIsEqualToSomething() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        // Get all the farmaceuticaList where propietario equals to DEFAULT_PROPIETARIO
        defaultFarmaceuticaShouldBeFound("propietario.equals=" + DEFAULT_PROPIETARIO);

        // Get all the farmaceuticaList where propietario equals to UPDATED_PROPIETARIO
        defaultFarmaceuticaShouldNotBeFound("propietario.equals=" + UPDATED_PROPIETARIO);
    }

    @Test
    @Transactional
    void getAllFarmaceuticasByPropietarioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        // Get all the farmaceuticaList where propietario not equals to DEFAULT_PROPIETARIO
        defaultFarmaceuticaShouldNotBeFound("propietario.notEquals=" + DEFAULT_PROPIETARIO);

        // Get all the farmaceuticaList where propietario not equals to UPDATED_PROPIETARIO
        defaultFarmaceuticaShouldBeFound("propietario.notEquals=" + UPDATED_PROPIETARIO);
    }

    @Test
    @Transactional
    void getAllFarmaceuticasByPropietarioIsInShouldWork() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        // Get all the farmaceuticaList where propietario in DEFAULT_PROPIETARIO or UPDATED_PROPIETARIO
        defaultFarmaceuticaShouldBeFound("propietario.in=" + DEFAULT_PROPIETARIO + "," + UPDATED_PROPIETARIO);

        // Get all the farmaceuticaList where propietario equals to UPDATED_PROPIETARIO
        defaultFarmaceuticaShouldNotBeFound("propietario.in=" + UPDATED_PROPIETARIO);
    }

    @Test
    @Transactional
    void getAllFarmaceuticasByPropietarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        // Get all the farmaceuticaList where propietario is not null
        defaultFarmaceuticaShouldBeFound("propietario.specified=true");

        // Get all the farmaceuticaList where propietario is null
        defaultFarmaceuticaShouldNotBeFound("propietario.specified=false");
    }

    @Test
    @Transactional
    void getAllFarmaceuticasByPropietarioContainsSomething() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        // Get all the farmaceuticaList where propietario contains DEFAULT_PROPIETARIO
        defaultFarmaceuticaShouldBeFound("propietario.contains=" + DEFAULT_PROPIETARIO);

        // Get all the farmaceuticaList where propietario contains UPDATED_PROPIETARIO
        defaultFarmaceuticaShouldNotBeFound("propietario.contains=" + UPDATED_PROPIETARIO);
    }

    @Test
    @Transactional
    void getAllFarmaceuticasByPropietarioNotContainsSomething() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        // Get all the farmaceuticaList where propietario does not contain DEFAULT_PROPIETARIO
        defaultFarmaceuticaShouldNotBeFound("propietario.doesNotContain=" + DEFAULT_PROPIETARIO);

        // Get all the farmaceuticaList where propietario does not contain UPDATED_PROPIETARIO
        defaultFarmaceuticaShouldBeFound("propietario.doesNotContain=" + UPDATED_PROPIETARIO);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFarmaceuticaShouldBeFound(String filter) throws Exception {
        restFarmaceuticaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(farmaceutica.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].propietario").value(hasItem(DEFAULT_PROPIETARIO)));

        // Check, that the count call also returns 1
        restFarmaceuticaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFarmaceuticaShouldNotBeFound(String filter) throws Exception {
        restFarmaceuticaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFarmaceuticaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFarmaceutica() throws Exception {
        // Get the farmaceutica
        restFarmaceuticaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFarmaceutica() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        int databaseSizeBeforeUpdate = farmaceuticaRepository.findAll().size();

        // Update the farmaceutica
        Farmaceutica updatedFarmaceutica = farmaceuticaRepository.findById(farmaceutica.getId()).get();
        // Disconnect from session so that the updates on updatedFarmaceutica are not directly saved in db
        em.detach(updatedFarmaceutica);
        updatedFarmaceutica.nombre(UPDATED_NOMBRE).direccion(UPDATED_DIRECCION).propietario(UPDATED_PROPIETARIO);

        restFarmaceuticaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFarmaceutica.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFarmaceutica))
            )
            .andExpect(status().isOk());

        // Validate the Farmaceutica in the database
        List<Farmaceutica> farmaceuticaList = farmaceuticaRepository.findAll();
        assertThat(farmaceuticaList).hasSize(databaseSizeBeforeUpdate);
        Farmaceutica testFarmaceutica = farmaceuticaList.get(farmaceuticaList.size() - 1);
        assertThat(testFarmaceutica.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testFarmaceutica.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testFarmaceutica.getPropietario()).isEqualTo(UPDATED_PROPIETARIO);
    }

    @Test
    @Transactional
    void putNonExistingFarmaceutica() throws Exception {
        int databaseSizeBeforeUpdate = farmaceuticaRepository.findAll().size();
        farmaceutica.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFarmaceuticaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, farmaceutica.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(farmaceutica))
            )
            .andExpect(status().isBadRequest());

        // Validate the Farmaceutica in the database
        List<Farmaceutica> farmaceuticaList = farmaceuticaRepository.findAll();
        assertThat(farmaceuticaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFarmaceutica() throws Exception {
        int databaseSizeBeforeUpdate = farmaceuticaRepository.findAll().size();
        farmaceutica.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmaceuticaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(farmaceutica))
            )
            .andExpect(status().isBadRequest());

        // Validate the Farmaceutica in the database
        List<Farmaceutica> farmaceuticaList = farmaceuticaRepository.findAll();
        assertThat(farmaceuticaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFarmaceutica() throws Exception {
        int databaseSizeBeforeUpdate = farmaceuticaRepository.findAll().size();
        farmaceutica.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmaceuticaMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(farmaceutica))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Farmaceutica in the database
        List<Farmaceutica> farmaceuticaList = farmaceuticaRepository.findAll();
        assertThat(farmaceuticaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFarmaceuticaWithPatch() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        int databaseSizeBeforeUpdate = farmaceuticaRepository.findAll().size();

        // Update the farmaceutica using partial update
        Farmaceutica partialUpdatedFarmaceutica = new Farmaceutica();
        partialUpdatedFarmaceutica.setId(farmaceutica.getId());

        partialUpdatedFarmaceutica.nombre(UPDATED_NOMBRE).propietario(UPDATED_PROPIETARIO);

        restFarmaceuticaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFarmaceutica.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFarmaceutica))
            )
            .andExpect(status().isOk());

        // Validate the Farmaceutica in the database
        List<Farmaceutica> farmaceuticaList = farmaceuticaRepository.findAll();
        assertThat(farmaceuticaList).hasSize(databaseSizeBeforeUpdate);
        Farmaceutica testFarmaceutica = farmaceuticaList.get(farmaceuticaList.size() - 1);
        assertThat(testFarmaceutica.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testFarmaceutica.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testFarmaceutica.getPropietario()).isEqualTo(UPDATED_PROPIETARIO);
    }

    @Test
    @Transactional
    void fullUpdateFarmaceuticaWithPatch() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        int databaseSizeBeforeUpdate = farmaceuticaRepository.findAll().size();

        // Update the farmaceutica using partial update
        Farmaceutica partialUpdatedFarmaceutica = new Farmaceutica();
        partialUpdatedFarmaceutica.setId(farmaceutica.getId());

        partialUpdatedFarmaceutica.nombre(UPDATED_NOMBRE).direccion(UPDATED_DIRECCION).propietario(UPDATED_PROPIETARIO);

        restFarmaceuticaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFarmaceutica.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFarmaceutica))
            )
            .andExpect(status().isOk());

        // Validate the Farmaceutica in the database
        List<Farmaceutica> farmaceuticaList = farmaceuticaRepository.findAll();
        assertThat(farmaceuticaList).hasSize(databaseSizeBeforeUpdate);
        Farmaceutica testFarmaceutica = farmaceuticaList.get(farmaceuticaList.size() - 1);
        assertThat(testFarmaceutica.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testFarmaceutica.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testFarmaceutica.getPropietario()).isEqualTo(UPDATED_PROPIETARIO);
    }

    @Test
    @Transactional
    void patchNonExistingFarmaceutica() throws Exception {
        int databaseSizeBeforeUpdate = farmaceuticaRepository.findAll().size();
        farmaceutica.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFarmaceuticaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, farmaceutica.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(farmaceutica))
            )
            .andExpect(status().isBadRequest());

        // Validate the Farmaceutica in the database
        List<Farmaceutica> farmaceuticaList = farmaceuticaRepository.findAll();
        assertThat(farmaceuticaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFarmaceutica() throws Exception {
        int databaseSizeBeforeUpdate = farmaceuticaRepository.findAll().size();
        farmaceutica.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmaceuticaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(farmaceutica))
            )
            .andExpect(status().isBadRequest());

        // Validate the Farmaceutica in the database
        List<Farmaceutica> farmaceuticaList = farmaceuticaRepository.findAll();
        assertThat(farmaceuticaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFarmaceutica() throws Exception {
        int databaseSizeBeforeUpdate = farmaceuticaRepository.findAll().size();
        farmaceutica.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFarmaceuticaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(farmaceutica))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Farmaceutica in the database
        List<Farmaceutica> farmaceuticaList = farmaceuticaRepository.findAll();
        assertThat(farmaceuticaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFarmaceutica() throws Exception {
        // Initialize the database
        farmaceuticaRepository.saveAndFlush(farmaceutica);

        int databaseSizeBeforeDelete = farmaceuticaRepository.findAll().size();

        // Delete the farmaceutica
        restFarmaceuticaMockMvc
            .perform(delete(ENTITY_API_URL_ID, farmaceutica.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Farmaceutica> farmaceuticaList = farmaceuticaRepository.findAll();
        assertThat(farmaceuticaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

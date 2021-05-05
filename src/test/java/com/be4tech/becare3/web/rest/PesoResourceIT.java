package com.be4tech.becare3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.Peso;
import com.be4tech.becare3.domain.User;
import com.be4tech.becare3.repository.PesoRepository;
import com.be4tech.becare3.service.criteria.PesoCriteria;
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
 * Integration tests for the {@link PesoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PesoResourceIT {

    private static final Integer DEFAULT_PESO_KG = 1;
    private static final Integer UPDATED_PESO_KG = 2;
    private static final Integer SMALLER_PESO_KG = 1 - 1;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/pesos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PesoRepository pesoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPesoMockMvc;

    private Peso peso;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Peso createEntity(EntityManager em) {
        Peso peso = new Peso().pesoKG(DEFAULT_PESO_KG).descripcion(DEFAULT_DESCRIPCION).fechaRegistro(DEFAULT_FECHA_REGISTRO);
        return peso;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Peso createUpdatedEntity(EntityManager em) {
        Peso peso = new Peso().pesoKG(UPDATED_PESO_KG).descripcion(UPDATED_DESCRIPCION).fechaRegistro(UPDATED_FECHA_REGISTRO);
        return peso;
    }

    @BeforeEach
    public void initTest() {
        peso = createEntity(em);
    }

    @Test
    @Transactional
    void createPeso() throws Exception {
        int databaseSizeBeforeCreate = pesoRepository.findAll().size();
        // Create the Peso
        restPesoMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(peso))
            )
            .andExpect(status().isCreated());

        // Validate the Peso in the database
        List<Peso> pesoList = pesoRepository.findAll();
        assertThat(pesoList).hasSize(databaseSizeBeforeCreate + 1);
        Peso testPeso = pesoList.get(pesoList.size() - 1);
        assertThat(testPeso.getPesoKG()).isEqualTo(DEFAULT_PESO_KG);
        assertThat(testPeso.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testPeso.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void createPesoWithExistingId() throws Exception {
        // Create the Peso with an existing ID
        peso.setId(1L);

        int databaseSizeBeforeCreate = pesoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPesoMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(peso))
            )
            .andExpect(status().isBadRequest());

        // Validate the Peso in the database
        List<Peso> pesoList = pesoRepository.findAll();
        assertThat(pesoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPesos() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        // Get all the pesoList
        restPesoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(peso.getId().intValue())))
            .andExpect(jsonPath("$.[*].pesoKG").value(hasItem(DEFAULT_PESO_KG)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));
    }

    @Test
    @Transactional
    void getPeso() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        // Get the peso
        restPesoMockMvc
            .perform(get(ENTITY_API_URL_ID, peso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(peso.getId().intValue()))
            .andExpect(jsonPath("$.pesoKG").value(DEFAULT_PESO_KG))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()));
    }

    @Test
    @Transactional
    void getPesosByIdFiltering() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        Long id = peso.getId();

        defaultPesoShouldBeFound("id.equals=" + id);
        defaultPesoShouldNotBeFound("id.notEquals=" + id);

        defaultPesoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPesoShouldNotBeFound("id.greaterThan=" + id);

        defaultPesoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPesoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPesosByPesoKGIsEqualToSomething() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        // Get all the pesoList where pesoKG equals to DEFAULT_PESO_KG
        defaultPesoShouldBeFound("pesoKG.equals=" + DEFAULT_PESO_KG);

        // Get all the pesoList where pesoKG equals to UPDATED_PESO_KG
        defaultPesoShouldNotBeFound("pesoKG.equals=" + UPDATED_PESO_KG);
    }

    @Test
    @Transactional
    void getAllPesosByPesoKGIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        // Get all the pesoList where pesoKG not equals to DEFAULT_PESO_KG
        defaultPesoShouldNotBeFound("pesoKG.notEquals=" + DEFAULT_PESO_KG);

        // Get all the pesoList where pesoKG not equals to UPDATED_PESO_KG
        defaultPesoShouldBeFound("pesoKG.notEquals=" + UPDATED_PESO_KG);
    }

    @Test
    @Transactional
    void getAllPesosByPesoKGIsInShouldWork() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        // Get all the pesoList where pesoKG in DEFAULT_PESO_KG or UPDATED_PESO_KG
        defaultPesoShouldBeFound("pesoKG.in=" + DEFAULT_PESO_KG + "," + UPDATED_PESO_KG);

        // Get all the pesoList where pesoKG equals to UPDATED_PESO_KG
        defaultPesoShouldNotBeFound("pesoKG.in=" + UPDATED_PESO_KG);
    }

    @Test
    @Transactional
    void getAllPesosByPesoKGIsNullOrNotNull() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        // Get all the pesoList where pesoKG is not null
        defaultPesoShouldBeFound("pesoKG.specified=true");

        // Get all the pesoList where pesoKG is null
        defaultPesoShouldNotBeFound("pesoKG.specified=false");
    }

    @Test
    @Transactional
    void getAllPesosByPesoKGIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        // Get all the pesoList where pesoKG is greater than or equal to DEFAULT_PESO_KG
        defaultPesoShouldBeFound("pesoKG.greaterThanOrEqual=" + DEFAULT_PESO_KG);

        // Get all the pesoList where pesoKG is greater than or equal to UPDATED_PESO_KG
        defaultPesoShouldNotBeFound("pesoKG.greaterThanOrEqual=" + UPDATED_PESO_KG);
    }

    @Test
    @Transactional
    void getAllPesosByPesoKGIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        // Get all the pesoList where pesoKG is less than or equal to DEFAULT_PESO_KG
        defaultPesoShouldBeFound("pesoKG.lessThanOrEqual=" + DEFAULT_PESO_KG);

        // Get all the pesoList where pesoKG is less than or equal to SMALLER_PESO_KG
        defaultPesoShouldNotBeFound("pesoKG.lessThanOrEqual=" + SMALLER_PESO_KG);
    }

    @Test
    @Transactional
    void getAllPesosByPesoKGIsLessThanSomething() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        // Get all the pesoList where pesoKG is less than DEFAULT_PESO_KG
        defaultPesoShouldNotBeFound("pesoKG.lessThan=" + DEFAULT_PESO_KG);

        // Get all the pesoList where pesoKG is less than UPDATED_PESO_KG
        defaultPesoShouldBeFound("pesoKG.lessThan=" + UPDATED_PESO_KG);
    }

    @Test
    @Transactional
    void getAllPesosByPesoKGIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        // Get all the pesoList where pesoKG is greater than DEFAULT_PESO_KG
        defaultPesoShouldNotBeFound("pesoKG.greaterThan=" + DEFAULT_PESO_KG);

        // Get all the pesoList where pesoKG is greater than SMALLER_PESO_KG
        defaultPesoShouldBeFound("pesoKG.greaterThan=" + SMALLER_PESO_KG);
    }

    @Test
    @Transactional
    void getAllPesosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        // Get all the pesoList where descripcion equals to DEFAULT_DESCRIPCION
        defaultPesoShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the pesoList where descripcion equals to UPDATED_DESCRIPCION
        defaultPesoShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllPesosByDescripcionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        // Get all the pesoList where descripcion not equals to DEFAULT_DESCRIPCION
        defaultPesoShouldNotBeFound("descripcion.notEquals=" + DEFAULT_DESCRIPCION);

        // Get all the pesoList where descripcion not equals to UPDATED_DESCRIPCION
        defaultPesoShouldBeFound("descripcion.notEquals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllPesosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        // Get all the pesoList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultPesoShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the pesoList where descripcion equals to UPDATED_DESCRIPCION
        defaultPesoShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllPesosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        // Get all the pesoList where descripcion is not null
        defaultPesoShouldBeFound("descripcion.specified=true");

        // Get all the pesoList where descripcion is null
        defaultPesoShouldNotBeFound("descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllPesosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        // Get all the pesoList where descripcion contains DEFAULT_DESCRIPCION
        defaultPesoShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the pesoList where descripcion contains UPDATED_DESCRIPCION
        defaultPesoShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllPesosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        // Get all the pesoList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultPesoShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the pesoList where descripcion does not contain UPDATED_DESCRIPCION
        defaultPesoShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllPesosByFechaRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        // Get all the pesoList where fechaRegistro equals to DEFAULT_FECHA_REGISTRO
        defaultPesoShouldBeFound("fechaRegistro.equals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the pesoList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultPesoShouldNotBeFound("fechaRegistro.equals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllPesosByFechaRegistroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        // Get all the pesoList where fechaRegistro not equals to DEFAULT_FECHA_REGISTRO
        defaultPesoShouldNotBeFound("fechaRegistro.notEquals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the pesoList where fechaRegistro not equals to UPDATED_FECHA_REGISTRO
        defaultPesoShouldBeFound("fechaRegistro.notEquals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllPesosByFechaRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        // Get all the pesoList where fechaRegistro in DEFAULT_FECHA_REGISTRO or UPDATED_FECHA_REGISTRO
        defaultPesoShouldBeFound("fechaRegistro.in=" + DEFAULT_FECHA_REGISTRO + "," + UPDATED_FECHA_REGISTRO);

        // Get all the pesoList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultPesoShouldNotBeFound("fechaRegistro.in=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllPesosByFechaRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        // Get all the pesoList where fechaRegistro is not null
        defaultPesoShouldBeFound("fechaRegistro.specified=true");

        // Get all the pesoList where fechaRegistro is null
        defaultPesoShouldNotBeFound("fechaRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllPesosByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        peso.setUser(user);
        pesoRepository.saveAndFlush(peso);
        String userId = user.getId();

        // Get all the pesoList where user equals to userId
        defaultPesoShouldBeFound("userId.equals=" + userId);

        // Get all the pesoList where user equals to "invalid-id"
        defaultPesoShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPesoShouldBeFound(String filter) throws Exception {
        restPesoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(peso.getId().intValue())))
            .andExpect(jsonPath("$.[*].pesoKG").value(hasItem(DEFAULT_PESO_KG)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));

        // Check, that the count call also returns 1
        restPesoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPesoShouldNotBeFound(String filter) throws Exception {
        restPesoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPesoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPeso() throws Exception {
        // Get the peso
        restPesoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPeso() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        int databaseSizeBeforeUpdate = pesoRepository.findAll().size();

        // Update the peso
        Peso updatedPeso = pesoRepository.findById(peso.getId()).get();
        // Disconnect from session so that the updates on updatedPeso are not directly saved in db
        em.detach(updatedPeso);
        updatedPeso.pesoKG(UPDATED_PESO_KG).descripcion(UPDATED_DESCRIPCION).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restPesoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPeso.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPeso))
            )
            .andExpect(status().isOk());

        // Validate the Peso in the database
        List<Peso> pesoList = pesoRepository.findAll();
        assertThat(pesoList).hasSize(databaseSizeBeforeUpdate);
        Peso testPeso = pesoList.get(pesoList.size() - 1);
        assertThat(testPeso.getPesoKG()).isEqualTo(UPDATED_PESO_KG);
        assertThat(testPeso.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testPeso.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void putNonExistingPeso() throws Exception {
        int databaseSizeBeforeUpdate = pesoRepository.findAll().size();
        peso.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPesoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, peso.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(peso))
            )
            .andExpect(status().isBadRequest());

        // Validate the Peso in the database
        List<Peso> pesoList = pesoRepository.findAll();
        assertThat(pesoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPeso() throws Exception {
        int databaseSizeBeforeUpdate = pesoRepository.findAll().size();
        peso.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPesoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(peso))
            )
            .andExpect(status().isBadRequest());

        // Validate the Peso in the database
        List<Peso> pesoList = pesoRepository.findAll();
        assertThat(pesoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPeso() throws Exception {
        int databaseSizeBeforeUpdate = pesoRepository.findAll().size();
        peso.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPesoMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(peso))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Peso in the database
        List<Peso> pesoList = pesoRepository.findAll();
        assertThat(pesoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePesoWithPatch() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        int databaseSizeBeforeUpdate = pesoRepository.findAll().size();

        // Update the peso using partial update
        Peso partialUpdatedPeso = new Peso();
        partialUpdatedPeso.setId(peso.getId());

        partialUpdatedPeso.fechaRegistro(UPDATED_FECHA_REGISTRO);

        restPesoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeso.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPeso))
            )
            .andExpect(status().isOk());

        // Validate the Peso in the database
        List<Peso> pesoList = pesoRepository.findAll();
        assertThat(pesoList).hasSize(databaseSizeBeforeUpdate);
        Peso testPeso = pesoList.get(pesoList.size() - 1);
        assertThat(testPeso.getPesoKG()).isEqualTo(DEFAULT_PESO_KG);
        assertThat(testPeso.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testPeso.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void fullUpdatePesoWithPatch() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        int databaseSizeBeforeUpdate = pesoRepository.findAll().size();

        // Update the peso using partial update
        Peso partialUpdatedPeso = new Peso();
        partialUpdatedPeso.setId(peso.getId());

        partialUpdatedPeso.pesoKG(UPDATED_PESO_KG).descripcion(UPDATED_DESCRIPCION).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restPesoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeso.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPeso))
            )
            .andExpect(status().isOk());

        // Validate the Peso in the database
        List<Peso> pesoList = pesoRepository.findAll();
        assertThat(pesoList).hasSize(databaseSizeBeforeUpdate);
        Peso testPeso = pesoList.get(pesoList.size() - 1);
        assertThat(testPeso.getPesoKG()).isEqualTo(UPDATED_PESO_KG);
        assertThat(testPeso.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testPeso.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void patchNonExistingPeso() throws Exception {
        int databaseSizeBeforeUpdate = pesoRepository.findAll().size();
        peso.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPesoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, peso.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(peso))
            )
            .andExpect(status().isBadRequest());

        // Validate the Peso in the database
        List<Peso> pesoList = pesoRepository.findAll();
        assertThat(pesoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPeso() throws Exception {
        int databaseSizeBeforeUpdate = pesoRepository.findAll().size();
        peso.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPesoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(peso))
            )
            .andExpect(status().isBadRequest());

        // Validate the Peso in the database
        List<Peso> pesoList = pesoRepository.findAll();
        assertThat(pesoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPeso() throws Exception {
        int databaseSizeBeforeUpdate = pesoRepository.findAll().size();
        peso.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPesoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(peso))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Peso in the database
        List<Peso> pesoList = pesoRepository.findAll();
        assertThat(pesoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePeso() throws Exception {
        // Initialize the database
        pesoRepository.saveAndFlush(peso);

        int databaseSizeBeforeDelete = pesoRepository.findAll().size();

        // Delete the peso
        restPesoMockMvc
            .perform(delete(ENTITY_API_URL_ID, peso.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Peso> pesoList = pesoRepository.findAll();
        assertThat(pesoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

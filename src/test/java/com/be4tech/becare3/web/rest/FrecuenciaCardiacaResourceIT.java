package com.be4tech.becare3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.FrecuenciaCardiaca;
import com.be4tech.becare3.domain.User;
import com.be4tech.becare3.repository.FrecuenciaCardiacaRepository;
import com.be4tech.becare3.service.criteria.FrecuenciaCardiacaCriteria;
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
 * Integration tests for the {@link FrecuenciaCardiacaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FrecuenciaCardiacaResourceIT {

    private static final Integer DEFAULT_FRECUENCIA_CARDIACA = 1;
    private static final Integer UPDATED_FRECUENCIA_CARDIACA = 2;
    private static final Integer SMALLER_FRECUENCIA_CARDIACA = 1 - 1;

    private static final Instant DEFAULT_FECHA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/frecuencia-cardiacas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FrecuenciaCardiacaRepository frecuenciaCardiacaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFrecuenciaCardiacaMockMvc;

    private FrecuenciaCardiaca frecuenciaCardiaca;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FrecuenciaCardiaca createEntity(EntityManager em) {
        FrecuenciaCardiaca frecuenciaCardiaca = new FrecuenciaCardiaca()
            .frecuenciaCardiaca(DEFAULT_FRECUENCIA_CARDIACA)
            .fechaRegistro(DEFAULT_FECHA_REGISTRO);
        return frecuenciaCardiaca;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FrecuenciaCardiaca createUpdatedEntity(EntityManager em) {
        FrecuenciaCardiaca frecuenciaCardiaca = new FrecuenciaCardiaca()
            .frecuenciaCardiaca(UPDATED_FRECUENCIA_CARDIACA)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);
        return frecuenciaCardiaca;
    }

    @BeforeEach
    public void initTest() {
        frecuenciaCardiaca = createEntity(em);
    }

    @Test
    @Transactional
    void createFrecuenciaCardiaca() throws Exception {
        int databaseSizeBeforeCreate = frecuenciaCardiacaRepository.findAll().size();
        // Create the FrecuenciaCardiaca
        restFrecuenciaCardiacaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(frecuenciaCardiaca))
            )
            .andExpect(status().isCreated());

        // Validate the FrecuenciaCardiaca in the database
        List<FrecuenciaCardiaca> frecuenciaCardiacaList = frecuenciaCardiacaRepository.findAll();
        assertThat(frecuenciaCardiacaList).hasSize(databaseSizeBeforeCreate + 1);
        FrecuenciaCardiaca testFrecuenciaCardiaca = frecuenciaCardiacaList.get(frecuenciaCardiacaList.size() - 1);
        assertThat(testFrecuenciaCardiaca.getFrecuenciaCardiaca()).isEqualTo(DEFAULT_FRECUENCIA_CARDIACA);
        assertThat(testFrecuenciaCardiaca.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void createFrecuenciaCardiacaWithExistingId() throws Exception {
        // Create the FrecuenciaCardiaca with an existing ID
        frecuenciaCardiaca.setId(1L);

        int databaseSizeBeforeCreate = frecuenciaCardiacaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFrecuenciaCardiacaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(frecuenciaCardiaca))
            )
            .andExpect(status().isBadRequest());

        // Validate the FrecuenciaCardiaca in the database
        List<FrecuenciaCardiaca> frecuenciaCardiacaList = frecuenciaCardiacaRepository.findAll();
        assertThat(frecuenciaCardiacaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFrecuenciaCardiacas() throws Exception {
        // Initialize the database
        frecuenciaCardiacaRepository.saveAndFlush(frecuenciaCardiaca);

        // Get all the frecuenciaCardiacaList
        restFrecuenciaCardiacaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(frecuenciaCardiaca.getId().intValue())))
            .andExpect(jsonPath("$.[*].frecuenciaCardiaca").value(hasItem(DEFAULT_FRECUENCIA_CARDIACA)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));
    }

    @Test
    @Transactional
    void getFrecuenciaCardiaca() throws Exception {
        // Initialize the database
        frecuenciaCardiacaRepository.saveAndFlush(frecuenciaCardiaca);

        // Get the frecuenciaCardiaca
        restFrecuenciaCardiacaMockMvc
            .perform(get(ENTITY_API_URL_ID, frecuenciaCardiaca.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(frecuenciaCardiaca.getId().intValue()))
            .andExpect(jsonPath("$.frecuenciaCardiaca").value(DEFAULT_FRECUENCIA_CARDIACA))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()));
    }

    @Test
    @Transactional
    void getFrecuenciaCardiacasByIdFiltering() throws Exception {
        // Initialize the database
        frecuenciaCardiacaRepository.saveAndFlush(frecuenciaCardiaca);

        Long id = frecuenciaCardiaca.getId();

        defaultFrecuenciaCardiacaShouldBeFound("id.equals=" + id);
        defaultFrecuenciaCardiacaShouldNotBeFound("id.notEquals=" + id);

        defaultFrecuenciaCardiacaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFrecuenciaCardiacaShouldNotBeFound("id.greaterThan=" + id);

        defaultFrecuenciaCardiacaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFrecuenciaCardiacaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFrecuenciaCardiacasByFrecuenciaCardiacaIsEqualToSomething() throws Exception {
        // Initialize the database
        frecuenciaCardiacaRepository.saveAndFlush(frecuenciaCardiaca);

        // Get all the frecuenciaCardiacaList where frecuenciaCardiaca equals to DEFAULT_FRECUENCIA_CARDIACA
        defaultFrecuenciaCardiacaShouldBeFound("frecuenciaCardiaca.equals=" + DEFAULT_FRECUENCIA_CARDIACA);

        // Get all the frecuenciaCardiacaList where frecuenciaCardiaca equals to UPDATED_FRECUENCIA_CARDIACA
        defaultFrecuenciaCardiacaShouldNotBeFound("frecuenciaCardiaca.equals=" + UPDATED_FRECUENCIA_CARDIACA);
    }

    @Test
    @Transactional
    void getAllFrecuenciaCardiacasByFrecuenciaCardiacaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        frecuenciaCardiacaRepository.saveAndFlush(frecuenciaCardiaca);

        // Get all the frecuenciaCardiacaList where frecuenciaCardiaca not equals to DEFAULT_FRECUENCIA_CARDIACA
        defaultFrecuenciaCardiacaShouldNotBeFound("frecuenciaCardiaca.notEquals=" + DEFAULT_FRECUENCIA_CARDIACA);

        // Get all the frecuenciaCardiacaList where frecuenciaCardiaca not equals to UPDATED_FRECUENCIA_CARDIACA
        defaultFrecuenciaCardiacaShouldBeFound("frecuenciaCardiaca.notEquals=" + UPDATED_FRECUENCIA_CARDIACA);
    }

    @Test
    @Transactional
    void getAllFrecuenciaCardiacasByFrecuenciaCardiacaIsInShouldWork() throws Exception {
        // Initialize the database
        frecuenciaCardiacaRepository.saveAndFlush(frecuenciaCardiaca);

        // Get all the frecuenciaCardiacaList where frecuenciaCardiaca in DEFAULT_FRECUENCIA_CARDIACA or UPDATED_FRECUENCIA_CARDIACA
        defaultFrecuenciaCardiacaShouldBeFound("frecuenciaCardiaca.in=" + DEFAULT_FRECUENCIA_CARDIACA + "," + UPDATED_FRECUENCIA_CARDIACA);

        // Get all the frecuenciaCardiacaList where frecuenciaCardiaca equals to UPDATED_FRECUENCIA_CARDIACA
        defaultFrecuenciaCardiacaShouldNotBeFound("frecuenciaCardiaca.in=" + UPDATED_FRECUENCIA_CARDIACA);
    }

    @Test
    @Transactional
    void getAllFrecuenciaCardiacasByFrecuenciaCardiacaIsNullOrNotNull() throws Exception {
        // Initialize the database
        frecuenciaCardiacaRepository.saveAndFlush(frecuenciaCardiaca);

        // Get all the frecuenciaCardiacaList where frecuenciaCardiaca is not null
        defaultFrecuenciaCardiacaShouldBeFound("frecuenciaCardiaca.specified=true");

        // Get all the frecuenciaCardiacaList where frecuenciaCardiaca is null
        defaultFrecuenciaCardiacaShouldNotBeFound("frecuenciaCardiaca.specified=false");
    }

    @Test
    @Transactional
    void getAllFrecuenciaCardiacasByFrecuenciaCardiacaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        frecuenciaCardiacaRepository.saveAndFlush(frecuenciaCardiaca);

        // Get all the frecuenciaCardiacaList where frecuenciaCardiaca is greater than or equal to DEFAULT_FRECUENCIA_CARDIACA
        defaultFrecuenciaCardiacaShouldBeFound("frecuenciaCardiaca.greaterThanOrEqual=" + DEFAULT_FRECUENCIA_CARDIACA);

        // Get all the frecuenciaCardiacaList where frecuenciaCardiaca is greater than or equal to UPDATED_FRECUENCIA_CARDIACA
        defaultFrecuenciaCardiacaShouldNotBeFound("frecuenciaCardiaca.greaterThanOrEqual=" + UPDATED_FRECUENCIA_CARDIACA);
    }

    @Test
    @Transactional
    void getAllFrecuenciaCardiacasByFrecuenciaCardiacaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        frecuenciaCardiacaRepository.saveAndFlush(frecuenciaCardiaca);

        // Get all the frecuenciaCardiacaList where frecuenciaCardiaca is less than or equal to DEFAULT_FRECUENCIA_CARDIACA
        defaultFrecuenciaCardiacaShouldBeFound("frecuenciaCardiaca.lessThanOrEqual=" + DEFAULT_FRECUENCIA_CARDIACA);

        // Get all the frecuenciaCardiacaList where frecuenciaCardiaca is less than or equal to SMALLER_FRECUENCIA_CARDIACA
        defaultFrecuenciaCardiacaShouldNotBeFound("frecuenciaCardiaca.lessThanOrEqual=" + SMALLER_FRECUENCIA_CARDIACA);
    }

    @Test
    @Transactional
    void getAllFrecuenciaCardiacasByFrecuenciaCardiacaIsLessThanSomething() throws Exception {
        // Initialize the database
        frecuenciaCardiacaRepository.saveAndFlush(frecuenciaCardiaca);

        // Get all the frecuenciaCardiacaList where frecuenciaCardiaca is less than DEFAULT_FRECUENCIA_CARDIACA
        defaultFrecuenciaCardiacaShouldNotBeFound("frecuenciaCardiaca.lessThan=" + DEFAULT_FRECUENCIA_CARDIACA);

        // Get all the frecuenciaCardiacaList where frecuenciaCardiaca is less than UPDATED_FRECUENCIA_CARDIACA
        defaultFrecuenciaCardiacaShouldBeFound("frecuenciaCardiaca.lessThan=" + UPDATED_FRECUENCIA_CARDIACA);
    }

    @Test
    @Transactional
    void getAllFrecuenciaCardiacasByFrecuenciaCardiacaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        frecuenciaCardiacaRepository.saveAndFlush(frecuenciaCardiaca);

        // Get all the frecuenciaCardiacaList where frecuenciaCardiaca is greater than DEFAULT_FRECUENCIA_CARDIACA
        defaultFrecuenciaCardiacaShouldNotBeFound("frecuenciaCardiaca.greaterThan=" + DEFAULT_FRECUENCIA_CARDIACA);

        // Get all the frecuenciaCardiacaList where frecuenciaCardiaca is greater than SMALLER_FRECUENCIA_CARDIACA
        defaultFrecuenciaCardiacaShouldBeFound("frecuenciaCardiaca.greaterThan=" + SMALLER_FRECUENCIA_CARDIACA);
    }

    @Test
    @Transactional
    void getAllFrecuenciaCardiacasByFechaRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        frecuenciaCardiacaRepository.saveAndFlush(frecuenciaCardiaca);

        // Get all the frecuenciaCardiacaList where fechaRegistro equals to DEFAULT_FECHA_REGISTRO
        defaultFrecuenciaCardiacaShouldBeFound("fechaRegistro.equals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the frecuenciaCardiacaList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultFrecuenciaCardiacaShouldNotBeFound("fechaRegistro.equals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllFrecuenciaCardiacasByFechaRegistroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        frecuenciaCardiacaRepository.saveAndFlush(frecuenciaCardiaca);

        // Get all the frecuenciaCardiacaList where fechaRegistro not equals to DEFAULT_FECHA_REGISTRO
        defaultFrecuenciaCardiacaShouldNotBeFound("fechaRegistro.notEquals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the frecuenciaCardiacaList where fechaRegistro not equals to UPDATED_FECHA_REGISTRO
        defaultFrecuenciaCardiacaShouldBeFound("fechaRegistro.notEquals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllFrecuenciaCardiacasByFechaRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        frecuenciaCardiacaRepository.saveAndFlush(frecuenciaCardiaca);

        // Get all the frecuenciaCardiacaList where fechaRegistro in DEFAULT_FECHA_REGISTRO or UPDATED_FECHA_REGISTRO
        defaultFrecuenciaCardiacaShouldBeFound("fechaRegistro.in=" + DEFAULT_FECHA_REGISTRO + "," + UPDATED_FECHA_REGISTRO);

        // Get all the frecuenciaCardiacaList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultFrecuenciaCardiacaShouldNotBeFound("fechaRegistro.in=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllFrecuenciaCardiacasByFechaRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        frecuenciaCardiacaRepository.saveAndFlush(frecuenciaCardiaca);

        // Get all the frecuenciaCardiacaList where fechaRegistro is not null
        defaultFrecuenciaCardiacaShouldBeFound("fechaRegistro.specified=true");

        // Get all the frecuenciaCardiacaList where fechaRegistro is null
        defaultFrecuenciaCardiacaShouldNotBeFound("fechaRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllFrecuenciaCardiacasByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        frecuenciaCardiacaRepository.saveAndFlush(frecuenciaCardiaca);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        frecuenciaCardiaca.setUser(user);
        frecuenciaCardiacaRepository.saveAndFlush(frecuenciaCardiaca);
        String userId = user.getId();

        // Get all the frecuenciaCardiacaList where user equals to userId
        defaultFrecuenciaCardiacaShouldBeFound("userId.equals=" + userId);

        // Get all the frecuenciaCardiacaList where user equals to "invalid-id"
        defaultFrecuenciaCardiacaShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFrecuenciaCardiacaShouldBeFound(String filter) throws Exception {
        restFrecuenciaCardiacaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(frecuenciaCardiaca.getId().intValue())))
            .andExpect(jsonPath("$.[*].frecuenciaCardiaca").value(hasItem(DEFAULT_FRECUENCIA_CARDIACA)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));

        // Check, that the count call also returns 1
        restFrecuenciaCardiacaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFrecuenciaCardiacaShouldNotBeFound(String filter) throws Exception {
        restFrecuenciaCardiacaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFrecuenciaCardiacaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFrecuenciaCardiaca() throws Exception {
        // Get the frecuenciaCardiaca
        restFrecuenciaCardiacaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFrecuenciaCardiaca() throws Exception {
        // Initialize the database
        frecuenciaCardiacaRepository.saveAndFlush(frecuenciaCardiaca);

        int databaseSizeBeforeUpdate = frecuenciaCardiacaRepository.findAll().size();

        // Update the frecuenciaCardiaca
        FrecuenciaCardiaca updatedFrecuenciaCardiaca = frecuenciaCardiacaRepository.findById(frecuenciaCardiaca.getId()).get();
        // Disconnect from session so that the updates on updatedFrecuenciaCardiaca are not directly saved in db
        em.detach(updatedFrecuenciaCardiaca);
        updatedFrecuenciaCardiaca.frecuenciaCardiaca(UPDATED_FRECUENCIA_CARDIACA).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restFrecuenciaCardiacaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFrecuenciaCardiaca.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFrecuenciaCardiaca))
            )
            .andExpect(status().isOk());

        // Validate the FrecuenciaCardiaca in the database
        List<FrecuenciaCardiaca> frecuenciaCardiacaList = frecuenciaCardiacaRepository.findAll();
        assertThat(frecuenciaCardiacaList).hasSize(databaseSizeBeforeUpdate);
        FrecuenciaCardiaca testFrecuenciaCardiaca = frecuenciaCardiacaList.get(frecuenciaCardiacaList.size() - 1);
        assertThat(testFrecuenciaCardiaca.getFrecuenciaCardiaca()).isEqualTo(UPDATED_FRECUENCIA_CARDIACA);
        assertThat(testFrecuenciaCardiaca.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void putNonExistingFrecuenciaCardiaca() throws Exception {
        int databaseSizeBeforeUpdate = frecuenciaCardiacaRepository.findAll().size();
        frecuenciaCardiaca.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFrecuenciaCardiacaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, frecuenciaCardiaca.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(frecuenciaCardiaca))
            )
            .andExpect(status().isBadRequest());

        // Validate the FrecuenciaCardiaca in the database
        List<FrecuenciaCardiaca> frecuenciaCardiacaList = frecuenciaCardiacaRepository.findAll();
        assertThat(frecuenciaCardiacaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFrecuenciaCardiaca() throws Exception {
        int databaseSizeBeforeUpdate = frecuenciaCardiacaRepository.findAll().size();
        frecuenciaCardiaca.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFrecuenciaCardiacaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(frecuenciaCardiaca))
            )
            .andExpect(status().isBadRequest());

        // Validate the FrecuenciaCardiaca in the database
        List<FrecuenciaCardiaca> frecuenciaCardiacaList = frecuenciaCardiacaRepository.findAll();
        assertThat(frecuenciaCardiacaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFrecuenciaCardiaca() throws Exception {
        int databaseSizeBeforeUpdate = frecuenciaCardiacaRepository.findAll().size();
        frecuenciaCardiaca.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFrecuenciaCardiacaMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(frecuenciaCardiaca))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FrecuenciaCardiaca in the database
        List<FrecuenciaCardiaca> frecuenciaCardiacaList = frecuenciaCardiacaRepository.findAll();
        assertThat(frecuenciaCardiacaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFrecuenciaCardiacaWithPatch() throws Exception {
        // Initialize the database
        frecuenciaCardiacaRepository.saveAndFlush(frecuenciaCardiaca);

        int databaseSizeBeforeUpdate = frecuenciaCardiacaRepository.findAll().size();

        // Update the frecuenciaCardiaca using partial update
        FrecuenciaCardiaca partialUpdatedFrecuenciaCardiaca = new FrecuenciaCardiaca();
        partialUpdatedFrecuenciaCardiaca.setId(frecuenciaCardiaca.getId());

        partialUpdatedFrecuenciaCardiaca.frecuenciaCardiaca(UPDATED_FRECUENCIA_CARDIACA);

        restFrecuenciaCardiacaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFrecuenciaCardiaca.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFrecuenciaCardiaca))
            )
            .andExpect(status().isOk());

        // Validate the FrecuenciaCardiaca in the database
        List<FrecuenciaCardiaca> frecuenciaCardiacaList = frecuenciaCardiacaRepository.findAll();
        assertThat(frecuenciaCardiacaList).hasSize(databaseSizeBeforeUpdate);
        FrecuenciaCardiaca testFrecuenciaCardiaca = frecuenciaCardiacaList.get(frecuenciaCardiacaList.size() - 1);
        assertThat(testFrecuenciaCardiaca.getFrecuenciaCardiaca()).isEqualTo(UPDATED_FRECUENCIA_CARDIACA);
        assertThat(testFrecuenciaCardiaca.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void fullUpdateFrecuenciaCardiacaWithPatch() throws Exception {
        // Initialize the database
        frecuenciaCardiacaRepository.saveAndFlush(frecuenciaCardiaca);

        int databaseSizeBeforeUpdate = frecuenciaCardiacaRepository.findAll().size();

        // Update the frecuenciaCardiaca using partial update
        FrecuenciaCardiaca partialUpdatedFrecuenciaCardiaca = new FrecuenciaCardiaca();
        partialUpdatedFrecuenciaCardiaca.setId(frecuenciaCardiaca.getId());

        partialUpdatedFrecuenciaCardiaca.frecuenciaCardiaca(UPDATED_FRECUENCIA_CARDIACA).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restFrecuenciaCardiacaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFrecuenciaCardiaca.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFrecuenciaCardiaca))
            )
            .andExpect(status().isOk());

        // Validate the FrecuenciaCardiaca in the database
        List<FrecuenciaCardiaca> frecuenciaCardiacaList = frecuenciaCardiacaRepository.findAll();
        assertThat(frecuenciaCardiacaList).hasSize(databaseSizeBeforeUpdate);
        FrecuenciaCardiaca testFrecuenciaCardiaca = frecuenciaCardiacaList.get(frecuenciaCardiacaList.size() - 1);
        assertThat(testFrecuenciaCardiaca.getFrecuenciaCardiaca()).isEqualTo(UPDATED_FRECUENCIA_CARDIACA);
        assertThat(testFrecuenciaCardiaca.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void patchNonExistingFrecuenciaCardiaca() throws Exception {
        int databaseSizeBeforeUpdate = frecuenciaCardiacaRepository.findAll().size();
        frecuenciaCardiaca.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFrecuenciaCardiacaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, frecuenciaCardiaca.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(frecuenciaCardiaca))
            )
            .andExpect(status().isBadRequest());

        // Validate the FrecuenciaCardiaca in the database
        List<FrecuenciaCardiaca> frecuenciaCardiacaList = frecuenciaCardiacaRepository.findAll();
        assertThat(frecuenciaCardiacaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFrecuenciaCardiaca() throws Exception {
        int databaseSizeBeforeUpdate = frecuenciaCardiacaRepository.findAll().size();
        frecuenciaCardiaca.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFrecuenciaCardiacaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(frecuenciaCardiaca))
            )
            .andExpect(status().isBadRequest());

        // Validate the FrecuenciaCardiaca in the database
        List<FrecuenciaCardiaca> frecuenciaCardiacaList = frecuenciaCardiacaRepository.findAll();
        assertThat(frecuenciaCardiacaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFrecuenciaCardiaca() throws Exception {
        int databaseSizeBeforeUpdate = frecuenciaCardiacaRepository.findAll().size();
        frecuenciaCardiaca.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFrecuenciaCardiacaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(frecuenciaCardiaca))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FrecuenciaCardiaca in the database
        List<FrecuenciaCardiaca> frecuenciaCardiacaList = frecuenciaCardiacaRepository.findAll();
        assertThat(frecuenciaCardiacaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFrecuenciaCardiaca() throws Exception {
        // Initialize the database
        frecuenciaCardiacaRepository.saveAndFlush(frecuenciaCardiaca);

        int databaseSizeBeforeDelete = frecuenciaCardiacaRepository.findAll().size();

        // Delete the frecuenciaCardiaca
        restFrecuenciaCardiacaMockMvc
            .perform(delete(ENTITY_API_URL_ID, frecuenciaCardiaca.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FrecuenciaCardiaca> frecuenciaCardiacaList = frecuenciaCardiacaRepository.findAll();
        assertThat(frecuenciaCardiacaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

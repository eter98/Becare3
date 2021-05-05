package com.be4tech.becare3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.Caloria;
import com.be4tech.becare3.domain.User;
import com.be4tech.becare3.repository.CaloriaRepository;
import com.be4tech.becare3.service.criteria.CaloriaCriteria;
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
 * Integration tests for the {@link CaloriaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CaloriaResourceIT {

    private static final Integer DEFAULT_CALORIAS_ACTIVAS = 1;
    private static final Integer UPDATED_CALORIAS_ACTIVAS = 2;
    private static final Integer SMALLER_CALORIAS_ACTIVAS = 1 - 1;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/calorias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CaloriaRepository caloriaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCaloriaMockMvc;

    private Caloria caloria;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caloria createEntity(EntityManager em) {
        Caloria caloria = new Caloria()
            .caloriasActivas(DEFAULT_CALORIAS_ACTIVAS)
            .descripcion(DEFAULT_DESCRIPCION)
            .fechaRegistro(DEFAULT_FECHA_REGISTRO);
        return caloria;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Caloria createUpdatedEntity(EntityManager em) {
        Caloria caloria = new Caloria()
            .caloriasActivas(UPDATED_CALORIAS_ACTIVAS)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);
        return caloria;
    }

    @BeforeEach
    public void initTest() {
        caloria = createEntity(em);
    }

    @Test
    @Transactional
    void createCaloria() throws Exception {
        int databaseSizeBeforeCreate = caloriaRepository.findAll().size();
        // Create the Caloria
        restCaloriaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caloria))
            )
            .andExpect(status().isCreated());

        // Validate the Caloria in the database
        List<Caloria> caloriaList = caloriaRepository.findAll();
        assertThat(caloriaList).hasSize(databaseSizeBeforeCreate + 1);
        Caloria testCaloria = caloriaList.get(caloriaList.size() - 1);
        assertThat(testCaloria.getCaloriasActivas()).isEqualTo(DEFAULT_CALORIAS_ACTIVAS);
        assertThat(testCaloria.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testCaloria.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void createCaloriaWithExistingId() throws Exception {
        // Create the Caloria with an existing ID
        caloria.setId(1L);

        int databaseSizeBeforeCreate = caloriaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCaloriaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caloria))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caloria in the database
        List<Caloria> caloriaList = caloriaRepository.findAll();
        assertThat(caloriaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCalorias() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        // Get all the caloriaList
        restCaloriaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caloria.getId().intValue())))
            .andExpect(jsonPath("$.[*].caloriasActivas").value(hasItem(DEFAULT_CALORIAS_ACTIVAS)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));
    }

    @Test
    @Transactional
    void getCaloria() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        // Get the caloria
        restCaloriaMockMvc
            .perform(get(ENTITY_API_URL_ID, caloria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(caloria.getId().intValue()))
            .andExpect(jsonPath("$.caloriasActivas").value(DEFAULT_CALORIAS_ACTIVAS))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()));
    }

    @Test
    @Transactional
    void getCaloriasByIdFiltering() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        Long id = caloria.getId();

        defaultCaloriaShouldBeFound("id.equals=" + id);
        defaultCaloriaShouldNotBeFound("id.notEquals=" + id);

        defaultCaloriaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCaloriaShouldNotBeFound("id.greaterThan=" + id);

        defaultCaloriaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCaloriaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCaloriasByCaloriasActivasIsEqualToSomething() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        // Get all the caloriaList where caloriasActivas equals to DEFAULT_CALORIAS_ACTIVAS
        defaultCaloriaShouldBeFound("caloriasActivas.equals=" + DEFAULT_CALORIAS_ACTIVAS);

        // Get all the caloriaList where caloriasActivas equals to UPDATED_CALORIAS_ACTIVAS
        defaultCaloriaShouldNotBeFound("caloriasActivas.equals=" + UPDATED_CALORIAS_ACTIVAS);
    }

    @Test
    @Transactional
    void getAllCaloriasByCaloriasActivasIsNotEqualToSomething() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        // Get all the caloriaList where caloriasActivas not equals to DEFAULT_CALORIAS_ACTIVAS
        defaultCaloriaShouldNotBeFound("caloriasActivas.notEquals=" + DEFAULT_CALORIAS_ACTIVAS);

        // Get all the caloriaList where caloriasActivas not equals to UPDATED_CALORIAS_ACTIVAS
        defaultCaloriaShouldBeFound("caloriasActivas.notEquals=" + UPDATED_CALORIAS_ACTIVAS);
    }

    @Test
    @Transactional
    void getAllCaloriasByCaloriasActivasIsInShouldWork() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        // Get all the caloriaList where caloriasActivas in DEFAULT_CALORIAS_ACTIVAS or UPDATED_CALORIAS_ACTIVAS
        defaultCaloriaShouldBeFound("caloriasActivas.in=" + DEFAULT_CALORIAS_ACTIVAS + "," + UPDATED_CALORIAS_ACTIVAS);

        // Get all the caloriaList where caloriasActivas equals to UPDATED_CALORIAS_ACTIVAS
        defaultCaloriaShouldNotBeFound("caloriasActivas.in=" + UPDATED_CALORIAS_ACTIVAS);
    }

    @Test
    @Transactional
    void getAllCaloriasByCaloriasActivasIsNullOrNotNull() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        // Get all the caloriaList where caloriasActivas is not null
        defaultCaloriaShouldBeFound("caloriasActivas.specified=true");

        // Get all the caloriaList where caloriasActivas is null
        defaultCaloriaShouldNotBeFound("caloriasActivas.specified=false");
    }

    @Test
    @Transactional
    void getAllCaloriasByCaloriasActivasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        // Get all the caloriaList where caloriasActivas is greater than or equal to DEFAULT_CALORIAS_ACTIVAS
        defaultCaloriaShouldBeFound("caloriasActivas.greaterThanOrEqual=" + DEFAULT_CALORIAS_ACTIVAS);

        // Get all the caloriaList where caloriasActivas is greater than or equal to UPDATED_CALORIAS_ACTIVAS
        defaultCaloriaShouldNotBeFound("caloriasActivas.greaterThanOrEqual=" + UPDATED_CALORIAS_ACTIVAS);
    }

    @Test
    @Transactional
    void getAllCaloriasByCaloriasActivasIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        // Get all the caloriaList where caloriasActivas is less than or equal to DEFAULT_CALORIAS_ACTIVAS
        defaultCaloriaShouldBeFound("caloriasActivas.lessThanOrEqual=" + DEFAULT_CALORIAS_ACTIVAS);

        // Get all the caloriaList where caloriasActivas is less than or equal to SMALLER_CALORIAS_ACTIVAS
        defaultCaloriaShouldNotBeFound("caloriasActivas.lessThanOrEqual=" + SMALLER_CALORIAS_ACTIVAS);
    }

    @Test
    @Transactional
    void getAllCaloriasByCaloriasActivasIsLessThanSomething() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        // Get all the caloriaList where caloriasActivas is less than DEFAULT_CALORIAS_ACTIVAS
        defaultCaloriaShouldNotBeFound("caloriasActivas.lessThan=" + DEFAULT_CALORIAS_ACTIVAS);

        // Get all the caloriaList where caloriasActivas is less than UPDATED_CALORIAS_ACTIVAS
        defaultCaloriaShouldBeFound("caloriasActivas.lessThan=" + UPDATED_CALORIAS_ACTIVAS);
    }

    @Test
    @Transactional
    void getAllCaloriasByCaloriasActivasIsGreaterThanSomething() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        // Get all the caloriaList where caloriasActivas is greater than DEFAULT_CALORIAS_ACTIVAS
        defaultCaloriaShouldNotBeFound("caloriasActivas.greaterThan=" + DEFAULT_CALORIAS_ACTIVAS);

        // Get all the caloriaList where caloriasActivas is greater than SMALLER_CALORIAS_ACTIVAS
        defaultCaloriaShouldBeFound("caloriasActivas.greaterThan=" + SMALLER_CALORIAS_ACTIVAS);
    }

    @Test
    @Transactional
    void getAllCaloriasByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        // Get all the caloriaList where descripcion equals to DEFAULT_DESCRIPCION
        defaultCaloriaShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the caloriaList where descripcion equals to UPDATED_DESCRIPCION
        defaultCaloriaShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCaloriasByDescripcionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        // Get all the caloriaList where descripcion not equals to DEFAULT_DESCRIPCION
        defaultCaloriaShouldNotBeFound("descripcion.notEquals=" + DEFAULT_DESCRIPCION);

        // Get all the caloriaList where descripcion not equals to UPDATED_DESCRIPCION
        defaultCaloriaShouldBeFound("descripcion.notEquals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCaloriasByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        // Get all the caloriaList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultCaloriaShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the caloriaList where descripcion equals to UPDATED_DESCRIPCION
        defaultCaloriaShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCaloriasByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        // Get all the caloriaList where descripcion is not null
        defaultCaloriaShouldBeFound("descripcion.specified=true");

        // Get all the caloriaList where descripcion is null
        defaultCaloriaShouldNotBeFound("descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllCaloriasByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        // Get all the caloriaList where descripcion contains DEFAULT_DESCRIPCION
        defaultCaloriaShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the caloriaList where descripcion contains UPDATED_DESCRIPCION
        defaultCaloriaShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCaloriasByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        // Get all the caloriaList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultCaloriaShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the caloriaList where descripcion does not contain UPDATED_DESCRIPCION
        defaultCaloriaShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllCaloriasByFechaRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        // Get all the caloriaList where fechaRegistro equals to DEFAULT_FECHA_REGISTRO
        defaultCaloriaShouldBeFound("fechaRegistro.equals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the caloriaList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultCaloriaShouldNotBeFound("fechaRegistro.equals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllCaloriasByFechaRegistroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        // Get all the caloriaList where fechaRegistro not equals to DEFAULT_FECHA_REGISTRO
        defaultCaloriaShouldNotBeFound("fechaRegistro.notEquals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the caloriaList where fechaRegistro not equals to UPDATED_FECHA_REGISTRO
        defaultCaloriaShouldBeFound("fechaRegistro.notEquals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllCaloriasByFechaRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        // Get all the caloriaList where fechaRegistro in DEFAULT_FECHA_REGISTRO or UPDATED_FECHA_REGISTRO
        defaultCaloriaShouldBeFound("fechaRegistro.in=" + DEFAULT_FECHA_REGISTRO + "," + UPDATED_FECHA_REGISTRO);

        // Get all the caloriaList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultCaloriaShouldNotBeFound("fechaRegistro.in=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllCaloriasByFechaRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        // Get all the caloriaList where fechaRegistro is not null
        defaultCaloriaShouldBeFound("fechaRegistro.specified=true");

        // Get all the caloriaList where fechaRegistro is null
        defaultCaloriaShouldNotBeFound("fechaRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllCaloriasByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        caloria.setUser(user);
        caloriaRepository.saveAndFlush(caloria);
        String userId = user.getId();

        // Get all the caloriaList where user equals to userId
        defaultCaloriaShouldBeFound("userId.equals=" + userId);

        // Get all the caloriaList where user equals to "invalid-id"
        defaultCaloriaShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCaloriaShouldBeFound(String filter) throws Exception {
        restCaloriaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(caloria.getId().intValue())))
            .andExpect(jsonPath("$.[*].caloriasActivas").value(hasItem(DEFAULT_CALORIAS_ACTIVAS)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));

        // Check, that the count call also returns 1
        restCaloriaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCaloriaShouldNotBeFound(String filter) throws Exception {
        restCaloriaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCaloriaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCaloria() throws Exception {
        // Get the caloria
        restCaloriaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCaloria() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        int databaseSizeBeforeUpdate = caloriaRepository.findAll().size();

        // Update the caloria
        Caloria updatedCaloria = caloriaRepository.findById(caloria.getId()).get();
        // Disconnect from session so that the updates on updatedCaloria are not directly saved in db
        em.detach(updatedCaloria);
        updatedCaloria.caloriasActivas(UPDATED_CALORIAS_ACTIVAS).descripcion(UPDATED_DESCRIPCION).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restCaloriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCaloria.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCaloria))
            )
            .andExpect(status().isOk());

        // Validate the Caloria in the database
        List<Caloria> caloriaList = caloriaRepository.findAll();
        assertThat(caloriaList).hasSize(databaseSizeBeforeUpdate);
        Caloria testCaloria = caloriaList.get(caloriaList.size() - 1);
        assertThat(testCaloria.getCaloriasActivas()).isEqualTo(UPDATED_CALORIAS_ACTIVAS);
        assertThat(testCaloria.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testCaloria.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void putNonExistingCaloria() throws Exception {
        int databaseSizeBeforeUpdate = caloriaRepository.findAll().size();
        caloria.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaloriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, caloria.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caloria))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caloria in the database
        List<Caloria> caloriaList = caloriaRepository.findAll();
        assertThat(caloriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCaloria() throws Exception {
        int databaseSizeBeforeUpdate = caloriaRepository.findAll().size();
        caloria.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaloriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(caloria))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caloria in the database
        List<Caloria> caloriaList = caloriaRepository.findAll();
        assertThat(caloriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCaloria() throws Exception {
        int databaseSizeBeforeUpdate = caloriaRepository.findAll().size();
        caloria.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaloriaMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(caloria))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Caloria in the database
        List<Caloria> caloriaList = caloriaRepository.findAll();
        assertThat(caloriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCaloriaWithPatch() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        int databaseSizeBeforeUpdate = caloriaRepository.findAll().size();

        // Update the caloria using partial update
        Caloria partialUpdatedCaloria = new Caloria();
        partialUpdatedCaloria.setId(caloria.getId());

        partialUpdatedCaloria
            .caloriasActivas(UPDATED_CALORIAS_ACTIVAS)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restCaloriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaloria.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCaloria))
            )
            .andExpect(status().isOk());

        // Validate the Caloria in the database
        List<Caloria> caloriaList = caloriaRepository.findAll();
        assertThat(caloriaList).hasSize(databaseSizeBeforeUpdate);
        Caloria testCaloria = caloriaList.get(caloriaList.size() - 1);
        assertThat(testCaloria.getCaloriasActivas()).isEqualTo(UPDATED_CALORIAS_ACTIVAS);
        assertThat(testCaloria.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testCaloria.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void fullUpdateCaloriaWithPatch() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        int databaseSizeBeforeUpdate = caloriaRepository.findAll().size();

        // Update the caloria using partial update
        Caloria partialUpdatedCaloria = new Caloria();
        partialUpdatedCaloria.setId(caloria.getId());

        partialUpdatedCaloria
            .caloriasActivas(UPDATED_CALORIAS_ACTIVAS)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restCaloriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCaloria.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCaloria))
            )
            .andExpect(status().isOk());

        // Validate the Caloria in the database
        List<Caloria> caloriaList = caloriaRepository.findAll();
        assertThat(caloriaList).hasSize(databaseSizeBeforeUpdate);
        Caloria testCaloria = caloriaList.get(caloriaList.size() - 1);
        assertThat(testCaloria.getCaloriasActivas()).isEqualTo(UPDATED_CALORIAS_ACTIVAS);
        assertThat(testCaloria.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testCaloria.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void patchNonExistingCaloria() throws Exception {
        int databaseSizeBeforeUpdate = caloriaRepository.findAll().size();
        caloria.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCaloriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, caloria.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(caloria))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caloria in the database
        List<Caloria> caloriaList = caloriaRepository.findAll();
        assertThat(caloriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCaloria() throws Exception {
        int databaseSizeBeforeUpdate = caloriaRepository.findAll().size();
        caloria.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaloriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(caloria))
            )
            .andExpect(status().isBadRequest());

        // Validate the Caloria in the database
        List<Caloria> caloriaList = caloriaRepository.findAll();
        assertThat(caloriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCaloria() throws Exception {
        int databaseSizeBeforeUpdate = caloriaRepository.findAll().size();
        caloria.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCaloriaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(caloria))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Caloria in the database
        List<Caloria> caloriaList = caloriaRepository.findAll();
        assertThat(caloriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCaloria() throws Exception {
        // Initialize the database
        caloriaRepository.saveAndFlush(caloria);

        int databaseSizeBeforeDelete = caloriaRepository.findAll().size();

        // Delete the caloria
        restCaloriaMockMvc
            .perform(delete(ENTITY_API_URL_ID, caloria.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Caloria> caloriaList = caloriaRepository.findAll();
        assertThat(caloriaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

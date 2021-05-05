package com.be4tech.becare3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.Programa;
import com.be4tech.becare3.domain.User;
import com.be4tech.becare3.repository.ProgramaRepository;
import com.be4tech.becare3.service.criteria.ProgramaCriteria;
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
 * Integration tests for the {@link ProgramaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProgramaResourceIT {

    private static final Integer DEFAULT_CALORIAS_ACTIVIDAD = 1;
    private static final Integer UPDATED_CALORIAS_ACTIVIDAD = 2;
    private static final Integer SMALLER_CALORIAS_ACTIVIDAD = 1 - 1;

    private static final Integer DEFAULT_PASOS_ACTIVIDAD = 1;
    private static final Integer UPDATED_PASOS_ACTIVIDAD = 2;
    private static final Integer SMALLER_PASOS_ACTIVIDAD = 1 - 1;

    private static final Instant DEFAULT_FECHA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/programas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProgramaRepository programaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProgramaMockMvc;

    private Programa programa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Programa createEntity(EntityManager em) {
        Programa programa = new Programa()
            .caloriasActividad(DEFAULT_CALORIAS_ACTIVIDAD)
            .pasosActividad(DEFAULT_PASOS_ACTIVIDAD)
            .fechaRegistro(DEFAULT_FECHA_REGISTRO);
        return programa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Programa createUpdatedEntity(EntityManager em) {
        Programa programa = new Programa()
            .caloriasActividad(UPDATED_CALORIAS_ACTIVIDAD)
            .pasosActividad(UPDATED_PASOS_ACTIVIDAD)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);
        return programa;
    }

    @BeforeEach
    public void initTest() {
        programa = createEntity(em);
    }

    @Test
    @Transactional
    void createPrograma() throws Exception {
        int databaseSizeBeforeCreate = programaRepository.findAll().size();
        // Create the Programa
        restProgramaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programa))
            )
            .andExpect(status().isCreated());

        // Validate the Programa in the database
        List<Programa> programaList = programaRepository.findAll();
        assertThat(programaList).hasSize(databaseSizeBeforeCreate + 1);
        Programa testPrograma = programaList.get(programaList.size() - 1);
        assertThat(testPrograma.getCaloriasActividad()).isEqualTo(DEFAULT_CALORIAS_ACTIVIDAD);
        assertThat(testPrograma.getPasosActividad()).isEqualTo(DEFAULT_PASOS_ACTIVIDAD);
        assertThat(testPrograma.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void createProgramaWithExistingId() throws Exception {
        // Create the Programa with an existing ID
        programa.setId(1L);

        int databaseSizeBeforeCreate = programaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgramaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Programa in the database
        List<Programa> programaList = programaRepository.findAll();
        assertThat(programaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProgramas() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        // Get all the programaList
        restProgramaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(programa.getId().intValue())))
            .andExpect(jsonPath("$.[*].caloriasActividad").value(hasItem(DEFAULT_CALORIAS_ACTIVIDAD)))
            .andExpect(jsonPath("$.[*].pasosActividad").value(hasItem(DEFAULT_PASOS_ACTIVIDAD)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));
    }

    @Test
    @Transactional
    void getPrograma() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        // Get the programa
        restProgramaMockMvc
            .perform(get(ENTITY_API_URL_ID, programa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(programa.getId().intValue()))
            .andExpect(jsonPath("$.caloriasActividad").value(DEFAULT_CALORIAS_ACTIVIDAD))
            .andExpect(jsonPath("$.pasosActividad").value(DEFAULT_PASOS_ACTIVIDAD))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()));
    }

    @Test
    @Transactional
    void getProgramasByIdFiltering() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        Long id = programa.getId();

        defaultProgramaShouldBeFound("id.equals=" + id);
        defaultProgramaShouldNotBeFound("id.notEquals=" + id);

        defaultProgramaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProgramaShouldNotBeFound("id.greaterThan=" + id);

        defaultProgramaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProgramaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProgramasByCaloriasActividadIsEqualToSomething() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        // Get all the programaList where caloriasActividad equals to DEFAULT_CALORIAS_ACTIVIDAD
        defaultProgramaShouldBeFound("caloriasActividad.equals=" + DEFAULT_CALORIAS_ACTIVIDAD);

        // Get all the programaList where caloriasActividad equals to UPDATED_CALORIAS_ACTIVIDAD
        defaultProgramaShouldNotBeFound("caloriasActividad.equals=" + UPDATED_CALORIAS_ACTIVIDAD);
    }

    @Test
    @Transactional
    void getAllProgramasByCaloriasActividadIsNotEqualToSomething() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        // Get all the programaList where caloriasActividad not equals to DEFAULT_CALORIAS_ACTIVIDAD
        defaultProgramaShouldNotBeFound("caloriasActividad.notEquals=" + DEFAULT_CALORIAS_ACTIVIDAD);

        // Get all the programaList where caloriasActividad not equals to UPDATED_CALORIAS_ACTIVIDAD
        defaultProgramaShouldBeFound("caloriasActividad.notEquals=" + UPDATED_CALORIAS_ACTIVIDAD);
    }

    @Test
    @Transactional
    void getAllProgramasByCaloriasActividadIsInShouldWork() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        // Get all the programaList where caloriasActividad in DEFAULT_CALORIAS_ACTIVIDAD or UPDATED_CALORIAS_ACTIVIDAD
        defaultProgramaShouldBeFound("caloriasActividad.in=" + DEFAULT_CALORIAS_ACTIVIDAD + "," + UPDATED_CALORIAS_ACTIVIDAD);

        // Get all the programaList where caloriasActividad equals to UPDATED_CALORIAS_ACTIVIDAD
        defaultProgramaShouldNotBeFound("caloriasActividad.in=" + UPDATED_CALORIAS_ACTIVIDAD);
    }

    @Test
    @Transactional
    void getAllProgramasByCaloriasActividadIsNullOrNotNull() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        // Get all the programaList where caloriasActividad is not null
        defaultProgramaShouldBeFound("caloriasActividad.specified=true");

        // Get all the programaList where caloriasActividad is null
        defaultProgramaShouldNotBeFound("caloriasActividad.specified=false");
    }

    @Test
    @Transactional
    void getAllProgramasByCaloriasActividadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        // Get all the programaList where caloriasActividad is greater than or equal to DEFAULT_CALORIAS_ACTIVIDAD
        defaultProgramaShouldBeFound("caloriasActividad.greaterThanOrEqual=" + DEFAULT_CALORIAS_ACTIVIDAD);

        // Get all the programaList where caloriasActividad is greater than or equal to UPDATED_CALORIAS_ACTIVIDAD
        defaultProgramaShouldNotBeFound("caloriasActividad.greaterThanOrEqual=" + UPDATED_CALORIAS_ACTIVIDAD);
    }

    @Test
    @Transactional
    void getAllProgramasByCaloriasActividadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        // Get all the programaList where caloriasActividad is less than or equal to DEFAULT_CALORIAS_ACTIVIDAD
        defaultProgramaShouldBeFound("caloriasActividad.lessThanOrEqual=" + DEFAULT_CALORIAS_ACTIVIDAD);

        // Get all the programaList where caloriasActividad is less than or equal to SMALLER_CALORIAS_ACTIVIDAD
        defaultProgramaShouldNotBeFound("caloriasActividad.lessThanOrEqual=" + SMALLER_CALORIAS_ACTIVIDAD);
    }

    @Test
    @Transactional
    void getAllProgramasByCaloriasActividadIsLessThanSomething() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        // Get all the programaList where caloriasActividad is less than DEFAULT_CALORIAS_ACTIVIDAD
        defaultProgramaShouldNotBeFound("caloriasActividad.lessThan=" + DEFAULT_CALORIAS_ACTIVIDAD);

        // Get all the programaList where caloriasActividad is less than UPDATED_CALORIAS_ACTIVIDAD
        defaultProgramaShouldBeFound("caloriasActividad.lessThan=" + UPDATED_CALORIAS_ACTIVIDAD);
    }

    @Test
    @Transactional
    void getAllProgramasByCaloriasActividadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        // Get all the programaList where caloriasActividad is greater than DEFAULT_CALORIAS_ACTIVIDAD
        defaultProgramaShouldNotBeFound("caloriasActividad.greaterThan=" + DEFAULT_CALORIAS_ACTIVIDAD);

        // Get all the programaList where caloriasActividad is greater than SMALLER_CALORIAS_ACTIVIDAD
        defaultProgramaShouldBeFound("caloriasActividad.greaterThan=" + SMALLER_CALORIAS_ACTIVIDAD);
    }

    @Test
    @Transactional
    void getAllProgramasByPasosActividadIsEqualToSomething() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        // Get all the programaList where pasosActividad equals to DEFAULT_PASOS_ACTIVIDAD
        defaultProgramaShouldBeFound("pasosActividad.equals=" + DEFAULT_PASOS_ACTIVIDAD);

        // Get all the programaList where pasosActividad equals to UPDATED_PASOS_ACTIVIDAD
        defaultProgramaShouldNotBeFound("pasosActividad.equals=" + UPDATED_PASOS_ACTIVIDAD);
    }

    @Test
    @Transactional
    void getAllProgramasByPasosActividadIsNotEqualToSomething() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        // Get all the programaList where pasosActividad not equals to DEFAULT_PASOS_ACTIVIDAD
        defaultProgramaShouldNotBeFound("pasosActividad.notEquals=" + DEFAULT_PASOS_ACTIVIDAD);

        // Get all the programaList where pasosActividad not equals to UPDATED_PASOS_ACTIVIDAD
        defaultProgramaShouldBeFound("pasosActividad.notEquals=" + UPDATED_PASOS_ACTIVIDAD);
    }

    @Test
    @Transactional
    void getAllProgramasByPasosActividadIsInShouldWork() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        // Get all the programaList where pasosActividad in DEFAULT_PASOS_ACTIVIDAD or UPDATED_PASOS_ACTIVIDAD
        defaultProgramaShouldBeFound("pasosActividad.in=" + DEFAULT_PASOS_ACTIVIDAD + "," + UPDATED_PASOS_ACTIVIDAD);

        // Get all the programaList where pasosActividad equals to UPDATED_PASOS_ACTIVIDAD
        defaultProgramaShouldNotBeFound("pasosActividad.in=" + UPDATED_PASOS_ACTIVIDAD);
    }

    @Test
    @Transactional
    void getAllProgramasByPasosActividadIsNullOrNotNull() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        // Get all the programaList where pasosActividad is not null
        defaultProgramaShouldBeFound("pasosActividad.specified=true");

        // Get all the programaList where pasosActividad is null
        defaultProgramaShouldNotBeFound("pasosActividad.specified=false");
    }

    @Test
    @Transactional
    void getAllProgramasByPasosActividadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        // Get all the programaList where pasosActividad is greater than or equal to DEFAULT_PASOS_ACTIVIDAD
        defaultProgramaShouldBeFound("pasosActividad.greaterThanOrEqual=" + DEFAULT_PASOS_ACTIVIDAD);

        // Get all the programaList where pasosActividad is greater than or equal to UPDATED_PASOS_ACTIVIDAD
        defaultProgramaShouldNotBeFound("pasosActividad.greaterThanOrEqual=" + UPDATED_PASOS_ACTIVIDAD);
    }

    @Test
    @Transactional
    void getAllProgramasByPasosActividadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        // Get all the programaList where pasosActividad is less than or equal to DEFAULT_PASOS_ACTIVIDAD
        defaultProgramaShouldBeFound("pasosActividad.lessThanOrEqual=" + DEFAULT_PASOS_ACTIVIDAD);

        // Get all the programaList where pasosActividad is less than or equal to SMALLER_PASOS_ACTIVIDAD
        defaultProgramaShouldNotBeFound("pasosActividad.lessThanOrEqual=" + SMALLER_PASOS_ACTIVIDAD);
    }

    @Test
    @Transactional
    void getAllProgramasByPasosActividadIsLessThanSomething() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        // Get all the programaList where pasosActividad is less than DEFAULT_PASOS_ACTIVIDAD
        defaultProgramaShouldNotBeFound("pasosActividad.lessThan=" + DEFAULT_PASOS_ACTIVIDAD);

        // Get all the programaList where pasosActividad is less than UPDATED_PASOS_ACTIVIDAD
        defaultProgramaShouldBeFound("pasosActividad.lessThan=" + UPDATED_PASOS_ACTIVIDAD);
    }

    @Test
    @Transactional
    void getAllProgramasByPasosActividadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        // Get all the programaList where pasosActividad is greater than DEFAULT_PASOS_ACTIVIDAD
        defaultProgramaShouldNotBeFound("pasosActividad.greaterThan=" + DEFAULT_PASOS_ACTIVIDAD);

        // Get all the programaList where pasosActividad is greater than SMALLER_PASOS_ACTIVIDAD
        defaultProgramaShouldBeFound("pasosActividad.greaterThan=" + SMALLER_PASOS_ACTIVIDAD);
    }

    @Test
    @Transactional
    void getAllProgramasByFechaRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        // Get all the programaList where fechaRegistro equals to DEFAULT_FECHA_REGISTRO
        defaultProgramaShouldBeFound("fechaRegistro.equals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the programaList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultProgramaShouldNotBeFound("fechaRegistro.equals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllProgramasByFechaRegistroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        // Get all the programaList where fechaRegistro not equals to DEFAULT_FECHA_REGISTRO
        defaultProgramaShouldNotBeFound("fechaRegistro.notEquals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the programaList where fechaRegistro not equals to UPDATED_FECHA_REGISTRO
        defaultProgramaShouldBeFound("fechaRegistro.notEquals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllProgramasByFechaRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        // Get all the programaList where fechaRegistro in DEFAULT_FECHA_REGISTRO or UPDATED_FECHA_REGISTRO
        defaultProgramaShouldBeFound("fechaRegistro.in=" + DEFAULT_FECHA_REGISTRO + "," + UPDATED_FECHA_REGISTRO);

        // Get all the programaList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultProgramaShouldNotBeFound("fechaRegistro.in=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllProgramasByFechaRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        // Get all the programaList where fechaRegistro is not null
        defaultProgramaShouldBeFound("fechaRegistro.specified=true");

        // Get all the programaList where fechaRegistro is null
        defaultProgramaShouldNotBeFound("fechaRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllProgramasByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        programa.setUser(user);
        programaRepository.saveAndFlush(programa);
        String userId = user.getId();

        // Get all the programaList where user equals to userId
        defaultProgramaShouldBeFound("userId.equals=" + userId);

        // Get all the programaList where user equals to "invalid-id"
        defaultProgramaShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProgramaShouldBeFound(String filter) throws Exception {
        restProgramaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(programa.getId().intValue())))
            .andExpect(jsonPath("$.[*].caloriasActividad").value(hasItem(DEFAULT_CALORIAS_ACTIVIDAD)))
            .andExpect(jsonPath("$.[*].pasosActividad").value(hasItem(DEFAULT_PASOS_ACTIVIDAD)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));

        // Check, that the count call also returns 1
        restProgramaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProgramaShouldNotBeFound(String filter) throws Exception {
        restProgramaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProgramaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPrograma() throws Exception {
        // Get the programa
        restProgramaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPrograma() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        int databaseSizeBeforeUpdate = programaRepository.findAll().size();

        // Update the programa
        Programa updatedPrograma = programaRepository.findById(programa.getId()).get();
        // Disconnect from session so that the updates on updatedPrograma are not directly saved in db
        em.detach(updatedPrograma);
        updatedPrograma
            .caloriasActividad(UPDATED_CALORIAS_ACTIVIDAD)
            .pasosActividad(UPDATED_PASOS_ACTIVIDAD)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restProgramaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPrograma.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPrograma))
            )
            .andExpect(status().isOk());

        // Validate the Programa in the database
        List<Programa> programaList = programaRepository.findAll();
        assertThat(programaList).hasSize(databaseSizeBeforeUpdate);
        Programa testPrograma = programaList.get(programaList.size() - 1);
        assertThat(testPrograma.getCaloriasActividad()).isEqualTo(UPDATED_CALORIAS_ACTIVIDAD);
        assertThat(testPrograma.getPasosActividad()).isEqualTo(UPDATED_PASOS_ACTIVIDAD);
        assertThat(testPrograma.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void putNonExistingPrograma() throws Exception {
        int databaseSizeBeforeUpdate = programaRepository.findAll().size();
        programa.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgramaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, programa.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Programa in the database
        List<Programa> programaList = programaRepository.findAll();
        assertThat(programaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrograma() throws Exception {
        int databaseSizeBeforeUpdate = programaRepository.findAll().size();
        programa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Programa in the database
        List<Programa> programaList = programaRepository.findAll();
        assertThat(programaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrograma() throws Exception {
        int databaseSizeBeforeUpdate = programaRepository.findAll().size();
        programa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramaMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(programa))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Programa in the database
        List<Programa> programaList = programaRepository.findAll();
        assertThat(programaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProgramaWithPatch() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        int databaseSizeBeforeUpdate = programaRepository.findAll().size();

        // Update the programa using partial update
        Programa partialUpdatedPrograma = new Programa();
        partialUpdatedPrograma.setId(programa.getId());

        partialUpdatedPrograma.fechaRegistro(UPDATED_FECHA_REGISTRO);

        restProgramaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrograma.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrograma))
            )
            .andExpect(status().isOk());

        // Validate the Programa in the database
        List<Programa> programaList = programaRepository.findAll();
        assertThat(programaList).hasSize(databaseSizeBeforeUpdate);
        Programa testPrograma = programaList.get(programaList.size() - 1);
        assertThat(testPrograma.getCaloriasActividad()).isEqualTo(DEFAULT_CALORIAS_ACTIVIDAD);
        assertThat(testPrograma.getPasosActividad()).isEqualTo(DEFAULT_PASOS_ACTIVIDAD);
        assertThat(testPrograma.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void fullUpdateProgramaWithPatch() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        int databaseSizeBeforeUpdate = programaRepository.findAll().size();

        // Update the programa using partial update
        Programa partialUpdatedPrograma = new Programa();
        partialUpdatedPrograma.setId(programa.getId());

        partialUpdatedPrograma
            .caloriasActividad(UPDATED_CALORIAS_ACTIVIDAD)
            .pasosActividad(UPDATED_PASOS_ACTIVIDAD)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restProgramaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrograma.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrograma))
            )
            .andExpect(status().isOk());

        // Validate the Programa in the database
        List<Programa> programaList = programaRepository.findAll();
        assertThat(programaList).hasSize(databaseSizeBeforeUpdate);
        Programa testPrograma = programaList.get(programaList.size() - 1);
        assertThat(testPrograma.getCaloriasActividad()).isEqualTo(UPDATED_CALORIAS_ACTIVIDAD);
        assertThat(testPrograma.getPasosActividad()).isEqualTo(UPDATED_PASOS_ACTIVIDAD);
        assertThat(testPrograma.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void patchNonExistingPrograma() throws Exception {
        int databaseSizeBeforeUpdate = programaRepository.findAll().size();
        programa.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgramaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, programa.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(programa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Programa in the database
        List<Programa> programaList = programaRepository.findAll();
        assertThat(programaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrograma() throws Exception {
        int databaseSizeBeforeUpdate = programaRepository.findAll().size();
        programa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(programa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Programa in the database
        List<Programa> programaList = programaRepository.findAll();
        assertThat(programaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrograma() throws Exception {
        int databaseSizeBeforeUpdate = programaRepository.findAll().size();
        programa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(programa))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Programa in the database
        List<Programa> programaList = programaRepository.findAll();
        assertThat(programaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrograma() throws Exception {
        // Initialize the database
        programaRepository.saveAndFlush(programa);

        int databaseSizeBeforeDelete = programaRepository.findAll().size();

        // Delete the programa
        restProgramaMockMvc
            .perform(delete(ENTITY_API_URL_ID, programa.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Programa> programaList = programaRepository.findAll();
        assertThat(programaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

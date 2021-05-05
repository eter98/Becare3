package com.be4tech.becare3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.Alarma;
import com.be4tech.becare3.domain.User;
import com.be4tech.becare3.repository.AlarmaRepository;
import com.be4tech.becare3.service.criteria.AlarmaCriteria;
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
 * Integration tests for the {@link AlarmaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlarmaResourceIT {

    private static final Instant DEFAULT_TIME_INSTANT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME_INSTANT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_PROCEDIMIENTO = "AAAAAAAAAA";
    private static final String UPDATED_PROCEDIMIENTO = "BBBBBBBBBB";

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_VERIFICAR = false;
    private static final Boolean UPDATED_VERIFICAR = true;

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final String DEFAULT_PRIORIDAD = "AAAAAAAAAA";
    private static final String UPDATED_PRIORIDAD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/alarmas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AlarmaRepository alarmaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlarmaMockMvc;

    private Alarma alarma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alarma createEntity(EntityManager em) {
        Alarma alarma = new Alarma()
            .timeInstant(DEFAULT_TIME_INSTANT)
            .descripcion(DEFAULT_DESCRIPCION)
            .procedimiento(DEFAULT_PROCEDIMIENTO)
            .titulo(DEFAULT_TITULO)
            .verificar(DEFAULT_VERIFICAR)
            .observaciones(DEFAULT_OBSERVACIONES)
            .prioridad(DEFAULT_PRIORIDAD);
        return alarma;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alarma createUpdatedEntity(EntityManager em) {
        Alarma alarma = new Alarma()
            .timeInstant(UPDATED_TIME_INSTANT)
            .descripcion(UPDATED_DESCRIPCION)
            .procedimiento(UPDATED_PROCEDIMIENTO)
            .titulo(UPDATED_TITULO)
            .verificar(UPDATED_VERIFICAR)
            .observaciones(UPDATED_OBSERVACIONES)
            .prioridad(UPDATED_PRIORIDAD);
        return alarma;
    }

    @BeforeEach
    public void initTest() {
        alarma = createEntity(em);
    }

    @Test
    @Transactional
    void createAlarma() throws Exception {
        int databaseSizeBeforeCreate = alarmaRepository.findAll().size();
        // Create the Alarma
        restAlarmaMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alarma))
            )
            .andExpect(status().isCreated());

        // Validate the Alarma in the database
        List<Alarma> alarmaList = alarmaRepository.findAll();
        assertThat(alarmaList).hasSize(databaseSizeBeforeCreate + 1);
        Alarma testAlarma = alarmaList.get(alarmaList.size() - 1);
        assertThat(testAlarma.getTimeInstant()).isEqualTo(DEFAULT_TIME_INSTANT);
        assertThat(testAlarma.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testAlarma.getProcedimiento()).isEqualTo(DEFAULT_PROCEDIMIENTO);
        assertThat(testAlarma.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testAlarma.getVerificar()).isEqualTo(DEFAULT_VERIFICAR);
        assertThat(testAlarma.getObservaciones()).isEqualTo(DEFAULT_OBSERVACIONES);
        assertThat(testAlarma.getPrioridad()).isEqualTo(DEFAULT_PRIORIDAD);
    }

    @Test
    @Transactional
    void createAlarmaWithExistingId() throws Exception {
        // Create the Alarma with an existing ID
        alarma.setId(1L);

        int databaseSizeBeforeCreate = alarmaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlarmaMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alarma))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alarma in the database
        List<Alarma> alarmaList = alarmaRepository.findAll();
        assertThat(alarmaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlarmas() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList
        restAlarmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alarma.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeInstant").value(hasItem(DEFAULT_TIME_INSTANT.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].procedimiento").value(hasItem(DEFAULT_PROCEDIMIENTO)))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].verificar").value(hasItem(DEFAULT_VERIFICAR.booleanValue())))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)))
            .andExpect(jsonPath("$.[*].prioridad").value(hasItem(DEFAULT_PRIORIDAD)));
    }

    @Test
    @Transactional
    void getAlarma() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get the alarma
        restAlarmaMockMvc
            .perform(get(ENTITY_API_URL_ID, alarma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alarma.getId().intValue()))
            .andExpect(jsonPath("$.timeInstant").value(DEFAULT_TIME_INSTANT.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.procedimiento").value(DEFAULT_PROCEDIMIENTO))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.verificar").value(DEFAULT_VERIFICAR.booleanValue()))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES))
            .andExpect(jsonPath("$.prioridad").value(DEFAULT_PRIORIDAD));
    }

    @Test
    @Transactional
    void getAlarmasByIdFiltering() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        Long id = alarma.getId();

        defaultAlarmaShouldBeFound("id.equals=" + id);
        defaultAlarmaShouldNotBeFound("id.notEquals=" + id);

        defaultAlarmaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAlarmaShouldNotBeFound("id.greaterThan=" + id);

        defaultAlarmaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAlarmaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlarmasByTimeInstantIsEqualToSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where timeInstant equals to DEFAULT_TIME_INSTANT
        defaultAlarmaShouldBeFound("timeInstant.equals=" + DEFAULT_TIME_INSTANT);

        // Get all the alarmaList where timeInstant equals to UPDATED_TIME_INSTANT
        defaultAlarmaShouldNotBeFound("timeInstant.equals=" + UPDATED_TIME_INSTANT);
    }

    @Test
    @Transactional
    void getAllAlarmasByTimeInstantIsNotEqualToSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where timeInstant not equals to DEFAULT_TIME_INSTANT
        defaultAlarmaShouldNotBeFound("timeInstant.notEquals=" + DEFAULT_TIME_INSTANT);

        // Get all the alarmaList where timeInstant not equals to UPDATED_TIME_INSTANT
        defaultAlarmaShouldBeFound("timeInstant.notEquals=" + UPDATED_TIME_INSTANT);
    }

    @Test
    @Transactional
    void getAllAlarmasByTimeInstantIsInShouldWork() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where timeInstant in DEFAULT_TIME_INSTANT or UPDATED_TIME_INSTANT
        defaultAlarmaShouldBeFound("timeInstant.in=" + DEFAULT_TIME_INSTANT + "," + UPDATED_TIME_INSTANT);

        // Get all the alarmaList where timeInstant equals to UPDATED_TIME_INSTANT
        defaultAlarmaShouldNotBeFound("timeInstant.in=" + UPDATED_TIME_INSTANT);
    }

    @Test
    @Transactional
    void getAllAlarmasByTimeInstantIsNullOrNotNull() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where timeInstant is not null
        defaultAlarmaShouldBeFound("timeInstant.specified=true");

        // Get all the alarmaList where timeInstant is null
        defaultAlarmaShouldNotBeFound("timeInstant.specified=false");
    }

    @Test
    @Transactional
    void getAllAlarmasByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where descripcion equals to DEFAULT_DESCRIPCION
        defaultAlarmaShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the alarmaList where descripcion equals to UPDATED_DESCRIPCION
        defaultAlarmaShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllAlarmasByDescripcionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where descripcion not equals to DEFAULT_DESCRIPCION
        defaultAlarmaShouldNotBeFound("descripcion.notEquals=" + DEFAULT_DESCRIPCION);

        // Get all the alarmaList where descripcion not equals to UPDATED_DESCRIPCION
        defaultAlarmaShouldBeFound("descripcion.notEquals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllAlarmasByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultAlarmaShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the alarmaList where descripcion equals to UPDATED_DESCRIPCION
        defaultAlarmaShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllAlarmasByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where descripcion is not null
        defaultAlarmaShouldBeFound("descripcion.specified=true");

        // Get all the alarmaList where descripcion is null
        defaultAlarmaShouldNotBeFound("descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllAlarmasByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where descripcion contains DEFAULT_DESCRIPCION
        defaultAlarmaShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the alarmaList where descripcion contains UPDATED_DESCRIPCION
        defaultAlarmaShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllAlarmasByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultAlarmaShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the alarmaList where descripcion does not contain UPDATED_DESCRIPCION
        defaultAlarmaShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllAlarmasByProcedimientoIsEqualToSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where procedimiento equals to DEFAULT_PROCEDIMIENTO
        defaultAlarmaShouldBeFound("procedimiento.equals=" + DEFAULT_PROCEDIMIENTO);

        // Get all the alarmaList where procedimiento equals to UPDATED_PROCEDIMIENTO
        defaultAlarmaShouldNotBeFound("procedimiento.equals=" + UPDATED_PROCEDIMIENTO);
    }

    @Test
    @Transactional
    void getAllAlarmasByProcedimientoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where procedimiento not equals to DEFAULT_PROCEDIMIENTO
        defaultAlarmaShouldNotBeFound("procedimiento.notEquals=" + DEFAULT_PROCEDIMIENTO);

        // Get all the alarmaList where procedimiento not equals to UPDATED_PROCEDIMIENTO
        defaultAlarmaShouldBeFound("procedimiento.notEquals=" + UPDATED_PROCEDIMIENTO);
    }

    @Test
    @Transactional
    void getAllAlarmasByProcedimientoIsInShouldWork() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where procedimiento in DEFAULT_PROCEDIMIENTO or UPDATED_PROCEDIMIENTO
        defaultAlarmaShouldBeFound("procedimiento.in=" + DEFAULT_PROCEDIMIENTO + "," + UPDATED_PROCEDIMIENTO);

        // Get all the alarmaList where procedimiento equals to UPDATED_PROCEDIMIENTO
        defaultAlarmaShouldNotBeFound("procedimiento.in=" + UPDATED_PROCEDIMIENTO);
    }

    @Test
    @Transactional
    void getAllAlarmasByProcedimientoIsNullOrNotNull() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where procedimiento is not null
        defaultAlarmaShouldBeFound("procedimiento.specified=true");

        // Get all the alarmaList where procedimiento is null
        defaultAlarmaShouldNotBeFound("procedimiento.specified=false");
    }

    @Test
    @Transactional
    void getAllAlarmasByProcedimientoContainsSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where procedimiento contains DEFAULT_PROCEDIMIENTO
        defaultAlarmaShouldBeFound("procedimiento.contains=" + DEFAULT_PROCEDIMIENTO);

        // Get all the alarmaList where procedimiento contains UPDATED_PROCEDIMIENTO
        defaultAlarmaShouldNotBeFound("procedimiento.contains=" + UPDATED_PROCEDIMIENTO);
    }

    @Test
    @Transactional
    void getAllAlarmasByProcedimientoNotContainsSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where procedimiento does not contain DEFAULT_PROCEDIMIENTO
        defaultAlarmaShouldNotBeFound("procedimiento.doesNotContain=" + DEFAULT_PROCEDIMIENTO);

        // Get all the alarmaList where procedimiento does not contain UPDATED_PROCEDIMIENTO
        defaultAlarmaShouldBeFound("procedimiento.doesNotContain=" + UPDATED_PROCEDIMIENTO);
    }

    @Test
    @Transactional
    void getAllAlarmasByTituloIsEqualToSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where titulo equals to DEFAULT_TITULO
        defaultAlarmaShouldBeFound("titulo.equals=" + DEFAULT_TITULO);

        // Get all the alarmaList where titulo equals to UPDATED_TITULO
        defaultAlarmaShouldNotBeFound("titulo.equals=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllAlarmasByTituloIsNotEqualToSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where titulo not equals to DEFAULT_TITULO
        defaultAlarmaShouldNotBeFound("titulo.notEquals=" + DEFAULT_TITULO);

        // Get all the alarmaList where titulo not equals to UPDATED_TITULO
        defaultAlarmaShouldBeFound("titulo.notEquals=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllAlarmasByTituloIsInShouldWork() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where titulo in DEFAULT_TITULO or UPDATED_TITULO
        defaultAlarmaShouldBeFound("titulo.in=" + DEFAULT_TITULO + "," + UPDATED_TITULO);

        // Get all the alarmaList where titulo equals to UPDATED_TITULO
        defaultAlarmaShouldNotBeFound("titulo.in=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllAlarmasByTituloIsNullOrNotNull() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where titulo is not null
        defaultAlarmaShouldBeFound("titulo.specified=true");

        // Get all the alarmaList where titulo is null
        defaultAlarmaShouldNotBeFound("titulo.specified=false");
    }

    @Test
    @Transactional
    void getAllAlarmasByTituloContainsSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where titulo contains DEFAULT_TITULO
        defaultAlarmaShouldBeFound("titulo.contains=" + DEFAULT_TITULO);

        // Get all the alarmaList where titulo contains UPDATED_TITULO
        defaultAlarmaShouldNotBeFound("titulo.contains=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllAlarmasByTituloNotContainsSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where titulo does not contain DEFAULT_TITULO
        defaultAlarmaShouldNotBeFound("titulo.doesNotContain=" + DEFAULT_TITULO);

        // Get all the alarmaList where titulo does not contain UPDATED_TITULO
        defaultAlarmaShouldBeFound("titulo.doesNotContain=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllAlarmasByVerificarIsEqualToSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where verificar equals to DEFAULT_VERIFICAR
        defaultAlarmaShouldBeFound("verificar.equals=" + DEFAULT_VERIFICAR);

        // Get all the alarmaList where verificar equals to UPDATED_VERIFICAR
        defaultAlarmaShouldNotBeFound("verificar.equals=" + UPDATED_VERIFICAR);
    }

    @Test
    @Transactional
    void getAllAlarmasByVerificarIsNotEqualToSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where verificar not equals to DEFAULT_VERIFICAR
        defaultAlarmaShouldNotBeFound("verificar.notEquals=" + DEFAULT_VERIFICAR);

        // Get all the alarmaList where verificar not equals to UPDATED_VERIFICAR
        defaultAlarmaShouldBeFound("verificar.notEquals=" + UPDATED_VERIFICAR);
    }

    @Test
    @Transactional
    void getAllAlarmasByVerificarIsInShouldWork() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where verificar in DEFAULT_VERIFICAR or UPDATED_VERIFICAR
        defaultAlarmaShouldBeFound("verificar.in=" + DEFAULT_VERIFICAR + "," + UPDATED_VERIFICAR);

        // Get all the alarmaList where verificar equals to UPDATED_VERIFICAR
        defaultAlarmaShouldNotBeFound("verificar.in=" + UPDATED_VERIFICAR);
    }

    @Test
    @Transactional
    void getAllAlarmasByVerificarIsNullOrNotNull() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where verificar is not null
        defaultAlarmaShouldBeFound("verificar.specified=true");

        // Get all the alarmaList where verificar is null
        defaultAlarmaShouldNotBeFound("verificar.specified=false");
    }

    @Test
    @Transactional
    void getAllAlarmasByObservacionesIsEqualToSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where observaciones equals to DEFAULT_OBSERVACIONES
        defaultAlarmaShouldBeFound("observaciones.equals=" + DEFAULT_OBSERVACIONES);

        // Get all the alarmaList where observaciones equals to UPDATED_OBSERVACIONES
        defaultAlarmaShouldNotBeFound("observaciones.equals=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllAlarmasByObservacionesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where observaciones not equals to DEFAULT_OBSERVACIONES
        defaultAlarmaShouldNotBeFound("observaciones.notEquals=" + DEFAULT_OBSERVACIONES);

        // Get all the alarmaList where observaciones not equals to UPDATED_OBSERVACIONES
        defaultAlarmaShouldBeFound("observaciones.notEquals=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllAlarmasByObservacionesIsInShouldWork() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where observaciones in DEFAULT_OBSERVACIONES or UPDATED_OBSERVACIONES
        defaultAlarmaShouldBeFound("observaciones.in=" + DEFAULT_OBSERVACIONES + "," + UPDATED_OBSERVACIONES);

        // Get all the alarmaList where observaciones equals to UPDATED_OBSERVACIONES
        defaultAlarmaShouldNotBeFound("observaciones.in=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllAlarmasByObservacionesIsNullOrNotNull() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where observaciones is not null
        defaultAlarmaShouldBeFound("observaciones.specified=true");

        // Get all the alarmaList where observaciones is null
        defaultAlarmaShouldNotBeFound("observaciones.specified=false");
    }

    @Test
    @Transactional
    void getAllAlarmasByObservacionesContainsSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where observaciones contains DEFAULT_OBSERVACIONES
        defaultAlarmaShouldBeFound("observaciones.contains=" + DEFAULT_OBSERVACIONES);

        // Get all the alarmaList where observaciones contains UPDATED_OBSERVACIONES
        defaultAlarmaShouldNotBeFound("observaciones.contains=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllAlarmasByObservacionesNotContainsSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where observaciones does not contain DEFAULT_OBSERVACIONES
        defaultAlarmaShouldNotBeFound("observaciones.doesNotContain=" + DEFAULT_OBSERVACIONES);

        // Get all the alarmaList where observaciones does not contain UPDATED_OBSERVACIONES
        defaultAlarmaShouldBeFound("observaciones.doesNotContain=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllAlarmasByPrioridadIsEqualToSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where prioridad equals to DEFAULT_PRIORIDAD
        defaultAlarmaShouldBeFound("prioridad.equals=" + DEFAULT_PRIORIDAD);

        // Get all the alarmaList where prioridad equals to UPDATED_PRIORIDAD
        defaultAlarmaShouldNotBeFound("prioridad.equals=" + UPDATED_PRIORIDAD);
    }

    @Test
    @Transactional
    void getAllAlarmasByPrioridadIsNotEqualToSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where prioridad not equals to DEFAULT_PRIORIDAD
        defaultAlarmaShouldNotBeFound("prioridad.notEquals=" + DEFAULT_PRIORIDAD);

        // Get all the alarmaList where prioridad not equals to UPDATED_PRIORIDAD
        defaultAlarmaShouldBeFound("prioridad.notEquals=" + UPDATED_PRIORIDAD);
    }

    @Test
    @Transactional
    void getAllAlarmasByPrioridadIsInShouldWork() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where prioridad in DEFAULT_PRIORIDAD or UPDATED_PRIORIDAD
        defaultAlarmaShouldBeFound("prioridad.in=" + DEFAULT_PRIORIDAD + "," + UPDATED_PRIORIDAD);

        // Get all the alarmaList where prioridad equals to UPDATED_PRIORIDAD
        defaultAlarmaShouldNotBeFound("prioridad.in=" + UPDATED_PRIORIDAD);
    }

    @Test
    @Transactional
    void getAllAlarmasByPrioridadIsNullOrNotNull() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where prioridad is not null
        defaultAlarmaShouldBeFound("prioridad.specified=true");

        // Get all the alarmaList where prioridad is null
        defaultAlarmaShouldNotBeFound("prioridad.specified=false");
    }

    @Test
    @Transactional
    void getAllAlarmasByPrioridadContainsSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where prioridad contains DEFAULT_PRIORIDAD
        defaultAlarmaShouldBeFound("prioridad.contains=" + DEFAULT_PRIORIDAD);

        // Get all the alarmaList where prioridad contains UPDATED_PRIORIDAD
        defaultAlarmaShouldNotBeFound("prioridad.contains=" + UPDATED_PRIORIDAD);
    }

    @Test
    @Transactional
    void getAllAlarmasByPrioridadNotContainsSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        // Get all the alarmaList where prioridad does not contain DEFAULT_PRIORIDAD
        defaultAlarmaShouldNotBeFound("prioridad.doesNotContain=" + DEFAULT_PRIORIDAD);

        // Get all the alarmaList where prioridad does not contain UPDATED_PRIORIDAD
        defaultAlarmaShouldBeFound("prioridad.doesNotContain=" + UPDATED_PRIORIDAD);
    }

    @Test
    @Transactional
    void getAllAlarmasByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        alarma.setUser(user);
        alarmaRepository.saveAndFlush(alarma);
        String userId = user.getId();

        // Get all the alarmaList where user equals to userId
        defaultAlarmaShouldBeFound("userId.equals=" + userId);

        // Get all the alarmaList where user equals to "invalid-id"
        defaultAlarmaShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlarmaShouldBeFound(String filter) throws Exception {
        restAlarmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alarma.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeInstant").value(hasItem(DEFAULT_TIME_INSTANT.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].procedimiento").value(hasItem(DEFAULT_PROCEDIMIENTO)))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].verificar").value(hasItem(DEFAULT_VERIFICAR.booleanValue())))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)))
            .andExpect(jsonPath("$.[*].prioridad").value(hasItem(DEFAULT_PRIORIDAD)));

        // Check, that the count call also returns 1
        restAlarmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlarmaShouldNotBeFound(String filter) throws Exception {
        restAlarmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlarmaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlarma() throws Exception {
        // Get the alarma
        restAlarmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAlarma() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        int databaseSizeBeforeUpdate = alarmaRepository.findAll().size();

        // Update the alarma
        Alarma updatedAlarma = alarmaRepository.findById(alarma.getId()).get();
        // Disconnect from session so that the updates on updatedAlarma are not directly saved in db
        em.detach(updatedAlarma);
        updatedAlarma
            .timeInstant(UPDATED_TIME_INSTANT)
            .descripcion(UPDATED_DESCRIPCION)
            .procedimiento(UPDATED_PROCEDIMIENTO)
            .titulo(UPDATED_TITULO)
            .verificar(UPDATED_VERIFICAR)
            .observaciones(UPDATED_OBSERVACIONES)
            .prioridad(UPDATED_PRIORIDAD);

        restAlarmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlarma.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAlarma))
            )
            .andExpect(status().isOk());

        // Validate the Alarma in the database
        List<Alarma> alarmaList = alarmaRepository.findAll();
        assertThat(alarmaList).hasSize(databaseSizeBeforeUpdate);
        Alarma testAlarma = alarmaList.get(alarmaList.size() - 1);
        assertThat(testAlarma.getTimeInstant()).isEqualTo(UPDATED_TIME_INSTANT);
        assertThat(testAlarma.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testAlarma.getProcedimiento()).isEqualTo(UPDATED_PROCEDIMIENTO);
        assertThat(testAlarma.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testAlarma.getVerificar()).isEqualTo(UPDATED_VERIFICAR);
        assertThat(testAlarma.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
        assertThat(testAlarma.getPrioridad()).isEqualTo(UPDATED_PRIORIDAD);
    }

    @Test
    @Transactional
    void putNonExistingAlarma() throws Exception {
        int databaseSizeBeforeUpdate = alarmaRepository.findAll().size();
        alarma.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlarmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alarma.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alarma))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alarma in the database
        List<Alarma> alarmaList = alarmaRepository.findAll();
        assertThat(alarmaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlarma() throws Exception {
        int databaseSizeBeforeUpdate = alarmaRepository.findAll().size();
        alarma.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlarmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(alarma))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alarma in the database
        List<Alarma> alarmaList = alarmaRepository.findAll();
        assertThat(alarmaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlarma() throws Exception {
        int databaseSizeBeforeUpdate = alarmaRepository.findAll().size();
        alarma.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlarmaMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(alarma))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alarma in the database
        List<Alarma> alarmaList = alarmaRepository.findAll();
        assertThat(alarmaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlarmaWithPatch() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        int databaseSizeBeforeUpdate = alarmaRepository.findAll().size();

        // Update the alarma using partial update
        Alarma partialUpdatedAlarma = new Alarma();
        partialUpdatedAlarma.setId(alarma.getId());

        partialUpdatedAlarma.procedimiento(UPDATED_PROCEDIMIENTO).prioridad(UPDATED_PRIORIDAD);

        restAlarmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlarma.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlarma))
            )
            .andExpect(status().isOk());

        // Validate the Alarma in the database
        List<Alarma> alarmaList = alarmaRepository.findAll();
        assertThat(alarmaList).hasSize(databaseSizeBeforeUpdate);
        Alarma testAlarma = alarmaList.get(alarmaList.size() - 1);
        assertThat(testAlarma.getTimeInstant()).isEqualTo(DEFAULT_TIME_INSTANT);
        assertThat(testAlarma.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testAlarma.getProcedimiento()).isEqualTo(UPDATED_PROCEDIMIENTO);
        assertThat(testAlarma.getTitulo()).isEqualTo(DEFAULT_TITULO);
        assertThat(testAlarma.getVerificar()).isEqualTo(DEFAULT_VERIFICAR);
        assertThat(testAlarma.getObservaciones()).isEqualTo(DEFAULT_OBSERVACIONES);
        assertThat(testAlarma.getPrioridad()).isEqualTo(UPDATED_PRIORIDAD);
    }

    @Test
    @Transactional
    void fullUpdateAlarmaWithPatch() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        int databaseSizeBeforeUpdate = alarmaRepository.findAll().size();

        // Update the alarma using partial update
        Alarma partialUpdatedAlarma = new Alarma();
        partialUpdatedAlarma.setId(alarma.getId());

        partialUpdatedAlarma
            .timeInstant(UPDATED_TIME_INSTANT)
            .descripcion(UPDATED_DESCRIPCION)
            .procedimiento(UPDATED_PROCEDIMIENTO)
            .titulo(UPDATED_TITULO)
            .verificar(UPDATED_VERIFICAR)
            .observaciones(UPDATED_OBSERVACIONES)
            .prioridad(UPDATED_PRIORIDAD);

        restAlarmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlarma.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAlarma))
            )
            .andExpect(status().isOk());

        // Validate the Alarma in the database
        List<Alarma> alarmaList = alarmaRepository.findAll();
        assertThat(alarmaList).hasSize(databaseSizeBeforeUpdate);
        Alarma testAlarma = alarmaList.get(alarmaList.size() - 1);
        assertThat(testAlarma.getTimeInstant()).isEqualTo(UPDATED_TIME_INSTANT);
        assertThat(testAlarma.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testAlarma.getProcedimiento()).isEqualTo(UPDATED_PROCEDIMIENTO);
        assertThat(testAlarma.getTitulo()).isEqualTo(UPDATED_TITULO);
        assertThat(testAlarma.getVerificar()).isEqualTo(UPDATED_VERIFICAR);
        assertThat(testAlarma.getObservaciones()).isEqualTo(UPDATED_OBSERVACIONES);
        assertThat(testAlarma.getPrioridad()).isEqualTo(UPDATED_PRIORIDAD);
    }

    @Test
    @Transactional
    void patchNonExistingAlarma() throws Exception {
        int databaseSizeBeforeUpdate = alarmaRepository.findAll().size();
        alarma.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlarmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alarma.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(alarma))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alarma in the database
        List<Alarma> alarmaList = alarmaRepository.findAll();
        assertThat(alarmaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlarma() throws Exception {
        int databaseSizeBeforeUpdate = alarmaRepository.findAll().size();
        alarma.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlarmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(alarma))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alarma in the database
        List<Alarma> alarmaList = alarmaRepository.findAll();
        assertThat(alarmaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlarma() throws Exception {
        int databaseSizeBeforeUpdate = alarmaRepository.findAll().size();
        alarma.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlarmaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(alarma))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alarma in the database
        List<Alarma> alarmaList = alarmaRepository.findAll();
        assertThat(alarmaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlarma() throws Exception {
        // Initialize the database
        alarmaRepository.saveAndFlush(alarma);

        int databaseSizeBeforeDelete = alarmaRepository.findAll().size();

        // Delete the alarma
        restAlarmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, alarma.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Alarma> alarmaList = alarmaRepository.findAll();
        assertThat(alarmaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

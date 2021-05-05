package com.be4tech.becare3.web.rest;

import static com.be4tech.becare3.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.Notificacion;
import com.be4tech.becare3.domain.TokenDisp;
import com.be4tech.becare3.repository.NotificacionRepository;
import com.be4tech.becare3.service.criteria.NotificacionCriteria;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link NotificacionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NotificacionResourceIT {

    private static final Instant DEFAULT_FECHA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ZonedDateTime DEFAULT_FECHA_ACTUALIZACION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_ACTUALIZACION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_FECHA_ACTUALIZACION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_ESTADO = 1;
    private static final Integer UPDATED_ESTADO = 2;
    private static final Integer SMALLER_ESTADO = 1 - 1;

    private static final Integer DEFAULT_TIPO_NOTIFICACION = 1;
    private static final Integer UPDATED_TIPO_NOTIFICACION = 2;
    private static final Integer SMALLER_TIPO_NOTIFICACION = 1 - 1;

    private static final String ENTITY_API_URL = "/api/notificacions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificacionMockMvc;

    private Notificacion notificacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notificacion createEntity(EntityManager em) {
        Notificacion notificacion = new Notificacion()
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .fechaActualizacion(DEFAULT_FECHA_ACTUALIZACION)
            .estado(DEFAULT_ESTADO)
            .tipoNotificacion(DEFAULT_TIPO_NOTIFICACION);
        return notificacion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notificacion createUpdatedEntity(EntityManager em) {
        Notificacion notificacion = new Notificacion()
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaActualizacion(UPDATED_FECHA_ACTUALIZACION)
            .estado(UPDATED_ESTADO)
            .tipoNotificacion(UPDATED_TIPO_NOTIFICACION);
        return notificacion;
    }

    @BeforeEach
    public void initTest() {
        notificacion = createEntity(em);
    }

    @Test
    @Transactional
    void createNotificacion() throws Exception {
        int databaseSizeBeforeCreate = notificacionRepository.findAll().size();
        // Create the Notificacion
        restNotificacionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificacion))
            )
            .andExpect(status().isCreated());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeCreate + 1);
        Notificacion testNotificacion = notificacionList.get(notificacionList.size() - 1);
        assertThat(testNotificacion.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testNotificacion.getFechaActualizacion()).isEqualTo(DEFAULT_FECHA_ACTUALIZACION);
        assertThat(testNotificacion.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testNotificacion.getTipoNotificacion()).isEqualTo(DEFAULT_TIPO_NOTIFICACION);
    }

    @Test
    @Transactional
    void createNotificacionWithExistingId() throws Exception {
        // Create the Notificacion with an existing ID
        notificacion.setId(1L);

        int databaseSizeBeforeCreate = notificacionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificacionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNotificacions() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList
        restNotificacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaActualizacion").value(hasItem(sameInstant(DEFAULT_FECHA_ACTUALIZACION))))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].tipoNotificacion").value(hasItem(DEFAULT_TIPO_NOTIFICACION)));
    }

    @Test
    @Transactional
    void getNotificacion() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get the notificacion
        restNotificacionMockMvc
            .perform(get(ENTITY_API_URL_ID, notificacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notificacion.getId().intValue()))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaActualizacion").value(sameInstant(DEFAULT_FECHA_ACTUALIZACION)))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.tipoNotificacion").value(DEFAULT_TIPO_NOTIFICACION));
    }

    @Test
    @Transactional
    void getNotificacionsByIdFiltering() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        Long id = notificacion.getId();

        defaultNotificacionShouldBeFound("id.equals=" + id);
        defaultNotificacionShouldNotBeFound("id.notEquals=" + id);

        defaultNotificacionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNotificacionShouldNotBeFound("id.greaterThan=" + id);

        defaultNotificacionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNotificacionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNotificacionsByFechaInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where fechaInicio equals to DEFAULT_FECHA_INICIO
        defaultNotificacionShouldBeFound("fechaInicio.equals=" + DEFAULT_FECHA_INICIO);

        // Get all the notificacionList where fechaInicio equals to UPDATED_FECHA_INICIO
        defaultNotificacionShouldNotBeFound("fechaInicio.equals=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllNotificacionsByFechaInicioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where fechaInicio not equals to DEFAULT_FECHA_INICIO
        defaultNotificacionShouldNotBeFound("fechaInicio.notEquals=" + DEFAULT_FECHA_INICIO);

        // Get all the notificacionList where fechaInicio not equals to UPDATED_FECHA_INICIO
        defaultNotificacionShouldBeFound("fechaInicio.notEquals=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllNotificacionsByFechaInicioIsInShouldWork() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where fechaInicio in DEFAULT_FECHA_INICIO or UPDATED_FECHA_INICIO
        defaultNotificacionShouldBeFound("fechaInicio.in=" + DEFAULT_FECHA_INICIO + "," + UPDATED_FECHA_INICIO);

        // Get all the notificacionList where fechaInicio equals to UPDATED_FECHA_INICIO
        defaultNotificacionShouldNotBeFound("fechaInicio.in=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllNotificacionsByFechaInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where fechaInicio is not null
        defaultNotificacionShouldBeFound("fechaInicio.specified=true");

        // Get all the notificacionList where fechaInicio is null
        defaultNotificacionShouldNotBeFound("fechaInicio.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificacionsByFechaActualizacionIsEqualToSomething() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where fechaActualizacion equals to DEFAULT_FECHA_ACTUALIZACION
        defaultNotificacionShouldBeFound("fechaActualizacion.equals=" + DEFAULT_FECHA_ACTUALIZACION);

        // Get all the notificacionList where fechaActualizacion equals to UPDATED_FECHA_ACTUALIZACION
        defaultNotificacionShouldNotBeFound("fechaActualizacion.equals=" + UPDATED_FECHA_ACTUALIZACION);
    }

    @Test
    @Transactional
    void getAllNotificacionsByFechaActualizacionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where fechaActualizacion not equals to DEFAULT_FECHA_ACTUALIZACION
        defaultNotificacionShouldNotBeFound("fechaActualizacion.notEquals=" + DEFAULT_FECHA_ACTUALIZACION);

        // Get all the notificacionList where fechaActualizacion not equals to UPDATED_FECHA_ACTUALIZACION
        defaultNotificacionShouldBeFound("fechaActualizacion.notEquals=" + UPDATED_FECHA_ACTUALIZACION);
    }

    @Test
    @Transactional
    void getAllNotificacionsByFechaActualizacionIsInShouldWork() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where fechaActualizacion in DEFAULT_FECHA_ACTUALIZACION or UPDATED_FECHA_ACTUALIZACION
        defaultNotificacionShouldBeFound("fechaActualizacion.in=" + DEFAULT_FECHA_ACTUALIZACION + "," + UPDATED_FECHA_ACTUALIZACION);

        // Get all the notificacionList where fechaActualizacion equals to UPDATED_FECHA_ACTUALIZACION
        defaultNotificacionShouldNotBeFound("fechaActualizacion.in=" + UPDATED_FECHA_ACTUALIZACION);
    }

    @Test
    @Transactional
    void getAllNotificacionsByFechaActualizacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where fechaActualizacion is not null
        defaultNotificacionShouldBeFound("fechaActualizacion.specified=true");

        // Get all the notificacionList where fechaActualizacion is null
        defaultNotificacionShouldNotBeFound("fechaActualizacion.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificacionsByFechaActualizacionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where fechaActualizacion is greater than or equal to DEFAULT_FECHA_ACTUALIZACION
        defaultNotificacionShouldBeFound("fechaActualizacion.greaterThanOrEqual=" + DEFAULT_FECHA_ACTUALIZACION);

        // Get all the notificacionList where fechaActualizacion is greater than or equal to UPDATED_FECHA_ACTUALIZACION
        defaultNotificacionShouldNotBeFound("fechaActualizacion.greaterThanOrEqual=" + UPDATED_FECHA_ACTUALIZACION);
    }

    @Test
    @Transactional
    void getAllNotificacionsByFechaActualizacionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where fechaActualizacion is less than or equal to DEFAULT_FECHA_ACTUALIZACION
        defaultNotificacionShouldBeFound("fechaActualizacion.lessThanOrEqual=" + DEFAULT_FECHA_ACTUALIZACION);

        // Get all the notificacionList where fechaActualizacion is less than or equal to SMALLER_FECHA_ACTUALIZACION
        defaultNotificacionShouldNotBeFound("fechaActualizacion.lessThanOrEqual=" + SMALLER_FECHA_ACTUALIZACION);
    }

    @Test
    @Transactional
    void getAllNotificacionsByFechaActualizacionIsLessThanSomething() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where fechaActualizacion is less than DEFAULT_FECHA_ACTUALIZACION
        defaultNotificacionShouldNotBeFound("fechaActualizacion.lessThan=" + DEFAULT_FECHA_ACTUALIZACION);

        // Get all the notificacionList where fechaActualizacion is less than UPDATED_FECHA_ACTUALIZACION
        defaultNotificacionShouldBeFound("fechaActualizacion.lessThan=" + UPDATED_FECHA_ACTUALIZACION);
    }

    @Test
    @Transactional
    void getAllNotificacionsByFechaActualizacionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where fechaActualizacion is greater than DEFAULT_FECHA_ACTUALIZACION
        defaultNotificacionShouldNotBeFound("fechaActualizacion.greaterThan=" + DEFAULT_FECHA_ACTUALIZACION);

        // Get all the notificacionList where fechaActualizacion is greater than SMALLER_FECHA_ACTUALIZACION
        defaultNotificacionShouldBeFound("fechaActualizacion.greaterThan=" + SMALLER_FECHA_ACTUALIZACION);
    }

    @Test
    @Transactional
    void getAllNotificacionsByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where estado equals to DEFAULT_ESTADO
        defaultNotificacionShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the notificacionList where estado equals to UPDATED_ESTADO
        defaultNotificacionShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllNotificacionsByEstadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where estado not equals to DEFAULT_ESTADO
        defaultNotificacionShouldNotBeFound("estado.notEquals=" + DEFAULT_ESTADO);

        // Get all the notificacionList where estado not equals to UPDATED_ESTADO
        defaultNotificacionShouldBeFound("estado.notEquals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllNotificacionsByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultNotificacionShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the notificacionList where estado equals to UPDATED_ESTADO
        defaultNotificacionShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllNotificacionsByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where estado is not null
        defaultNotificacionShouldBeFound("estado.specified=true");

        // Get all the notificacionList where estado is null
        defaultNotificacionShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificacionsByEstadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where estado is greater than or equal to DEFAULT_ESTADO
        defaultNotificacionShouldBeFound("estado.greaterThanOrEqual=" + DEFAULT_ESTADO);

        // Get all the notificacionList where estado is greater than or equal to UPDATED_ESTADO
        defaultNotificacionShouldNotBeFound("estado.greaterThanOrEqual=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllNotificacionsByEstadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where estado is less than or equal to DEFAULT_ESTADO
        defaultNotificacionShouldBeFound("estado.lessThanOrEqual=" + DEFAULT_ESTADO);

        // Get all the notificacionList where estado is less than or equal to SMALLER_ESTADO
        defaultNotificacionShouldNotBeFound("estado.lessThanOrEqual=" + SMALLER_ESTADO);
    }

    @Test
    @Transactional
    void getAllNotificacionsByEstadoIsLessThanSomething() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where estado is less than DEFAULT_ESTADO
        defaultNotificacionShouldNotBeFound("estado.lessThan=" + DEFAULT_ESTADO);

        // Get all the notificacionList where estado is less than UPDATED_ESTADO
        defaultNotificacionShouldBeFound("estado.lessThan=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllNotificacionsByEstadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where estado is greater than DEFAULT_ESTADO
        defaultNotificacionShouldNotBeFound("estado.greaterThan=" + DEFAULT_ESTADO);

        // Get all the notificacionList where estado is greater than SMALLER_ESTADO
        defaultNotificacionShouldBeFound("estado.greaterThan=" + SMALLER_ESTADO);
    }

    @Test
    @Transactional
    void getAllNotificacionsByTipoNotificacionIsEqualToSomething() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where tipoNotificacion equals to DEFAULT_TIPO_NOTIFICACION
        defaultNotificacionShouldBeFound("tipoNotificacion.equals=" + DEFAULT_TIPO_NOTIFICACION);

        // Get all the notificacionList where tipoNotificacion equals to UPDATED_TIPO_NOTIFICACION
        defaultNotificacionShouldNotBeFound("tipoNotificacion.equals=" + UPDATED_TIPO_NOTIFICACION);
    }

    @Test
    @Transactional
    void getAllNotificacionsByTipoNotificacionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where tipoNotificacion not equals to DEFAULT_TIPO_NOTIFICACION
        defaultNotificacionShouldNotBeFound("tipoNotificacion.notEquals=" + DEFAULT_TIPO_NOTIFICACION);

        // Get all the notificacionList where tipoNotificacion not equals to UPDATED_TIPO_NOTIFICACION
        defaultNotificacionShouldBeFound("tipoNotificacion.notEquals=" + UPDATED_TIPO_NOTIFICACION);
    }

    @Test
    @Transactional
    void getAllNotificacionsByTipoNotificacionIsInShouldWork() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where tipoNotificacion in DEFAULT_TIPO_NOTIFICACION or UPDATED_TIPO_NOTIFICACION
        defaultNotificacionShouldBeFound("tipoNotificacion.in=" + DEFAULT_TIPO_NOTIFICACION + "," + UPDATED_TIPO_NOTIFICACION);

        // Get all the notificacionList where tipoNotificacion equals to UPDATED_TIPO_NOTIFICACION
        defaultNotificacionShouldNotBeFound("tipoNotificacion.in=" + UPDATED_TIPO_NOTIFICACION);
    }

    @Test
    @Transactional
    void getAllNotificacionsByTipoNotificacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where tipoNotificacion is not null
        defaultNotificacionShouldBeFound("tipoNotificacion.specified=true");

        // Get all the notificacionList where tipoNotificacion is null
        defaultNotificacionShouldNotBeFound("tipoNotificacion.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificacionsByTipoNotificacionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where tipoNotificacion is greater than or equal to DEFAULT_TIPO_NOTIFICACION
        defaultNotificacionShouldBeFound("tipoNotificacion.greaterThanOrEqual=" + DEFAULT_TIPO_NOTIFICACION);

        // Get all the notificacionList where tipoNotificacion is greater than or equal to UPDATED_TIPO_NOTIFICACION
        defaultNotificacionShouldNotBeFound("tipoNotificacion.greaterThanOrEqual=" + UPDATED_TIPO_NOTIFICACION);
    }

    @Test
    @Transactional
    void getAllNotificacionsByTipoNotificacionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where tipoNotificacion is less than or equal to DEFAULT_TIPO_NOTIFICACION
        defaultNotificacionShouldBeFound("tipoNotificacion.lessThanOrEqual=" + DEFAULT_TIPO_NOTIFICACION);

        // Get all the notificacionList where tipoNotificacion is less than or equal to SMALLER_TIPO_NOTIFICACION
        defaultNotificacionShouldNotBeFound("tipoNotificacion.lessThanOrEqual=" + SMALLER_TIPO_NOTIFICACION);
    }

    @Test
    @Transactional
    void getAllNotificacionsByTipoNotificacionIsLessThanSomething() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where tipoNotificacion is less than DEFAULT_TIPO_NOTIFICACION
        defaultNotificacionShouldNotBeFound("tipoNotificacion.lessThan=" + DEFAULT_TIPO_NOTIFICACION);

        // Get all the notificacionList where tipoNotificacion is less than UPDATED_TIPO_NOTIFICACION
        defaultNotificacionShouldBeFound("tipoNotificacion.lessThan=" + UPDATED_TIPO_NOTIFICACION);
    }

    @Test
    @Transactional
    void getAllNotificacionsByTipoNotificacionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList where tipoNotificacion is greater than DEFAULT_TIPO_NOTIFICACION
        defaultNotificacionShouldNotBeFound("tipoNotificacion.greaterThan=" + DEFAULT_TIPO_NOTIFICACION);

        // Get all the notificacionList where tipoNotificacion is greater than SMALLER_TIPO_NOTIFICACION
        defaultNotificacionShouldBeFound("tipoNotificacion.greaterThan=" + SMALLER_TIPO_NOTIFICACION);
    }

    @Test
    @Transactional
    void getAllNotificacionsByTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);
        TokenDisp token = TokenDispResourceIT.createEntity(em);
        em.persist(token);
        em.flush();
        notificacion.setToken(token);
        notificacionRepository.saveAndFlush(notificacion);
        Long tokenId = token.getId();

        // Get all the notificacionList where token equals to tokenId
        defaultNotificacionShouldBeFound("tokenId.equals=" + tokenId);

        // Get all the notificacionList where token equals to (tokenId + 1)
        defaultNotificacionShouldNotBeFound("tokenId.equals=" + (tokenId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotificacionShouldBeFound(String filter) throws Exception {
        restNotificacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaActualizacion").value(hasItem(sameInstant(DEFAULT_FECHA_ACTUALIZACION))))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].tipoNotificacion").value(hasItem(DEFAULT_TIPO_NOTIFICACION)));

        // Check, that the count call also returns 1
        restNotificacionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotificacionShouldNotBeFound(String filter) throws Exception {
        restNotificacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotificacionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNotificacion() throws Exception {
        // Get the notificacion
        restNotificacionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNotificacion() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();

        // Update the notificacion
        Notificacion updatedNotificacion = notificacionRepository.findById(notificacion.getId()).get();
        // Disconnect from session so that the updates on updatedNotificacion are not directly saved in db
        em.detach(updatedNotificacion);
        updatedNotificacion
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaActualizacion(UPDATED_FECHA_ACTUALIZACION)
            .estado(UPDATED_ESTADO)
            .tipoNotificacion(UPDATED_TIPO_NOTIFICACION);

        restNotificacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNotificacion.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedNotificacion))
            )
            .andExpect(status().isOk());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
        Notificacion testNotificacion = notificacionList.get(notificacionList.size() - 1);
        assertThat(testNotificacion.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testNotificacion.getFechaActualizacion()).isEqualTo(UPDATED_FECHA_ACTUALIZACION);
        assertThat(testNotificacion.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testNotificacion.getTipoNotificacion()).isEqualTo(UPDATED_TIPO_NOTIFICACION);
    }

    @Test
    @Transactional
    void putNonExistingNotificacion() throws Exception {
        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();
        notificacion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificacion.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotificacion() throws Exception {
        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();
        notificacion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotificacion() throws Exception {
        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();
        notificacion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificacionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificacion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotificacionWithPatch() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();

        // Update the notificacion using partial update
        Notificacion partialUpdatedNotificacion = new Notificacion();
        partialUpdatedNotificacion.setId(notificacion.getId());

        partialUpdatedNotificacion.fechaInicio(UPDATED_FECHA_INICIO).tipoNotificacion(UPDATED_TIPO_NOTIFICACION);

        restNotificacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificacion.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotificacion))
            )
            .andExpect(status().isOk());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
        Notificacion testNotificacion = notificacionList.get(notificacionList.size() - 1);
        assertThat(testNotificacion.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testNotificacion.getFechaActualizacion()).isEqualTo(DEFAULT_FECHA_ACTUALIZACION);
        assertThat(testNotificacion.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testNotificacion.getTipoNotificacion()).isEqualTo(UPDATED_TIPO_NOTIFICACION);
    }

    @Test
    @Transactional
    void fullUpdateNotificacionWithPatch() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();

        // Update the notificacion using partial update
        Notificacion partialUpdatedNotificacion = new Notificacion();
        partialUpdatedNotificacion.setId(notificacion.getId());

        partialUpdatedNotificacion
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaActualizacion(UPDATED_FECHA_ACTUALIZACION)
            .estado(UPDATED_ESTADO)
            .tipoNotificacion(UPDATED_TIPO_NOTIFICACION);

        restNotificacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificacion.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotificacion))
            )
            .andExpect(status().isOk());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
        Notificacion testNotificacion = notificacionList.get(notificacionList.size() - 1);
        assertThat(testNotificacion.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testNotificacion.getFechaActualizacion()).isEqualTo(UPDATED_FECHA_ACTUALIZACION);
        assertThat(testNotificacion.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testNotificacion.getTipoNotificacion()).isEqualTo(UPDATED_TIPO_NOTIFICACION);
    }

    @Test
    @Transactional
    void patchNonExistingNotificacion() throws Exception {
        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();
        notificacion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notificacion.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotificacion() throws Exception {
        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();
        notificacion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificacion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotificacion() throws Exception {
        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();
        notificacion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificacionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificacion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotificacion() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        int databaseSizeBeforeDelete = notificacionRepository.findAll().size();

        // Delete the notificacion
        restNotificacionMockMvc
            .perform(delete(ENTITY_API_URL_ID, notificacion.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

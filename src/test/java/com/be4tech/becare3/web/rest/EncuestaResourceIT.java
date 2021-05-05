package com.be4tech.becare3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.Encuesta;
import com.be4tech.becare3.domain.User;
import com.be4tech.becare3.repository.EncuestaRepository;
import com.be4tech.becare3.service.criteria.EncuestaCriteria;
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
 * Integration tests for the {@link EncuestaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EncuestaResourceIT {

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_DEBILIDAD = false;
    private static final Boolean UPDATED_DEBILIDAD = true;

    private static final Boolean DEFAULT_CEFALEA = false;
    private static final Boolean UPDATED_CEFALEA = true;

    private static final Boolean DEFAULT_CALAMBRES = false;
    private static final Boolean UPDATED_CALAMBRES = true;

    private static final Boolean DEFAULT_NAUSEAS = false;
    private static final Boolean UPDATED_NAUSEAS = true;

    private static final Boolean DEFAULT_VOMITO = false;
    private static final Boolean UPDATED_VOMITO = true;

    private static final Boolean DEFAULT_MAREO = false;
    private static final Boolean UPDATED_MAREO = true;

    private static final Boolean DEFAULT_NINGUNA = false;
    private static final Boolean UPDATED_NINGUNA = true;

    private static final String ENTITY_API_URL = "/api/encuestas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EncuestaRepository encuestaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEncuestaMockMvc;

    private Encuesta encuesta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Encuesta createEntity(EntityManager em) {
        Encuesta encuesta = new Encuesta()
            .fecha(DEFAULT_FECHA)
            .debilidad(DEFAULT_DEBILIDAD)
            .cefalea(DEFAULT_CEFALEA)
            .calambres(DEFAULT_CALAMBRES)
            .nauseas(DEFAULT_NAUSEAS)
            .vomito(DEFAULT_VOMITO)
            .mareo(DEFAULT_MAREO)
            .ninguna(DEFAULT_NINGUNA);
        return encuesta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Encuesta createUpdatedEntity(EntityManager em) {
        Encuesta encuesta = new Encuesta()
            .fecha(UPDATED_FECHA)
            .debilidad(UPDATED_DEBILIDAD)
            .cefalea(UPDATED_CEFALEA)
            .calambres(UPDATED_CALAMBRES)
            .nauseas(UPDATED_NAUSEAS)
            .vomito(UPDATED_VOMITO)
            .mareo(UPDATED_MAREO)
            .ninguna(UPDATED_NINGUNA);
        return encuesta;
    }

    @BeforeEach
    public void initTest() {
        encuesta = createEntity(em);
    }

    @Test
    @Transactional
    void createEncuesta() throws Exception {
        int databaseSizeBeforeCreate = encuestaRepository.findAll().size();
        // Create the Encuesta
        restEncuestaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(encuesta))
            )
            .andExpect(status().isCreated());

        // Validate the Encuesta in the database
        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeCreate + 1);
        Encuesta testEncuesta = encuestaList.get(encuestaList.size() - 1);
        assertThat(testEncuesta.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testEncuesta.getDebilidad()).isEqualTo(DEFAULT_DEBILIDAD);
        assertThat(testEncuesta.getCefalea()).isEqualTo(DEFAULT_CEFALEA);
        assertThat(testEncuesta.getCalambres()).isEqualTo(DEFAULT_CALAMBRES);
        assertThat(testEncuesta.getNauseas()).isEqualTo(DEFAULT_NAUSEAS);
        assertThat(testEncuesta.getVomito()).isEqualTo(DEFAULT_VOMITO);
        assertThat(testEncuesta.getMareo()).isEqualTo(DEFAULT_MAREO);
        assertThat(testEncuesta.getNinguna()).isEqualTo(DEFAULT_NINGUNA);
    }

    @Test
    @Transactional
    void createEncuestaWithExistingId() throws Exception {
        // Create the Encuesta with an existing ID
        encuesta.setId(1L);

        int databaseSizeBeforeCreate = encuestaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEncuestaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(encuesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Encuesta in the database
        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEncuestas() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList
        restEncuestaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(encuesta.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].debilidad").value(hasItem(DEFAULT_DEBILIDAD.booleanValue())))
            .andExpect(jsonPath("$.[*].cefalea").value(hasItem(DEFAULT_CEFALEA.booleanValue())))
            .andExpect(jsonPath("$.[*].calambres").value(hasItem(DEFAULT_CALAMBRES.booleanValue())))
            .andExpect(jsonPath("$.[*].nauseas").value(hasItem(DEFAULT_NAUSEAS.booleanValue())))
            .andExpect(jsonPath("$.[*].vomito").value(hasItem(DEFAULT_VOMITO.booleanValue())))
            .andExpect(jsonPath("$.[*].mareo").value(hasItem(DEFAULT_MAREO.booleanValue())))
            .andExpect(jsonPath("$.[*].ninguna").value(hasItem(DEFAULT_NINGUNA.booleanValue())));
    }

    @Test
    @Transactional
    void getEncuesta() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get the encuesta
        restEncuestaMockMvc
            .perform(get(ENTITY_API_URL_ID, encuesta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(encuesta.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.debilidad").value(DEFAULT_DEBILIDAD.booleanValue()))
            .andExpect(jsonPath("$.cefalea").value(DEFAULT_CEFALEA.booleanValue()))
            .andExpect(jsonPath("$.calambres").value(DEFAULT_CALAMBRES.booleanValue()))
            .andExpect(jsonPath("$.nauseas").value(DEFAULT_NAUSEAS.booleanValue()))
            .andExpect(jsonPath("$.vomito").value(DEFAULT_VOMITO.booleanValue()))
            .andExpect(jsonPath("$.mareo").value(DEFAULT_MAREO.booleanValue()))
            .andExpect(jsonPath("$.ninguna").value(DEFAULT_NINGUNA.booleanValue()));
    }

    @Test
    @Transactional
    void getEncuestasByIdFiltering() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        Long id = encuesta.getId();

        defaultEncuestaShouldBeFound("id.equals=" + id);
        defaultEncuestaShouldNotBeFound("id.notEquals=" + id);

        defaultEncuestaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEncuestaShouldNotBeFound("id.greaterThan=" + id);

        defaultEncuestaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEncuestaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaIsEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fecha equals to DEFAULT_FECHA
        defaultEncuestaShouldBeFound("fecha.equals=" + DEFAULT_FECHA);

        // Get all the encuestaList where fecha equals to UPDATED_FECHA
        defaultEncuestaShouldNotBeFound("fecha.equals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fecha not equals to DEFAULT_FECHA
        defaultEncuestaShouldNotBeFound("fecha.notEquals=" + DEFAULT_FECHA);

        // Get all the encuestaList where fecha not equals to UPDATED_FECHA
        defaultEncuestaShouldBeFound("fecha.notEquals=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaIsInShouldWork() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fecha in DEFAULT_FECHA or UPDATED_FECHA
        defaultEncuestaShouldBeFound("fecha.in=" + DEFAULT_FECHA + "," + UPDATED_FECHA);

        // Get all the encuestaList where fecha equals to UPDATED_FECHA
        defaultEncuestaShouldNotBeFound("fecha.in=" + UPDATED_FECHA);
    }

    @Test
    @Transactional
    void getAllEncuestasByFechaIsNullOrNotNull() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where fecha is not null
        defaultEncuestaShouldBeFound("fecha.specified=true");

        // Get all the encuestaList where fecha is null
        defaultEncuestaShouldNotBeFound("fecha.specified=false");
    }

    @Test
    @Transactional
    void getAllEncuestasByDebilidadIsEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where debilidad equals to DEFAULT_DEBILIDAD
        defaultEncuestaShouldBeFound("debilidad.equals=" + DEFAULT_DEBILIDAD);

        // Get all the encuestaList where debilidad equals to UPDATED_DEBILIDAD
        defaultEncuestaShouldNotBeFound("debilidad.equals=" + UPDATED_DEBILIDAD);
    }

    @Test
    @Transactional
    void getAllEncuestasByDebilidadIsNotEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where debilidad not equals to DEFAULT_DEBILIDAD
        defaultEncuestaShouldNotBeFound("debilidad.notEquals=" + DEFAULT_DEBILIDAD);

        // Get all the encuestaList where debilidad not equals to UPDATED_DEBILIDAD
        defaultEncuestaShouldBeFound("debilidad.notEquals=" + UPDATED_DEBILIDAD);
    }

    @Test
    @Transactional
    void getAllEncuestasByDebilidadIsInShouldWork() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where debilidad in DEFAULT_DEBILIDAD or UPDATED_DEBILIDAD
        defaultEncuestaShouldBeFound("debilidad.in=" + DEFAULT_DEBILIDAD + "," + UPDATED_DEBILIDAD);

        // Get all the encuestaList where debilidad equals to UPDATED_DEBILIDAD
        defaultEncuestaShouldNotBeFound("debilidad.in=" + UPDATED_DEBILIDAD);
    }

    @Test
    @Transactional
    void getAllEncuestasByDebilidadIsNullOrNotNull() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where debilidad is not null
        defaultEncuestaShouldBeFound("debilidad.specified=true");

        // Get all the encuestaList where debilidad is null
        defaultEncuestaShouldNotBeFound("debilidad.specified=false");
    }

    @Test
    @Transactional
    void getAllEncuestasByCefaleaIsEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where cefalea equals to DEFAULT_CEFALEA
        defaultEncuestaShouldBeFound("cefalea.equals=" + DEFAULT_CEFALEA);

        // Get all the encuestaList where cefalea equals to UPDATED_CEFALEA
        defaultEncuestaShouldNotBeFound("cefalea.equals=" + UPDATED_CEFALEA);
    }

    @Test
    @Transactional
    void getAllEncuestasByCefaleaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where cefalea not equals to DEFAULT_CEFALEA
        defaultEncuestaShouldNotBeFound("cefalea.notEquals=" + DEFAULT_CEFALEA);

        // Get all the encuestaList where cefalea not equals to UPDATED_CEFALEA
        defaultEncuestaShouldBeFound("cefalea.notEquals=" + UPDATED_CEFALEA);
    }

    @Test
    @Transactional
    void getAllEncuestasByCefaleaIsInShouldWork() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where cefalea in DEFAULT_CEFALEA or UPDATED_CEFALEA
        defaultEncuestaShouldBeFound("cefalea.in=" + DEFAULT_CEFALEA + "," + UPDATED_CEFALEA);

        // Get all the encuestaList where cefalea equals to UPDATED_CEFALEA
        defaultEncuestaShouldNotBeFound("cefalea.in=" + UPDATED_CEFALEA);
    }

    @Test
    @Transactional
    void getAllEncuestasByCefaleaIsNullOrNotNull() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where cefalea is not null
        defaultEncuestaShouldBeFound("cefalea.specified=true");

        // Get all the encuestaList where cefalea is null
        defaultEncuestaShouldNotBeFound("cefalea.specified=false");
    }

    @Test
    @Transactional
    void getAllEncuestasByCalambresIsEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where calambres equals to DEFAULT_CALAMBRES
        defaultEncuestaShouldBeFound("calambres.equals=" + DEFAULT_CALAMBRES);

        // Get all the encuestaList where calambres equals to UPDATED_CALAMBRES
        defaultEncuestaShouldNotBeFound("calambres.equals=" + UPDATED_CALAMBRES);
    }

    @Test
    @Transactional
    void getAllEncuestasByCalambresIsNotEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where calambres not equals to DEFAULT_CALAMBRES
        defaultEncuestaShouldNotBeFound("calambres.notEquals=" + DEFAULT_CALAMBRES);

        // Get all the encuestaList where calambres not equals to UPDATED_CALAMBRES
        defaultEncuestaShouldBeFound("calambres.notEquals=" + UPDATED_CALAMBRES);
    }

    @Test
    @Transactional
    void getAllEncuestasByCalambresIsInShouldWork() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where calambres in DEFAULT_CALAMBRES or UPDATED_CALAMBRES
        defaultEncuestaShouldBeFound("calambres.in=" + DEFAULT_CALAMBRES + "," + UPDATED_CALAMBRES);

        // Get all the encuestaList where calambres equals to UPDATED_CALAMBRES
        defaultEncuestaShouldNotBeFound("calambres.in=" + UPDATED_CALAMBRES);
    }

    @Test
    @Transactional
    void getAllEncuestasByCalambresIsNullOrNotNull() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where calambres is not null
        defaultEncuestaShouldBeFound("calambres.specified=true");

        // Get all the encuestaList where calambres is null
        defaultEncuestaShouldNotBeFound("calambres.specified=false");
    }

    @Test
    @Transactional
    void getAllEncuestasByNauseasIsEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where nauseas equals to DEFAULT_NAUSEAS
        defaultEncuestaShouldBeFound("nauseas.equals=" + DEFAULT_NAUSEAS);

        // Get all the encuestaList where nauseas equals to UPDATED_NAUSEAS
        defaultEncuestaShouldNotBeFound("nauseas.equals=" + UPDATED_NAUSEAS);
    }

    @Test
    @Transactional
    void getAllEncuestasByNauseasIsNotEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where nauseas not equals to DEFAULT_NAUSEAS
        defaultEncuestaShouldNotBeFound("nauseas.notEquals=" + DEFAULT_NAUSEAS);

        // Get all the encuestaList where nauseas not equals to UPDATED_NAUSEAS
        defaultEncuestaShouldBeFound("nauseas.notEquals=" + UPDATED_NAUSEAS);
    }

    @Test
    @Transactional
    void getAllEncuestasByNauseasIsInShouldWork() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where nauseas in DEFAULT_NAUSEAS or UPDATED_NAUSEAS
        defaultEncuestaShouldBeFound("nauseas.in=" + DEFAULT_NAUSEAS + "," + UPDATED_NAUSEAS);

        // Get all the encuestaList where nauseas equals to UPDATED_NAUSEAS
        defaultEncuestaShouldNotBeFound("nauseas.in=" + UPDATED_NAUSEAS);
    }

    @Test
    @Transactional
    void getAllEncuestasByNauseasIsNullOrNotNull() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where nauseas is not null
        defaultEncuestaShouldBeFound("nauseas.specified=true");

        // Get all the encuestaList where nauseas is null
        defaultEncuestaShouldNotBeFound("nauseas.specified=false");
    }

    @Test
    @Transactional
    void getAllEncuestasByVomitoIsEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where vomito equals to DEFAULT_VOMITO
        defaultEncuestaShouldBeFound("vomito.equals=" + DEFAULT_VOMITO);

        // Get all the encuestaList where vomito equals to UPDATED_VOMITO
        defaultEncuestaShouldNotBeFound("vomito.equals=" + UPDATED_VOMITO);
    }

    @Test
    @Transactional
    void getAllEncuestasByVomitoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where vomito not equals to DEFAULT_VOMITO
        defaultEncuestaShouldNotBeFound("vomito.notEquals=" + DEFAULT_VOMITO);

        // Get all the encuestaList where vomito not equals to UPDATED_VOMITO
        defaultEncuestaShouldBeFound("vomito.notEquals=" + UPDATED_VOMITO);
    }

    @Test
    @Transactional
    void getAllEncuestasByVomitoIsInShouldWork() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where vomito in DEFAULT_VOMITO or UPDATED_VOMITO
        defaultEncuestaShouldBeFound("vomito.in=" + DEFAULT_VOMITO + "," + UPDATED_VOMITO);

        // Get all the encuestaList where vomito equals to UPDATED_VOMITO
        defaultEncuestaShouldNotBeFound("vomito.in=" + UPDATED_VOMITO);
    }

    @Test
    @Transactional
    void getAllEncuestasByVomitoIsNullOrNotNull() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where vomito is not null
        defaultEncuestaShouldBeFound("vomito.specified=true");

        // Get all the encuestaList where vomito is null
        defaultEncuestaShouldNotBeFound("vomito.specified=false");
    }

    @Test
    @Transactional
    void getAllEncuestasByMareoIsEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where mareo equals to DEFAULT_MAREO
        defaultEncuestaShouldBeFound("mareo.equals=" + DEFAULT_MAREO);

        // Get all the encuestaList where mareo equals to UPDATED_MAREO
        defaultEncuestaShouldNotBeFound("mareo.equals=" + UPDATED_MAREO);
    }

    @Test
    @Transactional
    void getAllEncuestasByMareoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where mareo not equals to DEFAULT_MAREO
        defaultEncuestaShouldNotBeFound("mareo.notEquals=" + DEFAULT_MAREO);

        // Get all the encuestaList where mareo not equals to UPDATED_MAREO
        defaultEncuestaShouldBeFound("mareo.notEquals=" + UPDATED_MAREO);
    }

    @Test
    @Transactional
    void getAllEncuestasByMareoIsInShouldWork() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where mareo in DEFAULT_MAREO or UPDATED_MAREO
        defaultEncuestaShouldBeFound("mareo.in=" + DEFAULT_MAREO + "," + UPDATED_MAREO);

        // Get all the encuestaList where mareo equals to UPDATED_MAREO
        defaultEncuestaShouldNotBeFound("mareo.in=" + UPDATED_MAREO);
    }

    @Test
    @Transactional
    void getAllEncuestasByMareoIsNullOrNotNull() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where mareo is not null
        defaultEncuestaShouldBeFound("mareo.specified=true");

        // Get all the encuestaList where mareo is null
        defaultEncuestaShouldNotBeFound("mareo.specified=false");
    }

    @Test
    @Transactional
    void getAllEncuestasByNingunaIsEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where ninguna equals to DEFAULT_NINGUNA
        defaultEncuestaShouldBeFound("ninguna.equals=" + DEFAULT_NINGUNA);

        // Get all the encuestaList where ninguna equals to UPDATED_NINGUNA
        defaultEncuestaShouldNotBeFound("ninguna.equals=" + UPDATED_NINGUNA);
    }

    @Test
    @Transactional
    void getAllEncuestasByNingunaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where ninguna not equals to DEFAULT_NINGUNA
        defaultEncuestaShouldNotBeFound("ninguna.notEquals=" + DEFAULT_NINGUNA);

        // Get all the encuestaList where ninguna not equals to UPDATED_NINGUNA
        defaultEncuestaShouldBeFound("ninguna.notEquals=" + UPDATED_NINGUNA);
    }

    @Test
    @Transactional
    void getAllEncuestasByNingunaIsInShouldWork() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where ninguna in DEFAULT_NINGUNA or UPDATED_NINGUNA
        defaultEncuestaShouldBeFound("ninguna.in=" + DEFAULT_NINGUNA + "," + UPDATED_NINGUNA);

        // Get all the encuestaList where ninguna equals to UPDATED_NINGUNA
        defaultEncuestaShouldNotBeFound("ninguna.in=" + UPDATED_NINGUNA);
    }

    @Test
    @Transactional
    void getAllEncuestasByNingunaIsNullOrNotNull() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        // Get all the encuestaList where ninguna is not null
        defaultEncuestaShouldBeFound("ninguna.specified=true");

        // Get all the encuestaList where ninguna is null
        defaultEncuestaShouldNotBeFound("ninguna.specified=false");
    }

    @Test
    @Transactional
    void getAllEncuestasByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        encuesta.setUser(user);
        encuestaRepository.saveAndFlush(encuesta);
        String userId = user.getId();

        // Get all the encuestaList where user equals to userId
        defaultEncuestaShouldBeFound("userId.equals=" + userId);

        // Get all the encuestaList where user equals to "invalid-id"
        defaultEncuestaShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEncuestaShouldBeFound(String filter) throws Exception {
        restEncuestaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(encuesta.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].debilidad").value(hasItem(DEFAULT_DEBILIDAD.booleanValue())))
            .andExpect(jsonPath("$.[*].cefalea").value(hasItem(DEFAULT_CEFALEA.booleanValue())))
            .andExpect(jsonPath("$.[*].calambres").value(hasItem(DEFAULT_CALAMBRES.booleanValue())))
            .andExpect(jsonPath("$.[*].nauseas").value(hasItem(DEFAULT_NAUSEAS.booleanValue())))
            .andExpect(jsonPath("$.[*].vomito").value(hasItem(DEFAULT_VOMITO.booleanValue())))
            .andExpect(jsonPath("$.[*].mareo").value(hasItem(DEFAULT_MAREO.booleanValue())))
            .andExpect(jsonPath("$.[*].ninguna").value(hasItem(DEFAULT_NINGUNA.booleanValue())));

        // Check, that the count call also returns 1
        restEncuestaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEncuestaShouldNotBeFound(String filter) throws Exception {
        restEncuestaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEncuestaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEncuesta() throws Exception {
        // Get the encuesta
        restEncuestaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEncuesta() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        int databaseSizeBeforeUpdate = encuestaRepository.findAll().size();

        // Update the encuesta
        Encuesta updatedEncuesta = encuestaRepository.findById(encuesta.getId()).get();
        // Disconnect from session so that the updates on updatedEncuesta are not directly saved in db
        em.detach(updatedEncuesta);
        updatedEncuesta
            .fecha(UPDATED_FECHA)
            .debilidad(UPDATED_DEBILIDAD)
            .cefalea(UPDATED_CEFALEA)
            .calambres(UPDATED_CALAMBRES)
            .nauseas(UPDATED_NAUSEAS)
            .vomito(UPDATED_VOMITO)
            .mareo(UPDATED_MAREO)
            .ninguna(UPDATED_NINGUNA);

        restEncuestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEncuesta.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEncuesta))
            )
            .andExpect(status().isOk());

        // Validate the Encuesta in the database
        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeUpdate);
        Encuesta testEncuesta = encuestaList.get(encuestaList.size() - 1);
        assertThat(testEncuesta.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testEncuesta.getDebilidad()).isEqualTo(UPDATED_DEBILIDAD);
        assertThat(testEncuesta.getCefalea()).isEqualTo(UPDATED_CEFALEA);
        assertThat(testEncuesta.getCalambres()).isEqualTo(UPDATED_CALAMBRES);
        assertThat(testEncuesta.getNauseas()).isEqualTo(UPDATED_NAUSEAS);
        assertThat(testEncuesta.getVomito()).isEqualTo(UPDATED_VOMITO);
        assertThat(testEncuesta.getMareo()).isEqualTo(UPDATED_MAREO);
        assertThat(testEncuesta.getNinguna()).isEqualTo(UPDATED_NINGUNA);
    }

    @Test
    @Transactional
    void putNonExistingEncuesta() throws Exception {
        int databaseSizeBeforeUpdate = encuestaRepository.findAll().size();
        encuesta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEncuestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, encuesta.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(encuesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Encuesta in the database
        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEncuesta() throws Exception {
        int databaseSizeBeforeUpdate = encuestaRepository.findAll().size();
        encuesta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEncuestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(encuesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Encuesta in the database
        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEncuesta() throws Exception {
        int databaseSizeBeforeUpdate = encuestaRepository.findAll().size();
        encuesta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEncuestaMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(encuesta))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Encuesta in the database
        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEncuestaWithPatch() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        int databaseSizeBeforeUpdate = encuestaRepository.findAll().size();

        // Update the encuesta using partial update
        Encuesta partialUpdatedEncuesta = new Encuesta();
        partialUpdatedEncuesta.setId(encuesta.getId());

        partialUpdatedEncuesta
            .fecha(UPDATED_FECHA)
            .debilidad(UPDATED_DEBILIDAD)
            .nauseas(UPDATED_NAUSEAS)
            .mareo(UPDATED_MAREO)
            .ninguna(UPDATED_NINGUNA);

        restEncuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEncuesta.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEncuesta))
            )
            .andExpect(status().isOk());

        // Validate the Encuesta in the database
        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeUpdate);
        Encuesta testEncuesta = encuestaList.get(encuestaList.size() - 1);
        assertThat(testEncuesta.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testEncuesta.getDebilidad()).isEqualTo(UPDATED_DEBILIDAD);
        assertThat(testEncuesta.getCefalea()).isEqualTo(DEFAULT_CEFALEA);
        assertThat(testEncuesta.getCalambres()).isEqualTo(DEFAULT_CALAMBRES);
        assertThat(testEncuesta.getNauseas()).isEqualTo(UPDATED_NAUSEAS);
        assertThat(testEncuesta.getVomito()).isEqualTo(DEFAULT_VOMITO);
        assertThat(testEncuesta.getMareo()).isEqualTo(UPDATED_MAREO);
        assertThat(testEncuesta.getNinguna()).isEqualTo(UPDATED_NINGUNA);
    }

    @Test
    @Transactional
    void fullUpdateEncuestaWithPatch() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        int databaseSizeBeforeUpdate = encuestaRepository.findAll().size();

        // Update the encuesta using partial update
        Encuesta partialUpdatedEncuesta = new Encuesta();
        partialUpdatedEncuesta.setId(encuesta.getId());

        partialUpdatedEncuesta
            .fecha(UPDATED_FECHA)
            .debilidad(UPDATED_DEBILIDAD)
            .cefalea(UPDATED_CEFALEA)
            .calambres(UPDATED_CALAMBRES)
            .nauseas(UPDATED_NAUSEAS)
            .vomito(UPDATED_VOMITO)
            .mareo(UPDATED_MAREO)
            .ninguna(UPDATED_NINGUNA);

        restEncuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEncuesta.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEncuesta))
            )
            .andExpect(status().isOk());

        // Validate the Encuesta in the database
        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeUpdate);
        Encuesta testEncuesta = encuestaList.get(encuestaList.size() - 1);
        assertThat(testEncuesta.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testEncuesta.getDebilidad()).isEqualTo(UPDATED_DEBILIDAD);
        assertThat(testEncuesta.getCefalea()).isEqualTo(UPDATED_CEFALEA);
        assertThat(testEncuesta.getCalambres()).isEqualTo(UPDATED_CALAMBRES);
        assertThat(testEncuesta.getNauseas()).isEqualTo(UPDATED_NAUSEAS);
        assertThat(testEncuesta.getVomito()).isEqualTo(UPDATED_VOMITO);
        assertThat(testEncuesta.getMareo()).isEqualTo(UPDATED_MAREO);
        assertThat(testEncuesta.getNinguna()).isEqualTo(UPDATED_NINGUNA);
    }

    @Test
    @Transactional
    void patchNonExistingEncuesta() throws Exception {
        int databaseSizeBeforeUpdate = encuestaRepository.findAll().size();
        encuesta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEncuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, encuesta.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(encuesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Encuesta in the database
        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEncuesta() throws Exception {
        int databaseSizeBeforeUpdate = encuestaRepository.findAll().size();
        encuesta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEncuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(encuesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Encuesta in the database
        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEncuesta() throws Exception {
        int databaseSizeBeforeUpdate = encuestaRepository.findAll().size();
        encuesta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEncuestaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(encuesta))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Encuesta in the database
        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEncuesta() throws Exception {
        // Initialize the database
        encuestaRepository.saveAndFlush(encuesta);

        int databaseSizeBeforeDelete = encuestaRepository.findAll().size();

        // Delete the encuesta
        restEncuestaMockMvc
            .perform(delete(ENTITY_API_URL_ID, encuesta.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Encuesta> encuestaList = encuestaRepository.findAll();
        assertThat(encuestaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

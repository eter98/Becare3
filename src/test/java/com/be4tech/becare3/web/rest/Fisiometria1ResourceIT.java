package com.be4tech.becare3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.Fisiometria1;
import com.be4tech.becare3.domain.User;
import com.be4tech.becare3.repository.Fisiometria1Repository;
import com.be4tech.becare3.service.criteria.Fisiometria1Criteria;
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
 * Integration tests for the {@link Fisiometria1Resource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class Fisiometria1ResourceIT {

    private static final Integer DEFAULT_RITMO_CARDIACO = 1;
    private static final Integer UPDATED_RITMO_CARDIACO = 2;
    private static final Integer SMALLER_RITMO_CARDIACO = 1 - 1;

    private static final Integer DEFAULT_RITMO_RESPIRATORIO = 1;
    private static final Integer UPDATED_RITMO_RESPIRATORIO = 2;
    private static final Integer SMALLER_RITMO_RESPIRATORIO = 1 - 1;

    private static final Integer DEFAULT_OXIMETRIA = 1;
    private static final Integer UPDATED_OXIMETRIA = 2;
    private static final Integer SMALLER_OXIMETRIA = 1 - 1;

    private static final Integer DEFAULT_PRESION_ARTERIAL_SISTOLICA = 1;
    private static final Integer UPDATED_PRESION_ARTERIAL_SISTOLICA = 2;
    private static final Integer SMALLER_PRESION_ARTERIAL_SISTOLICA = 1 - 1;

    private static final Integer DEFAULT_PRESION_ARTERIAL_DIASTOLICA = 1;
    private static final Integer UPDATED_PRESION_ARTERIAL_DIASTOLICA = 2;
    private static final Integer SMALLER_PRESION_ARTERIAL_DIASTOLICA = 1 - 1;

    private static final Float DEFAULT_TEMPERATURA = 1F;
    private static final Float UPDATED_TEMPERATURA = 2F;
    private static final Float SMALLER_TEMPERATURA = 1F - 1F;

    private static final Instant DEFAULT_FECHA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FECHA_TOMA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_TOMA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/fisiometria-1-s";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private Fisiometria1Repository fisiometria1Repository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFisiometria1MockMvc;

    private Fisiometria1 fisiometria1;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fisiometria1 createEntity(EntityManager em) {
        Fisiometria1 fisiometria1 = new Fisiometria1()
            .ritmoCardiaco(DEFAULT_RITMO_CARDIACO)
            .ritmoRespiratorio(DEFAULT_RITMO_RESPIRATORIO)
            .oximetria(DEFAULT_OXIMETRIA)
            .presionArterialSistolica(DEFAULT_PRESION_ARTERIAL_SISTOLICA)
            .presionArterialDiastolica(DEFAULT_PRESION_ARTERIAL_DIASTOLICA)
            .temperatura(DEFAULT_TEMPERATURA)
            .fechaRegistro(DEFAULT_FECHA_REGISTRO)
            .fechaToma(DEFAULT_FECHA_TOMA);
        return fisiometria1;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fisiometria1 createUpdatedEntity(EntityManager em) {
        Fisiometria1 fisiometria1 = new Fisiometria1()
            .ritmoCardiaco(UPDATED_RITMO_CARDIACO)
            .ritmoRespiratorio(UPDATED_RITMO_RESPIRATORIO)
            .oximetria(UPDATED_OXIMETRIA)
            .presionArterialSistolica(UPDATED_PRESION_ARTERIAL_SISTOLICA)
            .presionArterialDiastolica(UPDATED_PRESION_ARTERIAL_DIASTOLICA)
            .temperatura(UPDATED_TEMPERATURA)
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .fechaToma(UPDATED_FECHA_TOMA);
        return fisiometria1;
    }

    @BeforeEach
    public void initTest() {
        fisiometria1 = createEntity(em);
    }

    @Test
    @Transactional
    void createFisiometria1() throws Exception {
        int databaseSizeBeforeCreate = fisiometria1Repository.findAll().size();
        // Create the Fisiometria1
        restFisiometria1MockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fisiometria1))
            )
            .andExpect(status().isCreated());

        // Validate the Fisiometria1 in the database
        List<Fisiometria1> fisiometria1List = fisiometria1Repository.findAll();
        assertThat(fisiometria1List).hasSize(databaseSizeBeforeCreate + 1);
        Fisiometria1 testFisiometria1 = fisiometria1List.get(fisiometria1List.size() - 1);
        assertThat(testFisiometria1.getRitmoCardiaco()).isEqualTo(DEFAULT_RITMO_CARDIACO);
        assertThat(testFisiometria1.getRitmoRespiratorio()).isEqualTo(DEFAULT_RITMO_RESPIRATORIO);
        assertThat(testFisiometria1.getOximetria()).isEqualTo(DEFAULT_OXIMETRIA);
        assertThat(testFisiometria1.getPresionArterialSistolica()).isEqualTo(DEFAULT_PRESION_ARTERIAL_SISTOLICA);
        assertThat(testFisiometria1.getPresionArterialDiastolica()).isEqualTo(DEFAULT_PRESION_ARTERIAL_DIASTOLICA);
        assertThat(testFisiometria1.getTemperatura()).isEqualTo(DEFAULT_TEMPERATURA);
        assertThat(testFisiometria1.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
        assertThat(testFisiometria1.getFechaToma()).isEqualTo(DEFAULT_FECHA_TOMA);
    }

    @Test
    @Transactional
    void createFisiometria1WithExistingId() throws Exception {
        // Create the Fisiometria1 with an existing ID
        fisiometria1.setId(1L);

        int databaseSizeBeforeCreate = fisiometria1Repository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFisiometria1MockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fisiometria1))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fisiometria1 in the database
        List<Fisiometria1> fisiometria1List = fisiometria1Repository.findAll();
        assertThat(fisiometria1List).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFisiometria1s() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List
        restFisiometria1MockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fisiometria1.getId().intValue())))
            .andExpect(jsonPath("$.[*].ritmoCardiaco").value(hasItem(DEFAULT_RITMO_CARDIACO)))
            .andExpect(jsonPath("$.[*].ritmoRespiratorio").value(hasItem(DEFAULT_RITMO_RESPIRATORIO)))
            .andExpect(jsonPath("$.[*].oximetria").value(hasItem(DEFAULT_OXIMETRIA)))
            .andExpect(jsonPath("$.[*].presionArterialSistolica").value(hasItem(DEFAULT_PRESION_ARTERIAL_SISTOLICA)))
            .andExpect(jsonPath("$.[*].presionArterialDiastolica").value(hasItem(DEFAULT_PRESION_ARTERIAL_DIASTOLICA)))
            .andExpect(jsonPath("$.[*].temperatura").value(hasItem(DEFAULT_TEMPERATURA.doubleValue())))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())))
            .andExpect(jsonPath("$.[*].fechaToma").value(hasItem(DEFAULT_FECHA_TOMA.toString())));
    }

    @Test
    @Transactional
    void getFisiometria1() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get the fisiometria1
        restFisiometria1MockMvc
            .perform(get(ENTITY_API_URL_ID, fisiometria1.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fisiometria1.getId().intValue()))
            .andExpect(jsonPath("$.ritmoCardiaco").value(DEFAULT_RITMO_CARDIACO))
            .andExpect(jsonPath("$.ritmoRespiratorio").value(DEFAULT_RITMO_RESPIRATORIO))
            .andExpect(jsonPath("$.oximetria").value(DEFAULT_OXIMETRIA))
            .andExpect(jsonPath("$.presionArterialSistolica").value(DEFAULT_PRESION_ARTERIAL_SISTOLICA))
            .andExpect(jsonPath("$.presionArterialDiastolica").value(DEFAULT_PRESION_ARTERIAL_DIASTOLICA))
            .andExpect(jsonPath("$.temperatura").value(DEFAULT_TEMPERATURA.doubleValue()))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()))
            .andExpect(jsonPath("$.fechaToma").value(DEFAULT_FECHA_TOMA.toString()));
    }

    @Test
    @Transactional
    void getFisiometria1sByIdFiltering() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        Long id = fisiometria1.getId();

        defaultFisiometria1ShouldBeFound("id.equals=" + id);
        defaultFisiometria1ShouldNotBeFound("id.notEquals=" + id);

        defaultFisiometria1ShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFisiometria1ShouldNotBeFound("id.greaterThan=" + id);

        defaultFisiometria1ShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFisiometria1ShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByRitmoCardiacoIsEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where ritmoCardiaco equals to DEFAULT_RITMO_CARDIACO
        defaultFisiometria1ShouldBeFound("ritmoCardiaco.equals=" + DEFAULT_RITMO_CARDIACO);

        // Get all the fisiometria1List where ritmoCardiaco equals to UPDATED_RITMO_CARDIACO
        defaultFisiometria1ShouldNotBeFound("ritmoCardiaco.equals=" + UPDATED_RITMO_CARDIACO);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByRitmoCardiacoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where ritmoCardiaco not equals to DEFAULT_RITMO_CARDIACO
        defaultFisiometria1ShouldNotBeFound("ritmoCardiaco.notEquals=" + DEFAULT_RITMO_CARDIACO);

        // Get all the fisiometria1List where ritmoCardiaco not equals to UPDATED_RITMO_CARDIACO
        defaultFisiometria1ShouldBeFound("ritmoCardiaco.notEquals=" + UPDATED_RITMO_CARDIACO);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByRitmoCardiacoIsInShouldWork() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where ritmoCardiaco in DEFAULT_RITMO_CARDIACO or UPDATED_RITMO_CARDIACO
        defaultFisiometria1ShouldBeFound("ritmoCardiaco.in=" + DEFAULT_RITMO_CARDIACO + "," + UPDATED_RITMO_CARDIACO);

        // Get all the fisiometria1List where ritmoCardiaco equals to UPDATED_RITMO_CARDIACO
        defaultFisiometria1ShouldNotBeFound("ritmoCardiaco.in=" + UPDATED_RITMO_CARDIACO);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByRitmoCardiacoIsNullOrNotNull() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where ritmoCardiaco is not null
        defaultFisiometria1ShouldBeFound("ritmoCardiaco.specified=true");

        // Get all the fisiometria1List where ritmoCardiaco is null
        defaultFisiometria1ShouldNotBeFound("ritmoCardiaco.specified=false");
    }

    @Test
    @Transactional
    void getAllFisiometria1sByRitmoCardiacoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where ritmoCardiaco is greater than or equal to DEFAULT_RITMO_CARDIACO
        defaultFisiometria1ShouldBeFound("ritmoCardiaco.greaterThanOrEqual=" + DEFAULT_RITMO_CARDIACO);

        // Get all the fisiometria1List where ritmoCardiaco is greater than or equal to UPDATED_RITMO_CARDIACO
        defaultFisiometria1ShouldNotBeFound("ritmoCardiaco.greaterThanOrEqual=" + UPDATED_RITMO_CARDIACO);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByRitmoCardiacoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where ritmoCardiaco is less than or equal to DEFAULT_RITMO_CARDIACO
        defaultFisiometria1ShouldBeFound("ritmoCardiaco.lessThanOrEqual=" + DEFAULT_RITMO_CARDIACO);

        // Get all the fisiometria1List where ritmoCardiaco is less than or equal to SMALLER_RITMO_CARDIACO
        defaultFisiometria1ShouldNotBeFound("ritmoCardiaco.lessThanOrEqual=" + SMALLER_RITMO_CARDIACO);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByRitmoCardiacoIsLessThanSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where ritmoCardiaco is less than DEFAULT_RITMO_CARDIACO
        defaultFisiometria1ShouldNotBeFound("ritmoCardiaco.lessThan=" + DEFAULT_RITMO_CARDIACO);

        // Get all the fisiometria1List where ritmoCardiaco is less than UPDATED_RITMO_CARDIACO
        defaultFisiometria1ShouldBeFound("ritmoCardiaco.lessThan=" + UPDATED_RITMO_CARDIACO);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByRitmoCardiacoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where ritmoCardiaco is greater than DEFAULT_RITMO_CARDIACO
        defaultFisiometria1ShouldNotBeFound("ritmoCardiaco.greaterThan=" + DEFAULT_RITMO_CARDIACO);

        // Get all the fisiometria1List where ritmoCardiaco is greater than SMALLER_RITMO_CARDIACO
        defaultFisiometria1ShouldBeFound("ritmoCardiaco.greaterThan=" + SMALLER_RITMO_CARDIACO);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByRitmoRespiratorioIsEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where ritmoRespiratorio equals to DEFAULT_RITMO_RESPIRATORIO
        defaultFisiometria1ShouldBeFound("ritmoRespiratorio.equals=" + DEFAULT_RITMO_RESPIRATORIO);

        // Get all the fisiometria1List where ritmoRespiratorio equals to UPDATED_RITMO_RESPIRATORIO
        defaultFisiometria1ShouldNotBeFound("ritmoRespiratorio.equals=" + UPDATED_RITMO_RESPIRATORIO);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByRitmoRespiratorioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where ritmoRespiratorio not equals to DEFAULT_RITMO_RESPIRATORIO
        defaultFisiometria1ShouldNotBeFound("ritmoRespiratorio.notEquals=" + DEFAULT_RITMO_RESPIRATORIO);

        // Get all the fisiometria1List where ritmoRespiratorio not equals to UPDATED_RITMO_RESPIRATORIO
        defaultFisiometria1ShouldBeFound("ritmoRespiratorio.notEquals=" + UPDATED_RITMO_RESPIRATORIO);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByRitmoRespiratorioIsInShouldWork() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where ritmoRespiratorio in DEFAULT_RITMO_RESPIRATORIO or UPDATED_RITMO_RESPIRATORIO
        defaultFisiometria1ShouldBeFound("ritmoRespiratorio.in=" + DEFAULT_RITMO_RESPIRATORIO + "," + UPDATED_RITMO_RESPIRATORIO);

        // Get all the fisiometria1List where ritmoRespiratorio equals to UPDATED_RITMO_RESPIRATORIO
        defaultFisiometria1ShouldNotBeFound("ritmoRespiratorio.in=" + UPDATED_RITMO_RESPIRATORIO);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByRitmoRespiratorioIsNullOrNotNull() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where ritmoRespiratorio is not null
        defaultFisiometria1ShouldBeFound("ritmoRespiratorio.specified=true");

        // Get all the fisiometria1List where ritmoRespiratorio is null
        defaultFisiometria1ShouldNotBeFound("ritmoRespiratorio.specified=false");
    }

    @Test
    @Transactional
    void getAllFisiometria1sByRitmoRespiratorioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where ritmoRespiratorio is greater than or equal to DEFAULT_RITMO_RESPIRATORIO
        defaultFisiometria1ShouldBeFound("ritmoRespiratorio.greaterThanOrEqual=" + DEFAULT_RITMO_RESPIRATORIO);

        // Get all the fisiometria1List where ritmoRespiratorio is greater than or equal to UPDATED_RITMO_RESPIRATORIO
        defaultFisiometria1ShouldNotBeFound("ritmoRespiratorio.greaterThanOrEqual=" + UPDATED_RITMO_RESPIRATORIO);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByRitmoRespiratorioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where ritmoRespiratorio is less than or equal to DEFAULT_RITMO_RESPIRATORIO
        defaultFisiometria1ShouldBeFound("ritmoRespiratorio.lessThanOrEqual=" + DEFAULT_RITMO_RESPIRATORIO);

        // Get all the fisiometria1List where ritmoRespiratorio is less than or equal to SMALLER_RITMO_RESPIRATORIO
        defaultFisiometria1ShouldNotBeFound("ritmoRespiratorio.lessThanOrEqual=" + SMALLER_RITMO_RESPIRATORIO);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByRitmoRespiratorioIsLessThanSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where ritmoRespiratorio is less than DEFAULT_RITMO_RESPIRATORIO
        defaultFisiometria1ShouldNotBeFound("ritmoRespiratorio.lessThan=" + DEFAULT_RITMO_RESPIRATORIO);

        // Get all the fisiometria1List where ritmoRespiratorio is less than UPDATED_RITMO_RESPIRATORIO
        defaultFisiometria1ShouldBeFound("ritmoRespiratorio.lessThan=" + UPDATED_RITMO_RESPIRATORIO);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByRitmoRespiratorioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where ritmoRespiratorio is greater than DEFAULT_RITMO_RESPIRATORIO
        defaultFisiometria1ShouldNotBeFound("ritmoRespiratorio.greaterThan=" + DEFAULT_RITMO_RESPIRATORIO);

        // Get all the fisiometria1List where ritmoRespiratorio is greater than SMALLER_RITMO_RESPIRATORIO
        defaultFisiometria1ShouldBeFound("ritmoRespiratorio.greaterThan=" + SMALLER_RITMO_RESPIRATORIO);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByOximetriaIsEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where oximetria equals to DEFAULT_OXIMETRIA
        defaultFisiometria1ShouldBeFound("oximetria.equals=" + DEFAULT_OXIMETRIA);

        // Get all the fisiometria1List where oximetria equals to UPDATED_OXIMETRIA
        defaultFisiometria1ShouldNotBeFound("oximetria.equals=" + UPDATED_OXIMETRIA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByOximetriaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where oximetria not equals to DEFAULT_OXIMETRIA
        defaultFisiometria1ShouldNotBeFound("oximetria.notEquals=" + DEFAULT_OXIMETRIA);

        // Get all the fisiometria1List where oximetria not equals to UPDATED_OXIMETRIA
        defaultFisiometria1ShouldBeFound("oximetria.notEquals=" + UPDATED_OXIMETRIA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByOximetriaIsInShouldWork() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where oximetria in DEFAULT_OXIMETRIA or UPDATED_OXIMETRIA
        defaultFisiometria1ShouldBeFound("oximetria.in=" + DEFAULT_OXIMETRIA + "," + UPDATED_OXIMETRIA);

        // Get all the fisiometria1List where oximetria equals to UPDATED_OXIMETRIA
        defaultFisiometria1ShouldNotBeFound("oximetria.in=" + UPDATED_OXIMETRIA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByOximetriaIsNullOrNotNull() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where oximetria is not null
        defaultFisiometria1ShouldBeFound("oximetria.specified=true");

        // Get all the fisiometria1List where oximetria is null
        defaultFisiometria1ShouldNotBeFound("oximetria.specified=false");
    }

    @Test
    @Transactional
    void getAllFisiometria1sByOximetriaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where oximetria is greater than or equal to DEFAULT_OXIMETRIA
        defaultFisiometria1ShouldBeFound("oximetria.greaterThanOrEqual=" + DEFAULT_OXIMETRIA);

        // Get all the fisiometria1List where oximetria is greater than or equal to UPDATED_OXIMETRIA
        defaultFisiometria1ShouldNotBeFound("oximetria.greaterThanOrEqual=" + UPDATED_OXIMETRIA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByOximetriaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where oximetria is less than or equal to DEFAULT_OXIMETRIA
        defaultFisiometria1ShouldBeFound("oximetria.lessThanOrEqual=" + DEFAULT_OXIMETRIA);

        // Get all the fisiometria1List where oximetria is less than or equal to SMALLER_OXIMETRIA
        defaultFisiometria1ShouldNotBeFound("oximetria.lessThanOrEqual=" + SMALLER_OXIMETRIA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByOximetriaIsLessThanSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where oximetria is less than DEFAULT_OXIMETRIA
        defaultFisiometria1ShouldNotBeFound("oximetria.lessThan=" + DEFAULT_OXIMETRIA);

        // Get all the fisiometria1List where oximetria is less than UPDATED_OXIMETRIA
        defaultFisiometria1ShouldBeFound("oximetria.lessThan=" + UPDATED_OXIMETRIA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByOximetriaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where oximetria is greater than DEFAULT_OXIMETRIA
        defaultFisiometria1ShouldNotBeFound("oximetria.greaterThan=" + DEFAULT_OXIMETRIA);

        // Get all the fisiometria1List where oximetria is greater than SMALLER_OXIMETRIA
        defaultFisiometria1ShouldBeFound("oximetria.greaterThan=" + SMALLER_OXIMETRIA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByPresionArterialSistolicaIsEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where presionArterialSistolica equals to DEFAULT_PRESION_ARTERIAL_SISTOLICA
        defaultFisiometria1ShouldBeFound("presionArterialSistolica.equals=" + DEFAULT_PRESION_ARTERIAL_SISTOLICA);

        // Get all the fisiometria1List where presionArterialSistolica equals to UPDATED_PRESION_ARTERIAL_SISTOLICA
        defaultFisiometria1ShouldNotBeFound("presionArterialSistolica.equals=" + UPDATED_PRESION_ARTERIAL_SISTOLICA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByPresionArterialSistolicaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where presionArterialSistolica not equals to DEFAULT_PRESION_ARTERIAL_SISTOLICA
        defaultFisiometria1ShouldNotBeFound("presionArterialSistolica.notEquals=" + DEFAULT_PRESION_ARTERIAL_SISTOLICA);

        // Get all the fisiometria1List where presionArterialSistolica not equals to UPDATED_PRESION_ARTERIAL_SISTOLICA
        defaultFisiometria1ShouldBeFound("presionArterialSistolica.notEquals=" + UPDATED_PRESION_ARTERIAL_SISTOLICA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByPresionArterialSistolicaIsInShouldWork() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where presionArterialSistolica in DEFAULT_PRESION_ARTERIAL_SISTOLICA or UPDATED_PRESION_ARTERIAL_SISTOLICA
        defaultFisiometria1ShouldBeFound(
            "presionArterialSistolica.in=" + DEFAULT_PRESION_ARTERIAL_SISTOLICA + "," + UPDATED_PRESION_ARTERIAL_SISTOLICA
        );

        // Get all the fisiometria1List where presionArterialSistolica equals to UPDATED_PRESION_ARTERIAL_SISTOLICA
        defaultFisiometria1ShouldNotBeFound("presionArterialSistolica.in=" + UPDATED_PRESION_ARTERIAL_SISTOLICA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByPresionArterialSistolicaIsNullOrNotNull() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where presionArterialSistolica is not null
        defaultFisiometria1ShouldBeFound("presionArterialSistolica.specified=true");

        // Get all the fisiometria1List where presionArterialSistolica is null
        defaultFisiometria1ShouldNotBeFound("presionArterialSistolica.specified=false");
    }

    @Test
    @Transactional
    void getAllFisiometria1sByPresionArterialSistolicaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where presionArterialSistolica is greater than or equal to DEFAULT_PRESION_ARTERIAL_SISTOLICA
        defaultFisiometria1ShouldBeFound("presionArterialSistolica.greaterThanOrEqual=" + DEFAULT_PRESION_ARTERIAL_SISTOLICA);

        // Get all the fisiometria1List where presionArterialSistolica is greater than or equal to UPDATED_PRESION_ARTERIAL_SISTOLICA
        defaultFisiometria1ShouldNotBeFound("presionArterialSistolica.greaterThanOrEqual=" + UPDATED_PRESION_ARTERIAL_SISTOLICA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByPresionArterialSistolicaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where presionArterialSistolica is less than or equal to DEFAULT_PRESION_ARTERIAL_SISTOLICA
        defaultFisiometria1ShouldBeFound("presionArterialSistolica.lessThanOrEqual=" + DEFAULT_PRESION_ARTERIAL_SISTOLICA);

        // Get all the fisiometria1List where presionArterialSistolica is less than or equal to SMALLER_PRESION_ARTERIAL_SISTOLICA
        defaultFisiometria1ShouldNotBeFound("presionArterialSistolica.lessThanOrEqual=" + SMALLER_PRESION_ARTERIAL_SISTOLICA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByPresionArterialSistolicaIsLessThanSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where presionArterialSistolica is less than DEFAULT_PRESION_ARTERIAL_SISTOLICA
        defaultFisiometria1ShouldNotBeFound("presionArterialSistolica.lessThan=" + DEFAULT_PRESION_ARTERIAL_SISTOLICA);

        // Get all the fisiometria1List where presionArterialSistolica is less than UPDATED_PRESION_ARTERIAL_SISTOLICA
        defaultFisiometria1ShouldBeFound("presionArterialSistolica.lessThan=" + UPDATED_PRESION_ARTERIAL_SISTOLICA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByPresionArterialSistolicaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where presionArterialSistolica is greater than DEFAULT_PRESION_ARTERIAL_SISTOLICA
        defaultFisiometria1ShouldNotBeFound("presionArterialSistolica.greaterThan=" + DEFAULT_PRESION_ARTERIAL_SISTOLICA);

        // Get all the fisiometria1List where presionArterialSistolica is greater than SMALLER_PRESION_ARTERIAL_SISTOLICA
        defaultFisiometria1ShouldBeFound("presionArterialSistolica.greaterThan=" + SMALLER_PRESION_ARTERIAL_SISTOLICA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByPresionArterialDiastolicaIsEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where presionArterialDiastolica equals to DEFAULT_PRESION_ARTERIAL_DIASTOLICA
        defaultFisiometria1ShouldBeFound("presionArterialDiastolica.equals=" + DEFAULT_PRESION_ARTERIAL_DIASTOLICA);

        // Get all the fisiometria1List where presionArterialDiastolica equals to UPDATED_PRESION_ARTERIAL_DIASTOLICA
        defaultFisiometria1ShouldNotBeFound("presionArterialDiastolica.equals=" + UPDATED_PRESION_ARTERIAL_DIASTOLICA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByPresionArterialDiastolicaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where presionArterialDiastolica not equals to DEFAULT_PRESION_ARTERIAL_DIASTOLICA
        defaultFisiometria1ShouldNotBeFound("presionArterialDiastolica.notEquals=" + DEFAULT_PRESION_ARTERIAL_DIASTOLICA);

        // Get all the fisiometria1List where presionArterialDiastolica not equals to UPDATED_PRESION_ARTERIAL_DIASTOLICA
        defaultFisiometria1ShouldBeFound("presionArterialDiastolica.notEquals=" + UPDATED_PRESION_ARTERIAL_DIASTOLICA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByPresionArterialDiastolicaIsInShouldWork() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where presionArterialDiastolica in DEFAULT_PRESION_ARTERIAL_DIASTOLICA or UPDATED_PRESION_ARTERIAL_DIASTOLICA
        defaultFisiometria1ShouldBeFound(
            "presionArterialDiastolica.in=" + DEFAULT_PRESION_ARTERIAL_DIASTOLICA + "," + UPDATED_PRESION_ARTERIAL_DIASTOLICA
        );

        // Get all the fisiometria1List where presionArterialDiastolica equals to UPDATED_PRESION_ARTERIAL_DIASTOLICA
        defaultFisiometria1ShouldNotBeFound("presionArterialDiastolica.in=" + UPDATED_PRESION_ARTERIAL_DIASTOLICA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByPresionArterialDiastolicaIsNullOrNotNull() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where presionArterialDiastolica is not null
        defaultFisiometria1ShouldBeFound("presionArterialDiastolica.specified=true");

        // Get all the fisiometria1List where presionArterialDiastolica is null
        defaultFisiometria1ShouldNotBeFound("presionArterialDiastolica.specified=false");
    }

    @Test
    @Transactional
    void getAllFisiometria1sByPresionArterialDiastolicaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where presionArterialDiastolica is greater than or equal to DEFAULT_PRESION_ARTERIAL_DIASTOLICA
        defaultFisiometria1ShouldBeFound("presionArterialDiastolica.greaterThanOrEqual=" + DEFAULT_PRESION_ARTERIAL_DIASTOLICA);

        // Get all the fisiometria1List where presionArterialDiastolica is greater than or equal to UPDATED_PRESION_ARTERIAL_DIASTOLICA
        defaultFisiometria1ShouldNotBeFound("presionArterialDiastolica.greaterThanOrEqual=" + UPDATED_PRESION_ARTERIAL_DIASTOLICA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByPresionArterialDiastolicaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where presionArterialDiastolica is less than or equal to DEFAULT_PRESION_ARTERIAL_DIASTOLICA
        defaultFisiometria1ShouldBeFound("presionArterialDiastolica.lessThanOrEqual=" + DEFAULT_PRESION_ARTERIAL_DIASTOLICA);

        // Get all the fisiometria1List where presionArterialDiastolica is less than or equal to SMALLER_PRESION_ARTERIAL_DIASTOLICA
        defaultFisiometria1ShouldNotBeFound("presionArterialDiastolica.lessThanOrEqual=" + SMALLER_PRESION_ARTERIAL_DIASTOLICA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByPresionArterialDiastolicaIsLessThanSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where presionArterialDiastolica is less than DEFAULT_PRESION_ARTERIAL_DIASTOLICA
        defaultFisiometria1ShouldNotBeFound("presionArterialDiastolica.lessThan=" + DEFAULT_PRESION_ARTERIAL_DIASTOLICA);

        // Get all the fisiometria1List where presionArterialDiastolica is less than UPDATED_PRESION_ARTERIAL_DIASTOLICA
        defaultFisiometria1ShouldBeFound("presionArterialDiastolica.lessThan=" + UPDATED_PRESION_ARTERIAL_DIASTOLICA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByPresionArterialDiastolicaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where presionArterialDiastolica is greater than DEFAULT_PRESION_ARTERIAL_DIASTOLICA
        defaultFisiometria1ShouldNotBeFound("presionArterialDiastolica.greaterThan=" + DEFAULT_PRESION_ARTERIAL_DIASTOLICA);

        // Get all the fisiometria1List where presionArterialDiastolica is greater than SMALLER_PRESION_ARTERIAL_DIASTOLICA
        defaultFisiometria1ShouldBeFound("presionArterialDiastolica.greaterThan=" + SMALLER_PRESION_ARTERIAL_DIASTOLICA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByTemperaturaIsEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where temperatura equals to DEFAULT_TEMPERATURA
        defaultFisiometria1ShouldBeFound("temperatura.equals=" + DEFAULT_TEMPERATURA);

        // Get all the fisiometria1List where temperatura equals to UPDATED_TEMPERATURA
        defaultFisiometria1ShouldNotBeFound("temperatura.equals=" + UPDATED_TEMPERATURA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByTemperaturaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where temperatura not equals to DEFAULT_TEMPERATURA
        defaultFisiometria1ShouldNotBeFound("temperatura.notEquals=" + DEFAULT_TEMPERATURA);

        // Get all the fisiometria1List where temperatura not equals to UPDATED_TEMPERATURA
        defaultFisiometria1ShouldBeFound("temperatura.notEquals=" + UPDATED_TEMPERATURA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByTemperaturaIsInShouldWork() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where temperatura in DEFAULT_TEMPERATURA or UPDATED_TEMPERATURA
        defaultFisiometria1ShouldBeFound("temperatura.in=" + DEFAULT_TEMPERATURA + "," + UPDATED_TEMPERATURA);

        // Get all the fisiometria1List where temperatura equals to UPDATED_TEMPERATURA
        defaultFisiometria1ShouldNotBeFound("temperatura.in=" + UPDATED_TEMPERATURA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByTemperaturaIsNullOrNotNull() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where temperatura is not null
        defaultFisiometria1ShouldBeFound("temperatura.specified=true");

        // Get all the fisiometria1List where temperatura is null
        defaultFisiometria1ShouldNotBeFound("temperatura.specified=false");
    }

    @Test
    @Transactional
    void getAllFisiometria1sByTemperaturaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where temperatura is greater than or equal to DEFAULT_TEMPERATURA
        defaultFisiometria1ShouldBeFound("temperatura.greaterThanOrEqual=" + DEFAULT_TEMPERATURA);

        // Get all the fisiometria1List where temperatura is greater than or equal to UPDATED_TEMPERATURA
        defaultFisiometria1ShouldNotBeFound("temperatura.greaterThanOrEqual=" + UPDATED_TEMPERATURA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByTemperaturaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where temperatura is less than or equal to DEFAULT_TEMPERATURA
        defaultFisiometria1ShouldBeFound("temperatura.lessThanOrEqual=" + DEFAULT_TEMPERATURA);

        // Get all the fisiometria1List where temperatura is less than or equal to SMALLER_TEMPERATURA
        defaultFisiometria1ShouldNotBeFound("temperatura.lessThanOrEqual=" + SMALLER_TEMPERATURA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByTemperaturaIsLessThanSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where temperatura is less than DEFAULT_TEMPERATURA
        defaultFisiometria1ShouldNotBeFound("temperatura.lessThan=" + DEFAULT_TEMPERATURA);

        // Get all the fisiometria1List where temperatura is less than UPDATED_TEMPERATURA
        defaultFisiometria1ShouldBeFound("temperatura.lessThan=" + UPDATED_TEMPERATURA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByTemperaturaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where temperatura is greater than DEFAULT_TEMPERATURA
        defaultFisiometria1ShouldNotBeFound("temperatura.greaterThan=" + DEFAULT_TEMPERATURA);

        // Get all the fisiometria1List where temperatura is greater than SMALLER_TEMPERATURA
        defaultFisiometria1ShouldBeFound("temperatura.greaterThan=" + SMALLER_TEMPERATURA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByFechaRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where fechaRegistro equals to DEFAULT_FECHA_REGISTRO
        defaultFisiometria1ShouldBeFound("fechaRegistro.equals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the fisiometria1List where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultFisiometria1ShouldNotBeFound("fechaRegistro.equals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByFechaRegistroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where fechaRegistro not equals to DEFAULT_FECHA_REGISTRO
        defaultFisiometria1ShouldNotBeFound("fechaRegistro.notEquals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the fisiometria1List where fechaRegistro not equals to UPDATED_FECHA_REGISTRO
        defaultFisiometria1ShouldBeFound("fechaRegistro.notEquals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByFechaRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where fechaRegistro in DEFAULT_FECHA_REGISTRO or UPDATED_FECHA_REGISTRO
        defaultFisiometria1ShouldBeFound("fechaRegistro.in=" + DEFAULT_FECHA_REGISTRO + "," + UPDATED_FECHA_REGISTRO);

        // Get all the fisiometria1List where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultFisiometria1ShouldNotBeFound("fechaRegistro.in=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByFechaRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where fechaRegistro is not null
        defaultFisiometria1ShouldBeFound("fechaRegistro.specified=true");

        // Get all the fisiometria1List where fechaRegistro is null
        defaultFisiometria1ShouldNotBeFound("fechaRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllFisiometria1sByFechaTomaIsEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where fechaToma equals to DEFAULT_FECHA_TOMA
        defaultFisiometria1ShouldBeFound("fechaToma.equals=" + DEFAULT_FECHA_TOMA);

        // Get all the fisiometria1List where fechaToma equals to UPDATED_FECHA_TOMA
        defaultFisiometria1ShouldNotBeFound("fechaToma.equals=" + UPDATED_FECHA_TOMA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByFechaTomaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where fechaToma not equals to DEFAULT_FECHA_TOMA
        defaultFisiometria1ShouldNotBeFound("fechaToma.notEquals=" + DEFAULT_FECHA_TOMA);

        // Get all the fisiometria1List where fechaToma not equals to UPDATED_FECHA_TOMA
        defaultFisiometria1ShouldBeFound("fechaToma.notEquals=" + UPDATED_FECHA_TOMA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByFechaTomaIsInShouldWork() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where fechaToma in DEFAULT_FECHA_TOMA or UPDATED_FECHA_TOMA
        defaultFisiometria1ShouldBeFound("fechaToma.in=" + DEFAULT_FECHA_TOMA + "," + UPDATED_FECHA_TOMA);

        // Get all the fisiometria1List where fechaToma equals to UPDATED_FECHA_TOMA
        defaultFisiometria1ShouldNotBeFound("fechaToma.in=" + UPDATED_FECHA_TOMA);
    }

    @Test
    @Transactional
    void getAllFisiometria1sByFechaTomaIsNullOrNotNull() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        // Get all the fisiometria1List where fechaToma is not null
        defaultFisiometria1ShouldBeFound("fechaToma.specified=true");

        // Get all the fisiometria1List where fechaToma is null
        defaultFisiometria1ShouldNotBeFound("fechaToma.specified=false");
    }

    @Test
    @Transactional
    void getAllFisiometria1sByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        fisiometria1.setUser(user);
        fisiometria1Repository.saveAndFlush(fisiometria1);
        String userId = user.getId();

        // Get all the fisiometria1List where user equals to userId
        defaultFisiometria1ShouldBeFound("userId.equals=" + userId);

        // Get all the fisiometria1List where user equals to "invalid-id"
        defaultFisiometria1ShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFisiometria1ShouldBeFound(String filter) throws Exception {
        restFisiometria1MockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fisiometria1.getId().intValue())))
            .andExpect(jsonPath("$.[*].ritmoCardiaco").value(hasItem(DEFAULT_RITMO_CARDIACO)))
            .andExpect(jsonPath("$.[*].ritmoRespiratorio").value(hasItem(DEFAULT_RITMO_RESPIRATORIO)))
            .andExpect(jsonPath("$.[*].oximetria").value(hasItem(DEFAULT_OXIMETRIA)))
            .andExpect(jsonPath("$.[*].presionArterialSistolica").value(hasItem(DEFAULT_PRESION_ARTERIAL_SISTOLICA)))
            .andExpect(jsonPath("$.[*].presionArterialDiastolica").value(hasItem(DEFAULT_PRESION_ARTERIAL_DIASTOLICA)))
            .andExpect(jsonPath("$.[*].temperatura").value(hasItem(DEFAULT_TEMPERATURA.doubleValue())))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())))
            .andExpect(jsonPath("$.[*].fechaToma").value(hasItem(DEFAULT_FECHA_TOMA.toString())));

        // Check, that the count call also returns 1
        restFisiometria1MockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFisiometria1ShouldNotBeFound(String filter) throws Exception {
        restFisiometria1MockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFisiometria1MockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFisiometria1() throws Exception {
        // Get the fisiometria1
        restFisiometria1MockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFisiometria1() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        int databaseSizeBeforeUpdate = fisiometria1Repository.findAll().size();

        // Update the fisiometria1
        Fisiometria1 updatedFisiometria1 = fisiometria1Repository.findById(fisiometria1.getId()).get();
        // Disconnect from session so that the updates on updatedFisiometria1 are not directly saved in db
        em.detach(updatedFisiometria1);
        updatedFisiometria1
            .ritmoCardiaco(UPDATED_RITMO_CARDIACO)
            .ritmoRespiratorio(UPDATED_RITMO_RESPIRATORIO)
            .oximetria(UPDATED_OXIMETRIA)
            .presionArterialSistolica(UPDATED_PRESION_ARTERIAL_SISTOLICA)
            .presionArterialDiastolica(UPDATED_PRESION_ARTERIAL_DIASTOLICA)
            .temperatura(UPDATED_TEMPERATURA)
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .fechaToma(UPDATED_FECHA_TOMA);

        restFisiometria1MockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFisiometria1.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFisiometria1))
            )
            .andExpect(status().isOk());

        // Validate the Fisiometria1 in the database
        List<Fisiometria1> fisiometria1List = fisiometria1Repository.findAll();
        assertThat(fisiometria1List).hasSize(databaseSizeBeforeUpdate);
        Fisiometria1 testFisiometria1 = fisiometria1List.get(fisiometria1List.size() - 1);
        assertThat(testFisiometria1.getRitmoCardiaco()).isEqualTo(UPDATED_RITMO_CARDIACO);
        assertThat(testFisiometria1.getRitmoRespiratorio()).isEqualTo(UPDATED_RITMO_RESPIRATORIO);
        assertThat(testFisiometria1.getOximetria()).isEqualTo(UPDATED_OXIMETRIA);
        assertThat(testFisiometria1.getPresionArterialSistolica()).isEqualTo(UPDATED_PRESION_ARTERIAL_SISTOLICA);
        assertThat(testFisiometria1.getPresionArterialDiastolica()).isEqualTo(UPDATED_PRESION_ARTERIAL_DIASTOLICA);
        assertThat(testFisiometria1.getTemperatura()).isEqualTo(UPDATED_TEMPERATURA);
        assertThat(testFisiometria1.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
        assertThat(testFisiometria1.getFechaToma()).isEqualTo(UPDATED_FECHA_TOMA);
    }

    @Test
    @Transactional
    void putNonExistingFisiometria1() throws Exception {
        int databaseSizeBeforeUpdate = fisiometria1Repository.findAll().size();
        fisiometria1.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFisiometria1MockMvc
            .perform(
                put(ENTITY_API_URL_ID, fisiometria1.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fisiometria1))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fisiometria1 in the database
        List<Fisiometria1> fisiometria1List = fisiometria1Repository.findAll();
        assertThat(fisiometria1List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFisiometria1() throws Exception {
        int databaseSizeBeforeUpdate = fisiometria1Repository.findAll().size();
        fisiometria1.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFisiometria1MockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fisiometria1))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fisiometria1 in the database
        List<Fisiometria1> fisiometria1List = fisiometria1Repository.findAll();
        assertThat(fisiometria1List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFisiometria1() throws Exception {
        int databaseSizeBeforeUpdate = fisiometria1Repository.findAll().size();
        fisiometria1.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFisiometria1MockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fisiometria1))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fisiometria1 in the database
        List<Fisiometria1> fisiometria1List = fisiometria1Repository.findAll();
        assertThat(fisiometria1List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFisiometria1WithPatch() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        int databaseSizeBeforeUpdate = fisiometria1Repository.findAll().size();

        // Update the fisiometria1 using partial update
        Fisiometria1 partialUpdatedFisiometria1 = new Fisiometria1();
        partialUpdatedFisiometria1.setId(fisiometria1.getId());

        partialUpdatedFisiometria1
            .ritmoCardiaco(UPDATED_RITMO_CARDIACO)
            .ritmoRespiratorio(UPDATED_RITMO_RESPIRATORIO)
            .oximetria(UPDATED_OXIMETRIA)
            .presionArterialSistolica(UPDATED_PRESION_ARTERIAL_SISTOLICA)
            .fechaToma(UPDATED_FECHA_TOMA);

        restFisiometria1MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFisiometria1.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFisiometria1))
            )
            .andExpect(status().isOk());

        // Validate the Fisiometria1 in the database
        List<Fisiometria1> fisiometria1List = fisiometria1Repository.findAll();
        assertThat(fisiometria1List).hasSize(databaseSizeBeforeUpdate);
        Fisiometria1 testFisiometria1 = fisiometria1List.get(fisiometria1List.size() - 1);
        assertThat(testFisiometria1.getRitmoCardiaco()).isEqualTo(UPDATED_RITMO_CARDIACO);
        assertThat(testFisiometria1.getRitmoRespiratorio()).isEqualTo(UPDATED_RITMO_RESPIRATORIO);
        assertThat(testFisiometria1.getOximetria()).isEqualTo(UPDATED_OXIMETRIA);
        assertThat(testFisiometria1.getPresionArterialSistolica()).isEqualTo(UPDATED_PRESION_ARTERIAL_SISTOLICA);
        assertThat(testFisiometria1.getPresionArterialDiastolica()).isEqualTo(DEFAULT_PRESION_ARTERIAL_DIASTOLICA);
        assertThat(testFisiometria1.getTemperatura()).isEqualTo(DEFAULT_TEMPERATURA);
        assertThat(testFisiometria1.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
        assertThat(testFisiometria1.getFechaToma()).isEqualTo(UPDATED_FECHA_TOMA);
    }

    @Test
    @Transactional
    void fullUpdateFisiometria1WithPatch() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        int databaseSizeBeforeUpdate = fisiometria1Repository.findAll().size();

        // Update the fisiometria1 using partial update
        Fisiometria1 partialUpdatedFisiometria1 = new Fisiometria1();
        partialUpdatedFisiometria1.setId(fisiometria1.getId());

        partialUpdatedFisiometria1
            .ritmoCardiaco(UPDATED_RITMO_CARDIACO)
            .ritmoRespiratorio(UPDATED_RITMO_RESPIRATORIO)
            .oximetria(UPDATED_OXIMETRIA)
            .presionArterialSistolica(UPDATED_PRESION_ARTERIAL_SISTOLICA)
            .presionArterialDiastolica(UPDATED_PRESION_ARTERIAL_DIASTOLICA)
            .temperatura(UPDATED_TEMPERATURA)
            .fechaRegistro(UPDATED_FECHA_REGISTRO)
            .fechaToma(UPDATED_FECHA_TOMA);

        restFisiometria1MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFisiometria1.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFisiometria1))
            )
            .andExpect(status().isOk());

        // Validate the Fisiometria1 in the database
        List<Fisiometria1> fisiometria1List = fisiometria1Repository.findAll();
        assertThat(fisiometria1List).hasSize(databaseSizeBeforeUpdate);
        Fisiometria1 testFisiometria1 = fisiometria1List.get(fisiometria1List.size() - 1);
        assertThat(testFisiometria1.getRitmoCardiaco()).isEqualTo(UPDATED_RITMO_CARDIACO);
        assertThat(testFisiometria1.getRitmoRespiratorio()).isEqualTo(UPDATED_RITMO_RESPIRATORIO);
        assertThat(testFisiometria1.getOximetria()).isEqualTo(UPDATED_OXIMETRIA);
        assertThat(testFisiometria1.getPresionArterialSistolica()).isEqualTo(UPDATED_PRESION_ARTERIAL_SISTOLICA);
        assertThat(testFisiometria1.getPresionArterialDiastolica()).isEqualTo(UPDATED_PRESION_ARTERIAL_DIASTOLICA);
        assertThat(testFisiometria1.getTemperatura()).isEqualTo(UPDATED_TEMPERATURA);
        assertThat(testFisiometria1.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
        assertThat(testFisiometria1.getFechaToma()).isEqualTo(UPDATED_FECHA_TOMA);
    }

    @Test
    @Transactional
    void patchNonExistingFisiometria1() throws Exception {
        int databaseSizeBeforeUpdate = fisiometria1Repository.findAll().size();
        fisiometria1.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFisiometria1MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fisiometria1.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fisiometria1))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fisiometria1 in the database
        List<Fisiometria1> fisiometria1List = fisiometria1Repository.findAll();
        assertThat(fisiometria1List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFisiometria1() throws Exception {
        int databaseSizeBeforeUpdate = fisiometria1Repository.findAll().size();
        fisiometria1.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFisiometria1MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fisiometria1))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fisiometria1 in the database
        List<Fisiometria1> fisiometria1List = fisiometria1Repository.findAll();
        assertThat(fisiometria1List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFisiometria1() throws Exception {
        int databaseSizeBeforeUpdate = fisiometria1Repository.findAll().size();
        fisiometria1.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFisiometria1MockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fisiometria1))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fisiometria1 in the database
        List<Fisiometria1> fisiometria1List = fisiometria1Repository.findAll();
        assertThat(fisiometria1List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFisiometria1() throws Exception {
        // Initialize the database
        fisiometria1Repository.saveAndFlush(fisiometria1);

        int databaseSizeBeforeDelete = fisiometria1Repository.findAll().size();

        // Delete the fisiometria1
        restFisiometria1MockMvc
            .perform(delete(ENTITY_API_URL_ID, fisiometria1.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fisiometria1> fisiometria1List = fisiometria1Repository.findAll();
        assertThat(fisiometria1List).hasSize(databaseSizeBeforeDelete - 1);
    }
}

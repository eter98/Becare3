package com.be4tech.becare3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.PresionSanguinea;
import com.be4tech.becare3.domain.User;
import com.be4tech.becare3.repository.PresionSanguineaRepository;
import com.be4tech.becare3.service.criteria.PresionSanguineaCriteria;
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
 * Integration tests for the {@link PresionSanguineaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PresionSanguineaResourceIT {

    private static final Integer DEFAULT_PRESION_SANGUINEA_SISTOLICA = 1;
    private static final Integer UPDATED_PRESION_SANGUINEA_SISTOLICA = 2;
    private static final Integer SMALLER_PRESION_SANGUINEA_SISTOLICA = 1 - 1;

    private static final Integer DEFAULT_PRESION_SANGUINEA_DIASTOLICA = 1;
    private static final Integer UPDATED_PRESION_SANGUINEA_DIASTOLICA = 2;
    private static final Integer SMALLER_PRESION_SANGUINEA_DIASTOLICA = 1 - 1;

    private static final Instant DEFAULT_FECHA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/presion-sanguineas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PresionSanguineaRepository presionSanguineaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPresionSanguineaMockMvc;

    private PresionSanguinea presionSanguinea;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PresionSanguinea createEntity(EntityManager em) {
        PresionSanguinea presionSanguinea = new PresionSanguinea()
            .presionSanguineaSistolica(DEFAULT_PRESION_SANGUINEA_SISTOLICA)
            .presionSanguineaDiastolica(DEFAULT_PRESION_SANGUINEA_DIASTOLICA)
            .fechaRegistro(DEFAULT_FECHA_REGISTRO);
        return presionSanguinea;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PresionSanguinea createUpdatedEntity(EntityManager em) {
        PresionSanguinea presionSanguinea = new PresionSanguinea()
            .presionSanguineaSistolica(UPDATED_PRESION_SANGUINEA_SISTOLICA)
            .presionSanguineaDiastolica(UPDATED_PRESION_SANGUINEA_DIASTOLICA)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);
        return presionSanguinea;
    }

    @BeforeEach
    public void initTest() {
        presionSanguinea = createEntity(em);
    }

    @Test
    @Transactional
    void createPresionSanguinea() throws Exception {
        int databaseSizeBeforeCreate = presionSanguineaRepository.findAll().size();
        // Create the PresionSanguinea
        restPresionSanguineaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(presionSanguinea))
            )
            .andExpect(status().isCreated());

        // Validate the PresionSanguinea in the database
        List<PresionSanguinea> presionSanguineaList = presionSanguineaRepository.findAll();
        assertThat(presionSanguineaList).hasSize(databaseSizeBeforeCreate + 1);
        PresionSanguinea testPresionSanguinea = presionSanguineaList.get(presionSanguineaList.size() - 1);
        assertThat(testPresionSanguinea.getPresionSanguineaSistolica()).isEqualTo(DEFAULT_PRESION_SANGUINEA_SISTOLICA);
        assertThat(testPresionSanguinea.getPresionSanguineaDiastolica()).isEqualTo(DEFAULT_PRESION_SANGUINEA_DIASTOLICA);
        assertThat(testPresionSanguinea.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void createPresionSanguineaWithExistingId() throws Exception {
        // Create the PresionSanguinea with an existing ID
        presionSanguinea.setId(1L);

        int databaseSizeBeforeCreate = presionSanguineaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPresionSanguineaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(presionSanguinea))
            )
            .andExpect(status().isBadRequest());

        // Validate the PresionSanguinea in the database
        List<PresionSanguinea> presionSanguineaList = presionSanguineaRepository.findAll();
        assertThat(presionSanguineaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPresionSanguineas() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        // Get all the presionSanguineaList
        restPresionSanguineaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(presionSanguinea.getId().intValue())))
            .andExpect(jsonPath("$.[*].presionSanguineaSistolica").value(hasItem(DEFAULT_PRESION_SANGUINEA_SISTOLICA)))
            .andExpect(jsonPath("$.[*].presionSanguineaDiastolica").value(hasItem(DEFAULT_PRESION_SANGUINEA_DIASTOLICA)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));
    }

    @Test
    @Transactional
    void getPresionSanguinea() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        // Get the presionSanguinea
        restPresionSanguineaMockMvc
            .perform(get(ENTITY_API_URL_ID, presionSanguinea.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(presionSanguinea.getId().intValue()))
            .andExpect(jsonPath("$.presionSanguineaSistolica").value(DEFAULT_PRESION_SANGUINEA_SISTOLICA))
            .andExpect(jsonPath("$.presionSanguineaDiastolica").value(DEFAULT_PRESION_SANGUINEA_DIASTOLICA))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()));
    }

    @Test
    @Transactional
    void getPresionSanguineasByIdFiltering() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        Long id = presionSanguinea.getId();

        defaultPresionSanguineaShouldBeFound("id.equals=" + id);
        defaultPresionSanguineaShouldNotBeFound("id.notEquals=" + id);

        defaultPresionSanguineaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPresionSanguineaShouldNotBeFound("id.greaterThan=" + id);

        defaultPresionSanguineaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPresionSanguineaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPresionSanguineasByPresionSanguineaSistolicaIsEqualToSomething() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        // Get all the presionSanguineaList where presionSanguineaSistolica equals to DEFAULT_PRESION_SANGUINEA_SISTOLICA
        defaultPresionSanguineaShouldBeFound("presionSanguineaSistolica.equals=" + DEFAULT_PRESION_SANGUINEA_SISTOLICA);

        // Get all the presionSanguineaList where presionSanguineaSistolica equals to UPDATED_PRESION_SANGUINEA_SISTOLICA
        defaultPresionSanguineaShouldNotBeFound("presionSanguineaSistolica.equals=" + UPDATED_PRESION_SANGUINEA_SISTOLICA);
    }

    @Test
    @Transactional
    void getAllPresionSanguineasByPresionSanguineaSistolicaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        // Get all the presionSanguineaList where presionSanguineaSistolica not equals to DEFAULT_PRESION_SANGUINEA_SISTOLICA
        defaultPresionSanguineaShouldNotBeFound("presionSanguineaSistolica.notEquals=" + DEFAULT_PRESION_SANGUINEA_SISTOLICA);

        // Get all the presionSanguineaList where presionSanguineaSistolica not equals to UPDATED_PRESION_SANGUINEA_SISTOLICA
        defaultPresionSanguineaShouldBeFound("presionSanguineaSistolica.notEquals=" + UPDATED_PRESION_SANGUINEA_SISTOLICA);
    }

    @Test
    @Transactional
    void getAllPresionSanguineasByPresionSanguineaSistolicaIsInShouldWork() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        // Get all the presionSanguineaList where presionSanguineaSistolica in DEFAULT_PRESION_SANGUINEA_SISTOLICA or UPDATED_PRESION_SANGUINEA_SISTOLICA
        defaultPresionSanguineaShouldBeFound(
            "presionSanguineaSistolica.in=" + DEFAULT_PRESION_SANGUINEA_SISTOLICA + "," + UPDATED_PRESION_SANGUINEA_SISTOLICA
        );

        // Get all the presionSanguineaList where presionSanguineaSistolica equals to UPDATED_PRESION_SANGUINEA_SISTOLICA
        defaultPresionSanguineaShouldNotBeFound("presionSanguineaSistolica.in=" + UPDATED_PRESION_SANGUINEA_SISTOLICA);
    }

    @Test
    @Transactional
    void getAllPresionSanguineasByPresionSanguineaSistolicaIsNullOrNotNull() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        // Get all the presionSanguineaList where presionSanguineaSistolica is not null
        defaultPresionSanguineaShouldBeFound("presionSanguineaSistolica.specified=true");

        // Get all the presionSanguineaList where presionSanguineaSistolica is null
        defaultPresionSanguineaShouldNotBeFound("presionSanguineaSistolica.specified=false");
    }

    @Test
    @Transactional
    void getAllPresionSanguineasByPresionSanguineaSistolicaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        // Get all the presionSanguineaList where presionSanguineaSistolica is greater than or equal to DEFAULT_PRESION_SANGUINEA_SISTOLICA
        defaultPresionSanguineaShouldBeFound("presionSanguineaSistolica.greaterThanOrEqual=" + DEFAULT_PRESION_SANGUINEA_SISTOLICA);

        // Get all the presionSanguineaList where presionSanguineaSistolica is greater than or equal to UPDATED_PRESION_SANGUINEA_SISTOLICA
        defaultPresionSanguineaShouldNotBeFound("presionSanguineaSistolica.greaterThanOrEqual=" + UPDATED_PRESION_SANGUINEA_SISTOLICA);
    }

    @Test
    @Transactional
    void getAllPresionSanguineasByPresionSanguineaSistolicaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        // Get all the presionSanguineaList where presionSanguineaSistolica is less than or equal to DEFAULT_PRESION_SANGUINEA_SISTOLICA
        defaultPresionSanguineaShouldBeFound("presionSanguineaSistolica.lessThanOrEqual=" + DEFAULT_PRESION_SANGUINEA_SISTOLICA);

        // Get all the presionSanguineaList where presionSanguineaSistolica is less than or equal to SMALLER_PRESION_SANGUINEA_SISTOLICA
        defaultPresionSanguineaShouldNotBeFound("presionSanguineaSistolica.lessThanOrEqual=" + SMALLER_PRESION_SANGUINEA_SISTOLICA);
    }

    @Test
    @Transactional
    void getAllPresionSanguineasByPresionSanguineaSistolicaIsLessThanSomething() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        // Get all the presionSanguineaList where presionSanguineaSistolica is less than DEFAULT_PRESION_SANGUINEA_SISTOLICA
        defaultPresionSanguineaShouldNotBeFound("presionSanguineaSistolica.lessThan=" + DEFAULT_PRESION_SANGUINEA_SISTOLICA);

        // Get all the presionSanguineaList where presionSanguineaSistolica is less than UPDATED_PRESION_SANGUINEA_SISTOLICA
        defaultPresionSanguineaShouldBeFound("presionSanguineaSistolica.lessThan=" + UPDATED_PRESION_SANGUINEA_SISTOLICA);
    }

    @Test
    @Transactional
    void getAllPresionSanguineasByPresionSanguineaSistolicaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        // Get all the presionSanguineaList where presionSanguineaSistolica is greater than DEFAULT_PRESION_SANGUINEA_SISTOLICA
        defaultPresionSanguineaShouldNotBeFound("presionSanguineaSistolica.greaterThan=" + DEFAULT_PRESION_SANGUINEA_SISTOLICA);

        // Get all the presionSanguineaList where presionSanguineaSistolica is greater than SMALLER_PRESION_SANGUINEA_SISTOLICA
        defaultPresionSanguineaShouldBeFound("presionSanguineaSistolica.greaterThan=" + SMALLER_PRESION_SANGUINEA_SISTOLICA);
    }

    @Test
    @Transactional
    void getAllPresionSanguineasByPresionSanguineaDiastolicaIsEqualToSomething() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        // Get all the presionSanguineaList where presionSanguineaDiastolica equals to DEFAULT_PRESION_SANGUINEA_DIASTOLICA
        defaultPresionSanguineaShouldBeFound("presionSanguineaDiastolica.equals=" + DEFAULT_PRESION_SANGUINEA_DIASTOLICA);

        // Get all the presionSanguineaList where presionSanguineaDiastolica equals to UPDATED_PRESION_SANGUINEA_DIASTOLICA
        defaultPresionSanguineaShouldNotBeFound("presionSanguineaDiastolica.equals=" + UPDATED_PRESION_SANGUINEA_DIASTOLICA);
    }

    @Test
    @Transactional
    void getAllPresionSanguineasByPresionSanguineaDiastolicaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        // Get all the presionSanguineaList where presionSanguineaDiastolica not equals to DEFAULT_PRESION_SANGUINEA_DIASTOLICA
        defaultPresionSanguineaShouldNotBeFound("presionSanguineaDiastolica.notEquals=" + DEFAULT_PRESION_SANGUINEA_DIASTOLICA);

        // Get all the presionSanguineaList where presionSanguineaDiastolica not equals to UPDATED_PRESION_SANGUINEA_DIASTOLICA
        defaultPresionSanguineaShouldBeFound("presionSanguineaDiastolica.notEquals=" + UPDATED_PRESION_SANGUINEA_DIASTOLICA);
    }

    @Test
    @Transactional
    void getAllPresionSanguineasByPresionSanguineaDiastolicaIsInShouldWork() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        // Get all the presionSanguineaList where presionSanguineaDiastolica in DEFAULT_PRESION_SANGUINEA_DIASTOLICA or UPDATED_PRESION_SANGUINEA_DIASTOLICA
        defaultPresionSanguineaShouldBeFound(
            "presionSanguineaDiastolica.in=" + DEFAULT_PRESION_SANGUINEA_DIASTOLICA + "," + UPDATED_PRESION_SANGUINEA_DIASTOLICA
        );

        // Get all the presionSanguineaList where presionSanguineaDiastolica equals to UPDATED_PRESION_SANGUINEA_DIASTOLICA
        defaultPresionSanguineaShouldNotBeFound("presionSanguineaDiastolica.in=" + UPDATED_PRESION_SANGUINEA_DIASTOLICA);
    }

    @Test
    @Transactional
    void getAllPresionSanguineasByPresionSanguineaDiastolicaIsNullOrNotNull() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        // Get all the presionSanguineaList where presionSanguineaDiastolica is not null
        defaultPresionSanguineaShouldBeFound("presionSanguineaDiastolica.specified=true");

        // Get all the presionSanguineaList where presionSanguineaDiastolica is null
        defaultPresionSanguineaShouldNotBeFound("presionSanguineaDiastolica.specified=false");
    }

    @Test
    @Transactional
    void getAllPresionSanguineasByPresionSanguineaDiastolicaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        // Get all the presionSanguineaList where presionSanguineaDiastolica is greater than or equal to DEFAULT_PRESION_SANGUINEA_DIASTOLICA
        defaultPresionSanguineaShouldBeFound("presionSanguineaDiastolica.greaterThanOrEqual=" + DEFAULT_PRESION_SANGUINEA_DIASTOLICA);

        // Get all the presionSanguineaList where presionSanguineaDiastolica is greater than or equal to UPDATED_PRESION_SANGUINEA_DIASTOLICA
        defaultPresionSanguineaShouldNotBeFound("presionSanguineaDiastolica.greaterThanOrEqual=" + UPDATED_PRESION_SANGUINEA_DIASTOLICA);
    }

    @Test
    @Transactional
    void getAllPresionSanguineasByPresionSanguineaDiastolicaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        // Get all the presionSanguineaList where presionSanguineaDiastolica is less than or equal to DEFAULT_PRESION_SANGUINEA_DIASTOLICA
        defaultPresionSanguineaShouldBeFound("presionSanguineaDiastolica.lessThanOrEqual=" + DEFAULT_PRESION_SANGUINEA_DIASTOLICA);

        // Get all the presionSanguineaList where presionSanguineaDiastolica is less than or equal to SMALLER_PRESION_SANGUINEA_DIASTOLICA
        defaultPresionSanguineaShouldNotBeFound("presionSanguineaDiastolica.lessThanOrEqual=" + SMALLER_PRESION_SANGUINEA_DIASTOLICA);
    }

    @Test
    @Transactional
    void getAllPresionSanguineasByPresionSanguineaDiastolicaIsLessThanSomething() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        // Get all the presionSanguineaList where presionSanguineaDiastolica is less than DEFAULT_PRESION_SANGUINEA_DIASTOLICA
        defaultPresionSanguineaShouldNotBeFound("presionSanguineaDiastolica.lessThan=" + DEFAULT_PRESION_SANGUINEA_DIASTOLICA);

        // Get all the presionSanguineaList where presionSanguineaDiastolica is less than UPDATED_PRESION_SANGUINEA_DIASTOLICA
        defaultPresionSanguineaShouldBeFound("presionSanguineaDiastolica.lessThan=" + UPDATED_PRESION_SANGUINEA_DIASTOLICA);
    }

    @Test
    @Transactional
    void getAllPresionSanguineasByPresionSanguineaDiastolicaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        // Get all the presionSanguineaList where presionSanguineaDiastolica is greater than DEFAULT_PRESION_SANGUINEA_DIASTOLICA
        defaultPresionSanguineaShouldNotBeFound("presionSanguineaDiastolica.greaterThan=" + DEFAULT_PRESION_SANGUINEA_DIASTOLICA);

        // Get all the presionSanguineaList where presionSanguineaDiastolica is greater than SMALLER_PRESION_SANGUINEA_DIASTOLICA
        defaultPresionSanguineaShouldBeFound("presionSanguineaDiastolica.greaterThan=" + SMALLER_PRESION_SANGUINEA_DIASTOLICA);
    }

    @Test
    @Transactional
    void getAllPresionSanguineasByFechaRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        // Get all the presionSanguineaList where fechaRegistro equals to DEFAULT_FECHA_REGISTRO
        defaultPresionSanguineaShouldBeFound("fechaRegistro.equals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the presionSanguineaList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultPresionSanguineaShouldNotBeFound("fechaRegistro.equals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllPresionSanguineasByFechaRegistroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        // Get all the presionSanguineaList where fechaRegistro not equals to DEFAULT_FECHA_REGISTRO
        defaultPresionSanguineaShouldNotBeFound("fechaRegistro.notEquals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the presionSanguineaList where fechaRegistro not equals to UPDATED_FECHA_REGISTRO
        defaultPresionSanguineaShouldBeFound("fechaRegistro.notEquals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllPresionSanguineasByFechaRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        // Get all the presionSanguineaList where fechaRegistro in DEFAULT_FECHA_REGISTRO or UPDATED_FECHA_REGISTRO
        defaultPresionSanguineaShouldBeFound("fechaRegistro.in=" + DEFAULT_FECHA_REGISTRO + "," + UPDATED_FECHA_REGISTRO);

        // Get all the presionSanguineaList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultPresionSanguineaShouldNotBeFound("fechaRegistro.in=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllPresionSanguineasByFechaRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        // Get all the presionSanguineaList where fechaRegistro is not null
        defaultPresionSanguineaShouldBeFound("fechaRegistro.specified=true");

        // Get all the presionSanguineaList where fechaRegistro is null
        defaultPresionSanguineaShouldNotBeFound("fechaRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllPresionSanguineasByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        presionSanguinea.setUser(user);
        presionSanguineaRepository.saveAndFlush(presionSanguinea);
        String userId = user.getId();

        // Get all the presionSanguineaList where user equals to userId
        defaultPresionSanguineaShouldBeFound("userId.equals=" + userId);

        // Get all the presionSanguineaList where user equals to "invalid-id"
        defaultPresionSanguineaShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPresionSanguineaShouldBeFound(String filter) throws Exception {
        restPresionSanguineaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(presionSanguinea.getId().intValue())))
            .andExpect(jsonPath("$.[*].presionSanguineaSistolica").value(hasItem(DEFAULT_PRESION_SANGUINEA_SISTOLICA)))
            .andExpect(jsonPath("$.[*].presionSanguineaDiastolica").value(hasItem(DEFAULT_PRESION_SANGUINEA_DIASTOLICA)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));

        // Check, that the count call also returns 1
        restPresionSanguineaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPresionSanguineaShouldNotBeFound(String filter) throws Exception {
        restPresionSanguineaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPresionSanguineaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPresionSanguinea() throws Exception {
        // Get the presionSanguinea
        restPresionSanguineaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPresionSanguinea() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        int databaseSizeBeforeUpdate = presionSanguineaRepository.findAll().size();

        // Update the presionSanguinea
        PresionSanguinea updatedPresionSanguinea = presionSanguineaRepository.findById(presionSanguinea.getId()).get();
        // Disconnect from session so that the updates on updatedPresionSanguinea are not directly saved in db
        em.detach(updatedPresionSanguinea);
        updatedPresionSanguinea
            .presionSanguineaSistolica(UPDATED_PRESION_SANGUINEA_SISTOLICA)
            .presionSanguineaDiastolica(UPDATED_PRESION_SANGUINEA_DIASTOLICA)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restPresionSanguineaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPresionSanguinea.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPresionSanguinea))
            )
            .andExpect(status().isOk());

        // Validate the PresionSanguinea in the database
        List<PresionSanguinea> presionSanguineaList = presionSanguineaRepository.findAll();
        assertThat(presionSanguineaList).hasSize(databaseSizeBeforeUpdate);
        PresionSanguinea testPresionSanguinea = presionSanguineaList.get(presionSanguineaList.size() - 1);
        assertThat(testPresionSanguinea.getPresionSanguineaSistolica()).isEqualTo(UPDATED_PRESION_SANGUINEA_SISTOLICA);
        assertThat(testPresionSanguinea.getPresionSanguineaDiastolica()).isEqualTo(UPDATED_PRESION_SANGUINEA_DIASTOLICA);
        assertThat(testPresionSanguinea.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void putNonExistingPresionSanguinea() throws Exception {
        int databaseSizeBeforeUpdate = presionSanguineaRepository.findAll().size();
        presionSanguinea.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPresionSanguineaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, presionSanguinea.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(presionSanguinea))
            )
            .andExpect(status().isBadRequest());

        // Validate the PresionSanguinea in the database
        List<PresionSanguinea> presionSanguineaList = presionSanguineaRepository.findAll();
        assertThat(presionSanguineaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPresionSanguinea() throws Exception {
        int databaseSizeBeforeUpdate = presionSanguineaRepository.findAll().size();
        presionSanguinea.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresionSanguineaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(presionSanguinea))
            )
            .andExpect(status().isBadRequest());

        // Validate the PresionSanguinea in the database
        List<PresionSanguinea> presionSanguineaList = presionSanguineaRepository.findAll();
        assertThat(presionSanguineaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPresionSanguinea() throws Exception {
        int databaseSizeBeforeUpdate = presionSanguineaRepository.findAll().size();
        presionSanguinea.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresionSanguineaMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(presionSanguinea))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PresionSanguinea in the database
        List<PresionSanguinea> presionSanguineaList = presionSanguineaRepository.findAll();
        assertThat(presionSanguineaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePresionSanguineaWithPatch() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        int databaseSizeBeforeUpdate = presionSanguineaRepository.findAll().size();

        // Update the presionSanguinea using partial update
        PresionSanguinea partialUpdatedPresionSanguinea = new PresionSanguinea();
        partialUpdatedPresionSanguinea.setId(presionSanguinea.getId());

        partialUpdatedPresionSanguinea
            .presionSanguineaDiastolica(UPDATED_PRESION_SANGUINEA_DIASTOLICA)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restPresionSanguineaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPresionSanguinea.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPresionSanguinea))
            )
            .andExpect(status().isOk());

        // Validate the PresionSanguinea in the database
        List<PresionSanguinea> presionSanguineaList = presionSanguineaRepository.findAll();
        assertThat(presionSanguineaList).hasSize(databaseSizeBeforeUpdate);
        PresionSanguinea testPresionSanguinea = presionSanguineaList.get(presionSanguineaList.size() - 1);
        assertThat(testPresionSanguinea.getPresionSanguineaSistolica()).isEqualTo(DEFAULT_PRESION_SANGUINEA_SISTOLICA);
        assertThat(testPresionSanguinea.getPresionSanguineaDiastolica()).isEqualTo(UPDATED_PRESION_SANGUINEA_DIASTOLICA);
        assertThat(testPresionSanguinea.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void fullUpdatePresionSanguineaWithPatch() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        int databaseSizeBeforeUpdate = presionSanguineaRepository.findAll().size();

        // Update the presionSanguinea using partial update
        PresionSanguinea partialUpdatedPresionSanguinea = new PresionSanguinea();
        partialUpdatedPresionSanguinea.setId(presionSanguinea.getId());

        partialUpdatedPresionSanguinea
            .presionSanguineaSistolica(UPDATED_PRESION_SANGUINEA_SISTOLICA)
            .presionSanguineaDiastolica(UPDATED_PRESION_SANGUINEA_DIASTOLICA)
            .fechaRegistro(UPDATED_FECHA_REGISTRO);

        restPresionSanguineaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPresionSanguinea.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPresionSanguinea))
            )
            .andExpect(status().isOk());

        // Validate the PresionSanguinea in the database
        List<PresionSanguinea> presionSanguineaList = presionSanguineaRepository.findAll();
        assertThat(presionSanguineaList).hasSize(databaseSizeBeforeUpdate);
        PresionSanguinea testPresionSanguinea = presionSanguineaList.get(presionSanguineaList.size() - 1);
        assertThat(testPresionSanguinea.getPresionSanguineaSistolica()).isEqualTo(UPDATED_PRESION_SANGUINEA_SISTOLICA);
        assertThat(testPresionSanguinea.getPresionSanguineaDiastolica()).isEqualTo(UPDATED_PRESION_SANGUINEA_DIASTOLICA);
        assertThat(testPresionSanguinea.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void patchNonExistingPresionSanguinea() throws Exception {
        int databaseSizeBeforeUpdate = presionSanguineaRepository.findAll().size();
        presionSanguinea.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPresionSanguineaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, presionSanguinea.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(presionSanguinea))
            )
            .andExpect(status().isBadRequest());

        // Validate the PresionSanguinea in the database
        List<PresionSanguinea> presionSanguineaList = presionSanguineaRepository.findAll();
        assertThat(presionSanguineaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPresionSanguinea() throws Exception {
        int databaseSizeBeforeUpdate = presionSanguineaRepository.findAll().size();
        presionSanguinea.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresionSanguineaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(presionSanguinea))
            )
            .andExpect(status().isBadRequest());

        // Validate the PresionSanguinea in the database
        List<PresionSanguinea> presionSanguineaList = presionSanguineaRepository.findAll();
        assertThat(presionSanguineaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPresionSanguinea() throws Exception {
        int databaseSizeBeforeUpdate = presionSanguineaRepository.findAll().size();
        presionSanguinea.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPresionSanguineaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(presionSanguinea))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PresionSanguinea in the database
        List<PresionSanguinea> presionSanguineaList = presionSanguineaRepository.findAll();
        assertThat(presionSanguineaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePresionSanguinea() throws Exception {
        // Initialize the database
        presionSanguineaRepository.saveAndFlush(presionSanguinea);

        int databaseSizeBeforeDelete = presionSanguineaRepository.findAll().size();

        // Delete the presionSanguinea
        restPresionSanguineaMockMvc
            .perform(delete(ENTITY_API_URL_ID, presionSanguinea.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PresionSanguinea> presionSanguineaList = presionSanguineaRepository.findAll();
        assertThat(presionSanguineaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

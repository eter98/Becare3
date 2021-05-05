package com.be4tech.becare3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.Sueno;
import com.be4tech.becare3.domain.User;
import com.be4tech.becare3.repository.SuenoRepository;
import com.be4tech.becare3.service.criteria.SuenoCriteria;
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
 * Integration tests for the {@link SuenoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SuenoResourceIT {

    private static final Integer DEFAULT_SUPERFICIAL = 1;
    private static final Integer UPDATED_SUPERFICIAL = 2;
    private static final Integer SMALLER_SUPERFICIAL = 1 - 1;

    private static final Integer DEFAULT_PROFUNDO = 1;
    private static final Integer UPDATED_PROFUNDO = 2;
    private static final Integer SMALLER_PROFUNDO = 1 - 1;

    private static final Integer DEFAULT_DESPIERTO = 1;
    private static final Integer UPDATED_DESPIERTO = 2;
    private static final Integer SMALLER_DESPIERTO = 1 - 1;

    private static final Instant DEFAULT_TIME_INSTANT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME_INSTANT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/suenos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SuenoRepository suenoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSuenoMockMvc;

    private Sueno sueno;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sueno createEntity(EntityManager em) {
        Sueno sueno = new Sueno()
            .superficial(DEFAULT_SUPERFICIAL)
            .profundo(DEFAULT_PROFUNDO)
            .despierto(DEFAULT_DESPIERTO)
            .timeInstant(DEFAULT_TIME_INSTANT);
        return sueno;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sueno createUpdatedEntity(EntityManager em) {
        Sueno sueno = new Sueno()
            .superficial(UPDATED_SUPERFICIAL)
            .profundo(UPDATED_PROFUNDO)
            .despierto(UPDATED_DESPIERTO)
            .timeInstant(UPDATED_TIME_INSTANT);
        return sueno;
    }

    @BeforeEach
    public void initTest() {
        sueno = createEntity(em);
    }

    @Test
    @Transactional
    void createSueno() throws Exception {
        int databaseSizeBeforeCreate = suenoRepository.findAll().size();
        // Create the Sueno
        restSuenoMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sueno))
            )
            .andExpect(status().isCreated());

        // Validate the Sueno in the database
        List<Sueno> suenoList = suenoRepository.findAll();
        assertThat(suenoList).hasSize(databaseSizeBeforeCreate + 1);
        Sueno testSueno = suenoList.get(suenoList.size() - 1);
        assertThat(testSueno.getSuperficial()).isEqualTo(DEFAULT_SUPERFICIAL);
        assertThat(testSueno.getProfundo()).isEqualTo(DEFAULT_PROFUNDO);
        assertThat(testSueno.getDespierto()).isEqualTo(DEFAULT_DESPIERTO);
        assertThat(testSueno.getTimeInstant()).isEqualTo(DEFAULT_TIME_INSTANT);
    }

    @Test
    @Transactional
    void createSuenoWithExistingId() throws Exception {
        // Create the Sueno with an existing ID
        sueno.setId(1L);

        int databaseSizeBeforeCreate = suenoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSuenoMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sueno))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sueno in the database
        List<Sueno> suenoList = suenoRepository.findAll();
        assertThat(suenoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSuenos() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList
        restSuenoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sueno.getId().intValue())))
            .andExpect(jsonPath("$.[*].superficial").value(hasItem(DEFAULT_SUPERFICIAL)))
            .andExpect(jsonPath("$.[*].profundo").value(hasItem(DEFAULT_PROFUNDO)))
            .andExpect(jsonPath("$.[*].despierto").value(hasItem(DEFAULT_DESPIERTO)))
            .andExpect(jsonPath("$.[*].timeInstant").value(hasItem(DEFAULT_TIME_INSTANT.toString())));
    }

    @Test
    @Transactional
    void getSueno() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get the sueno
        restSuenoMockMvc
            .perform(get(ENTITY_API_URL_ID, sueno.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sueno.getId().intValue()))
            .andExpect(jsonPath("$.superficial").value(DEFAULT_SUPERFICIAL))
            .andExpect(jsonPath("$.profundo").value(DEFAULT_PROFUNDO))
            .andExpect(jsonPath("$.despierto").value(DEFAULT_DESPIERTO))
            .andExpect(jsonPath("$.timeInstant").value(DEFAULT_TIME_INSTANT.toString()));
    }

    @Test
    @Transactional
    void getSuenosByIdFiltering() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        Long id = sueno.getId();

        defaultSuenoShouldBeFound("id.equals=" + id);
        defaultSuenoShouldNotBeFound("id.notEquals=" + id);

        defaultSuenoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSuenoShouldNotBeFound("id.greaterThan=" + id);

        defaultSuenoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSuenoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSuenosBySuperficialIsEqualToSomething() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where superficial equals to DEFAULT_SUPERFICIAL
        defaultSuenoShouldBeFound("superficial.equals=" + DEFAULT_SUPERFICIAL);

        // Get all the suenoList where superficial equals to UPDATED_SUPERFICIAL
        defaultSuenoShouldNotBeFound("superficial.equals=" + UPDATED_SUPERFICIAL);
    }

    @Test
    @Transactional
    void getAllSuenosBySuperficialIsNotEqualToSomething() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where superficial not equals to DEFAULT_SUPERFICIAL
        defaultSuenoShouldNotBeFound("superficial.notEquals=" + DEFAULT_SUPERFICIAL);

        // Get all the suenoList where superficial not equals to UPDATED_SUPERFICIAL
        defaultSuenoShouldBeFound("superficial.notEquals=" + UPDATED_SUPERFICIAL);
    }

    @Test
    @Transactional
    void getAllSuenosBySuperficialIsInShouldWork() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where superficial in DEFAULT_SUPERFICIAL or UPDATED_SUPERFICIAL
        defaultSuenoShouldBeFound("superficial.in=" + DEFAULT_SUPERFICIAL + "," + UPDATED_SUPERFICIAL);

        // Get all the suenoList where superficial equals to UPDATED_SUPERFICIAL
        defaultSuenoShouldNotBeFound("superficial.in=" + UPDATED_SUPERFICIAL);
    }

    @Test
    @Transactional
    void getAllSuenosBySuperficialIsNullOrNotNull() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where superficial is not null
        defaultSuenoShouldBeFound("superficial.specified=true");

        // Get all the suenoList where superficial is null
        defaultSuenoShouldNotBeFound("superficial.specified=false");
    }

    @Test
    @Transactional
    void getAllSuenosBySuperficialIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where superficial is greater than or equal to DEFAULT_SUPERFICIAL
        defaultSuenoShouldBeFound("superficial.greaterThanOrEqual=" + DEFAULT_SUPERFICIAL);

        // Get all the suenoList where superficial is greater than or equal to UPDATED_SUPERFICIAL
        defaultSuenoShouldNotBeFound("superficial.greaterThanOrEqual=" + UPDATED_SUPERFICIAL);
    }

    @Test
    @Transactional
    void getAllSuenosBySuperficialIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where superficial is less than or equal to DEFAULT_SUPERFICIAL
        defaultSuenoShouldBeFound("superficial.lessThanOrEqual=" + DEFAULT_SUPERFICIAL);

        // Get all the suenoList where superficial is less than or equal to SMALLER_SUPERFICIAL
        defaultSuenoShouldNotBeFound("superficial.lessThanOrEqual=" + SMALLER_SUPERFICIAL);
    }

    @Test
    @Transactional
    void getAllSuenosBySuperficialIsLessThanSomething() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where superficial is less than DEFAULT_SUPERFICIAL
        defaultSuenoShouldNotBeFound("superficial.lessThan=" + DEFAULT_SUPERFICIAL);

        // Get all the suenoList where superficial is less than UPDATED_SUPERFICIAL
        defaultSuenoShouldBeFound("superficial.lessThan=" + UPDATED_SUPERFICIAL);
    }

    @Test
    @Transactional
    void getAllSuenosBySuperficialIsGreaterThanSomething() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where superficial is greater than DEFAULT_SUPERFICIAL
        defaultSuenoShouldNotBeFound("superficial.greaterThan=" + DEFAULT_SUPERFICIAL);

        // Get all the suenoList where superficial is greater than SMALLER_SUPERFICIAL
        defaultSuenoShouldBeFound("superficial.greaterThan=" + SMALLER_SUPERFICIAL);
    }

    @Test
    @Transactional
    void getAllSuenosByProfundoIsEqualToSomething() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where profundo equals to DEFAULT_PROFUNDO
        defaultSuenoShouldBeFound("profundo.equals=" + DEFAULT_PROFUNDO);

        // Get all the suenoList where profundo equals to UPDATED_PROFUNDO
        defaultSuenoShouldNotBeFound("profundo.equals=" + UPDATED_PROFUNDO);
    }

    @Test
    @Transactional
    void getAllSuenosByProfundoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where profundo not equals to DEFAULT_PROFUNDO
        defaultSuenoShouldNotBeFound("profundo.notEquals=" + DEFAULT_PROFUNDO);

        // Get all the suenoList where profundo not equals to UPDATED_PROFUNDO
        defaultSuenoShouldBeFound("profundo.notEquals=" + UPDATED_PROFUNDO);
    }

    @Test
    @Transactional
    void getAllSuenosByProfundoIsInShouldWork() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where profundo in DEFAULT_PROFUNDO or UPDATED_PROFUNDO
        defaultSuenoShouldBeFound("profundo.in=" + DEFAULT_PROFUNDO + "," + UPDATED_PROFUNDO);

        // Get all the suenoList where profundo equals to UPDATED_PROFUNDO
        defaultSuenoShouldNotBeFound("profundo.in=" + UPDATED_PROFUNDO);
    }

    @Test
    @Transactional
    void getAllSuenosByProfundoIsNullOrNotNull() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where profundo is not null
        defaultSuenoShouldBeFound("profundo.specified=true");

        // Get all the suenoList where profundo is null
        defaultSuenoShouldNotBeFound("profundo.specified=false");
    }

    @Test
    @Transactional
    void getAllSuenosByProfundoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where profundo is greater than or equal to DEFAULT_PROFUNDO
        defaultSuenoShouldBeFound("profundo.greaterThanOrEqual=" + DEFAULT_PROFUNDO);

        // Get all the suenoList where profundo is greater than or equal to UPDATED_PROFUNDO
        defaultSuenoShouldNotBeFound("profundo.greaterThanOrEqual=" + UPDATED_PROFUNDO);
    }

    @Test
    @Transactional
    void getAllSuenosByProfundoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where profundo is less than or equal to DEFAULT_PROFUNDO
        defaultSuenoShouldBeFound("profundo.lessThanOrEqual=" + DEFAULT_PROFUNDO);

        // Get all the suenoList where profundo is less than or equal to SMALLER_PROFUNDO
        defaultSuenoShouldNotBeFound("profundo.lessThanOrEqual=" + SMALLER_PROFUNDO);
    }

    @Test
    @Transactional
    void getAllSuenosByProfundoIsLessThanSomething() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where profundo is less than DEFAULT_PROFUNDO
        defaultSuenoShouldNotBeFound("profundo.lessThan=" + DEFAULT_PROFUNDO);

        // Get all the suenoList where profundo is less than UPDATED_PROFUNDO
        defaultSuenoShouldBeFound("profundo.lessThan=" + UPDATED_PROFUNDO);
    }

    @Test
    @Transactional
    void getAllSuenosByProfundoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where profundo is greater than DEFAULT_PROFUNDO
        defaultSuenoShouldNotBeFound("profundo.greaterThan=" + DEFAULT_PROFUNDO);

        // Get all the suenoList where profundo is greater than SMALLER_PROFUNDO
        defaultSuenoShouldBeFound("profundo.greaterThan=" + SMALLER_PROFUNDO);
    }

    @Test
    @Transactional
    void getAllSuenosByDespiertoIsEqualToSomething() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where despierto equals to DEFAULT_DESPIERTO
        defaultSuenoShouldBeFound("despierto.equals=" + DEFAULT_DESPIERTO);

        // Get all the suenoList where despierto equals to UPDATED_DESPIERTO
        defaultSuenoShouldNotBeFound("despierto.equals=" + UPDATED_DESPIERTO);
    }

    @Test
    @Transactional
    void getAllSuenosByDespiertoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where despierto not equals to DEFAULT_DESPIERTO
        defaultSuenoShouldNotBeFound("despierto.notEquals=" + DEFAULT_DESPIERTO);

        // Get all the suenoList where despierto not equals to UPDATED_DESPIERTO
        defaultSuenoShouldBeFound("despierto.notEquals=" + UPDATED_DESPIERTO);
    }

    @Test
    @Transactional
    void getAllSuenosByDespiertoIsInShouldWork() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where despierto in DEFAULT_DESPIERTO or UPDATED_DESPIERTO
        defaultSuenoShouldBeFound("despierto.in=" + DEFAULT_DESPIERTO + "," + UPDATED_DESPIERTO);

        // Get all the suenoList where despierto equals to UPDATED_DESPIERTO
        defaultSuenoShouldNotBeFound("despierto.in=" + UPDATED_DESPIERTO);
    }

    @Test
    @Transactional
    void getAllSuenosByDespiertoIsNullOrNotNull() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where despierto is not null
        defaultSuenoShouldBeFound("despierto.specified=true");

        // Get all the suenoList where despierto is null
        defaultSuenoShouldNotBeFound("despierto.specified=false");
    }

    @Test
    @Transactional
    void getAllSuenosByDespiertoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where despierto is greater than or equal to DEFAULT_DESPIERTO
        defaultSuenoShouldBeFound("despierto.greaterThanOrEqual=" + DEFAULT_DESPIERTO);

        // Get all the suenoList where despierto is greater than or equal to UPDATED_DESPIERTO
        defaultSuenoShouldNotBeFound("despierto.greaterThanOrEqual=" + UPDATED_DESPIERTO);
    }

    @Test
    @Transactional
    void getAllSuenosByDespiertoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where despierto is less than or equal to DEFAULT_DESPIERTO
        defaultSuenoShouldBeFound("despierto.lessThanOrEqual=" + DEFAULT_DESPIERTO);

        // Get all the suenoList where despierto is less than or equal to SMALLER_DESPIERTO
        defaultSuenoShouldNotBeFound("despierto.lessThanOrEqual=" + SMALLER_DESPIERTO);
    }

    @Test
    @Transactional
    void getAllSuenosByDespiertoIsLessThanSomething() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where despierto is less than DEFAULT_DESPIERTO
        defaultSuenoShouldNotBeFound("despierto.lessThan=" + DEFAULT_DESPIERTO);

        // Get all the suenoList where despierto is less than UPDATED_DESPIERTO
        defaultSuenoShouldBeFound("despierto.lessThan=" + UPDATED_DESPIERTO);
    }

    @Test
    @Transactional
    void getAllSuenosByDespiertoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where despierto is greater than DEFAULT_DESPIERTO
        defaultSuenoShouldNotBeFound("despierto.greaterThan=" + DEFAULT_DESPIERTO);

        // Get all the suenoList where despierto is greater than SMALLER_DESPIERTO
        defaultSuenoShouldBeFound("despierto.greaterThan=" + SMALLER_DESPIERTO);
    }

    @Test
    @Transactional
    void getAllSuenosByTimeInstantIsEqualToSomething() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where timeInstant equals to DEFAULT_TIME_INSTANT
        defaultSuenoShouldBeFound("timeInstant.equals=" + DEFAULT_TIME_INSTANT);

        // Get all the suenoList where timeInstant equals to UPDATED_TIME_INSTANT
        defaultSuenoShouldNotBeFound("timeInstant.equals=" + UPDATED_TIME_INSTANT);
    }

    @Test
    @Transactional
    void getAllSuenosByTimeInstantIsNotEqualToSomething() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where timeInstant not equals to DEFAULT_TIME_INSTANT
        defaultSuenoShouldNotBeFound("timeInstant.notEquals=" + DEFAULT_TIME_INSTANT);

        // Get all the suenoList where timeInstant not equals to UPDATED_TIME_INSTANT
        defaultSuenoShouldBeFound("timeInstant.notEquals=" + UPDATED_TIME_INSTANT);
    }

    @Test
    @Transactional
    void getAllSuenosByTimeInstantIsInShouldWork() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where timeInstant in DEFAULT_TIME_INSTANT or UPDATED_TIME_INSTANT
        defaultSuenoShouldBeFound("timeInstant.in=" + DEFAULT_TIME_INSTANT + "," + UPDATED_TIME_INSTANT);

        // Get all the suenoList where timeInstant equals to UPDATED_TIME_INSTANT
        defaultSuenoShouldNotBeFound("timeInstant.in=" + UPDATED_TIME_INSTANT);
    }

    @Test
    @Transactional
    void getAllSuenosByTimeInstantIsNullOrNotNull() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        // Get all the suenoList where timeInstant is not null
        defaultSuenoShouldBeFound("timeInstant.specified=true");

        // Get all the suenoList where timeInstant is null
        defaultSuenoShouldNotBeFound("timeInstant.specified=false");
    }

    @Test
    @Transactional
    void getAllSuenosByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        sueno.setUser(user);
        suenoRepository.saveAndFlush(sueno);
        String userId = user.getId();

        // Get all the suenoList where user equals to userId
        defaultSuenoShouldBeFound("userId.equals=" + userId);

        // Get all the suenoList where user equals to "invalid-id"
        defaultSuenoShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSuenoShouldBeFound(String filter) throws Exception {
        restSuenoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sueno.getId().intValue())))
            .andExpect(jsonPath("$.[*].superficial").value(hasItem(DEFAULT_SUPERFICIAL)))
            .andExpect(jsonPath("$.[*].profundo").value(hasItem(DEFAULT_PROFUNDO)))
            .andExpect(jsonPath("$.[*].despierto").value(hasItem(DEFAULT_DESPIERTO)))
            .andExpect(jsonPath("$.[*].timeInstant").value(hasItem(DEFAULT_TIME_INSTANT.toString())));

        // Check, that the count call also returns 1
        restSuenoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSuenoShouldNotBeFound(String filter) throws Exception {
        restSuenoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSuenoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSueno() throws Exception {
        // Get the sueno
        restSuenoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSueno() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        int databaseSizeBeforeUpdate = suenoRepository.findAll().size();

        // Update the sueno
        Sueno updatedSueno = suenoRepository.findById(sueno.getId()).get();
        // Disconnect from session so that the updates on updatedSueno are not directly saved in db
        em.detach(updatedSueno);
        updatedSueno
            .superficial(UPDATED_SUPERFICIAL)
            .profundo(UPDATED_PROFUNDO)
            .despierto(UPDATED_DESPIERTO)
            .timeInstant(UPDATED_TIME_INSTANT);

        restSuenoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSueno.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSueno))
            )
            .andExpect(status().isOk());

        // Validate the Sueno in the database
        List<Sueno> suenoList = suenoRepository.findAll();
        assertThat(suenoList).hasSize(databaseSizeBeforeUpdate);
        Sueno testSueno = suenoList.get(suenoList.size() - 1);
        assertThat(testSueno.getSuperficial()).isEqualTo(UPDATED_SUPERFICIAL);
        assertThat(testSueno.getProfundo()).isEqualTo(UPDATED_PROFUNDO);
        assertThat(testSueno.getDespierto()).isEqualTo(UPDATED_DESPIERTO);
        assertThat(testSueno.getTimeInstant()).isEqualTo(UPDATED_TIME_INSTANT);
    }

    @Test
    @Transactional
    void putNonExistingSueno() throws Exception {
        int databaseSizeBeforeUpdate = suenoRepository.findAll().size();
        sueno.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuenoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sueno.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sueno))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sueno in the database
        List<Sueno> suenoList = suenoRepository.findAll();
        assertThat(suenoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSueno() throws Exception {
        int databaseSizeBeforeUpdate = suenoRepository.findAll().size();
        sueno.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuenoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sueno))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sueno in the database
        List<Sueno> suenoList = suenoRepository.findAll();
        assertThat(suenoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSueno() throws Exception {
        int databaseSizeBeforeUpdate = suenoRepository.findAll().size();
        sueno.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuenoMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sueno))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sueno in the database
        List<Sueno> suenoList = suenoRepository.findAll();
        assertThat(suenoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSuenoWithPatch() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        int databaseSizeBeforeUpdate = suenoRepository.findAll().size();

        // Update the sueno using partial update
        Sueno partialUpdatedSueno = new Sueno();
        partialUpdatedSueno.setId(sueno.getId());

        partialUpdatedSueno.superficial(UPDATED_SUPERFICIAL).timeInstant(UPDATED_TIME_INSTANT);

        restSuenoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSueno.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSueno))
            )
            .andExpect(status().isOk());

        // Validate the Sueno in the database
        List<Sueno> suenoList = suenoRepository.findAll();
        assertThat(suenoList).hasSize(databaseSizeBeforeUpdate);
        Sueno testSueno = suenoList.get(suenoList.size() - 1);
        assertThat(testSueno.getSuperficial()).isEqualTo(UPDATED_SUPERFICIAL);
        assertThat(testSueno.getProfundo()).isEqualTo(DEFAULT_PROFUNDO);
        assertThat(testSueno.getDespierto()).isEqualTo(DEFAULT_DESPIERTO);
        assertThat(testSueno.getTimeInstant()).isEqualTo(UPDATED_TIME_INSTANT);
    }

    @Test
    @Transactional
    void fullUpdateSuenoWithPatch() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        int databaseSizeBeforeUpdate = suenoRepository.findAll().size();

        // Update the sueno using partial update
        Sueno partialUpdatedSueno = new Sueno();
        partialUpdatedSueno.setId(sueno.getId());

        partialUpdatedSueno
            .superficial(UPDATED_SUPERFICIAL)
            .profundo(UPDATED_PROFUNDO)
            .despierto(UPDATED_DESPIERTO)
            .timeInstant(UPDATED_TIME_INSTANT);

        restSuenoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSueno.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSueno))
            )
            .andExpect(status().isOk());

        // Validate the Sueno in the database
        List<Sueno> suenoList = suenoRepository.findAll();
        assertThat(suenoList).hasSize(databaseSizeBeforeUpdate);
        Sueno testSueno = suenoList.get(suenoList.size() - 1);
        assertThat(testSueno.getSuperficial()).isEqualTo(UPDATED_SUPERFICIAL);
        assertThat(testSueno.getProfundo()).isEqualTo(UPDATED_PROFUNDO);
        assertThat(testSueno.getDespierto()).isEqualTo(UPDATED_DESPIERTO);
        assertThat(testSueno.getTimeInstant()).isEqualTo(UPDATED_TIME_INSTANT);
    }

    @Test
    @Transactional
    void patchNonExistingSueno() throws Exception {
        int databaseSizeBeforeUpdate = suenoRepository.findAll().size();
        sueno.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuenoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sueno.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sueno))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sueno in the database
        List<Sueno> suenoList = suenoRepository.findAll();
        assertThat(suenoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSueno() throws Exception {
        int databaseSizeBeforeUpdate = suenoRepository.findAll().size();
        sueno.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuenoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sueno))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sueno in the database
        List<Sueno> suenoList = suenoRepository.findAll();
        assertThat(suenoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSueno() throws Exception {
        int databaseSizeBeforeUpdate = suenoRepository.findAll().size();
        sueno.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuenoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sueno))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sueno in the database
        List<Sueno> suenoList = suenoRepository.findAll();
        assertThat(suenoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSueno() throws Exception {
        // Initialize the database
        suenoRepository.saveAndFlush(sueno);

        int databaseSizeBeforeDelete = suenoRepository.findAll().size();

        // Delete the sueno
        restSuenoMockMvc
            .perform(delete(ENTITY_API_URL_ID, sueno.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sueno> suenoList = suenoRepository.findAll();
        assertThat(suenoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

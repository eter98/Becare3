package com.be4tech.becare3.web.rest;

import static com.be4tech.becare3.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.TokenDisp;
import com.be4tech.becare3.domain.User;
import com.be4tech.becare3.repository.TokenDispRepository;
import com.be4tech.becare3.service.criteria.TokenDispCriteria;
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
 * Integration tests for the {@link TokenDispResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TokenDispResourceIT {

    private static final String DEFAULT_TOKEN_CONEXION = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN_CONEXION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final Instant DEFAULT_FECHA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ZonedDateTime DEFAULT_FECHA_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_FECHA_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/token-disps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TokenDispRepository tokenDispRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTokenDispMockMvc;

    private TokenDisp tokenDisp;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TokenDisp createEntity(EntityManager em) {
        TokenDisp tokenDisp = new TokenDisp()
            .tokenConexion(DEFAULT_TOKEN_CONEXION)
            .activo(DEFAULT_ACTIVO)
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .fechaFin(DEFAULT_FECHA_FIN);
        return tokenDisp;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TokenDisp createUpdatedEntity(EntityManager em) {
        TokenDisp tokenDisp = new TokenDisp()
            .tokenConexion(UPDATED_TOKEN_CONEXION)
            .activo(UPDATED_ACTIVO)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN);
        return tokenDisp;
    }

    @BeforeEach
    public void initTest() {
        tokenDisp = createEntity(em);
    }

    @Test
    @Transactional
    void createTokenDisp() throws Exception {
        int databaseSizeBeforeCreate = tokenDispRepository.findAll().size();
        // Create the TokenDisp
        restTokenDispMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tokenDisp))
            )
            .andExpect(status().isCreated());

        // Validate the TokenDisp in the database
        List<TokenDisp> tokenDispList = tokenDispRepository.findAll();
        assertThat(tokenDispList).hasSize(databaseSizeBeforeCreate + 1);
        TokenDisp testTokenDisp = tokenDispList.get(tokenDispList.size() - 1);
        assertThat(testTokenDisp.getTokenConexion()).isEqualTo(DEFAULT_TOKEN_CONEXION);
        assertThat(testTokenDisp.getActivo()).isEqualTo(DEFAULT_ACTIVO);
        assertThat(testTokenDisp.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testTokenDisp.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
    }

    @Test
    @Transactional
    void createTokenDispWithExistingId() throws Exception {
        // Create the TokenDisp with an existing ID
        tokenDisp.setId(1L);

        int databaseSizeBeforeCreate = tokenDispRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTokenDispMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tokenDisp))
            )
            .andExpect(status().isBadRequest());

        // Validate the TokenDisp in the database
        List<TokenDisp> tokenDispList = tokenDispRepository.findAll();
        assertThat(tokenDispList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTokenDisps() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        // Get all the tokenDispList
        restTokenDispMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tokenDisp.getId().intValue())))
            .andExpect(jsonPath("$.[*].tokenConexion").value(hasItem(DEFAULT_TOKEN_CONEXION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(sameInstant(DEFAULT_FECHA_FIN))));
    }

    @Test
    @Transactional
    void getTokenDisp() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        // Get the tokenDisp
        restTokenDispMockMvc
            .perform(get(ENTITY_API_URL_ID, tokenDisp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tokenDisp.getId().intValue()))
            .andExpect(jsonPath("$.tokenConexion").value(DEFAULT_TOKEN_CONEXION))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO.booleanValue()))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFin").value(sameInstant(DEFAULT_FECHA_FIN)));
    }

    @Test
    @Transactional
    void getTokenDispsByIdFiltering() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        Long id = tokenDisp.getId();

        defaultTokenDispShouldBeFound("id.equals=" + id);
        defaultTokenDispShouldNotBeFound("id.notEquals=" + id);

        defaultTokenDispShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTokenDispShouldNotBeFound("id.greaterThan=" + id);

        defaultTokenDispShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTokenDispShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTokenDispsByTokenConexionIsEqualToSomething() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        // Get all the tokenDispList where tokenConexion equals to DEFAULT_TOKEN_CONEXION
        defaultTokenDispShouldBeFound("tokenConexion.equals=" + DEFAULT_TOKEN_CONEXION);

        // Get all the tokenDispList where tokenConexion equals to UPDATED_TOKEN_CONEXION
        defaultTokenDispShouldNotBeFound("tokenConexion.equals=" + UPDATED_TOKEN_CONEXION);
    }

    @Test
    @Transactional
    void getAllTokenDispsByTokenConexionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        // Get all the tokenDispList where tokenConexion not equals to DEFAULT_TOKEN_CONEXION
        defaultTokenDispShouldNotBeFound("tokenConexion.notEquals=" + DEFAULT_TOKEN_CONEXION);

        // Get all the tokenDispList where tokenConexion not equals to UPDATED_TOKEN_CONEXION
        defaultTokenDispShouldBeFound("tokenConexion.notEquals=" + UPDATED_TOKEN_CONEXION);
    }

    @Test
    @Transactional
    void getAllTokenDispsByTokenConexionIsInShouldWork() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        // Get all the tokenDispList where tokenConexion in DEFAULT_TOKEN_CONEXION or UPDATED_TOKEN_CONEXION
        defaultTokenDispShouldBeFound("tokenConexion.in=" + DEFAULT_TOKEN_CONEXION + "," + UPDATED_TOKEN_CONEXION);

        // Get all the tokenDispList where tokenConexion equals to UPDATED_TOKEN_CONEXION
        defaultTokenDispShouldNotBeFound("tokenConexion.in=" + UPDATED_TOKEN_CONEXION);
    }

    @Test
    @Transactional
    void getAllTokenDispsByTokenConexionIsNullOrNotNull() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        // Get all the tokenDispList where tokenConexion is not null
        defaultTokenDispShouldBeFound("tokenConexion.specified=true");

        // Get all the tokenDispList where tokenConexion is null
        defaultTokenDispShouldNotBeFound("tokenConexion.specified=false");
    }

    @Test
    @Transactional
    void getAllTokenDispsByTokenConexionContainsSomething() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        // Get all the tokenDispList where tokenConexion contains DEFAULT_TOKEN_CONEXION
        defaultTokenDispShouldBeFound("tokenConexion.contains=" + DEFAULT_TOKEN_CONEXION);

        // Get all the tokenDispList where tokenConexion contains UPDATED_TOKEN_CONEXION
        defaultTokenDispShouldNotBeFound("tokenConexion.contains=" + UPDATED_TOKEN_CONEXION);
    }

    @Test
    @Transactional
    void getAllTokenDispsByTokenConexionNotContainsSomething() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        // Get all the tokenDispList where tokenConexion does not contain DEFAULT_TOKEN_CONEXION
        defaultTokenDispShouldNotBeFound("tokenConexion.doesNotContain=" + DEFAULT_TOKEN_CONEXION);

        // Get all the tokenDispList where tokenConexion does not contain UPDATED_TOKEN_CONEXION
        defaultTokenDispShouldBeFound("tokenConexion.doesNotContain=" + UPDATED_TOKEN_CONEXION);
    }

    @Test
    @Transactional
    void getAllTokenDispsByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        // Get all the tokenDispList where activo equals to DEFAULT_ACTIVO
        defaultTokenDispShouldBeFound("activo.equals=" + DEFAULT_ACTIVO);

        // Get all the tokenDispList where activo equals to UPDATED_ACTIVO
        defaultTokenDispShouldNotBeFound("activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllTokenDispsByActivoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        // Get all the tokenDispList where activo not equals to DEFAULT_ACTIVO
        defaultTokenDispShouldNotBeFound("activo.notEquals=" + DEFAULT_ACTIVO);

        // Get all the tokenDispList where activo not equals to UPDATED_ACTIVO
        defaultTokenDispShouldBeFound("activo.notEquals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllTokenDispsByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        // Get all the tokenDispList where activo in DEFAULT_ACTIVO or UPDATED_ACTIVO
        defaultTokenDispShouldBeFound("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO);

        // Get all the tokenDispList where activo equals to UPDATED_ACTIVO
        defaultTokenDispShouldNotBeFound("activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllTokenDispsByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        // Get all the tokenDispList where activo is not null
        defaultTokenDispShouldBeFound("activo.specified=true");

        // Get all the tokenDispList where activo is null
        defaultTokenDispShouldNotBeFound("activo.specified=false");
    }

    @Test
    @Transactional
    void getAllTokenDispsByFechaInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        // Get all the tokenDispList where fechaInicio equals to DEFAULT_FECHA_INICIO
        defaultTokenDispShouldBeFound("fechaInicio.equals=" + DEFAULT_FECHA_INICIO);

        // Get all the tokenDispList where fechaInicio equals to UPDATED_FECHA_INICIO
        defaultTokenDispShouldNotBeFound("fechaInicio.equals=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllTokenDispsByFechaInicioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        // Get all the tokenDispList where fechaInicio not equals to DEFAULT_FECHA_INICIO
        defaultTokenDispShouldNotBeFound("fechaInicio.notEquals=" + DEFAULT_FECHA_INICIO);

        // Get all the tokenDispList where fechaInicio not equals to UPDATED_FECHA_INICIO
        defaultTokenDispShouldBeFound("fechaInicio.notEquals=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllTokenDispsByFechaInicioIsInShouldWork() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        // Get all the tokenDispList where fechaInicio in DEFAULT_FECHA_INICIO or UPDATED_FECHA_INICIO
        defaultTokenDispShouldBeFound("fechaInicio.in=" + DEFAULT_FECHA_INICIO + "," + UPDATED_FECHA_INICIO);

        // Get all the tokenDispList where fechaInicio equals to UPDATED_FECHA_INICIO
        defaultTokenDispShouldNotBeFound("fechaInicio.in=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllTokenDispsByFechaInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        // Get all the tokenDispList where fechaInicio is not null
        defaultTokenDispShouldBeFound("fechaInicio.specified=true");

        // Get all the tokenDispList where fechaInicio is null
        defaultTokenDispShouldNotBeFound("fechaInicio.specified=false");
    }

    @Test
    @Transactional
    void getAllTokenDispsByFechaFinIsEqualToSomething() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        // Get all the tokenDispList where fechaFin equals to DEFAULT_FECHA_FIN
        defaultTokenDispShouldBeFound("fechaFin.equals=" + DEFAULT_FECHA_FIN);

        // Get all the tokenDispList where fechaFin equals to UPDATED_FECHA_FIN
        defaultTokenDispShouldNotBeFound("fechaFin.equals=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTokenDispsByFechaFinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        // Get all the tokenDispList where fechaFin not equals to DEFAULT_FECHA_FIN
        defaultTokenDispShouldNotBeFound("fechaFin.notEquals=" + DEFAULT_FECHA_FIN);

        // Get all the tokenDispList where fechaFin not equals to UPDATED_FECHA_FIN
        defaultTokenDispShouldBeFound("fechaFin.notEquals=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTokenDispsByFechaFinIsInShouldWork() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        // Get all the tokenDispList where fechaFin in DEFAULT_FECHA_FIN or UPDATED_FECHA_FIN
        defaultTokenDispShouldBeFound("fechaFin.in=" + DEFAULT_FECHA_FIN + "," + UPDATED_FECHA_FIN);

        // Get all the tokenDispList where fechaFin equals to UPDATED_FECHA_FIN
        defaultTokenDispShouldNotBeFound("fechaFin.in=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTokenDispsByFechaFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        // Get all the tokenDispList where fechaFin is not null
        defaultTokenDispShouldBeFound("fechaFin.specified=true");

        // Get all the tokenDispList where fechaFin is null
        defaultTokenDispShouldNotBeFound("fechaFin.specified=false");
    }

    @Test
    @Transactional
    void getAllTokenDispsByFechaFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        // Get all the tokenDispList where fechaFin is greater than or equal to DEFAULT_FECHA_FIN
        defaultTokenDispShouldBeFound("fechaFin.greaterThanOrEqual=" + DEFAULT_FECHA_FIN);

        // Get all the tokenDispList where fechaFin is greater than or equal to UPDATED_FECHA_FIN
        defaultTokenDispShouldNotBeFound("fechaFin.greaterThanOrEqual=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTokenDispsByFechaFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        // Get all the tokenDispList where fechaFin is less than or equal to DEFAULT_FECHA_FIN
        defaultTokenDispShouldBeFound("fechaFin.lessThanOrEqual=" + DEFAULT_FECHA_FIN);

        // Get all the tokenDispList where fechaFin is less than or equal to SMALLER_FECHA_FIN
        defaultTokenDispShouldNotBeFound("fechaFin.lessThanOrEqual=" + SMALLER_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTokenDispsByFechaFinIsLessThanSomething() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        // Get all the tokenDispList where fechaFin is less than DEFAULT_FECHA_FIN
        defaultTokenDispShouldNotBeFound("fechaFin.lessThan=" + DEFAULT_FECHA_FIN);

        // Get all the tokenDispList where fechaFin is less than UPDATED_FECHA_FIN
        defaultTokenDispShouldBeFound("fechaFin.lessThan=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTokenDispsByFechaFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        // Get all the tokenDispList where fechaFin is greater than DEFAULT_FECHA_FIN
        defaultTokenDispShouldNotBeFound("fechaFin.greaterThan=" + DEFAULT_FECHA_FIN);

        // Get all the tokenDispList where fechaFin is greater than SMALLER_FECHA_FIN
        defaultTokenDispShouldBeFound("fechaFin.greaterThan=" + SMALLER_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTokenDispsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        tokenDisp.setUser(user);
        tokenDispRepository.saveAndFlush(tokenDisp);
        String userId = user.getId();

        // Get all the tokenDispList where user equals to userId
        defaultTokenDispShouldBeFound("userId.equals=" + userId);

        // Get all the tokenDispList where user equals to "invalid-id"
        defaultTokenDispShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTokenDispShouldBeFound(String filter) throws Exception {
        restTokenDispMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tokenDisp.getId().intValue())))
            .andExpect(jsonPath("$.[*].tokenConexion").value(hasItem(DEFAULT_TOKEN_CONEXION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(sameInstant(DEFAULT_FECHA_FIN))));

        // Check, that the count call also returns 1
        restTokenDispMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTokenDispShouldNotBeFound(String filter) throws Exception {
        restTokenDispMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTokenDispMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTokenDisp() throws Exception {
        // Get the tokenDisp
        restTokenDispMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTokenDisp() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        int databaseSizeBeforeUpdate = tokenDispRepository.findAll().size();

        // Update the tokenDisp
        TokenDisp updatedTokenDisp = tokenDispRepository.findById(tokenDisp.getId()).get();
        // Disconnect from session so that the updates on updatedTokenDisp are not directly saved in db
        em.detach(updatedTokenDisp);
        updatedTokenDisp
            .tokenConexion(UPDATED_TOKEN_CONEXION)
            .activo(UPDATED_ACTIVO)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN);

        restTokenDispMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTokenDisp.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTokenDisp))
            )
            .andExpect(status().isOk());

        // Validate the TokenDisp in the database
        List<TokenDisp> tokenDispList = tokenDispRepository.findAll();
        assertThat(tokenDispList).hasSize(databaseSizeBeforeUpdate);
        TokenDisp testTokenDisp = tokenDispList.get(tokenDispList.size() - 1);
        assertThat(testTokenDisp.getTokenConexion()).isEqualTo(UPDATED_TOKEN_CONEXION);
        assertThat(testTokenDisp.getActivo()).isEqualTo(UPDATED_ACTIVO);
        assertThat(testTokenDisp.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testTokenDisp.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void putNonExistingTokenDisp() throws Exception {
        int databaseSizeBeforeUpdate = tokenDispRepository.findAll().size();
        tokenDisp.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTokenDispMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tokenDisp.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tokenDisp))
            )
            .andExpect(status().isBadRequest());

        // Validate the TokenDisp in the database
        List<TokenDisp> tokenDispList = tokenDispRepository.findAll();
        assertThat(tokenDispList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTokenDisp() throws Exception {
        int databaseSizeBeforeUpdate = tokenDispRepository.findAll().size();
        tokenDisp.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTokenDispMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tokenDisp))
            )
            .andExpect(status().isBadRequest());

        // Validate the TokenDisp in the database
        List<TokenDisp> tokenDispList = tokenDispRepository.findAll();
        assertThat(tokenDispList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTokenDisp() throws Exception {
        int databaseSizeBeforeUpdate = tokenDispRepository.findAll().size();
        tokenDisp.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTokenDispMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tokenDisp))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TokenDisp in the database
        List<TokenDisp> tokenDispList = tokenDispRepository.findAll();
        assertThat(tokenDispList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTokenDispWithPatch() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        int databaseSizeBeforeUpdate = tokenDispRepository.findAll().size();

        // Update the tokenDisp using partial update
        TokenDisp partialUpdatedTokenDisp = new TokenDisp();
        partialUpdatedTokenDisp.setId(tokenDisp.getId());

        partialUpdatedTokenDisp.tokenConexion(UPDATED_TOKEN_CONEXION).fechaInicio(UPDATED_FECHA_INICIO);

        restTokenDispMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTokenDisp.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTokenDisp))
            )
            .andExpect(status().isOk());

        // Validate the TokenDisp in the database
        List<TokenDisp> tokenDispList = tokenDispRepository.findAll();
        assertThat(tokenDispList).hasSize(databaseSizeBeforeUpdate);
        TokenDisp testTokenDisp = tokenDispList.get(tokenDispList.size() - 1);
        assertThat(testTokenDisp.getTokenConexion()).isEqualTo(UPDATED_TOKEN_CONEXION);
        assertThat(testTokenDisp.getActivo()).isEqualTo(DEFAULT_ACTIVO);
        assertThat(testTokenDisp.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testTokenDisp.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
    }

    @Test
    @Transactional
    void fullUpdateTokenDispWithPatch() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        int databaseSizeBeforeUpdate = tokenDispRepository.findAll().size();

        // Update the tokenDisp using partial update
        TokenDisp partialUpdatedTokenDisp = new TokenDisp();
        partialUpdatedTokenDisp.setId(tokenDisp.getId());

        partialUpdatedTokenDisp
            .tokenConexion(UPDATED_TOKEN_CONEXION)
            .activo(UPDATED_ACTIVO)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN);

        restTokenDispMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTokenDisp.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTokenDisp))
            )
            .andExpect(status().isOk());

        // Validate the TokenDisp in the database
        List<TokenDisp> tokenDispList = tokenDispRepository.findAll();
        assertThat(tokenDispList).hasSize(databaseSizeBeforeUpdate);
        TokenDisp testTokenDisp = tokenDispList.get(tokenDispList.size() - 1);
        assertThat(testTokenDisp.getTokenConexion()).isEqualTo(UPDATED_TOKEN_CONEXION);
        assertThat(testTokenDisp.getActivo()).isEqualTo(UPDATED_ACTIVO);
        assertThat(testTokenDisp.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testTokenDisp.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void patchNonExistingTokenDisp() throws Exception {
        int databaseSizeBeforeUpdate = tokenDispRepository.findAll().size();
        tokenDisp.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTokenDispMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tokenDisp.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tokenDisp))
            )
            .andExpect(status().isBadRequest());

        // Validate the TokenDisp in the database
        List<TokenDisp> tokenDispList = tokenDispRepository.findAll();
        assertThat(tokenDispList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTokenDisp() throws Exception {
        int databaseSizeBeforeUpdate = tokenDispRepository.findAll().size();
        tokenDisp.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTokenDispMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tokenDisp))
            )
            .andExpect(status().isBadRequest());

        // Validate the TokenDisp in the database
        List<TokenDisp> tokenDispList = tokenDispRepository.findAll();
        assertThat(tokenDispList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTokenDisp() throws Exception {
        int databaseSizeBeforeUpdate = tokenDispRepository.findAll().size();
        tokenDisp.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTokenDispMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tokenDisp))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TokenDisp in the database
        List<TokenDisp> tokenDispList = tokenDispRepository.findAll();
        assertThat(tokenDispList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTokenDisp() throws Exception {
        // Initialize the database
        tokenDispRepository.saveAndFlush(tokenDisp);

        int databaseSizeBeforeDelete = tokenDispRepository.findAll().size();

        // Delete the tokenDisp
        restTokenDispMockMvc
            .perform(delete(ENTITY_API_URL_ID, tokenDisp.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TokenDisp> tokenDispList = tokenDispRepository.findAll();
        assertThat(tokenDispList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

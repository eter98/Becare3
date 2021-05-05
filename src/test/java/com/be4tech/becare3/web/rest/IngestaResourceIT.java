package com.be4tech.becare3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.Ingesta;
import com.be4tech.becare3.domain.User;
import com.be4tech.becare3.repository.IngestaRepository;
import com.be4tech.becare3.service.criteria.IngestaCriteria;
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
 * Integration tests for the {@link IngestaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IngestaResourceIT {

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final Integer DEFAULT_CONSUMO_CALORIAS = 1;
    private static final Integer UPDATED_CONSUMO_CALORIAS = 2;
    private static final Integer SMALLER_CONSUMO_CALORIAS = 1 - 1;

    private static final Instant DEFAULT_FECHA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/ingestas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IngestaRepository ingestaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIngestaMockMvc;

    private Ingesta ingesta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ingesta createEntity(EntityManager em) {
        Ingesta ingesta = new Ingesta().tipo(DEFAULT_TIPO).consumoCalorias(DEFAULT_CONSUMO_CALORIAS).fechaRegistro(DEFAULT_FECHA_REGISTRO);
        return ingesta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ingesta createUpdatedEntity(EntityManager em) {
        Ingesta ingesta = new Ingesta().tipo(UPDATED_TIPO).consumoCalorias(UPDATED_CONSUMO_CALORIAS).fechaRegistro(UPDATED_FECHA_REGISTRO);
        return ingesta;
    }

    @BeforeEach
    public void initTest() {
        ingesta = createEntity(em);
    }

    @Test
    @Transactional
    void createIngesta() throws Exception {
        int databaseSizeBeforeCreate = ingestaRepository.findAll().size();
        // Create the Ingesta
        restIngestaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ingesta))
            )
            .andExpect(status().isCreated());

        // Validate the Ingesta in the database
        List<Ingesta> ingestaList = ingestaRepository.findAll();
        assertThat(ingestaList).hasSize(databaseSizeBeforeCreate + 1);
        Ingesta testIngesta = ingestaList.get(ingestaList.size() - 1);
        assertThat(testIngesta.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testIngesta.getConsumoCalorias()).isEqualTo(DEFAULT_CONSUMO_CALORIAS);
        assertThat(testIngesta.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void createIngestaWithExistingId() throws Exception {
        // Create the Ingesta with an existing ID
        ingesta.setId(1L);

        int databaseSizeBeforeCreate = ingestaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIngestaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ingesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingesta in the database
        List<Ingesta> ingestaList = ingestaRepository.findAll();
        assertThat(ingestaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIngestas() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        // Get all the ingestaList
        restIngestaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingesta.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].consumoCalorias").value(hasItem(DEFAULT_CONSUMO_CALORIAS)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));
    }

    @Test
    @Transactional
    void getIngesta() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        // Get the ingesta
        restIngestaMockMvc
            .perform(get(ENTITY_API_URL_ID, ingesta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ingesta.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO))
            .andExpect(jsonPath("$.consumoCalorias").value(DEFAULT_CONSUMO_CALORIAS))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()));
    }

    @Test
    @Transactional
    void getIngestasByIdFiltering() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        Long id = ingesta.getId();

        defaultIngestaShouldBeFound("id.equals=" + id);
        defaultIngestaShouldNotBeFound("id.notEquals=" + id);

        defaultIngestaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultIngestaShouldNotBeFound("id.greaterThan=" + id);

        defaultIngestaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultIngestaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIngestasByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        // Get all the ingestaList where tipo equals to DEFAULT_TIPO
        defaultIngestaShouldBeFound("tipo.equals=" + DEFAULT_TIPO);

        // Get all the ingestaList where tipo equals to UPDATED_TIPO
        defaultIngestaShouldNotBeFound("tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllIngestasByTipoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        // Get all the ingestaList where tipo not equals to DEFAULT_TIPO
        defaultIngestaShouldNotBeFound("tipo.notEquals=" + DEFAULT_TIPO);

        // Get all the ingestaList where tipo not equals to UPDATED_TIPO
        defaultIngestaShouldBeFound("tipo.notEquals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllIngestasByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        // Get all the ingestaList where tipo in DEFAULT_TIPO or UPDATED_TIPO
        defaultIngestaShouldBeFound("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO);

        // Get all the ingestaList where tipo equals to UPDATED_TIPO
        defaultIngestaShouldNotBeFound("tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllIngestasByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        // Get all the ingestaList where tipo is not null
        defaultIngestaShouldBeFound("tipo.specified=true");

        // Get all the ingestaList where tipo is null
        defaultIngestaShouldNotBeFound("tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllIngestasByTipoContainsSomething() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        // Get all the ingestaList where tipo contains DEFAULT_TIPO
        defaultIngestaShouldBeFound("tipo.contains=" + DEFAULT_TIPO);

        // Get all the ingestaList where tipo contains UPDATED_TIPO
        defaultIngestaShouldNotBeFound("tipo.contains=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllIngestasByTipoNotContainsSomething() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        // Get all the ingestaList where tipo does not contain DEFAULT_TIPO
        defaultIngestaShouldNotBeFound("tipo.doesNotContain=" + DEFAULT_TIPO);

        // Get all the ingestaList where tipo does not contain UPDATED_TIPO
        defaultIngestaShouldBeFound("tipo.doesNotContain=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllIngestasByConsumoCaloriasIsEqualToSomething() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        // Get all the ingestaList where consumoCalorias equals to DEFAULT_CONSUMO_CALORIAS
        defaultIngestaShouldBeFound("consumoCalorias.equals=" + DEFAULT_CONSUMO_CALORIAS);

        // Get all the ingestaList where consumoCalorias equals to UPDATED_CONSUMO_CALORIAS
        defaultIngestaShouldNotBeFound("consumoCalorias.equals=" + UPDATED_CONSUMO_CALORIAS);
    }

    @Test
    @Transactional
    void getAllIngestasByConsumoCaloriasIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        // Get all the ingestaList where consumoCalorias not equals to DEFAULT_CONSUMO_CALORIAS
        defaultIngestaShouldNotBeFound("consumoCalorias.notEquals=" + DEFAULT_CONSUMO_CALORIAS);

        // Get all the ingestaList where consumoCalorias not equals to UPDATED_CONSUMO_CALORIAS
        defaultIngestaShouldBeFound("consumoCalorias.notEquals=" + UPDATED_CONSUMO_CALORIAS);
    }

    @Test
    @Transactional
    void getAllIngestasByConsumoCaloriasIsInShouldWork() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        // Get all the ingestaList where consumoCalorias in DEFAULT_CONSUMO_CALORIAS or UPDATED_CONSUMO_CALORIAS
        defaultIngestaShouldBeFound("consumoCalorias.in=" + DEFAULT_CONSUMO_CALORIAS + "," + UPDATED_CONSUMO_CALORIAS);

        // Get all the ingestaList where consumoCalorias equals to UPDATED_CONSUMO_CALORIAS
        defaultIngestaShouldNotBeFound("consumoCalorias.in=" + UPDATED_CONSUMO_CALORIAS);
    }

    @Test
    @Transactional
    void getAllIngestasByConsumoCaloriasIsNullOrNotNull() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        // Get all the ingestaList where consumoCalorias is not null
        defaultIngestaShouldBeFound("consumoCalorias.specified=true");

        // Get all the ingestaList where consumoCalorias is null
        defaultIngestaShouldNotBeFound("consumoCalorias.specified=false");
    }

    @Test
    @Transactional
    void getAllIngestasByConsumoCaloriasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        // Get all the ingestaList where consumoCalorias is greater than or equal to DEFAULT_CONSUMO_CALORIAS
        defaultIngestaShouldBeFound("consumoCalorias.greaterThanOrEqual=" + DEFAULT_CONSUMO_CALORIAS);

        // Get all the ingestaList where consumoCalorias is greater than or equal to UPDATED_CONSUMO_CALORIAS
        defaultIngestaShouldNotBeFound("consumoCalorias.greaterThanOrEqual=" + UPDATED_CONSUMO_CALORIAS);
    }

    @Test
    @Transactional
    void getAllIngestasByConsumoCaloriasIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        // Get all the ingestaList where consumoCalorias is less than or equal to DEFAULT_CONSUMO_CALORIAS
        defaultIngestaShouldBeFound("consumoCalorias.lessThanOrEqual=" + DEFAULT_CONSUMO_CALORIAS);

        // Get all the ingestaList where consumoCalorias is less than or equal to SMALLER_CONSUMO_CALORIAS
        defaultIngestaShouldNotBeFound("consumoCalorias.lessThanOrEqual=" + SMALLER_CONSUMO_CALORIAS);
    }

    @Test
    @Transactional
    void getAllIngestasByConsumoCaloriasIsLessThanSomething() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        // Get all the ingestaList where consumoCalorias is less than DEFAULT_CONSUMO_CALORIAS
        defaultIngestaShouldNotBeFound("consumoCalorias.lessThan=" + DEFAULT_CONSUMO_CALORIAS);

        // Get all the ingestaList where consumoCalorias is less than UPDATED_CONSUMO_CALORIAS
        defaultIngestaShouldBeFound("consumoCalorias.lessThan=" + UPDATED_CONSUMO_CALORIAS);
    }

    @Test
    @Transactional
    void getAllIngestasByConsumoCaloriasIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        // Get all the ingestaList where consumoCalorias is greater than DEFAULT_CONSUMO_CALORIAS
        defaultIngestaShouldNotBeFound("consumoCalorias.greaterThan=" + DEFAULT_CONSUMO_CALORIAS);

        // Get all the ingestaList where consumoCalorias is greater than SMALLER_CONSUMO_CALORIAS
        defaultIngestaShouldBeFound("consumoCalorias.greaterThan=" + SMALLER_CONSUMO_CALORIAS);
    }

    @Test
    @Transactional
    void getAllIngestasByFechaRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        // Get all the ingestaList where fechaRegistro equals to DEFAULT_FECHA_REGISTRO
        defaultIngestaShouldBeFound("fechaRegistro.equals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the ingestaList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultIngestaShouldNotBeFound("fechaRegistro.equals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllIngestasByFechaRegistroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        // Get all the ingestaList where fechaRegistro not equals to DEFAULT_FECHA_REGISTRO
        defaultIngestaShouldNotBeFound("fechaRegistro.notEquals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the ingestaList where fechaRegistro not equals to UPDATED_FECHA_REGISTRO
        defaultIngestaShouldBeFound("fechaRegistro.notEquals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllIngestasByFechaRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        // Get all the ingestaList where fechaRegistro in DEFAULT_FECHA_REGISTRO or UPDATED_FECHA_REGISTRO
        defaultIngestaShouldBeFound("fechaRegistro.in=" + DEFAULT_FECHA_REGISTRO + "," + UPDATED_FECHA_REGISTRO);

        // Get all the ingestaList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultIngestaShouldNotBeFound("fechaRegistro.in=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllIngestasByFechaRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        // Get all the ingestaList where fechaRegistro is not null
        defaultIngestaShouldBeFound("fechaRegistro.specified=true");

        // Get all the ingestaList where fechaRegistro is null
        defaultIngestaShouldNotBeFound("fechaRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllIngestasByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        ingesta.setUser(user);
        ingestaRepository.saveAndFlush(ingesta);
        String userId = user.getId();

        // Get all the ingestaList where user equals to userId
        defaultIngestaShouldBeFound("userId.equals=" + userId);

        // Get all the ingestaList where user equals to "invalid-id"
        defaultIngestaShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIngestaShouldBeFound(String filter) throws Exception {
        restIngestaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingesta.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].consumoCalorias").value(hasItem(DEFAULT_CONSUMO_CALORIAS)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));

        // Check, that the count call also returns 1
        restIngestaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIngestaShouldNotBeFound(String filter) throws Exception {
        restIngestaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIngestaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIngesta() throws Exception {
        // Get the ingesta
        restIngestaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIngesta() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        int databaseSizeBeforeUpdate = ingestaRepository.findAll().size();

        // Update the ingesta
        Ingesta updatedIngesta = ingestaRepository.findById(ingesta.getId()).get();
        // Disconnect from session so that the updates on updatedIngesta are not directly saved in db
        em.detach(updatedIngesta);
        updatedIngesta.tipo(UPDATED_TIPO).consumoCalorias(UPDATED_CONSUMO_CALORIAS).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restIngestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIngesta.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIngesta))
            )
            .andExpect(status().isOk());

        // Validate the Ingesta in the database
        List<Ingesta> ingestaList = ingestaRepository.findAll();
        assertThat(ingestaList).hasSize(databaseSizeBeforeUpdate);
        Ingesta testIngesta = ingestaList.get(ingestaList.size() - 1);
        assertThat(testIngesta.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testIngesta.getConsumoCalorias()).isEqualTo(UPDATED_CONSUMO_CALORIAS);
        assertThat(testIngesta.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void putNonExistingIngesta() throws Exception {
        int databaseSizeBeforeUpdate = ingestaRepository.findAll().size();
        ingesta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ingesta.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ingesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingesta in the database
        List<Ingesta> ingestaList = ingestaRepository.findAll();
        assertThat(ingestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIngesta() throws Exception {
        int databaseSizeBeforeUpdate = ingestaRepository.findAll().size();
        ingesta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ingesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingesta in the database
        List<Ingesta> ingestaList = ingestaRepository.findAll();
        assertThat(ingestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIngesta() throws Exception {
        int databaseSizeBeforeUpdate = ingestaRepository.findAll().size();
        ingesta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngestaMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingesta))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ingesta in the database
        List<Ingesta> ingestaList = ingestaRepository.findAll();
        assertThat(ingestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIngestaWithPatch() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        int databaseSizeBeforeUpdate = ingestaRepository.findAll().size();

        // Update the ingesta using partial update
        Ingesta partialUpdatedIngesta = new Ingesta();
        partialUpdatedIngesta.setId(ingesta.getId());

        partialUpdatedIngesta.fechaRegistro(UPDATED_FECHA_REGISTRO);

        restIngestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIngesta.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIngesta))
            )
            .andExpect(status().isOk());

        // Validate the Ingesta in the database
        List<Ingesta> ingestaList = ingestaRepository.findAll();
        assertThat(ingestaList).hasSize(databaseSizeBeforeUpdate);
        Ingesta testIngesta = ingestaList.get(ingestaList.size() - 1);
        assertThat(testIngesta.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testIngesta.getConsumoCalorias()).isEqualTo(DEFAULT_CONSUMO_CALORIAS);
        assertThat(testIngesta.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void fullUpdateIngestaWithPatch() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        int databaseSizeBeforeUpdate = ingestaRepository.findAll().size();

        // Update the ingesta using partial update
        Ingesta partialUpdatedIngesta = new Ingesta();
        partialUpdatedIngesta.setId(ingesta.getId());

        partialUpdatedIngesta.tipo(UPDATED_TIPO).consumoCalorias(UPDATED_CONSUMO_CALORIAS).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restIngestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIngesta.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIngesta))
            )
            .andExpect(status().isOk());

        // Validate the Ingesta in the database
        List<Ingesta> ingestaList = ingestaRepository.findAll();
        assertThat(ingestaList).hasSize(databaseSizeBeforeUpdate);
        Ingesta testIngesta = ingestaList.get(ingestaList.size() - 1);
        assertThat(testIngesta.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testIngesta.getConsumoCalorias()).isEqualTo(UPDATED_CONSUMO_CALORIAS);
        assertThat(testIngesta.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void patchNonExistingIngesta() throws Exception {
        int databaseSizeBeforeUpdate = ingestaRepository.findAll().size();
        ingesta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ingesta.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ingesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingesta in the database
        List<Ingesta> ingestaList = ingestaRepository.findAll();
        assertThat(ingestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIngesta() throws Exception {
        int databaseSizeBeforeUpdate = ingestaRepository.findAll().size();
        ingesta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ingesta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingesta in the database
        List<Ingesta> ingestaList = ingestaRepository.findAll();
        assertThat(ingestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIngesta() throws Exception {
        int databaseSizeBeforeUpdate = ingestaRepository.findAll().size();
        ingesta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngestaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ingesta))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ingesta in the database
        List<Ingesta> ingestaList = ingestaRepository.findAll();
        assertThat(ingestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIngesta() throws Exception {
        // Initialize the database
        ingestaRepository.saveAndFlush(ingesta);

        int databaseSizeBeforeDelete = ingestaRepository.findAll().size();

        // Delete the ingesta
        restIngestaMockMvc
            .perform(delete(ENTITY_API_URL_ID, ingesta.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ingesta> ingestaList = ingestaRepository.findAll();
        assertThat(ingestaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

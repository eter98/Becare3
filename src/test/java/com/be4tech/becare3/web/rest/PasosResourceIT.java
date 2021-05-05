package com.be4tech.becare3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.Pasos;
import com.be4tech.becare3.domain.User;
import com.be4tech.becare3.repository.PasosRepository;
import com.be4tech.becare3.service.criteria.PasosCriteria;
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
 * Integration tests for the {@link PasosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PasosResourceIT {

    private static final Integer DEFAULT_NRO_PASOS = 1;
    private static final Integer UPDATED_NRO_PASOS = 2;
    private static final Integer SMALLER_NRO_PASOS = 1 - 1;

    private static final Instant DEFAULT_TIME_INSTANT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME_INSTANT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/pasos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PasosRepository pasosRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPasosMockMvc;

    private Pasos pasos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pasos createEntity(EntityManager em) {
        Pasos pasos = new Pasos().nroPasos(DEFAULT_NRO_PASOS).timeInstant(DEFAULT_TIME_INSTANT);
        return pasos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pasos createUpdatedEntity(EntityManager em) {
        Pasos pasos = new Pasos().nroPasos(UPDATED_NRO_PASOS).timeInstant(UPDATED_TIME_INSTANT);
        return pasos;
    }

    @BeforeEach
    public void initTest() {
        pasos = createEntity(em);
    }

    @Test
    @Transactional
    void createPasos() throws Exception {
        int databaseSizeBeforeCreate = pasosRepository.findAll().size();
        // Create the Pasos
        restPasosMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pasos))
            )
            .andExpect(status().isCreated());

        // Validate the Pasos in the database
        List<Pasos> pasosList = pasosRepository.findAll();
        assertThat(pasosList).hasSize(databaseSizeBeforeCreate + 1);
        Pasos testPasos = pasosList.get(pasosList.size() - 1);
        assertThat(testPasos.getNroPasos()).isEqualTo(DEFAULT_NRO_PASOS);
        assertThat(testPasos.getTimeInstant()).isEqualTo(DEFAULT_TIME_INSTANT);
    }

    @Test
    @Transactional
    void createPasosWithExistingId() throws Exception {
        // Create the Pasos with an existing ID
        pasos.setId(1L);

        int databaseSizeBeforeCreate = pasosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPasosMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pasos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pasos in the database
        List<Pasos> pasosList = pasosRepository.findAll();
        assertThat(pasosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPasos() throws Exception {
        // Initialize the database
        pasosRepository.saveAndFlush(pasos);

        // Get all the pasosList
        restPasosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pasos.getId().intValue())))
            .andExpect(jsonPath("$.[*].nroPasos").value(hasItem(DEFAULT_NRO_PASOS)))
            .andExpect(jsonPath("$.[*].timeInstant").value(hasItem(DEFAULT_TIME_INSTANT.toString())));
    }

    @Test
    @Transactional
    void getPasos() throws Exception {
        // Initialize the database
        pasosRepository.saveAndFlush(pasos);

        // Get the pasos
        restPasosMockMvc
            .perform(get(ENTITY_API_URL_ID, pasos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pasos.getId().intValue()))
            .andExpect(jsonPath("$.nroPasos").value(DEFAULT_NRO_PASOS))
            .andExpect(jsonPath("$.timeInstant").value(DEFAULT_TIME_INSTANT.toString()));
    }

    @Test
    @Transactional
    void getPasosByIdFiltering() throws Exception {
        // Initialize the database
        pasosRepository.saveAndFlush(pasos);

        Long id = pasos.getId();

        defaultPasosShouldBeFound("id.equals=" + id);
        defaultPasosShouldNotBeFound("id.notEquals=" + id);

        defaultPasosShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPasosShouldNotBeFound("id.greaterThan=" + id);

        defaultPasosShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPasosShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPasosByNroPasosIsEqualToSomething() throws Exception {
        // Initialize the database
        pasosRepository.saveAndFlush(pasos);

        // Get all the pasosList where nroPasos equals to DEFAULT_NRO_PASOS
        defaultPasosShouldBeFound("nroPasos.equals=" + DEFAULT_NRO_PASOS);

        // Get all the pasosList where nroPasos equals to UPDATED_NRO_PASOS
        defaultPasosShouldNotBeFound("nroPasos.equals=" + UPDATED_NRO_PASOS);
    }

    @Test
    @Transactional
    void getAllPasosByNroPasosIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pasosRepository.saveAndFlush(pasos);

        // Get all the pasosList where nroPasos not equals to DEFAULT_NRO_PASOS
        defaultPasosShouldNotBeFound("nroPasos.notEquals=" + DEFAULT_NRO_PASOS);

        // Get all the pasosList where nroPasos not equals to UPDATED_NRO_PASOS
        defaultPasosShouldBeFound("nroPasos.notEquals=" + UPDATED_NRO_PASOS);
    }

    @Test
    @Transactional
    void getAllPasosByNroPasosIsInShouldWork() throws Exception {
        // Initialize the database
        pasosRepository.saveAndFlush(pasos);

        // Get all the pasosList where nroPasos in DEFAULT_NRO_PASOS or UPDATED_NRO_PASOS
        defaultPasosShouldBeFound("nroPasos.in=" + DEFAULT_NRO_PASOS + "," + UPDATED_NRO_PASOS);

        // Get all the pasosList where nroPasos equals to UPDATED_NRO_PASOS
        defaultPasosShouldNotBeFound("nroPasos.in=" + UPDATED_NRO_PASOS);
    }

    @Test
    @Transactional
    void getAllPasosByNroPasosIsNullOrNotNull() throws Exception {
        // Initialize the database
        pasosRepository.saveAndFlush(pasos);

        // Get all the pasosList where nroPasos is not null
        defaultPasosShouldBeFound("nroPasos.specified=true");

        // Get all the pasosList where nroPasos is null
        defaultPasosShouldNotBeFound("nroPasos.specified=false");
    }

    @Test
    @Transactional
    void getAllPasosByNroPasosIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pasosRepository.saveAndFlush(pasos);

        // Get all the pasosList where nroPasos is greater than or equal to DEFAULT_NRO_PASOS
        defaultPasosShouldBeFound("nroPasos.greaterThanOrEqual=" + DEFAULT_NRO_PASOS);

        // Get all the pasosList where nroPasos is greater than or equal to UPDATED_NRO_PASOS
        defaultPasosShouldNotBeFound("nroPasos.greaterThanOrEqual=" + UPDATED_NRO_PASOS);
    }

    @Test
    @Transactional
    void getAllPasosByNroPasosIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pasosRepository.saveAndFlush(pasos);

        // Get all the pasosList where nroPasos is less than or equal to DEFAULT_NRO_PASOS
        defaultPasosShouldBeFound("nroPasos.lessThanOrEqual=" + DEFAULT_NRO_PASOS);

        // Get all the pasosList where nroPasos is less than or equal to SMALLER_NRO_PASOS
        defaultPasosShouldNotBeFound("nroPasos.lessThanOrEqual=" + SMALLER_NRO_PASOS);
    }

    @Test
    @Transactional
    void getAllPasosByNroPasosIsLessThanSomething() throws Exception {
        // Initialize the database
        pasosRepository.saveAndFlush(pasos);

        // Get all the pasosList where nroPasos is less than DEFAULT_NRO_PASOS
        defaultPasosShouldNotBeFound("nroPasos.lessThan=" + DEFAULT_NRO_PASOS);

        // Get all the pasosList where nroPasos is less than UPDATED_NRO_PASOS
        defaultPasosShouldBeFound("nroPasos.lessThan=" + UPDATED_NRO_PASOS);
    }

    @Test
    @Transactional
    void getAllPasosByNroPasosIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pasosRepository.saveAndFlush(pasos);

        // Get all the pasosList where nroPasos is greater than DEFAULT_NRO_PASOS
        defaultPasosShouldNotBeFound("nroPasos.greaterThan=" + DEFAULT_NRO_PASOS);

        // Get all the pasosList where nroPasos is greater than SMALLER_NRO_PASOS
        defaultPasosShouldBeFound("nroPasos.greaterThan=" + SMALLER_NRO_PASOS);
    }

    @Test
    @Transactional
    void getAllPasosByTimeInstantIsEqualToSomething() throws Exception {
        // Initialize the database
        pasosRepository.saveAndFlush(pasos);

        // Get all the pasosList where timeInstant equals to DEFAULT_TIME_INSTANT
        defaultPasosShouldBeFound("timeInstant.equals=" + DEFAULT_TIME_INSTANT);

        // Get all the pasosList where timeInstant equals to UPDATED_TIME_INSTANT
        defaultPasosShouldNotBeFound("timeInstant.equals=" + UPDATED_TIME_INSTANT);
    }

    @Test
    @Transactional
    void getAllPasosByTimeInstantIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pasosRepository.saveAndFlush(pasos);

        // Get all the pasosList where timeInstant not equals to DEFAULT_TIME_INSTANT
        defaultPasosShouldNotBeFound("timeInstant.notEquals=" + DEFAULT_TIME_INSTANT);

        // Get all the pasosList where timeInstant not equals to UPDATED_TIME_INSTANT
        defaultPasosShouldBeFound("timeInstant.notEquals=" + UPDATED_TIME_INSTANT);
    }

    @Test
    @Transactional
    void getAllPasosByTimeInstantIsInShouldWork() throws Exception {
        // Initialize the database
        pasosRepository.saveAndFlush(pasos);

        // Get all the pasosList where timeInstant in DEFAULT_TIME_INSTANT or UPDATED_TIME_INSTANT
        defaultPasosShouldBeFound("timeInstant.in=" + DEFAULT_TIME_INSTANT + "," + UPDATED_TIME_INSTANT);

        // Get all the pasosList where timeInstant equals to UPDATED_TIME_INSTANT
        defaultPasosShouldNotBeFound("timeInstant.in=" + UPDATED_TIME_INSTANT);
    }

    @Test
    @Transactional
    void getAllPasosByTimeInstantIsNullOrNotNull() throws Exception {
        // Initialize the database
        pasosRepository.saveAndFlush(pasos);

        // Get all the pasosList where timeInstant is not null
        defaultPasosShouldBeFound("timeInstant.specified=true");

        // Get all the pasosList where timeInstant is null
        defaultPasosShouldNotBeFound("timeInstant.specified=false");
    }

    @Test
    @Transactional
    void getAllPasosByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        pasosRepository.saveAndFlush(pasos);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        pasos.setUser(user);
        pasosRepository.saveAndFlush(pasos);
        String userId = user.getId();

        // Get all the pasosList where user equals to userId
        defaultPasosShouldBeFound("userId.equals=" + userId);

        // Get all the pasosList where user equals to "invalid-id"
        defaultPasosShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPasosShouldBeFound(String filter) throws Exception {
        restPasosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pasos.getId().intValue())))
            .andExpect(jsonPath("$.[*].nroPasos").value(hasItem(DEFAULT_NRO_PASOS)))
            .andExpect(jsonPath("$.[*].timeInstant").value(hasItem(DEFAULT_TIME_INSTANT.toString())));

        // Check, that the count call also returns 1
        restPasosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPasosShouldNotBeFound(String filter) throws Exception {
        restPasosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPasosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPasos() throws Exception {
        // Get the pasos
        restPasosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPasos() throws Exception {
        // Initialize the database
        pasosRepository.saveAndFlush(pasos);

        int databaseSizeBeforeUpdate = pasosRepository.findAll().size();

        // Update the pasos
        Pasos updatedPasos = pasosRepository.findById(pasos.getId()).get();
        // Disconnect from session so that the updates on updatedPasos are not directly saved in db
        em.detach(updatedPasos);
        updatedPasos.nroPasos(UPDATED_NRO_PASOS).timeInstant(UPDATED_TIME_INSTANT);

        restPasosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPasos.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPasos))
            )
            .andExpect(status().isOk());

        // Validate the Pasos in the database
        List<Pasos> pasosList = pasosRepository.findAll();
        assertThat(pasosList).hasSize(databaseSizeBeforeUpdate);
        Pasos testPasos = pasosList.get(pasosList.size() - 1);
        assertThat(testPasos.getNroPasos()).isEqualTo(UPDATED_NRO_PASOS);
        assertThat(testPasos.getTimeInstant()).isEqualTo(UPDATED_TIME_INSTANT);
    }

    @Test
    @Transactional
    void putNonExistingPasos() throws Exception {
        int databaseSizeBeforeUpdate = pasosRepository.findAll().size();
        pasos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPasosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pasos.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pasos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pasos in the database
        List<Pasos> pasosList = pasosRepository.findAll();
        assertThat(pasosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPasos() throws Exception {
        int databaseSizeBeforeUpdate = pasosRepository.findAll().size();
        pasos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPasosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pasos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pasos in the database
        List<Pasos> pasosList = pasosRepository.findAll();
        assertThat(pasosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPasos() throws Exception {
        int databaseSizeBeforeUpdate = pasosRepository.findAll().size();
        pasos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPasosMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pasos))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pasos in the database
        List<Pasos> pasosList = pasosRepository.findAll();
        assertThat(pasosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePasosWithPatch() throws Exception {
        // Initialize the database
        pasosRepository.saveAndFlush(pasos);

        int databaseSizeBeforeUpdate = pasosRepository.findAll().size();

        // Update the pasos using partial update
        Pasos partialUpdatedPasos = new Pasos();
        partialUpdatedPasos.setId(pasos.getId());

        partialUpdatedPasos.nroPasos(UPDATED_NRO_PASOS).timeInstant(UPDATED_TIME_INSTANT);

        restPasosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPasos.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPasos))
            )
            .andExpect(status().isOk());

        // Validate the Pasos in the database
        List<Pasos> pasosList = pasosRepository.findAll();
        assertThat(pasosList).hasSize(databaseSizeBeforeUpdate);
        Pasos testPasos = pasosList.get(pasosList.size() - 1);
        assertThat(testPasos.getNroPasos()).isEqualTo(UPDATED_NRO_PASOS);
        assertThat(testPasos.getTimeInstant()).isEqualTo(UPDATED_TIME_INSTANT);
    }

    @Test
    @Transactional
    void fullUpdatePasosWithPatch() throws Exception {
        // Initialize the database
        pasosRepository.saveAndFlush(pasos);

        int databaseSizeBeforeUpdate = pasosRepository.findAll().size();

        // Update the pasos using partial update
        Pasos partialUpdatedPasos = new Pasos();
        partialUpdatedPasos.setId(pasos.getId());

        partialUpdatedPasos.nroPasos(UPDATED_NRO_PASOS).timeInstant(UPDATED_TIME_INSTANT);

        restPasosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPasos.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPasos))
            )
            .andExpect(status().isOk());

        // Validate the Pasos in the database
        List<Pasos> pasosList = pasosRepository.findAll();
        assertThat(pasosList).hasSize(databaseSizeBeforeUpdate);
        Pasos testPasos = pasosList.get(pasosList.size() - 1);
        assertThat(testPasos.getNroPasos()).isEqualTo(UPDATED_NRO_PASOS);
        assertThat(testPasos.getTimeInstant()).isEqualTo(UPDATED_TIME_INSTANT);
    }

    @Test
    @Transactional
    void patchNonExistingPasos() throws Exception {
        int databaseSizeBeforeUpdate = pasosRepository.findAll().size();
        pasos.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPasosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pasos.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pasos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pasos in the database
        List<Pasos> pasosList = pasosRepository.findAll();
        assertThat(pasosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPasos() throws Exception {
        int databaseSizeBeforeUpdate = pasosRepository.findAll().size();
        pasos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPasosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pasos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pasos in the database
        List<Pasos> pasosList = pasosRepository.findAll();
        assertThat(pasosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPasos() throws Exception {
        int databaseSizeBeforeUpdate = pasosRepository.findAll().size();
        pasos.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPasosMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pasos))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pasos in the database
        List<Pasos> pasosList = pasosRepository.findAll();
        assertThat(pasosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePasos() throws Exception {
        // Initialize the database
        pasosRepository.saveAndFlush(pasos);

        int databaseSizeBeforeDelete = pasosRepository.findAll().size();

        // Delete the pasos
        restPasosMockMvc
            .perform(delete(ENTITY_API_URL_ID, pasos.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pasos> pasosList = pasosRepository.findAll();
        assertThat(pasosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

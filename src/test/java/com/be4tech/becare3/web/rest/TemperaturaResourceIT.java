package com.be4tech.becare3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.Temperatura;
import com.be4tech.becare3.domain.User;
import com.be4tech.becare3.repository.TemperaturaRepository;
import com.be4tech.becare3.service.criteria.TemperaturaCriteria;
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
 * Integration tests for the {@link TemperaturaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TemperaturaResourceIT {

    private static final Float DEFAULT_TEMPERATURA = 1F;
    private static final Float UPDATED_TEMPERATURA = 2F;
    private static final Float SMALLER_TEMPERATURA = 1F - 1F;

    private static final Instant DEFAULT_FECHA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/temperaturas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TemperaturaRepository temperaturaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTemperaturaMockMvc;

    private Temperatura temperatura;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Temperatura createEntity(EntityManager em) {
        Temperatura temperatura = new Temperatura().temperatura(DEFAULT_TEMPERATURA).fechaRegistro(DEFAULT_FECHA_REGISTRO);
        return temperatura;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Temperatura createUpdatedEntity(EntityManager em) {
        Temperatura temperatura = new Temperatura().temperatura(UPDATED_TEMPERATURA).fechaRegistro(UPDATED_FECHA_REGISTRO);
        return temperatura;
    }

    @BeforeEach
    public void initTest() {
        temperatura = createEntity(em);
    }

    @Test
    @Transactional
    void createTemperatura() throws Exception {
        int databaseSizeBeforeCreate = temperaturaRepository.findAll().size();
        // Create the Temperatura
        restTemperaturaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(temperatura))
            )
            .andExpect(status().isCreated());

        // Validate the Temperatura in the database
        List<Temperatura> temperaturaList = temperaturaRepository.findAll();
        assertThat(temperaturaList).hasSize(databaseSizeBeforeCreate + 1);
        Temperatura testTemperatura = temperaturaList.get(temperaturaList.size() - 1);
        assertThat(testTemperatura.getTemperatura()).isEqualTo(DEFAULT_TEMPERATURA);
        assertThat(testTemperatura.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void createTemperaturaWithExistingId() throws Exception {
        // Create the Temperatura with an existing ID
        temperatura.setId(1L);

        int databaseSizeBeforeCreate = temperaturaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTemperaturaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(temperatura))
            )
            .andExpect(status().isBadRequest());

        // Validate the Temperatura in the database
        List<Temperatura> temperaturaList = temperaturaRepository.findAll();
        assertThat(temperaturaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTemperaturas() throws Exception {
        // Initialize the database
        temperaturaRepository.saveAndFlush(temperatura);

        // Get all the temperaturaList
        restTemperaturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(temperatura.getId().intValue())))
            .andExpect(jsonPath("$.[*].temperatura").value(hasItem(DEFAULT_TEMPERATURA.doubleValue())))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));
    }

    @Test
    @Transactional
    void getTemperatura() throws Exception {
        // Initialize the database
        temperaturaRepository.saveAndFlush(temperatura);

        // Get the temperatura
        restTemperaturaMockMvc
            .perform(get(ENTITY_API_URL_ID, temperatura.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(temperatura.getId().intValue()))
            .andExpect(jsonPath("$.temperatura").value(DEFAULT_TEMPERATURA.doubleValue()))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()));
    }

    @Test
    @Transactional
    void getTemperaturasByIdFiltering() throws Exception {
        // Initialize the database
        temperaturaRepository.saveAndFlush(temperatura);

        Long id = temperatura.getId();

        defaultTemperaturaShouldBeFound("id.equals=" + id);
        defaultTemperaturaShouldNotBeFound("id.notEquals=" + id);

        defaultTemperaturaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTemperaturaShouldNotBeFound("id.greaterThan=" + id);

        defaultTemperaturaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTemperaturaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTemperaturasByTemperaturaIsEqualToSomething() throws Exception {
        // Initialize the database
        temperaturaRepository.saveAndFlush(temperatura);

        // Get all the temperaturaList where temperatura equals to DEFAULT_TEMPERATURA
        defaultTemperaturaShouldBeFound("temperatura.equals=" + DEFAULT_TEMPERATURA);

        // Get all the temperaturaList where temperatura equals to UPDATED_TEMPERATURA
        defaultTemperaturaShouldNotBeFound("temperatura.equals=" + UPDATED_TEMPERATURA);
    }

    @Test
    @Transactional
    void getAllTemperaturasByTemperaturaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        temperaturaRepository.saveAndFlush(temperatura);

        // Get all the temperaturaList where temperatura not equals to DEFAULT_TEMPERATURA
        defaultTemperaturaShouldNotBeFound("temperatura.notEquals=" + DEFAULT_TEMPERATURA);

        // Get all the temperaturaList where temperatura not equals to UPDATED_TEMPERATURA
        defaultTemperaturaShouldBeFound("temperatura.notEquals=" + UPDATED_TEMPERATURA);
    }

    @Test
    @Transactional
    void getAllTemperaturasByTemperaturaIsInShouldWork() throws Exception {
        // Initialize the database
        temperaturaRepository.saveAndFlush(temperatura);

        // Get all the temperaturaList where temperatura in DEFAULT_TEMPERATURA or UPDATED_TEMPERATURA
        defaultTemperaturaShouldBeFound("temperatura.in=" + DEFAULT_TEMPERATURA + "," + UPDATED_TEMPERATURA);

        // Get all the temperaturaList where temperatura equals to UPDATED_TEMPERATURA
        defaultTemperaturaShouldNotBeFound("temperatura.in=" + UPDATED_TEMPERATURA);
    }

    @Test
    @Transactional
    void getAllTemperaturasByTemperaturaIsNullOrNotNull() throws Exception {
        // Initialize the database
        temperaturaRepository.saveAndFlush(temperatura);

        // Get all the temperaturaList where temperatura is not null
        defaultTemperaturaShouldBeFound("temperatura.specified=true");

        // Get all the temperaturaList where temperatura is null
        defaultTemperaturaShouldNotBeFound("temperatura.specified=false");
    }

    @Test
    @Transactional
    void getAllTemperaturasByTemperaturaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        temperaturaRepository.saveAndFlush(temperatura);

        // Get all the temperaturaList where temperatura is greater than or equal to DEFAULT_TEMPERATURA
        defaultTemperaturaShouldBeFound("temperatura.greaterThanOrEqual=" + DEFAULT_TEMPERATURA);

        // Get all the temperaturaList where temperatura is greater than or equal to UPDATED_TEMPERATURA
        defaultTemperaturaShouldNotBeFound("temperatura.greaterThanOrEqual=" + UPDATED_TEMPERATURA);
    }

    @Test
    @Transactional
    void getAllTemperaturasByTemperaturaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        temperaturaRepository.saveAndFlush(temperatura);

        // Get all the temperaturaList where temperatura is less than or equal to DEFAULT_TEMPERATURA
        defaultTemperaturaShouldBeFound("temperatura.lessThanOrEqual=" + DEFAULT_TEMPERATURA);

        // Get all the temperaturaList where temperatura is less than or equal to SMALLER_TEMPERATURA
        defaultTemperaturaShouldNotBeFound("temperatura.lessThanOrEqual=" + SMALLER_TEMPERATURA);
    }

    @Test
    @Transactional
    void getAllTemperaturasByTemperaturaIsLessThanSomething() throws Exception {
        // Initialize the database
        temperaturaRepository.saveAndFlush(temperatura);

        // Get all the temperaturaList where temperatura is less than DEFAULT_TEMPERATURA
        defaultTemperaturaShouldNotBeFound("temperatura.lessThan=" + DEFAULT_TEMPERATURA);

        // Get all the temperaturaList where temperatura is less than UPDATED_TEMPERATURA
        defaultTemperaturaShouldBeFound("temperatura.lessThan=" + UPDATED_TEMPERATURA);
    }

    @Test
    @Transactional
    void getAllTemperaturasByTemperaturaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        temperaturaRepository.saveAndFlush(temperatura);

        // Get all the temperaturaList where temperatura is greater than DEFAULT_TEMPERATURA
        defaultTemperaturaShouldNotBeFound("temperatura.greaterThan=" + DEFAULT_TEMPERATURA);

        // Get all the temperaturaList where temperatura is greater than SMALLER_TEMPERATURA
        defaultTemperaturaShouldBeFound("temperatura.greaterThan=" + SMALLER_TEMPERATURA);
    }

    @Test
    @Transactional
    void getAllTemperaturasByFechaRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        temperaturaRepository.saveAndFlush(temperatura);

        // Get all the temperaturaList where fechaRegistro equals to DEFAULT_FECHA_REGISTRO
        defaultTemperaturaShouldBeFound("fechaRegistro.equals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the temperaturaList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultTemperaturaShouldNotBeFound("fechaRegistro.equals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllTemperaturasByFechaRegistroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        temperaturaRepository.saveAndFlush(temperatura);

        // Get all the temperaturaList where fechaRegistro not equals to DEFAULT_FECHA_REGISTRO
        defaultTemperaturaShouldNotBeFound("fechaRegistro.notEquals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the temperaturaList where fechaRegistro not equals to UPDATED_FECHA_REGISTRO
        defaultTemperaturaShouldBeFound("fechaRegistro.notEquals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllTemperaturasByFechaRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        temperaturaRepository.saveAndFlush(temperatura);

        // Get all the temperaturaList where fechaRegistro in DEFAULT_FECHA_REGISTRO or UPDATED_FECHA_REGISTRO
        defaultTemperaturaShouldBeFound("fechaRegistro.in=" + DEFAULT_FECHA_REGISTRO + "," + UPDATED_FECHA_REGISTRO);

        // Get all the temperaturaList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultTemperaturaShouldNotBeFound("fechaRegistro.in=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllTemperaturasByFechaRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        temperaturaRepository.saveAndFlush(temperatura);

        // Get all the temperaturaList where fechaRegistro is not null
        defaultTemperaturaShouldBeFound("fechaRegistro.specified=true");

        // Get all the temperaturaList where fechaRegistro is null
        defaultTemperaturaShouldNotBeFound("fechaRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllTemperaturasByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        temperaturaRepository.saveAndFlush(temperatura);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        temperatura.setUser(user);
        temperaturaRepository.saveAndFlush(temperatura);
        String userId = user.getId();

        // Get all the temperaturaList where user equals to userId
        defaultTemperaturaShouldBeFound("userId.equals=" + userId);

        // Get all the temperaturaList where user equals to "invalid-id"
        defaultTemperaturaShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTemperaturaShouldBeFound(String filter) throws Exception {
        restTemperaturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(temperatura.getId().intValue())))
            .andExpect(jsonPath("$.[*].temperatura").value(hasItem(DEFAULT_TEMPERATURA.doubleValue())))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));

        // Check, that the count call also returns 1
        restTemperaturaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTemperaturaShouldNotBeFound(String filter) throws Exception {
        restTemperaturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTemperaturaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTemperatura() throws Exception {
        // Get the temperatura
        restTemperaturaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTemperatura() throws Exception {
        // Initialize the database
        temperaturaRepository.saveAndFlush(temperatura);

        int databaseSizeBeforeUpdate = temperaturaRepository.findAll().size();

        // Update the temperatura
        Temperatura updatedTemperatura = temperaturaRepository.findById(temperatura.getId()).get();
        // Disconnect from session so that the updates on updatedTemperatura are not directly saved in db
        em.detach(updatedTemperatura);
        updatedTemperatura.temperatura(UPDATED_TEMPERATURA).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restTemperaturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTemperatura.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTemperatura))
            )
            .andExpect(status().isOk());

        // Validate the Temperatura in the database
        List<Temperatura> temperaturaList = temperaturaRepository.findAll();
        assertThat(temperaturaList).hasSize(databaseSizeBeforeUpdate);
        Temperatura testTemperatura = temperaturaList.get(temperaturaList.size() - 1);
        assertThat(testTemperatura.getTemperatura()).isEqualTo(UPDATED_TEMPERATURA);
        assertThat(testTemperatura.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void putNonExistingTemperatura() throws Exception {
        int databaseSizeBeforeUpdate = temperaturaRepository.findAll().size();
        temperatura.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemperaturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, temperatura.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(temperatura))
            )
            .andExpect(status().isBadRequest());

        // Validate the Temperatura in the database
        List<Temperatura> temperaturaList = temperaturaRepository.findAll();
        assertThat(temperaturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTemperatura() throws Exception {
        int databaseSizeBeforeUpdate = temperaturaRepository.findAll().size();
        temperatura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemperaturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(temperatura))
            )
            .andExpect(status().isBadRequest());

        // Validate the Temperatura in the database
        List<Temperatura> temperaturaList = temperaturaRepository.findAll();
        assertThat(temperaturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTemperatura() throws Exception {
        int databaseSizeBeforeUpdate = temperaturaRepository.findAll().size();
        temperatura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemperaturaMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(temperatura))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Temperatura in the database
        List<Temperatura> temperaturaList = temperaturaRepository.findAll();
        assertThat(temperaturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTemperaturaWithPatch() throws Exception {
        // Initialize the database
        temperaturaRepository.saveAndFlush(temperatura);

        int databaseSizeBeforeUpdate = temperaturaRepository.findAll().size();

        // Update the temperatura using partial update
        Temperatura partialUpdatedTemperatura = new Temperatura();
        partialUpdatedTemperatura.setId(temperatura.getId());

        restTemperaturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTemperatura.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTemperatura))
            )
            .andExpect(status().isOk());

        // Validate the Temperatura in the database
        List<Temperatura> temperaturaList = temperaturaRepository.findAll();
        assertThat(temperaturaList).hasSize(databaseSizeBeforeUpdate);
        Temperatura testTemperatura = temperaturaList.get(temperaturaList.size() - 1);
        assertThat(testTemperatura.getTemperatura()).isEqualTo(DEFAULT_TEMPERATURA);
        assertThat(testTemperatura.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void fullUpdateTemperaturaWithPatch() throws Exception {
        // Initialize the database
        temperaturaRepository.saveAndFlush(temperatura);

        int databaseSizeBeforeUpdate = temperaturaRepository.findAll().size();

        // Update the temperatura using partial update
        Temperatura partialUpdatedTemperatura = new Temperatura();
        partialUpdatedTemperatura.setId(temperatura.getId());

        partialUpdatedTemperatura.temperatura(UPDATED_TEMPERATURA).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restTemperaturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTemperatura.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTemperatura))
            )
            .andExpect(status().isOk());

        // Validate the Temperatura in the database
        List<Temperatura> temperaturaList = temperaturaRepository.findAll();
        assertThat(temperaturaList).hasSize(databaseSizeBeforeUpdate);
        Temperatura testTemperatura = temperaturaList.get(temperaturaList.size() - 1);
        assertThat(testTemperatura.getTemperatura()).isEqualTo(UPDATED_TEMPERATURA);
        assertThat(testTemperatura.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void patchNonExistingTemperatura() throws Exception {
        int databaseSizeBeforeUpdate = temperaturaRepository.findAll().size();
        temperatura.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemperaturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, temperatura.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(temperatura))
            )
            .andExpect(status().isBadRequest());

        // Validate the Temperatura in the database
        List<Temperatura> temperaturaList = temperaturaRepository.findAll();
        assertThat(temperaturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTemperatura() throws Exception {
        int databaseSizeBeforeUpdate = temperaturaRepository.findAll().size();
        temperatura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemperaturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(temperatura))
            )
            .andExpect(status().isBadRequest());

        // Validate the Temperatura in the database
        List<Temperatura> temperaturaList = temperaturaRepository.findAll();
        assertThat(temperaturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTemperatura() throws Exception {
        int databaseSizeBeforeUpdate = temperaturaRepository.findAll().size();
        temperatura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemperaturaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(temperatura))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Temperatura in the database
        List<Temperatura> temperaturaList = temperaturaRepository.findAll();
        assertThat(temperaturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTemperatura() throws Exception {
        // Initialize the database
        temperaturaRepository.saveAndFlush(temperatura);

        int databaseSizeBeforeDelete = temperaturaRepository.findAll().size();

        // Delete the temperatura
        restTemperaturaMockMvc
            .perform(delete(ENTITY_API_URL_ID, temperatura.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Temperatura> temperaturaList = temperaturaRepository.findAll();
        assertThat(temperaturaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

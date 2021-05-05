package com.be4tech.becare3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.Oximetria;
import com.be4tech.becare3.domain.User;
import com.be4tech.becare3.repository.OximetriaRepository;
import com.be4tech.becare3.service.criteria.OximetriaCriteria;
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
 * Integration tests for the {@link OximetriaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OximetriaResourceIT {

    private static final Integer DEFAULT_OXIMETRIA = 1;
    private static final Integer UPDATED_OXIMETRIA = 2;
    private static final Integer SMALLER_OXIMETRIA = 1 - 1;

    private static final Instant DEFAULT_FECHA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/oximetrias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OximetriaRepository oximetriaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOximetriaMockMvc;

    private Oximetria oximetria;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Oximetria createEntity(EntityManager em) {
        Oximetria oximetria = new Oximetria().oximetria(DEFAULT_OXIMETRIA).fechaRegistro(DEFAULT_FECHA_REGISTRO);
        return oximetria;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Oximetria createUpdatedEntity(EntityManager em) {
        Oximetria oximetria = new Oximetria().oximetria(UPDATED_OXIMETRIA).fechaRegistro(UPDATED_FECHA_REGISTRO);
        return oximetria;
    }

    @BeforeEach
    public void initTest() {
        oximetria = createEntity(em);
    }

    @Test
    @Transactional
    void createOximetria() throws Exception {
        int databaseSizeBeforeCreate = oximetriaRepository.findAll().size();
        // Create the Oximetria
        restOximetriaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oximetria))
            )
            .andExpect(status().isCreated());

        // Validate the Oximetria in the database
        List<Oximetria> oximetriaList = oximetriaRepository.findAll();
        assertThat(oximetriaList).hasSize(databaseSizeBeforeCreate + 1);
        Oximetria testOximetria = oximetriaList.get(oximetriaList.size() - 1);
        assertThat(testOximetria.getOximetria()).isEqualTo(DEFAULT_OXIMETRIA);
        assertThat(testOximetria.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void createOximetriaWithExistingId() throws Exception {
        // Create the Oximetria with an existing ID
        oximetria.setId(1L);

        int databaseSizeBeforeCreate = oximetriaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOximetriaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oximetria))
            )
            .andExpect(status().isBadRequest());

        // Validate the Oximetria in the database
        List<Oximetria> oximetriaList = oximetriaRepository.findAll();
        assertThat(oximetriaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOximetrias() throws Exception {
        // Initialize the database
        oximetriaRepository.saveAndFlush(oximetria);

        // Get all the oximetriaList
        restOximetriaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oximetria.getId().intValue())))
            .andExpect(jsonPath("$.[*].oximetria").value(hasItem(DEFAULT_OXIMETRIA)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));
    }

    @Test
    @Transactional
    void getOximetria() throws Exception {
        // Initialize the database
        oximetriaRepository.saveAndFlush(oximetria);

        // Get the oximetria
        restOximetriaMockMvc
            .perform(get(ENTITY_API_URL_ID, oximetria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(oximetria.getId().intValue()))
            .andExpect(jsonPath("$.oximetria").value(DEFAULT_OXIMETRIA))
            .andExpect(jsonPath("$.fechaRegistro").value(DEFAULT_FECHA_REGISTRO.toString()));
    }

    @Test
    @Transactional
    void getOximetriasByIdFiltering() throws Exception {
        // Initialize the database
        oximetriaRepository.saveAndFlush(oximetria);

        Long id = oximetria.getId();

        defaultOximetriaShouldBeFound("id.equals=" + id);
        defaultOximetriaShouldNotBeFound("id.notEquals=" + id);

        defaultOximetriaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOximetriaShouldNotBeFound("id.greaterThan=" + id);

        defaultOximetriaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOximetriaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOximetriasByOximetriaIsEqualToSomething() throws Exception {
        // Initialize the database
        oximetriaRepository.saveAndFlush(oximetria);

        // Get all the oximetriaList where oximetria equals to DEFAULT_OXIMETRIA
        defaultOximetriaShouldBeFound("oximetria.equals=" + DEFAULT_OXIMETRIA);

        // Get all the oximetriaList where oximetria equals to UPDATED_OXIMETRIA
        defaultOximetriaShouldNotBeFound("oximetria.equals=" + UPDATED_OXIMETRIA);
    }

    @Test
    @Transactional
    void getAllOximetriasByOximetriaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        oximetriaRepository.saveAndFlush(oximetria);

        // Get all the oximetriaList where oximetria not equals to DEFAULT_OXIMETRIA
        defaultOximetriaShouldNotBeFound("oximetria.notEquals=" + DEFAULT_OXIMETRIA);

        // Get all the oximetriaList where oximetria not equals to UPDATED_OXIMETRIA
        defaultOximetriaShouldBeFound("oximetria.notEquals=" + UPDATED_OXIMETRIA);
    }

    @Test
    @Transactional
    void getAllOximetriasByOximetriaIsInShouldWork() throws Exception {
        // Initialize the database
        oximetriaRepository.saveAndFlush(oximetria);

        // Get all the oximetriaList where oximetria in DEFAULT_OXIMETRIA or UPDATED_OXIMETRIA
        defaultOximetriaShouldBeFound("oximetria.in=" + DEFAULT_OXIMETRIA + "," + UPDATED_OXIMETRIA);

        // Get all the oximetriaList where oximetria equals to UPDATED_OXIMETRIA
        defaultOximetriaShouldNotBeFound("oximetria.in=" + UPDATED_OXIMETRIA);
    }

    @Test
    @Transactional
    void getAllOximetriasByOximetriaIsNullOrNotNull() throws Exception {
        // Initialize the database
        oximetriaRepository.saveAndFlush(oximetria);

        // Get all the oximetriaList where oximetria is not null
        defaultOximetriaShouldBeFound("oximetria.specified=true");

        // Get all the oximetriaList where oximetria is null
        defaultOximetriaShouldNotBeFound("oximetria.specified=false");
    }

    @Test
    @Transactional
    void getAllOximetriasByOximetriaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        oximetriaRepository.saveAndFlush(oximetria);

        // Get all the oximetriaList where oximetria is greater than or equal to DEFAULT_OXIMETRIA
        defaultOximetriaShouldBeFound("oximetria.greaterThanOrEqual=" + DEFAULT_OXIMETRIA);

        // Get all the oximetriaList where oximetria is greater than or equal to UPDATED_OXIMETRIA
        defaultOximetriaShouldNotBeFound("oximetria.greaterThanOrEqual=" + UPDATED_OXIMETRIA);
    }

    @Test
    @Transactional
    void getAllOximetriasByOximetriaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        oximetriaRepository.saveAndFlush(oximetria);

        // Get all the oximetriaList where oximetria is less than or equal to DEFAULT_OXIMETRIA
        defaultOximetriaShouldBeFound("oximetria.lessThanOrEqual=" + DEFAULT_OXIMETRIA);

        // Get all the oximetriaList where oximetria is less than or equal to SMALLER_OXIMETRIA
        defaultOximetriaShouldNotBeFound("oximetria.lessThanOrEqual=" + SMALLER_OXIMETRIA);
    }

    @Test
    @Transactional
    void getAllOximetriasByOximetriaIsLessThanSomething() throws Exception {
        // Initialize the database
        oximetriaRepository.saveAndFlush(oximetria);

        // Get all the oximetriaList where oximetria is less than DEFAULT_OXIMETRIA
        defaultOximetriaShouldNotBeFound("oximetria.lessThan=" + DEFAULT_OXIMETRIA);

        // Get all the oximetriaList where oximetria is less than UPDATED_OXIMETRIA
        defaultOximetriaShouldBeFound("oximetria.lessThan=" + UPDATED_OXIMETRIA);
    }

    @Test
    @Transactional
    void getAllOximetriasByOximetriaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        oximetriaRepository.saveAndFlush(oximetria);

        // Get all the oximetriaList where oximetria is greater than DEFAULT_OXIMETRIA
        defaultOximetriaShouldNotBeFound("oximetria.greaterThan=" + DEFAULT_OXIMETRIA);

        // Get all the oximetriaList where oximetria is greater than SMALLER_OXIMETRIA
        defaultOximetriaShouldBeFound("oximetria.greaterThan=" + SMALLER_OXIMETRIA);
    }

    @Test
    @Transactional
    void getAllOximetriasByFechaRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        oximetriaRepository.saveAndFlush(oximetria);

        // Get all the oximetriaList where fechaRegistro equals to DEFAULT_FECHA_REGISTRO
        defaultOximetriaShouldBeFound("fechaRegistro.equals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the oximetriaList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultOximetriaShouldNotBeFound("fechaRegistro.equals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllOximetriasByFechaRegistroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        oximetriaRepository.saveAndFlush(oximetria);

        // Get all the oximetriaList where fechaRegistro not equals to DEFAULT_FECHA_REGISTRO
        defaultOximetriaShouldNotBeFound("fechaRegistro.notEquals=" + DEFAULT_FECHA_REGISTRO);

        // Get all the oximetriaList where fechaRegistro not equals to UPDATED_FECHA_REGISTRO
        defaultOximetriaShouldBeFound("fechaRegistro.notEquals=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllOximetriasByFechaRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        oximetriaRepository.saveAndFlush(oximetria);

        // Get all the oximetriaList where fechaRegistro in DEFAULT_FECHA_REGISTRO or UPDATED_FECHA_REGISTRO
        defaultOximetriaShouldBeFound("fechaRegistro.in=" + DEFAULT_FECHA_REGISTRO + "," + UPDATED_FECHA_REGISTRO);

        // Get all the oximetriaList where fechaRegistro equals to UPDATED_FECHA_REGISTRO
        defaultOximetriaShouldNotBeFound("fechaRegistro.in=" + UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllOximetriasByFechaRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        oximetriaRepository.saveAndFlush(oximetria);

        // Get all the oximetriaList where fechaRegistro is not null
        defaultOximetriaShouldBeFound("fechaRegistro.specified=true");

        // Get all the oximetriaList where fechaRegistro is null
        defaultOximetriaShouldNotBeFound("fechaRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllOximetriasByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        oximetriaRepository.saveAndFlush(oximetria);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        oximetria.setUser(user);
        oximetriaRepository.saveAndFlush(oximetria);
        String userId = user.getId();

        // Get all the oximetriaList where user equals to userId
        defaultOximetriaShouldBeFound("userId.equals=" + userId);

        // Get all the oximetriaList where user equals to "invalid-id"
        defaultOximetriaShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOximetriaShouldBeFound(String filter) throws Exception {
        restOximetriaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oximetria.getId().intValue())))
            .andExpect(jsonPath("$.[*].oximetria").value(hasItem(DEFAULT_OXIMETRIA)))
            .andExpect(jsonPath("$.[*].fechaRegistro").value(hasItem(DEFAULT_FECHA_REGISTRO.toString())));

        // Check, that the count call also returns 1
        restOximetriaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOximetriaShouldNotBeFound(String filter) throws Exception {
        restOximetriaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOximetriaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOximetria() throws Exception {
        // Get the oximetria
        restOximetriaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOximetria() throws Exception {
        // Initialize the database
        oximetriaRepository.saveAndFlush(oximetria);

        int databaseSizeBeforeUpdate = oximetriaRepository.findAll().size();

        // Update the oximetria
        Oximetria updatedOximetria = oximetriaRepository.findById(oximetria.getId()).get();
        // Disconnect from session so that the updates on updatedOximetria are not directly saved in db
        em.detach(updatedOximetria);
        updatedOximetria.oximetria(UPDATED_OXIMETRIA).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restOximetriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOximetria.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOximetria))
            )
            .andExpect(status().isOk());

        // Validate the Oximetria in the database
        List<Oximetria> oximetriaList = oximetriaRepository.findAll();
        assertThat(oximetriaList).hasSize(databaseSizeBeforeUpdate);
        Oximetria testOximetria = oximetriaList.get(oximetriaList.size() - 1);
        assertThat(testOximetria.getOximetria()).isEqualTo(UPDATED_OXIMETRIA);
        assertThat(testOximetria.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void putNonExistingOximetria() throws Exception {
        int databaseSizeBeforeUpdate = oximetriaRepository.findAll().size();
        oximetria.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOximetriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, oximetria.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oximetria))
            )
            .andExpect(status().isBadRequest());

        // Validate the Oximetria in the database
        List<Oximetria> oximetriaList = oximetriaRepository.findAll();
        assertThat(oximetriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOximetria() throws Exception {
        int databaseSizeBeforeUpdate = oximetriaRepository.findAll().size();
        oximetria.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOximetriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oximetria))
            )
            .andExpect(status().isBadRequest());

        // Validate the Oximetria in the database
        List<Oximetria> oximetriaList = oximetriaRepository.findAll();
        assertThat(oximetriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOximetria() throws Exception {
        int databaseSizeBeforeUpdate = oximetriaRepository.findAll().size();
        oximetria.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOximetriaMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oximetria))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Oximetria in the database
        List<Oximetria> oximetriaList = oximetriaRepository.findAll();
        assertThat(oximetriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOximetriaWithPatch() throws Exception {
        // Initialize the database
        oximetriaRepository.saveAndFlush(oximetria);

        int databaseSizeBeforeUpdate = oximetriaRepository.findAll().size();

        // Update the oximetria using partial update
        Oximetria partialUpdatedOximetria = new Oximetria();
        partialUpdatedOximetria.setId(oximetria.getId());

        restOximetriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOximetria.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOximetria))
            )
            .andExpect(status().isOk());

        // Validate the Oximetria in the database
        List<Oximetria> oximetriaList = oximetriaRepository.findAll();
        assertThat(oximetriaList).hasSize(databaseSizeBeforeUpdate);
        Oximetria testOximetria = oximetriaList.get(oximetriaList.size() - 1);
        assertThat(testOximetria.getOximetria()).isEqualTo(DEFAULT_OXIMETRIA);
        assertThat(testOximetria.getFechaRegistro()).isEqualTo(DEFAULT_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void fullUpdateOximetriaWithPatch() throws Exception {
        // Initialize the database
        oximetriaRepository.saveAndFlush(oximetria);

        int databaseSizeBeforeUpdate = oximetriaRepository.findAll().size();

        // Update the oximetria using partial update
        Oximetria partialUpdatedOximetria = new Oximetria();
        partialUpdatedOximetria.setId(oximetria.getId());

        partialUpdatedOximetria.oximetria(UPDATED_OXIMETRIA).fechaRegistro(UPDATED_FECHA_REGISTRO);

        restOximetriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOximetria.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOximetria))
            )
            .andExpect(status().isOk());

        // Validate the Oximetria in the database
        List<Oximetria> oximetriaList = oximetriaRepository.findAll();
        assertThat(oximetriaList).hasSize(databaseSizeBeforeUpdate);
        Oximetria testOximetria = oximetriaList.get(oximetriaList.size() - 1);
        assertThat(testOximetria.getOximetria()).isEqualTo(UPDATED_OXIMETRIA);
        assertThat(testOximetria.getFechaRegistro()).isEqualTo(UPDATED_FECHA_REGISTRO);
    }

    @Test
    @Transactional
    void patchNonExistingOximetria() throws Exception {
        int databaseSizeBeforeUpdate = oximetriaRepository.findAll().size();
        oximetria.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOximetriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, oximetria.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oximetria))
            )
            .andExpect(status().isBadRequest());

        // Validate the Oximetria in the database
        List<Oximetria> oximetriaList = oximetriaRepository.findAll();
        assertThat(oximetriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOximetria() throws Exception {
        int databaseSizeBeforeUpdate = oximetriaRepository.findAll().size();
        oximetria.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOximetriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oximetria))
            )
            .andExpect(status().isBadRequest());

        // Validate the Oximetria in the database
        List<Oximetria> oximetriaList = oximetriaRepository.findAll();
        assertThat(oximetriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOximetria() throws Exception {
        int databaseSizeBeforeUpdate = oximetriaRepository.findAll().size();
        oximetria.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOximetriaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oximetria))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Oximetria in the database
        List<Oximetria> oximetriaList = oximetriaRepository.findAll();
        assertThat(oximetriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOximetria() throws Exception {
        // Initialize the database
        oximetriaRepository.saveAndFlush(oximetria);

        int databaseSizeBeforeDelete = oximetriaRepository.findAll().size();

        // Delete the oximetria
        restOximetriaMockMvc
            .perform(delete(ENTITY_API_URL_ID, oximetria.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Oximetria> oximetriaList = oximetriaRepository.findAll();
        assertThat(oximetriaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.be4tech.becare3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.Condicion;
import com.be4tech.becare3.repository.CondicionRepository;
import com.be4tech.becare3.service.criteria.CondicionCriteria;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link CondicionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CondicionResourceIT {

    private static final String DEFAULT_CONDICION = "AAAAAAAAAA";
    private static final String UPDATED_CONDICION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/condicions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CondicionRepository condicionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCondicionMockMvc;

    private Condicion condicion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Condicion createEntity(EntityManager em) {
        Condicion condicion = new Condicion().condicion(DEFAULT_CONDICION).descripcion(DEFAULT_DESCRIPCION);
        return condicion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Condicion createUpdatedEntity(EntityManager em) {
        Condicion condicion = new Condicion().condicion(UPDATED_CONDICION).descripcion(UPDATED_DESCRIPCION);
        return condicion;
    }

    @BeforeEach
    public void initTest() {
        condicion = createEntity(em);
    }

    @Test
    @Transactional
    void createCondicion() throws Exception {
        int databaseSizeBeforeCreate = condicionRepository.findAll().size();
        // Create the Condicion
        restCondicionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(condicion))
            )
            .andExpect(status().isCreated());

        // Validate the Condicion in the database
        List<Condicion> condicionList = condicionRepository.findAll();
        assertThat(condicionList).hasSize(databaseSizeBeforeCreate + 1);
        Condicion testCondicion = condicionList.get(condicionList.size() - 1);
        assertThat(testCondicion.getCondicion()).isEqualTo(DEFAULT_CONDICION);
        assertThat(testCondicion.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void createCondicionWithExistingId() throws Exception {
        // Create the Condicion with an existing ID
        condicion.setId(1L);

        int databaseSizeBeforeCreate = condicionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCondicionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(condicion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Condicion in the database
        List<Condicion> condicionList = condicionRepository.findAll();
        assertThat(condicionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCondicions() throws Exception {
        // Initialize the database
        condicionRepository.saveAndFlush(condicion);

        // Get all the condicionList
        restCondicionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(condicion.getId().intValue())))
            .andExpect(jsonPath("$.[*].condicion").value(hasItem(DEFAULT_CONDICION)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }

    @Test
    @Transactional
    void getCondicion() throws Exception {
        // Initialize the database
        condicionRepository.saveAndFlush(condicion);

        // Get the condicion
        restCondicionMockMvc
            .perform(get(ENTITY_API_URL_ID, condicion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(condicion.getId().intValue()))
            .andExpect(jsonPath("$.condicion").value(DEFAULT_CONDICION))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    @Transactional
    void getCondicionsByIdFiltering() throws Exception {
        // Initialize the database
        condicionRepository.saveAndFlush(condicion);

        Long id = condicion.getId();

        defaultCondicionShouldBeFound("id.equals=" + id);
        defaultCondicionShouldNotBeFound("id.notEquals=" + id);

        defaultCondicionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCondicionShouldNotBeFound("id.greaterThan=" + id);

        defaultCondicionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCondicionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCondicionsByCondicionIsEqualToSomething() throws Exception {
        // Initialize the database
        condicionRepository.saveAndFlush(condicion);

        // Get all the condicionList where condicion equals to DEFAULT_CONDICION
        defaultCondicionShouldBeFound("condicion.equals=" + DEFAULT_CONDICION);

        // Get all the condicionList where condicion equals to UPDATED_CONDICION
        defaultCondicionShouldNotBeFound("condicion.equals=" + UPDATED_CONDICION);
    }

    @Test
    @Transactional
    void getAllCondicionsByCondicionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        condicionRepository.saveAndFlush(condicion);

        // Get all the condicionList where condicion not equals to DEFAULT_CONDICION
        defaultCondicionShouldNotBeFound("condicion.notEquals=" + DEFAULT_CONDICION);

        // Get all the condicionList where condicion not equals to UPDATED_CONDICION
        defaultCondicionShouldBeFound("condicion.notEquals=" + UPDATED_CONDICION);
    }

    @Test
    @Transactional
    void getAllCondicionsByCondicionIsInShouldWork() throws Exception {
        // Initialize the database
        condicionRepository.saveAndFlush(condicion);

        // Get all the condicionList where condicion in DEFAULT_CONDICION or UPDATED_CONDICION
        defaultCondicionShouldBeFound("condicion.in=" + DEFAULT_CONDICION + "," + UPDATED_CONDICION);

        // Get all the condicionList where condicion equals to UPDATED_CONDICION
        defaultCondicionShouldNotBeFound("condicion.in=" + UPDATED_CONDICION);
    }

    @Test
    @Transactional
    void getAllCondicionsByCondicionIsNullOrNotNull() throws Exception {
        // Initialize the database
        condicionRepository.saveAndFlush(condicion);

        // Get all the condicionList where condicion is not null
        defaultCondicionShouldBeFound("condicion.specified=true");

        // Get all the condicionList where condicion is null
        defaultCondicionShouldNotBeFound("condicion.specified=false");
    }

    @Test
    @Transactional
    void getAllCondicionsByCondicionContainsSomething() throws Exception {
        // Initialize the database
        condicionRepository.saveAndFlush(condicion);

        // Get all the condicionList where condicion contains DEFAULT_CONDICION
        defaultCondicionShouldBeFound("condicion.contains=" + DEFAULT_CONDICION);

        // Get all the condicionList where condicion contains UPDATED_CONDICION
        defaultCondicionShouldNotBeFound("condicion.contains=" + UPDATED_CONDICION);
    }

    @Test
    @Transactional
    void getAllCondicionsByCondicionNotContainsSomething() throws Exception {
        // Initialize the database
        condicionRepository.saveAndFlush(condicion);

        // Get all the condicionList where condicion does not contain DEFAULT_CONDICION
        defaultCondicionShouldNotBeFound("condicion.doesNotContain=" + DEFAULT_CONDICION);

        // Get all the condicionList where condicion does not contain UPDATED_CONDICION
        defaultCondicionShouldBeFound("condicion.doesNotContain=" + UPDATED_CONDICION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCondicionShouldBeFound(String filter) throws Exception {
        restCondicionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(condicion.getId().intValue())))
            .andExpect(jsonPath("$.[*].condicion").value(hasItem(DEFAULT_CONDICION)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));

        // Check, that the count call also returns 1
        restCondicionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCondicionShouldNotBeFound(String filter) throws Exception {
        restCondicionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCondicionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCondicion() throws Exception {
        // Get the condicion
        restCondicionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCondicion() throws Exception {
        // Initialize the database
        condicionRepository.saveAndFlush(condicion);

        int databaseSizeBeforeUpdate = condicionRepository.findAll().size();

        // Update the condicion
        Condicion updatedCondicion = condicionRepository.findById(condicion.getId()).get();
        // Disconnect from session so that the updates on updatedCondicion are not directly saved in db
        em.detach(updatedCondicion);
        updatedCondicion.condicion(UPDATED_CONDICION).descripcion(UPDATED_DESCRIPCION);

        restCondicionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCondicion.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCondicion))
            )
            .andExpect(status().isOk());

        // Validate the Condicion in the database
        List<Condicion> condicionList = condicionRepository.findAll();
        assertThat(condicionList).hasSize(databaseSizeBeforeUpdate);
        Condicion testCondicion = condicionList.get(condicionList.size() - 1);
        assertThat(testCondicion.getCondicion()).isEqualTo(UPDATED_CONDICION);
        assertThat(testCondicion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void putNonExistingCondicion() throws Exception {
        int databaseSizeBeforeUpdate = condicionRepository.findAll().size();
        condicion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCondicionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, condicion.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(condicion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Condicion in the database
        List<Condicion> condicionList = condicionRepository.findAll();
        assertThat(condicionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCondicion() throws Exception {
        int databaseSizeBeforeUpdate = condicionRepository.findAll().size();
        condicion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCondicionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(condicion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Condicion in the database
        List<Condicion> condicionList = condicionRepository.findAll();
        assertThat(condicionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCondicion() throws Exception {
        int databaseSizeBeforeUpdate = condicionRepository.findAll().size();
        condicion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCondicionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(condicion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Condicion in the database
        List<Condicion> condicionList = condicionRepository.findAll();
        assertThat(condicionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCondicionWithPatch() throws Exception {
        // Initialize the database
        condicionRepository.saveAndFlush(condicion);

        int databaseSizeBeforeUpdate = condicionRepository.findAll().size();

        // Update the condicion using partial update
        Condicion partialUpdatedCondicion = new Condicion();
        partialUpdatedCondicion.setId(condicion.getId());

        partialUpdatedCondicion.condicion(UPDATED_CONDICION);

        restCondicionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCondicion.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCondicion))
            )
            .andExpect(status().isOk());

        // Validate the Condicion in the database
        List<Condicion> condicionList = condicionRepository.findAll();
        assertThat(condicionList).hasSize(databaseSizeBeforeUpdate);
        Condicion testCondicion = condicionList.get(condicionList.size() - 1);
        assertThat(testCondicion.getCondicion()).isEqualTo(UPDATED_CONDICION);
        assertThat(testCondicion.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void fullUpdateCondicionWithPatch() throws Exception {
        // Initialize the database
        condicionRepository.saveAndFlush(condicion);

        int databaseSizeBeforeUpdate = condicionRepository.findAll().size();

        // Update the condicion using partial update
        Condicion partialUpdatedCondicion = new Condicion();
        partialUpdatedCondicion.setId(condicion.getId());

        partialUpdatedCondicion.condicion(UPDATED_CONDICION).descripcion(UPDATED_DESCRIPCION);

        restCondicionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCondicion.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCondicion))
            )
            .andExpect(status().isOk());

        // Validate the Condicion in the database
        List<Condicion> condicionList = condicionRepository.findAll();
        assertThat(condicionList).hasSize(databaseSizeBeforeUpdate);
        Condicion testCondicion = condicionList.get(condicionList.size() - 1);
        assertThat(testCondicion.getCondicion()).isEqualTo(UPDATED_CONDICION);
        assertThat(testCondicion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void patchNonExistingCondicion() throws Exception {
        int databaseSizeBeforeUpdate = condicionRepository.findAll().size();
        condicion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCondicionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, condicion.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(condicion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Condicion in the database
        List<Condicion> condicionList = condicionRepository.findAll();
        assertThat(condicionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCondicion() throws Exception {
        int databaseSizeBeforeUpdate = condicionRepository.findAll().size();
        condicion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCondicionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(condicion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Condicion in the database
        List<Condicion> condicionList = condicionRepository.findAll();
        assertThat(condicionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCondicion() throws Exception {
        int databaseSizeBeforeUpdate = condicionRepository.findAll().size();
        condicion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCondicionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(condicion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Condicion in the database
        List<Condicion> condicionList = condicionRepository.findAll();
        assertThat(condicionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCondicion() throws Exception {
        // Initialize the database
        condicionRepository.saveAndFlush(condicion);

        int databaseSizeBeforeDelete = condicionRepository.findAll().size();

        // Delete the condicion
        restCondicionMockMvc
            .perform(delete(ENTITY_API_URL_ID, condicion.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Condicion> condicionList = condicionRepository.findAll();
        assertThat(condicionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

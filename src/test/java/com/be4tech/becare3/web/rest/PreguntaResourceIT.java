package com.be4tech.becare3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.Condicion;
import com.be4tech.becare3.domain.Pregunta;
import com.be4tech.becare3.repository.PreguntaRepository;
import com.be4tech.becare3.service.criteria.PreguntaCriteria;
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
 * Integration tests for the {@link PreguntaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PreguntaResourceIT {

    private static final String DEFAULT_PREGUNTA = "AAAAAAAAAA";
    private static final String UPDATED_PREGUNTA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/preguntas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPreguntaMockMvc;

    private Pregunta pregunta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pregunta createEntity(EntityManager em) {
        Pregunta pregunta = new Pregunta().pregunta(DEFAULT_PREGUNTA);
        return pregunta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pregunta createUpdatedEntity(EntityManager em) {
        Pregunta pregunta = new Pregunta().pregunta(UPDATED_PREGUNTA);
        return pregunta;
    }

    @BeforeEach
    public void initTest() {
        pregunta = createEntity(em);
    }

    @Test
    @Transactional
    void createPregunta() throws Exception {
        int databaseSizeBeforeCreate = preguntaRepository.findAll().size();
        // Create the Pregunta
        restPreguntaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pregunta))
            )
            .andExpect(status().isCreated());

        // Validate the Pregunta in the database
        List<Pregunta> preguntaList = preguntaRepository.findAll();
        assertThat(preguntaList).hasSize(databaseSizeBeforeCreate + 1);
        Pregunta testPregunta = preguntaList.get(preguntaList.size() - 1);
        assertThat(testPregunta.getPregunta()).isEqualTo(DEFAULT_PREGUNTA);
    }

    @Test
    @Transactional
    void createPreguntaWithExistingId() throws Exception {
        // Create the Pregunta with an existing ID
        pregunta.setId(1L);

        int databaseSizeBeforeCreate = preguntaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPreguntaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pregunta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pregunta in the database
        List<Pregunta> preguntaList = preguntaRepository.findAll();
        assertThat(preguntaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPreguntas() throws Exception {
        // Initialize the database
        preguntaRepository.saveAndFlush(pregunta);

        // Get all the preguntaList
        restPreguntaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pregunta.getId().intValue())))
            .andExpect(jsonPath("$.[*].pregunta").value(hasItem(DEFAULT_PREGUNTA)));
    }

    @Test
    @Transactional
    void getPregunta() throws Exception {
        // Initialize the database
        preguntaRepository.saveAndFlush(pregunta);

        // Get the pregunta
        restPreguntaMockMvc
            .perform(get(ENTITY_API_URL_ID, pregunta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pregunta.getId().intValue()))
            .andExpect(jsonPath("$.pregunta").value(DEFAULT_PREGUNTA));
    }

    @Test
    @Transactional
    void getPreguntasByIdFiltering() throws Exception {
        // Initialize the database
        preguntaRepository.saveAndFlush(pregunta);

        Long id = pregunta.getId();

        defaultPreguntaShouldBeFound("id.equals=" + id);
        defaultPreguntaShouldNotBeFound("id.notEquals=" + id);

        defaultPreguntaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPreguntaShouldNotBeFound("id.greaterThan=" + id);

        defaultPreguntaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPreguntaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPreguntasByPreguntaIsEqualToSomething() throws Exception {
        // Initialize the database
        preguntaRepository.saveAndFlush(pregunta);

        // Get all the preguntaList where pregunta equals to DEFAULT_PREGUNTA
        defaultPreguntaShouldBeFound("pregunta.equals=" + DEFAULT_PREGUNTA);

        // Get all the preguntaList where pregunta equals to UPDATED_PREGUNTA
        defaultPreguntaShouldNotBeFound("pregunta.equals=" + UPDATED_PREGUNTA);
    }

    @Test
    @Transactional
    void getAllPreguntasByPreguntaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        preguntaRepository.saveAndFlush(pregunta);

        // Get all the preguntaList where pregunta not equals to DEFAULT_PREGUNTA
        defaultPreguntaShouldNotBeFound("pregunta.notEquals=" + DEFAULT_PREGUNTA);

        // Get all the preguntaList where pregunta not equals to UPDATED_PREGUNTA
        defaultPreguntaShouldBeFound("pregunta.notEquals=" + UPDATED_PREGUNTA);
    }

    @Test
    @Transactional
    void getAllPreguntasByPreguntaIsInShouldWork() throws Exception {
        // Initialize the database
        preguntaRepository.saveAndFlush(pregunta);

        // Get all the preguntaList where pregunta in DEFAULT_PREGUNTA or UPDATED_PREGUNTA
        defaultPreguntaShouldBeFound("pregunta.in=" + DEFAULT_PREGUNTA + "," + UPDATED_PREGUNTA);

        // Get all the preguntaList where pregunta equals to UPDATED_PREGUNTA
        defaultPreguntaShouldNotBeFound("pregunta.in=" + UPDATED_PREGUNTA);
    }

    @Test
    @Transactional
    void getAllPreguntasByPreguntaIsNullOrNotNull() throws Exception {
        // Initialize the database
        preguntaRepository.saveAndFlush(pregunta);

        // Get all the preguntaList where pregunta is not null
        defaultPreguntaShouldBeFound("pregunta.specified=true");

        // Get all the preguntaList where pregunta is null
        defaultPreguntaShouldNotBeFound("pregunta.specified=false");
    }

    @Test
    @Transactional
    void getAllPreguntasByPreguntaContainsSomething() throws Exception {
        // Initialize the database
        preguntaRepository.saveAndFlush(pregunta);

        // Get all the preguntaList where pregunta contains DEFAULT_PREGUNTA
        defaultPreguntaShouldBeFound("pregunta.contains=" + DEFAULT_PREGUNTA);

        // Get all the preguntaList where pregunta contains UPDATED_PREGUNTA
        defaultPreguntaShouldNotBeFound("pregunta.contains=" + UPDATED_PREGUNTA);
    }

    @Test
    @Transactional
    void getAllPreguntasByPreguntaNotContainsSomething() throws Exception {
        // Initialize the database
        preguntaRepository.saveAndFlush(pregunta);

        // Get all the preguntaList where pregunta does not contain DEFAULT_PREGUNTA
        defaultPreguntaShouldNotBeFound("pregunta.doesNotContain=" + DEFAULT_PREGUNTA);

        // Get all the preguntaList where pregunta does not contain UPDATED_PREGUNTA
        defaultPreguntaShouldBeFound("pregunta.doesNotContain=" + UPDATED_PREGUNTA);
    }

    @Test
    @Transactional
    void getAllPreguntasByCondicionIsEqualToSomething() throws Exception {
        // Initialize the database
        preguntaRepository.saveAndFlush(pregunta);
        Condicion condicion = CondicionResourceIT.createEntity(em);
        em.persist(condicion);
        em.flush();
        pregunta.setCondicion(condicion);
        preguntaRepository.saveAndFlush(pregunta);
        Long condicionId = condicion.getId();

        // Get all the preguntaList where condicion equals to condicionId
        defaultPreguntaShouldBeFound("condicionId.equals=" + condicionId);

        // Get all the preguntaList where condicion equals to (condicionId + 1)
        defaultPreguntaShouldNotBeFound("condicionId.equals=" + (condicionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPreguntaShouldBeFound(String filter) throws Exception {
        restPreguntaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pregunta.getId().intValue())))
            .andExpect(jsonPath("$.[*].pregunta").value(hasItem(DEFAULT_PREGUNTA)));

        // Check, that the count call also returns 1
        restPreguntaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPreguntaShouldNotBeFound(String filter) throws Exception {
        restPreguntaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPreguntaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPregunta() throws Exception {
        // Get the pregunta
        restPreguntaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPregunta() throws Exception {
        // Initialize the database
        preguntaRepository.saveAndFlush(pregunta);

        int databaseSizeBeforeUpdate = preguntaRepository.findAll().size();

        // Update the pregunta
        Pregunta updatedPregunta = preguntaRepository.findById(pregunta.getId()).get();
        // Disconnect from session so that the updates on updatedPregunta are not directly saved in db
        em.detach(updatedPregunta);
        updatedPregunta.pregunta(UPDATED_PREGUNTA);

        restPreguntaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPregunta.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPregunta))
            )
            .andExpect(status().isOk());

        // Validate the Pregunta in the database
        List<Pregunta> preguntaList = preguntaRepository.findAll();
        assertThat(preguntaList).hasSize(databaseSizeBeforeUpdate);
        Pregunta testPregunta = preguntaList.get(preguntaList.size() - 1);
        assertThat(testPregunta.getPregunta()).isEqualTo(UPDATED_PREGUNTA);
    }

    @Test
    @Transactional
    void putNonExistingPregunta() throws Exception {
        int databaseSizeBeforeUpdate = preguntaRepository.findAll().size();
        pregunta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPreguntaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pregunta.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pregunta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pregunta in the database
        List<Pregunta> preguntaList = preguntaRepository.findAll();
        assertThat(preguntaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPregunta() throws Exception {
        int databaseSizeBeforeUpdate = preguntaRepository.findAll().size();
        pregunta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPreguntaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pregunta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pregunta in the database
        List<Pregunta> preguntaList = preguntaRepository.findAll();
        assertThat(preguntaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPregunta() throws Exception {
        int databaseSizeBeforeUpdate = preguntaRepository.findAll().size();
        pregunta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPreguntaMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pregunta))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pregunta in the database
        List<Pregunta> preguntaList = preguntaRepository.findAll();
        assertThat(preguntaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePreguntaWithPatch() throws Exception {
        // Initialize the database
        preguntaRepository.saveAndFlush(pregunta);

        int databaseSizeBeforeUpdate = preguntaRepository.findAll().size();

        // Update the pregunta using partial update
        Pregunta partialUpdatedPregunta = new Pregunta();
        partialUpdatedPregunta.setId(pregunta.getId());

        restPreguntaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPregunta.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPregunta))
            )
            .andExpect(status().isOk());

        // Validate the Pregunta in the database
        List<Pregunta> preguntaList = preguntaRepository.findAll();
        assertThat(preguntaList).hasSize(databaseSizeBeforeUpdate);
        Pregunta testPregunta = preguntaList.get(preguntaList.size() - 1);
        assertThat(testPregunta.getPregunta()).isEqualTo(DEFAULT_PREGUNTA);
    }

    @Test
    @Transactional
    void fullUpdatePreguntaWithPatch() throws Exception {
        // Initialize the database
        preguntaRepository.saveAndFlush(pregunta);

        int databaseSizeBeforeUpdate = preguntaRepository.findAll().size();

        // Update the pregunta using partial update
        Pregunta partialUpdatedPregunta = new Pregunta();
        partialUpdatedPregunta.setId(pregunta.getId());

        partialUpdatedPregunta.pregunta(UPDATED_PREGUNTA);

        restPreguntaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPregunta.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPregunta))
            )
            .andExpect(status().isOk());

        // Validate the Pregunta in the database
        List<Pregunta> preguntaList = preguntaRepository.findAll();
        assertThat(preguntaList).hasSize(databaseSizeBeforeUpdate);
        Pregunta testPregunta = preguntaList.get(preguntaList.size() - 1);
        assertThat(testPregunta.getPregunta()).isEqualTo(UPDATED_PREGUNTA);
    }

    @Test
    @Transactional
    void patchNonExistingPregunta() throws Exception {
        int databaseSizeBeforeUpdate = preguntaRepository.findAll().size();
        pregunta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPreguntaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pregunta.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pregunta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pregunta in the database
        List<Pregunta> preguntaList = preguntaRepository.findAll();
        assertThat(preguntaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPregunta() throws Exception {
        int databaseSizeBeforeUpdate = preguntaRepository.findAll().size();
        pregunta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPreguntaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pregunta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pregunta in the database
        List<Pregunta> preguntaList = preguntaRepository.findAll();
        assertThat(preguntaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPregunta() throws Exception {
        int databaseSizeBeforeUpdate = preguntaRepository.findAll().size();
        pregunta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPreguntaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pregunta))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pregunta in the database
        List<Pregunta> preguntaList = preguntaRepository.findAll();
        assertThat(preguntaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePregunta() throws Exception {
        // Initialize the database
        preguntaRepository.saveAndFlush(pregunta);

        int databaseSizeBeforeDelete = preguntaRepository.findAll().size();

        // Delete the pregunta
        restPreguntaMockMvc
            .perform(delete(ENTITY_API_URL_ID, pregunta.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pregunta> preguntaList = preguntaRepository.findAll();
        assertThat(preguntaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

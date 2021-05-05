package com.be4tech.becare3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.CuestionarioEstado;
import com.be4tech.becare3.domain.Pregunta;
import com.be4tech.becare3.domain.User;
import com.be4tech.becare3.repository.CuestionarioEstadoRepository;
import com.be4tech.becare3.service.criteria.CuestionarioEstadoCriteria;
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
 * Integration tests for the {@link CuestionarioEstadoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CuestionarioEstadoResourceIT {

    private static final Integer DEFAULT_VALOR = 1;
    private static final Integer UPDATED_VALOR = 2;
    private static final Integer SMALLER_VALOR = 1 - 1;

    private static final String DEFAULT_VALORACION = "AAAAAAAAAA";
    private static final String UPDATED_VALORACION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cuestionario-estados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CuestionarioEstadoRepository cuestionarioEstadoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCuestionarioEstadoMockMvc;

    private CuestionarioEstado cuestionarioEstado;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CuestionarioEstado createEntity(EntityManager em) {
        CuestionarioEstado cuestionarioEstado = new CuestionarioEstado().valor(DEFAULT_VALOR).valoracion(DEFAULT_VALORACION);
        return cuestionarioEstado;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CuestionarioEstado createUpdatedEntity(EntityManager em) {
        CuestionarioEstado cuestionarioEstado = new CuestionarioEstado().valor(UPDATED_VALOR).valoracion(UPDATED_VALORACION);
        return cuestionarioEstado;
    }

    @BeforeEach
    public void initTest() {
        cuestionarioEstado = createEntity(em);
    }

    @Test
    @Transactional
    void createCuestionarioEstado() throws Exception {
        int databaseSizeBeforeCreate = cuestionarioEstadoRepository.findAll().size();
        // Create the CuestionarioEstado
        restCuestionarioEstadoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cuestionarioEstado))
            )
            .andExpect(status().isCreated());

        // Validate the CuestionarioEstado in the database
        List<CuestionarioEstado> cuestionarioEstadoList = cuestionarioEstadoRepository.findAll();
        assertThat(cuestionarioEstadoList).hasSize(databaseSizeBeforeCreate + 1);
        CuestionarioEstado testCuestionarioEstado = cuestionarioEstadoList.get(cuestionarioEstadoList.size() - 1);
        assertThat(testCuestionarioEstado.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testCuestionarioEstado.getValoracion()).isEqualTo(DEFAULT_VALORACION);
    }

    @Test
    @Transactional
    void createCuestionarioEstadoWithExistingId() throws Exception {
        // Create the CuestionarioEstado with an existing ID
        cuestionarioEstado.setId(1L);

        int databaseSizeBeforeCreate = cuestionarioEstadoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCuestionarioEstadoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cuestionarioEstado))
            )
            .andExpect(status().isBadRequest());

        // Validate the CuestionarioEstado in the database
        List<CuestionarioEstado> cuestionarioEstadoList = cuestionarioEstadoRepository.findAll();
        assertThat(cuestionarioEstadoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCuestionarioEstados() throws Exception {
        // Initialize the database
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);

        // Get all the cuestionarioEstadoList
        restCuestionarioEstadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cuestionarioEstado.getId().intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.[*].valoracion").value(hasItem(DEFAULT_VALORACION)));
    }

    @Test
    @Transactional
    void getCuestionarioEstado() throws Exception {
        // Initialize the database
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);

        // Get the cuestionarioEstado
        restCuestionarioEstadoMockMvc
            .perform(get(ENTITY_API_URL_ID, cuestionarioEstado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cuestionarioEstado.getId().intValue()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR))
            .andExpect(jsonPath("$.valoracion").value(DEFAULT_VALORACION));
    }

    @Test
    @Transactional
    void getCuestionarioEstadosByIdFiltering() throws Exception {
        // Initialize the database
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);

        Long id = cuestionarioEstado.getId();

        defaultCuestionarioEstadoShouldBeFound("id.equals=" + id);
        defaultCuestionarioEstadoShouldNotBeFound("id.notEquals=" + id);

        defaultCuestionarioEstadoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCuestionarioEstadoShouldNotBeFound("id.greaterThan=" + id);

        defaultCuestionarioEstadoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCuestionarioEstadoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCuestionarioEstadosByValorIsEqualToSomething() throws Exception {
        // Initialize the database
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);

        // Get all the cuestionarioEstadoList where valor equals to DEFAULT_VALOR
        defaultCuestionarioEstadoShouldBeFound("valor.equals=" + DEFAULT_VALOR);

        // Get all the cuestionarioEstadoList where valor equals to UPDATED_VALOR
        defaultCuestionarioEstadoShouldNotBeFound("valor.equals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllCuestionarioEstadosByValorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);

        // Get all the cuestionarioEstadoList where valor not equals to DEFAULT_VALOR
        defaultCuestionarioEstadoShouldNotBeFound("valor.notEquals=" + DEFAULT_VALOR);

        // Get all the cuestionarioEstadoList where valor not equals to UPDATED_VALOR
        defaultCuestionarioEstadoShouldBeFound("valor.notEquals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllCuestionarioEstadosByValorIsInShouldWork() throws Exception {
        // Initialize the database
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);

        // Get all the cuestionarioEstadoList where valor in DEFAULT_VALOR or UPDATED_VALOR
        defaultCuestionarioEstadoShouldBeFound("valor.in=" + DEFAULT_VALOR + "," + UPDATED_VALOR);

        // Get all the cuestionarioEstadoList where valor equals to UPDATED_VALOR
        defaultCuestionarioEstadoShouldNotBeFound("valor.in=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllCuestionarioEstadosByValorIsNullOrNotNull() throws Exception {
        // Initialize the database
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);

        // Get all the cuestionarioEstadoList where valor is not null
        defaultCuestionarioEstadoShouldBeFound("valor.specified=true");

        // Get all the cuestionarioEstadoList where valor is null
        defaultCuestionarioEstadoShouldNotBeFound("valor.specified=false");
    }

    @Test
    @Transactional
    void getAllCuestionarioEstadosByValorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);

        // Get all the cuestionarioEstadoList where valor is greater than or equal to DEFAULT_VALOR
        defaultCuestionarioEstadoShouldBeFound("valor.greaterThanOrEqual=" + DEFAULT_VALOR);

        // Get all the cuestionarioEstadoList where valor is greater than or equal to UPDATED_VALOR
        defaultCuestionarioEstadoShouldNotBeFound("valor.greaterThanOrEqual=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllCuestionarioEstadosByValorIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);

        // Get all the cuestionarioEstadoList where valor is less than or equal to DEFAULT_VALOR
        defaultCuestionarioEstadoShouldBeFound("valor.lessThanOrEqual=" + DEFAULT_VALOR);

        // Get all the cuestionarioEstadoList where valor is less than or equal to SMALLER_VALOR
        defaultCuestionarioEstadoShouldNotBeFound("valor.lessThanOrEqual=" + SMALLER_VALOR);
    }

    @Test
    @Transactional
    void getAllCuestionarioEstadosByValorIsLessThanSomething() throws Exception {
        // Initialize the database
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);

        // Get all the cuestionarioEstadoList where valor is less than DEFAULT_VALOR
        defaultCuestionarioEstadoShouldNotBeFound("valor.lessThan=" + DEFAULT_VALOR);

        // Get all the cuestionarioEstadoList where valor is less than UPDATED_VALOR
        defaultCuestionarioEstadoShouldBeFound("valor.lessThan=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllCuestionarioEstadosByValorIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);

        // Get all the cuestionarioEstadoList where valor is greater than DEFAULT_VALOR
        defaultCuestionarioEstadoShouldNotBeFound("valor.greaterThan=" + DEFAULT_VALOR);

        // Get all the cuestionarioEstadoList where valor is greater than SMALLER_VALOR
        defaultCuestionarioEstadoShouldBeFound("valor.greaterThan=" + SMALLER_VALOR);
    }

    @Test
    @Transactional
    void getAllCuestionarioEstadosByValoracionIsEqualToSomething() throws Exception {
        // Initialize the database
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);

        // Get all the cuestionarioEstadoList where valoracion equals to DEFAULT_VALORACION
        defaultCuestionarioEstadoShouldBeFound("valoracion.equals=" + DEFAULT_VALORACION);

        // Get all the cuestionarioEstadoList where valoracion equals to UPDATED_VALORACION
        defaultCuestionarioEstadoShouldNotBeFound("valoracion.equals=" + UPDATED_VALORACION);
    }

    @Test
    @Transactional
    void getAllCuestionarioEstadosByValoracionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);

        // Get all the cuestionarioEstadoList where valoracion not equals to DEFAULT_VALORACION
        defaultCuestionarioEstadoShouldNotBeFound("valoracion.notEquals=" + DEFAULT_VALORACION);

        // Get all the cuestionarioEstadoList where valoracion not equals to UPDATED_VALORACION
        defaultCuestionarioEstadoShouldBeFound("valoracion.notEquals=" + UPDATED_VALORACION);
    }

    @Test
    @Transactional
    void getAllCuestionarioEstadosByValoracionIsInShouldWork() throws Exception {
        // Initialize the database
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);

        // Get all the cuestionarioEstadoList where valoracion in DEFAULT_VALORACION or UPDATED_VALORACION
        defaultCuestionarioEstadoShouldBeFound("valoracion.in=" + DEFAULT_VALORACION + "," + UPDATED_VALORACION);

        // Get all the cuestionarioEstadoList where valoracion equals to UPDATED_VALORACION
        defaultCuestionarioEstadoShouldNotBeFound("valoracion.in=" + UPDATED_VALORACION);
    }

    @Test
    @Transactional
    void getAllCuestionarioEstadosByValoracionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);

        // Get all the cuestionarioEstadoList where valoracion is not null
        defaultCuestionarioEstadoShouldBeFound("valoracion.specified=true");

        // Get all the cuestionarioEstadoList where valoracion is null
        defaultCuestionarioEstadoShouldNotBeFound("valoracion.specified=false");
    }

    @Test
    @Transactional
    void getAllCuestionarioEstadosByValoracionContainsSomething() throws Exception {
        // Initialize the database
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);

        // Get all the cuestionarioEstadoList where valoracion contains DEFAULT_VALORACION
        defaultCuestionarioEstadoShouldBeFound("valoracion.contains=" + DEFAULT_VALORACION);

        // Get all the cuestionarioEstadoList where valoracion contains UPDATED_VALORACION
        defaultCuestionarioEstadoShouldNotBeFound("valoracion.contains=" + UPDATED_VALORACION);
    }

    @Test
    @Transactional
    void getAllCuestionarioEstadosByValoracionNotContainsSomething() throws Exception {
        // Initialize the database
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);

        // Get all the cuestionarioEstadoList where valoracion does not contain DEFAULT_VALORACION
        defaultCuestionarioEstadoShouldNotBeFound("valoracion.doesNotContain=" + DEFAULT_VALORACION);

        // Get all the cuestionarioEstadoList where valoracion does not contain UPDATED_VALORACION
        defaultCuestionarioEstadoShouldBeFound("valoracion.doesNotContain=" + UPDATED_VALORACION);
    }

    @Test
    @Transactional
    void getAllCuestionarioEstadosByPreguntaIsEqualToSomething() throws Exception {
        // Initialize the database
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);
        Pregunta pregunta = PreguntaResourceIT.createEntity(em);
        em.persist(pregunta);
        em.flush();
        cuestionarioEstado.setPregunta(pregunta);
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);
        Long preguntaId = pregunta.getId();

        // Get all the cuestionarioEstadoList where pregunta equals to preguntaId
        defaultCuestionarioEstadoShouldBeFound("preguntaId.equals=" + preguntaId);

        // Get all the cuestionarioEstadoList where pregunta equals to (preguntaId + 1)
        defaultCuestionarioEstadoShouldNotBeFound("preguntaId.equals=" + (preguntaId + 1));
    }

    @Test
    @Transactional
    void getAllCuestionarioEstadosByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        cuestionarioEstado.setUser(user);
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);
        String userId = user.getId();

        // Get all the cuestionarioEstadoList where user equals to userId
        defaultCuestionarioEstadoShouldBeFound("userId.equals=" + userId);

        // Get all the cuestionarioEstadoList where user equals to "invalid-id"
        defaultCuestionarioEstadoShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCuestionarioEstadoShouldBeFound(String filter) throws Exception {
        restCuestionarioEstadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cuestionarioEstado.getId().intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.[*].valoracion").value(hasItem(DEFAULT_VALORACION)));

        // Check, that the count call also returns 1
        restCuestionarioEstadoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCuestionarioEstadoShouldNotBeFound(String filter) throws Exception {
        restCuestionarioEstadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCuestionarioEstadoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCuestionarioEstado() throws Exception {
        // Get the cuestionarioEstado
        restCuestionarioEstadoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCuestionarioEstado() throws Exception {
        // Initialize the database
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);

        int databaseSizeBeforeUpdate = cuestionarioEstadoRepository.findAll().size();

        // Update the cuestionarioEstado
        CuestionarioEstado updatedCuestionarioEstado = cuestionarioEstadoRepository.findById(cuestionarioEstado.getId()).get();
        // Disconnect from session so that the updates on updatedCuestionarioEstado are not directly saved in db
        em.detach(updatedCuestionarioEstado);
        updatedCuestionarioEstado.valor(UPDATED_VALOR).valoracion(UPDATED_VALORACION);

        restCuestionarioEstadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCuestionarioEstado.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCuestionarioEstado))
            )
            .andExpect(status().isOk());

        // Validate the CuestionarioEstado in the database
        List<CuestionarioEstado> cuestionarioEstadoList = cuestionarioEstadoRepository.findAll();
        assertThat(cuestionarioEstadoList).hasSize(databaseSizeBeforeUpdate);
        CuestionarioEstado testCuestionarioEstado = cuestionarioEstadoList.get(cuestionarioEstadoList.size() - 1);
        assertThat(testCuestionarioEstado.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testCuestionarioEstado.getValoracion()).isEqualTo(UPDATED_VALORACION);
    }

    @Test
    @Transactional
    void putNonExistingCuestionarioEstado() throws Exception {
        int databaseSizeBeforeUpdate = cuestionarioEstadoRepository.findAll().size();
        cuestionarioEstado.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCuestionarioEstadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cuestionarioEstado.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cuestionarioEstado))
            )
            .andExpect(status().isBadRequest());

        // Validate the CuestionarioEstado in the database
        List<CuestionarioEstado> cuestionarioEstadoList = cuestionarioEstadoRepository.findAll();
        assertThat(cuestionarioEstadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCuestionarioEstado() throws Exception {
        int databaseSizeBeforeUpdate = cuestionarioEstadoRepository.findAll().size();
        cuestionarioEstado.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuestionarioEstadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cuestionarioEstado))
            )
            .andExpect(status().isBadRequest());

        // Validate the CuestionarioEstado in the database
        List<CuestionarioEstado> cuestionarioEstadoList = cuestionarioEstadoRepository.findAll();
        assertThat(cuestionarioEstadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCuestionarioEstado() throws Exception {
        int databaseSizeBeforeUpdate = cuestionarioEstadoRepository.findAll().size();
        cuestionarioEstado.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuestionarioEstadoMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cuestionarioEstado))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CuestionarioEstado in the database
        List<CuestionarioEstado> cuestionarioEstadoList = cuestionarioEstadoRepository.findAll();
        assertThat(cuestionarioEstadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCuestionarioEstadoWithPatch() throws Exception {
        // Initialize the database
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);

        int databaseSizeBeforeUpdate = cuestionarioEstadoRepository.findAll().size();

        // Update the cuestionarioEstado using partial update
        CuestionarioEstado partialUpdatedCuestionarioEstado = new CuestionarioEstado();
        partialUpdatedCuestionarioEstado.setId(cuestionarioEstado.getId());

        restCuestionarioEstadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCuestionarioEstado.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCuestionarioEstado))
            )
            .andExpect(status().isOk());

        // Validate the CuestionarioEstado in the database
        List<CuestionarioEstado> cuestionarioEstadoList = cuestionarioEstadoRepository.findAll();
        assertThat(cuestionarioEstadoList).hasSize(databaseSizeBeforeUpdate);
        CuestionarioEstado testCuestionarioEstado = cuestionarioEstadoList.get(cuestionarioEstadoList.size() - 1);
        assertThat(testCuestionarioEstado.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testCuestionarioEstado.getValoracion()).isEqualTo(DEFAULT_VALORACION);
    }

    @Test
    @Transactional
    void fullUpdateCuestionarioEstadoWithPatch() throws Exception {
        // Initialize the database
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);

        int databaseSizeBeforeUpdate = cuestionarioEstadoRepository.findAll().size();

        // Update the cuestionarioEstado using partial update
        CuestionarioEstado partialUpdatedCuestionarioEstado = new CuestionarioEstado();
        partialUpdatedCuestionarioEstado.setId(cuestionarioEstado.getId());

        partialUpdatedCuestionarioEstado.valor(UPDATED_VALOR).valoracion(UPDATED_VALORACION);

        restCuestionarioEstadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCuestionarioEstado.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCuestionarioEstado))
            )
            .andExpect(status().isOk());

        // Validate the CuestionarioEstado in the database
        List<CuestionarioEstado> cuestionarioEstadoList = cuestionarioEstadoRepository.findAll();
        assertThat(cuestionarioEstadoList).hasSize(databaseSizeBeforeUpdate);
        CuestionarioEstado testCuestionarioEstado = cuestionarioEstadoList.get(cuestionarioEstadoList.size() - 1);
        assertThat(testCuestionarioEstado.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testCuestionarioEstado.getValoracion()).isEqualTo(UPDATED_VALORACION);
    }

    @Test
    @Transactional
    void patchNonExistingCuestionarioEstado() throws Exception {
        int databaseSizeBeforeUpdate = cuestionarioEstadoRepository.findAll().size();
        cuestionarioEstado.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCuestionarioEstadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cuestionarioEstado.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cuestionarioEstado))
            )
            .andExpect(status().isBadRequest());

        // Validate the CuestionarioEstado in the database
        List<CuestionarioEstado> cuestionarioEstadoList = cuestionarioEstadoRepository.findAll();
        assertThat(cuestionarioEstadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCuestionarioEstado() throws Exception {
        int databaseSizeBeforeUpdate = cuestionarioEstadoRepository.findAll().size();
        cuestionarioEstado.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuestionarioEstadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cuestionarioEstado))
            )
            .andExpect(status().isBadRequest());

        // Validate the CuestionarioEstado in the database
        List<CuestionarioEstado> cuestionarioEstadoList = cuestionarioEstadoRepository.findAll();
        assertThat(cuestionarioEstadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCuestionarioEstado() throws Exception {
        int databaseSizeBeforeUpdate = cuestionarioEstadoRepository.findAll().size();
        cuestionarioEstado.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuestionarioEstadoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cuestionarioEstado))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CuestionarioEstado in the database
        List<CuestionarioEstado> cuestionarioEstadoList = cuestionarioEstadoRepository.findAll();
        assertThat(cuestionarioEstadoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCuestionarioEstado() throws Exception {
        // Initialize the database
        cuestionarioEstadoRepository.saveAndFlush(cuestionarioEstado);

        int databaseSizeBeforeDelete = cuestionarioEstadoRepository.findAll().size();

        // Delete the cuestionarioEstado
        restCuestionarioEstadoMockMvc
            .perform(delete(ENTITY_API_URL_ID, cuestionarioEstado.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CuestionarioEstado> cuestionarioEstadoList = cuestionarioEstadoRepository.findAll();
        assertThat(cuestionarioEstadoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

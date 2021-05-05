package com.be4tech.becare3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.Medicamento;
import com.be4tech.becare3.domain.TratamientoMedicamento;
import com.be4tech.becare3.domain.Tratamieto;
import com.be4tech.becare3.repository.TratamientoMedicamentoRepository;
import com.be4tech.becare3.service.criteria.TratamientoMedicamentoCriteria;
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
 * Integration tests for the {@link TratamientoMedicamentoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TratamientoMedicamentoResourceIT {

    private static final String DEFAULT_DOSIS = "AAAAAAAAAA";
    private static final String UPDATED_DOSIS = "BBBBBBBBBB";

    private static final String DEFAULT_INTENSIDAD = "AAAAAAAAAA";
    private static final String UPDATED_INTENSIDAD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tratamiento-medicamentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TratamientoMedicamentoRepository tratamientoMedicamentoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTratamientoMedicamentoMockMvc;

    private TratamientoMedicamento tratamientoMedicamento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TratamientoMedicamento createEntity(EntityManager em) {
        TratamientoMedicamento tratamientoMedicamento = new TratamientoMedicamento().dosis(DEFAULT_DOSIS).intensidad(DEFAULT_INTENSIDAD);
        return tratamientoMedicamento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TratamientoMedicamento createUpdatedEntity(EntityManager em) {
        TratamientoMedicamento tratamientoMedicamento = new TratamientoMedicamento().dosis(UPDATED_DOSIS).intensidad(UPDATED_INTENSIDAD);
        return tratamientoMedicamento;
    }

    @BeforeEach
    public void initTest() {
        tratamientoMedicamento = createEntity(em);
    }

    @Test
    @Transactional
    void createTratamientoMedicamento() throws Exception {
        int databaseSizeBeforeCreate = tratamientoMedicamentoRepository.findAll().size();
        // Create the TratamientoMedicamento
        restTratamientoMedicamentoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tratamientoMedicamento))
            )
            .andExpect(status().isCreated());

        // Validate the TratamientoMedicamento in the database
        List<TratamientoMedicamento> tratamientoMedicamentoList = tratamientoMedicamentoRepository.findAll();
        assertThat(tratamientoMedicamentoList).hasSize(databaseSizeBeforeCreate + 1);
        TratamientoMedicamento testTratamientoMedicamento = tratamientoMedicamentoList.get(tratamientoMedicamentoList.size() - 1);
        assertThat(testTratamientoMedicamento.getDosis()).isEqualTo(DEFAULT_DOSIS);
        assertThat(testTratamientoMedicamento.getIntensidad()).isEqualTo(DEFAULT_INTENSIDAD);
    }

    @Test
    @Transactional
    void createTratamientoMedicamentoWithExistingId() throws Exception {
        // Create the TratamientoMedicamento with an existing ID
        tratamientoMedicamento.setId(1L);

        int databaseSizeBeforeCreate = tratamientoMedicamentoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTratamientoMedicamentoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tratamientoMedicamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the TratamientoMedicamento in the database
        List<TratamientoMedicamento> tratamientoMedicamentoList = tratamientoMedicamentoRepository.findAll();
        assertThat(tratamientoMedicamentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTratamientoMedicamentos() throws Exception {
        // Initialize the database
        tratamientoMedicamentoRepository.saveAndFlush(tratamientoMedicamento);

        // Get all the tratamientoMedicamentoList
        restTratamientoMedicamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tratamientoMedicamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].dosis").value(hasItem(DEFAULT_DOSIS)))
            .andExpect(jsonPath("$.[*].intensidad").value(hasItem(DEFAULT_INTENSIDAD)));
    }

    @Test
    @Transactional
    void getTratamientoMedicamento() throws Exception {
        // Initialize the database
        tratamientoMedicamentoRepository.saveAndFlush(tratamientoMedicamento);

        // Get the tratamientoMedicamento
        restTratamientoMedicamentoMockMvc
            .perform(get(ENTITY_API_URL_ID, tratamientoMedicamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tratamientoMedicamento.getId().intValue()))
            .andExpect(jsonPath("$.dosis").value(DEFAULT_DOSIS))
            .andExpect(jsonPath("$.intensidad").value(DEFAULT_INTENSIDAD));
    }

    @Test
    @Transactional
    void getTratamientoMedicamentosByIdFiltering() throws Exception {
        // Initialize the database
        tratamientoMedicamentoRepository.saveAndFlush(tratamientoMedicamento);

        Long id = tratamientoMedicamento.getId();

        defaultTratamientoMedicamentoShouldBeFound("id.equals=" + id);
        defaultTratamientoMedicamentoShouldNotBeFound("id.notEquals=" + id);

        defaultTratamientoMedicamentoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTratamientoMedicamentoShouldNotBeFound("id.greaterThan=" + id);

        defaultTratamientoMedicamentoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTratamientoMedicamentoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTratamientoMedicamentosByDosisIsEqualToSomething() throws Exception {
        // Initialize the database
        tratamientoMedicamentoRepository.saveAndFlush(tratamientoMedicamento);

        // Get all the tratamientoMedicamentoList where dosis equals to DEFAULT_DOSIS
        defaultTratamientoMedicamentoShouldBeFound("dosis.equals=" + DEFAULT_DOSIS);

        // Get all the tratamientoMedicamentoList where dosis equals to UPDATED_DOSIS
        defaultTratamientoMedicamentoShouldNotBeFound("dosis.equals=" + UPDATED_DOSIS);
    }

    @Test
    @Transactional
    void getAllTratamientoMedicamentosByDosisIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tratamientoMedicamentoRepository.saveAndFlush(tratamientoMedicamento);

        // Get all the tratamientoMedicamentoList where dosis not equals to DEFAULT_DOSIS
        defaultTratamientoMedicamentoShouldNotBeFound("dosis.notEquals=" + DEFAULT_DOSIS);

        // Get all the tratamientoMedicamentoList where dosis not equals to UPDATED_DOSIS
        defaultTratamientoMedicamentoShouldBeFound("dosis.notEquals=" + UPDATED_DOSIS);
    }

    @Test
    @Transactional
    void getAllTratamientoMedicamentosByDosisIsInShouldWork() throws Exception {
        // Initialize the database
        tratamientoMedicamentoRepository.saveAndFlush(tratamientoMedicamento);

        // Get all the tratamientoMedicamentoList where dosis in DEFAULT_DOSIS or UPDATED_DOSIS
        defaultTratamientoMedicamentoShouldBeFound("dosis.in=" + DEFAULT_DOSIS + "," + UPDATED_DOSIS);

        // Get all the tratamientoMedicamentoList where dosis equals to UPDATED_DOSIS
        defaultTratamientoMedicamentoShouldNotBeFound("dosis.in=" + UPDATED_DOSIS);
    }

    @Test
    @Transactional
    void getAllTratamientoMedicamentosByDosisIsNullOrNotNull() throws Exception {
        // Initialize the database
        tratamientoMedicamentoRepository.saveAndFlush(tratamientoMedicamento);

        // Get all the tratamientoMedicamentoList where dosis is not null
        defaultTratamientoMedicamentoShouldBeFound("dosis.specified=true");

        // Get all the tratamientoMedicamentoList where dosis is null
        defaultTratamientoMedicamentoShouldNotBeFound("dosis.specified=false");
    }

    @Test
    @Transactional
    void getAllTratamientoMedicamentosByDosisContainsSomething() throws Exception {
        // Initialize the database
        tratamientoMedicamentoRepository.saveAndFlush(tratamientoMedicamento);

        // Get all the tratamientoMedicamentoList where dosis contains DEFAULT_DOSIS
        defaultTratamientoMedicamentoShouldBeFound("dosis.contains=" + DEFAULT_DOSIS);

        // Get all the tratamientoMedicamentoList where dosis contains UPDATED_DOSIS
        defaultTratamientoMedicamentoShouldNotBeFound("dosis.contains=" + UPDATED_DOSIS);
    }

    @Test
    @Transactional
    void getAllTratamientoMedicamentosByDosisNotContainsSomething() throws Exception {
        // Initialize the database
        tratamientoMedicamentoRepository.saveAndFlush(tratamientoMedicamento);

        // Get all the tratamientoMedicamentoList where dosis does not contain DEFAULT_DOSIS
        defaultTratamientoMedicamentoShouldNotBeFound("dosis.doesNotContain=" + DEFAULT_DOSIS);

        // Get all the tratamientoMedicamentoList where dosis does not contain UPDATED_DOSIS
        defaultTratamientoMedicamentoShouldBeFound("dosis.doesNotContain=" + UPDATED_DOSIS);
    }

    @Test
    @Transactional
    void getAllTratamientoMedicamentosByIntensidadIsEqualToSomething() throws Exception {
        // Initialize the database
        tratamientoMedicamentoRepository.saveAndFlush(tratamientoMedicamento);

        // Get all the tratamientoMedicamentoList where intensidad equals to DEFAULT_INTENSIDAD
        defaultTratamientoMedicamentoShouldBeFound("intensidad.equals=" + DEFAULT_INTENSIDAD);

        // Get all the tratamientoMedicamentoList where intensidad equals to UPDATED_INTENSIDAD
        defaultTratamientoMedicamentoShouldNotBeFound("intensidad.equals=" + UPDATED_INTENSIDAD);
    }

    @Test
    @Transactional
    void getAllTratamientoMedicamentosByIntensidadIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tratamientoMedicamentoRepository.saveAndFlush(tratamientoMedicamento);

        // Get all the tratamientoMedicamentoList where intensidad not equals to DEFAULT_INTENSIDAD
        defaultTratamientoMedicamentoShouldNotBeFound("intensidad.notEquals=" + DEFAULT_INTENSIDAD);

        // Get all the tratamientoMedicamentoList where intensidad not equals to UPDATED_INTENSIDAD
        defaultTratamientoMedicamentoShouldBeFound("intensidad.notEquals=" + UPDATED_INTENSIDAD);
    }

    @Test
    @Transactional
    void getAllTratamientoMedicamentosByIntensidadIsInShouldWork() throws Exception {
        // Initialize the database
        tratamientoMedicamentoRepository.saveAndFlush(tratamientoMedicamento);

        // Get all the tratamientoMedicamentoList where intensidad in DEFAULT_INTENSIDAD or UPDATED_INTENSIDAD
        defaultTratamientoMedicamentoShouldBeFound("intensidad.in=" + DEFAULT_INTENSIDAD + "," + UPDATED_INTENSIDAD);

        // Get all the tratamientoMedicamentoList where intensidad equals to UPDATED_INTENSIDAD
        defaultTratamientoMedicamentoShouldNotBeFound("intensidad.in=" + UPDATED_INTENSIDAD);
    }

    @Test
    @Transactional
    void getAllTratamientoMedicamentosByIntensidadIsNullOrNotNull() throws Exception {
        // Initialize the database
        tratamientoMedicamentoRepository.saveAndFlush(tratamientoMedicamento);

        // Get all the tratamientoMedicamentoList where intensidad is not null
        defaultTratamientoMedicamentoShouldBeFound("intensidad.specified=true");

        // Get all the tratamientoMedicamentoList where intensidad is null
        defaultTratamientoMedicamentoShouldNotBeFound("intensidad.specified=false");
    }

    @Test
    @Transactional
    void getAllTratamientoMedicamentosByIntensidadContainsSomething() throws Exception {
        // Initialize the database
        tratamientoMedicamentoRepository.saveAndFlush(tratamientoMedicamento);

        // Get all the tratamientoMedicamentoList where intensidad contains DEFAULT_INTENSIDAD
        defaultTratamientoMedicamentoShouldBeFound("intensidad.contains=" + DEFAULT_INTENSIDAD);

        // Get all the tratamientoMedicamentoList where intensidad contains UPDATED_INTENSIDAD
        defaultTratamientoMedicamentoShouldNotBeFound("intensidad.contains=" + UPDATED_INTENSIDAD);
    }

    @Test
    @Transactional
    void getAllTratamientoMedicamentosByIntensidadNotContainsSomething() throws Exception {
        // Initialize the database
        tratamientoMedicamentoRepository.saveAndFlush(tratamientoMedicamento);

        // Get all the tratamientoMedicamentoList where intensidad does not contain DEFAULT_INTENSIDAD
        defaultTratamientoMedicamentoShouldNotBeFound("intensidad.doesNotContain=" + DEFAULT_INTENSIDAD);

        // Get all the tratamientoMedicamentoList where intensidad does not contain UPDATED_INTENSIDAD
        defaultTratamientoMedicamentoShouldBeFound("intensidad.doesNotContain=" + UPDATED_INTENSIDAD);
    }

    @Test
    @Transactional
    void getAllTratamientoMedicamentosByTratamietoIsEqualToSomething() throws Exception {
        // Initialize the database
        tratamientoMedicamentoRepository.saveAndFlush(tratamientoMedicamento);
        Tratamieto tratamieto = TratamietoResourceIT.createEntity(em);
        em.persist(tratamieto);
        em.flush();
        tratamientoMedicamento.setTratamieto(tratamieto);
        tratamientoMedicamentoRepository.saveAndFlush(tratamientoMedicamento);
        Long tratamietoId = tratamieto.getId();

        // Get all the tratamientoMedicamentoList where tratamieto equals to tratamietoId
        defaultTratamientoMedicamentoShouldBeFound("tratamietoId.equals=" + tratamietoId);

        // Get all the tratamientoMedicamentoList where tratamieto equals to (tratamietoId + 1)
        defaultTratamientoMedicamentoShouldNotBeFound("tratamietoId.equals=" + (tratamietoId + 1));
    }

    @Test
    @Transactional
    void getAllTratamientoMedicamentosByMedicamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        tratamientoMedicamentoRepository.saveAndFlush(tratamientoMedicamento);
        Medicamento medicamento = MedicamentoResourceIT.createEntity(em);
        em.persist(medicamento);
        em.flush();
        tratamientoMedicamento.setMedicamento(medicamento);
        tratamientoMedicamentoRepository.saveAndFlush(tratamientoMedicamento);
        Long medicamentoId = medicamento.getId();

        // Get all the tratamientoMedicamentoList where medicamento equals to medicamentoId
        defaultTratamientoMedicamentoShouldBeFound("medicamentoId.equals=" + medicamentoId);

        // Get all the tratamientoMedicamentoList where medicamento equals to (medicamentoId + 1)
        defaultTratamientoMedicamentoShouldNotBeFound("medicamentoId.equals=" + (medicamentoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTratamientoMedicamentoShouldBeFound(String filter) throws Exception {
        restTratamientoMedicamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tratamientoMedicamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].dosis").value(hasItem(DEFAULT_DOSIS)))
            .andExpect(jsonPath("$.[*].intensidad").value(hasItem(DEFAULT_INTENSIDAD)));

        // Check, that the count call also returns 1
        restTratamientoMedicamentoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTratamientoMedicamentoShouldNotBeFound(String filter) throws Exception {
        restTratamientoMedicamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTratamientoMedicamentoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTratamientoMedicamento() throws Exception {
        // Get the tratamientoMedicamento
        restTratamientoMedicamentoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTratamientoMedicamento() throws Exception {
        // Initialize the database
        tratamientoMedicamentoRepository.saveAndFlush(tratamientoMedicamento);

        int databaseSizeBeforeUpdate = tratamientoMedicamentoRepository.findAll().size();

        // Update the tratamientoMedicamento
        TratamientoMedicamento updatedTratamientoMedicamento = tratamientoMedicamentoRepository
            .findById(tratamientoMedicamento.getId())
            .get();
        // Disconnect from session so that the updates on updatedTratamientoMedicamento are not directly saved in db
        em.detach(updatedTratamientoMedicamento);
        updatedTratamientoMedicamento.dosis(UPDATED_DOSIS).intensidad(UPDATED_INTENSIDAD);

        restTratamientoMedicamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTratamientoMedicamento.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTratamientoMedicamento))
            )
            .andExpect(status().isOk());

        // Validate the TratamientoMedicamento in the database
        List<TratamientoMedicamento> tratamientoMedicamentoList = tratamientoMedicamentoRepository.findAll();
        assertThat(tratamientoMedicamentoList).hasSize(databaseSizeBeforeUpdate);
        TratamientoMedicamento testTratamientoMedicamento = tratamientoMedicamentoList.get(tratamientoMedicamentoList.size() - 1);
        assertThat(testTratamientoMedicamento.getDosis()).isEqualTo(UPDATED_DOSIS);
        assertThat(testTratamientoMedicamento.getIntensidad()).isEqualTo(UPDATED_INTENSIDAD);
    }

    @Test
    @Transactional
    void putNonExistingTratamientoMedicamento() throws Exception {
        int databaseSizeBeforeUpdate = tratamientoMedicamentoRepository.findAll().size();
        tratamientoMedicamento.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTratamientoMedicamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tratamientoMedicamento.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tratamientoMedicamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the TratamientoMedicamento in the database
        List<TratamientoMedicamento> tratamientoMedicamentoList = tratamientoMedicamentoRepository.findAll();
        assertThat(tratamientoMedicamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTratamientoMedicamento() throws Exception {
        int databaseSizeBeforeUpdate = tratamientoMedicamentoRepository.findAll().size();
        tratamientoMedicamento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTratamientoMedicamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tratamientoMedicamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the TratamientoMedicamento in the database
        List<TratamientoMedicamento> tratamientoMedicamentoList = tratamientoMedicamentoRepository.findAll();
        assertThat(tratamientoMedicamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTratamientoMedicamento() throws Exception {
        int databaseSizeBeforeUpdate = tratamientoMedicamentoRepository.findAll().size();
        tratamientoMedicamento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTratamientoMedicamentoMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tratamientoMedicamento))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TratamientoMedicamento in the database
        List<TratamientoMedicamento> tratamientoMedicamentoList = tratamientoMedicamentoRepository.findAll();
        assertThat(tratamientoMedicamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTratamientoMedicamentoWithPatch() throws Exception {
        // Initialize the database
        tratamientoMedicamentoRepository.saveAndFlush(tratamientoMedicamento);

        int databaseSizeBeforeUpdate = tratamientoMedicamentoRepository.findAll().size();

        // Update the tratamientoMedicamento using partial update
        TratamientoMedicamento partialUpdatedTratamientoMedicamento = new TratamientoMedicamento();
        partialUpdatedTratamientoMedicamento.setId(tratamientoMedicamento.getId());

        partialUpdatedTratamientoMedicamento.dosis(UPDATED_DOSIS).intensidad(UPDATED_INTENSIDAD);

        restTratamientoMedicamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTratamientoMedicamento.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTratamientoMedicamento))
            )
            .andExpect(status().isOk());

        // Validate the TratamientoMedicamento in the database
        List<TratamientoMedicamento> tratamientoMedicamentoList = tratamientoMedicamentoRepository.findAll();
        assertThat(tratamientoMedicamentoList).hasSize(databaseSizeBeforeUpdate);
        TratamientoMedicamento testTratamientoMedicamento = tratamientoMedicamentoList.get(tratamientoMedicamentoList.size() - 1);
        assertThat(testTratamientoMedicamento.getDosis()).isEqualTo(UPDATED_DOSIS);
        assertThat(testTratamientoMedicamento.getIntensidad()).isEqualTo(UPDATED_INTENSIDAD);
    }

    @Test
    @Transactional
    void fullUpdateTratamientoMedicamentoWithPatch() throws Exception {
        // Initialize the database
        tratamientoMedicamentoRepository.saveAndFlush(tratamientoMedicamento);

        int databaseSizeBeforeUpdate = tratamientoMedicamentoRepository.findAll().size();

        // Update the tratamientoMedicamento using partial update
        TratamientoMedicamento partialUpdatedTratamientoMedicamento = new TratamientoMedicamento();
        partialUpdatedTratamientoMedicamento.setId(tratamientoMedicamento.getId());

        partialUpdatedTratamientoMedicamento.dosis(UPDATED_DOSIS).intensidad(UPDATED_INTENSIDAD);

        restTratamientoMedicamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTratamientoMedicamento.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTratamientoMedicamento))
            )
            .andExpect(status().isOk());

        // Validate the TratamientoMedicamento in the database
        List<TratamientoMedicamento> tratamientoMedicamentoList = tratamientoMedicamentoRepository.findAll();
        assertThat(tratamientoMedicamentoList).hasSize(databaseSizeBeforeUpdate);
        TratamientoMedicamento testTratamientoMedicamento = tratamientoMedicamentoList.get(tratamientoMedicamentoList.size() - 1);
        assertThat(testTratamientoMedicamento.getDosis()).isEqualTo(UPDATED_DOSIS);
        assertThat(testTratamientoMedicamento.getIntensidad()).isEqualTo(UPDATED_INTENSIDAD);
    }

    @Test
    @Transactional
    void patchNonExistingTratamientoMedicamento() throws Exception {
        int databaseSizeBeforeUpdate = tratamientoMedicamentoRepository.findAll().size();
        tratamientoMedicamento.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTratamientoMedicamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tratamientoMedicamento.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tratamientoMedicamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the TratamientoMedicamento in the database
        List<TratamientoMedicamento> tratamientoMedicamentoList = tratamientoMedicamentoRepository.findAll();
        assertThat(tratamientoMedicamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTratamientoMedicamento() throws Exception {
        int databaseSizeBeforeUpdate = tratamientoMedicamentoRepository.findAll().size();
        tratamientoMedicamento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTratamientoMedicamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tratamientoMedicamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the TratamientoMedicamento in the database
        List<TratamientoMedicamento> tratamientoMedicamentoList = tratamientoMedicamentoRepository.findAll();
        assertThat(tratamientoMedicamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTratamientoMedicamento() throws Exception {
        int databaseSizeBeforeUpdate = tratamientoMedicamentoRepository.findAll().size();
        tratamientoMedicamento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTratamientoMedicamentoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tratamientoMedicamento))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TratamientoMedicamento in the database
        List<TratamientoMedicamento> tratamientoMedicamentoList = tratamientoMedicamentoRepository.findAll();
        assertThat(tratamientoMedicamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTratamientoMedicamento() throws Exception {
        // Initialize the database
        tratamientoMedicamentoRepository.saveAndFlush(tratamientoMedicamento);

        int databaseSizeBeforeDelete = tratamientoMedicamentoRepository.findAll().size();

        // Delete the tratamientoMedicamento
        restTratamientoMedicamentoMockMvc
            .perform(delete(ENTITY_API_URL_ID, tratamientoMedicamento.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TratamientoMedicamento> tratamientoMedicamentoList = tratamientoMedicamentoRepository.findAll();
        assertThat(tratamientoMedicamentoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

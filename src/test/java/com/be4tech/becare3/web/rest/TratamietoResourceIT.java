package com.be4tech.becare3.web.rest;

import static com.be4tech.becare3.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.Tratamieto;
import com.be4tech.becare3.repository.TratamietoRepository;
import com.be4tech.becare3.service.criteria.TratamietoCriteria;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link TratamietoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TratamietoResourceIT {

    private static final String DEFAULT_DESCRIPCION_TRATAMIENTO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION_TRATAMIENTO = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ZonedDateTime DEFAULT_FECHA_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_FECHA_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/tratamietos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TratamietoRepository tratamietoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTratamietoMockMvc;

    private Tratamieto tratamieto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tratamieto createEntity(EntityManager em) {
        Tratamieto tratamieto = new Tratamieto()
            .descripcionTratamiento(DEFAULT_DESCRIPCION_TRATAMIENTO)
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .fechaFin(DEFAULT_FECHA_FIN);
        return tratamieto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tratamieto createUpdatedEntity(EntityManager em) {
        Tratamieto tratamieto = new Tratamieto()
            .descripcionTratamiento(UPDATED_DESCRIPCION_TRATAMIENTO)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN);
        return tratamieto;
    }

    @BeforeEach
    public void initTest() {
        tratamieto = createEntity(em);
    }

    @Test
    @Transactional
    void createTratamieto() throws Exception {
        int databaseSizeBeforeCreate = tratamietoRepository.findAll().size();
        // Create the Tratamieto
        restTratamietoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tratamieto))
            )
            .andExpect(status().isCreated());

        // Validate the Tratamieto in the database
        List<Tratamieto> tratamietoList = tratamietoRepository.findAll();
        assertThat(tratamietoList).hasSize(databaseSizeBeforeCreate + 1);
        Tratamieto testTratamieto = tratamietoList.get(tratamietoList.size() - 1);
        assertThat(testTratamieto.getDescripcionTratamiento()).isEqualTo(DEFAULT_DESCRIPCION_TRATAMIENTO);
        assertThat(testTratamieto.getFechaInicio()).isEqualTo(DEFAULT_FECHA_INICIO);
        assertThat(testTratamieto.getFechaFin()).isEqualTo(DEFAULT_FECHA_FIN);
    }

    @Test
    @Transactional
    void createTratamietoWithExistingId() throws Exception {
        // Create the Tratamieto with an existing ID
        tratamieto.setId(1L);

        int databaseSizeBeforeCreate = tratamietoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTratamietoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tratamieto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tratamieto in the database
        List<Tratamieto> tratamietoList = tratamietoRepository.findAll();
        assertThat(tratamietoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTratamietos() throws Exception {
        // Initialize the database
        tratamietoRepository.saveAndFlush(tratamieto);

        // Get all the tratamietoList
        restTratamietoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tratamieto.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcionTratamiento").value(hasItem(DEFAULT_DESCRIPCION_TRATAMIENTO.toString())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(sameInstant(DEFAULT_FECHA_FIN))));
    }

    @Test
    @Transactional
    void getTratamieto() throws Exception {
        // Initialize the database
        tratamietoRepository.saveAndFlush(tratamieto);

        // Get the tratamieto
        restTratamietoMockMvc
            .perform(get(ENTITY_API_URL_ID, tratamieto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tratamieto.getId().intValue()))
            .andExpect(jsonPath("$.descripcionTratamiento").value(DEFAULT_DESCRIPCION_TRATAMIENTO.toString()))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFin").value(sameInstant(DEFAULT_FECHA_FIN)));
    }

    @Test
    @Transactional
    void getTratamietosByIdFiltering() throws Exception {
        // Initialize the database
        tratamietoRepository.saveAndFlush(tratamieto);

        Long id = tratamieto.getId();

        defaultTratamietoShouldBeFound("id.equals=" + id);
        defaultTratamietoShouldNotBeFound("id.notEquals=" + id);

        defaultTratamietoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTratamietoShouldNotBeFound("id.greaterThan=" + id);

        defaultTratamietoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTratamietoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTratamietosByFechaInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        tratamietoRepository.saveAndFlush(tratamieto);

        // Get all the tratamietoList where fechaInicio equals to DEFAULT_FECHA_INICIO
        defaultTratamietoShouldBeFound("fechaInicio.equals=" + DEFAULT_FECHA_INICIO);

        // Get all the tratamietoList where fechaInicio equals to UPDATED_FECHA_INICIO
        defaultTratamietoShouldNotBeFound("fechaInicio.equals=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllTratamietosByFechaInicioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tratamietoRepository.saveAndFlush(tratamieto);

        // Get all the tratamietoList where fechaInicio not equals to DEFAULT_FECHA_INICIO
        defaultTratamietoShouldNotBeFound("fechaInicio.notEquals=" + DEFAULT_FECHA_INICIO);

        // Get all the tratamietoList where fechaInicio not equals to UPDATED_FECHA_INICIO
        defaultTratamietoShouldBeFound("fechaInicio.notEquals=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllTratamietosByFechaInicioIsInShouldWork() throws Exception {
        // Initialize the database
        tratamietoRepository.saveAndFlush(tratamieto);

        // Get all the tratamietoList where fechaInicio in DEFAULT_FECHA_INICIO or UPDATED_FECHA_INICIO
        defaultTratamietoShouldBeFound("fechaInicio.in=" + DEFAULT_FECHA_INICIO + "," + UPDATED_FECHA_INICIO);

        // Get all the tratamietoList where fechaInicio equals to UPDATED_FECHA_INICIO
        defaultTratamietoShouldNotBeFound("fechaInicio.in=" + UPDATED_FECHA_INICIO);
    }

    @Test
    @Transactional
    void getAllTratamietosByFechaInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        tratamietoRepository.saveAndFlush(tratamieto);

        // Get all the tratamietoList where fechaInicio is not null
        defaultTratamietoShouldBeFound("fechaInicio.specified=true");

        // Get all the tratamietoList where fechaInicio is null
        defaultTratamietoShouldNotBeFound("fechaInicio.specified=false");
    }

    @Test
    @Transactional
    void getAllTratamietosByFechaFinIsEqualToSomething() throws Exception {
        // Initialize the database
        tratamietoRepository.saveAndFlush(tratamieto);

        // Get all the tratamietoList where fechaFin equals to DEFAULT_FECHA_FIN
        defaultTratamietoShouldBeFound("fechaFin.equals=" + DEFAULT_FECHA_FIN);

        // Get all the tratamietoList where fechaFin equals to UPDATED_FECHA_FIN
        defaultTratamietoShouldNotBeFound("fechaFin.equals=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTratamietosByFechaFinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tratamietoRepository.saveAndFlush(tratamieto);

        // Get all the tratamietoList where fechaFin not equals to DEFAULT_FECHA_FIN
        defaultTratamietoShouldNotBeFound("fechaFin.notEquals=" + DEFAULT_FECHA_FIN);

        // Get all the tratamietoList where fechaFin not equals to UPDATED_FECHA_FIN
        defaultTratamietoShouldBeFound("fechaFin.notEquals=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTratamietosByFechaFinIsInShouldWork() throws Exception {
        // Initialize the database
        tratamietoRepository.saveAndFlush(tratamieto);

        // Get all the tratamietoList where fechaFin in DEFAULT_FECHA_FIN or UPDATED_FECHA_FIN
        defaultTratamietoShouldBeFound("fechaFin.in=" + DEFAULT_FECHA_FIN + "," + UPDATED_FECHA_FIN);

        // Get all the tratamietoList where fechaFin equals to UPDATED_FECHA_FIN
        defaultTratamietoShouldNotBeFound("fechaFin.in=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTratamietosByFechaFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        tratamietoRepository.saveAndFlush(tratamieto);

        // Get all the tratamietoList where fechaFin is not null
        defaultTratamietoShouldBeFound("fechaFin.specified=true");

        // Get all the tratamietoList where fechaFin is null
        defaultTratamietoShouldNotBeFound("fechaFin.specified=false");
    }

    @Test
    @Transactional
    void getAllTratamietosByFechaFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tratamietoRepository.saveAndFlush(tratamieto);

        // Get all the tratamietoList where fechaFin is greater than or equal to DEFAULT_FECHA_FIN
        defaultTratamietoShouldBeFound("fechaFin.greaterThanOrEqual=" + DEFAULT_FECHA_FIN);

        // Get all the tratamietoList where fechaFin is greater than or equal to UPDATED_FECHA_FIN
        defaultTratamietoShouldNotBeFound("fechaFin.greaterThanOrEqual=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTratamietosByFechaFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tratamietoRepository.saveAndFlush(tratamieto);

        // Get all the tratamietoList where fechaFin is less than or equal to DEFAULT_FECHA_FIN
        defaultTratamietoShouldBeFound("fechaFin.lessThanOrEqual=" + DEFAULT_FECHA_FIN);

        // Get all the tratamietoList where fechaFin is less than or equal to SMALLER_FECHA_FIN
        defaultTratamietoShouldNotBeFound("fechaFin.lessThanOrEqual=" + SMALLER_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTratamietosByFechaFinIsLessThanSomething() throws Exception {
        // Initialize the database
        tratamietoRepository.saveAndFlush(tratamieto);

        // Get all the tratamietoList where fechaFin is less than DEFAULT_FECHA_FIN
        defaultTratamietoShouldNotBeFound("fechaFin.lessThan=" + DEFAULT_FECHA_FIN);

        // Get all the tratamietoList where fechaFin is less than UPDATED_FECHA_FIN
        defaultTratamietoShouldBeFound("fechaFin.lessThan=" + UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void getAllTratamietosByFechaFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tratamietoRepository.saveAndFlush(tratamieto);

        // Get all the tratamietoList where fechaFin is greater than DEFAULT_FECHA_FIN
        defaultTratamietoShouldNotBeFound("fechaFin.greaterThan=" + DEFAULT_FECHA_FIN);

        // Get all the tratamietoList where fechaFin is greater than SMALLER_FECHA_FIN
        defaultTratamietoShouldBeFound("fechaFin.greaterThan=" + SMALLER_FECHA_FIN);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTratamietoShouldBeFound(String filter) throws Exception {
        restTratamietoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tratamieto.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcionTratamiento").value(hasItem(DEFAULT_DESCRIPCION_TRATAMIENTO.toString())))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(sameInstant(DEFAULT_FECHA_FIN))));

        // Check, that the count call also returns 1
        restTratamietoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTratamietoShouldNotBeFound(String filter) throws Exception {
        restTratamietoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTratamietoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTratamieto() throws Exception {
        // Get the tratamieto
        restTratamietoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTratamieto() throws Exception {
        // Initialize the database
        tratamietoRepository.saveAndFlush(tratamieto);

        int databaseSizeBeforeUpdate = tratamietoRepository.findAll().size();

        // Update the tratamieto
        Tratamieto updatedTratamieto = tratamietoRepository.findById(tratamieto.getId()).get();
        // Disconnect from session so that the updates on updatedTratamieto are not directly saved in db
        em.detach(updatedTratamieto);
        updatedTratamieto
            .descripcionTratamiento(UPDATED_DESCRIPCION_TRATAMIENTO)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN);

        restTratamietoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTratamieto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTratamieto))
            )
            .andExpect(status().isOk());

        // Validate the Tratamieto in the database
        List<Tratamieto> tratamietoList = tratamietoRepository.findAll();
        assertThat(tratamietoList).hasSize(databaseSizeBeforeUpdate);
        Tratamieto testTratamieto = tratamietoList.get(tratamietoList.size() - 1);
        assertThat(testTratamieto.getDescripcionTratamiento()).isEqualTo(UPDATED_DESCRIPCION_TRATAMIENTO);
        assertThat(testTratamieto.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testTratamieto.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void putNonExistingTratamieto() throws Exception {
        int databaseSizeBeforeUpdate = tratamietoRepository.findAll().size();
        tratamieto.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTratamietoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tratamieto.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tratamieto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tratamieto in the database
        List<Tratamieto> tratamietoList = tratamietoRepository.findAll();
        assertThat(tratamietoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTratamieto() throws Exception {
        int databaseSizeBeforeUpdate = tratamietoRepository.findAll().size();
        tratamieto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTratamietoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tratamieto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tratamieto in the database
        List<Tratamieto> tratamietoList = tratamietoRepository.findAll();
        assertThat(tratamietoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTratamieto() throws Exception {
        int databaseSizeBeforeUpdate = tratamietoRepository.findAll().size();
        tratamieto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTratamietoMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tratamieto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tratamieto in the database
        List<Tratamieto> tratamietoList = tratamietoRepository.findAll();
        assertThat(tratamietoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTratamietoWithPatch() throws Exception {
        // Initialize the database
        tratamietoRepository.saveAndFlush(tratamieto);

        int databaseSizeBeforeUpdate = tratamietoRepository.findAll().size();

        // Update the tratamieto using partial update
        Tratamieto partialUpdatedTratamieto = new Tratamieto();
        partialUpdatedTratamieto.setId(tratamieto.getId());

        partialUpdatedTratamieto.fechaInicio(UPDATED_FECHA_INICIO).fechaFin(UPDATED_FECHA_FIN);

        restTratamietoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTratamieto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTratamieto))
            )
            .andExpect(status().isOk());

        // Validate the Tratamieto in the database
        List<Tratamieto> tratamietoList = tratamietoRepository.findAll();
        assertThat(tratamietoList).hasSize(databaseSizeBeforeUpdate);
        Tratamieto testTratamieto = tratamietoList.get(tratamietoList.size() - 1);
        assertThat(testTratamieto.getDescripcionTratamiento()).isEqualTo(DEFAULT_DESCRIPCION_TRATAMIENTO);
        assertThat(testTratamieto.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testTratamieto.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void fullUpdateTratamietoWithPatch() throws Exception {
        // Initialize the database
        tratamietoRepository.saveAndFlush(tratamieto);

        int databaseSizeBeforeUpdate = tratamietoRepository.findAll().size();

        // Update the tratamieto using partial update
        Tratamieto partialUpdatedTratamieto = new Tratamieto();
        partialUpdatedTratamieto.setId(tratamieto.getId());

        partialUpdatedTratamieto
            .descripcionTratamiento(UPDATED_DESCRIPCION_TRATAMIENTO)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN);

        restTratamietoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTratamieto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTratamieto))
            )
            .andExpect(status().isOk());

        // Validate the Tratamieto in the database
        List<Tratamieto> tratamietoList = tratamietoRepository.findAll();
        assertThat(tratamietoList).hasSize(databaseSizeBeforeUpdate);
        Tratamieto testTratamieto = tratamietoList.get(tratamietoList.size() - 1);
        assertThat(testTratamieto.getDescripcionTratamiento()).isEqualTo(UPDATED_DESCRIPCION_TRATAMIENTO);
        assertThat(testTratamieto.getFechaInicio()).isEqualTo(UPDATED_FECHA_INICIO);
        assertThat(testTratamieto.getFechaFin()).isEqualTo(UPDATED_FECHA_FIN);
    }

    @Test
    @Transactional
    void patchNonExistingTratamieto() throws Exception {
        int databaseSizeBeforeUpdate = tratamietoRepository.findAll().size();
        tratamieto.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTratamietoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tratamieto.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tratamieto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tratamieto in the database
        List<Tratamieto> tratamietoList = tratamietoRepository.findAll();
        assertThat(tratamietoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTratamieto() throws Exception {
        int databaseSizeBeforeUpdate = tratamietoRepository.findAll().size();
        tratamieto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTratamietoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tratamieto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tratamieto in the database
        List<Tratamieto> tratamietoList = tratamietoRepository.findAll();
        assertThat(tratamietoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTratamieto() throws Exception {
        int databaseSizeBeforeUpdate = tratamietoRepository.findAll().size();
        tratamieto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTratamietoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tratamieto))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tratamieto in the database
        List<Tratamieto> tratamietoList = tratamietoRepository.findAll();
        assertThat(tratamietoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTratamieto() throws Exception {
        // Initialize the database
        tratamietoRepository.saveAndFlush(tratamieto);

        int databaseSizeBeforeDelete = tratamietoRepository.findAll().size();

        // Delete the tratamieto
        restTratamietoMockMvc
            .perform(delete(ENTITY_API_URL_ID, tratamieto.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tratamieto> tratamietoList = tratamietoRepository.findAll();
        assertThat(tratamietoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

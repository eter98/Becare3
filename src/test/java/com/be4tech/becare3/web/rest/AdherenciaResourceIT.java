package com.be4tech.becare3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.Adherencia;
import com.be4tech.becare3.domain.Medicamento;
import com.be4tech.becare3.domain.Paciente;
import com.be4tech.becare3.repository.AdherenciaRepository;
import com.be4tech.becare3.service.criteria.AdherenciaCriteria;
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
 * Integration tests for the {@link AdherenciaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdherenciaResourceIT {

    private static final Instant DEFAULT_HORA_TOMA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HORA_TOMA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_RESPUESTA = false;
    private static final Boolean UPDATED_RESPUESTA = true;

    private static final Integer DEFAULT_VALOR = 1;
    private static final Integer UPDATED_VALOR = 2;
    private static final Integer SMALLER_VALOR = 1 - 1;

    private static final String DEFAULT_COMENTARIO = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARIO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/adherencias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdherenciaRepository adherenciaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdherenciaMockMvc;

    private Adherencia adherencia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Adherencia createEntity(EntityManager em) {
        Adherencia adherencia = new Adherencia()
            .horaToma(DEFAULT_HORA_TOMA)
            .respuesta(DEFAULT_RESPUESTA)
            .valor(DEFAULT_VALOR)
            .comentario(DEFAULT_COMENTARIO);
        return adherencia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Adherencia createUpdatedEntity(EntityManager em) {
        Adherencia adherencia = new Adherencia()
            .horaToma(UPDATED_HORA_TOMA)
            .respuesta(UPDATED_RESPUESTA)
            .valor(UPDATED_VALOR)
            .comentario(UPDATED_COMENTARIO);
        return adherencia;
    }

    @BeforeEach
    public void initTest() {
        adherencia = createEntity(em);
    }

    @Test
    @Transactional
    void createAdherencia() throws Exception {
        int databaseSizeBeforeCreate = adherenciaRepository.findAll().size();
        // Create the Adherencia
        restAdherenciaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adherencia))
            )
            .andExpect(status().isCreated());

        // Validate the Adherencia in the database
        List<Adherencia> adherenciaList = adherenciaRepository.findAll();
        assertThat(adherenciaList).hasSize(databaseSizeBeforeCreate + 1);
        Adherencia testAdherencia = adherenciaList.get(adherenciaList.size() - 1);
        assertThat(testAdherencia.getHoraToma()).isEqualTo(DEFAULT_HORA_TOMA);
        assertThat(testAdherencia.getRespuesta()).isEqualTo(DEFAULT_RESPUESTA);
        assertThat(testAdherencia.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testAdherencia.getComentario()).isEqualTo(DEFAULT_COMENTARIO);
    }

    @Test
    @Transactional
    void createAdherenciaWithExistingId() throws Exception {
        // Create the Adherencia with an existing ID
        adherencia.setId(1L);

        int databaseSizeBeforeCreate = adherenciaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdherenciaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adherencia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adherencia in the database
        List<Adherencia> adherenciaList = adherenciaRepository.findAll();
        assertThat(adherenciaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAdherencias() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        // Get all the adherenciaList
        restAdherenciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adherencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].horaToma").value(hasItem(DEFAULT_HORA_TOMA.toString())))
            .andExpect(jsonPath("$.[*].respuesta").value(hasItem(DEFAULT_RESPUESTA.booleanValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.[*].comentario").value(hasItem(DEFAULT_COMENTARIO)));
    }

    @Test
    @Transactional
    void getAdherencia() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        // Get the adherencia
        restAdherenciaMockMvc
            .perform(get(ENTITY_API_URL_ID, adherencia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(adherencia.getId().intValue()))
            .andExpect(jsonPath("$.horaToma").value(DEFAULT_HORA_TOMA.toString()))
            .andExpect(jsonPath("$.respuesta").value(DEFAULT_RESPUESTA.booleanValue()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR))
            .andExpect(jsonPath("$.comentario").value(DEFAULT_COMENTARIO));
    }

    @Test
    @Transactional
    void getAdherenciasByIdFiltering() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        Long id = adherencia.getId();

        defaultAdherenciaShouldBeFound("id.equals=" + id);
        defaultAdherenciaShouldNotBeFound("id.notEquals=" + id);

        defaultAdherenciaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAdherenciaShouldNotBeFound("id.greaterThan=" + id);

        defaultAdherenciaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAdherenciaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAdherenciasByHoraTomaIsEqualToSomething() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        // Get all the adherenciaList where horaToma equals to DEFAULT_HORA_TOMA
        defaultAdherenciaShouldBeFound("horaToma.equals=" + DEFAULT_HORA_TOMA);

        // Get all the adherenciaList where horaToma equals to UPDATED_HORA_TOMA
        defaultAdherenciaShouldNotBeFound("horaToma.equals=" + UPDATED_HORA_TOMA);
    }

    @Test
    @Transactional
    void getAllAdherenciasByHoraTomaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        // Get all the adherenciaList where horaToma not equals to DEFAULT_HORA_TOMA
        defaultAdherenciaShouldNotBeFound("horaToma.notEquals=" + DEFAULT_HORA_TOMA);

        // Get all the adherenciaList where horaToma not equals to UPDATED_HORA_TOMA
        defaultAdherenciaShouldBeFound("horaToma.notEquals=" + UPDATED_HORA_TOMA);
    }

    @Test
    @Transactional
    void getAllAdherenciasByHoraTomaIsInShouldWork() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        // Get all the adherenciaList where horaToma in DEFAULT_HORA_TOMA or UPDATED_HORA_TOMA
        defaultAdherenciaShouldBeFound("horaToma.in=" + DEFAULT_HORA_TOMA + "," + UPDATED_HORA_TOMA);

        // Get all the adherenciaList where horaToma equals to UPDATED_HORA_TOMA
        defaultAdherenciaShouldNotBeFound("horaToma.in=" + UPDATED_HORA_TOMA);
    }

    @Test
    @Transactional
    void getAllAdherenciasByHoraTomaIsNullOrNotNull() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        // Get all the adherenciaList where horaToma is not null
        defaultAdherenciaShouldBeFound("horaToma.specified=true");

        // Get all the adherenciaList where horaToma is null
        defaultAdherenciaShouldNotBeFound("horaToma.specified=false");
    }

    @Test
    @Transactional
    void getAllAdherenciasByRespuestaIsEqualToSomething() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        // Get all the adherenciaList where respuesta equals to DEFAULT_RESPUESTA
        defaultAdherenciaShouldBeFound("respuesta.equals=" + DEFAULT_RESPUESTA);

        // Get all the adherenciaList where respuesta equals to UPDATED_RESPUESTA
        defaultAdherenciaShouldNotBeFound("respuesta.equals=" + UPDATED_RESPUESTA);
    }

    @Test
    @Transactional
    void getAllAdherenciasByRespuestaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        // Get all the adherenciaList where respuesta not equals to DEFAULT_RESPUESTA
        defaultAdherenciaShouldNotBeFound("respuesta.notEquals=" + DEFAULT_RESPUESTA);

        // Get all the adherenciaList where respuesta not equals to UPDATED_RESPUESTA
        defaultAdherenciaShouldBeFound("respuesta.notEquals=" + UPDATED_RESPUESTA);
    }

    @Test
    @Transactional
    void getAllAdherenciasByRespuestaIsInShouldWork() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        // Get all the adherenciaList where respuesta in DEFAULT_RESPUESTA or UPDATED_RESPUESTA
        defaultAdherenciaShouldBeFound("respuesta.in=" + DEFAULT_RESPUESTA + "," + UPDATED_RESPUESTA);

        // Get all the adherenciaList where respuesta equals to UPDATED_RESPUESTA
        defaultAdherenciaShouldNotBeFound("respuesta.in=" + UPDATED_RESPUESTA);
    }

    @Test
    @Transactional
    void getAllAdherenciasByRespuestaIsNullOrNotNull() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        // Get all the adherenciaList where respuesta is not null
        defaultAdherenciaShouldBeFound("respuesta.specified=true");

        // Get all the adherenciaList where respuesta is null
        defaultAdherenciaShouldNotBeFound("respuesta.specified=false");
    }

    @Test
    @Transactional
    void getAllAdherenciasByValorIsEqualToSomething() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        // Get all the adherenciaList where valor equals to DEFAULT_VALOR
        defaultAdherenciaShouldBeFound("valor.equals=" + DEFAULT_VALOR);

        // Get all the adherenciaList where valor equals to UPDATED_VALOR
        defaultAdherenciaShouldNotBeFound("valor.equals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllAdherenciasByValorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        // Get all the adherenciaList where valor not equals to DEFAULT_VALOR
        defaultAdherenciaShouldNotBeFound("valor.notEquals=" + DEFAULT_VALOR);

        // Get all the adherenciaList where valor not equals to UPDATED_VALOR
        defaultAdherenciaShouldBeFound("valor.notEquals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllAdherenciasByValorIsInShouldWork() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        // Get all the adherenciaList where valor in DEFAULT_VALOR or UPDATED_VALOR
        defaultAdherenciaShouldBeFound("valor.in=" + DEFAULT_VALOR + "," + UPDATED_VALOR);

        // Get all the adherenciaList where valor equals to UPDATED_VALOR
        defaultAdherenciaShouldNotBeFound("valor.in=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllAdherenciasByValorIsNullOrNotNull() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        // Get all the adherenciaList where valor is not null
        defaultAdherenciaShouldBeFound("valor.specified=true");

        // Get all the adherenciaList where valor is null
        defaultAdherenciaShouldNotBeFound("valor.specified=false");
    }

    @Test
    @Transactional
    void getAllAdherenciasByValorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        // Get all the adherenciaList where valor is greater than or equal to DEFAULT_VALOR
        defaultAdherenciaShouldBeFound("valor.greaterThanOrEqual=" + DEFAULT_VALOR);

        // Get all the adherenciaList where valor is greater than or equal to UPDATED_VALOR
        defaultAdherenciaShouldNotBeFound("valor.greaterThanOrEqual=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllAdherenciasByValorIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        // Get all the adherenciaList where valor is less than or equal to DEFAULT_VALOR
        defaultAdherenciaShouldBeFound("valor.lessThanOrEqual=" + DEFAULT_VALOR);

        // Get all the adherenciaList where valor is less than or equal to SMALLER_VALOR
        defaultAdherenciaShouldNotBeFound("valor.lessThanOrEqual=" + SMALLER_VALOR);
    }

    @Test
    @Transactional
    void getAllAdherenciasByValorIsLessThanSomething() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        // Get all the adherenciaList where valor is less than DEFAULT_VALOR
        defaultAdherenciaShouldNotBeFound("valor.lessThan=" + DEFAULT_VALOR);

        // Get all the adherenciaList where valor is less than UPDATED_VALOR
        defaultAdherenciaShouldBeFound("valor.lessThan=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllAdherenciasByValorIsGreaterThanSomething() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        // Get all the adherenciaList where valor is greater than DEFAULT_VALOR
        defaultAdherenciaShouldNotBeFound("valor.greaterThan=" + DEFAULT_VALOR);

        // Get all the adherenciaList where valor is greater than SMALLER_VALOR
        defaultAdherenciaShouldBeFound("valor.greaterThan=" + SMALLER_VALOR);
    }

    @Test
    @Transactional
    void getAllAdherenciasByComentarioIsEqualToSomething() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        // Get all the adherenciaList where comentario equals to DEFAULT_COMENTARIO
        defaultAdherenciaShouldBeFound("comentario.equals=" + DEFAULT_COMENTARIO);

        // Get all the adherenciaList where comentario equals to UPDATED_COMENTARIO
        defaultAdherenciaShouldNotBeFound("comentario.equals=" + UPDATED_COMENTARIO);
    }

    @Test
    @Transactional
    void getAllAdherenciasByComentarioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        // Get all the adherenciaList where comentario not equals to DEFAULT_COMENTARIO
        defaultAdherenciaShouldNotBeFound("comentario.notEquals=" + DEFAULT_COMENTARIO);

        // Get all the adherenciaList where comentario not equals to UPDATED_COMENTARIO
        defaultAdherenciaShouldBeFound("comentario.notEquals=" + UPDATED_COMENTARIO);
    }

    @Test
    @Transactional
    void getAllAdherenciasByComentarioIsInShouldWork() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        // Get all the adherenciaList where comentario in DEFAULT_COMENTARIO or UPDATED_COMENTARIO
        defaultAdherenciaShouldBeFound("comentario.in=" + DEFAULT_COMENTARIO + "," + UPDATED_COMENTARIO);

        // Get all the adherenciaList where comentario equals to UPDATED_COMENTARIO
        defaultAdherenciaShouldNotBeFound("comentario.in=" + UPDATED_COMENTARIO);
    }

    @Test
    @Transactional
    void getAllAdherenciasByComentarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        // Get all the adherenciaList where comentario is not null
        defaultAdherenciaShouldBeFound("comentario.specified=true");

        // Get all the adherenciaList where comentario is null
        defaultAdherenciaShouldNotBeFound("comentario.specified=false");
    }

    @Test
    @Transactional
    void getAllAdherenciasByComentarioContainsSomething() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        // Get all the adherenciaList where comentario contains DEFAULT_COMENTARIO
        defaultAdherenciaShouldBeFound("comentario.contains=" + DEFAULT_COMENTARIO);

        // Get all the adherenciaList where comentario contains UPDATED_COMENTARIO
        defaultAdherenciaShouldNotBeFound("comentario.contains=" + UPDATED_COMENTARIO);
    }

    @Test
    @Transactional
    void getAllAdherenciasByComentarioNotContainsSomething() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        // Get all the adherenciaList where comentario does not contain DEFAULT_COMENTARIO
        defaultAdherenciaShouldNotBeFound("comentario.doesNotContain=" + DEFAULT_COMENTARIO);

        // Get all the adherenciaList where comentario does not contain UPDATED_COMENTARIO
        defaultAdherenciaShouldBeFound("comentario.doesNotContain=" + UPDATED_COMENTARIO);
    }

    @Test
    @Transactional
    void getAllAdherenciasByMedicamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);
        Medicamento medicamento = MedicamentoResourceIT.createEntity(em);
        em.persist(medicamento);
        em.flush();
        adherencia.setMedicamento(medicamento);
        adherenciaRepository.saveAndFlush(adherencia);
        Long medicamentoId = medicamento.getId();

        // Get all the adherenciaList where medicamento equals to medicamentoId
        defaultAdherenciaShouldBeFound("medicamentoId.equals=" + medicamentoId);

        // Get all the adherenciaList where medicamento equals to (medicamentoId + 1)
        defaultAdherenciaShouldNotBeFound("medicamentoId.equals=" + (medicamentoId + 1));
    }

    @Test
    @Transactional
    void getAllAdherenciasByPacienteIsEqualToSomething() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);
        Paciente paciente = PacienteResourceIT.createEntity(em);
        em.persist(paciente);
        em.flush();
        adherencia.setPaciente(paciente);
        adherenciaRepository.saveAndFlush(adherencia);
        Long pacienteId = paciente.getId();

        // Get all the adherenciaList where paciente equals to pacienteId
        defaultAdherenciaShouldBeFound("pacienteId.equals=" + pacienteId);

        // Get all the adherenciaList where paciente equals to (pacienteId + 1)
        defaultAdherenciaShouldNotBeFound("pacienteId.equals=" + (pacienteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAdherenciaShouldBeFound(String filter) throws Exception {
        restAdherenciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adherencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].horaToma").value(hasItem(DEFAULT_HORA_TOMA.toString())))
            .andExpect(jsonPath("$.[*].respuesta").value(hasItem(DEFAULT_RESPUESTA.booleanValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.[*].comentario").value(hasItem(DEFAULT_COMENTARIO)));

        // Check, that the count call also returns 1
        restAdherenciaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAdherenciaShouldNotBeFound(String filter) throws Exception {
        restAdherenciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAdherenciaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAdherencia() throws Exception {
        // Get the adherencia
        restAdherenciaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAdherencia() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        int databaseSizeBeforeUpdate = adherenciaRepository.findAll().size();

        // Update the adherencia
        Adherencia updatedAdherencia = adherenciaRepository.findById(adherencia.getId()).get();
        // Disconnect from session so that the updates on updatedAdherencia are not directly saved in db
        em.detach(updatedAdherencia);
        updatedAdherencia.horaToma(UPDATED_HORA_TOMA).respuesta(UPDATED_RESPUESTA).valor(UPDATED_VALOR).comentario(UPDATED_COMENTARIO);

        restAdherenciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAdherencia.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAdherencia))
            )
            .andExpect(status().isOk());

        // Validate the Adherencia in the database
        List<Adherencia> adherenciaList = adherenciaRepository.findAll();
        assertThat(adherenciaList).hasSize(databaseSizeBeforeUpdate);
        Adherencia testAdherencia = adherenciaList.get(adherenciaList.size() - 1);
        assertThat(testAdherencia.getHoraToma()).isEqualTo(UPDATED_HORA_TOMA);
        assertThat(testAdherencia.getRespuesta()).isEqualTo(UPDATED_RESPUESTA);
        assertThat(testAdherencia.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testAdherencia.getComentario()).isEqualTo(UPDATED_COMENTARIO);
    }

    @Test
    @Transactional
    void putNonExistingAdherencia() throws Exception {
        int databaseSizeBeforeUpdate = adherenciaRepository.findAll().size();
        adherencia.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdherenciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adherencia.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adherencia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adherencia in the database
        List<Adherencia> adherenciaList = adherenciaRepository.findAll();
        assertThat(adherenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdherencia() throws Exception {
        int databaseSizeBeforeUpdate = adherenciaRepository.findAll().size();
        adherencia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdherenciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adherencia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adherencia in the database
        List<Adherencia> adherenciaList = adherenciaRepository.findAll();
        assertThat(adherenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdherencia() throws Exception {
        int databaseSizeBeforeUpdate = adherenciaRepository.findAll().size();
        adherencia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdherenciaMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adherencia))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Adherencia in the database
        List<Adherencia> adherenciaList = adherenciaRepository.findAll();
        assertThat(adherenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdherenciaWithPatch() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        int databaseSizeBeforeUpdate = adherenciaRepository.findAll().size();

        // Update the adherencia using partial update
        Adherencia partialUpdatedAdherencia = new Adherencia();
        partialUpdatedAdherencia.setId(adherencia.getId());

        partialUpdatedAdherencia.horaToma(UPDATED_HORA_TOMA).respuesta(UPDATED_RESPUESTA);

        restAdherenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdherencia.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdherencia))
            )
            .andExpect(status().isOk());

        // Validate the Adherencia in the database
        List<Adherencia> adherenciaList = adherenciaRepository.findAll();
        assertThat(adherenciaList).hasSize(databaseSizeBeforeUpdate);
        Adherencia testAdherencia = adherenciaList.get(adherenciaList.size() - 1);
        assertThat(testAdherencia.getHoraToma()).isEqualTo(UPDATED_HORA_TOMA);
        assertThat(testAdherencia.getRespuesta()).isEqualTo(UPDATED_RESPUESTA);
        assertThat(testAdherencia.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testAdherencia.getComentario()).isEqualTo(DEFAULT_COMENTARIO);
    }

    @Test
    @Transactional
    void fullUpdateAdherenciaWithPatch() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        int databaseSizeBeforeUpdate = adherenciaRepository.findAll().size();

        // Update the adherencia using partial update
        Adherencia partialUpdatedAdherencia = new Adherencia();
        partialUpdatedAdherencia.setId(adherencia.getId());

        partialUpdatedAdherencia
            .horaToma(UPDATED_HORA_TOMA)
            .respuesta(UPDATED_RESPUESTA)
            .valor(UPDATED_VALOR)
            .comentario(UPDATED_COMENTARIO);

        restAdherenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdherencia.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdherencia))
            )
            .andExpect(status().isOk());

        // Validate the Adherencia in the database
        List<Adherencia> adherenciaList = adherenciaRepository.findAll();
        assertThat(adherenciaList).hasSize(databaseSizeBeforeUpdate);
        Adherencia testAdherencia = adherenciaList.get(adherenciaList.size() - 1);
        assertThat(testAdherencia.getHoraToma()).isEqualTo(UPDATED_HORA_TOMA);
        assertThat(testAdherencia.getRespuesta()).isEqualTo(UPDATED_RESPUESTA);
        assertThat(testAdherencia.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testAdherencia.getComentario()).isEqualTo(UPDATED_COMENTARIO);
    }

    @Test
    @Transactional
    void patchNonExistingAdherencia() throws Exception {
        int databaseSizeBeforeUpdate = adherenciaRepository.findAll().size();
        adherencia.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdherenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, adherencia.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adherencia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adherencia in the database
        List<Adherencia> adherenciaList = adherenciaRepository.findAll();
        assertThat(adherenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdherencia() throws Exception {
        int databaseSizeBeforeUpdate = adherenciaRepository.findAll().size();
        adherencia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdherenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adherencia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adherencia in the database
        List<Adherencia> adherenciaList = adherenciaRepository.findAll();
        assertThat(adherenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdherencia() throws Exception {
        int databaseSizeBeforeUpdate = adherenciaRepository.findAll().size();
        adherencia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdherenciaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adherencia))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Adherencia in the database
        List<Adherencia> adherenciaList = adherenciaRepository.findAll();
        assertThat(adherenciaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdherencia() throws Exception {
        // Initialize the database
        adherenciaRepository.saveAndFlush(adherencia);

        int databaseSizeBeforeDelete = adherenciaRepository.findAll().size();

        // Delete the adherencia
        restAdherenciaMockMvc
            .perform(delete(ENTITY_API_URL_ID, adherencia.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Adherencia> adherenciaList = adherenciaRepository.findAll();
        assertThat(adherenciaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.be4tech.becare3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.Medicamento;
import com.be4tech.becare3.domain.enumeration.Presentacion;
import com.be4tech.becare3.repository.MedicamentoRepository;
import com.be4tech.becare3.service.criteria.MedicamentoCriteria;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link MedicamentoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MedicamentoResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_INGRESO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_INGRESO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Presentacion DEFAULT_PRESENTACION = Presentacion.Comprimido;
    private static final Presentacion UPDATED_PRESENTACION = Presentacion.Gragea;

    private static final String DEFAULT_GENERICO = "AAAAAAAAAA";
    private static final String UPDATED_GENERICO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/medicamentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicamentoMockMvc;

    private Medicamento medicamento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medicamento createEntity(EntityManager em) {
        Medicamento medicamento = new Medicamento()
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .fechaIngreso(DEFAULT_FECHA_INGRESO)
            .presentacion(DEFAULT_PRESENTACION)
            .generico(DEFAULT_GENERICO);
        return medicamento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medicamento createUpdatedEntity(EntityManager em) {
        Medicamento medicamento = new Medicamento()
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaIngreso(UPDATED_FECHA_INGRESO)
            .presentacion(UPDATED_PRESENTACION)
            .generico(UPDATED_GENERICO);
        return medicamento;
    }

    @BeforeEach
    public void initTest() {
        medicamento = createEntity(em);
    }

    @Test
    @Transactional
    void createMedicamento() throws Exception {
        int databaseSizeBeforeCreate = medicamentoRepository.findAll().size();
        // Create the Medicamento
        restMedicamentoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medicamento))
            )
            .andExpect(status().isCreated());

        // Validate the Medicamento in the database
        List<Medicamento> medicamentoList = medicamentoRepository.findAll();
        assertThat(medicamentoList).hasSize(databaseSizeBeforeCreate + 1);
        Medicamento testMedicamento = medicamentoList.get(medicamentoList.size() - 1);
        assertThat(testMedicamento.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testMedicamento.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testMedicamento.getFechaIngreso()).isEqualTo(DEFAULT_FECHA_INGRESO);
        assertThat(testMedicamento.getPresentacion()).isEqualTo(DEFAULT_PRESENTACION);
        assertThat(testMedicamento.getGenerico()).isEqualTo(DEFAULT_GENERICO);
    }

    @Test
    @Transactional
    void createMedicamentoWithExistingId() throws Exception {
        // Create the Medicamento with an existing ID
        medicamento.setId(1L);

        int databaseSizeBeforeCreate = medicamentoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicamentoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medicamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicamento in the database
        List<Medicamento> medicamentoList = medicamentoRepository.findAll();
        assertThat(medicamentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMedicamentos() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        // Get all the medicamentoList
        restMedicamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].fechaIngreso").value(hasItem(DEFAULT_FECHA_INGRESO.toString())))
            .andExpect(jsonPath("$.[*].presentacion").value(hasItem(DEFAULT_PRESENTACION.toString())))
            .andExpect(jsonPath("$.[*].generico").value(hasItem(DEFAULT_GENERICO.toString())));
    }

    @Test
    @Transactional
    void getMedicamento() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        // Get the medicamento
        restMedicamentoMockMvc
            .perform(get(ENTITY_API_URL_ID, medicamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicamento.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.fechaIngreso").value(DEFAULT_FECHA_INGRESO.toString()))
            .andExpect(jsonPath("$.presentacion").value(DEFAULT_PRESENTACION.toString()))
            .andExpect(jsonPath("$.generico").value(DEFAULT_GENERICO.toString()));
    }

    @Test
    @Transactional
    void getMedicamentosByIdFiltering() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        Long id = medicamento.getId();

        defaultMedicamentoShouldBeFound("id.equals=" + id);
        defaultMedicamentoShouldNotBeFound("id.notEquals=" + id);

        defaultMedicamentoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMedicamentoShouldNotBeFound("id.greaterThan=" + id);

        defaultMedicamentoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMedicamentoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMedicamentosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        // Get all the medicamentoList where nombre equals to DEFAULT_NOMBRE
        defaultMedicamentoShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the medicamentoList where nombre equals to UPDATED_NOMBRE
        defaultMedicamentoShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllMedicamentosByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        // Get all the medicamentoList where nombre not equals to DEFAULT_NOMBRE
        defaultMedicamentoShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the medicamentoList where nombre not equals to UPDATED_NOMBRE
        defaultMedicamentoShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllMedicamentosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        // Get all the medicamentoList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultMedicamentoShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the medicamentoList where nombre equals to UPDATED_NOMBRE
        defaultMedicamentoShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllMedicamentosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        // Get all the medicamentoList where nombre is not null
        defaultMedicamentoShouldBeFound("nombre.specified=true");

        // Get all the medicamentoList where nombre is null
        defaultMedicamentoShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicamentosByNombreContainsSomething() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        // Get all the medicamentoList where nombre contains DEFAULT_NOMBRE
        defaultMedicamentoShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the medicamentoList where nombre contains UPDATED_NOMBRE
        defaultMedicamentoShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllMedicamentosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        // Get all the medicamentoList where nombre does not contain DEFAULT_NOMBRE
        defaultMedicamentoShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the medicamentoList where nombre does not contain UPDATED_NOMBRE
        defaultMedicamentoShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllMedicamentosByFechaIngresoIsEqualToSomething() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        // Get all the medicamentoList where fechaIngreso equals to DEFAULT_FECHA_INGRESO
        defaultMedicamentoShouldBeFound("fechaIngreso.equals=" + DEFAULT_FECHA_INGRESO);

        // Get all the medicamentoList where fechaIngreso equals to UPDATED_FECHA_INGRESO
        defaultMedicamentoShouldNotBeFound("fechaIngreso.equals=" + UPDATED_FECHA_INGRESO);
    }

    @Test
    @Transactional
    void getAllMedicamentosByFechaIngresoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        // Get all the medicamentoList where fechaIngreso not equals to DEFAULT_FECHA_INGRESO
        defaultMedicamentoShouldNotBeFound("fechaIngreso.notEquals=" + DEFAULT_FECHA_INGRESO);

        // Get all the medicamentoList where fechaIngreso not equals to UPDATED_FECHA_INGRESO
        defaultMedicamentoShouldBeFound("fechaIngreso.notEquals=" + UPDATED_FECHA_INGRESO);
    }

    @Test
    @Transactional
    void getAllMedicamentosByFechaIngresoIsInShouldWork() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        // Get all the medicamentoList where fechaIngreso in DEFAULT_FECHA_INGRESO or UPDATED_FECHA_INGRESO
        defaultMedicamentoShouldBeFound("fechaIngreso.in=" + DEFAULT_FECHA_INGRESO + "," + UPDATED_FECHA_INGRESO);

        // Get all the medicamentoList where fechaIngreso equals to UPDATED_FECHA_INGRESO
        defaultMedicamentoShouldNotBeFound("fechaIngreso.in=" + UPDATED_FECHA_INGRESO);
    }

    @Test
    @Transactional
    void getAllMedicamentosByFechaIngresoIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        // Get all the medicamentoList where fechaIngreso is not null
        defaultMedicamentoShouldBeFound("fechaIngreso.specified=true");

        // Get all the medicamentoList where fechaIngreso is null
        defaultMedicamentoShouldNotBeFound("fechaIngreso.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicamentosByPresentacionIsEqualToSomething() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        // Get all the medicamentoList where presentacion equals to DEFAULT_PRESENTACION
        defaultMedicamentoShouldBeFound("presentacion.equals=" + DEFAULT_PRESENTACION);

        // Get all the medicamentoList where presentacion equals to UPDATED_PRESENTACION
        defaultMedicamentoShouldNotBeFound("presentacion.equals=" + UPDATED_PRESENTACION);
    }

    @Test
    @Transactional
    void getAllMedicamentosByPresentacionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        // Get all the medicamentoList where presentacion not equals to DEFAULT_PRESENTACION
        defaultMedicamentoShouldNotBeFound("presentacion.notEquals=" + DEFAULT_PRESENTACION);

        // Get all the medicamentoList where presentacion not equals to UPDATED_PRESENTACION
        defaultMedicamentoShouldBeFound("presentacion.notEquals=" + UPDATED_PRESENTACION);
    }

    @Test
    @Transactional
    void getAllMedicamentosByPresentacionIsInShouldWork() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        // Get all the medicamentoList where presentacion in DEFAULT_PRESENTACION or UPDATED_PRESENTACION
        defaultMedicamentoShouldBeFound("presentacion.in=" + DEFAULT_PRESENTACION + "," + UPDATED_PRESENTACION);

        // Get all the medicamentoList where presentacion equals to UPDATED_PRESENTACION
        defaultMedicamentoShouldNotBeFound("presentacion.in=" + UPDATED_PRESENTACION);
    }

    @Test
    @Transactional
    void getAllMedicamentosByPresentacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        // Get all the medicamentoList where presentacion is not null
        defaultMedicamentoShouldBeFound("presentacion.specified=true");

        // Get all the medicamentoList where presentacion is null
        defaultMedicamentoShouldNotBeFound("presentacion.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMedicamentoShouldBeFound(String filter) throws Exception {
        restMedicamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
            .andExpect(jsonPath("$.[*].fechaIngreso").value(hasItem(DEFAULT_FECHA_INGRESO.toString())))
            .andExpect(jsonPath("$.[*].presentacion").value(hasItem(DEFAULT_PRESENTACION.toString())))
            .andExpect(jsonPath("$.[*].generico").value(hasItem(DEFAULT_GENERICO.toString())));

        // Check, that the count call also returns 1
        restMedicamentoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMedicamentoShouldNotBeFound(String filter) throws Exception {
        restMedicamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMedicamentoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMedicamento() throws Exception {
        // Get the medicamento
        restMedicamentoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMedicamento() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        int databaseSizeBeforeUpdate = medicamentoRepository.findAll().size();

        // Update the medicamento
        Medicamento updatedMedicamento = medicamentoRepository.findById(medicamento.getId()).get();
        // Disconnect from session so that the updates on updatedMedicamento are not directly saved in db
        em.detach(updatedMedicamento);
        updatedMedicamento
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaIngreso(UPDATED_FECHA_INGRESO)
            .presentacion(UPDATED_PRESENTACION)
            .generico(UPDATED_GENERICO);

        restMedicamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMedicamento.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMedicamento))
            )
            .andExpect(status().isOk());

        // Validate the Medicamento in the database
        List<Medicamento> medicamentoList = medicamentoRepository.findAll();
        assertThat(medicamentoList).hasSize(databaseSizeBeforeUpdate);
        Medicamento testMedicamento = medicamentoList.get(medicamentoList.size() - 1);
        assertThat(testMedicamento.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testMedicamento.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testMedicamento.getFechaIngreso()).isEqualTo(UPDATED_FECHA_INGRESO);
        assertThat(testMedicamento.getPresentacion()).isEqualTo(UPDATED_PRESENTACION);
        assertThat(testMedicamento.getGenerico()).isEqualTo(UPDATED_GENERICO);
    }

    @Test
    @Transactional
    void putNonExistingMedicamento() throws Exception {
        int databaseSizeBeforeUpdate = medicamentoRepository.findAll().size();
        medicamento.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicamento.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medicamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicamento in the database
        List<Medicamento> medicamentoList = medicamentoRepository.findAll();
        assertThat(medicamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedicamento() throws Exception {
        int databaseSizeBeforeUpdate = medicamentoRepository.findAll().size();
        medicamento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medicamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicamento in the database
        List<Medicamento> medicamentoList = medicamentoRepository.findAll();
        assertThat(medicamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedicamento() throws Exception {
        int databaseSizeBeforeUpdate = medicamentoRepository.findAll().size();
        medicamento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicamentoMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medicamento))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medicamento in the database
        List<Medicamento> medicamentoList = medicamentoRepository.findAll();
        assertThat(medicamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicamentoWithPatch() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        int databaseSizeBeforeUpdate = medicamentoRepository.findAll().size();

        // Update the medicamento using partial update
        Medicamento partialUpdatedMedicamento = new Medicamento();
        partialUpdatedMedicamento.setId(medicamento.getId());

        partialUpdatedMedicamento.descripcion(UPDATED_DESCRIPCION);

        restMedicamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicamento.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedicamento))
            )
            .andExpect(status().isOk());

        // Validate the Medicamento in the database
        List<Medicamento> medicamentoList = medicamentoRepository.findAll();
        assertThat(medicamentoList).hasSize(databaseSizeBeforeUpdate);
        Medicamento testMedicamento = medicamentoList.get(medicamentoList.size() - 1);
        assertThat(testMedicamento.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testMedicamento.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testMedicamento.getFechaIngreso()).isEqualTo(DEFAULT_FECHA_INGRESO);
        assertThat(testMedicamento.getPresentacion()).isEqualTo(DEFAULT_PRESENTACION);
        assertThat(testMedicamento.getGenerico()).isEqualTo(DEFAULT_GENERICO);
    }

    @Test
    @Transactional
    void fullUpdateMedicamentoWithPatch() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        int databaseSizeBeforeUpdate = medicamentoRepository.findAll().size();

        // Update the medicamento using partial update
        Medicamento partialUpdatedMedicamento = new Medicamento();
        partialUpdatedMedicamento.setId(medicamento.getId());

        partialUpdatedMedicamento
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaIngreso(UPDATED_FECHA_INGRESO)
            .presentacion(UPDATED_PRESENTACION)
            .generico(UPDATED_GENERICO);

        restMedicamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicamento.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedicamento))
            )
            .andExpect(status().isOk());

        // Validate the Medicamento in the database
        List<Medicamento> medicamentoList = medicamentoRepository.findAll();
        assertThat(medicamentoList).hasSize(databaseSizeBeforeUpdate);
        Medicamento testMedicamento = medicamentoList.get(medicamentoList.size() - 1);
        assertThat(testMedicamento.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testMedicamento.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testMedicamento.getFechaIngreso()).isEqualTo(UPDATED_FECHA_INGRESO);
        assertThat(testMedicamento.getPresentacion()).isEqualTo(UPDATED_PRESENTACION);
        assertThat(testMedicamento.getGenerico()).isEqualTo(UPDATED_GENERICO);
    }

    @Test
    @Transactional
    void patchNonExistingMedicamento() throws Exception {
        int databaseSizeBeforeUpdate = medicamentoRepository.findAll().size();
        medicamento.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicamento.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(medicamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicamento in the database
        List<Medicamento> medicamentoList = medicamentoRepository.findAll();
        assertThat(medicamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedicamento() throws Exception {
        int databaseSizeBeforeUpdate = medicamentoRepository.findAll().size();
        medicamento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(medicamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicamento in the database
        List<Medicamento> medicamentoList = medicamentoRepository.findAll();
        assertThat(medicamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedicamento() throws Exception {
        int databaseSizeBeforeUpdate = medicamentoRepository.findAll().size();
        medicamento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicamentoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(medicamento))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medicamento in the database
        List<Medicamento> medicamentoList = medicamentoRepository.findAll();
        assertThat(medicamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedicamento() throws Exception {
        // Initialize the database
        medicamentoRepository.saveAndFlush(medicamento);

        int databaseSizeBeforeDelete = medicamentoRepository.findAll().size();

        // Delete the medicamento
        restMedicamentoMockMvc
            .perform(delete(ENTITY_API_URL_ID, medicamento.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Medicamento> medicamentoList = medicamentoRepository.findAll();
        assertThat(medicamentoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

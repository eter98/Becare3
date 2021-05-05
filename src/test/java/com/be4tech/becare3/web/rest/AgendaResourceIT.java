package com.be4tech.becare3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.Agenda;
import com.be4tech.becare3.domain.Medicamento;
import com.be4tech.becare3.repository.AgendaRepository;
import com.be4tech.becare3.service.criteria.AgendaCriteria;
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
 * Integration tests for the {@link AgendaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AgendaResourceIT {

    private static final Integer DEFAULT_HORA_MEDICAMENTO = 1;
    private static final Integer UPDATED_HORA_MEDICAMENTO = 2;
    private static final Integer SMALLER_HORA_MEDICAMENTO = 1 - 1;

    private static final String ENTITY_API_URL = "/api/agenda";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgendaMockMvc;

    private Agenda agenda;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agenda createEntity(EntityManager em) {
        Agenda agenda = new Agenda().horaMedicamento(DEFAULT_HORA_MEDICAMENTO);
        return agenda;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agenda createUpdatedEntity(EntityManager em) {
        Agenda agenda = new Agenda().horaMedicamento(UPDATED_HORA_MEDICAMENTO);
        return agenda;
    }

    @BeforeEach
    public void initTest() {
        agenda = createEntity(em);
    }

    @Test
    @Transactional
    void createAgenda() throws Exception {
        int databaseSizeBeforeCreate = agendaRepository.findAll().size();
        // Create the Agenda
        restAgendaMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agenda))
            )
            .andExpect(status().isCreated());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeCreate + 1);
        Agenda testAgenda = agendaList.get(agendaList.size() - 1);
        assertThat(testAgenda.getHoraMedicamento()).isEqualTo(DEFAULT_HORA_MEDICAMENTO);
    }

    @Test
    @Transactional
    void createAgendaWithExistingId() throws Exception {
        // Create the Agenda with an existing ID
        agenda.setId(1L);

        int databaseSizeBeforeCreate = agendaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgendaMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agenda))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAgenda() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        // Get all the agendaList
        restAgendaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agenda.getId().intValue())))
            .andExpect(jsonPath("$.[*].horaMedicamento").value(hasItem(DEFAULT_HORA_MEDICAMENTO)));
    }

    @Test
    @Transactional
    void getAgenda() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        // Get the agenda
        restAgendaMockMvc
            .perform(get(ENTITY_API_URL_ID, agenda.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agenda.getId().intValue()))
            .andExpect(jsonPath("$.horaMedicamento").value(DEFAULT_HORA_MEDICAMENTO));
    }

    @Test
    @Transactional
    void getAgendaByIdFiltering() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        Long id = agenda.getId();

        defaultAgendaShouldBeFound("id.equals=" + id);
        defaultAgendaShouldNotBeFound("id.notEquals=" + id);

        defaultAgendaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAgendaShouldNotBeFound("id.greaterThan=" + id);

        defaultAgendaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAgendaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAgendaByHoraMedicamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        // Get all the agendaList where horaMedicamento equals to DEFAULT_HORA_MEDICAMENTO
        defaultAgendaShouldBeFound("horaMedicamento.equals=" + DEFAULT_HORA_MEDICAMENTO);

        // Get all the agendaList where horaMedicamento equals to UPDATED_HORA_MEDICAMENTO
        defaultAgendaShouldNotBeFound("horaMedicamento.equals=" + UPDATED_HORA_MEDICAMENTO);
    }

    @Test
    @Transactional
    void getAllAgendaByHoraMedicamentoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        // Get all the agendaList where horaMedicamento not equals to DEFAULT_HORA_MEDICAMENTO
        defaultAgendaShouldNotBeFound("horaMedicamento.notEquals=" + DEFAULT_HORA_MEDICAMENTO);

        // Get all the agendaList where horaMedicamento not equals to UPDATED_HORA_MEDICAMENTO
        defaultAgendaShouldBeFound("horaMedicamento.notEquals=" + UPDATED_HORA_MEDICAMENTO);
    }

    @Test
    @Transactional
    void getAllAgendaByHoraMedicamentoIsInShouldWork() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        // Get all the agendaList where horaMedicamento in DEFAULT_HORA_MEDICAMENTO or UPDATED_HORA_MEDICAMENTO
        defaultAgendaShouldBeFound("horaMedicamento.in=" + DEFAULT_HORA_MEDICAMENTO + "," + UPDATED_HORA_MEDICAMENTO);

        // Get all the agendaList where horaMedicamento equals to UPDATED_HORA_MEDICAMENTO
        defaultAgendaShouldNotBeFound("horaMedicamento.in=" + UPDATED_HORA_MEDICAMENTO);
    }

    @Test
    @Transactional
    void getAllAgendaByHoraMedicamentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        // Get all the agendaList where horaMedicamento is not null
        defaultAgendaShouldBeFound("horaMedicamento.specified=true");

        // Get all the agendaList where horaMedicamento is null
        defaultAgendaShouldNotBeFound("horaMedicamento.specified=false");
    }

    @Test
    @Transactional
    void getAllAgendaByHoraMedicamentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        // Get all the agendaList where horaMedicamento is greater than or equal to DEFAULT_HORA_MEDICAMENTO
        defaultAgendaShouldBeFound("horaMedicamento.greaterThanOrEqual=" + DEFAULT_HORA_MEDICAMENTO);

        // Get all the agendaList where horaMedicamento is greater than or equal to UPDATED_HORA_MEDICAMENTO
        defaultAgendaShouldNotBeFound("horaMedicamento.greaterThanOrEqual=" + UPDATED_HORA_MEDICAMENTO);
    }

    @Test
    @Transactional
    void getAllAgendaByHoraMedicamentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        // Get all the agendaList where horaMedicamento is less than or equal to DEFAULT_HORA_MEDICAMENTO
        defaultAgendaShouldBeFound("horaMedicamento.lessThanOrEqual=" + DEFAULT_HORA_MEDICAMENTO);

        // Get all the agendaList where horaMedicamento is less than or equal to SMALLER_HORA_MEDICAMENTO
        defaultAgendaShouldNotBeFound("horaMedicamento.lessThanOrEqual=" + SMALLER_HORA_MEDICAMENTO);
    }

    @Test
    @Transactional
    void getAllAgendaByHoraMedicamentoIsLessThanSomething() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        // Get all the agendaList where horaMedicamento is less than DEFAULT_HORA_MEDICAMENTO
        defaultAgendaShouldNotBeFound("horaMedicamento.lessThan=" + DEFAULT_HORA_MEDICAMENTO);

        // Get all the agendaList where horaMedicamento is less than UPDATED_HORA_MEDICAMENTO
        defaultAgendaShouldBeFound("horaMedicamento.lessThan=" + UPDATED_HORA_MEDICAMENTO);
    }

    @Test
    @Transactional
    void getAllAgendaByHoraMedicamentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        // Get all the agendaList where horaMedicamento is greater than DEFAULT_HORA_MEDICAMENTO
        defaultAgendaShouldNotBeFound("horaMedicamento.greaterThan=" + DEFAULT_HORA_MEDICAMENTO);

        // Get all the agendaList where horaMedicamento is greater than SMALLER_HORA_MEDICAMENTO
        defaultAgendaShouldBeFound("horaMedicamento.greaterThan=" + SMALLER_HORA_MEDICAMENTO);
    }

    @Test
    @Transactional
    void getAllAgendaByMedicamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);
        Medicamento medicamento = MedicamentoResourceIT.createEntity(em);
        em.persist(medicamento);
        em.flush();
        agenda.setMedicamento(medicamento);
        agendaRepository.saveAndFlush(agenda);
        Long medicamentoId = medicamento.getId();

        // Get all the agendaList where medicamento equals to medicamentoId
        defaultAgendaShouldBeFound("medicamentoId.equals=" + medicamentoId);

        // Get all the agendaList where medicamento equals to (medicamentoId + 1)
        defaultAgendaShouldNotBeFound("medicamentoId.equals=" + (medicamentoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAgendaShouldBeFound(String filter) throws Exception {
        restAgendaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agenda.getId().intValue())))
            .andExpect(jsonPath("$.[*].horaMedicamento").value(hasItem(DEFAULT_HORA_MEDICAMENTO)));

        // Check, that the count call also returns 1
        restAgendaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAgendaShouldNotBeFound(String filter) throws Exception {
        restAgendaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAgendaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAgenda() throws Exception {
        // Get the agenda
        restAgendaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAgenda() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        int databaseSizeBeforeUpdate = agendaRepository.findAll().size();

        // Update the agenda
        Agenda updatedAgenda = agendaRepository.findById(agenda.getId()).get();
        // Disconnect from session so that the updates on updatedAgenda are not directly saved in db
        em.detach(updatedAgenda);
        updatedAgenda.horaMedicamento(UPDATED_HORA_MEDICAMENTO);

        restAgendaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAgenda.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAgenda))
            )
            .andExpect(status().isOk());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeUpdate);
        Agenda testAgenda = agendaList.get(agendaList.size() - 1);
        assertThat(testAgenda.getHoraMedicamento()).isEqualTo(UPDATED_HORA_MEDICAMENTO);
    }

    @Test
    @Transactional
    void putNonExistingAgenda() throws Exception {
        int databaseSizeBeforeUpdate = agendaRepository.findAll().size();
        agenda.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgendaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agenda.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agenda))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgenda() throws Exception {
        int databaseSizeBeforeUpdate = agendaRepository.findAll().size();
        agenda.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgendaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agenda))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgenda() throws Exception {
        int databaseSizeBeforeUpdate = agendaRepository.findAll().size();
        agenda.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgendaMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agenda))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAgendaWithPatch() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        int databaseSizeBeforeUpdate = agendaRepository.findAll().size();

        // Update the agenda using partial update
        Agenda partialUpdatedAgenda = new Agenda();
        partialUpdatedAgenda.setId(agenda.getId());

        partialUpdatedAgenda.horaMedicamento(UPDATED_HORA_MEDICAMENTO);

        restAgendaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgenda.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAgenda))
            )
            .andExpect(status().isOk());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeUpdate);
        Agenda testAgenda = agendaList.get(agendaList.size() - 1);
        assertThat(testAgenda.getHoraMedicamento()).isEqualTo(UPDATED_HORA_MEDICAMENTO);
    }

    @Test
    @Transactional
    void fullUpdateAgendaWithPatch() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        int databaseSizeBeforeUpdate = agendaRepository.findAll().size();

        // Update the agenda using partial update
        Agenda partialUpdatedAgenda = new Agenda();
        partialUpdatedAgenda.setId(agenda.getId());

        partialUpdatedAgenda.horaMedicamento(UPDATED_HORA_MEDICAMENTO);

        restAgendaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgenda.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAgenda))
            )
            .andExpect(status().isOk());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeUpdate);
        Agenda testAgenda = agendaList.get(agendaList.size() - 1);
        assertThat(testAgenda.getHoraMedicamento()).isEqualTo(UPDATED_HORA_MEDICAMENTO);
    }

    @Test
    @Transactional
    void patchNonExistingAgenda() throws Exception {
        int databaseSizeBeforeUpdate = agendaRepository.findAll().size();
        agenda.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgendaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agenda.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agenda))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgenda() throws Exception {
        int databaseSizeBeforeUpdate = agendaRepository.findAll().size();
        agenda.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgendaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agenda))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgenda() throws Exception {
        int databaseSizeBeforeUpdate = agendaRepository.findAll().size();
        agenda.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgendaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agenda))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAgenda() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        int databaseSizeBeforeDelete = agendaRepository.findAll().size();

        // Delete the agenda
        restAgendaMockMvc
            .perform(delete(ENTITY_API_URL_ID, agenda.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

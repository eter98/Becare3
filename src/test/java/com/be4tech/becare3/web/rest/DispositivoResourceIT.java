package com.be4tech.becare3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.Dispositivo;
import com.be4tech.becare3.domain.User;
import com.be4tech.becare3.repository.DispositivoRepository;
import com.be4tech.becare3.service.criteria.DispositivoCriteria;
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
 * Integration tests for the {@link DispositivoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DispositivoResourceIT {

    private static final String DEFAULT_DISPOSITIVO = "AAAAAAAAAA";
    private static final String UPDATED_DISPOSITIVO = "BBBBBBBBBB";

    private static final String DEFAULT_MAC = "AAAAAAAAAA";
    private static final String UPDATED_MAC = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CONECTADO = false;
    private static final Boolean UPDATED_CONECTADO = true;

    private static final String ENTITY_API_URL = "/api/dispositivos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDispositivoMockMvc;

    private Dispositivo dispositivo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dispositivo createEntity(EntityManager em) {
        Dispositivo dispositivo = new Dispositivo().dispositivo(DEFAULT_DISPOSITIVO).mac(DEFAULT_MAC).conectado(DEFAULT_CONECTADO);
        return dispositivo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dispositivo createUpdatedEntity(EntityManager em) {
        Dispositivo dispositivo = new Dispositivo().dispositivo(UPDATED_DISPOSITIVO).mac(UPDATED_MAC).conectado(UPDATED_CONECTADO);
        return dispositivo;
    }

    @BeforeEach
    public void initTest() {
        dispositivo = createEntity(em);
    }

    @Test
    @Transactional
    void createDispositivo() throws Exception {
        int databaseSizeBeforeCreate = dispositivoRepository.findAll().size();
        // Create the Dispositivo
        restDispositivoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dispositivo))
            )
            .andExpect(status().isCreated());

        // Validate the Dispositivo in the database
        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeCreate + 1);
        Dispositivo testDispositivo = dispositivoList.get(dispositivoList.size() - 1);
        assertThat(testDispositivo.getDispositivo()).isEqualTo(DEFAULT_DISPOSITIVO);
        assertThat(testDispositivo.getMac()).isEqualTo(DEFAULT_MAC);
        assertThat(testDispositivo.getConectado()).isEqualTo(DEFAULT_CONECTADO);
    }

    @Test
    @Transactional
    void createDispositivoWithExistingId() throws Exception {
        // Create the Dispositivo with an existing ID
        dispositivo.setId(1L);

        int databaseSizeBeforeCreate = dispositivoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDispositivoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dispositivo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispositivo in the database
        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDispositivos() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        // Get all the dispositivoList
        restDispositivoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dispositivo.getId().intValue())))
            .andExpect(jsonPath("$.[*].dispositivo").value(hasItem(DEFAULT_DISPOSITIVO)))
            .andExpect(jsonPath("$.[*].mac").value(hasItem(DEFAULT_MAC)))
            .andExpect(jsonPath("$.[*].conectado").value(hasItem(DEFAULT_CONECTADO.booleanValue())));
    }

    @Test
    @Transactional
    void getDispositivo() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        // Get the dispositivo
        restDispositivoMockMvc
            .perform(get(ENTITY_API_URL_ID, dispositivo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dispositivo.getId().intValue()))
            .andExpect(jsonPath("$.dispositivo").value(DEFAULT_DISPOSITIVO))
            .andExpect(jsonPath("$.mac").value(DEFAULT_MAC))
            .andExpect(jsonPath("$.conectado").value(DEFAULT_CONECTADO.booleanValue()));
    }

    @Test
    @Transactional
    void getDispositivosByIdFiltering() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        Long id = dispositivo.getId();

        defaultDispositivoShouldBeFound("id.equals=" + id);
        defaultDispositivoShouldNotBeFound("id.notEquals=" + id);

        defaultDispositivoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDispositivoShouldNotBeFound("id.greaterThan=" + id);

        defaultDispositivoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDispositivoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDispositivosByDispositivoIsEqualToSomething() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        // Get all the dispositivoList where dispositivo equals to DEFAULT_DISPOSITIVO
        defaultDispositivoShouldBeFound("dispositivo.equals=" + DEFAULT_DISPOSITIVO);

        // Get all the dispositivoList where dispositivo equals to UPDATED_DISPOSITIVO
        defaultDispositivoShouldNotBeFound("dispositivo.equals=" + UPDATED_DISPOSITIVO);
    }

    @Test
    @Transactional
    void getAllDispositivosByDispositivoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        // Get all the dispositivoList where dispositivo not equals to DEFAULT_DISPOSITIVO
        defaultDispositivoShouldNotBeFound("dispositivo.notEquals=" + DEFAULT_DISPOSITIVO);

        // Get all the dispositivoList where dispositivo not equals to UPDATED_DISPOSITIVO
        defaultDispositivoShouldBeFound("dispositivo.notEquals=" + UPDATED_DISPOSITIVO);
    }

    @Test
    @Transactional
    void getAllDispositivosByDispositivoIsInShouldWork() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        // Get all the dispositivoList where dispositivo in DEFAULT_DISPOSITIVO or UPDATED_DISPOSITIVO
        defaultDispositivoShouldBeFound("dispositivo.in=" + DEFAULT_DISPOSITIVO + "," + UPDATED_DISPOSITIVO);

        // Get all the dispositivoList where dispositivo equals to UPDATED_DISPOSITIVO
        defaultDispositivoShouldNotBeFound("dispositivo.in=" + UPDATED_DISPOSITIVO);
    }

    @Test
    @Transactional
    void getAllDispositivosByDispositivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        // Get all the dispositivoList where dispositivo is not null
        defaultDispositivoShouldBeFound("dispositivo.specified=true");

        // Get all the dispositivoList where dispositivo is null
        defaultDispositivoShouldNotBeFound("dispositivo.specified=false");
    }

    @Test
    @Transactional
    void getAllDispositivosByDispositivoContainsSomething() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        // Get all the dispositivoList where dispositivo contains DEFAULT_DISPOSITIVO
        defaultDispositivoShouldBeFound("dispositivo.contains=" + DEFAULT_DISPOSITIVO);

        // Get all the dispositivoList where dispositivo contains UPDATED_DISPOSITIVO
        defaultDispositivoShouldNotBeFound("dispositivo.contains=" + UPDATED_DISPOSITIVO);
    }

    @Test
    @Transactional
    void getAllDispositivosByDispositivoNotContainsSomething() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        // Get all the dispositivoList where dispositivo does not contain DEFAULT_DISPOSITIVO
        defaultDispositivoShouldNotBeFound("dispositivo.doesNotContain=" + DEFAULT_DISPOSITIVO);

        // Get all the dispositivoList where dispositivo does not contain UPDATED_DISPOSITIVO
        defaultDispositivoShouldBeFound("dispositivo.doesNotContain=" + UPDATED_DISPOSITIVO);
    }

    @Test
    @Transactional
    void getAllDispositivosByMacIsEqualToSomething() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        // Get all the dispositivoList where mac equals to DEFAULT_MAC
        defaultDispositivoShouldBeFound("mac.equals=" + DEFAULT_MAC);

        // Get all the dispositivoList where mac equals to UPDATED_MAC
        defaultDispositivoShouldNotBeFound("mac.equals=" + UPDATED_MAC);
    }

    @Test
    @Transactional
    void getAllDispositivosByMacIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        // Get all the dispositivoList where mac not equals to DEFAULT_MAC
        defaultDispositivoShouldNotBeFound("mac.notEquals=" + DEFAULT_MAC);

        // Get all the dispositivoList where mac not equals to UPDATED_MAC
        defaultDispositivoShouldBeFound("mac.notEquals=" + UPDATED_MAC);
    }

    @Test
    @Transactional
    void getAllDispositivosByMacIsInShouldWork() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        // Get all the dispositivoList where mac in DEFAULT_MAC or UPDATED_MAC
        defaultDispositivoShouldBeFound("mac.in=" + DEFAULT_MAC + "," + UPDATED_MAC);

        // Get all the dispositivoList where mac equals to UPDATED_MAC
        defaultDispositivoShouldNotBeFound("mac.in=" + UPDATED_MAC);
    }

    @Test
    @Transactional
    void getAllDispositivosByMacIsNullOrNotNull() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        // Get all the dispositivoList where mac is not null
        defaultDispositivoShouldBeFound("mac.specified=true");

        // Get all the dispositivoList where mac is null
        defaultDispositivoShouldNotBeFound("mac.specified=false");
    }

    @Test
    @Transactional
    void getAllDispositivosByMacContainsSomething() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        // Get all the dispositivoList where mac contains DEFAULT_MAC
        defaultDispositivoShouldBeFound("mac.contains=" + DEFAULT_MAC);

        // Get all the dispositivoList where mac contains UPDATED_MAC
        defaultDispositivoShouldNotBeFound("mac.contains=" + UPDATED_MAC);
    }

    @Test
    @Transactional
    void getAllDispositivosByMacNotContainsSomething() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        // Get all the dispositivoList where mac does not contain DEFAULT_MAC
        defaultDispositivoShouldNotBeFound("mac.doesNotContain=" + DEFAULT_MAC);

        // Get all the dispositivoList where mac does not contain UPDATED_MAC
        defaultDispositivoShouldBeFound("mac.doesNotContain=" + UPDATED_MAC);
    }

    @Test
    @Transactional
    void getAllDispositivosByConectadoIsEqualToSomething() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        // Get all the dispositivoList where conectado equals to DEFAULT_CONECTADO
        defaultDispositivoShouldBeFound("conectado.equals=" + DEFAULT_CONECTADO);

        // Get all the dispositivoList where conectado equals to UPDATED_CONECTADO
        defaultDispositivoShouldNotBeFound("conectado.equals=" + UPDATED_CONECTADO);
    }

    @Test
    @Transactional
    void getAllDispositivosByConectadoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        // Get all the dispositivoList where conectado not equals to DEFAULT_CONECTADO
        defaultDispositivoShouldNotBeFound("conectado.notEquals=" + DEFAULT_CONECTADO);

        // Get all the dispositivoList where conectado not equals to UPDATED_CONECTADO
        defaultDispositivoShouldBeFound("conectado.notEquals=" + UPDATED_CONECTADO);
    }

    @Test
    @Transactional
    void getAllDispositivosByConectadoIsInShouldWork() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        // Get all the dispositivoList where conectado in DEFAULT_CONECTADO or UPDATED_CONECTADO
        defaultDispositivoShouldBeFound("conectado.in=" + DEFAULT_CONECTADO + "," + UPDATED_CONECTADO);

        // Get all the dispositivoList where conectado equals to UPDATED_CONECTADO
        defaultDispositivoShouldNotBeFound("conectado.in=" + UPDATED_CONECTADO);
    }

    @Test
    @Transactional
    void getAllDispositivosByConectadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        // Get all the dispositivoList where conectado is not null
        defaultDispositivoShouldBeFound("conectado.specified=true");

        // Get all the dispositivoList where conectado is null
        defaultDispositivoShouldNotBeFound("conectado.specified=false");
    }

    @Test
    @Transactional
    void getAllDispositivosByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        dispositivo.setUser(user);
        dispositivoRepository.saveAndFlush(dispositivo);
        String userId = user.getId();

        // Get all the dispositivoList where user equals to userId
        defaultDispositivoShouldBeFound("userId.equals=" + userId);

        // Get all the dispositivoList where user equals to "invalid-id"
        defaultDispositivoShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDispositivoShouldBeFound(String filter) throws Exception {
        restDispositivoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dispositivo.getId().intValue())))
            .andExpect(jsonPath("$.[*].dispositivo").value(hasItem(DEFAULT_DISPOSITIVO)))
            .andExpect(jsonPath("$.[*].mac").value(hasItem(DEFAULT_MAC)))
            .andExpect(jsonPath("$.[*].conectado").value(hasItem(DEFAULT_CONECTADO.booleanValue())));

        // Check, that the count call also returns 1
        restDispositivoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDispositivoShouldNotBeFound(String filter) throws Exception {
        restDispositivoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDispositivoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDispositivo() throws Exception {
        // Get the dispositivo
        restDispositivoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDispositivo() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        int databaseSizeBeforeUpdate = dispositivoRepository.findAll().size();

        // Update the dispositivo
        Dispositivo updatedDispositivo = dispositivoRepository.findById(dispositivo.getId()).get();
        // Disconnect from session so that the updates on updatedDispositivo are not directly saved in db
        em.detach(updatedDispositivo);
        updatedDispositivo.dispositivo(UPDATED_DISPOSITIVO).mac(UPDATED_MAC).conectado(UPDATED_CONECTADO);

        restDispositivoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDispositivo.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDispositivo))
            )
            .andExpect(status().isOk());

        // Validate the Dispositivo in the database
        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeUpdate);
        Dispositivo testDispositivo = dispositivoList.get(dispositivoList.size() - 1);
        assertThat(testDispositivo.getDispositivo()).isEqualTo(UPDATED_DISPOSITIVO);
        assertThat(testDispositivo.getMac()).isEqualTo(UPDATED_MAC);
        assertThat(testDispositivo.getConectado()).isEqualTo(UPDATED_CONECTADO);
    }

    @Test
    @Transactional
    void putNonExistingDispositivo() throws Exception {
        int databaseSizeBeforeUpdate = dispositivoRepository.findAll().size();
        dispositivo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDispositivoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dispositivo.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dispositivo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispositivo in the database
        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDispositivo() throws Exception {
        int databaseSizeBeforeUpdate = dispositivoRepository.findAll().size();
        dispositivo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispositivoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dispositivo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispositivo in the database
        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDispositivo() throws Exception {
        int databaseSizeBeforeUpdate = dispositivoRepository.findAll().size();
        dispositivo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispositivoMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dispositivo))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dispositivo in the database
        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDispositivoWithPatch() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        int databaseSizeBeforeUpdate = dispositivoRepository.findAll().size();

        // Update the dispositivo using partial update
        Dispositivo partialUpdatedDispositivo = new Dispositivo();
        partialUpdatedDispositivo.setId(dispositivo.getId());

        partialUpdatedDispositivo.conectado(UPDATED_CONECTADO);

        restDispositivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDispositivo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDispositivo))
            )
            .andExpect(status().isOk());

        // Validate the Dispositivo in the database
        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeUpdate);
        Dispositivo testDispositivo = dispositivoList.get(dispositivoList.size() - 1);
        assertThat(testDispositivo.getDispositivo()).isEqualTo(DEFAULT_DISPOSITIVO);
        assertThat(testDispositivo.getMac()).isEqualTo(DEFAULT_MAC);
        assertThat(testDispositivo.getConectado()).isEqualTo(UPDATED_CONECTADO);
    }

    @Test
    @Transactional
    void fullUpdateDispositivoWithPatch() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        int databaseSizeBeforeUpdate = dispositivoRepository.findAll().size();

        // Update the dispositivo using partial update
        Dispositivo partialUpdatedDispositivo = new Dispositivo();
        partialUpdatedDispositivo.setId(dispositivo.getId());

        partialUpdatedDispositivo.dispositivo(UPDATED_DISPOSITIVO).mac(UPDATED_MAC).conectado(UPDATED_CONECTADO);

        restDispositivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDispositivo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDispositivo))
            )
            .andExpect(status().isOk());

        // Validate the Dispositivo in the database
        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeUpdate);
        Dispositivo testDispositivo = dispositivoList.get(dispositivoList.size() - 1);
        assertThat(testDispositivo.getDispositivo()).isEqualTo(UPDATED_DISPOSITIVO);
        assertThat(testDispositivo.getMac()).isEqualTo(UPDATED_MAC);
        assertThat(testDispositivo.getConectado()).isEqualTo(UPDATED_CONECTADO);
    }

    @Test
    @Transactional
    void patchNonExistingDispositivo() throws Exception {
        int databaseSizeBeforeUpdate = dispositivoRepository.findAll().size();
        dispositivo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDispositivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dispositivo.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dispositivo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispositivo in the database
        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDispositivo() throws Exception {
        int databaseSizeBeforeUpdate = dispositivoRepository.findAll().size();
        dispositivo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispositivoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dispositivo))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dispositivo in the database
        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDispositivo() throws Exception {
        int databaseSizeBeforeUpdate = dispositivoRepository.findAll().size();
        dispositivo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDispositivoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dispositivo))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dispositivo in the database
        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDispositivo() throws Exception {
        // Initialize the database
        dispositivoRepository.saveAndFlush(dispositivo);

        int databaseSizeBeforeDelete = dispositivoRepository.findAll().size();

        // Delete the dispositivo
        restDispositivoMockMvc
            .perform(delete(ENTITY_API_URL_ID, dispositivo.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dispositivo> dispositivoList = dispositivoRepository.findAll();
        assertThat(dispositivoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

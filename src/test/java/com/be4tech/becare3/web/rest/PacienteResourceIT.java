package com.be4tech.becare3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.be4tech.becare3.IntegrationTest;
import com.be4tech.becare3.domain.Condicion;
import com.be4tech.becare3.domain.Farmaceutica;
import com.be4tech.becare3.domain.IPS;
import com.be4tech.becare3.domain.Paciente;
import com.be4tech.becare3.domain.Tratamieto;
import com.be4tech.becare3.domain.User;
import com.be4tech.becare3.domain.enumeration.Identificaciont;
import com.be4tech.becare3.domain.enumeration.Sexop;
import com.be4tech.becare3.repository.PacienteRepository;
import com.be4tech.becare3.service.criteria.PacienteCriteria;
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
 * Integration tests for the {@link PacienteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PacienteResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Identificaciont DEFAULT_TIPO_IDENTIFICACION = Identificaciont.CEDULA;
    private static final Identificaciont UPDATED_TIPO_IDENTIFICACION = Identificaciont.TARJETA;

    private static final Integer DEFAULT_IDENTIFICACION = 1;
    private static final Integer UPDATED_IDENTIFICACION = 2;
    private static final Integer SMALLER_IDENTIFICACION = 1 - 1;

    private static final Integer DEFAULT_EDAD = 1;
    private static final Integer UPDATED_EDAD = 2;
    private static final Integer SMALLER_EDAD = 1 - 1;

    private static final Sexop DEFAULT_SEXO = Sexop.FEMENINO;
    private static final Sexop UPDATED_SEXO = Sexop.MASCULINO;

    private static final Float DEFAULT_PESO_KG = 1F;
    private static final Float UPDATED_PESO_KG = 2F;
    private static final Float SMALLER_PESO_KG = 1F - 1F;

    private static final Integer DEFAULT_ESTATURA_CM = 1;
    private static final Integer UPDATED_ESTATURA_CM = 2;
    private static final Integer SMALLER_ESTATURA_CM = 1 - 1;

    private static final Integer DEFAULT_OXIMETRIA_REFERENCIA = 1;
    private static final Integer UPDATED_OXIMETRIA_REFERENCIA = 2;
    private static final Integer SMALLER_OXIMETRIA_REFERENCIA = 1 - 1;

    private static final Float DEFAULT_TEMPERATURA_REFERENCIA = 1F;
    private static final Float UPDATED_TEMPERATURA_REFERENCIA = 2F;
    private static final Float SMALLER_TEMPERATURA_REFERENCIA = 1F - 1F;

    private static final Integer DEFAULT_RITMO_CARDIACO_REFERENCIA = 1;
    private static final Integer UPDATED_RITMO_CARDIACO_REFERENCIA = 2;
    private static final Integer SMALLER_RITMO_CARDIACO_REFERENCIA = 1 - 1;

    private static final Integer DEFAULT_PRESION_SISTOLICA_REFERENCIA = 1;
    private static final Integer UPDATED_PRESION_SISTOLICA_REFERENCIA = 2;
    private static final Integer SMALLER_PRESION_SISTOLICA_REFERENCIA = 1 - 1;

    private static final Integer DEFAULT_PRESION_DISTOLICA_REFERENCIA = 1;
    private static final Integer UPDATED_PRESION_DISTOLICA_REFERENCIA = 2;
    private static final Integer SMALLER_PRESION_DISTOLICA_REFERENCIA = 1 - 1;

    private static final String DEFAULT_COMENTARIOS = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARIOS = "BBBBBBBBBB";

    private static final Integer DEFAULT_PASOS_REFERENCIA = 1;
    private static final Integer UPDATED_PASOS_REFERENCIA = 2;
    private static final Integer SMALLER_PASOS_REFERENCIA = 1 - 1;

    private static final Integer DEFAULT_CALORIAS_REFERENCIA = 1;
    private static final Integer UPDATED_CALORIAS_REFERENCIA = 2;
    private static final Integer SMALLER_CALORIAS_REFERENCIA = 1 - 1;

    private static final String DEFAULT_META_REFERENCIA = "AAAAAAAAAA";
    private static final String UPDATED_META_REFERENCIA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pacientes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPacienteMockMvc;

    private Paciente paciente;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paciente createEntity(EntityManager em) {
        Paciente paciente = new Paciente()
            .nombre(DEFAULT_NOMBRE)
            .tipoIdentificacion(DEFAULT_TIPO_IDENTIFICACION)
            .identificacion(DEFAULT_IDENTIFICACION)
            .edad(DEFAULT_EDAD)
            .sexo(DEFAULT_SEXO)
            .pesoKG(DEFAULT_PESO_KG)
            .estaturaCM(DEFAULT_ESTATURA_CM)
            .oximetriaReferencia(DEFAULT_OXIMETRIA_REFERENCIA)
            .temperaturaReferencia(DEFAULT_TEMPERATURA_REFERENCIA)
            .ritmoCardiacoReferencia(DEFAULT_RITMO_CARDIACO_REFERENCIA)
            .presionSistolicaReferencia(DEFAULT_PRESION_SISTOLICA_REFERENCIA)
            .presionDistolicaReferencia(DEFAULT_PRESION_DISTOLICA_REFERENCIA)
            .comentarios(DEFAULT_COMENTARIOS)
            .pasosReferencia(DEFAULT_PASOS_REFERENCIA)
            .caloriasReferencia(DEFAULT_CALORIAS_REFERENCIA)
            .metaReferencia(DEFAULT_META_REFERENCIA);
        return paciente;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paciente createUpdatedEntity(EntityManager em) {
        Paciente paciente = new Paciente()
            .nombre(UPDATED_NOMBRE)
            .tipoIdentificacion(UPDATED_TIPO_IDENTIFICACION)
            .identificacion(UPDATED_IDENTIFICACION)
            .edad(UPDATED_EDAD)
            .sexo(UPDATED_SEXO)
            .pesoKG(UPDATED_PESO_KG)
            .estaturaCM(UPDATED_ESTATURA_CM)
            .oximetriaReferencia(UPDATED_OXIMETRIA_REFERENCIA)
            .temperaturaReferencia(UPDATED_TEMPERATURA_REFERENCIA)
            .ritmoCardiacoReferencia(UPDATED_RITMO_CARDIACO_REFERENCIA)
            .presionSistolicaReferencia(UPDATED_PRESION_SISTOLICA_REFERENCIA)
            .presionDistolicaReferencia(UPDATED_PRESION_DISTOLICA_REFERENCIA)
            .comentarios(UPDATED_COMENTARIOS)
            .pasosReferencia(UPDATED_PASOS_REFERENCIA)
            .caloriasReferencia(UPDATED_CALORIAS_REFERENCIA)
            .metaReferencia(UPDATED_META_REFERENCIA);
        return paciente;
    }

    @BeforeEach
    public void initTest() {
        paciente = createEntity(em);
    }

    @Test
    @Transactional
    void createPaciente() throws Exception {
        int databaseSizeBeforeCreate = pacienteRepository.findAll().size();
        // Create the Paciente
        restPacienteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paciente))
            )
            .andExpect(status().isCreated());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeCreate + 1);
        Paciente testPaciente = pacienteList.get(pacienteList.size() - 1);
        assertThat(testPaciente.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPaciente.getTipoIdentificacion()).isEqualTo(DEFAULT_TIPO_IDENTIFICACION);
        assertThat(testPaciente.getIdentificacion()).isEqualTo(DEFAULT_IDENTIFICACION);
        assertThat(testPaciente.getEdad()).isEqualTo(DEFAULT_EDAD);
        assertThat(testPaciente.getSexo()).isEqualTo(DEFAULT_SEXO);
        assertThat(testPaciente.getPesoKG()).isEqualTo(DEFAULT_PESO_KG);
        assertThat(testPaciente.getEstaturaCM()).isEqualTo(DEFAULT_ESTATURA_CM);
        assertThat(testPaciente.getOximetriaReferencia()).isEqualTo(DEFAULT_OXIMETRIA_REFERENCIA);
        assertThat(testPaciente.getTemperaturaReferencia()).isEqualTo(DEFAULT_TEMPERATURA_REFERENCIA);
        assertThat(testPaciente.getRitmoCardiacoReferencia()).isEqualTo(DEFAULT_RITMO_CARDIACO_REFERENCIA);
        assertThat(testPaciente.getPresionSistolicaReferencia()).isEqualTo(DEFAULT_PRESION_SISTOLICA_REFERENCIA);
        assertThat(testPaciente.getPresionDistolicaReferencia()).isEqualTo(DEFAULT_PRESION_DISTOLICA_REFERENCIA);
        assertThat(testPaciente.getComentarios()).isEqualTo(DEFAULT_COMENTARIOS);
        assertThat(testPaciente.getPasosReferencia()).isEqualTo(DEFAULT_PASOS_REFERENCIA);
        assertThat(testPaciente.getCaloriasReferencia()).isEqualTo(DEFAULT_CALORIAS_REFERENCIA);
        assertThat(testPaciente.getMetaReferencia()).isEqualTo(DEFAULT_META_REFERENCIA);
    }

    @Test
    @Transactional
    void createPacienteWithExistingId() throws Exception {
        // Create the Paciente with an existing ID
        paciente.setId(1L);

        int databaseSizeBeforeCreate = pacienteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPacienteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paciente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPacientes() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paciente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].tipoIdentificacion").value(hasItem(DEFAULT_TIPO_IDENTIFICACION.toString())))
            .andExpect(jsonPath("$.[*].identificacion").value(hasItem(DEFAULT_IDENTIFICACION)))
            .andExpect(jsonPath("$.[*].edad").value(hasItem(DEFAULT_EDAD)))
            .andExpect(jsonPath("$.[*].sexo").value(hasItem(DEFAULT_SEXO.toString())))
            .andExpect(jsonPath("$.[*].pesoKG").value(hasItem(DEFAULT_PESO_KG.doubleValue())))
            .andExpect(jsonPath("$.[*].estaturaCM").value(hasItem(DEFAULT_ESTATURA_CM)))
            .andExpect(jsonPath("$.[*].oximetriaReferencia").value(hasItem(DEFAULT_OXIMETRIA_REFERENCIA)))
            .andExpect(jsonPath("$.[*].temperaturaReferencia").value(hasItem(DEFAULT_TEMPERATURA_REFERENCIA.doubleValue())))
            .andExpect(jsonPath("$.[*].ritmoCardiacoReferencia").value(hasItem(DEFAULT_RITMO_CARDIACO_REFERENCIA)))
            .andExpect(jsonPath("$.[*].presionSistolicaReferencia").value(hasItem(DEFAULT_PRESION_SISTOLICA_REFERENCIA)))
            .andExpect(jsonPath("$.[*].presionDistolicaReferencia").value(hasItem(DEFAULT_PRESION_DISTOLICA_REFERENCIA)))
            .andExpect(jsonPath("$.[*].comentarios").value(hasItem(DEFAULT_COMENTARIOS.toString())))
            .andExpect(jsonPath("$.[*].pasosReferencia").value(hasItem(DEFAULT_PASOS_REFERENCIA)))
            .andExpect(jsonPath("$.[*].caloriasReferencia").value(hasItem(DEFAULT_CALORIAS_REFERENCIA)))
            .andExpect(jsonPath("$.[*].metaReferencia").value(hasItem(DEFAULT_META_REFERENCIA)));
    }

    @Test
    @Transactional
    void getPaciente() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get the paciente
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL_ID, paciente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paciente.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.tipoIdentificacion").value(DEFAULT_TIPO_IDENTIFICACION.toString()))
            .andExpect(jsonPath("$.identificacion").value(DEFAULT_IDENTIFICACION))
            .andExpect(jsonPath("$.edad").value(DEFAULT_EDAD))
            .andExpect(jsonPath("$.sexo").value(DEFAULT_SEXO.toString()))
            .andExpect(jsonPath("$.pesoKG").value(DEFAULT_PESO_KG.doubleValue()))
            .andExpect(jsonPath("$.estaturaCM").value(DEFAULT_ESTATURA_CM))
            .andExpect(jsonPath("$.oximetriaReferencia").value(DEFAULT_OXIMETRIA_REFERENCIA))
            .andExpect(jsonPath("$.temperaturaReferencia").value(DEFAULT_TEMPERATURA_REFERENCIA.doubleValue()))
            .andExpect(jsonPath("$.ritmoCardiacoReferencia").value(DEFAULT_RITMO_CARDIACO_REFERENCIA))
            .andExpect(jsonPath("$.presionSistolicaReferencia").value(DEFAULT_PRESION_SISTOLICA_REFERENCIA))
            .andExpect(jsonPath("$.presionDistolicaReferencia").value(DEFAULT_PRESION_DISTOLICA_REFERENCIA))
            .andExpect(jsonPath("$.comentarios").value(DEFAULT_COMENTARIOS.toString()))
            .andExpect(jsonPath("$.pasosReferencia").value(DEFAULT_PASOS_REFERENCIA))
            .andExpect(jsonPath("$.caloriasReferencia").value(DEFAULT_CALORIAS_REFERENCIA))
            .andExpect(jsonPath("$.metaReferencia").value(DEFAULT_META_REFERENCIA));
    }

    @Test
    @Transactional
    void getPacientesByIdFiltering() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        Long id = paciente.getId();

        defaultPacienteShouldBeFound("id.equals=" + id);
        defaultPacienteShouldNotBeFound("id.notEquals=" + id);

        defaultPacienteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPacienteShouldNotBeFound("id.greaterThan=" + id);

        defaultPacienteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPacienteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPacientesByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nombre equals to DEFAULT_NOMBRE
        defaultPacienteShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the pacienteList where nombre equals to UPDATED_NOMBRE
        defaultPacienteShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPacientesByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nombre not equals to DEFAULT_NOMBRE
        defaultPacienteShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the pacienteList where nombre not equals to UPDATED_NOMBRE
        defaultPacienteShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPacientesByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultPacienteShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the pacienteList where nombre equals to UPDATED_NOMBRE
        defaultPacienteShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPacientesByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nombre is not null
        defaultPacienteShouldBeFound("nombre.specified=true");

        // Get all the pacienteList where nombre is null
        defaultPacienteShouldNotBeFound("nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByNombreContainsSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nombre contains DEFAULT_NOMBRE
        defaultPacienteShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the pacienteList where nombre contains UPDATED_NOMBRE
        defaultPacienteShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPacientesByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where nombre does not contain DEFAULT_NOMBRE
        defaultPacienteShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the pacienteList where nombre does not contain UPDATED_NOMBRE
        defaultPacienteShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllPacientesByTipoIdentificacionIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where tipoIdentificacion equals to DEFAULT_TIPO_IDENTIFICACION
        defaultPacienteShouldBeFound("tipoIdentificacion.equals=" + DEFAULT_TIPO_IDENTIFICACION);

        // Get all the pacienteList where tipoIdentificacion equals to UPDATED_TIPO_IDENTIFICACION
        defaultPacienteShouldNotBeFound("tipoIdentificacion.equals=" + UPDATED_TIPO_IDENTIFICACION);
    }

    @Test
    @Transactional
    void getAllPacientesByTipoIdentificacionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where tipoIdentificacion not equals to DEFAULT_TIPO_IDENTIFICACION
        defaultPacienteShouldNotBeFound("tipoIdentificacion.notEquals=" + DEFAULT_TIPO_IDENTIFICACION);

        // Get all the pacienteList where tipoIdentificacion not equals to UPDATED_TIPO_IDENTIFICACION
        defaultPacienteShouldBeFound("tipoIdentificacion.notEquals=" + UPDATED_TIPO_IDENTIFICACION);
    }

    @Test
    @Transactional
    void getAllPacientesByTipoIdentificacionIsInShouldWork() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where tipoIdentificacion in DEFAULT_TIPO_IDENTIFICACION or UPDATED_TIPO_IDENTIFICACION
        defaultPacienteShouldBeFound("tipoIdentificacion.in=" + DEFAULT_TIPO_IDENTIFICACION + "," + UPDATED_TIPO_IDENTIFICACION);

        // Get all the pacienteList where tipoIdentificacion equals to UPDATED_TIPO_IDENTIFICACION
        defaultPacienteShouldNotBeFound("tipoIdentificacion.in=" + UPDATED_TIPO_IDENTIFICACION);
    }

    @Test
    @Transactional
    void getAllPacientesByTipoIdentificacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where tipoIdentificacion is not null
        defaultPacienteShouldBeFound("tipoIdentificacion.specified=true");

        // Get all the pacienteList where tipoIdentificacion is null
        defaultPacienteShouldNotBeFound("tipoIdentificacion.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByIdentificacionIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where identificacion equals to DEFAULT_IDENTIFICACION
        defaultPacienteShouldBeFound("identificacion.equals=" + DEFAULT_IDENTIFICACION);

        // Get all the pacienteList where identificacion equals to UPDATED_IDENTIFICACION
        defaultPacienteShouldNotBeFound("identificacion.equals=" + UPDATED_IDENTIFICACION);
    }

    @Test
    @Transactional
    void getAllPacientesByIdentificacionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where identificacion not equals to DEFAULT_IDENTIFICACION
        defaultPacienteShouldNotBeFound("identificacion.notEquals=" + DEFAULT_IDENTIFICACION);

        // Get all the pacienteList where identificacion not equals to UPDATED_IDENTIFICACION
        defaultPacienteShouldBeFound("identificacion.notEquals=" + UPDATED_IDENTIFICACION);
    }

    @Test
    @Transactional
    void getAllPacientesByIdentificacionIsInShouldWork() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where identificacion in DEFAULT_IDENTIFICACION or UPDATED_IDENTIFICACION
        defaultPacienteShouldBeFound("identificacion.in=" + DEFAULT_IDENTIFICACION + "," + UPDATED_IDENTIFICACION);

        // Get all the pacienteList where identificacion equals to UPDATED_IDENTIFICACION
        defaultPacienteShouldNotBeFound("identificacion.in=" + UPDATED_IDENTIFICACION);
    }

    @Test
    @Transactional
    void getAllPacientesByIdentificacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where identificacion is not null
        defaultPacienteShouldBeFound("identificacion.specified=true");

        // Get all the pacienteList where identificacion is null
        defaultPacienteShouldNotBeFound("identificacion.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByIdentificacionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where identificacion is greater than or equal to DEFAULT_IDENTIFICACION
        defaultPacienteShouldBeFound("identificacion.greaterThanOrEqual=" + DEFAULT_IDENTIFICACION);

        // Get all the pacienteList where identificacion is greater than or equal to UPDATED_IDENTIFICACION
        defaultPacienteShouldNotBeFound("identificacion.greaterThanOrEqual=" + UPDATED_IDENTIFICACION);
    }

    @Test
    @Transactional
    void getAllPacientesByIdentificacionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where identificacion is less than or equal to DEFAULT_IDENTIFICACION
        defaultPacienteShouldBeFound("identificacion.lessThanOrEqual=" + DEFAULT_IDENTIFICACION);

        // Get all the pacienteList where identificacion is less than or equal to SMALLER_IDENTIFICACION
        defaultPacienteShouldNotBeFound("identificacion.lessThanOrEqual=" + SMALLER_IDENTIFICACION);
    }

    @Test
    @Transactional
    void getAllPacientesByIdentificacionIsLessThanSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where identificacion is less than DEFAULT_IDENTIFICACION
        defaultPacienteShouldNotBeFound("identificacion.lessThan=" + DEFAULT_IDENTIFICACION);

        // Get all the pacienteList where identificacion is less than UPDATED_IDENTIFICACION
        defaultPacienteShouldBeFound("identificacion.lessThan=" + UPDATED_IDENTIFICACION);
    }

    @Test
    @Transactional
    void getAllPacientesByIdentificacionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where identificacion is greater than DEFAULT_IDENTIFICACION
        defaultPacienteShouldNotBeFound("identificacion.greaterThan=" + DEFAULT_IDENTIFICACION);

        // Get all the pacienteList where identificacion is greater than SMALLER_IDENTIFICACION
        defaultPacienteShouldBeFound("identificacion.greaterThan=" + SMALLER_IDENTIFICACION);
    }

    @Test
    @Transactional
    void getAllPacientesByEdadIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where edad equals to DEFAULT_EDAD
        defaultPacienteShouldBeFound("edad.equals=" + DEFAULT_EDAD);

        // Get all the pacienteList where edad equals to UPDATED_EDAD
        defaultPacienteShouldNotBeFound("edad.equals=" + UPDATED_EDAD);
    }

    @Test
    @Transactional
    void getAllPacientesByEdadIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where edad not equals to DEFAULT_EDAD
        defaultPacienteShouldNotBeFound("edad.notEquals=" + DEFAULT_EDAD);

        // Get all the pacienteList where edad not equals to UPDATED_EDAD
        defaultPacienteShouldBeFound("edad.notEquals=" + UPDATED_EDAD);
    }

    @Test
    @Transactional
    void getAllPacientesByEdadIsInShouldWork() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where edad in DEFAULT_EDAD or UPDATED_EDAD
        defaultPacienteShouldBeFound("edad.in=" + DEFAULT_EDAD + "," + UPDATED_EDAD);

        // Get all the pacienteList where edad equals to UPDATED_EDAD
        defaultPacienteShouldNotBeFound("edad.in=" + UPDATED_EDAD);
    }

    @Test
    @Transactional
    void getAllPacientesByEdadIsNullOrNotNull() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where edad is not null
        defaultPacienteShouldBeFound("edad.specified=true");

        // Get all the pacienteList where edad is null
        defaultPacienteShouldNotBeFound("edad.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByEdadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where edad is greater than or equal to DEFAULT_EDAD
        defaultPacienteShouldBeFound("edad.greaterThanOrEqual=" + DEFAULT_EDAD);

        // Get all the pacienteList where edad is greater than or equal to UPDATED_EDAD
        defaultPacienteShouldNotBeFound("edad.greaterThanOrEqual=" + UPDATED_EDAD);
    }

    @Test
    @Transactional
    void getAllPacientesByEdadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where edad is less than or equal to DEFAULT_EDAD
        defaultPacienteShouldBeFound("edad.lessThanOrEqual=" + DEFAULT_EDAD);

        // Get all the pacienteList where edad is less than or equal to SMALLER_EDAD
        defaultPacienteShouldNotBeFound("edad.lessThanOrEqual=" + SMALLER_EDAD);
    }

    @Test
    @Transactional
    void getAllPacientesByEdadIsLessThanSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where edad is less than DEFAULT_EDAD
        defaultPacienteShouldNotBeFound("edad.lessThan=" + DEFAULT_EDAD);

        // Get all the pacienteList where edad is less than UPDATED_EDAD
        defaultPacienteShouldBeFound("edad.lessThan=" + UPDATED_EDAD);
    }

    @Test
    @Transactional
    void getAllPacientesByEdadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where edad is greater than DEFAULT_EDAD
        defaultPacienteShouldNotBeFound("edad.greaterThan=" + DEFAULT_EDAD);

        // Get all the pacienteList where edad is greater than SMALLER_EDAD
        defaultPacienteShouldBeFound("edad.greaterThan=" + SMALLER_EDAD);
    }

    @Test
    @Transactional
    void getAllPacientesBySexoIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where sexo equals to DEFAULT_SEXO
        defaultPacienteShouldBeFound("sexo.equals=" + DEFAULT_SEXO);

        // Get all the pacienteList where sexo equals to UPDATED_SEXO
        defaultPacienteShouldNotBeFound("sexo.equals=" + UPDATED_SEXO);
    }

    @Test
    @Transactional
    void getAllPacientesBySexoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where sexo not equals to DEFAULT_SEXO
        defaultPacienteShouldNotBeFound("sexo.notEquals=" + DEFAULT_SEXO);

        // Get all the pacienteList where sexo not equals to UPDATED_SEXO
        defaultPacienteShouldBeFound("sexo.notEquals=" + UPDATED_SEXO);
    }

    @Test
    @Transactional
    void getAllPacientesBySexoIsInShouldWork() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where sexo in DEFAULT_SEXO or UPDATED_SEXO
        defaultPacienteShouldBeFound("sexo.in=" + DEFAULT_SEXO + "," + UPDATED_SEXO);

        // Get all the pacienteList where sexo equals to UPDATED_SEXO
        defaultPacienteShouldNotBeFound("sexo.in=" + UPDATED_SEXO);
    }

    @Test
    @Transactional
    void getAllPacientesBySexoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where sexo is not null
        defaultPacienteShouldBeFound("sexo.specified=true");

        // Get all the pacienteList where sexo is null
        defaultPacienteShouldNotBeFound("sexo.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByPesoKGIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where pesoKG equals to DEFAULT_PESO_KG
        defaultPacienteShouldBeFound("pesoKG.equals=" + DEFAULT_PESO_KG);

        // Get all the pacienteList where pesoKG equals to UPDATED_PESO_KG
        defaultPacienteShouldNotBeFound("pesoKG.equals=" + UPDATED_PESO_KG);
    }

    @Test
    @Transactional
    void getAllPacientesByPesoKGIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where pesoKG not equals to DEFAULT_PESO_KG
        defaultPacienteShouldNotBeFound("pesoKG.notEquals=" + DEFAULT_PESO_KG);

        // Get all the pacienteList where pesoKG not equals to UPDATED_PESO_KG
        defaultPacienteShouldBeFound("pesoKG.notEquals=" + UPDATED_PESO_KG);
    }

    @Test
    @Transactional
    void getAllPacientesByPesoKGIsInShouldWork() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where pesoKG in DEFAULT_PESO_KG or UPDATED_PESO_KG
        defaultPacienteShouldBeFound("pesoKG.in=" + DEFAULT_PESO_KG + "," + UPDATED_PESO_KG);

        // Get all the pacienteList where pesoKG equals to UPDATED_PESO_KG
        defaultPacienteShouldNotBeFound("pesoKG.in=" + UPDATED_PESO_KG);
    }

    @Test
    @Transactional
    void getAllPacientesByPesoKGIsNullOrNotNull() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where pesoKG is not null
        defaultPacienteShouldBeFound("pesoKG.specified=true");

        // Get all the pacienteList where pesoKG is null
        defaultPacienteShouldNotBeFound("pesoKG.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByPesoKGIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where pesoKG is greater than or equal to DEFAULT_PESO_KG
        defaultPacienteShouldBeFound("pesoKG.greaterThanOrEqual=" + DEFAULT_PESO_KG);

        // Get all the pacienteList where pesoKG is greater than or equal to UPDATED_PESO_KG
        defaultPacienteShouldNotBeFound("pesoKG.greaterThanOrEqual=" + UPDATED_PESO_KG);
    }

    @Test
    @Transactional
    void getAllPacientesByPesoKGIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where pesoKG is less than or equal to DEFAULT_PESO_KG
        defaultPacienteShouldBeFound("pesoKG.lessThanOrEqual=" + DEFAULT_PESO_KG);

        // Get all the pacienteList where pesoKG is less than or equal to SMALLER_PESO_KG
        defaultPacienteShouldNotBeFound("pesoKG.lessThanOrEqual=" + SMALLER_PESO_KG);
    }

    @Test
    @Transactional
    void getAllPacientesByPesoKGIsLessThanSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where pesoKG is less than DEFAULT_PESO_KG
        defaultPacienteShouldNotBeFound("pesoKG.lessThan=" + DEFAULT_PESO_KG);

        // Get all the pacienteList where pesoKG is less than UPDATED_PESO_KG
        defaultPacienteShouldBeFound("pesoKG.lessThan=" + UPDATED_PESO_KG);
    }

    @Test
    @Transactional
    void getAllPacientesByPesoKGIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where pesoKG is greater than DEFAULT_PESO_KG
        defaultPacienteShouldNotBeFound("pesoKG.greaterThan=" + DEFAULT_PESO_KG);

        // Get all the pacienteList where pesoKG is greater than SMALLER_PESO_KG
        defaultPacienteShouldBeFound("pesoKG.greaterThan=" + SMALLER_PESO_KG);
    }

    @Test
    @Transactional
    void getAllPacientesByEstaturaCMIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where estaturaCM equals to DEFAULT_ESTATURA_CM
        defaultPacienteShouldBeFound("estaturaCM.equals=" + DEFAULT_ESTATURA_CM);

        // Get all the pacienteList where estaturaCM equals to UPDATED_ESTATURA_CM
        defaultPacienteShouldNotBeFound("estaturaCM.equals=" + UPDATED_ESTATURA_CM);
    }

    @Test
    @Transactional
    void getAllPacientesByEstaturaCMIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where estaturaCM not equals to DEFAULT_ESTATURA_CM
        defaultPacienteShouldNotBeFound("estaturaCM.notEquals=" + DEFAULT_ESTATURA_CM);

        // Get all the pacienteList where estaturaCM not equals to UPDATED_ESTATURA_CM
        defaultPacienteShouldBeFound("estaturaCM.notEquals=" + UPDATED_ESTATURA_CM);
    }

    @Test
    @Transactional
    void getAllPacientesByEstaturaCMIsInShouldWork() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where estaturaCM in DEFAULT_ESTATURA_CM or UPDATED_ESTATURA_CM
        defaultPacienteShouldBeFound("estaturaCM.in=" + DEFAULT_ESTATURA_CM + "," + UPDATED_ESTATURA_CM);

        // Get all the pacienteList where estaturaCM equals to UPDATED_ESTATURA_CM
        defaultPacienteShouldNotBeFound("estaturaCM.in=" + UPDATED_ESTATURA_CM);
    }

    @Test
    @Transactional
    void getAllPacientesByEstaturaCMIsNullOrNotNull() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where estaturaCM is not null
        defaultPacienteShouldBeFound("estaturaCM.specified=true");

        // Get all the pacienteList where estaturaCM is null
        defaultPacienteShouldNotBeFound("estaturaCM.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByEstaturaCMIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where estaturaCM is greater than or equal to DEFAULT_ESTATURA_CM
        defaultPacienteShouldBeFound("estaturaCM.greaterThanOrEqual=" + DEFAULT_ESTATURA_CM);

        // Get all the pacienteList where estaturaCM is greater than or equal to UPDATED_ESTATURA_CM
        defaultPacienteShouldNotBeFound("estaturaCM.greaterThanOrEqual=" + UPDATED_ESTATURA_CM);
    }

    @Test
    @Transactional
    void getAllPacientesByEstaturaCMIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where estaturaCM is less than or equal to DEFAULT_ESTATURA_CM
        defaultPacienteShouldBeFound("estaturaCM.lessThanOrEqual=" + DEFAULT_ESTATURA_CM);

        // Get all the pacienteList where estaturaCM is less than or equal to SMALLER_ESTATURA_CM
        defaultPacienteShouldNotBeFound("estaturaCM.lessThanOrEqual=" + SMALLER_ESTATURA_CM);
    }

    @Test
    @Transactional
    void getAllPacientesByEstaturaCMIsLessThanSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where estaturaCM is less than DEFAULT_ESTATURA_CM
        defaultPacienteShouldNotBeFound("estaturaCM.lessThan=" + DEFAULT_ESTATURA_CM);

        // Get all the pacienteList where estaturaCM is less than UPDATED_ESTATURA_CM
        defaultPacienteShouldBeFound("estaturaCM.lessThan=" + UPDATED_ESTATURA_CM);
    }

    @Test
    @Transactional
    void getAllPacientesByEstaturaCMIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where estaturaCM is greater than DEFAULT_ESTATURA_CM
        defaultPacienteShouldNotBeFound("estaturaCM.greaterThan=" + DEFAULT_ESTATURA_CM);

        // Get all the pacienteList where estaturaCM is greater than SMALLER_ESTATURA_CM
        defaultPacienteShouldBeFound("estaturaCM.greaterThan=" + SMALLER_ESTATURA_CM);
    }

    @Test
    @Transactional
    void getAllPacientesByOximetriaReferenciaIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where oximetriaReferencia equals to DEFAULT_OXIMETRIA_REFERENCIA
        defaultPacienteShouldBeFound("oximetriaReferencia.equals=" + DEFAULT_OXIMETRIA_REFERENCIA);

        // Get all the pacienteList where oximetriaReferencia equals to UPDATED_OXIMETRIA_REFERENCIA
        defaultPacienteShouldNotBeFound("oximetriaReferencia.equals=" + UPDATED_OXIMETRIA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByOximetriaReferenciaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where oximetriaReferencia not equals to DEFAULT_OXIMETRIA_REFERENCIA
        defaultPacienteShouldNotBeFound("oximetriaReferencia.notEquals=" + DEFAULT_OXIMETRIA_REFERENCIA);

        // Get all the pacienteList where oximetriaReferencia not equals to UPDATED_OXIMETRIA_REFERENCIA
        defaultPacienteShouldBeFound("oximetriaReferencia.notEquals=" + UPDATED_OXIMETRIA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByOximetriaReferenciaIsInShouldWork() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where oximetriaReferencia in DEFAULT_OXIMETRIA_REFERENCIA or UPDATED_OXIMETRIA_REFERENCIA
        defaultPacienteShouldBeFound("oximetriaReferencia.in=" + DEFAULT_OXIMETRIA_REFERENCIA + "," + UPDATED_OXIMETRIA_REFERENCIA);

        // Get all the pacienteList where oximetriaReferencia equals to UPDATED_OXIMETRIA_REFERENCIA
        defaultPacienteShouldNotBeFound("oximetriaReferencia.in=" + UPDATED_OXIMETRIA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByOximetriaReferenciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where oximetriaReferencia is not null
        defaultPacienteShouldBeFound("oximetriaReferencia.specified=true");

        // Get all the pacienteList where oximetriaReferencia is null
        defaultPacienteShouldNotBeFound("oximetriaReferencia.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByOximetriaReferenciaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where oximetriaReferencia is greater than or equal to DEFAULT_OXIMETRIA_REFERENCIA
        defaultPacienteShouldBeFound("oximetriaReferencia.greaterThanOrEqual=" + DEFAULT_OXIMETRIA_REFERENCIA);

        // Get all the pacienteList where oximetriaReferencia is greater than or equal to UPDATED_OXIMETRIA_REFERENCIA
        defaultPacienteShouldNotBeFound("oximetriaReferencia.greaterThanOrEqual=" + UPDATED_OXIMETRIA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByOximetriaReferenciaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where oximetriaReferencia is less than or equal to DEFAULT_OXIMETRIA_REFERENCIA
        defaultPacienteShouldBeFound("oximetriaReferencia.lessThanOrEqual=" + DEFAULT_OXIMETRIA_REFERENCIA);

        // Get all the pacienteList where oximetriaReferencia is less than or equal to SMALLER_OXIMETRIA_REFERENCIA
        defaultPacienteShouldNotBeFound("oximetriaReferencia.lessThanOrEqual=" + SMALLER_OXIMETRIA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByOximetriaReferenciaIsLessThanSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where oximetriaReferencia is less than DEFAULT_OXIMETRIA_REFERENCIA
        defaultPacienteShouldNotBeFound("oximetriaReferencia.lessThan=" + DEFAULT_OXIMETRIA_REFERENCIA);

        // Get all the pacienteList where oximetriaReferencia is less than UPDATED_OXIMETRIA_REFERENCIA
        defaultPacienteShouldBeFound("oximetriaReferencia.lessThan=" + UPDATED_OXIMETRIA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByOximetriaReferenciaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where oximetriaReferencia is greater than DEFAULT_OXIMETRIA_REFERENCIA
        defaultPacienteShouldNotBeFound("oximetriaReferencia.greaterThan=" + DEFAULT_OXIMETRIA_REFERENCIA);

        // Get all the pacienteList where oximetriaReferencia is greater than SMALLER_OXIMETRIA_REFERENCIA
        defaultPacienteShouldBeFound("oximetriaReferencia.greaterThan=" + SMALLER_OXIMETRIA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByTemperaturaReferenciaIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where temperaturaReferencia equals to DEFAULT_TEMPERATURA_REFERENCIA
        defaultPacienteShouldBeFound("temperaturaReferencia.equals=" + DEFAULT_TEMPERATURA_REFERENCIA);

        // Get all the pacienteList where temperaturaReferencia equals to UPDATED_TEMPERATURA_REFERENCIA
        defaultPacienteShouldNotBeFound("temperaturaReferencia.equals=" + UPDATED_TEMPERATURA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByTemperaturaReferenciaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where temperaturaReferencia not equals to DEFAULT_TEMPERATURA_REFERENCIA
        defaultPacienteShouldNotBeFound("temperaturaReferencia.notEquals=" + DEFAULT_TEMPERATURA_REFERENCIA);

        // Get all the pacienteList where temperaturaReferencia not equals to UPDATED_TEMPERATURA_REFERENCIA
        defaultPacienteShouldBeFound("temperaturaReferencia.notEquals=" + UPDATED_TEMPERATURA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByTemperaturaReferenciaIsInShouldWork() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where temperaturaReferencia in DEFAULT_TEMPERATURA_REFERENCIA or UPDATED_TEMPERATURA_REFERENCIA
        defaultPacienteShouldBeFound("temperaturaReferencia.in=" + DEFAULT_TEMPERATURA_REFERENCIA + "," + UPDATED_TEMPERATURA_REFERENCIA);

        // Get all the pacienteList where temperaturaReferencia equals to UPDATED_TEMPERATURA_REFERENCIA
        defaultPacienteShouldNotBeFound("temperaturaReferencia.in=" + UPDATED_TEMPERATURA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByTemperaturaReferenciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where temperaturaReferencia is not null
        defaultPacienteShouldBeFound("temperaturaReferencia.specified=true");

        // Get all the pacienteList where temperaturaReferencia is null
        defaultPacienteShouldNotBeFound("temperaturaReferencia.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByTemperaturaReferenciaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where temperaturaReferencia is greater than or equal to DEFAULT_TEMPERATURA_REFERENCIA
        defaultPacienteShouldBeFound("temperaturaReferencia.greaterThanOrEqual=" + DEFAULT_TEMPERATURA_REFERENCIA);

        // Get all the pacienteList where temperaturaReferencia is greater than or equal to UPDATED_TEMPERATURA_REFERENCIA
        defaultPacienteShouldNotBeFound("temperaturaReferencia.greaterThanOrEqual=" + UPDATED_TEMPERATURA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByTemperaturaReferenciaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where temperaturaReferencia is less than or equal to DEFAULT_TEMPERATURA_REFERENCIA
        defaultPacienteShouldBeFound("temperaturaReferencia.lessThanOrEqual=" + DEFAULT_TEMPERATURA_REFERENCIA);

        // Get all the pacienteList where temperaturaReferencia is less than or equal to SMALLER_TEMPERATURA_REFERENCIA
        defaultPacienteShouldNotBeFound("temperaturaReferencia.lessThanOrEqual=" + SMALLER_TEMPERATURA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByTemperaturaReferenciaIsLessThanSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where temperaturaReferencia is less than DEFAULT_TEMPERATURA_REFERENCIA
        defaultPacienteShouldNotBeFound("temperaturaReferencia.lessThan=" + DEFAULT_TEMPERATURA_REFERENCIA);

        // Get all the pacienteList where temperaturaReferencia is less than UPDATED_TEMPERATURA_REFERENCIA
        defaultPacienteShouldBeFound("temperaturaReferencia.lessThan=" + UPDATED_TEMPERATURA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByTemperaturaReferenciaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where temperaturaReferencia is greater than DEFAULT_TEMPERATURA_REFERENCIA
        defaultPacienteShouldNotBeFound("temperaturaReferencia.greaterThan=" + DEFAULT_TEMPERATURA_REFERENCIA);

        // Get all the pacienteList where temperaturaReferencia is greater than SMALLER_TEMPERATURA_REFERENCIA
        defaultPacienteShouldBeFound("temperaturaReferencia.greaterThan=" + SMALLER_TEMPERATURA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByRitmoCardiacoReferenciaIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where ritmoCardiacoReferencia equals to DEFAULT_RITMO_CARDIACO_REFERENCIA
        defaultPacienteShouldBeFound("ritmoCardiacoReferencia.equals=" + DEFAULT_RITMO_CARDIACO_REFERENCIA);

        // Get all the pacienteList where ritmoCardiacoReferencia equals to UPDATED_RITMO_CARDIACO_REFERENCIA
        defaultPacienteShouldNotBeFound("ritmoCardiacoReferencia.equals=" + UPDATED_RITMO_CARDIACO_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByRitmoCardiacoReferenciaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where ritmoCardiacoReferencia not equals to DEFAULT_RITMO_CARDIACO_REFERENCIA
        defaultPacienteShouldNotBeFound("ritmoCardiacoReferencia.notEquals=" + DEFAULT_RITMO_CARDIACO_REFERENCIA);

        // Get all the pacienteList where ritmoCardiacoReferencia not equals to UPDATED_RITMO_CARDIACO_REFERENCIA
        defaultPacienteShouldBeFound("ritmoCardiacoReferencia.notEquals=" + UPDATED_RITMO_CARDIACO_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByRitmoCardiacoReferenciaIsInShouldWork() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where ritmoCardiacoReferencia in DEFAULT_RITMO_CARDIACO_REFERENCIA or UPDATED_RITMO_CARDIACO_REFERENCIA
        defaultPacienteShouldBeFound(
            "ritmoCardiacoReferencia.in=" + DEFAULT_RITMO_CARDIACO_REFERENCIA + "," + UPDATED_RITMO_CARDIACO_REFERENCIA
        );

        // Get all the pacienteList where ritmoCardiacoReferencia equals to UPDATED_RITMO_CARDIACO_REFERENCIA
        defaultPacienteShouldNotBeFound("ritmoCardiacoReferencia.in=" + UPDATED_RITMO_CARDIACO_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByRitmoCardiacoReferenciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where ritmoCardiacoReferencia is not null
        defaultPacienteShouldBeFound("ritmoCardiacoReferencia.specified=true");

        // Get all the pacienteList where ritmoCardiacoReferencia is null
        defaultPacienteShouldNotBeFound("ritmoCardiacoReferencia.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByRitmoCardiacoReferenciaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where ritmoCardiacoReferencia is greater than or equal to DEFAULT_RITMO_CARDIACO_REFERENCIA
        defaultPacienteShouldBeFound("ritmoCardiacoReferencia.greaterThanOrEqual=" + DEFAULT_RITMO_CARDIACO_REFERENCIA);

        // Get all the pacienteList where ritmoCardiacoReferencia is greater than or equal to UPDATED_RITMO_CARDIACO_REFERENCIA
        defaultPacienteShouldNotBeFound("ritmoCardiacoReferencia.greaterThanOrEqual=" + UPDATED_RITMO_CARDIACO_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByRitmoCardiacoReferenciaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where ritmoCardiacoReferencia is less than or equal to DEFAULT_RITMO_CARDIACO_REFERENCIA
        defaultPacienteShouldBeFound("ritmoCardiacoReferencia.lessThanOrEqual=" + DEFAULT_RITMO_CARDIACO_REFERENCIA);

        // Get all the pacienteList where ritmoCardiacoReferencia is less than or equal to SMALLER_RITMO_CARDIACO_REFERENCIA
        defaultPacienteShouldNotBeFound("ritmoCardiacoReferencia.lessThanOrEqual=" + SMALLER_RITMO_CARDIACO_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByRitmoCardiacoReferenciaIsLessThanSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where ritmoCardiacoReferencia is less than DEFAULT_RITMO_CARDIACO_REFERENCIA
        defaultPacienteShouldNotBeFound("ritmoCardiacoReferencia.lessThan=" + DEFAULT_RITMO_CARDIACO_REFERENCIA);

        // Get all the pacienteList where ritmoCardiacoReferencia is less than UPDATED_RITMO_CARDIACO_REFERENCIA
        defaultPacienteShouldBeFound("ritmoCardiacoReferencia.lessThan=" + UPDATED_RITMO_CARDIACO_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByRitmoCardiacoReferenciaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where ritmoCardiacoReferencia is greater than DEFAULT_RITMO_CARDIACO_REFERENCIA
        defaultPacienteShouldNotBeFound("ritmoCardiacoReferencia.greaterThan=" + DEFAULT_RITMO_CARDIACO_REFERENCIA);

        // Get all the pacienteList where ritmoCardiacoReferencia is greater than SMALLER_RITMO_CARDIACO_REFERENCIA
        defaultPacienteShouldBeFound("ritmoCardiacoReferencia.greaterThan=" + SMALLER_RITMO_CARDIACO_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByPresionSistolicaReferenciaIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where presionSistolicaReferencia equals to DEFAULT_PRESION_SISTOLICA_REFERENCIA
        defaultPacienteShouldBeFound("presionSistolicaReferencia.equals=" + DEFAULT_PRESION_SISTOLICA_REFERENCIA);

        // Get all the pacienteList where presionSistolicaReferencia equals to UPDATED_PRESION_SISTOLICA_REFERENCIA
        defaultPacienteShouldNotBeFound("presionSistolicaReferencia.equals=" + UPDATED_PRESION_SISTOLICA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByPresionSistolicaReferenciaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where presionSistolicaReferencia not equals to DEFAULT_PRESION_SISTOLICA_REFERENCIA
        defaultPacienteShouldNotBeFound("presionSistolicaReferencia.notEquals=" + DEFAULT_PRESION_SISTOLICA_REFERENCIA);

        // Get all the pacienteList where presionSistolicaReferencia not equals to UPDATED_PRESION_SISTOLICA_REFERENCIA
        defaultPacienteShouldBeFound("presionSistolicaReferencia.notEquals=" + UPDATED_PRESION_SISTOLICA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByPresionSistolicaReferenciaIsInShouldWork() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where presionSistolicaReferencia in DEFAULT_PRESION_SISTOLICA_REFERENCIA or UPDATED_PRESION_SISTOLICA_REFERENCIA
        defaultPacienteShouldBeFound(
            "presionSistolicaReferencia.in=" + DEFAULT_PRESION_SISTOLICA_REFERENCIA + "," + UPDATED_PRESION_SISTOLICA_REFERENCIA
        );

        // Get all the pacienteList where presionSistolicaReferencia equals to UPDATED_PRESION_SISTOLICA_REFERENCIA
        defaultPacienteShouldNotBeFound("presionSistolicaReferencia.in=" + UPDATED_PRESION_SISTOLICA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByPresionSistolicaReferenciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where presionSistolicaReferencia is not null
        defaultPacienteShouldBeFound("presionSistolicaReferencia.specified=true");

        // Get all the pacienteList where presionSistolicaReferencia is null
        defaultPacienteShouldNotBeFound("presionSistolicaReferencia.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByPresionSistolicaReferenciaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where presionSistolicaReferencia is greater than or equal to DEFAULT_PRESION_SISTOLICA_REFERENCIA
        defaultPacienteShouldBeFound("presionSistolicaReferencia.greaterThanOrEqual=" + DEFAULT_PRESION_SISTOLICA_REFERENCIA);

        // Get all the pacienteList where presionSistolicaReferencia is greater than or equal to UPDATED_PRESION_SISTOLICA_REFERENCIA
        defaultPacienteShouldNotBeFound("presionSistolicaReferencia.greaterThanOrEqual=" + UPDATED_PRESION_SISTOLICA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByPresionSistolicaReferenciaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where presionSistolicaReferencia is less than or equal to DEFAULT_PRESION_SISTOLICA_REFERENCIA
        defaultPacienteShouldBeFound("presionSistolicaReferencia.lessThanOrEqual=" + DEFAULT_PRESION_SISTOLICA_REFERENCIA);

        // Get all the pacienteList where presionSistolicaReferencia is less than or equal to SMALLER_PRESION_SISTOLICA_REFERENCIA
        defaultPacienteShouldNotBeFound("presionSistolicaReferencia.lessThanOrEqual=" + SMALLER_PRESION_SISTOLICA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByPresionSistolicaReferenciaIsLessThanSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where presionSistolicaReferencia is less than DEFAULT_PRESION_SISTOLICA_REFERENCIA
        defaultPacienteShouldNotBeFound("presionSistolicaReferencia.lessThan=" + DEFAULT_PRESION_SISTOLICA_REFERENCIA);

        // Get all the pacienteList where presionSistolicaReferencia is less than UPDATED_PRESION_SISTOLICA_REFERENCIA
        defaultPacienteShouldBeFound("presionSistolicaReferencia.lessThan=" + UPDATED_PRESION_SISTOLICA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByPresionSistolicaReferenciaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where presionSistolicaReferencia is greater than DEFAULT_PRESION_SISTOLICA_REFERENCIA
        defaultPacienteShouldNotBeFound("presionSistolicaReferencia.greaterThan=" + DEFAULT_PRESION_SISTOLICA_REFERENCIA);

        // Get all the pacienteList where presionSistolicaReferencia is greater than SMALLER_PRESION_SISTOLICA_REFERENCIA
        defaultPacienteShouldBeFound("presionSistolicaReferencia.greaterThan=" + SMALLER_PRESION_SISTOLICA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByPresionDistolicaReferenciaIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where presionDistolicaReferencia equals to DEFAULT_PRESION_DISTOLICA_REFERENCIA
        defaultPacienteShouldBeFound("presionDistolicaReferencia.equals=" + DEFAULT_PRESION_DISTOLICA_REFERENCIA);

        // Get all the pacienteList where presionDistolicaReferencia equals to UPDATED_PRESION_DISTOLICA_REFERENCIA
        defaultPacienteShouldNotBeFound("presionDistolicaReferencia.equals=" + UPDATED_PRESION_DISTOLICA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByPresionDistolicaReferenciaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where presionDistolicaReferencia not equals to DEFAULT_PRESION_DISTOLICA_REFERENCIA
        defaultPacienteShouldNotBeFound("presionDistolicaReferencia.notEquals=" + DEFAULT_PRESION_DISTOLICA_REFERENCIA);

        // Get all the pacienteList where presionDistolicaReferencia not equals to UPDATED_PRESION_DISTOLICA_REFERENCIA
        defaultPacienteShouldBeFound("presionDistolicaReferencia.notEquals=" + UPDATED_PRESION_DISTOLICA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByPresionDistolicaReferenciaIsInShouldWork() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where presionDistolicaReferencia in DEFAULT_PRESION_DISTOLICA_REFERENCIA or UPDATED_PRESION_DISTOLICA_REFERENCIA
        defaultPacienteShouldBeFound(
            "presionDistolicaReferencia.in=" + DEFAULT_PRESION_DISTOLICA_REFERENCIA + "," + UPDATED_PRESION_DISTOLICA_REFERENCIA
        );

        // Get all the pacienteList where presionDistolicaReferencia equals to UPDATED_PRESION_DISTOLICA_REFERENCIA
        defaultPacienteShouldNotBeFound("presionDistolicaReferencia.in=" + UPDATED_PRESION_DISTOLICA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByPresionDistolicaReferenciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where presionDistolicaReferencia is not null
        defaultPacienteShouldBeFound("presionDistolicaReferencia.specified=true");

        // Get all the pacienteList where presionDistolicaReferencia is null
        defaultPacienteShouldNotBeFound("presionDistolicaReferencia.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByPresionDistolicaReferenciaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where presionDistolicaReferencia is greater than or equal to DEFAULT_PRESION_DISTOLICA_REFERENCIA
        defaultPacienteShouldBeFound("presionDistolicaReferencia.greaterThanOrEqual=" + DEFAULT_PRESION_DISTOLICA_REFERENCIA);

        // Get all the pacienteList where presionDistolicaReferencia is greater than or equal to UPDATED_PRESION_DISTOLICA_REFERENCIA
        defaultPacienteShouldNotBeFound("presionDistolicaReferencia.greaterThanOrEqual=" + UPDATED_PRESION_DISTOLICA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByPresionDistolicaReferenciaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where presionDistolicaReferencia is less than or equal to DEFAULT_PRESION_DISTOLICA_REFERENCIA
        defaultPacienteShouldBeFound("presionDistolicaReferencia.lessThanOrEqual=" + DEFAULT_PRESION_DISTOLICA_REFERENCIA);

        // Get all the pacienteList where presionDistolicaReferencia is less than or equal to SMALLER_PRESION_DISTOLICA_REFERENCIA
        defaultPacienteShouldNotBeFound("presionDistolicaReferencia.lessThanOrEqual=" + SMALLER_PRESION_DISTOLICA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByPresionDistolicaReferenciaIsLessThanSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where presionDistolicaReferencia is less than DEFAULT_PRESION_DISTOLICA_REFERENCIA
        defaultPacienteShouldNotBeFound("presionDistolicaReferencia.lessThan=" + DEFAULT_PRESION_DISTOLICA_REFERENCIA);

        // Get all the pacienteList where presionDistolicaReferencia is less than UPDATED_PRESION_DISTOLICA_REFERENCIA
        defaultPacienteShouldBeFound("presionDistolicaReferencia.lessThan=" + UPDATED_PRESION_DISTOLICA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByPresionDistolicaReferenciaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where presionDistolicaReferencia is greater than DEFAULT_PRESION_DISTOLICA_REFERENCIA
        defaultPacienteShouldNotBeFound("presionDistolicaReferencia.greaterThan=" + DEFAULT_PRESION_DISTOLICA_REFERENCIA);

        // Get all the pacienteList where presionDistolicaReferencia is greater than SMALLER_PRESION_DISTOLICA_REFERENCIA
        defaultPacienteShouldBeFound("presionDistolicaReferencia.greaterThan=" + SMALLER_PRESION_DISTOLICA_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByPasosReferenciaIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where pasosReferencia equals to DEFAULT_PASOS_REFERENCIA
        defaultPacienteShouldBeFound("pasosReferencia.equals=" + DEFAULT_PASOS_REFERENCIA);

        // Get all the pacienteList where pasosReferencia equals to UPDATED_PASOS_REFERENCIA
        defaultPacienteShouldNotBeFound("pasosReferencia.equals=" + UPDATED_PASOS_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByPasosReferenciaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where pasosReferencia not equals to DEFAULT_PASOS_REFERENCIA
        defaultPacienteShouldNotBeFound("pasosReferencia.notEquals=" + DEFAULT_PASOS_REFERENCIA);

        // Get all the pacienteList where pasosReferencia not equals to UPDATED_PASOS_REFERENCIA
        defaultPacienteShouldBeFound("pasosReferencia.notEquals=" + UPDATED_PASOS_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByPasosReferenciaIsInShouldWork() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where pasosReferencia in DEFAULT_PASOS_REFERENCIA or UPDATED_PASOS_REFERENCIA
        defaultPacienteShouldBeFound("pasosReferencia.in=" + DEFAULT_PASOS_REFERENCIA + "," + UPDATED_PASOS_REFERENCIA);

        // Get all the pacienteList where pasosReferencia equals to UPDATED_PASOS_REFERENCIA
        defaultPacienteShouldNotBeFound("pasosReferencia.in=" + UPDATED_PASOS_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByPasosReferenciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where pasosReferencia is not null
        defaultPacienteShouldBeFound("pasosReferencia.specified=true");

        // Get all the pacienteList where pasosReferencia is null
        defaultPacienteShouldNotBeFound("pasosReferencia.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByPasosReferenciaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where pasosReferencia is greater than or equal to DEFAULT_PASOS_REFERENCIA
        defaultPacienteShouldBeFound("pasosReferencia.greaterThanOrEqual=" + DEFAULT_PASOS_REFERENCIA);

        // Get all the pacienteList where pasosReferencia is greater than or equal to UPDATED_PASOS_REFERENCIA
        defaultPacienteShouldNotBeFound("pasosReferencia.greaterThanOrEqual=" + UPDATED_PASOS_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByPasosReferenciaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where pasosReferencia is less than or equal to DEFAULT_PASOS_REFERENCIA
        defaultPacienteShouldBeFound("pasosReferencia.lessThanOrEqual=" + DEFAULT_PASOS_REFERENCIA);

        // Get all the pacienteList where pasosReferencia is less than or equal to SMALLER_PASOS_REFERENCIA
        defaultPacienteShouldNotBeFound("pasosReferencia.lessThanOrEqual=" + SMALLER_PASOS_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByPasosReferenciaIsLessThanSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where pasosReferencia is less than DEFAULT_PASOS_REFERENCIA
        defaultPacienteShouldNotBeFound("pasosReferencia.lessThan=" + DEFAULT_PASOS_REFERENCIA);

        // Get all the pacienteList where pasosReferencia is less than UPDATED_PASOS_REFERENCIA
        defaultPacienteShouldBeFound("pasosReferencia.lessThan=" + UPDATED_PASOS_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByPasosReferenciaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where pasosReferencia is greater than DEFAULT_PASOS_REFERENCIA
        defaultPacienteShouldNotBeFound("pasosReferencia.greaterThan=" + DEFAULT_PASOS_REFERENCIA);

        // Get all the pacienteList where pasosReferencia is greater than SMALLER_PASOS_REFERENCIA
        defaultPacienteShouldBeFound("pasosReferencia.greaterThan=" + SMALLER_PASOS_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByCaloriasReferenciaIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where caloriasReferencia equals to DEFAULT_CALORIAS_REFERENCIA
        defaultPacienteShouldBeFound("caloriasReferencia.equals=" + DEFAULT_CALORIAS_REFERENCIA);

        // Get all the pacienteList where caloriasReferencia equals to UPDATED_CALORIAS_REFERENCIA
        defaultPacienteShouldNotBeFound("caloriasReferencia.equals=" + UPDATED_CALORIAS_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByCaloriasReferenciaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where caloriasReferencia not equals to DEFAULT_CALORIAS_REFERENCIA
        defaultPacienteShouldNotBeFound("caloriasReferencia.notEquals=" + DEFAULT_CALORIAS_REFERENCIA);

        // Get all the pacienteList where caloriasReferencia not equals to UPDATED_CALORIAS_REFERENCIA
        defaultPacienteShouldBeFound("caloriasReferencia.notEquals=" + UPDATED_CALORIAS_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByCaloriasReferenciaIsInShouldWork() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where caloriasReferencia in DEFAULT_CALORIAS_REFERENCIA or UPDATED_CALORIAS_REFERENCIA
        defaultPacienteShouldBeFound("caloriasReferencia.in=" + DEFAULT_CALORIAS_REFERENCIA + "," + UPDATED_CALORIAS_REFERENCIA);

        // Get all the pacienteList where caloriasReferencia equals to UPDATED_CALORIAS_REFERENCIA
        defaultPacienteShouldNotBeFound("caloriasReferencia.in=" + UPDATED_CALORIAS_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByCaloriasReferenciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where caloriasReferencia is not null
        defaultPacienteShouldBeFound("caloriasReferencia.specified=true");

        // Get all the pacienteList where caloriasReferencia is null
        defaultPacienteShouldNotBeFound("caloriasReferencia.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByCaloriasReferenciaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where caloriasReferencia is greater than or equal to DEFAULT_CALORIAS_REFERENCIA
        defaultPacienteShouldBeFound("caloriasReferencia.greaterThanOrEqual=" + DEFAULT_CALORIAS_REFERENCIA);

        // Get all the pacienteList where caloriasReferencia is greater than or equal to UPDATED_CALORIAS_REFERENCIA
        defaultPacienteShouldNotBeFound("caloriasReferencia.greaterThanOrEqual=" + UPDATED_CALORIAS_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByCaloriasReferenciaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where caloriasReferencia is less than or equal to DEFAULT_CALORIAS_REFERENCIA
        defaultPacienteShouldBeFound("caloriasReferencia.lessThanOrEqual=" + DEFAULT_CALORIAS_REFERENCIA);

        // Get all the pacienteList where caloriasReferencia is less than or equal to SMALLER_CALORIAS_REFERENCIA
        defaultPacienteShouldNotBeFound("caloriasReferencia.lessThanOrEqual=" + SMALLER_CALORIAS_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByCaloriasReferenciaIsLessThanSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where caloriasReferencia is less than DEFAULT_CALORIAS_REFERENCIA
        defaultPacienteShouldNotBeFound("caloriasReferencia.lessThan=" + DEFAULT_CALORIAS_REFERENCIA);

        // Get all the pacienteList where caloriasReferencia is less than UPDATED_CALORIAS_REFERENCIA
        defaultPacienteShouldBeFound("caloriasReferencia.lessThan=" + UPDATED_CALORIAS_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByCaloriasReferenciaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where caloriasReferencia is greater than DEFAULT_CALORIAS_REFERENCIA
        defaultPacienteShouldNotBeFound("caloriasReferencia.greaterThan=" + DEFAULT_CALORIAS_REFERENCIA);

        // Get all the pacienteList where caloriasReferencia is greater than SMALLER_CALORIAS_REFERENCIA
        defaultPacienteShouldBeFound("caloriasReferencia.greaterThan=" + SMALLER_CALORIAS_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByMetaReferenciaIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where metaReferencia equals to DEFAULT_META_REFERENCIA
        defaultPacienteShouldBeFound("metaReferencia.equals=" + DEFAULT_META_REFERENCIA);

        // Get all the pacienteList where metaReferencia equals to UPDATED_META_REFERENCIA
        defaultPacienteShouldNotBeFound("metaReferencia.equals=" + UPDATED_META_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByMetaReferenciaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where metaReferencia not equals to DEFAULT_META_REFERENCIA
        defaultPacienteShouldNotBeFound("metaReferencia.notEquals=" + DEFAULT_META_REFERENCIA);

        // Get all the pacienteList where metaReferencia not equals to UPDATED_META_REFERENCIA
        defaultPacienteShouldBeFound("metaReferencia.notEquals=" + UPDATED_META_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByMetaReferenciaIsInShouldWork() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where metaReferencia in DEFAULT_META_REFERENCIA or UPDATED_META_REFERENCIA
        defaultPacienteShouldBeFound("metaReferencia.in=" + DEFAULT_META_REFERENCIA + "," + UPDATED_META_REFERENCIA);

        // Get all the pacienteList where metaReferencia equals to UPDATED_META_REFERENCIA
        defaultPacienteShouldNotBeFound("metaReferencia.in=" + UPDATED_META_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByMetaReferenciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where metaReferencia is not null
        defaultPacienteShouldBeFound("metaReferencia.specified=true");

        // Get all the pacienteList where metaReferencia is null
        defaultPacienteShouldNotBeFound("metaReferencia.specified=false");
    }

    @Test
    @Transactional
    void getAllPacientesByMetaReferenciaContainsSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where metaReferencia contains DEFAULT_META_REFERENCIA
        defaultPacienteShouldBeFound("metaReferencia.contains=" + DEFAULT_META_REFERENCIA);

        // Get all the pacienteList where metaReferencia contains UPDATED_META_REFERENCIA
        defaultPacienteShouldNotBeFound("metaReferencia.contains=" + UPDATED_META_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByMetaReferenciaNotContainsSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList where metaReferencia does not contain DEFAULT_META_REFERENCIA
        defaultPacienteShouldNotBeFound("metaReferencia.doesNotContain=" + DEFAULT_META_REFERENCIA);

        // Get all the pacienteList where metaReferencia does not contain UPDATED_META_REFERENCIA
        defaultPacienteShouldBeFound("metaReferencia.doesNotContain=" + UPDATED_META_REFERENCIA);
    }

    @Test
    @Transactional
    void getAllPacientesByCondicionIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);
        Condicion condicion = CondicionResourceIT.createEntity(em);
        em.persist(condicion);
        em.flush();
        paciente.setCondicion(condicion);
        pacienteRepository.saveAndFlush(paciente);
        Long condicionId = condicion.getId();

        // Get all the pacienteList where condicion equals to condicionId
        defaultPacienteShouldBeFound("condicionId.equals=" + condicionId);

        // Get all the pacienteList where condicion equals to (condicionId + 1)
        defaultPacienteShouldNotBeFound("condicionId.equals=" + (condicionId + 1));
    }

    @Test
    @Transactional
    void getAllPacientesByIpsIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);
        IPS ips = IPSResourceIT.createEntity(em);
        em.persist(ips);
        em.flush();
        paciente.setIps(ips);
        pacienteRepository.saveAndFlush(paciente);
        Long ipsId = ips.getId();

        // Get all the pacienteList where ips equals to ipsId
        defaultPacienteShouldBeFound("ipsId.equals=" + ipsId);

        // Get all the pacienteList where ips equals to (ipsId + 1)
        defaultPacienteShouldNotBeFound("ipsId.equals=" + (ipsId + 1));
    }

    @Test
    @Transactional
    void getAllPacientesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        paciente.setUser(user);
        pacienteRepository.saveAndFlush(paciente);
        String userId = user.getId();

        // Get all the pacienteList where user equals to userId
        defaultPacienteShouldBeFound("userId.equals=" + userId);

        // Get all the pacienteList where user equals to "invalid-id"
        defaultPacienteShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    @Test
    @Transactional
    void getAllPacientesByTratamientoIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);
        Tratamieto tratamiento = TratamietoResourceIT.createEntity(em);
        em.persist(tratamiento);
        em.flush();
        paciente.setTratamiento(tratamiento);
        pacienteRepository.saveAndFlush(paciente);
        Long tratamientoId = tratamiento.getId();

        // Get all the pacienteList where tratamiento equals to tratamientoId
        defaultPacienteShouldBeFound("tratamientoId.equals=" + tratamientoId);

        // Get all the pacienteList where tratamiento equals to (tratamientoId + 1)
        defaultPacienteShouldNotBeFound("tratamientoId.equals=" + (tratamientoId + 1));
    }

    @Test
    @Transactional
    void getAllPacientesByFarmaceuticaIsEqualToSomething() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);
        Farmaceutica farmaceutica = FarmaceuticaResourceIT.createEntity(em);
        em.persist(farmaceutica);
        em.flush();
        paciente.setFarmaceutica(farmaceutica);
        pacienteRepository.saveAndFlush(paciente);
        Long farmaceuticaId = farmaceutica.getId();

        // Get all the pacienteList where farmaceutica equals to farmaceuticaId
        defaultPacienteShouldBeFound("farmaceuticaId.equals=" + farmaceuticaId);

        // Get all the pacienteList where farmaceutica equals to (farmaceuticaId + 1)
        defaultPacienteShouldNotBeFound("farmaceuticaId.equals=" + (farmaceuticaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPacienteShouldBeFound(String filter) throws Exception {
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paciente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].tipoIdentificacion").value(hasItem(DEFAULT_TIPO_IDENTIFICACION.toString())))
            .andExpect(jsonPath("$.[*].identificacion").value(hasItem(DEFAULT_IDENTIFICACION)))
            .andExpect(jsonPath("$.[*].edad").value(hasItem(DEFAULT_EDAD)))
            .andExpect(jsonPath("$.[*].sexo").value(hasItem(DEFAULT_SEXO.toString())))
            .andExpect(jsonPath("$.[*].pesoKG").value(hasItem(DEFAULT_PESO_KG.doubleValue())))
            .andExpect(jsonPath("$.[*].estaturaCM").value(hasItem(DEFAULT_ESTATURA_CM)))
            .andExpect(jsonPath("$.[*].oximetriaReferencia").value(hasItem(DEFAULT_OXIMETRIA_REFERENCIA)))
            .andExpect(jsonPath("$.[*].temperaturaReferencia").value(hasItem(DEFAULT_TEMPERATURA_REFERENCIA.doubleValue())))
            .andExpect(jsonPath("$.[*].ritmoCardiacoReferencia").value(hasItem(DEFAULT_RITMO_CARDIACO_REFERENCIA)))
            .andExpect(jsonPath("$.[*].presionSistolicaReferencia").value(hasItem(DEFAULT_PRESION_SISTOLICA_REFERENCIA)))
            .andExpect(jsonPath("$.[*].presionDistolicaReferencia").value(hasItem(DEFAULT_PRESION_DISTOLICA_REFERENCIA)))
            .andExpect(jsonPath("$.[*].comentarios").value(hasItem(DEFAULT_COMENTARIOS.toString())))
            .andExpect(jsonPath("$.[*].pasosReferencia").value(hasItem(DEFAULT_PASOS_REFERENCIA)))
            .andExpect(jsonPath("$.[*].caloriasReferencia").value(hasItem(DEFAULT_CALORIAS_REFERENCIA)))
            .andExpect(jsonPath("$.[*].metaReferencia").value(hasItem(DEFAULT_META_REFERENCIA)));

        // Check, that the count call also returns 1
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPacienteShouldNotBeFound(String filter) throws Exception {
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPacienteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaciente() throws Exception {
        // Get the paciente
        restPacienteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPaciente() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();

        // Update the paciente
        Paciente updatedPaciente = pacienteRepository.findById(paciente.getId()).get();
        // Disconnect from session so that the updates on updatedPaciente are not directly saved in db
        em.detach(updatedPaciente);
        updatedPaciente
            .nombre(UPDATED_NOMBRE)
            .tipoIdentificacion(UPDATED_TIPO_IDENTIFICACION)
            .identificacion(UPDATED_IDENTIFICACION)
            .edad(UPDATED_EDAD)
            .sexo(UPDATED_SEXO)
            .pesoKG(UPDATED_PESO_KG)
            .estaturaCM(UPDATED_ESTATURA_CM)
            .oximetriaReferencia(UPDATED_OXIMETRIA_REFERENCIA)
            .temperaturaReferencia(UPDATED_TEMPERATURA_REFERENCIA)
            .ritmoCardiacoReferencia(UPDATED_RITMO_CARDIACO_REFERENCIA)
            .presionSistolicaReferencia(UPDATED_PRESION_SISTOLICA_REFERENCIA)
            .presionDistolicaReferencia(UPDATED_PRESION_DISTOLICA_REFERENCIA)
            .comentarios(UPDATED_COMENTARIOS)
            .pasosReferencia(UPDATED_PASOS_REFERENCIA)
            .caloriasReferencia(UPDATED_CALORIAS_REFERENCIA)
            .metaReferencia(UPDATED_META_REFERENCIA);

        restPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPaciente.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPaciente))
            )
            .andExpect(status().isOk());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
        Paciente testPaciente = pacienteList.get(pacienteList.size() - 1);
        assertThat(testPaciente.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPaciente.getTipoIdentificacion()).isEqualTo(UPDATED_TIPO_IDENTIFICACION);
        assertThat(testPaciente.getIdentificacion()).isEqualTo(UPDATED_IDENTIFICACION);
        assertThat(testPaciente.getEdad()).isEqualTo(UPDATED_EDAD);
        assertThat(testPaciente.getSexo()).isEqualTo(UPDATED_SEXO);
        assertThat(testPaciente.getPesoKG()).isEqualTo(UPDATED_PESO_KG);
        assertThat(testPaciente.getEstaturaCM()).isEqualTo(UPDATED_ESTATURA_CM);
        assertThat(testPaciente.getOximetriaReferencia()).isEqualTo(UPDATED_OXIMETRIA_REFERENCIA);
        assertThat(testPaciente.getTemperaturaReferencia()).isEqualTo(UPDATED_TEMPERATURA_REFERENCIA);
        assertThat(testPaciente.getRitmoCardiacoReferencia()).isEqualTo(UPDATED_RITMO_CARDIACO_REFERENCIA);
        assertThat(testPaciente.getPresionSistolicaReferencia()).isEqualTo(UPDATED_PRESION_SISTOLICA_REFERENCIA);
        assertThat(testPaciente.getPresionDistolicaReferencia()).isEqualTo(UPDATED_PRESION_DISTOLICA_REFERENCIA);
        assertThat(testPaciente.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
        assertThat(testPaciente.getPasosReferencia()).isEqualTo(UPDATED_PASOS_REFERENCIA);
        assertThat(testPaciente.getCaloriasReferencia()).isEqualTo(UPDATED_CALORIAS_REFERENCIA);
        assertThat(testPaciente.getMetaReferencia()).isEqualTo(UPDATED_META_REFERENCIA);
    }

    @Test
    @Transactional
    void putNonExistingPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();
        paciente.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paciente.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paciente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();
        paciente.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paciente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();
        paciente.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paciente))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePacienteWithPatch() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();

        // Update the paciente using partial update
        Paciente partialUpdatedPaciente = new Paciente();
        partialUpdatedPaciente.setId(paciente.getId());

        partialUpdatedPaciente
            .tipoIdentificacion(UPDATED_TIPO_IDENTIFICACION)
            .identificacion(UPDATED_IDENTIFICACION)
            .estaturaCM(UPDATED_ESTATURA_CM)
            .oximetriaReferencia(UPDATED_OXIMETRIA_REFERENCIA)
            .ritmoCardiacoReferencia(UPDATED_RITMO_CARDIACO_REFERENCIA)
            .comentarios(UPDATED_COMENTARIOS)
            .pasosReferencia(UPDATED_PASOS_REFERENCIA)
            .metaReferencia(UPDATED_META_REFERENCIA);

        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaciente.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaciente))
            )
            .andExpect(status().isOk());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
        Paciente testPaciente = pacienteList.get(pacienteList.size() - 1);
        assertThat(testPaciente.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPaciente.getTipoIdentificacion()).isEqualTo(UPDATED_TIPO_IDENTIFICACION);
        assertThat(testPaciente.getIdentificacion()).isEqualTo(UPDATED_IDENTIFICACION);
        assertThat(testPaciente.getEdad()).isEqualTo(DEFAULT_EDAD);
        assertThat(testPaciente.getSexo()).isEqualTo(DEFAULT_SEXO);
        assertThat(testPaciente.getPesoKG()).isEqualTo(DEFAULT_PESO_KG);
        assertThat(testPaciente.getEstaturaCM()).isEqualTo(UPDATED_ESTATURA_CM);
        assertThat(testPaciente.getOximetriaReferencia()).isEqualTo(UPDATED_OXIMETRIA_REFERENCIA);
        assertThat(testPaciente.getTemperaturaReferencia()).isEqualTo(DEFAULT_TEMPERATURA_REFERENCIA);
        assertThat(testPaciente.getRitmoCardiacoReferencia()).isEqualTo(UPDATED_RITMO_CARDIACO_REFERENCIA);
        assertThat(testPaciente.getPresionSistolicaReferencia()).isEqualTo(DEFAULT_PRESION_SISTOLICA_REFERENCIA);
        assertThat(testPaciente.getPresionDistolicaReferencia()).isEqualTo(DEFAULT_PRESION_DISTOLICA_REFERENCIA);
        assertThat(testPaciente.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
        assertThat(testPaciente.getPasosReferencia()).isEqualTo(UPDATED_PASOS_REFERENCIA);
        assertThat(testPaciente.getCaloriasReferencia()).isEqualTo(DEFAULT_CALORIAS_REFERENCIA);
        assertThat(testPaciente.getMetaReferencia()).isEqualTo(UPDATED_META_REFERENCIA);
    }

    @Test
    @Transactional
    void fullUpdatePacienteWithPatch() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();

        // Update the paciente using partial update
        Paciente partialUpdatedPaciente = new Paciente();
        partialUpdatedPaciente.setId(paciente.getId());

        partialUpdatedPaciente
            .nombre(UPDATED_NOMBRE)
            .tipoIdentificacion(UPDATED_TIPO_IDENTIFICACION)
            .identificacion(UPDATED_IDENTIFICACION)
            .edad(UPDATED_EDAD)
            .sexo(UPDATED_SEXO)
            .pesoKG(UPDATED_PESO_KG)
            .estaturaCM(UPDATED_ESTATURA_CM)
            .oximetriaReferencia(UPDATED_OXIMETRIA_REFERENCIA)
            .temperaturaReferencia(UPDATED_TEMPERATURA_REFERENCIA)
            .ritmoCardiacoReferencia(UPDATED_RITMO_CARDIACO_REFERENCIA)
            .presionSistolicaReferencia(UPDATED_PRESION_SISTOLICA_REFERENCIA)
            .presionDistolicaReferencia(UPDATED_PRESION_DISTOLICA_REFERENCIA)
            .comentarios(UPDATED_COMENTARIOS)
            .pasosReferencia(UPDATED_PASOS_REFERENCIA)
            .caloriasReferencia(UPDATED_CALORIAS_REFERENCIA)
            .metaReferencia(UPDATED_META_REFERENCIA);

        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaciente.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaciente))
            )
            .andExpect(status().isOk());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
        Paciente testPaciente = pacienteList.get(pacienteList.size() - 1);
        assertThat(testPaciente.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPaciente.getTipoIdentificacion()).isEqualTo(UPDATED_TIPO_IDENTIFICACION);
        assertThat(testPaciente.getIdentificacion()).isEqualTo(UPDATED_IDENTIFICACION);
        assertThat(testPaciente.getEdad()).isEqualTo(UPDATED_EDAD);
        assertThat(testPaciente.getSexo()).isEqualTo(UPDATED_SEXO);
        assertThat(testPaciente.getPesoKG()).isEqualTo(UPDATED_PESO_KG);
        assertThat(testPaciente.getEstaturaCM()).isEqualTo(UPDATED_ESTATURA_CM);
        assertThat(testPaciente.getOximetriaReferencia()).isEqualTo(UPDATED_OXIMETRIA_REFERENCIA);
        assertThat(testPaciente.getTemperaturaReferencia()).isEqualTo(UPDATED_TEMPERATURA_REFERENCIA);
        assertThat(testPaciente.getRitmoCardiacoReferencia()).isEqualTo(UPDATED_RITMO_CARDIACO_REFERENCIA);
        assertThat(testPaciente.getPresionSistolicaReferencia()).isEqualTo(UPDATED_PRESION_SISTOLICA_REFERENCIA);
        assertThat(testPaciente.getPresionDistolicaReferencia()).isEqualTo(UPDATED_PRESION_DISTOLICA_REFERENCIA);
        assertThat(testPaciente.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
        assertThat(testPaciente.getPasosReferencia()).isEqualTo(UPDATED_PASOS_REFERENCIA);
        assertThat(testPaciente.getCaloriasReferencia()).isEqualTo(UPDATED_CALORIAS_REFERENCIA);
        assertThat(testPaciente.getMetaReferencia()).isEqualTo(UPDATED_META_REFERENCIA);
    }

    @Test
    @Transactional
    void patchNonExistingPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();
        paciente.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paciente.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paciente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();
        paciente.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paciente))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();
        paciente.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPacienteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paciente))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaciente() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        int databaseSizeBeforeDelete = pacienteRepository.findAll().size();

        // Delete the paciente
        restPacienteMockMvc
            .perform(delete(ENTITY_API_URL_ID, paciente.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

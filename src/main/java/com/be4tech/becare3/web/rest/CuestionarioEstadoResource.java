package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.CuestionarioEstado;
import com.be4tech.becare3.repository.CuestionarioEstadoRepository;
import com.be4tech.becare3.service.CuestionarioEstadoQueryService;
import com.be4tech.becare3.service.CuestionarioEstadoService;
import com.be4tech.becare3.service.criteria.CuestionarioEstadoCriteria;
import com.be4tech.becare3.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.be4tech.becare3.domain.CuestionarioEstado}.
 */
@RestController
@RequestMapping("/api")
public class CuestionarioEstadoResource {

    private final Logger log = LoggerFactory.getLogger(CuestionarioEstadoResource.class);

    private static final String ENTITY_NAME = "cuestionarioEstado";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CuestionarioEstadoService cuestionarioEstadoService;

    private final CuestionarioEstadoRepository cuestionarioEstadoRepository;

    private final CuestionarioEstadoQueryService cuestionarioEstadoQueryService;

    public CuestionarioEstadoResource(
        CuestionarioEstadoService cuestionarioEstadoService,
        CuestionarioEstadoRepository cuestionarioEstadoRepository,
        CuestionarioEstadoQueryService cuestionarioEstadoQueryService
    ) {
        this.cuestionarioEstadoService = cuestionarioEstadoService;
        this.cuestionarioEstadoRepository = cuestionarioEstadoRepository;
        this.cuestionarioEstadoQueryService = cuestionarioEstadoQueryService;
    }

    /**
     * {@code POST  /cuestionario-estados} : Create a new cuestionarioEstado.
     *
     * @param cuestionarioEstado the cuestionarioEstado to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cuestionarioEstado, or with status {@code 400 (Bad Request)} if the cuestionarioEstado has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cuestionario-estados")
    public ResponseEntity<CuestionarioEstado> createCuestionarioEstado(@RequestBody CuestionarioEstado cuestionarioEstado)
        throws URISyntaxException {
        log.debug("REST request to save CuestionarioEstado : {}", cuestionarioEstado);
        if (cuestionarioEstado.getId() != null) {
            throw new BadRequestAlertException("A new cuestionarioEstado cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CuestionarioEstado result = cuestionarioEstadoService.save(cuestionarioEstado);
        return ResponseEntity
            .created(new URI("/api/cuestionario-estados/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cuestionario-estados/:id} : Updates an existing cuestionarioEstado.
     *
     * @param id the id of the cuestionarioEstado to save.
     * @param cuestionarioEstado the cuestionarioEstado to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cuestionarioEstado,
     * or with status {@code 400 (Bad Request)} if the cuestionarioEstado is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cuestionarioEstado couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cuestionario-estados/{id}")
    public ResponseEntity<CuestionarioEstado> updateCuestionarioEstado(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CuestionarioEstado cuestionarioEstado
    ) throws URISyntaxException {
        log.debug("REST request to update CuestionarioEstado : {}, {}", id, cuestionarioEstado);
        if (cuestionarioEstado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cuestionarioEstado.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cuestionarioEstadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CuestionarioEstado result = cuestionarioEstadoService.save(cuestionarioEstado);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cuestionarioEstado.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cuestionario-estados/:id} : Partial updates given fields of an existing cuestionarioEstado, field will ignore if it is null
     *
     * @param id the id of the cuestionarioEstado to save.
     * @param cuestionarioEstado the cuestionarioEstado to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cuestionarioEstado,
     * or with status {@code 400 (Bad Request)} if the cuestionarioEstado is not valid,
     * or with status {@code 404 (Not Found)} if the cuestionarioEstado is not found,
     * or with status {@code 500 (Internal Server Error)} if the cuestionarioEstado couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cuestionario-estados/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CuestionarioEstado> partialUpdateCuestionarioEstado(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CuestionarioEstado cuestionarioEstado
    ) throws URISyntaxException {
        log.debug("REST request to partial update CuestionarioEstado partially : {}, {}", id, cuestionarioEstado);
        if (cuestionarioEstado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cuestionarioEstado.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cuestionarioEstadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CuestionarioEstado> result = cuestionarioEstadoService.partialUpdate(cuestionarioEstado);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cuestionarioEstado.getId().toString())
        );
    }

    /**
     * {@code GET  /cuestionario-estados} : get all the cuestionarioEstados.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cuestionarioEstados in body.
     */
    @GetMapping("/cuestionario-estados")
    public ResponseEntity<List<CuestionarioEstado>> getAllCuestionarioEstados(CuestionarioEstadoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CuestionarioEstados by criteria: {}", criteria);
        Page<CuestionarioEstado> page = cuestionarioEstadoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cuestionario-estados/count} : count all the cuestionarioEstados.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cuestionario-estados/count")
    public ResponseEntity<Long> countCuestionarioEstados(CuestionarioEstadoCriteria criteria) {
        log.debug("REST request to count CuestionarioEstados by criteria: {}", criteria);
        return ResponseEntity.ok().body(cuestionarioEstadoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cuestionario-estados/:id} : get the "id" cuestionarioEstado.
     *
     * @param id the id of the cuestionarioEstado to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cuestionarioEstado, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cuestionario-estados/{id}")
    public ResponseEntity<CuestionarioEstado> getCuestionarioEstado(@PathVariable Long id) {
        log.debug("REST request to get CuestionarioEstado : {}", id);
        Optional<CuestionarioEstado> cuestionarioEstado = cuestionarioEstadoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cuestionarioEstado);
    }

    /**
     * {@code DELETE  /cuestionario-estados/:id} : delete the "id" cuestionarioEstado.
     *
     * @param id the id of the cuestionarioEstado to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cuestionario-estados/{id}")
    public ResponseEntity<Void> deleteCuestionarioEstado(@PathVariable Long id) {
        log.debug("REST request to delete CuestionarioEstado : {}", id);
        cuestionarioEstadoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.FrecuenciaCardiaca;
import com.be4tech.becare3.repository.FrecuenciaCardiacaRepository;
import com.be4tech.becare3.service.FrecuenciaCardiacaQueryService;
import com.be4tech.becare3.service.FrecuenciaCardiacaService;
import com.be4tech.becare3.service.criteria.FrecuenciaCardiacaCriteria;
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
 * REST controller for managing {@link com.be4tech.becare3.domain.FrecuenciaCardiaca}.
 */
@RestController
@RequestMapping("/api")
public class FrecuenciaCardiacaResource {

    private final Logger log = LoggerFactory.getLogger(FrecuenciaCardiacaResource.class);

    private static final String ENTITY_NAME = "frecuenciaCardiaca";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FrecuenciaCardiacaService frecuenciaCardiacaService;

    private final FrecuenciaCardiacaRepository frecuenciaCardiacaRepository;

    private final FrecuenciaCardiacaQueryService frecuenciaCardiacaQueryService;

    public FrecuenciaCardiacaResource(
        FrecuenciaCardiacaService frecuenciaCardiacaService,
        FrecuenciaCardiacaRepository frecuenciaCardiacaRepository,
        FrecuenciaCardiacaQueryService frecuenciaCardiacaQueryService
    ) {
        this.frecuenciaCardiacaService = frecuenciaCardiacaService;
        this.frecuenciaCardiacaRepository = frecuenciaCardiacaRepository;
        this.frecuenciaCardiacaQueryService = frecuenciaCardiacaQueryService;
    }

    /**
     * {@code POST  /frecuencia-cardiacas} : Create a new frecuenciaCardiaca.
     *
     * @param frecuenciaCardiaca the frecuenciaCardiaca to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new frecuenciaCardiaca, or with status {@code 400 (Bad Request)} if the frecuenciaCardiaca has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/frecuencia-cardiacas")
    public ResponseEntity<FrecuenciaCardiaca> createFrecuenciaCardiaca(@RequestBody FrecuenciaCardiaca frecuenciaCardiaca)
        throws URISyntaxException {
        log.debug("REST request to save FrecuenciaCardiaca : {}", frecuenciaCardiaca);
        if (frecuenciaCardiaca.getId() != null) {
            throw new BadRequestAlertException("A new frecuenciaCardiaca cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FrecuenciaCardiaca result = frecuenciaCardiacaService.save(frecuenciaCardiaca);
        return ResponseEntity
            .created(new URI("/api/frecuencia-cardiacas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /frecuencia-cardiacas/:id} : Updates an existing frecuenciaCardiaca.
     *
     * @param id the id of the frecuenciaCardiaca to save.
     * @param frecuenciaCardiaca the frecuenciaCardiaca to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated frecuenciaCardiaca,
     * or with status {@code 400 (Bad Request)} if the frecuenciaCardiaca is not valid,
     * or with status {@code 500 (Internal Server Error)} if the frecuenciaCardiaca couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/frecuencia-cardiacas/{id}")
    public ResponseEntity<FrecuenciaCardiaca> updateFrecuenciaCardiaca(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FrecuenciaCardiaca frecuenciaCardiaca
    ) throws URISyntaxException {
        log.debug("REST request to update FrecuenciaCardiaca : {}, {}", id, frecuenciaCardiaca);
        if (frecuenciaCardiaca.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, frecuenciaCardiaca.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!frecuenciaCardiacaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FrecuenciaCardiaca result = frecuenciaCardiacaService.save(frecuenciaCardiaca);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, frecuenciaCardiaca.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /frecuencia-cardiacas/:id} : Partial updates given fields of an existing frecuenciaCardiaca, field will ignore if it is null
     *
     * @param id the id of the frecuenciaCardiaca to save.
     * @param frecuenciaCardiaca the frecuenciaCardiaca to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated frecuenciaCardiaca,
     * or with status {@code 400 (Bad Request)} if the frecuenciaCardiaca is not valid,
     * or with status {@code 404 (Not Found)} if the frecuenciaCardiaca is not found,
     * or with status {@code 500 (Internal Server Error)} if the frecuenciaCardiaca couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/frecuencia-cardiacas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FrecuenciaCardiaca> partialUpdateFrecuenciaCardiaca(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FrecuenciaCardiaca frecuenciaCardiaca
    ) throws URISyntaxException {
        log.debug("REST request to partial update FrecuenciaCardiaca partially : {}, {}", id, frecuenciaCardiaca);
        if (frecuenciaCardiaca.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, frecuenciaCardiaca.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!frecuenciaCardiacaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FrecuenciaCardiaca> result = frecuenciaCardiacaService.partialUpdate(frecuenciaCardiaca);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, frecuenciaCardiaca.getId().toString())
        );
    }

    /**
     * {@code GET  /frecuencia-cardiacas} : get all the frecuenciaCardiacas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of frecuenciaCardiacas in body.
     */
    @GetMapping("/frecuencia-cardiacas")
    public ResponseEntity<List<FrecuenciaCardiaca>> getAllFrecuenciaCardiacas(FrecuenciaCardiacaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FrecuenciaCardiacas by criteria: {}", criteria);
        Page<FrecuenciaCardiaca> page = frecuenciaCardiacaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /frecuencia-cardiacas/count} : count all the frecuenciaCardiacas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/frecuencia-cardiacas/count")
    public ResponseEntity<Long> countFrecuenciaCardiacas(FrecuenciaCardiacaCriteria criteria) {
        log.debug("REST request to count FrecuenciaCardiacas by criteria: {}", criteria);
        return ResponseEntity.ok().body(frecuenciaCardiacaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /frecuencia-cardiacas/:id} : get the "id" frecuenciaCardiaca.
     *
     * @param id the id of the frecuenciaCardiaca to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the frecuenciaCardiaca, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/frecuencia-cardiacas/{id}")
    public ResponseEntity<FrecuenciaCardiaca> getFrecuenciaCardiaca(@PathVariable Long id) {
        log.debug("REST request to get FrecuenciaCardiaca : {}", id);
        Optional<FrecuenciaCardiaca> frecuenciaCardiaca = frecuenciaCardiacaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(frecuenciaCardiaca);
    }

    /**
     * {@code DELETE  /frecuencia-cardiacas/:id} : delete the "id" frecuenciaCardiaca.
     *
     * @param id the id of the frecuenciaCardiaca to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/frecuencia-cardiacas/{id}")
    public ResponseEntity<Void> deleteFrecuenciaCardiaca(@PathVariable Long id) {
        log.debug("REST request to delete FrecuenciaCardiaca : {}", id);
        frecuenciaCardiacaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

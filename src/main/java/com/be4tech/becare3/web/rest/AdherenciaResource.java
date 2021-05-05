package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.Adherencia;
import com.be4tech.becare3.repository.AdherenciaRepository;
import com.be4tech.becare3.service.AdherenciaQueryService;
import com.be4tech.becare3.service.AdherenciaService;
import com.be4tech.becare3.service.criteria.AdherenciaCriteria;
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
 * REST controller for managing {@link com.be4tech.becare3.domain.Adherencia}.
 */
@RestController
@RequestMapping("/api")
public class AdherenciaResource {

    private final Logger log = LoggerFactory.getLogger(AdherenciaResource.class);

    private static final String ENTITY_NAME = "adherencia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdherenciaService adherenciaService;

    private final AdherenciaRepository adherenciaRepository;

    private final AdherenciaQueryService adherenciaQueryService;

    public AdherenciaResource(
        AdherenciaService adherenciaService,
        AdherenciaRepository adherenciaRepository,
        AdherenciaQueryService adherenciaQueryService
    ) {
        this.adherenciaService = adherenciaService;
        this.adherenciaRepository = adherenciaRepository;
        this.adherenciaQueryService = adherenciaQueryService;
    }

    /**
     * {@code POST  /adherencias} : Create a new adherencia.
     *
     * @param adherencia the adherencia to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new adherencia, or with status {@code 400 (Bad Request)} if the adherencia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/adherencias")
    public ResponseEntity<Adherencia> createAdherencia(@RequestBody Adherencia adherencia) throws URISyntaxException {
        log.debug("REST request to save Adherencia : {}", adherencia);
        if (adherencia.getId() != null) {
            throw new BadRequestAlertException("A new adherencia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Adherencia result = adherenciaService.save(adherencia);
        return ResponseEntity
            .created(new URI("/api/adherencias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /adherencias/:id} : Updates an existing adherencia.
     *
     * @param id the id of the adherencia to save.
     * @param adherencia the adherencia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adherencia,
     * or with status {@code 400 (Bad Request)} if the adherencia is not valid,
     * or with status {@code 500 (Internal Server Error)} if the adherencia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/adherencias/{id}")
    public ResponseEntity<Adherencia> updateAdherencia(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Adherencia adherencia
    ) throws URISyntaxException {
        log.debug("REST request to update Adherencia : {}, {}", id, adherencia);
        if (adherencia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adherencia.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adherenciaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Adherencia result = adherenciaService.save(adherencia);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adherencia.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /adherencias/:id} : Partial updates given fields of an existing adherencia, field will ignore if it is null
     *
     * @param id the id of the adherencia to save.
     * @param adherencia the adherencia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adherencia,
     * or with status {@code 400 (Bad Request)} if the adherencia is not valid,
     * or with status {@code 404 (Not Found)} if the adherencia is not found,
     * or with status {@code 500 (Internal Server Error)} if the adherencia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/adherencias/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Adherencia> partialUpdateAdherencia(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Adherencia adherencia
    ) throws URISyntaxException {
        log.debug("REST request to partial update Adherencia partially : {}, {}", id, adherencia);
        if (adherencia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adherencia.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adherenciaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Adherencia> result = adherenciaService.partialUpdate(adherencia);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adherencia.getId().toString())
        );
    }

    /**
     * {@code GET  /adherencias} : get all the adherencias.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of adherencias in body.
     */
    @GetMapping("/adherencias")
    public ResponseEntity<List<Adherencia>> getAllAdherencias(AdherenciaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Adherencias by criteria: {}", criteria);
        Page<Adherencia> page = adherenciaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /adherencias/count} : count all the adherencias.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/adherencias/count")
    public ResponseEntity<Long> countAdherencias(AdherenciaCriteria criteria) {
        log.debug("REST request to count Adherencias by criteria: {}", criteria);
        return ResponseEntity.ok().body(adherenciaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /adherencias/:id} : get the "id" adherencia.
     *
     * @param id the id of the adherencia to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adherencia, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/adherencias/{id}")
    public ResponseEntity<Adherencia> getAdherencia(@PathVariable Long id) {
        log.debug("REST request to get Adherencia : {}", id);
        Optional<Adherencia> adherencia = adherenciaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(adherencia);
    }

    /**
     * {@code DELETE  /adherencias/:id} : delete the "id" adherencia.
     *
     * @param id the id of the adherencia to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/adherencias/{id}")
    public ResponseEntity<Void> deleteAdherencia(@PathVariable Long id) {
        log.debug("REST request to delete Adherencia : {}", id);
        adherenciaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

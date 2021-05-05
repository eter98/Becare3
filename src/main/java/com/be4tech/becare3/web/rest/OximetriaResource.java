package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.Oximetria;
import com.be4tech.becare3.repository.OximetriaRepository;
import com.be4tech.becare3.service.OximetriaQueryService;
import com.be4tech.becare3.service.OximetriaService;
import com.be4tech.becare3.service.criteria.OximetriaCriteria;
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
 * REST controller for managing {@link com.be4tech.becare3.domain.Oximetria}.
 */
@RestController
@RequestMapping("/api")
public class OximetriaResource {

    private final Logger log = LoggerFactory.getLogger(OximetriaResource.class);

    private static final String ENTITY_NAME = "oximetria";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OximetriaService oximetriaService;

    private final OximetriaRepository oximetriaRepository;

    private final OximetriaQueryService oximetriaQueryService;

    public OximetriaResource(
        OximetriaService oximetriaService,
        OximetriaRepository oximetriaRepository,
        OximetriaQueryService oximetriaQueryService
    ) {
        this.oximetriaService = oximetriaService;
        this.oximetriaRepository = oximetriaRepository;
        this.oximetriaQueryService = oximetriaQueryService;
    }

    /**
     * {@code POST  /oximetrias} : Create a new oximetria.
     *
     * @param oximetria the oximetria to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new oximetria, or with status {@code 400 (Bad Request)} if the oximetria has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/oximetrias")
    public ResponseEntity<Oximetria> createOximetria(@RequestBody Oximetria oximetria) throws URISyntaxException {
        log.debug("REST request to save Oximetria : {}", oximetria);
        if (oximetria.getId() != null) {
            throw new BadRequestAlertException("A new oximetria cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Oximetria result = oximetriaService.save(oximetria);
        return ResponseEntity
            .created(new URI("/api/oximetrias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /oximetrias/:id} : Updates an existing oximetria.
     *
     * @param id the id of the oximetria to save.
     * @param oximetria the oximetria to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oximetria,
     * or with status {@code 400 (Bad Request)} if the oximetria is not valid,
     * or with status {@code 500 (Internal Server Error)} if the oximetria couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/oximetrias/{id}")
    public ResponseEntity<Oximetria> updateOximetria(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Oximetria oximetria
    ) throws URISyntaxException {
        log.debug("REST request to update Oximetria : {}, {}", id, oximetria);
        if (oximetria.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, oximetria.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!oximetriaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Oximetria result = oximetriaService.save(oximetria);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oximetria.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /oximetrias/:id} : Partial updates given fields of an existing oximetria, field will ignore if it is null
     *
     * @param id the id of the oximetria to save.
     * @param oximetria the oximetria to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oximetria,
     * or with status {@code 400 (Bad Request)} if the oximetria is not valid,
     * or with status {@code 404 (Not Found)} if the oximetria is not found,
     * or with status {@code 500 (Internal Server Error)} if the oximetria couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/oximetrias/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Oximetria> partialUpdateOximetria(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Oximetria oximetria
    ) throws URISyntaxException {
        log.debug("REST request to partial update Oximetria partially : {}, {}", id, oximetria);
        if (oximetria.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, oximetria.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!oximetriaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Oximetria> result = oximetriaService.partialUpdate(oximetria);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oximetria.getId().toString())
        );
    }

    /**
     * {@code GET  /oximetrias} : get all the oximetrias.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of oximetrias in body.
     */
    @GetMapping("/oximetrias")
    public ResponseEntity<List<Oximetria>> getAllOximetrias(OximetriaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Oximetrias by criteria: {}", criteria);
        Page<Oximetria> page = oximetriaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /oximetrias/count} : count all the oximetrias.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/oximetrias/count")
    public ResponseEntity<Long> countOximetrias(OximetriaCriteria criteria) {
        log.debug("REST request to count Oximetrias by criteria: {}", criteria);
        return ResponseEntity.ok().body(oximetriaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /oximetrias/:id} : get the "id" oximetria.
     *
     * @param id the id of the oximetria to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the oximetria, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/oximetrias/{id}")
    public ResponseEntity<Oximetria> getOximetria(@PathVariable Long id) {
        log.debug("REST request to get Oximetria : {}", id);
        Optional<Oximetria> oximetria = oximetriaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(oximetria);
    }

    /**
     * {@code DELETE  /oximetrias/:id} : delete the "id" oximetria.
     *
     * @param id the id of the oximetria to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/oximetrias/{id}")
    public ResponseEntity<Void> deleteOximetria(@PathVariable Long id) {
        log.debug("REST request to delete Oximetria : {}", id);
        oximetriaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

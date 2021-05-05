package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.Fisiometria1;
import com.be4tech.becare3.repository.Fisiometria1Repository;
import com.be4tech.becare3.service.Fisiometria1QueryService;
import com.be4tech.becare3.service.Fisiometria1Service;
import com.be4tech.becare3.service.criteria.Fisiometria1Criteria;
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
 * REST controller for managing {@link com.be4tech.becare3.domain.Fisiometria1}.
 */
@RestController
@RequestMapping("/api")
public class Fisiometria1Resource {

    private final Logger log = LoggerFactory.getLogger(Fisiometria1Resource.class);

    private static final String ENTITY_NAME = "fisiometria1";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Fisiometria1Service fisiometria1Service;

    private final Fisiometria1Repository fisiometria1Repository;

    private final Fisiometria1QueryService fisiometria1QueryService;

    public Fisiometria1Resource(
        Fisiometria1Service fisiometria1Service,
        Fisiometria1Repository fisiometria1Repository,
        Fisiometria1QueryService fisiometria1QueryService
    ) {
        this.fisiometria1Service = fisiometria1Service;
        this.fisiometria1Repository = fisiometria1Repository;
        this.fisiometria1QueryService = fisiometria1QueryService;
    }

    /**
     * {@code POST  /fisiometria-1-s} : Create a new fisiometria1.
     *
     * @param fisiometria1 the fisiometria1 to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fisiometria1, or with status {@code 400 (Bad Request)} if the fisiometria1 has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fisiometria-1-s")
    public ResponseEntity<Fisiometria1> createFisiometria1(@RequestBody Fisiometria1 fisiometria1) throws URISyntaxException {
        log.debug("REST request to save Fisiometria1 : {}", fisiometria1);
        if (fisiometria1.getId() != null) {
            throw new BadRequestAlertException("A new fisiometria1 cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Fisiometria1 result = fisiometria1Service.save(fisiometria1);
        return ResponseEntity
            .created(new URI("/api/fisiometria-1-s/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fisiometria-1-s/:id} : Updates an existing fisiometria1.
     *
     * @param id the id of the fisiometria1 to save.
     * @param fisiometria1 the fisiometria1 to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fisiometria1,
     * or with status {@code 400 (Bad Request)} if the fisiometria1 is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fisiometria1 couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fisiometria-1-s/{id}")
    public ResponseEntity<Fisiometria1> updateFisiometria1(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Fisiometria1 fisiometria1
    ) throws URISyntaxException {
        log.debug("REST request to update Fisiometria1 : {}, {}", id, fisiometria1);
        if (fisiometria1.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fisiometria1.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fisiometria1Repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Fisiometria1 result = fisiometria1Service.save(fisiometria1);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fisiometria1.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fisiometria-1-s/:id} : Partial updates given fields of an existing fisiometria1, field will ignore if it is null
     *
     * @param id the id of the fisiometria1 to save.
     * @param fisiometria1 the fisiometria1 to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fisiometria1,
     * or with status {@code 400 (Bad Request)} if the fisiometria1 is not valid,
     * or with status {@code 404 (Not Found)} if the fisiometria1 is not found,
     * or with status {@code 500 (Internal Server Error)} if the fisiometria1 couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fisiometria-1-s/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Fisiometria1> partialUpdateFisiometria1(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Fisiometria1 fisiometria1
    ) throws URISyntaxException {
        log.debug("REST request to partial update Fisiometria1 partially : {}, {}", id, fisiometria1);
        if (fisiometria1.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fisiometria1.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fisiometria1Repository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Fisiometria1> result = fisiometria1Service.partialUpdate(fisiometria1);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fisiometria1.getId().toString())
        );
    }

    /**
     * {@code GET  /fisiometria-1-s} : get all the fisiometria1s.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fisiometria1s in body.
     */
    @GetMapping("/fisiometria-1-s")
    public ResponseEntity<List<Fisiometria1>> getAllFisiometria1s(Fisiometria1Criteria criteria, Pageable pageable) {
        log.debug("REST request to get Fisiometria1s by criteria: {}", criteria);
        Page<Fisiometria1> page = fisiometria1QueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fisiometria-1-s/count} : count all the fisiometria1s.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fisiometria-1-s/count")
    public ResponseEntity<Long> countFisiometria1s(Fisiometria1Criteria criteria) {
        log.debug("REST request to count Fisiometria1s by criteria: {}", criteria);
        return ResponseEntity.ok().body(fisiometria1QueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fisiometria-1-s/:id} : get the "id" fisiometria1.
     *
     * @param id the id of the fisiometria1 to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fisiometria1, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fisiometria-1-s/{id}")
    public ResponseEntity<Fisiometria1> getFisiometria1(@PathVariable Long id) {
        log.debug("REST request to get Fisiometria1 : {}", id);
        Optional<Fisiometria1> fisiometria1 = fisiometria1Service.findOne(id);
        return ResponseUtil.wrapOrNotFound(fisiometria1);
    }

    /**
     * {@code DELETE  /fisiometria-1-s/:id} : delete the "id" fisiometria1.
     *
     * @param id the id of the fisiometria1 to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fisiometria-1-s/{id}")
    public ResponseEntity<Void> deleteFisiometria1(@PathVariable Long id) {
        log.debug("REST request to delete Fisiometria1 : {}", id);
        fisiometria1Service.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

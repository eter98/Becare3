package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.Condicion;
import com.be4tech.becare3.repository.CondicionRepository;
import com.be4tech.becare3.service.CondicionQueryService;
import com.be4tech.becare3.service.CondicionService;
import com.be4tech.becare3.service.criteria.CondicionCriteria;
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
 * REST controller for managing {@link com.be4tech.becare3.domain.Condicion}.
 */
@RestController
@RequestMapping("/api")
public class CondicionResource {

    private final Logger log = LoggerFactory.getLogger(CondicionResource.class);

    private static final String ENTITY_NAME = "condicion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CondicionService condicionService;

    private final CondicionRepository condicionRepository;

    private final CondicionQueryService condicionQueryService;

    public CondicionResource(
        CondicionService condicionService,
        CondicionRepository condicionRepository,
        CondicionQueryService condicionQueryService
    ) {
        this.condicionService = condicionService;
        this.condicionRepository = condicionRepository;
        this.condicionQueryService = condicionQueryService;
    }

    /**
     * {@code POST  /condicions} : Create a new condicion.
     *
     * @param condicion the condicion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new condicion, or with status {@code 400 (Bad Request)} if the condicion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/condicions")
    public ResponseEntity<Condicion> createCondicion(@RequestBody Condicion condicion) throws URISyntaxException {
        log.debug("REST request to save Condicion : {}", condicion);
        if (condicion.getId() != null) {
            throw new BadRequestAlertException("A new condicion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Condicion result = condicionService.save(condicion);
        return ResponseEntity
            .created(new URI("/api/condicions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /condicions/:id} : Updates an existing condicion.
     *
     * @param id the id of the condicion to save.
     * @param condicion the condicion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated condicion,
     * or with status {@code 400 (Bad Request)} if the condicion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the condicion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/condicions/{id}")
    public ResponseEntity<Condicion> updateCondicion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Condicion condicion
    ) throws URISyntaxException {
        log.debug("REST request to update Condicion : {}, {}", id, condicion);
        if (condicion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, condicion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!condicionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Condicion result = condicionService.save(condicion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, condicion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /condicions/:id} : Partial updates given fields of an existing condicion, field will ignore if it is null
     *
     * @param id the id of the condicion to save.
     * @param condicion the condicion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated condicion,
     * or with status {@code 400 (Bad Request)} if the condicion is not valid,
     * or with status {@code 404 (Not Found)} if the condicion is not found,
     * or with status {@code 500 (Internal Server Error)} if the condicion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/condicions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Condicion> partialUpdateCondicion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Condicion condicion
    ) throws URISyntaxException {
        log.debug("REST request to partial update Condicion partially : {}, {}", id, condicion);
        if (condicion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, condicion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!condicionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Condicion> result = condicionService.partialUpdate(condicion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, condicion.getId().toString())
        );
    }

    /**
     * {@code GET  /condicions} : get all the condicions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of condicions in body.
     */
    @GetMapping("/condicions")
    public ResponseEntity<List<Condicion>> getAllCondicions(CondicionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Condicions by criteria: {}", criteria);
        Page<Condicion> page = condicionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /condicions/count} : count all the condicions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/condicions/count")
    public ResponseEntity<Long> countCondicions(CondicionCriteria criteria) {
        log.debug("REST request to count Condicions by criteria: {}", criteria);
        return ResponseEntity.ok().body(condicionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /condicions/:id} : get the "id" condicion.
     *
     * @param id the id of the condicion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the condicion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/condicions/{id}")
    public ResponseEntity<Condicion> getCondicion(@PathVariable Long id) {
        log.debug("REST request to get Condicion : {}", id);
        Optional<Condicion> condicion = condicionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(condicion);
    }

    /**
     * {@code DELETE  /condicions/:id} : delete the "id" condicion.
     *
     * @param id the id of the condicion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/condicions/{id}")
    public ResponseEntity<Void> deleteCondicion(@PathVariable Long id) {
        log.debug("REST request to delete Condicion : {}", id);
        condicionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

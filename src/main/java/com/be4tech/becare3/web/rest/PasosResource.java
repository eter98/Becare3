package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.Pasos;
import com.be4tech.becare3.repository.PasosRepository;
import com.be4tech.becare3.service.PasosQueryService;
import com.be4tech.becare3.service.PasosService;
import com.be4tech.becare3.service.criteria.PasosCriteria;
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
 * REST controller for managing {@link com.be4tech.becare3.domain.Pasos}.
 */
@RestController
@RequestMapping("/api")
public class PasosResource {

    private final Logger log = LoggerFactory.getLogger(PasosResource.class);

    private static final String ENTITY_NAME = "pasos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PasosService pasosService;

    private final PasosRepository pasosRepository;

    private final PasosQueryService pasosQueryService;

    public PasosResource(PasosService pasosService, PasosRepository pasosRepository, PasosQueryService pasosQueryService) {
        this.pasosService = pasosService;
        this.pasosRepository = pasosRepository;
        this.pasosQueryService = pasosQueryService;
    }

    /**
     * {@code POST  /pasos} : Create a new pasos.
     *
     * @param pasos the pasos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pasos, or with status {@code 400 (Bad Request)} if the pasos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pasos")
    public ResponseEntity<Pasos> createPasos(@RequestBody Pasos pasos) throws URISyntaxException {
        log.debug("REST request to save Pasos : {}", pasos);
        if (pasos.getId() != null) {
            throw new BadRequestAlertException("A new pasos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pasos result = pasosService.save(pasos);
        return ResponseEntity
            .created(new URI("/api/pasos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pasos/:id} : Updates an existing pasos.
     *
     * @param id the id of the pasos to save.
     * @param pasos the pasos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pasos,
     * or with status {@code 400 (Bad Request)} if the pasos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pasos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pasos/{id}")
    public ResponseEntity<Pasos> updatePasos(@PathVariable(value = "id", required = false) final Long id, @RequestBody Pasos pasos)
        throws URISyntaxException {
        log.debug("REST request to update Pasos : {}, {}", id, pasos);
        if (pasos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pasos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pasosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Pasos result = pasosService.save(pasos);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pasos.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pasos/:id} : Partial updates given fields of an existing pasos, field will ignore if it is null
     *
     * @param id the id of the pasos to save.
     * @param pasos the pasos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pasos,
     * or with status {@code 400 (Bad Request)} if the pasos is not valid,
     * or with status {@code 404 (Not Found)} if the pasos is not found,
     * or with status {@code 500 (Internal Server Error)} if the pasos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pasos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Pasos> partialUpdatePasos(@PathVariable(value = "id", required = false) final Long id, @RequestBody Pasos pasos)
        throws URISyntaxException {
        log.debug("REST request to partial update Pasos partially : {}, {}", id, pasos);
        if (pasos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pasos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pasosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pasos> result = pasosService.partialUpdate(pasos);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pasos.getId().toString())
        );
    }

    /**
     * {@code GET  /pasos} : get all the pasos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pasos in body.
     */
    @GetMapping("/pasos")
    public ResponseEntity<List<Pasos>> getAllPasos(PasosCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Pasos by criteria: {}", criteria);
        Page<Pasos> page = pasosQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pasos/count} : count all the pasos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/pasos/count")
    public ResponseEntity<Long> countPasos(PasosCriteria criteria) {
        log.debug("REST request to count Pasos by criteria: {}", criteria);
        return ResponseEntity.ok().body(pasosQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pasos/:id} : get the "id" pasos.
     *
     * @param id the id of the pasos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pasos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pasos/{id}")
    public ResponseEntity<Pasos> getPasos(@PathVariable Long id) {
        log.debug("REST request to get Pasos : {}", id);
        Optional<Pasos> pasos = pasosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pasos);
    }

    /**
     * {@code DELETE  /pasos/:id} : delete the "id" pasos.
     *
     * @param id the id of the pasos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pasos/{id}")
    public ResponseEntity<Void> deletePasos(@PathVariable Long id) {
        log.debug("REST request to delete Pasos : {}", id);
        pasosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

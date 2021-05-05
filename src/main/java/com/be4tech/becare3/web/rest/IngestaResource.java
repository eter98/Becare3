package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.Ingesta;
import com.be4tech.becare3.repository.IngestaRepository;
import com.be4tech.becare3.service.IngestaQueryService;
import com.be4tech.becare3.service.IngestaService;
import com.be4tech.becare3.service.criteria.IngestaCriteria;
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
 * REST controller for managing {@link com.be4tech.becare3.domain.Ingesta}.
 */
@RestController
@RequestMapping("/api")
public class IngestaResource {

    private final Logger log = LoggerFactory.getLogger(IngestaResource.class);

    private static final String ENTITY_NAME = "ingesta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IngestaService ingestaService;

    private final IngestaRepository ingestaRepository;

    private final IngestaQueryService ingestaQueryService;

    public IngestaResource(IngestaService ingestaService, IngestaRepository ingestaRepository, IngestaQueryService ingestaQueryService) {
        this.ingestaService = ingestaService;
        this.ingestaRepository = ingestaRepository;
        this.ingestaQueryService = ingestaQueryService;
    }

    /**
     * {@code POST  /ingestas} : Create a new ingesta.
     *
     * @param ingesta the ingesta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ingesta, or with status {@code 400 (Bad Request)} if the ingesta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ingestas")
    public ResponseEntity<Ingesta> createIngesta(@RequestBody Ingesta ingesta) throws URISyntaxException {
        log.debug("REST request to save Ingesta : {}", ingesta);
        if (ingesta.getId() != null) {
            throw new BadRequestAlertException("A new ingesta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ingesta result = ingestaService.save(ingesta);
        return ResponseEntity
            .created(new URI("/api/ingestas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ingestas/:id} : Updates an existing ingesta.
     *
     * @param id the id of the ingesta to save.
     * @param ingesta the ingesta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingesta,
     * or with status {@code 400 (Bad Request)} if the ingesta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ingesta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ingestas/{id}")
    public ResponseEntity<Ingesta> updateIngesta(@PathVariable(value = "id", required = false) final Long id, @RequestBody Ingesta ingesta)
        throws URISyntaxException {
        log.debug("REST request to update Ingesta : {}, {}", id, ingesta);
        if (ingesta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingesta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingestaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ingesta result = ingestaService.save(ingesta);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ingesta.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ingestas/:id} : Partial updates given fields of an existing ingesta, field will ignore if it is null
     *
     * @param id the id of the ingesta to save.
     * @param ingesta the ingesta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingesta,
     * or with status {@code 400 (Bad Request)} if the ingesta is not valid,
     * or with status {@code 404 (Not Found)} if the ingesta is not found,
     * or with status {@code 500 (Internal Server Error)} if the ingesta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ingestas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Ingesta> partialUpdateIngesta(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Ingesta ingesta
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ingesta partially : {}, {}", id, ingesta);
        if (ingesta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingesta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingestaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ingesta> result = ingestaService.partialUpdate(ingesta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ingesta.getId().toString())
        );
    }

    /**
     * {@code GET  /ingestas} : get all the ingestas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ingestas in body.
     */
    @GetMapping("/ingestas")
    public ResponseEntity<List<Ingesta>> getAllIngestas(IngestaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Ingestas by criteria: {}", criteria);
        Page<Ingesta> page = ingestaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ingestas/count} : count all the ingestas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ingestas/count")
    public ResponseEntity<Long> countIngestas(IngestaCriteria criteria) {
        log.debug("REST request to count Ingestas by criteria: {}", criteria);
        return ResponseEntity.ok().body(ingestaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ingestas/:id} : get the "id" ingesta.
     *
     * @param id the id of the ingesta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ingesta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ingestas/{id}")
    public ResponseEntity<Ingesta> getIngesta(@PathVariable Long id) {
        log.debug("REST request to get Ingesta : {}", id);
        Optional<Ingesta> ingesta = ingestaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ingesta);
    }

    /**
     * {@code DELETE  /ingestas/:id} : delete the "id" ingesta.
     *
     * @param id the id of the ingesta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ingestas/{id}")
    public ResponseEntity<Void> deleteIngesta(@PathVariable Long id) {
        log.debug("REST request to delete Ingesta : {}", id);
        ingestaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

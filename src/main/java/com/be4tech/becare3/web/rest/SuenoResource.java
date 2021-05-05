package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.Sueno;
import com.be4tech.becare3.repository.SuenoRepository;
import com.be4tech.becare3.service.SuenoQueryService;
import com.be4tech.becare3.service.SuenoService;
import com.be4tech.becare3.service.criteria.SuenoCriteria;
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
 * REST controller for managing {@link com.be4tech.becare3.domain.Sueno}.
 */
@RestController
@RequestMapping("/api")
public class SuenoResource {

    private final Logger log = LoggerFactory.getLogger(SuenoResource.class);

    private static final String ENTITY_NAME = "sueno";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SuenoService suenoService;

    private final SuenoRepository suenoRepository;

    private final SuenoQueryService suenoQueryService;

    public SuenoResource(SuenoService suenoService, SuenoRepository suenoRepository, SuenoQueryService suenoQueryService) {
        this.suenoService = suenoService;
        this.suenoRepository = suenoRepository;
        this.suenoQueryService = suenoQueryService;
    }

    /**
     * {@code POST  /suenos} : Create a new sueno.
     *
     * @param sueno the sueno to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sueno, or with status {@code 400 (Bad Request)} if the sueno has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/suenos")
    public ResponseEntity<Sueno> createSueno(@RequestBody Sueno sueno) throws URISyntaxException {
        log.debug("REST request to save Sueno : {}", sueno);
        if (sueno.getId() != null) {
            throw new BadRequestAlertException("A new sueno cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sueno result = suenoService.save(sueno);
        return ResponseEntity
            .created(new URI("/api/suenos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /suenos/:id} : Updates an existing sueno.
     *
     * @param id the id of the sueno to save.
     * @param sueno the sueno to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sueno,
     * or with status {@code 400 (Bad Request)} if the sueno is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sueno couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/suenos/{id}")
    public ResponseEntity<Sueno> updateSueno(@PathVariable(value = "id", required = false) final Long id, @RequestBody Sueno sueno)
        throws URISyntaxException {
        log.debug("REST request to update Sueno : {}, {}", id, sueno);
        if (sueno.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sueno.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!suenoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Sueno result = suenoService.save(sueno);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sueno.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /suenos/:id} : Partial updates given fields of an existing sueno, field will ignore if it is null
     *
     * @param id the id of the sueno to save.
     * @param sueno the sueno to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sueno,
     * or with status {@code 400 (Bad Request)} if the sueno is not valid,
     * or with status {@code 404 (Not Found)} if the sueno is not found,
     * or with status {@code 500 (Internal Server Error)} if the sueno couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/suenos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Sueno> partialUpdateSueno(@PathVariable(value = "id", required = false) final Long id, @RequestBody Sueno sueno)
        throws URISyntaxException {
        log.debug("REST request to partial update Sueno partially : {}, {}", id, sueno);
        if (sueno.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sueno.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!suenoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Sueno> result = suenoService.partialUpdate(sueno);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sueno.getId().toString())
        );
    }

    /**
     * {@code GET  /suenos} : get all the suenos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of suenos in body.
     */
    @GetMapping("/suenos")
    public ResponseEntity<List<Sueno>> getAllSuenos(SuenoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Suenos by criteria: {}", criteria);
        Page<Sueno> page = suenoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /suenos/count} : count all the suenos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/suenos/count")
    public ResponseEntity<Long> countSuenos(SuenoCriteria criteria) {
        log.debug("REST request to count Suenos by criteria: {}", criteria);
        return ResponseEntity.ok().body(suenoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /suenos/:id} : get the "id" sueno.
     *
     * @param id the id of the sueno to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sueno, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/suenos/{id}")
    public ResponseEntity<Sueno> getSueno(@PathVariable Long id) {
        log.debug("REST request to get Sueno : {}", id);
        Optional<Sueno> sueno = suenoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sueno);
    }

    /**
     * {@code DELETE  /suenos/:id} : delete the "id" sueno.
     *
     * @param id the id of the sueno to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/suenos/{id}")
    public ResponseEntity<Void> deleteSueno(@PathVariable Long id) {
        log.debug("REST request to delete Sueno : {}", id);
        suenoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

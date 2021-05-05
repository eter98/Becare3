package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.Tratamieto;
import com.be4tech.becare3.repository.TratamietoRepository;
import com.be4tech.becare3.service.TratamietoQueryService;
import com.be4tech.becare3.service.TratamietoService;
import com.be4tech.becare3.service.criteria.TratamietoCriteria;
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
 * REST controller for managing {@link com.be4tech.becare3.domain.Tratamieto}.
 */
@RestController
@RequestMapping("/api")
public class TratamietoResource {

    private final Logger log = LoggerFactory.getLogger(TratamietoResource.class);

    private static final String ENTITY_NAME = "tratamieto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TratamietoService tratamietoService;

    private final TratamietoRepository tratamietoRepository;

    private final TratamietoQueryService tratamietoQueryService;

    public TratamietoResource(
        TratamietoService tratamietoService,
        TratamietoRepository tratamietoRepository,
        TratamietoQueryService tratamietoQueryService
    ) {
        this.tratamietoService = tratamietoService;
        this.tratamietoRepository = tratamietoRepository;
        this.tratamietoQueryService = tratamietoQueryService;
    }

    /**
     * {@code POST  /tratamietos} : Create a new tratamieto.
     *
     * @param tratamieto the tratamieto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tratamieto, or with status {@code 400 (Bad Request)} if the tratamieto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tratamietos")
    public ResponseEntity<Tratamieto> createTratamieto(@RequestBody Tratamieto tratamieto) throws URISyntaxException {
        log.debug("REST request to save Tratamieto : {}", tratamieto);
        if (tratamieto.getId() != null) {
            throw new BadRequestAlertException("A new tratamieto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tratamieto result = tratamietoService.save(tratamieto);
        return ResponseEntity
            .created(new URI("/api/tratamietos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tratamietos/:id} : Updates an existing tratamieto.
     *
     * @param id the id of the tratamieto to save.
     * @param tratamieto the tratamieto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tratamieto,
     * or with status {@code 400 (Bad Request)} if the tratamieto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tratamieto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tratamietos/{id}")
    public ResponseEntity<Tratamieto> updateTratamieto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Tratamieto tratamieto
    ) throws URISyntaxException {
        log.debug("REST request to update Tratamieto : {}, {}", id, tratamieto);
        if (tratamieto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tratamieto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tratamietoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Tratamieto result = tratamietoService.save(tratamieto);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tratamieto.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tratamietos/:id} : Partial updates given fields of an existing tratamieto, field will ignore if it is null
     *
     * @param id the id of the tratamieto to save.
     * @param tratamieto the tratamieto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tratamieto,
     * or with status {@code 400 (Bad Request)} if the tratamieto is not valid,
     * or with status {@code 404 (Not Found)} if the tratamieto is not found,
     * or with status {@code 500 (Internal Server Error)} if the tratamieto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tratamietos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Tratamieto> partialUpdateTratamieto(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Tratamieto tratamieto
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tratamieto partially : {}, {}", id, tratamieto);
        if (tratamieto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tratamieto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tratamietoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Tratamieto> result = tratamietoService.partialUpdate(tratamieto);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tratamieto.getId().toString())
        );
    }

    /**
     * {@code GET  /tratamietos} : get all the tratamietos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tratamietos in body.
     */
    @GetMapping("/tratamietos")
    public ResponseEntity<List<Tratamieto>> getAllTratamietos(TratamietoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Tratamietos by criteria: {}", criteria);
        Page<Tratamieto> page = tratamietoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tratamietos/count} : count all the tratamietos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tratamietos/count")
    public ResponseEntity<Long> countTratamietos(TratamietoCriteria criteria) {
        log.debug("REST request to count Tratamietos by criteria: {}", criteria);
        return ResponseEntity.ok().body(tratamietoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tratamietos/:id} : get the "id" tratamieto.
     *
     * @param id the id of the tratamieto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tratamieto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tratamietos/{id}")
    public ResponseEntity<Tratamieto> getTratamieto(@PathVariable Long id) {
        log.debug("REST request to get Tratamieto : {}", id);
        Optional<Tratamieto> tratamieto = tratamietoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tratamieto);
    }

    /**
     * {@code DELETE  /tratamietos/:id} : delete the "id" tratamieto.
     *
     * @param id the id of the tratamieto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tratamietos/{id}")
    public ResponseEntity<Void> deleteTratamieto(@PathVariable Long id) {
        log.debug("REST request to delete Tratamieto : {}", id);
        tratamietoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

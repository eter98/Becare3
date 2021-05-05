package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.Caloria;
import com.be4tech.becare3.repository.CaloriaRepository;
import com.be4tech.becare3.service.CaloriaQueryService;
import com.be4tech.becare3.service.CaloriaService;
import com.be4tech.becare3.service.criteria.CaloriaCriteria;
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
 * REST controller for managing {@link com.be4tech.becare3.domain.Caloria}.
 */
@RestController
@RequestMapping("/api")
public class CaloriaResource {

    private final Logger log = LoggerFactory.getLogger(CaloriaResource.class);

    private static final String ENTITY_NAME = "caloria";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CaloriaService caloriaService;

    private final CaloriaRepository caloriaRepository;

    private final CaloriaQueryService caloriaQueryService;

    public CaloriaResource(CaloriaService caloriaService, CaloriaRepository caloriaRepository, CaloriaQueryService caloriaQueryService) {
        this.caloriaService = caloriaService;
        this.caloriaRepository = caloriaRepository;
        this.caloriaQueryService = caloriaQueryService;
    }

    /**
     * {@code POST  /calorias} : Create a new caloria.
     *
     * @param caloria the caloria to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new caloria, or with status {@code 400 (Bad Request)} if the caloria has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/calorias")
    public ResponseEntity<Caloria> createCaloria(@RequestBody Caloria caloria) throws URISyntaxException {
        log.debug("REST request to save Caloria : {}", caloria);
        if (caloria.getId() != null) {
            throw new BadRequestAlertException("A new caloria cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Caloria result = caloriaService.save(caloria);
        return ResponseEntity
            .created(new URI("/api/calorias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /calorias/:id} : Updates an existing caloria.
     *
     * @param id the id of the caloria to save.
     * @param caloria the caloria to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated caloria,
     * or with status {@code 400 (Bad Request)} if the caloria is not valid,
     * or with status {@code 500 (Internal Server Error)} if the caloria couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/calorias/{id}")
    public ResponseEntity<Caloria> updateCaloria(@PathVariable(value = "id", required = false) final Long id, @RequestBody Caloria caloria)
        throws URISyntaxException {
        log.debug("REST request to update Caloria : {}, {}", id, caloria);
        if (caloria.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, caloria.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!caloriaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Caloria result = caloriaService.save(caloria);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, caloria.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /calorias/:id} : Partial updates given fields of an existing caloria, field will ignore if it is null
     *
     * @param id the id of the caloria to save.
     * @param caloria the caloria to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated caloria,
     * or with status {@code 400 (Bad Request)} if the caloria is not valid,
     * or with status {@code 404 (Not Found)} if the caloria is not found,
     * or with status {@code 500 (Internal Server Error)} if the caloria couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/calorias/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Caloria> partialUpdateCaloria(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Caloria caloria
    ) throws URISyntaxException {
        log.debug("REST request to partial update Caloria partially : {}, {}", id, caloria);
        if (caloria.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, caloria.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!caloriaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Caloria> result = caloriaService.partialUpdate(caloria);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, caloria.getId().toString())
        );
    }

    /**
     * {@code GET  /calorias} : get all the calorias.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of calorias in body.
     */
    @GetMapping("/calorias")
    public ResponseEntity<List<Caloria>> getAllCalorias(CaloriaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Calorias by criteria: {}", criteria);
        Page<Caloria> page = caloriaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /calorias/count} : count all the calorias.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/calorias/count")
    public ResponseEntity<Long> countCalorias(CaloriaCriteria criteria) {
        log.debug("REST request to count Calorias by criteria: {}", criteria);
        return ResponseEntity.ok().body(caloriaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /calorias/:id} : get the "id" caloria.
     *
     * @param id the id of the caloria to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the caloria, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/calorias/{id}")
    public ResponseEntity<Caloria> getCaloria(@PathVariable Long id) {
        log.debug("REST request to get Caloria : {}", id);
        Optional<Caloria> caloria = caloriaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(caloria);
    }

    /**
     * {@code DELETE  /calorias/:id} : delete the "id" caloria.
     *
     * @param id the id of the caloria to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/calorias/{id}")
    public ResponseEntity<Void> deleteCaloria(@PathVariable Long id) {
        log.debug("REST request to delete Caloria : {}", id);
        caloriaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.Programa;
import com.be4tech.becare3.repository.ProgramaRepository;
import com.be4tech.becare3.service.ProgramaQueryService;
import com.be4tech.becare3.service.ProgramaService;
import com.be4tech.becare3.service.criteria.ProgramaCriteria;
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
 * REST controller for managing {@link com.be4tech.becare3.domain.Programa}.
 */
@RestController
@RequestMapping("/api")
public class ProgramaResource {

    private final Logger log = LoggerFactory.getLogger(ProgramaResource.class);

    private static final String ENTITY_NAME = "programa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProgramaService programaService;

    private final ProgramaRepository programaRepository;

    private final ProgramaQueryService programaQueryService;

    public ProgramaResource(
        ProgramaService programaService,
        ProgramaRepository programaRepository,
        ProgramaQueryService programaQueryService
    ) {
        this.programaService = programaService;
        this.programaRepository = programaRepository;
        this.programaQueryService = programaQueryService;
    }

    /**
     * {@code POST  /programas} : Create a new programa.
     *
     * @param programa the programa to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new programa, or with status {@code 400 (Bad Request)} if the programa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/programas")
    public ResponseEntity<Programa> createPrograma(@RequestBody Programa programa) throws URISyntaxException {
        log.debug("REST request to save Programa : {}", programa);
        if (programa.getId() != null) {
            throw new BadRequestAlertException("A new programa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Programa result = programaService.save(programa);
        return ResponseEntity
            .created(new URI("/api/programas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /programas/:id} : Updates an existing programa.
     *
     * @param id the id of the programa to save.
     * @param programa the programa to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated programa,
     * or with status {@code 400 (Bad Request)} if the programa is not valid,
     * or with status {@code 500 (Internal Server Error)} if the programa couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/programas/{id}")
    public ResponseEntity<Programa> updatePrograma(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Programa programa
    ) throws URISyntaxException {
        log.debug("REST request to update Programa : {}, {}", id, programa);
        if (programa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, programa.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!programaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Programa result = programaService.save(programa);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, programa.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /programas/:id} : Partial updates given fields of an existing programa, field will ignore if it is null
     *
     * @param id the id of the programa to save.
     * @param programa the programa to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated programa,
     * or with status {@code 400 (Bad Request)} if the programa is not valid,
     * or with status {@code 404 (Not Found)} if the programa is not found,
     * or with status {@code 500 (Internal Server Error)} if the programa couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/programas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Programa> partialUpdatePrograma(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Programa programa
    ) throws URISyntaxException {
        log.debug("REST request to partial update Programa partially : {}, {}", id, programa);
        if (programa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, programa.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!programaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Programa> result = programaService.partialUpdate(programa);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, programa.getId().toString())
        );
    }

    /**
     * {@code GET  /programas} : get all the programas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of programas in body.
     */
    @GetMapping("/programas")
    public ResponseEntity<List<Programa>> getAllProgramas(ProgramaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Programas by criteria: {}", criteria);
        Page<Programa> page = programaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /programas/count} : count all the programas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/programas/count")
    public ResponseEntity<Long> countProgramas(ProgramaCriteria criteria) {
        log.debug("REST request to count Programas by criteria: {}", criteria);
        return ResponseEntity.ok().body(programaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /programas/:id} : get the "id" programa.
     *
     * @param id the id of the programa to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the programa, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/programas/{id}")
    public ResponseEntity<Programa> getPrograma(@PathVariable Long id) {
        log.debug("REST request to get Programa : {}", id);
        Optional<Programa> programa = programaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(programa);
    }

    /**
     * {@code DELETE  /programas/:id} : delete the "id" programa.
     *
     * @param id the id of the programa to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/programas/{id}")
    public ResponseEntity<Void> deletePrograma(@PathVariable Long id) {
        log.debug("REST request to delete Programa : {}", id);
        programaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.Peso;
import com.be4tech.becare3.repository.PesoRepository;
import com.be4tech.becare3.service.PesoQueryService;
import com.be4tech.becare3.service.PesoService;
import com.be4tech.becare3.service.criteria.PesoCriteria;
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
 * REST controller for managing {@link com.be4tech.becare3.domain.Peso}.
 */
@RestController
@RequestMapping("/api")
public class PesoResource {

    private final Logger log = LoggerFactory.getLogger(PesoResource.class);

    private static final String ENTITY_NAME = "peso";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PesoService pesoService;

    private final PesoRepository pesoRepository;

    private final PesoQueryService pesoQueryService;

    public PesoResource(PesoService pesoService, PesoRepository pesoRepository, PesoQueryService pesoQueryService) {
        this.pesoService = pesoService;
        this.pesoRepository = pesoRepository;
        this.pesoQueryService = pesoQueryService;
    }

    /**
     * {@code POST  /pesos} : Create a new peso.
     *
     * @param peso the peso to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new peso, or with status {@code 400 (Bad Request)} if the peso has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pesos")
    public ResponseEntity<Peso> createPeso(@RequestBody Peso peso) throws URISyntaxException {
        log.debug("REST request to save Peso : {}", peso);
        if (peso.getId() != null) {
            throw new BadRequestAlertException("A new peso cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Peso result = pesoService.save(peso);
        return ResponseEntity
            .created(new URI("/api/pesos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pesos/:id} : Updates an existing peso.
     *
     * @param id the id of the peso to save.
     * @param peso the peso to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated peso,
     * or with status {@code 400 (Bad Request)} if the peso is not valid,
     * or with status {@code 500 (Internal Server Error)} if the peso couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pesos/{id}")
    public ResponseEntity<Peso> updatePeso(@PathVariable(value = "id", required = false) final Long id, @RequestBody Peso peso)
        throws URISyntaxException {
        log.debug("REST request to update Peso : {}, {}", id, peso);
        if (peso.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, peso.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pesoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Peso result = pesoService.save(peso);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, peso.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pesos/:id} : Partial updates given fields of an existing peso, field will ignore if it is null
     *
     * @param id the id of the peso to save.
     * @param peso the peso to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated peso,
     * or with status {@code 400 (Bad Request)} if the peso is not valid,
     * or with status {@code 404 (Not Found)} if the peso is not found,
     * or with status {@code 500 (Internal Server Error)} if the peso couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pesos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Peso> partialUpdatePeso(@PathVariable(value = "id", required = false) final Long id, @RequestBody Peso peso)
        throws URISyntaxException {
        log.debug("REST request to partial update Peso partially : {}, {}", id, peso);
        if (peso.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, peso.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pesoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Peso> result = pesoService.partialUpdate(peso);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, peso.getId().toString())
        );
    }

    /**
     * {@code GET  /pesos} : get all the pesos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pesos in body.
     */
    @GetMapping("/pesos")
    public ResponseEntity<List<Peso>> getAllPesos(PesoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Pesos by criteria: {}", criteria);
        Page<Peso> page = pesoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pesos/count} : count all the pesos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/pesos/count")
    public ResponseEntity<Long> countPesos(PesoCriteria criteria) {
        log.debug("REST request to count Pesos by criteria: {}", criteria);
        return ResponseEntity.ok().body(pesoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pesos/:id} : get the "id" peso.
     *
     * @param id the id of the peso to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the peso, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pesos/{id}")
    public ResponseEntity<Peso> getPeso(@PathVariable Long id) {
        log.debug("REST request to get Peso : {}", id);
        Optional<Peso> peso = pesoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(peso);
    }

    /**
     * {@code DELETE  /pesos/:id} : delete the "id" peso.
     *
     * @param id the id of the peso to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pesos/{id}")
    public ResponseEntity<Void> deletePeso(@PathVariable Long id) {
        log.debug("REST request to delete Peso : {}", id);
        pesoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

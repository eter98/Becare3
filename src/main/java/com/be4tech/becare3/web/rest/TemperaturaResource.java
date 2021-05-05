package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.Temperatura;
import com.be4tech.becare3.repository.TemperaturaRepository;
import com.be4tech.becare3.service.TemperaturaQueryService;
import com.be4tech.becare3.service.TemperaturaService;
import com.be4tech.becare3.service.criteria.TemperaturaCriteria;
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
 * REST controller for managing {@link com.be4tech.becare3.domain.Temperatura}.
 */
@RestController
@RequestMapping("/api")
public class TemperaturaResource {

    private final Logger log = LoggerFactory.getLogger(TemperaturaResource.class);

    private static final String ENTITY_NAME = "temperatura";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TemperaturaService temperaturaService;

    private final TemperaturaRepository temperaturaRepository;

    private final TemperaturaQueryService temperaturaQueryService;

    public TemperaturaResource(
        TemperaturaService temperaturaService,
        TemperaturaRepository temperaturaRepository,
        TemperaturaQueryService temperaturaQueryService
    ) {
        this.temperaturaService = temperaturaService;
        this.temperaturaRepository = temperaturaRepository;
        this.temperaturaQueryService = temperaturaQueryService;
    }

    /**
     * {@code POST  /temperaturas} : Create a new temperatura.
     *
     * @param temperatura the temperatura to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new temperatura, or with status {@code 400 (Bad Request)} if the temperatura has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/temperaturas")
    public ResponseEntity<Temperatura> createTemperatura(@RequestBody Temperatura temperatura) throws URISyntaxException {
        log.debug("REST request to save Temperatura : {}", temperatura);
        if (temperatura.getId() != null) {
            throw new BadRequestAlertException("A new temperatura cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Temperatura result = temperaturaService.save(temperatura);
        return ResponseEntity
            .created(new URI("/api/temperaturas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /temperaturas/:id} : Updates an existing temperatura.
     *
     * @param id the id of the temperatura to save.
     * @param temperatura the temperatura to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated temperatura,
     * or with status {@code 400 (Bad Request)} if the temperatura is not valid,
     * or with status {@code 500 (Internal Server Error)} if the temperatura couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/temperaturas/{id}")
    public ResponseEntity<Temperatura> updateTemperatura(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Temperatura temperatura
    ) throws URISyntaxException {
        log.debug("REST request to update Temperatura : {}, {}", id, temperatura);
        if (temperatura.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, temperatura.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!temperaturaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Temperatura result = temperaturaService.save(temperatura);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, temperatura.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /temperaturas/:id} : Partial updates given fields of an existing temperatura, field will ignore if it is null
     *
     * @param id the id of the temperatura to save.
     * @param temperatura the temperatura to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated temperatura,
     * or with status {@code 400 (Bad Request)} if the temperatura is not valid,
     * or with status {@code 404 (Not Found)} if the temperatura is not found,
     * or with status {@code 500 (Internal Server Error)} if the temperatura couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/temperaturas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Temperatura> partialUpdateTemperatura(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Temperatura temperatura
    ) throws URISyntaxException {
        log.debug("REST request to partial update Temperatura partially : {}, {}", id, temperatura);
        if (temperatura.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, temperatura.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!temperaturaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Temperatura> result = temperaturaService.partialUpdate(temperatura);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, temperatura.getId().toString())
        );
    }

    /**
     * {@code GET  /temperaturas} : get all the temperaturas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of temperaturas in body.
     */
    @GetMapping("/temperaturas")
    public ResponseEntity<List<Temperatura>> getAllTemperaturas(TemperaturaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Temperaturas by criteria: {}", criteria);
        Page<Temperatura> page = temperaturaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /temperaturas/count} : count all the temperaturas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/temperaturas/count")
    public ResponseEntity<Long> countTemperaturas(TemperaturaCriteria criteria) {
        log.debug("REST request to count Temperaturas by criteria: {}", criteria);
        return ResponseEntity.ok().body(temperaturaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /temperaturas/:id} : get the "id" temperatura.
     *
     * @param id the id of the temperatura to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the temperatura, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/temperaturas/{id}")
    public ResponseEntity<Temperatura> getTemperatura(@PathVariable Long id) {
        log.debug("REST request to get Temperatura : {}", id);
        Optional<Temperatura> temperatura = temperaturaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(temperatura);
    }

    /**
     * {@code DELETE  /temperaturas/:id} : delete the "id" temperatura.
     *
     * @param id the id of the temperatura to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/temperaturas/{id}")
    public ResponseEntity<Void> deleteTemperatura(@PathVariable Long id) {
        log.debug("REST request to delete Temperatura : {}", id);
        temperaturaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

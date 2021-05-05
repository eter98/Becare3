package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.Alarma;
import com.be4tech.becare3.repository.AlarmaRepository;
import com.be4tech.becare3.service.AlarmaQueryService;
import com.be4tech.becare3.service.AlarmaService;
import com.be4tech.becare3.service.criteria.AlarmaCriteria;
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
 * REST controller for managing {@link com.be4tech.becare3.domain.Alarma}.
 */
@RestController
@RequestMapping("/api")
public class AlarmaResource {

    private final Logger log = LoggerFactory.getLogger(AlarmaResource.class);

    private static final String ENTITY_NAME = "alarma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlarmaService alarmaService;

    private final AlarmaRepository alarmaRepository;

    private final AlarmaQueryService alarmaQueryService;

    public AlarmaResource(AlarmaService alarmaService, AlarmaRepository alarmaRepository, AlarmaQueryService alarmaQueryService) {
        this.alarmaService = alarmaService;
        this.alarmaRepository = alarmaRepository;
        this.alarmaQueryService = alarmaQueryService;
    }

    /**
     * {@code POST  /alarmas} : Create a new alarma.
     *
     * @param alarma the alarma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alarma, or with status {@code 400 (Bad Request)} if the alarma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/alarmas")
    public ResponseEntity<Alarma> createAlarma(@RequestBody Alarma alarma) throws URISyntaxException {
        log.debug("REST request to save Alarma : {}", alarma);
        if (alarma.getId() != null) {
            throw new BadRequestAlertException("A new alarma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Alarma result = alarmaService.save(alarma);
        return ResponseEntity
            .created(new URI("/api/alarmas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /alarmas/:id} : Updates an existing alarma.
     *
     * @param id the id of the alarma to save.
     * @param alarma the alarma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alarma,
     * or with status {@code 400 (Bad Request)} if the alarma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alarma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/alarmas/{id}")
    public ResponseEntity<Alarma> updateAlarma(@PathVariable(value = "id", required = false) final Long id, @RequestBody Alarma alarma)
        throws URISyntaxException {
        log.debug("REST request to update Alarma : {}, {}", id, alarma);
        if (alarma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alarma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alarmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Alarma result = alarmaService.save(alarma);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alarma.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /alarmas/:id} : Partial updates given fields of an existing alarma, field will ignore if it is null
     *
     * @param id the id of the alarma to save.
     * @param alarma the alarma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alarma,
     * or with status {@code 400 (Bad Request)} if the alarma is not valid,
     * or with status {@code 404 (Not Found)} if the alarma is not found,
     * or with status {@code 500 (Internal Server Error)} if the alarma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/alarmas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Alarma> partialUpdateAlarma(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Alarma alarma
    ) throws URISyntaxException {
        log.debug("REST request to partial update Alarma partially : {}, {}", id, alarma);
        if (alarma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alarma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alarmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Alarma> result = alarmaService.partialUpdate(alarma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alarma.getId().toString())
        );
    }

    /**
     * {@code GET  /alarmas} : get all the alarmas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alarmas in body.
     */
    @GetMapping("/alarmas")
    public ResponseEntity<List<Alarma>> getAllAlarmas(AlarmaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Alarmas by criteria: {}", criteria);
        Page<Alarma> page = alarmaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /alarmas/count} : count all the alarmas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/alarmas/count")
    public ResponseEntity<Long> countAlarmas(AlarmaCriteria criteria) {
        log.debug("REST request to count Alarmas by criteria: {}", criteria);
        return ResponseEntity.ok().body(alarmaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /alarmas/:id} : get the "id" alarma.
     *
     * @param id the id of the alarma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alarma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/alarmas/{id}")
    public ResponseEntity<Alarma> getAlarma(@PathVariable Long id) {
        log.debug("REST request to get Alarma : {}", id);
        Optional<Alarma> alarma = alarmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alarma);
    }

    /**
     * {@code DELETE  /alarmas/:id} : delete the "id" alarma.
     *
     * @param id the id of the alarma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/alarmas/{id}")
    public ResponseEntity<Void> deleteAlarma(@PathVariable Long id) {
        log.debug("REST request to delete Alarma : {}", id);
        alarmaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.PresionSanguinea;
import com.be4tech.becare3.repository.PresionSanguineaRepository;
import com.be4tech.becare3.service.PresionSanguineaQueryService;
import com.be4tech.becare3.service.PresionSanguineaService;
import com.be4tech.becare3.service.criteria.PresionSanguineaCriteria;
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
 * REST controller for managing {@link com.be4tech.becare3.domain.PresionSanguinea}.
 */
@RestController
@RequestMapping("/api")
public class PresionSanguineaResource {

    private final Logger log = LoggerFactory.getLogger(PresionSanguineaResource.class);

    private static final String ENTITY_NAME = "presionSanguinea";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PresionSanguineaService presionSanguineaService;

    private final PresionSanguineaRepository presionSanguineaRepository;

    private final PresionSanguineaQueryService presionSanguineaQueryService;

    public PresionSanguineaResource(
        PresionSanguineaService presionSanguineaService,
        PresionSanguineaRepository presionSanguineaRepository,
        PresionSanguineaQueryService presionSanguineaQueryService
    ) {
        this.presionSanguineaService = presionSanguineaService;
        this.presionSanguineaRepository = presionSanguineaRepository;
        this.presionSanguineaQueryService = presionSanguineaQueryService;
    }

    /**
     * {@code POST  /presion-sanguineas} : Create a new presionSanguinea.
     *
     * @param presionSanguinea the presionSanguinea to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new presionSanguinea, or with status {@code 400 (Bad Request)} if the presionSanguinea has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/presion-sanguineas")
    public ResponseEntity<PresionSanguinea> createPresionSanguinea(@RequestBody PresionSanguinea presionSanguinea)
        throws URISyntaxException {
        log.debug("REST request to save PresionSanguinea : {}", presionSanguinea);
        if (presionSanguinea.getId() != null) {
            throw new BadRequestAlertException("A new presionSanguinea cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PresionSanguinea result = presionSanguineaService.save(presionSanguinea);
        return ResponseEntity
            .created(new URI("/api/presion-sanguineas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /presion-sanguineas/:id} : Updates an existing presionSanguinea.
     *
     * @param id the id of the presionSanguinea to save.
     * @param presionSanguinea the presionSanguinea to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated presionSanguinea,
     * or with status {@code 400 (Bad Request)} if the presionSanguinea is not valid,
     * or with status {@code 500 (Internal Server Error)} if the presionSanguinea couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/presion-sanguineas/{id}")
    public ResponseEntity<PresionSanguinea> updatePresionSanguinea(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PresionSanguinea presionSanguinea
    ) throws URISyntaxException {
        log.debug("REST request to update PresionSanguinea : {}, {}", id, presionSanguinea);
        if (presionSanguinea.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, presionSanguinea.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!presionSanguineaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PresionSanguinea result = presionSanguineaService.save(presionSanguinea);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, presionSanguinea.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /presion-sanguineas/:id} : Partial updates given fields of an existing presionSanguinea, field will ignore if it is null
     *
     * @param id the id of the presionSanguinea to save.
     * @param presionSanguinea the presionSanguinea to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated presionSanguinea,
     * or with status {@code 400 (Bad Request)} if the presionSanguinea is not valid,
     * or with status {@code 404 (Not Found)} if the presionSanguinea is not found,
     * or with status {@code 500 (Internal Server Error)} if the presionSanguinea couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/presion-sanguineas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PresionSanguinea> partialUpdatePresionSanguinea(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PresionSanguinea presionSanguinea
    ) throws URISyntaxException {
        log.debug("REST request to partial update PresionSanguinea partially : {}, {}", id, presionSanguinea);
        if (presionSanguinea.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, presionSanguinea.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!presionSanguineaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PresionSanguinea> result = presionSanguineaService.partialUpdate(presionSanguinea);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, presionSanguinea.getId().toString())
        );
    }

    /**
     * {@code GET  /presion-sanguineas} : get all the presionSanguineas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of presionSanguineas in body.
     */
    @GetMapping("/presion-sanguineas")
    public ResponseEntity<List<PresionSanguinea>> getAllPresionSanguineas(PresionSanguineaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PresionSanguineas by criteria: {}", criteria);
        Page<PresionSanguinea> page = presionSanguineaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /presion-sanguineas/count} : count all the presionSanguineas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/presion-sanguineas/count")
    public ResponseEntity<Long> countPresionSanguineas(PresionSanguineaCriteria criteria) {
        log.debug("REST request to count PresionSanguineas by criteria: {}", criteria);
        return ResponseEntity.ok().body(presionSanguineaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /presion-sanguineas/:id} : get the "id" presionSanguinea.
     *
     * @param id the id of the presionSanguinea to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the presionSanguinea, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/presion-sanguineas/{id}")
    public ResponseEntity<PresionSanguinea> getPresionSanguinea(@PathVariable Long id) {
        log.debug("REST request to get PresionSanguinea : {}", id);
        Optional<PresionSanguinea> presionSanguinea = presionSanguineaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(presionSanguinea);
    }

    /**
     * {@code DELETE  /presion-sanguineas/:id} : delete the "id" presionSanguinea.
     *
     * @param id the id of the presionSanguinea to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/presion-sanguineas/{id}")
    public ResponseEntity<Void> deletePresionSanguinea(@PathVariable Long id) {
        log.debug("REST request to delete PresionSanguinea : {}", id);
        presionSanguineaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

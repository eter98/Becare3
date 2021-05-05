package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.Farmaceutica;
import com.be4tech.becare3.repository.FarmaceuticaRepository;
import com.be4tech.becare3.service.FarmaceuticaQueryService;
import com.be4tech.becare3.service.FarmaceuticaService;
import com.be4tech.becare3.service.criteria.FarmaceuticaCriteria;
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
 * REST controller for managing {@link com.be4tech.becare3.domain.Farmaceutica}.
 */
@RestController
@RequestMapping("/api")
public class FarmaceuticaResource {

    private final Logger log = LoggerFactory.getLogger(FarmaceuticaResource.class);

    private static final String ENTITY_NAME = "farmaceutica";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FarmaceuticaService farmaceuticaService;

    private final FarmaceuticaRepository farmaceuticaRepository;

    private final FarmaceuticaQueryService farmaceuticaQueryService;

    public FarmaceuticaResource(
        FarmaceuticaService farmaceuticaService,
        FarmaceuticaRepository farmaceuticaRepository,
        FarmaceuticaQueryService farmaceuticaQueryService
    ) {
        this.farmaceuticaService = farmaceuticaService;
        this.farmaceuticaRepository = farmaceuticaRepository;
        this.farmaceuticaQueryService = farmaceuticaQueryService;
    }

    /**
     * {@code POST  /farmaceuticas} : Create a new farmaceutica.
     *
     * @param farmaceutica the farmaceutica to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new farmaceutica, or with status {@code 400 (Bad Request)} if the farmaceutica has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/farmaceuticas")
    public ResponseEntity<Farmaceutica> createFarmaceutica(@RequestBody Farmaceutica farmaceutica) throws URISyntaxException {
        log.debug("REST request to save Farmaceutica : {}", farmaceutica);
        if (farmaceutica.getId() != null) {
            throw new BadRequestAlertException("A new farmaceutica cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Farmaceutica result = farmaceuticaService.save(farmaceutica);
        return ResponseEntity
            .created(new URI("/api/farmaceuticas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /farmaceuticas/:id} : Updates an existing farmaceutica.
     *
     * @param id the id of the farmaceutica to save.
     * @param farmaceutica the farmaceutica to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated farmaceutica,
     * or with status {@code 400 (Bad Request)} if the farmaceutica is not valid,
     * or with status {@code 500 (Internal Server Error)} if the farmaceutica couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/farmaceuticas/{id}")
    public ResponseEntity<Farmaceutica> updateFarmaceutica(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Farmaceutica farmaceutica
    ) throws URISyntaxException {
        log.debug("REST request to update Farmaceutica : {}, {}", id, farmaceutica);
        if (farmaceutica.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, farmaceutica.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!farmaceuticaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Farmaceutica result = farmaceuticaService.save(farmaceutica);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, farmaceutica.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /farmaceuticas/:id} : Partial updates given fields of an existing farmaceutica, field will ignore if it is null
     *
     * @param id the id of the farmaceutica to save.
     * @param farmaceutica the farmaceutica to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated farmaceutica,
     * or with status {@code 400 (Bad Request)} if the farmaceutica is not valid,
     * or with status {@code 404 (Not Found)} if the farmaceutica is not found,
     * or with status {@code 500 (Internal Server Error)} if the farmaceutica couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/farmaceuticas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Farmaceutica> partialUpdateFarmaceutica(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Farmaceutica farmaceutica
    ) throws URISyntaxException {
        log.debug("REST request to partial update Farmaceutica partially : {}, {}", id, farmaceutica);
        if (farmaceutica.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, farmaceutica.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!farmaceuticaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Farmaceutica> result = farmaceuticaService.partialUpdate(farmaceutica);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, farmaceutica.getId().toString())
        );
    }

    /**
     * {@code GET  /farmaceuticas} : get all the farmaceuticas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of farmaceuticas in body.
     */
    @GetMapping("/farmaceuticas")
    public ResponseEntity<List<Farmaceutica>> getAllFarmaceuticas(FarmaceuticaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Farmaceuticas by criteria: {}", criteria);
        Page<Farmaceutica> page = farmaceuticaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /farmaceuticas/count} : count all the farmaceuticas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/farmaceuticas/count")
    public ResponseEntity<Long> countFarmaceuticas(FarmaceuticaCriteria criteria) {
        log.debug("REST request to count Farmaceuticas by criteria: {}", criteria);
        return ResponseEntity.ok().body(farmaceuticaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /farmaceuticas/:id} : get the "id" farmaceutica.
     *
     * @param id the id of the farmaceutica to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the farmaceutica, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/farmaceuticas/{id}")
    public ResponseEntity<Farmaceutica> getFarmaceutica(@PathVariable Long id) {
        log.debug("REST request to get Farmaceutica : {}", id);
        Optional<Farmaceutica> farmaceutica = farmaceuticaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(farmaceutica);
    }

    /**
     * {@code DELETE  /farmaceuticas/:id} : delete the "id" farmaceutica.
     *
     * @param id the id of the farmaceutica to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/farmaceuticas/{id}")
    public ResponseEntity<Void> deleteFarmaceutica(@PathVariable Long id) {
        log.debug("REST request to delete Farmaceutica : {}", id);
        farmaceuticaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

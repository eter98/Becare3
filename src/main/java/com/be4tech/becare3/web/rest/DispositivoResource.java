package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.Dispositivo;
import com.be4tech.becare3.repository.DispositivoRepository;
import com.be4tech.becare3.service.DispositivoQueryService;
import com.be4tech.becare3.service.DispositivoService;
import com.be4tech.becare3.service.criteria.DispositivoCriteria;
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
 * REST controller for managing {@link com.be4tech.becare3.domain.Dispositivo}.
 */
@RestController
@RequestMapping("/api")
public class DispositivoResource {

    private final Logger log = LoggerFactory.getLogger(DispositivoResource.class);

    private static final String ENTITY_NAME = "dispositivo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DispositivoService dispositivoService;

    private final DispositivoRepository dispositivoRepository;

    private final DispositivoQueryService dispositivoQueryService;

    public DispositivoResource(
        DispositivoService dispositivoService,
        DispositivoRepository dispositivoRepository,
        DispositivoQueryService dispositivoQueryService
    ) {
        this.dispositivoService = dispositivoService;
        this.dispositivoRepository = dispositivoRepository;
        this.dispositivoQueryService = dispositivoQueryService;
    }

    /**
     * {@code POST  /dispositivos} : Create a new dispositivo.
     *
     * @param dispositivo the dispositivo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dispositivo, or with status {@code 400 (Bad Request)} if the dispositivo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dispositivos")
    public ResponseEntity<Dispositivo> createDispositivo(@RequestBody Dispositivo dispositivo) throws URISyntaxException {
        log.debug("REST request to save Dispositivo : {}", dispositivo);
        if (dispositivo.getId() != null) {
            throw new BadRequestAlertException("A new dispositivo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dispositivo result = dispositivoService.save(dispositivo);
        return ResponseEntity
            .created(new URI("/api/dispositivos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dispositivos/:id} : Updates an existing dispositivo.
     *
     * @param id the id of the dispositivo to save.
     * @param dispositivo the dispositivo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dispositivo,
     * or with status {@code 400 (Bad Request)} if the dispositivo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dispositivo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dispositivos/{id}")
    public ResponseEntity<Dispositivo> updateDispositivo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Dispositivo dispositivo
    ) throws URISyntaxException {
        log.debug("REST request to update Dispositivo : {}, {}", id, dispositivo);
        if (dispositivo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dispositivo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dispositivoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Dispositivo result = dispositivoService.save(dispositivo);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dispositivo.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dispositivos/:id} : Partial updates given fields of an existing dispositivo, field will ignore if it is null
     *
     * @param id the id of the dispositivo to save.
     * @param dispositivo the dispositivo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dispositivo,
     * or with status {@code 400 (Bad Request)} if the dispositivo is not valid,
     * or with status {@code 404 (Not Found)} if the dispositivo is not found,
     * or with status {@code 500 (Internal Server Error)} if the dispositivo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dispositivos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Dispositivo> partialUpdateDispositivo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Dispositivo dispositivo
    ) throws URISyntaxException {
        log.debug("REST request to partial update Dispositivo partially : {}, {}", id, dispositivo);
        if (dispositivo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dispositivo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dispositivoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Dispositivo> result = dispositivoService.partialUpdate(dispositivo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dispositivo.getId().toString())
        );
    }

    /**
     * {@code GET  /dispositivos} : get all the dispositivos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dispositivos in body.
     */
    @GetMapping("/dispositivos")
    public ResponseEntity<List<Dispositivo>> getAllDispositivos(DispositivoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Dispositivos by criteria: {}", criteria);
        Page<Dispositivo> page = dispositivoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dispositivos/count} : count all the dispositivos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/dispositivos/count")
    public ResponseEntity<Long> countDispositivos(DispositivoCriteria criteria) {
        log.debug("REST request to count Dispositivos by criteria: {}", criteria);
        return ResponseEntity.ok().body(dispositivoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /dispositivos/:id} : get the "id" dispositivo.
     *
     * @param id the id of the dispositivo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dispositivo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dispositivos/{id}")
    public ResponseEntity<Dispositivo> getDispositivo(@PathVariable Long id) {
        log.debug("REST request to get Dispositivo : {}", id);
        Optional<Dispositivo> dispositivo = dispositivoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dispositivo);
    }

    /**
     * {@code DELETE  /dispositivos/:id} : delete the "id" dispositivo.
     *
     * @param id the id of the dispositivo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dispositivos/{id}")
    public ResponseEntity<Void> deleteDispositivo(@PathVariable Long id) {
        log.debug("REST request to delete Dispositivo : {}", id);
        dispositivoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

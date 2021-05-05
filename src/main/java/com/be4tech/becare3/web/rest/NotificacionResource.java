package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.Notificacion;
import com.be4tech.becare3.repository.NotificacionRepository;
import com.be4tech.becare3.service.NotificacionQueryService;
import com.be4tech.becare3.service.NotificacionService;
import com.be4tech.becare3.service.criteria.NotificacionCriteria;
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
 * REST controller for managing {@link com.be4tech.becare3.domain.Notificacion}.
 */
@RestController
@RequestMapping("/api")
public class NotificacionResource {

    private final Logger log = LoggerFactory.getLogger(NotificacionResource.class);

    private static final String ENTITY_NAME = "notificacion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificacionService notificacionService;

    private final NotificacionRepository notificacionRepository;

    private final NotificacionQueryService notificacionQueryService;

    public NotificacionResource(
        NotificacionService notificacionService,
        NotificacionRepository notificacionRepository,
        NotificacionQueryService notificacionQueryService
    ) {
        this.notificacionService = notificacionService;
        this.notificacionRepository = notificacionRepository;
        this.notificacionQueryService = notificacionQueryService;
    }

    /**
     * {@code POST  /notificacions} : Create a new notificacion.
     *
     * @param notificacion the notificacion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificacion, or with status {@code 400 (Bad Request)} if the notificacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/notificacions")
    public ResponseEntity<Notificacion> createNotificacion(@RequestBody Notificacion notificacion) throws URISyntaxException {
        log.debug("REST request to save Notificacion : {}", notificacion);
        if (notificacion.getId() != null) {
            throw new BadRequestAlertException("A new notificacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Notificacion result = notificacionService.save(notificacion);
        return ResponseEntity
            .created(new URI("/api/notificacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /notificacions/:id} : Updates an existing notificacion.
     *
     * @param id the id of the notificacion to save.
     * @param notificacion the notificacion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificacion,
     * or with status {@code 400 (Bad Request)} if the notificacion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificacion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/notificacions/{id}")
    public ResponseEntity<Notificacion> updateNotificacion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Notificacion notificacion
    ) throws URISyntaxException {
        log.debug("REST request to update Notificacion : {}, {}", id, notificacion);
        if (notificacion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificacion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Notificacion result = notificacionService.save(notificacion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificacion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /notificacions/:id} : Partial updates given fields of an existing notificacion, field will ignore if it is null
     *
     * @param id the id of the notificacion to save.
     * @param notificacion the notificacion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificacion,
     * or with status {@code 400 (Bad Request)} if the notificacion is not valid,
     * or with status {@code 404 (Not Found)} if the notificacion is not found,
     * or with status {@code 500 (Internal Server Error)} if the notificacion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/notificacions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Notificacion> partialUpdateNotificacion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Notificacion notificacion
    ) throws URISyntaxException {
        log.debug("REST request to partial update Notificacion partially : {}, {}", id, notificacion);
        if (notificacion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificacion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Notificacion> result = notificacionService.partialUpdate(notificacion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificacion.getId().toString())
        );
    }

    /**
     * {@code GET  /notificacions} : get all the notificacions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notificacions in body.
     */
    @GetMapping("/notificacions")
    public ResponseEntity<List<Notificacion>> getAllNotificacions(NotificacionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Notificacions by criteria: {}", criteria);
        Page<Notificacion> page = notificacionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /notificacions/count} : count all the notificacions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/notificacions/count")
    public ResponseEntity<Long> countNotificacions(NotificacionCriteria criteria) {
        log.debug("REST request to count Notificacions by criteria: {}", criteria);
        return ResponseEntity.ok().body(notificacionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /notificacions/:id} : get the "id" notificacion.
     *
     * @param id the id of the notificacion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificacion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/notificacions/{id}")
    public ResponseEntity<Notificacion> getNotificacion(@PathVariable Long id) {
        log.debug("REST request to get Notificacion : {}", id);
        Optional<Notificacion> notificacion = notificacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notificacion);
    }

    /**
     * {@code DELETE  /notificacions/:id} : delete the "id" notificacion.
     *
     * @param id the id of the notificacion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/notificacions/{id}")
    public ResponseEntity<Void> deleteNotificacion(@PathVariable Long id) {
        log.debug("REST request to delete Notificacion : {}", id);
        notificacionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

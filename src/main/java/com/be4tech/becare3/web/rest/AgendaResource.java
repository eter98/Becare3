package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.Agenda;
import com.be4tech.becare3.repository.AgendaRepository;
import com.be4tech.becare3.service.AgendaQueryService;
import com.be4tech.becare3.service.AgendaService;
import com.be4tech.becare3.service.criteria.AgendaCriteria;
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
 * REST controller for managing {@link com.be4tech.becare3.domain.Agenda}.
 */
@RestController
@RequestMapping("/api")
public class AgendaResource {

    private final Logger log = LoggerFactory.getLogger(AgendaResource.class);

    private static final String ENTITY_NAME = "agenda";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgendaService agendaService;

    private final AgendaRepository agendaRepository;

    private final AgendaQueryService agendaQueryService;

    public AgendaResource(AgendaService agendaService, AgendaRepository agendaRepository, AgendaQueryService agendaQueryService) {
        this.agendaService = agendaService;
        this.agendaRepository = agendaRepository;
        this.agendaQueryService = agendaQueryService;
    }

    /**
     * {@code POST  /agenda} : Create a new agenda.
     *
     * @param agenda the agenda to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agenda, or with status {@code 400 (Bad Request)} if the agenda has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/agenda")
    public ResponseEntity<Agenda> createAgenda(@RequestBody Agenda agenda) throws URISyntaxException {
        log.debug("REST request to save Agenda : {}", agenda);
        if (agenda.getId() != null) {
            throw new BadRequestAlertException("A new agenda cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Agenda result = agendaService.save(agenda);
        return ResponseEntity
            .created(new URI("/api/agenda/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /agenda/:id} : Updates an existing agenda.
     *
     * @param id the id of the agenda to save.
     * @param agenda the agenda to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agenda,
     * or with status {@code 400 (Bad Request)} if the agenda is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agenda couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/agenda/{id}")
    public ResponseEntity<Agenda> updateAgenda(@PathVariable(value = "id", required = false) final Long id, @RequestBody Agenda agenda)
        throws URISyntaxException {
        log.debug("REST request to update Agenda : {}, {}", id, agenda);
        if (agenda.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agenda.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agendaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Agenda result = agendaService.save(agenda);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agenda.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /agenda/:id} : Partial updates given fields of an existing agenda, field will ignore if it is null
     *
     * @param id the id of the agenda to save.
     * @param agenda the agenda to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agenda,
     * or with status {@code 400 (Bad Request)} if the agenda is not valid,
     * or with status {@code 404 (Not Found)} if the agenda is not found,
     * or with status {@code 500 (Internal Server Error)} if the agenda couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/agenda/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Agenda> partialUpdateAgenda(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Agenda agenda
    ) throws URISyntaxException {
        log.debug("REST request to partial update Agenda partially : {}, {}", id, agenda);
        if (agenda.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agenda.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agendaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Agenda> result = agendaService.partialUpdate(agenda);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agenda.getId().toString())
        );
    }

    /**
     * {@code GET  /agenda} : get all the agenda.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agenda in body.
     */
    @GetMapping("/agenda")
    public ResponseEntity<List<Agenda>> getAllAgenda(AgendaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Agenda by criteria: {}", criteria);
        Page<Agenda> page = agendaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /agenda/count} : count all the agenda.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/agenda/count")
    public ResponseEntity<Long> countAgenda(AgendaCriteria criteria) {
        log.debug("REST request to count Agenda by criteria: {}", criteria);
        return ResponseEntity.ok().body(agendaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /agenda/:id} : get the "id" agenda.
     *
     * @param id the id of the agenda to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agenda, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/agenda/{id}")
    public ResponseEntity<Agenda> getAgenda(@PathVariable Long id) {
        log.debug("REST request to get Agenda : {}", id);
        Optional<Agenda> agenda = agendaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agenda);
    }

    /**
     * {@code DELETE  /agenda/:id} : delete the "id" agenda.
     *
     * @param id the id of the agenda to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/agenda/{id}")
    public ResponseEntity<Void> deleteAgenda(@PathVariable Long id) {
        log.debug("REST request to delete Agenda : {}", id);
        agendaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

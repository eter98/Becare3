package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.Pregunta;
import com.be4tech.becare3.repository.PreguntaRepository;
import com.be4tech.becare3.service.PreguntaQueryService;
import com.be4tech.becare3.service.PreguntaService;
import com.be4tech.becare3.service.criteria.PreguntaCriteria;
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
 * REST controller for managing {@link com.be4tech.becare3.domain.Pregunta}.
 */
@RestController
@RequestMapping("/api")
public class PreguntaResource {

    private final Logger log = LoggerFactory.getLogger(PreguntaResource.class);

    private static final String ENTITY_NAME = "pregunta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PreguntaService preguntaService;

    private final PreguntaRepository preguntaRepository;

    private final PreguntaQueryService preguntaQueryService;

    public PreguntaResource(
        PreguntaService preguntaService,
        PreguntaRepository preguntaRepository,
        PreguntaQueryService preguntaQueryService
    ) {
        this.preguntaService = preguntaService;
        this.preguntaRepository = preguntaRepository;
        this.preguntaQueryService = preguntaQueryService;
    }

    /**
     * {@code POST  /preguntas} : Create a new pregunta.
     *
     * @param pregunta the pregunta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pregunta, or with status {@code 400 (Bad Request)} if the pregunta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/preguntas")
    public ResponseEntity<Pregunta> createPregunta(@RequestBody Pregunta pregunta) throws URISyntaxException {
        log.debug("REST request to save Pregunta : {}", pregunta);
        if (pregunta.getId() != null) {
            throw new BadRequestAlertException("A new pregunta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pregunta result = preguntaService.save(pregunta);
        return ResponseEntity
            .created(new URI("/api/preguntas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /preguntas/:id} : Updates an existing pregunta.
     *
     * @param id the id of the pregunta to save.
     * @param pregunta the pregunta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pregunta,
     * or with status {@code 400 (Bad Request)} if the pregunta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pregunta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/preguntas/{id}")
    public ResponseEntity<Pregunta> updatePregunta(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Pregunta pregunta
    ) throws URISyntaxException {
        log.debug("REST request to update Pregunta : {}, {}", id, pregunta);
        if (pregunta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pregunta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!preguntaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Pregunta result = preguntaService.save(pregunta);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pregunta.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /preguntas/:id} : Partial updates given fields of an existing pregunta, field will ignore if it is null
     *
     * @param id the id of the pregunta to save.
     * @param pregunta the pregunta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pregunta,
     * or with status {@code 400 (Bad Request)} if the pregunta is not valid,
     * or with status {@code 404 (Not Found)} if the pregunta is not found,
     * or with status {@code 500 (Internal Server Error)} if the pregunta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/preguntas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Pregunta> partialUpdatePregunta(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Pregunta pregunta
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pregunta partially : {}, {}", id, pregunta);
        if (pregunta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pregunta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!preguntaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pregunta> result = preguntaService.partialUpdate(pregunta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pregunta.getId().toString())
        );
    }

    /**
     * {@code GET  /preguntas} : get all the preguntas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of preguntas in body.
     */
    @GetMapping("/preguntas")
    public ResponseEntity<List<Pregunta>> getAllPreguntas(PreguntaCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Preguntas by criteria: {}", criteria);
        Page<Pregunta> page = preguntaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /preguntas/count} : count all the preguntas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/preguntas/count")
    public ResponseEntity<Long> countPreguntas(PreguntaCriteria criteria) {
        log.debug("REST request to count Preguntas by criteria: {}", criteria);
        return ResponseEntity.ok().body(preguntaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /preguntas/:id} : get the "id" pregunta.
     *
     * @param id the id of the pregunta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pregunta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/preguntas/{id}")
    public ResponseEntity<Pregunta> getPregunta(@PathVariable Long id) {
        log.debug("REST request to get Pregunta : {}", id);
        Optional<Pregunta> pregunta = preguntaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pregunta);
    }

    /**
     * {@code DELETE  /preguntas/:id} : delete the "id" pregunta.
     *
     * @param id the id of the pregunta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/preguntas/{id}")
    public ResponseEntity<Void> deletePregunta(@PathVariable Long id) {
        log.debug("REST request to delete Pregunta : {}", id);
        preguntaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

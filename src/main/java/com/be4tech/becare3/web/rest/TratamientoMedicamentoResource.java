package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.TratamientoMedicamento;
import com.be4tech.becare3.repository.TratamientoMedicamentoRepository;
import com.be4tech.becare3.service.TratamientoMedicamentoQueryService;
import com.be4tech.becare3.service.TratamientoMedicamentoService;
import com.be4tech.becare3.service.criteria.TratamientoMedicamentoCriteria;
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
 * REST controller for managing {@link com.be4tech.becare3.domain.TratamientoMedicamento}.
 */
@RestController
@RequestMapping("/api")
public class TratamientoMedicamentoResource {

    private final Logger log = LoggerFactory.getLogger(TratamientoMedicamentoResource.class);

    private static final String ENTITY_NAME = "tratamientoMedicamento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TratamientoMedicamentoService tratamientoMedicamentoService;

    private final TratamientoMedicamentoRepository tratamientoMedicamentoRepository;

    private final TratamientoMedicamentoQueryService tratamientoMedicamentoQueryService;

    public TratamientoMedicamentoResource(
        TratamientoMedicamentoService tratamientoMedicamentoService,
        TratamientoMedicamentoRepository tratamientoMedicamentoRepository,
        TratamientoMedicamentoQueryService tratamientoMedicamentoQueryService
    ) {
        this.tratamientoMedicamentoService = tratamientoMedicamentoService;
        this.tratamientoMedicamentoRepository = tratamientoMedicamentoRepository;
        this.tratamientoMedicamentoQueryService = tratamientoMedicamentoQueryService;
    }

    /**
     * {@code POST  /tratamiento-medicamentos} : Create a new tratamientoMedicamento.
     *
     * @param tratamientoMedicamento the tratamientoMedicamento to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tratamientoMedicamento, or with status {@code 400 (Bad Request)} if the tratamientoMedicamento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tratamiento-medicamentos")
    public ResponseEntity<TratamientoMedicamento> createTratamientoMedicamento(@RequestBody TratamientoMedicamento tratamientoMedicamento)
        throws URISyntaxException {
        log.debug("REST request to save TratamientoMedicamento : {}", tratamientoMedicamento);
        if (tratamientoMedicamento.getId() != null) {
            throw new BadRequestAlertException("A new tratamientoMedicamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TratamientoMedicamento result = tratamientoMedicamentoService.save(tratamientoMedicamento);
        return ResponseEntity
            .created(new URI("/api/tratamiento-medicamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tratamiento-medicamentos/:id} : Updates an existing tratamientoMedicamento.
     *
     * @param id the id of the tratamientoMedicamento to save.
     * @param tratamientoMedicamento the tratamientoMedicamento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tratamientoMedicamento,
     * or with status {@code 400 (Bad Request)} if the tratamientoMedicamento is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tratamientoMedicamento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tratamiento-medicamentos/{id}")
    public ResponseEntity<TratamientoMedicamento> updateTratamientoMedicamento(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TratamientoMedicamento tratamientoMedicamento
    ) throws URISyntaxException {
        log.debug("REST request to update TratamientoMedicamento : {}, {}", id, tratamientoMedicamento);
        if (tratamientoMedicamento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tratamientoMedicamento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tratamientoMedicamentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TratamientoMedicamento result = tratamientoMedicamentoService.save(tratamientoMedicamento);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tratamientoMedicamento.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tratamiento-medicamentos/:id} : Partial updates given fields of an existing tratamientoMedicamento, field will ignore if it is null
     *
     * @param id the id of the tratamientoMedicamento to save.
     * @param tratamientoMedicamento the tratamientoMedicamento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tratamientoMedicamento,
     * or with status {@code 400 (Bad Request)} if the tratamientoMedicamento is not valid,
     * or with status {@code 404 (Not Found)} if the tratamientoMedicamento is not found,
     * or with status {@code 500 (Internal Server Error)} if the tratamientoMedicamento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tratamiento-medicamentos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TratamientoMedicamento> partialUpdateTratamientoMedicamento(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TratamientoMedicamento tratamientoMedicamento
    ) throws URISyntaxException {
        log.debug("REST request to partial update TratamientoMedicamento partially : {}, {}", id, tratamientoMedicamento);
        if (tratamientoMedicamento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tratamientoMedicamento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tratamientoMedicamentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TratamientoMedicamento> result = tratamientoMedicamentoService.partialUpdate(tratamientoMedicamento);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tratamientoMedicamento.getId().toString())
        );
    }

    /**
     * {@code GET  /tratamiento-medicamentos} : get all the tratamientoMedicamentos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tratamientoMedicamentos in body.
     */
    @GetMapping("/tratamiento-medicamentos")
    public ResponseEntity<List<TratamientoMedicamento>> getAllTratamientoMedicamentos(
        TratamientoMedicamentoCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get TratamientoMedicamentos by criteria: {}", criteria);
        Page<TratamientoMedicamento> page = tratamientoMedicamentoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tratamiento-medicamentos/count} : count all the tratamientoMedicamentos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tratamiento-medicamentos/count")
    public ResponseEntity<Long> countTratamientoMedicamentos(TratamientoMedicamentoCriteria criteria) {
        log.debug("REST request to count TratamientoMedicamentos by criteria: {}", criteria);
        return ResponseEntity.ok().body(tratamientoMedicamentoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tratamiento-medicamentos/:id} : get the "id" tratamientoMedicamento.
     *
     * @param id the id of the tratamientoMedicamento to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tratamientoMedicamento, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tratamiento-medicamentos/{id}")
    public ResponseEntity<TratamientoMedicamento> getTratamientoMedicamento(@PathVariable Long id) {
        log.debug("REST request to get TratamientoMedicamento : {}", id);
        Optional<TratamientoMedicamento> tratamientoMedicamento = tratamientoMedicamentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tratamientoMedicamento);
    }

    /**
     * {@code DELETE  /tratamiento-medicamentos/:id} : delete the "id" tratamientoMedicamento.
     *
     * @param id the id of the tratamientoMedicamento to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tratamiento-medicamentos/{id}")
    public ResponseEntity<Void> deleteTratamientoMedicamento(@PathVariable Long id) {
        log.debug("REST request to delete TratamientoMedicamento : {}", id);
        tratamientoMedicamentoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

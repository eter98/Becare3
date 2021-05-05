package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.Medicamento;
import com.be4tech.becare3.repository.MedicamentoRepository;
import com.be4tech.becare3.service.MedicamentoQueryService;
import com.be4tech.becare3.service.MedicamentoService;
import com.be4tech.becare3.service.criteria.MedicamentoCriteria;
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
 * REST controller for managing {@link com.be4tech.becare3.domain.Medicamento}.
 */
@RestController
@RequestMapping("/api")
public class MedicamentoResource {

    private final Logger log = LoggerFactory.getLogger(MedicamentoResource.class);

    private static final String ENTITY_NAME = "medicamento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicamentoService medicamentoService;

    private final MedicamentoRepository medicamentoRepository;

    private final MedicamentoQueryService medicamentoQueryService;

    public MedicamentoResource(
        MedicamentoService medicamentoService,
        MedicamentoRepository medicamentoRepository,
        MedicamentoQueryService medicamentoQueryService
    ) {
        this.medicamentoService = medicamentoService;
        this.medicamentoRepository = medicamentoRepository;
        this.medicamentoQueryService = medicamentoQueryService;
    }

    /**
     * {@code POST  /medicamentos} : Create a new medicamento.
     *
     * @param medicamento the medicamento to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicamento, or with status {@code 400 (Bad Request)} if the medicamento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/medicamentos")
    public ResponseEntity<Medicamento> createMedicamento(@RequestBody Medicamento medicamento) throws URISyntaxException {
        log.debug("REST request to save Medicamento : {}", medicamento);
        if (medicamento.getId() != null) {
            throw new BadRequestAlertException("A new medicamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Medicamento result = medicamentoService.save(medicamento);
        return ResponseEntity
            .created(new URI("/api/medicamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /medicamentos/:id} : Updates an existing medicamento.
     *
     * @param id the id of the medicamento to save.
     * @param medicamento the medicamento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicamento,
     * or with status {@code 400 (Bad Request)} if the medicamento is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicamento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/medicamentos/{id}")
    public ResponseEntity<Medicamento> updateMedicamento(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Medicamento medicamento
    ) throws URISyntaxException {
        log.debug("REST request to update Medicamento : {}, {}", id, medicamento);
        if (medicamento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicamento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicamentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Medicamento result = medicamentoService.save(medicamento);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicamento.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /medicamentos/:id} : Partial updates given fields of an existing medicamento, field will ignore if it is null
     *
     * @param id the id of the medicamento to save.
     * @param medicamento the medicamento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicamento,
     * or with status {@code 400 (Bad Request)} if the medicamento is not valid,
     * or with status {@code 404 (Not Found)} if the medicamento is not found,
     * or with status {@code 500 (Internal Server Error)} if the medicamento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/medicamentos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Medicamento> partialUpdateMedicamento(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Medicamento medicamento
    ) throws URISyntaxException {
        log.debug("REST request to partial update Medicamento partially : {}, {}", id, medicamento);
        if (medicamento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicamento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicamentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Medicamento> result = medicamentoService.partialUpdate(medicamento);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicamento.getId().toString())
        );
    }

    /**
     * {@code GET  /medicamentos} : get all the medicamentos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medicamentos in body.
     */
    @GetMapping("/medicamentos")
    public ResponseEntity<List<Medicamento>> getAllMedicamentos(MedicamentoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Medicamentos by criteria: {}", criteria);
        Page<Medicamento> page = medicamentoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /medicamentos/count} : count all the medicamentos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/medicamentos/count")
    public ResponseEntity<Long> countMedicamentos(MedicamentoCriteria criteria) {
        log.debug("REST request to count Medicamentos by criteria: {}", criteria);
        return ResponseEntity.ok().body(medicamentoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /medicamentos/:id} : get the "id" medicamento.
     *
     * @param id the id of the medicamento to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicamento, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/medicamentos/{id}")
    public ResponseEntity<Medicamento> getMedicamento(@PathVariable Long id) {
        log.debug("REST request to get Medicamento : {}", id);
        Optional<Medicamento> medicamento = medicamentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medicamento);
    }

    /**
     * {@code DELETE  /medicamentos/:id} : delete the "id" medicamento.
     *
     * @param id the id of the medicamento to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/medicamentos/{id}")
    public ResponseEntity<Void> deleteMedicamento(@PathVariable Long id) {
        log.debug("REST request to delete Medicamento : {}", id);
        medicamentoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

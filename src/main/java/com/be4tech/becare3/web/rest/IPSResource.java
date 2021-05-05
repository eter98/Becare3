package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.IPS;
import com.be4tech.becare3.repository.IPSRepository;
import com.be4tech.becare3.service.IPSQueryService;
import com.be4tech.becare3.service.IPSService;
import com.be4tech.becare3.service.criteria.IPSCriteria;
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
 * REST controller for managing {@link com.be4tech.becare3.domain.IPS}.
 */
@RestController
@RequestMapping("/api")
public class IPSResource {

    private final Logger log = LoggerFactory.getLogger(IPSResource.class);

    private static final String ENTITY_NAME = "iPS";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IPSService iPSService;

    private final IPSRepository iPSRepository;

    private final IPSQueryService iPSQueryService;

    public IPSResource(IPSService iPSService, IPSRepository iPSRepository, IPSQueryService iPSQueryService) {
        this.iPSService = iPSService;
        this.iPSRepository = iPSRepository;
        this.iPSQueryService = iPSQueryService;
    }

    /**
     * {@code POST  /ips} : Create a new iPS.
     *
     * @param iPS the iPS to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new iPS, or with status {@code 400 (Bad Request)} if the iPS has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ips")
    public ResponseEntity<IPS> createIPS(@RequestBody IPS iPS) throws URISyntaxException {
        log.debug("REST request to save IPS : {}", iPS);
        if (iPS.getId() != null) {
            throw new BadRequestAlertException("A new iPS cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IPS result = iPSService.save(iPS);
        return ResponseEntity
            .created(new URI("/api/ips/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ips/:id} : Updates an existing iPS.
     *
     * @param id the id of the iPS to save.
     * @param iPS the iPS to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iPS,
     * or with status {@code 400 (Bad Request)} if the iPS is not valid,
     * or with status {@code 500 (Internal Server Error)} if the iPS couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ips/{id}")
    public ResponseEntity<IPS> updateIPS(@PathVariable(value = "id", required = false) final Long id, @RequestBody IPS iPS)
        throws URISyntaxException {
        log.debug("REST request to update IPS : {}, {}", id, iPS);
        if (iPS.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iPS.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!iPSRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IPS result = iPSService.save(iPS);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, iPS.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ips/:id} : Partial updates given fields of an existing iPS, field will ignore if it is null
     *
     * @param id the id of the iPS to save.
     * @param iPS the iPS to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iPS,
     * or with status {@code 400 (Bad Request)} if the iPS is not valid,
     * or with status {@code 404 (Not Found)} if the iPS is not found,
     * or with status {@code 500 (Internal Server Error)} if the iPS couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ips/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<IPS> partialUpdateIPS(@PathVariable(value = "id", required = false) final Long id, @RequestBody IPS iPS)
        throws URISyntaxException {
        log.debug("REST request to partial update IPS partially : {}, {}", id, iPS);
        if (iPS.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iPS.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!iPSRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IPS> result = iPSService.partialUpdate(iPS);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, iPS.getId().toString())
        );
    }

    /**
     * {@code GET  /ips} : get all the iPS.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of iPS in body.
     */
    @GetMapping("/ips")
    public ResponseEntity<List<IPS>> getAllIPS(IPSCriteria criteria, Pageable pageable) {
        log.debug("REST request to get IPS by criteria: {}", criteria);
        Page<IPS> page = iPSQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ips/count} : count all the iPS.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ips/count")
    public ResponseEntity<Long> countIPS(IPSCriteria criteria) {
        log.debug("REST request to count IPS by criteria: {}", criteria);
        return ResponseEntity.ok().body(iPSQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ips/:id} : get the "id" iPS.
     *
     * @param id the id of the iPS to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the iPS, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ips/{id}")
    public ResponseEntity<IPS> getIPS(@PathVariable Long id) {
        log.debug("REST request to get IPS : {}", id);
        Optional<IPS> iPS = iPSService.findOne(id);
        return ResponseUtil.wrapOrNotFound(iPS);
    }

    /**
     * {@code DELETE  /ips/:id} : delete the "id" iPS.
     *
     * @param id the id of the iPS to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ips/{id}")
    public ResponseEntity<Void> deleteIPS(@PathVariable Long id) {
        log.debug("REST request to delete IPS : {}", id);
        iPSService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

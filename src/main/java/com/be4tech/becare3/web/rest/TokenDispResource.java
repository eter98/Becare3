package com.be4tech.becare3.web.rest;

import com.be4tech.becare3.domain.TokenDisp;
import com.be4tech.becare3.repository.TokenDispRepository;
import com.be4tech.becare3.service.TokenDispQueryService;
import com.be4tech.becare3.service.TokenDispService;
import com.be4tech.becare3.service.criteria.TokenDispCriteria;
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
 * REST controller for managing {@link com.be4tech.becare3.domain.TokenDisp}.
 */
@RestController
@RequestMapping("/api")
public class TokenDispResource {

    private final Logger log = LoggerFactory.getLogger(TokenDispResource.class);

    private static final String ENTITY_NAME = "tokenDisp";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TokenDispService tokenDispService;

    private final TokenDispRepository tokenDispRepository;

    private final TokenDispQueryService tokenDispQueryService;

    public TokenDispResource(
        TokenDispService tokenDispService,
        TokenDispRepository tokenDispRepository,
        TokenDispQueryService tokenDispQueryService
    ) {
        this.tokenDispService = tokenDispService;
        this.tokenDispRepository = tokenDispRepository;
        this.tokenDispQueryService = tokenDispQueryService;
    }

    /**
     * {@code POST  /token-disps} : Create a new tokenDisp.
     *
     * @param tokenDisp the tokenDisp to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tokenDisp, or with status {@code 400 (Bad Request)} if the tokenDisp has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/token-disps")
    public ResponseEntity<TokenDisp> createTokenDisp(@RequestBody TokenDisp tokenDisp) throws URISyntaxException {
        log.debug("REST request to save TokenDisp : {}", tokenDisp);
        if (tokenDisp.getId() != null) {
            throw new BadRequestAlertException("A new tokenDisp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TokenDisp result = tokenDispService.save(tokenDisp);
        return ResponseEntity
            .created(new URI("/api/token-disps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /token-disps/:id} : Updates an existing tokenDisp.
     *
     * @param id the id of the tokenDisp to save.
     * @param tokenDisp the tokenDisp to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tokenDisp,
     * or with status {@code 400 (Bad Request)} if the tokenDisp is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tokenDisp couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/token-disps/{id}")
    public ResponseEntity<TokenDisp> updateTokenDisp(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TokenDisp tokenDisp
    ) throws URISyntaxException {
        log.debug("REST request to update TokenDisp : {}, {}", id, tokenDisp);
        if (tokenDisp.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tokenDisp.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tokenDispRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TokenDisp result = tokenDispService.save(tokenDisp);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tokenDisp.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /token-disps/:id} : Partial updates given fields of an existing tokenDisp, field will ignore if it is null
     *
     * @param id the id of the tokenDisp to save.
     * @param tokenDisp the tokenDisp to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tokenDisp,
     * or with status {@code 400 (Bad Request)} if the tokenDisp is not valid,
     * or with status {@code 404 (Not Found)} if the tokenDisp is not found,
     * or with status {@code 500 (Internal Server Error)} if the tokenDisp couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/token-disps/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TokenDisp> partialUpdateTokenDisp(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TokenDisp tokenDisp
    ) throws URISyntaxException {
        log.debug("REST request to partial update TokenDisp partially : {}, {}", id, tokenDisp);
        if (tokenDisp.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tokenDisp.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tokenDispRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TokenDisp> result = tokenDispService.partialUpdate(tokenDisp);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tokenDisp.getId().toString())
        );
    }

    /**
     * {@code GET  /token-disps} : get all the tokenDisps.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tokenDisps in body.
     */
    @GetMapping("/token-disps")
    public ResponseEntity<List<TokenDisp>> getAllTokenDisps(TokenDispCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TokenDisps by criteria: {}", criteria);
        Page<TokenDisp> page = tokenDispQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /token-disps/count} : count all the tokenDisps.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/token-disps/count")
    public ResponseEntity<Long> countTokenDisps(TokenDispCriteria criteria) {
        log.debug("REST request to count TokenDisps by criteria: {}", criteria);
        return ResponseEntity.ok().body(tokenDispQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /token-disps/:id} : get the "id" tokenDisp.
     *
     * @param id the id of the tokenDisp to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tokenDisp, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/token-disps/{id}")
    public ResponseEntity<TokenDisp> getTokenDisp(@PathVariable Long id) {
        log.debug("REST request to get TokenDisp : {}", id);
        Optional<TokenDisp> tokenDisp = tokenDispService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tokenDisp);
    }

    /**
     * {@code DELETE  /token-disps/:id} : delete the "id" tokenDisp.
     *
     * @param id the id of the tokenDisp to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/token-disps/{id}")
    public ResponseEntity<Void> deleteTokenDisp(@PathVariable Long id) {
        log.debug("REST request to delete TokenDisp : {}", id);
        tokenDispService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

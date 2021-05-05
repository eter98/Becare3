package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.TokenDisp;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TokenDisp}.
 */
public interface TokenDispService {
    /**
     * Save a tokenDisp.
     *
     * @param tokenDisp the entity to save.
     * @return the persisted entity.
     */
    TokenDisp save(TokenDisp tokenDisp);

    /**
     * Partially updates a tokenDisp.
     *
     * @param tokenDisp the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TokenDisp> partialUpdate(TokenDisp tokenDisp);

    /**
     * Get all the tokenDisps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TokenDisp> findAll(Pageable pageable);

    /**
     * Get the "id" tokenDisp.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TokenDisp> findOne(Long id);

    /**
     * Delete the "id" tokenDisp.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

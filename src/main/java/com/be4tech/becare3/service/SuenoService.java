package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.Sueno;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Sueno}.
 */
public interface SuenoService {
    /**
     * Save a sueno.
     *
     * @param sueno the entity to save.
     * @return the persisted entity.
     */
    Sueno save(Sueno sueno);

    /**
     * Partially updates a sueno.
     *
     * @param sueno the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Sueno> partialUpdate(Sueno sueno);

    /**
     * Get all the suenos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Sueno> findAll(Pageable pageable);

    /**
     * Get the "id" sueno.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Sueno> findOne(Long id);

    /**
     * Delete the "id" sueno.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

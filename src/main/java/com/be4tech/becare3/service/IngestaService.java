package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.Ingesta;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Ingesta}.
 */
public interface IngestaService {
    /**
     * Save a ingesta.
     *
     * @param ingesta the entity to save.
     * @return the persisted entity.
     */
    Ingesta save(Ingesta ingesta);

    /**
     * Partially updates a ingesta.
     *
     * @param ingesta the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Ingesta> partialUpdate(Ingesta ingesta);

    /**
     * Get all the ingestas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Ingesta> findAll(Pageable pageable);

    /**
     * Get the "id" ingesta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Ingesta> findOne(Long id);

    /**
     * Delete the "id" ingesta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.Fisiometria1;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Fisiometria1}.
 */
public interface Fisiometria1Service {
    /**
     * Save a fisiometria1.
     *
     * @param fisiometria1 the entity to save.
     * @return the persisted entity.
     */
    Fisiometria1 save(Fisiometria1 fisiometria1);

    /**
     * Partially updates a fisiometria1.
     *
     * @param fisiometria1 the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Fisiometria1> partialUpdate(Fisiometria1 fisiometria1);

    /**
     * Get all the fisiometria1s.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Fisiometria1> findAll(Pageable pageable);

    /**
     * Get the "id" fisiometria1.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Fisiometria1> findOne(Long id);

    /**
     * Delete the "id" fisiometria1.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

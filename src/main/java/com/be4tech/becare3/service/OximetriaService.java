package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.Oximetria;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Oximetria}.
 */
public interface OximetriaService {
    /**
     * Save a oximetria.
     *
     * @param oximetria the entity to save.
     * @return the persisted entity.
     */
    Oximetria save(Oximetria oximetria);

    /**
     * Partially updates a oximetria.
     *
     * @param oximetria the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Oximetria> partialUpdate(Oximetria oximetria);

    /**
     * Get all the oximetrias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Oximetria> findAll(Pageable pageable);

    /**
     * Get the "id" oximetria.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Oximetria> findOne(Long id);

    /**
     * Delete the "id" oximetria.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

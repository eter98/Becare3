package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.Pasos;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Pasos}.
 */
public interface PasosService {
    /**
     * Save a pasos.
     *
     * @param pasos the entity to save.
     * @return the persisted entity.
     */
    Pasos save(Pasos pasos);

    /**
     * Partially updates a pasos.
     *
     * @param pasos the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Pasos> partialUpdate(Pasos pasos);

    /**
     * Get all the pasos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Pasos> findAll(Pageable pageable);

    /**
     * Get the "id" pasos.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Pasos> findOne(Long id);

    /**
     * Delete the "id" pasos.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

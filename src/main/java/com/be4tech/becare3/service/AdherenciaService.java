package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.Adherencia;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Adherencia}.
 */
public interface AdherenciaService {
    /**
     * Save a adherencia.
     *
     * @param adherencia the entity to save.
     * @return the persisted entity.
     */
    Adherencia save(Adherencia adherencia);

    /**
     * Partially updates a adherencia.
     *
     * @param adherencia the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Adherencia> partialUpdate(Adherencia adherencia);

    /**
     * Get all the adherencias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Adherencia> findAll(Pageable pageable);

    /**
     * Get the "id" adherencia.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Adherencia> findOne(Long id);

    /**
     * Delete the "id" adherencia.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

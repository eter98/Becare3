package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.Alarma;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Alarma}.
 */
public interface AlarmaService {
    /**
     * Save a alarma.
     *
     * @param alarma the entity to save.
     * @return the persisted entity.
     */
    Alarma save(Alarma alarma);

    /**
     * Partially updates a alarma.
     *
     * @param alarma the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Alarma> partialUpdate(Alarma alarma);

    /**
     * Get all the alarmas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Alarma> findAll(Pageable pageable);

    /**
     * Get the "id" alarma.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Alarma> findOne(Long id);

    /**
     * Delete the "id" alarma.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

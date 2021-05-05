package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.Programa;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Programa}.
 */
public interface ProgramaService {
    /**
     * Save a programa.
     *
     * @param programa the entity to save.
     * @return the persisted entity.
     */
    Programa save(Programa programa);

    /**
     * Partially updates a programa.
     *
     * @param programa the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Programa> partialUpdate(Programa programa);

    /**
     * Get all the programas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Programa> findAll(Pageable pageable);

    /**
     * Get the "id" programa.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Programa> findOne(Long id);

    /**
     * Delete the "id" programa.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

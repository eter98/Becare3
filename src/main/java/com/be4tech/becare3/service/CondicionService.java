package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.Condicion;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Condicion}.
 */
public interface CondicionService {
    /**
     * Save a condicion.
     *
     * @param condicion the entity to save.
     * @return the persisted entity.
     */
    Condicion save(Condicion condicion);

    /**
     * Partially updates a condicion.
     *
     * @param condicion the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Condicion> partialUpdate(Condicion condicion);

    /**
     * Get all the condicions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Condicion> findAll(Pageable pageable);

    /**
     * Get the "id" condicion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Condicion> findOne(Long id);

    /**
     * Delete the "id" condicion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

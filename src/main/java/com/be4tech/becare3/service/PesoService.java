package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.Peso;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Peso}.
 */
public interface PesoService {
    /**
     * Save a peso.
     *
     * @param peso the entity to save.
     * @return the persisted entity.
     */
    Peso save(Peso peso);

    /**
     * Partially updates a peso.
     *
     * @param peso the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Peso> partialUpdate(Peso peso);

    /**
     * Get all the pesos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Peso> findAll(Pageable pageable);

    /**
     * Get the "id" peso.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Peso> findOne(Long id);

    /**
     * Delete the "id" peso.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

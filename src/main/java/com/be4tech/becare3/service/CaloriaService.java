package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.Caloria;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Caloria}.
 */
public interface CaloriaService {
    /**
     * Save a caloria.
     *
     * @param caloria the entity to save.
     * @return the persisted entity.
     */
    Caloria save(Caloria caloria);

    /**
     * Partially updates a caloria.
     *
     * @param caloria the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Caloria> partialUpdate(Caloria caloria);

    /**
     * Get all the calorias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Caloria> findAll(Pageable pageable);

    /**
     * Get the "id" caloria.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Caloria> findOne(Long id);

    /**
     * Delete the "id" caloria.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

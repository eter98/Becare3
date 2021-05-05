package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.Temperatura;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Temperatura}.
 */
public interface TemperaturaService {
    /**
     * Save a temperatura.
     *
     * @param temperatura the entity to save.
     * @return the persisted entity.
     */
    Temperatura save(Temperatura temperatura);

    /**
     * Partially updates a temperatura.
     *
     * @param temperatura the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Temperatura> partialUpdate(Temperatura temperatura);

    /**
     * Get all the temperaturas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Temperatura> findAll(Pageable pageable);

    /**
     * Get the "id" temperatura.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Temperatura> findOne(Long id);

    /**
     * Delete the "id" temperatura.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

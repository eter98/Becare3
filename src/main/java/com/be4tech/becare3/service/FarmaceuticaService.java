package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.Farmaceutica;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Farmaceutica}.
 */
public interface FarmaceuticaService {
    /**
     * Save a farmaceutica.
     *
     * @param farmaceutica the entity to save.
     * @return the persisted entity.
     */
    Farmaceutica save(Farmaceutica farmaceutica);

    /**
     * Partially updates a farmaceutica.
     *
     * @param farmaceutica the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Farmaceutica> partialUpdate(Farmaceutica farmaceutica);

    /**
     * Get all the farmaceuticas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Farmaceutica> findAll(Pageable pageable);

    /**
     * Get the "id" farmaceutica.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Farmaceutica> findOne(Long id);

    /**
     * Delete the "id" farmaceutica.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

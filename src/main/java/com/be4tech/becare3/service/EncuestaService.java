package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.Encuesta;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Encuesta}.
 */
public interface EncuestaService {
    /**
     * Save a encuesta.
     *
     * @param encuesta the entity to save.
     * @return the persisted entity.
     */
    Encuesta save(Encuesta encuesta);

    /**
     * Partially updates a encuesta.
     *
     * @param encuesta the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Encuesta> partialUpdate(Encuesta encuesta);

    /**
     * Get all the encuestas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Encuesta> findAll(Pageable pageable);

    /**
     * Get the "id" encuesta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Encuesta> findOne(Long id);

    /**
     * Delete the "id" encuesta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.Pregunta;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Pregunta}.
 */
public interface PreguntaService {
    /**
     * Save a pregunta.
     *
     * @param pregunta the entity to save.
     * @return the persisted entity.
     */
    Pregunta save(Pregunta pregunta);

    /**
     * Partially updates a pregunta.
     *
     * @param pregunta the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Pregunta> partialUpdate(Pregunta pregunta);

    /**
     * Get all the preguntas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Pregunta> findAll(Pageable pageable);

    /**
     * Get the "id" pregunta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Pregunta> findOne(Long id);

    /**
     * Delete the "id" pregunta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

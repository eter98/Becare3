package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.Notificacion;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Notificacion}.
 */
public interface NotificacionService {
    /**
     * Save a notificacion.
     *
     * @param notificacion the entity to save.
     * @return the persisted entity.
     */
    Notificacion save(Notificacion notificacion);

    /**
     * Partially updates a notificacion.
     *
     * @param notificacion the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Notificacion> partialUpdate(Notificacion notificacion);

    /**
     * Get all the notificacions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Notificacion> findAll(Pageable pageable);

    /**
     * Get the "id" notificacion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Notificacion> findOne(Long id);

    /**
     * Delete the "id" notificacion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

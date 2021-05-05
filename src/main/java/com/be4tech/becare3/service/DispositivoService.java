package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.Dispositivo;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Dispositivo}.
 */
public interface DispositivoService {
    /**
     * Save a dispositivo.
     *
     * @param dispositivo the entity to save.
     * @return the persisted entity.
     */
    Dispositivo save(Dispositivo dispositivo);

    /**
     * Partially updates a dispositivo.
     *
     * @param dispositivo the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Dispositivo> partialUpdate(Dispositivo dispositivo);

    /**
     * Get all the dispositivos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Dispositivo> findAll(Pageable pageable);

    /**
     * Get the "id" dispositivo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Dispositivo> findOne(Long id);

    /**
     * Delete the "id" dispositivo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

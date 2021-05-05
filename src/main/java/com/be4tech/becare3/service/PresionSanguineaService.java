package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.PresionSanguinea;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link PresionSanguinea}.
 */
public interface PresionSanguineaService {
    /**
     * Save a presionSanguinea.
     *
     * @param presionSanguinea the entity to save.
     * @return the persisted entity.
     */
    PresionSanguinea save(PresionSanguinea presionSanguinea);

    /**
     * Partially updates a presionSanguinea.
     *
     * @param presionSanguinea the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PresionSanguinea> partialUpdate(PresionSanguinea presionSanguinea);

    /**
     * Get all the presionSanguineas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PresionSanguinea> findAll(Pageable pageable);

    /**
     * Get the "id" presionSanguinea.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PresionSanguinea> findOne(Long id);

    /**
     * Delete the "id" presionSanguinea.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

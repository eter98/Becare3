package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.IPS;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link IPS}.
 */
public interface IPSService {
    /**
     * Save a iPS.
     *
     * @param iPS the entity to save.
     * @return the persisted entity.
     */
    IPS save(IPS iPS);

    /**
     * Partially updates a iPS.
     *
     * @param iPS the entity to update partially.
     * @return the persisted entity.
     */
    Optional<IPS> partialUpdate(IPS iPS);

    /**
     * Get all the iPS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<IPS> findAll(Pageable pageable);

    /**
     * Get the "id" iPS.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<IPS> findOne(Long id);

    /**
     * Delete the "id" iPS.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

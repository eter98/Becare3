package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.Tratamieto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Tratamieto}.
 */
public interface TratamietoService {
    /**
     * Save a tratamieto.
     *
     * @param tratamieto the entity to save.
     * @return the persisted entity.
     */
    Tratamieto save(Tratamieto tratamieto);

    /**
     * Partially updates a tratamieto.
     *
     * @param tratamieto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Tratamieto> partialUpdate(Tratamieto tratamieto);

    /**
     * Get all the tratamietos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Tratamieto> findAll(Pageable pageable);

    /**
     * Get the "id" tratamieto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Tratamieto> findOne(Long id);

    /**
     * Delete the "id" tratamieto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

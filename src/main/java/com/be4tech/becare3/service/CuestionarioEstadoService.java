package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.CuestionarioEstado;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link CuestionarioEstado}.
 */
public interface CuestionarioEstadoService {
    /**
     * Save a cuestionarioEstado.
     *
     * @param cuestionarioEstado the entity to save.
     * @return the persisted entity.
     */
    CuestionarioEstado save(CuestionarioEstado cuestionarioEstado);

    /**
     * Partially updates a cuestionarioEstado.
     *
     * @param cuestionarioEstado the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CuestionarioEstado> partialUpdate(CuestionarioEstado cuestionarioEstado);

    /**
     * Get all the cuestionarioEstados.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CuestionarioEstado> findAll(Pageable pageable);

    /**
     * Get the "id" cuestionarioEstado.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CuestionarioEstado> findOne(Long id);

    /**
     * Delete the "id" cuestionarioEstado.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

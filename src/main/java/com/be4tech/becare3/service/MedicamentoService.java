package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.Medicamento;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Medicamento}.
 */
public interface MedicamentoService {
    /**
     * Save a medicamento.
     *
     * @param medicamento the entity to save.
     * @return the persisted entity.
     */
    Medicamento save(Medicamento medicamento);

    /**
     * Partially updates a medicamento.
     *
     * @param medicamento the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Medicamento> partialUpdate(Medicamento medicamento);

    /**
     * Get all the medicamentos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Medicamento> findAll(Pageable pageable);

    /**
     * Get the "id" medicamento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Medicamento> findOne(Long id);

    /**
     * Delete the "id" medicamento.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

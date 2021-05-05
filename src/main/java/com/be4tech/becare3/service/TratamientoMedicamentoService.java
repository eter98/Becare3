package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.TratamientoMedicamento;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TratamientoMedicamento}.
 */
public interface TratamientoMedicamentoService {
    /**
     * Save a tratamientoMedicamento.
     *
     * @param tratamientoMedicamento the entity to save.
     * @return the persisted entity.
     */
    TratamientoMedicamento save(TratamientoMedicamento tratamientoMedicamento);

    /**
     * Partially updates a tratamientoMedicamento.
     *
     * @param tratamientoMedicamento the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TratamientoMedicamento> partialUpdate(TratamientoMedicamento tratamientoMedicamento);

    /**
     * Get all the tratamientoMedicamentos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TratamientoMedicamento> findAll(Pageable pageable);

    /**
     * Get the "id" tratamientoMedicamento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TratamientoMedicamento> findOne(Long id);

    /**
     * Delete the "id" tratamientoMedicamento.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

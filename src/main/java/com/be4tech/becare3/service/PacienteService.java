package com.be4tech.becare3.service;

import com.be4tech.becare3.domain.Paciente;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Paciente}.
 */
public interface PacienteService {
    /**
     * Save a paciente.
     *
     * @param paciente the entity to save.
     * @return the persisted entity.
     */
    Paciente save(Paciente paciente);

    /**
     * Partially updates a paciente.
     *
     * @param paciente the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Paciente> partialUpdate(Paciente paciente);

    /**
     * Get all the pacientes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Paciente> findAll(Pageable pageable);

    /**
     * Get the "id" paciente.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Paciente> findOne(Long id);

    /**
     * Delete the "id" paciente.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

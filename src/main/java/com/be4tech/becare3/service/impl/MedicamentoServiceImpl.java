package com.be4tech.becare3.service.impl;

import com.be4tech.becare3.domain.Medicamento;
import com.be4tech.becare3.repository.MedicamentoRepository;
import com.be4tech.becare3.service.MedicamentoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Medicamento}.
 */
@Service
@Transactional
public class MedicamentoServiceImpl implements MedicamentoService {

    private final Logger log = LoggerFactory.getLogger(MedicamentoServiceImpl.class);

    private final MedicamentoRepository medicamentoRepository;

    public MedicamentoServiceImpl(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    @Override
    public Medicamento save(Medicamento medicamento) {
        log.debug("Request to save Medicamento : {}", medicamento);
        return medicamentoRepository.save(medicamento);
    }

    @Override
    public Optional<Medicamento> partialUpdate(Medicamento medicamento) {
        log.debug("Request to partially update Medicamento : {}", medicamento);

        return medicamentoRepository
            .findById(medicamento.getId())
            .map(
                existingMedicamento -> {
                    if (medicamento.getNombre() != null) {
                        existingMedicamento.setNombre(medicamento.getNombre());
                    }
                    if (medicamento.getDescripcion() != null) {
                        existingMedicamento.setDescripcion(medicamento.getDescripcion());
                    }
                    if (medicamento.getFechaIngreso() != null) {
                        existingMedicamento.setFechaIngreso(medicamento.getFechaIngreso());
                    }
                    if (medicamento.getPresentacion() != null) {
                        existingMedicamento.setPresentacion(medicamento.getPresentacion());
                    }
                    if (medicamento.getGenerico() != null) {
                        existingMedicamento.setGenerico(medicamento.getGenerico());
                    }

                    return existingMedicamento;
                }
            )
            .map(medicamentoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Medicamento> findAll(Pageable pageable) {
        log.debug("Request to get all Medicamentos");
        return medicamentoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Medicamento> findOne(Long id) {
        log.debug("Request to get Medicamento : {}", id);
        return medicamentoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Medicamento : {}", id);
        medicamentoRepository.deleteById(id);
    }
}
